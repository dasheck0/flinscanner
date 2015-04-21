package ocedo.flinscanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stefan on 21.04.15.
 */
public class HostListItemAdapter extends BaseAdapter {

    Context context;
    List<HostListItem> items;

    private HostListItemAdapter() {
    }

    public HostListItemAdapter(Context context) {
        this.context = context;
        this.items = new ArrayList<>();
    }

    public HostListItemAdapter(Context context, List<HostListItem> items) {
        this.context = context;

        if(items == null) {
            this.items = new ArrayList<>();
        } else {
            this.items = items;
        }
    }

    public void setItems(List<HostListItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void addItem(HostListItem item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void removeItem(HostListItem item) {
        items.remove(item);
        notifyDataSetChanged();
    }

    public void removeItem(int i) {
        items.remove(i);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public HostListItem getItem(int i) {
        if(i >= 0 && i < items.size()) {
            return items.get(i);
        }

        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HostListItem item = getItem(i);

        LinearLayout mainLayout = null;

        if(view == null) {
            mainLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.host_listview_item, null, false);
        } else {
            mainLayout = (LinearLayout) view;
        }

        TextView hostNameTextView = (TextView) mainLayout.findViewById(R.id.hostItemNameTextView);
        TextView hostAddressTextView = (TextView) mainLayout.findViewById(R.id.hostItemIPTextView);
        TextView macAddressTextView = (TextView) mainLayout.findViewById(R.id.hostItemMACTextView);

        hostNameTextView.setText(item.getHostName());
        hostAddressTextView.setText(item.getHostAddress());

        StringBuilder macAddressStringbuilder = new StringBuilder();
        macAddressStringbuilder.append(item.getHardwareAddress());
        if(!item.getVendor().equals("")) {
            macAddressStringbuilder.append("( " + item.getVendor() + " )");
        }

        macAddressTextView.setText(macAddressStringbuilder.toString());

        return mainLayout;
    }
}
