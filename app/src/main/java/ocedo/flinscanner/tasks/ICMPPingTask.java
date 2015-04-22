package ocedo.flinscanner.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import ocedo.flinscanner.lists.HostListItem;

/**
 * Created by stefan on 22.04.15.
 */
public class ICMPPingTask extends AsyncTask<String, Void, Boolean> {

    private static final String SUCCESS_COLOR = "#c1ffc1";

    private static final String FAILURE_COLOR = "#d93a32";

    public static final String DEFAULT_COLOR = "#ffffff";

    private static final int TIMEOUT = 3600;

    private HostListItem item;

    private LinearLayout mainLayout;

    private ProgressDialog dialog;

    private Context context;

    public ICMPPingTask(Context context, HostListItem item, LinearLayout mainLayout) {
        this.context = context;
        this.item = item;
        this.mainLayout = mainLayout;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        dialog = new ProgressDialog(context);
        dialog.setMessage("ICMP pinging " + item.getHostAddress());
        dialog.show();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        dialog.dismiss();

        if(item.getState() == HostListItem.LIFE_STATE.A_LIVE) {
            mainLayout.setBackgroundColor(Color.parseColor(SUCCESS_COLOR));
        } else if(item.getState() == HostListItem.LIFE_STATE.DEAD) {
            mainLayout.setBackgroundColor(Color.parseColor(FAILURE_COLOR));
            Toast.makeText(context, "Ping timeout for " + item.getHostAddress(), Toast.LENGTH_SHORT).show();
        } else {
            mainLayout.setBackgroundColor(Color.parseColor(DEFAULT_COLOR));
        }
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String ip = strings[0];

        try {
            InetAddress address = InetAddress.getByName(ip);
            boolean state = address.isReachable(TIMEOUT);

            if(state) {
                item.setState(HostListItem.LIFE_STATE.A_LIVE);
            } else {
                item.setState(HostListItem.LIFE_STATE.DEAD);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }
}
