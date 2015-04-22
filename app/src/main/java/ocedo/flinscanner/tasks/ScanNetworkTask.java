package ocedo.flinscanner.tasks;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import in.srain.cube.views.ptr.PtrFrameLayout;
import ocedo.flinscanner.lists.HostListItem;
import ocedo.flinscanner.lists.HostListItemAdapter;

/**
 * Created by stefan on 21.04.15.
 */
public class ScanNetworkTask extends AsyncTask<Integer, Integer, HostListItem> {

    private HostListItemAdapter adapter;

    private PtrFrameLayout pullToRefreshContainer;

    private ProgressBar progressBar;

    private HashMap<String, String> macAddresses;

    public ScanNetworkTask(HostListItemAdapter adapter,
                           PtrFrameLayout pullToRefreshContainer,
                           ProgressBar progressBar) {
        this.adapter = adapter;
        this.pullToRefreshContainer = pullToRefreshContainer;
        this.progressBar = progressBar;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        progressBar.setProgress(values[0]);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(HostListItem hostListItem) {
        super.onPostExecute(hostListItem);

        progressBar.setVisibility(View.GONE);
        pullToRefreshContainer.refreshComplete();
    }

    @Override
    protected HostListItem doInBackground(Integer... params) {
        Log.d("TAG", "STargin");

        macAddresses = retrieveMacAdressList();

        Log.d("TAG", "DONEFDJK");

        Iterator it = macAddresses.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Log.d("TAG", pair.getKey() + " : " + pair.getValue());
        }

        Log.d("TAG", "Danke");

        String subnet = "192.168.2.";
        resetHostListItemAdapter();

        for (int i = 0; i < 256; i++) {
            publishProgress(i);

            if (isCancelled()) {
                break;
            }

            try {
                InetAddress addr = InetAddress.getByName(subnet + String.valueOf(i));
                if (addr.isReachable(250)) {
                    addHostListItem(addr);
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return null;
    }

    private void addHostListItem(final InetAddress address) {
        final HostListItem item = new HostListItem(address.getHostName(), address.getHostAddress());

        if (macAddresses != null) {
            if (macAddresses.containsKey(address.getHostAddress())) {
                item.setHardwareAddress(macAddresses.get(address.getHostAddress()));
            } else {
                String macAddress = getMacAddressFromIP(address.getHostAddress());
                if(!macAddress.equals("")) {
                    item.setHardwareAddress(macAddress);
                }
            }
        }

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (adapter != null) {
                    adapter.addItem(item);
                }
            }
        });
    }

    private void resetHostListItemAdapter() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (adapter != null) {
                    adapter.clear();
                }
            }
        });
    }

    private HashMap<String, String> retrieveMacAdressList() {
        macAddresses = new HashMap<>();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("/proc/net/arp"));
            String line;
            while ((line = br.readLine()) != null) {
                Log.d("TAG", line);

                String[] splitted = line.split(" +");
                if (splitted != null && splitted.length >= 4) {
                    // Basic sanity check
                    String mac = splitted[3];
                    if (mac.matches("..:..:..:..:..:..")) {
                        macAddresses.put(splitted[0], mac);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return macAddresses;
    }

    private String getMacAddressFromIP(String hostAddress) {
        try {
            NetworkInterface network = NetworkInterface.getByInetAddress(InetAddress.getByName(hostAddress));

            if(network != null) {
                byte[] mac = network.getHardwareAddress();

                StringBuilder builder = new StringBuilder();

                for(int i = 0; i < mac.length; i ++) {
                    builder.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));
                }

                return builder.toString();
            }

            return "";

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
