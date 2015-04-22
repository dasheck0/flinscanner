package ocedo.flinscanner;

import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import ocedo.flinscanner.lists.HostListItem;
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

    private void startScanConcurrently() {
        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        if(manager != null) {
            hostListAdapter.clear();

            DhcpInfo dhcpInfo = manager.getDhcpInfo();

            HostListItem item = new HostListItem("Router", Formatter.formatIpAddress(dhcpInfo.gateway));
            item.setType(HostListItem.HOST_ITEM_TYPE.UNKNOWN);

            hostListAdapter.addItem(item);

            ScanNetworkTask task = new ScanNetworkTask(hostListAdapter, pullToRefreshContainer, progressBar);
            task.execute(0, 256);

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
