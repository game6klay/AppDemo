package com.example.patja2r.test1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by patja2r on 6/9/2016.
 */
public class SubmitSurveyActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_survey);

        Button btSubmitSurvey = (Button) findViewById(R.id.btSubmitSurvey);
        btSubmitSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send the survey answers back to the server

                android.os.Process.killProcess(android.os.Process.myPid());
                //finish();
                System.exit(1);
            }
        });
    }
}
