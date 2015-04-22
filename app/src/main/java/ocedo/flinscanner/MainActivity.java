package ocedo.flinscanner;

import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import ocedo.flinscanner.lists.HostListItemAdapter;
import ocedo.flinscanner.tasks.ScanNetworkTask;


public class MainActivity extends ActionBarActivity {

    private ListView hostListView;

    private HostListItemAdapter hostListAdapter;

    private PtrFrameLayout pullToRefreshContainer;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hostListView = (ListView) findViewById(R.id.hostListView);

        LinearLayout progressBarLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.progressbar, null, false);
        hostListView.addHeaderView(progressBarLayout);

        progressBar = (ProgressBar) progressBarLayout.findViewById(R.id.progressBar);

        hostListAdapter = new HostListItemAdapter(getApplicationContext());
        hostListView.setAdapter(hostListAdapter);

        pullToRefreshContainer = (PtrFrameLayout) findViewById(R.id.pullToRefreshContainer);
        pullToRefreshContainer.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(ptrFrameLayout, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                startScanConcurrently();
            }
        });
    }

    private void getGateWay(WifiManager manager) {
        try {
            for(Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface networkInterface = en.nextElement();
                String name = networkInterface.getName();
                String ip = "";
                String gateWayIP = "";
                String netmaskIP = "";

                for(Enumeration<InetAddress> addresses = networkInterface.getInetAddresses(); addresses.hasMoreElements(); ) {
                    InetAddress address = addresses.nextElement();
                    if(!address.isLoopbackAddress()) {
                        if(address instanceof Inet6Address) {
                            continue;
                        }

                        ip = address.getHostAddress();
                    }
                }

                if(manager != null) {
                    DhcpInfo info = manager.getDhcpInfo();

                    gateWayIP = String.format(
                            "%d.%d.%d.%d",
                            (info.gateway & 0xff),
                            (info.gateway >> 8 & 0xff),
                            (info.gateway >> 16 & 0xff),
                            (info.gateway >> 24 & 0xff));

                    netmaskIP = String.format(
                            "%d.%d.%d.%d",
                            (info.netmask & 0xff),
                            (info.netmask >> 8 & 0xff),
                            (info.netmask >> 16 & 0xff),
                            (info.netmask >> 24 & 0xff));
                }

                Log.d("TAG", "Name: " + name + ", IP: " + ip + ", Gateway: " + gateWayIP + ", Subnet: " + netmaskIP);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private String integerIpToString(int ip) {
       return String.format(
                "%d.%d.%d.%d",
                (ip & 0xff),
                (ip >> 8 & 0xff),
                (ip >> 16 & 0xff),
                (ip >> 24 & 0xff));
    }

    private void startScanConcurrently() {
        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        if(manager != null) {
            hostListAdapter.clear();

            DhcpInfo dhcpInfo = manager.getDhcpInfo();
            String gateway = integerIpToString(dhcpInfo.gateway);

            int last = gateway.lastIndexOf(".");
            String maskedGateway = gateway.substring(0, last+1);

            Log.d("TAG", "Searching in " + maskedGateway);

            ScanNetworkTask task = new ScanNetworkTask(getApplicationContext(), hostListAdapter, pullToRefreshContainer, progressBar);
            task.execute(maskedGateway);

        } else {
            Toast.makeText(getApplicationContext(), "Could not start scan", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
