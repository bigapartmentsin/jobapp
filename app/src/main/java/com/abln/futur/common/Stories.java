package com.abln.futur.common;


import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.abln.futur.R;
import com.abln.futur.stories.PlayerData;
import com.abln.futur.stories.StoryModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;



public class Stories extends NewBaseActivity   {
    @BindView(R.id.image)
    ImageView iv;
    long pressTime = 0L;
    long limit = 500L;
    private int counter = 0;
    private final String[] resources = new String[]{
            "https://res.cloudinary.com/medinin/image/upload/v1562649354/futur/stories/image_20200117_103123",
            "https://res.cloudinary.com/medinin/image/upload/v1562649354/futur//stories/image_20200116_175313",
            "https://images.unsplash.com/photo-1579128190739-55bbb36da2a7?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=334&q=80"

    };


    /* todo new variable will be added to the pooling system */


    private ArrayList<StoryModel> storyImageUris;
    private static final int PROGRESS_COUNT = 2;
    @Override
    public int getLayoutId() {
        return R.layout.stories_layout;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storyImageUris = new ArrayList<>();
        ArrayList<StoryModel> uris = new ArrayList<>();
        uris.add(new StoryModel("https://res.cloudinary.com/medinin/image/upload/v1562649354/futur/stories/image_20200117_103123","",""));
        uris.add(new StoryModel("https://res.cloudinary.com/medinin/image/upload/v1562649354/futur//stories/image_20200116_175313","",""));
        uris.add(new StoryModel("https://images.unsplash.com/photo-1579128190739-55bbb36da2a7?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=334&q=80","",""));
        Intent intent = new Intent(this, PlayerData.class);
        intent.putParcelableArrayListExtra(PlayerData.STORY_IMAGE_KEY,uris);
        startActivity(intent);
    }
}
