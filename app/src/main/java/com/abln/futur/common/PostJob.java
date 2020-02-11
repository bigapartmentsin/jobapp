package com.abln.futur.common;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.abln.chat.utils.CircleImageView;
import com.abln.futur.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;


public class PostJob extends BottomNav {


    private static final int PICK_IMAGE_MULTIPLE = 10;
    private static int REQUEST_CODE_FOR_ON_ACTIVITY_RESULT = 2019;
    private static int REQUEST_CODE_FOR_ON_ACTIVITY_RESULT_PDF = 2020;
    int PLACE_PICKER_REQUEST = 1;
    @BindView(R.id.galleryPhotoListWrap)
    LinearLayout galleryPhotoListWrap;
    @BindView(R.id.addMorePhotoImgBtn)
    ImageView addMorePhotoImgBtn;
    @BindView(R.id.add_more_photo_sec)
    RelativeLayout addMorePhotoSec;
    @BindView(R.id.add_photo_sec)
    LinearLayout addPhotoSec;
    private ArrayList<String> galleryImages = new ArrayList<>();

    public static PostJob newInstance() {

        PostJob fragment = new PostJob();

        return fragment;
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_create_job_post;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }


    private void setImageData() {

        //galleryPhotoListWrap.removeAllViews();

        ArrayList<ImageSlide> slideArrayList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(slideArrayList)) {
            for (int i = 0; i < slideArrayList.size(); i++) {


                ImageSlide imageSlide = slideArrayList.get(i);
                addPhotoSec.setVisibility(View.GONE);
                addMorePhotoSec.setVisibility(View.VISIBLE);
                final View preview = getLayoutInflater().inflate(R.layout.preview_round_img, null);
                final CircleImageView circleImageView = preview.findViewById(R.id.img_view_sec);
                ImageLoader.loadImage(imageSlide.getLarge(), circleImageView);
                int position = i;
                circleImageView.setTag(imageSlide);
                //   circleImageView.setOnClickListener(v -> showBigImgPopup(slideArrayList, position));
                galleryPhotoListWrap.addView(preview);


            }
        } else {

            UIUtility.showToastMsg_withErrorShort(getActivity(), " Error occured to get the files ");
        }


    }


    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);

    }


    @OnClick({R.id.add_photo_sec})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.add_photo_sec:
                openImagePicker();
                UIUtility.showToastMsg_withSuccessShort(getActivity(), " Add Photo online ");
                break;


        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == PICK_IMAGE_MULTIPLE && requestCode == RESULT_OK && null != data) {
            try {
                addPhotoSec.setVisibility(View.GONE);
                addMorePhotoSec.setVisibility(View.VISIBLE);
                if (data.getData() != null) {
                    Uri mImageUri = data.getData();
                    String img_url = ImageEvents.getAbsolutePath(getActivity(), mImageUri);
                    galleryImages.add(img_url);

                    setImageData();
                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();

                        for (int i = 0; i < mClipData.getItemCount(); i++) {


                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            String img_url = ImageEvents.getAbsolutePath(getActivity(), uri);
                            galleryImages.add(img_url);
                            setImageData();


                        }
                    }
                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }


    private void openFilePickerToUploadJobPost_Image() {
        Intent filesIntent;
        filesIntent = new Intent(Intent.ACTION_GET_CONTENT);
        filesIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        filesIntent.addCategory(Intent.CATEGORY_OPENABLE);
        filesIntent.setType("image/*");
        startActivityForResult(filesIntent, REQUEST_CODE_FOR_ON_ACTIVITY_RESULT);
    }


    private void openFilePickerToUploadJobPost_Pdf() {
        Intent filesIntent;
        filesIntent = new Intent(Intent.ACTION_GET_CONTENT);
        filesIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        filesIntent.addCategory(Intent.CATEGORY_OPENABLE);
        filesIntent.setType("application/pdf");
        startActivityForResult(filesIntent, REQUEST_CODE_FOR_ON_ACTIVITY_RESULT_PDF);
    }


}
