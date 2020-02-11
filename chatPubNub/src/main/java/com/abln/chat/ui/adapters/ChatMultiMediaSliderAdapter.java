package com.abln.chat.ui.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore.Video.Thumbnails;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.abln.chat.R;
import com.abln.chat.ui.helper.ChatMultiMediaHelper;

import java.util.ArrayList;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import uk.co.senab.photoview.PhotoViewAttacher;


public class ChatMultiMediaSliderAdapter extends PagerAdapter {

    private ArrayList<String> mMultimediaPathList = new ArrayList<>();
    private Context mContext;

    public ChatMultiMediaSliderAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mMultimediaPathList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup multimediaContainer, final int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View multimediaView = inflater.inflate(R.layout.im_item_multimedia_slide_page, multimediaContainer, false);

        ImageView thumbnail = (ImageView) multimediaView.findViewById(R.id.im_iv_multimedia_slider);
        ImageView playIcon = (ImageView) multimediaView.findViewById(R.id.im_iv_multimedia_slider_video_icon);

        if (!mMultimediaPathList.get(position).endsWith(".mp4")) {
            setChatMultimediaImage(thumbnail, playIcon, mMultimediaPathList.get(position));
        } else {
            setChatMulimediaVideo(thumbnail, playIcon, mMultimediaPathList.get(position));
        }

        multimediaContainer.addView(multimediaView, 0);
        return multimediaView;
    }

    private void setChatMultimediaImage(ImageView thumbnail, ImageView playIcon, String imagePath) {
        thumbnail.setImageBitmap(BitmapFactory.decodeFile(imagePath));
        playIcon.setVisibility(View.GONE);
        new PhotoViewAttacher(thumbnail);
    }

    private void setChatMulimediaVideo(ImageView thumbnail, ImageView playIcon, String videoPath) {
        thumbnail.setImageBitmap(ThumbnailUtils.createVideoThumbnail(videoPath, Thumbnails.MINI_KIND));
        playIcon.setVisibility(View.VISIBLE);

        setOnVideoClickListener(thumbnail, videoPath);
    }

    public void setMultimediaList(ArrayList<String> multiMediaPathList) {
        mMultimediaPathList.clear();
        mMultimediaPathList.addAll(multiMediaPathList);
        notifyDataSetChanged();
    }

    public int getCurrentPos(String msgId) {
        int pos = 0;
        for (int i = 0; i < mMultimediaPathList.size(); i++) {
            if (mMultimediaPathList.get(i).contains(msgId)) {
                pos = i;
            }
        }
        return pos;
    }


    private void setOnVideoClickListener(ImageView imageView, final String videoPath) {
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatMultiMediaHelper.openChatVideoPlayer(mContext, videoPath);
            }
        });
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((FrameLayout) object);

    }
}
