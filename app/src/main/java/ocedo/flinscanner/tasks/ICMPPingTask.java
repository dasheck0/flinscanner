package ocedo.flinscanner.tasks;

import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.LinearLayout;

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

    public ICMPPingTask(HostListItem item, LinearLayout mainLayout) {
        this.item = item;
        this.mainLayout = mainLayout;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        if(item.getState() == HostListItem.LIFE_STATE.A_LIVE) {
            mainLayout.setBackgroundColor(Color.parseColor(SUCCESS_COLOR));
        } else if(item.getState() == HostListItem.LIFE_STATE.DEAD) {
            mainLayout.setBackgroundColor(Color.parseColor(FAILURE_COLOR));
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
