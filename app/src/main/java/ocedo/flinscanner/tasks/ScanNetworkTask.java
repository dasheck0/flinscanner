package ocedo.flinscanner.tasks;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

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

    private int index;

    public ScanNetworkTask(HostListItemAdapter adapter,
                           PtrFrameLayout pullToRefreshContainer,
                           ProgressBar progressBar) {
        this.adapter = adapter;
        this.pullToRefreshContainer = pullToRefreshContainer;
        this.progressBar = progressBar;

        this.index = -1;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        progressBar.incrementProgressBy(1);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(HostListItem hostListItem) {
        super.onPostExecute(hostListItem);

        if(index >= 255) {
            progressBar.setVisibility(View.GONE);
            pullToRefreshContainer.refreshComplete();
        }
    }

    @Override
    protected HostListItem doInBackground(Integer... params) {
        /*macAddresses = retrieveMacAdressList();

        Iterator it = macAddresses.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Log.d("TAG", pair.getKey() + " : " + pair.getValue());
        }*/

        int startAddress = params[0];
        int endAddress = params[1];


        String subnet = "192.168.2.";
       // resetHostListItemAdapter();

        for (int i = startAddress; i < endAddress; i++) {
            index = i;
            publishProgress(i);

            if (isCancelled()) {
                break;
            }

            try {
                InetAddress addr = InetAddress.getByName(subnet + String.valueOf(i));

                //Log.d("TAG", addr.getHostAddress() + ", " + addr.getCanonicalHostName() + ", " + addr.getHostName());

                if (addr.getHostName() != null && !addr.getHostName().equals(addr.getHostAddress())) {
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

        if(address.getHostName().startsWith("android")) {
            item.setType(HostListItem.HOST_ITEM_TYPE.MOBILE);
        } else {
            item.setType(HostListItem.HOST_ITEM_TYPE.DESKTOP);
        }

        item.setHardwareAddress(RetrieveMacAddressListTask.getHardwareAddressFromIP(item.getHostAddress()));

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (adapter != null) {
                    adapter.addItem(item);
                }
            }
        });
    }
}
