package com.abln.chat.utils;

import com.daimajia.swipe.SwipeLayout;

public class IMSwipeEventListener implements SwipeLayout.SwipeListener {

    private int position;
    private OnSwipeLayoutCallbacks layoutCallbacks;

    public IMSwipeEventListener(OnSwipeLayoutCallbacks layoutCallbacks) {
        this.layoutCallbacks = layoutCallbacks;
    }

    public IMSwipeEventListener(int position, OnSwipeLayoutCallbacks layoutCallbacks) {
        this.position = position;
        this.layoutCallbacks = layoutCallbacks;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * on swiping the view this overriden methods comes  into action
     *
     * @param layout
     */
    @Override
    public void onStartOpen(SwipeLayout layout) {
        if (layoutCallbacks != null) {
            layoutCallbacks.closeSwipes();
            layoutCallbacks.setPreviousPosition(position);
        }
    }

    @Override
    public void onOpen(SwipeLayout layout) {

    }

    @Override
    public void onStartClose(SwipeLayout layout) {

    }

    @Override
    public void onClose(SwipeLayout layout) {

    }

    @Override
    public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

    }

    @Override
    public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

    }

    public interface OnSwipeLayoutCallbacks {
        void closeSwipes();

        void setPreviousPosition(int position);
    }

}
