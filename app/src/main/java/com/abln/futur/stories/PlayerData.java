package com.abln.futur.stories;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.abln.futur.R;
import com.abln.futur.common.UIUtility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;



public class PlayerData extends AppCompatActivity implements  StoryPlayerProgressView.StoryPlayerListener  {

    public static final String STORY_IMAGE_KEY = "storyImages";
    StoryPlayerProgressView storyPlayerProgressView;
    ImageView imageView;
    TextView name;
    TextView time;
    ArrayList<StoryModel> stories;
    StoryPreference storyPreference;
    View revese, skip;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advance_story_layout);
        storyPlayerProgressView = findViewById(R.id.progressBarView);
        name = findViewById(R.id.storyUserName);
        time = findViewById(R.id.storyTime);

        revese = findViewById(R.id.reverse);
        skip = findViewById(R.id.skip);





        storyPlayerProgressView.setSingleStoryDisplayTime(2000);
        imageView = findViewById(R.id.storyImage);
        storyPreference = new StoryPreference(this);
        Intent intent = getIntent();
        if (intent != null) {
            stories = intent.getParcelableArrayListExtra(STORY_IMAGE_KEY);
            initStoryProgressView();
        }



        revese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtility.showToastMsg_withAlertInfoShort(getApplicationContext(),"Back invoked");
            }
        });


        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UIUtility.showToastMsg_withAlertInfoShort(getApplicationContext(),"Skip inovked");
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        storyPlayerProgressView.cancelAnimation();
    }

    private void initStoryProgressView() {
        if (stories != null && stories.size() > 0) {
            storyPlayerProgressView.setStoryPlayerListener(this);
            storyPlayerProgressView.setProgressBarsCount(stories.size());
            setTouchListener();
        }
    }




    private void setTouchListener() {
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    //pause
                    storyPlayerProgressView.pauseProgress();
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    //resume
                    storyPlayerProgressView.resumeProgress();
                    return true;
                }else {
                    return false;
                }
            }
        });
    }



    @Override
    public void onStartedPlaying(int index) {
        loadImage(index);
        name.setText(stories.get(index).name);
        time.setText(stories.get(index).time);
        storyPreference.setStoryVisited(stories.get(index).imageUri);
    }

    @Override
    public void onFinishedPlaying() {
        finish();
    }

    private void loadImage(int index) {
        Glide.with(this)
                .load(stories.get(index).imageUri)
                .transition(DrawableTransitionOptions.withCrossFade(800))
                .into(imageView);
    }





}
