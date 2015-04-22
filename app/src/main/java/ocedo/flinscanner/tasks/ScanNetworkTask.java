package ocedo.flinscanner.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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
public class ScanNetworkTask extends AsyncTask<String, Integer, HostListItem> {

    private HostListItemAdapter adapter;

    private PtrFrameLayout pullToRefreshContainer;

    private ProgressBar progressBar;

    private HashMap<String, String> macAddresses;

    private Context context;

    public ScanNetworkTask(Context context,
                           HostListItemAdapter adapter,
                           PtrFrameLayout pullToRefreshContainer,
                           ProgressBar progressBar) {
        this.context = context;
        this.adapter = adapter;
        this.pullToRefreshContainer = pullToRefreshContainer;
        this.progressBar = progressBar;
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

        progressBar.setVisibility(View.GONE);
        pullToRefreshContainer.refreshComplete();

        if(adapter.getCount() == 0) {
            Toast.makeText(context, "The search is complete, but no hosts were found.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "The search is complete.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected HostListItem doInBackground(String... params) {
        String subnet = params[0];

        for (int i = 0; i < 255; i++) {
            publishProgress(i);

            if (isCancelled()) {
                break;
            }

            try {

                InetAddress addr = InetAddress.getByName(subnet + String.valueOf(i));

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
        final HostListItem item = new HostListItem(adapter, address.getHostName(), address.getHostAddress());

        if (address.getHostName().startsWith("android")) {
            item.setType(HostListItem.HOST_ITEM_TYPE.MOBILE);
        } else {
            item.setType(HostListItem.HOST_ITEM_TYPE.DESKTOP);
        }

        item.setHardwareAddress(RetrieveMacAddressListTask.getHardwareAddressFromIP(item.getHostAddress()));
        if(item.getHardwareAddress() != RetrieveMacAddressListTask.DEFAULT_MAC) {
            RetrieveMacAddressListTask.getVendorFromMacAddress(item);
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
}
