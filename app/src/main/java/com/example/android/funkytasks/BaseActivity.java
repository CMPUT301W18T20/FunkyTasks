package com.example.android.funkytasks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {

    private String username;

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        username = LoginActivity.username;
        this.registerReceiver(this.mConnReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
            boolean isFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);

            NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            NetworkInfo otherNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

            if(currentNetworkInfo.isConnected()){
                OfflineController controller = new OfflineController(getApplicationContext(), username);
                ArrayList<Task> tasks = controller.loadFromFile();

                for (Task eachtask : tasks) {
                    Log.d("task title base", eachtask.getTitle());
                    Log.d("task description base", eachtask.getDescription());
                    if (eachtask.getId() == null) {
                        ElasticSearchController.PostTask postTask = new ElasticSearchController.PostTask();
                        postTask.execute(eachtask);
                        try {
                            Task t = postTask.get();
                            while (t == null) {
                                ElasticSearchController.PostTask postTaskAgain = new ElasticSearchController.PostTask();
                                postTaskAgain.execute(eachtask);
                                t = postTaskAgain.get();
                            }

                        } catch (Exception e) {
                            Log.e("Error", "We are not able to sync task to the online database");
                            return;

                        }
                        controller.deleteFromArrayList();
                    } else {
                        Log.d("task ID base", eachtask.getId());
                        ElasticSearchController.updateTask updateTask = new ElasticSearchController.updateTask();
                        updateTask.execute(eachtask);
                        controller.deleteFromArrayList();
                    }
                }

            }
        }
    };

    @Override
    protected void onResume() {
        registerReceiver(mConnReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mConnReceiver);
        super.onPause();
    }

}
