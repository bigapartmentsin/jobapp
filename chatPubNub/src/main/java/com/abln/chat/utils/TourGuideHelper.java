package com.abln.chat.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.abln.chat.R;

import java.util.ArrayList;

import tourguide.tourguide.ChainTourGuide;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.Sequence;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

public class TourGuideHelper {
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = TourGuideHelper.class.getSimpleName();

    private boolean isTourGuideEnabled = true;
    private boolean isShowing = false;
    private ChainTourGuide mTourGuideHandler;
    private Activity mContext;
    private Sequence.SequenceBuilder mSequenceBuilder;
    private Animation mEnterAnimation, mExitAnimation;
    private ArrayList<ChainTourGuide> mChainTourGuideList = new ArrayList<>();

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private String mColor = "#05D4F3";

    public TourGuideHelper(Activity context) {
        this.mContext = context;
        mSequenceBuilder = new Sequence.SequenceBuilder();

        mPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        mEditor = mPreferences.edit();

        /* setup enter and exit animation */
        mEnterAnimation = new AlphaAnimation(0f, 1f);
        mEnterAnimation.setDuration(600);
        mEnterAnimation.setFillAfter(true);

        mExitAnimation = new AlphaAnimation(1f, 0f);
        mExitAnimation.setDuration(600);
        mExitAnimation.setFillAfter(true);

        mColor = mContext.getString(R.string.TourGuideHelperColor);
    }

    public TourGuideHelper addView(View view, String title, String description, int gravity) {
        final ChainTourGuide tourGuide = ChainTourGuide.init(mContext);
        ToolTip toolTip = new ToolTip()
                .setTitle(title)
                .setDescription(description)
                .setGravity(gravity)
                .setBackgroundColor(Color.parseColor("#00000000"))
                .setTextColor(Color.parseColor(mColor))
                .setShadow(false);

        tourGuide.setToolTip(toolTip)
                .setOverlay(new Overlay()
                        .setBackgroundColor(Color.parseColor("#EE000000"))
                        .setEnterAnimation(mEnterAnimation)
                        .setExitAnimation(mExitAnimation)
                        .disableClick(false))
                .playLater(view);

        String viewID = "view_" + view.getId();
        if (isTourGuideEnabled &&
                !mPreferences.getBoolean(viewID, false)) {
            mChainTourGuideList.add(tourGuide);
            mEditor.putBoolean(viewID, true).commit();
        }
        return this;
    }

    TourGuide mTourGuide;

    public TourGuide showView(View view, String title, String description, int gravity) {
        String viewID = "view_" + view.getId();
        if (isTourGuideEnabled && mPreferences.getBoolean(viewID, false)) {
            return null;
        }

        isShowing = true;
        ToolTip toolTip = new ToolTip()
                .setTitle(title)
                .setDescription(description)
                .setGravity(gravity)
                .setBackgroundColor(Color.parseColor("#00000000"))
                .setTextColor(Color.parseColor(mColor))
                .setShadow(false);

        mTourGuide = TourGuide.init(mContext).with(TourGuide.Technique.CLICK);
        //tourGuide.setPointer(new Pointer());
        mTourGuide.setToolTip(toolTip)
                .setOverlay(new Overlay()
                        .setBackgroundColor(Color.parseColor("#CC000000"))
                        .setEnterAnimation(mEnterAnimation)
                        .setExitAnimation(mExitAnimation)
                        .disableClick(false)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mTourGuide.cleanUp();
                            }
                        }))
                .playOn(view);
        mEditor.putBoolean(viewID, true).commit();
        return mTourGuide;
    }

    public boolean isShowing() {
        return isShowing;
    }

    public void close() {
        if (mTourGuide != null) {
            mTourGuide.cleanUp();
        }
    }

    public void onStop() {
        if (isShowing()) {
            close();
        }
    }

    public void showGuide() {
        if (mChainTourGuideList.size() <= 1) {
            return;
        }

        mSequenceBuilder.add(mChainTourGuideList.toArray(new ChainTourGuide[0]));
        mSequenceBuilder.setDefaultOverlay(
                new Overlay()
                        .setBackgroundColor(Color.parseColor("#EE000000"))
                        .setEnterAnimation(mEnterAnimation)
                        .setExitAnimation(mExitAnimation)
                        .disableClick(false)
                        .setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mTourGuideHandler.next();
                                    }
                                }
                        ));
        mSequenceBuilder.setDefaultPointer(null);
        mSequenceBuilder.setContinueMethod(Sequence.ContinueMethod.OVERLAY_LISTENER);
        Sequence sequence = mSequenceBuilder.build();
        if (isTourGuideEnabled) {
            mTourGuideHandler = ChainTourGuide.init(mContext).playInSequence(sequence);
        }
    }
}
