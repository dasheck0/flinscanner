package ocedo.flinscanner;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

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
       // progressBar.setVisibility(View.GONE);

        hostListAdapter = new HostListItemAdapter(getApplicationContext());
        hostListView.setAdapter(hostListAdapter);

        HostListItem item = new HostListItem("Localhost", "127.0.0.1");
        hostListAdapter.addItem(item);

        pullToRefreshContainer = (PtrFrameLayout) findViewById(R.id.pullToRefreshContainer);

        pullToRefreshContainer.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(ptrFrameLayout, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                ScanNetworkTask task = new ScanNetworkTask(hostListAdapter, pullToRefreshContainer, progressBar);
                task.execute(0);
            }
        });
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
