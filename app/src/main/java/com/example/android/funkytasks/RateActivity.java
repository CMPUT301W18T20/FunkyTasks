package com.example.android.funkytasks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;


public class RateActivity extends AppCompatActivity {

    String uname;
    User solver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_rate);

        // Retrieving the username as string
        Intent intent = getIntent();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uname = extras.getString("username");
        }

        // initiate rating bar and a button
        final RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        Button rateButton = (Button) findViewById(R.id.rateButton);
        // perform click event on button

        /**
         * Onclick to set off elasticsearch functions and rating changes.
         */
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float newrating = ratingBar.getRating();

                // Getting the solver user
                ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
                getUser.execute(uname);
                try{
                    solver = getUser.get();
                    Log.e("Got the solver: ", solver.getUsername());
                }catch (Exception e){
                    Log.e("Error", "We aren't getting the solver");
                    return;
                }

                //float rating_double = Float.parseFloat(rating);

                solver.addRatingToList(newrating);
                solver.setRating();

                // Updating the elasticsearch user
                ElasticSearchController.updateUser updateUser = new ElasticSearchController.updateUser();
                updateUser.execute(solver);

                finish();
            }
        });

    }
}
