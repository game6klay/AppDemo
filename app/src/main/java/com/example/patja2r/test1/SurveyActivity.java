package com.example.patja2r.test1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


/**
 * Created by patja2r on 5/26/2016.
 */
public class SurveyActivity extends FragmentActivity {
    private ViewPager viewPager;
    private View view;
    private int numberOfItems = 1;
    private int limit=11;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_activity);

        //setting up the swipe adapter

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
        viewPager.setAdapter(swipeAdapter);

        view = (View) findViewById((R.id.view1));

        view.setOnTouchListener(new OnSwipeTouchListener(SurveyActivity.this){

            public void onSwipeRight() {
                Toast.makeText(SurveyActivity.this, "YES", Toast.LENGTH_SHORT).show();
                //answer.setText("Your Previous Answer : Yes");
                if (numberOfItems == limit){
                    startActivity(new Intent(SurveyActivity.this,SubmitSurveyActivity.class));
                }
                else {

                    viewPager.setCurrentItem(numberOfItems++);

                }
                //questions are pulled of the array of string received from the server
                changeQuestionOnSwipe();

                //check if the limit of total questions is reached it will redirect to submit the survey
                checkLimitOfSurvey();

            }
            public void onSwipeLeft() {
                Toast.makeText(SurveyActivity.this, "NO", Toast.LENGTH_SHORT).show();
                //answer.setText("Your Previous Answer : No ");
                if (numberOfItems == limit){
                    startActivity(new Intent(SurveyActivity.this,SubmitSurveyActivity.class));
                }
                else {

                    viewPager.setCurrentItem(numberOfItems++);

                }
                //questions are pulled of the array of string received from the server
                changeQuestionOnSwipe();

                //check if the limit of the total questions is reached it will redirect to submit the survey
                checkLimitOfSurvey();

            }
        });

    }

    private void checkLimitOfSurvey() {

        if (numberOfItems == limit){
            startActivity(new Intent(SurveyActivity.this,SubmitSurveyActivity.class));
        }
    }

    // method to change the fragments on swipes

    public void changeQuestionOnSwipe(){

        QuestionOneFragment nextFragment = new QuestionOneFragment();
        Bundle args = new Bundle();
        nextFragment.setArguments(args);

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.viewPager,nextFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}

class OnSwipeTouchListener implements View.OnTouchListener {

    private final GestureDetector gestureDetector;

    public OnSwipeTouchListener(Context context) {
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    public void onSwipeLeft() {

    }

    public void onSwipeRight() {
    }

    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_DISTANCE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float distanceX = e2.getX() - e1.getX();
            float distanceY = e2.getY() - e1.getY();
            if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceX > 0)
                    onSwipeRight();
                else
                    onSwipeLeft();
                return true;
            }
            return false;
        }
    }

}




