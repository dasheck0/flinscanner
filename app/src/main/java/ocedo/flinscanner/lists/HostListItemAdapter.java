package ocedo.flinscanner.lists;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ocedo.flinscanner.R;
import ocedo.flinscanner.tasks.ICMPPingTask;

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

    public void clear() {
        items.clear();
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
        final HostListItem item = getItem(i);

        LinearLayout mainLayout = null;

        if(view == null) {
            mainLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.host_listview_item, null, false);
        } else {
            mainLayout = (LinearLayout) view;
        }

        mainLayout.setBackgroundColor(Color.parseColor(ICMPPingTask.DEFAULT_COLOR));

        TextView hostNameTextView = (TextView) mainLayout.findViewById(R.id.hostItemNameTextView);
        TextView hostAddressTextView = (TextView) mainLayout.findViewById(R.id.hostItemIPTextView);
        TextView macAddressTextView = (TextView) mainLayout.findViewById(R.id.hostItemMACTextView);
        TextView vendorTextView = (TextView) mainLayout.findViewById(R.id.hostItemVendorTextView);
        ImageButton typeImageButton = (ImageButton) mainLayout.findViewById(R.id.hostItemTypeImageButton);

        final LinearLayout finalMainLayout = mainLayout;

        typeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ICMPPingTask task = new ICMPPingTask(context, item, finalMainLayout);
                task.execute(item.getHostAddress());
            }
        });

        hostNameTextView.setText(item.getHostName());
        hostAddressTextView.setText(item.getHostAddress());
        macAddressTextView.setText(item.getHardwareAddress());
        vendorTextView.setText(item.getVendor());
        typeImageButton.setImageResource(item.getDrawableIDForType());

        return mainLayout;
    }
}
