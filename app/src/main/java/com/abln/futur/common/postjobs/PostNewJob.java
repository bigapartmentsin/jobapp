package com.abln.futur.common.postjobs;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;

import com.abln.futur.R;
import com.abln.futur.common.CollectionUtils;
import com.abln.futur.common.ConstantUtils;
import com.abln.futur.common.ImageEvents;
import com.abln.futur.common.ImageLoader;
import com.abln.futur.common.ImageSlide;
import com.abln.futur.common.NewJobDetailsData;
import com.abln.futur.common.SlideshowDialogFragment;
import com.abln.futur.common.UIUtility;
import com.google.android.gms.maps.model.LatLng;
import com.rtchagas.pingplacepicker.PingPlacePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class PostNewJob extends BaseBottomSheetDialogFragment {


    private static final int PICK_IMAGE_MULTIPLE = 10;
    @BindView(R.id.imageView6)
    ImageView imageView6;
    @BindView(R.id.add_photo_txt)
    TextView addPhotoTxt;
    @BindView(R.id.add_photo_sec)
    LinearLayout addPhotoSec;
    @BindView(R.id.galleryPhotoListWrap)
    LinearLayout galleryPhotoListWrap;
    @BindView(R.id.addMorePhotoImgBtn)
    ImageView addMorePhotoImgBtn;
    @BindView(R.id.add_more_photo_sec)
    RelativeLayout addMorePhotoSec;
    @BindView(R.id.linearLayout8)
    LinearLayout linearLayout8;
    @BindView(R.id.locationTxt)
    TextView locationTxt;
    @BindView(R.id.location_wrap)
    LinearLayout locationWrap;
    @BindView(R.id.offffffffffff)
    FrameLayout f;
    int PLACE_PICKER_REQUEST = 1;
    private String clinicId;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
    private AlertDialog alertPageExitDialog;
    private Calendar startTime = Calendar.getInstance();
    private Calendar endTime = Calendar.getInstance();
    private ArrayList<String> galleryImages = new ArrayList<>();
    private NewJobDetailsData newDetailsResp;
    private String fee;

    public static PostNewJob newInstance() {

        PostNewJob fragment = new PostNewJob();

        return fragment;
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_post_new_jobs;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        addPhotoSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtility.showToastMsg_withErrorShort(getActivity(), "Event Happing");
                openImagePicker();
            }
        });

        locationWrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtility.showToastMsg_withErrorShort(getActivity(), "Event Happing");
                showPlacePicker();
            }
        });


        //   EventBus.getDefault().register(this);

    }


    private void showPlacePicker() {
        PingPlacePicker.IntentBuilder builder = new PingPlacePicker.IntentBuilder();
        builder.setAndroidApiKey("AIzaSyBpFqMMVYzhJollDlqLgouez9vJVj33Wpg")


                .setMapsApiKey("AIzaSyDTJeRUhI_2G4rC-an7_ZRXs02CxLexitw");


        if (newDetailsResp != null && newDetailsResp.clinic_details != null) {


            builder.setLatLng(new LatLng(Double.parseDouble(newDetailsResp.clinic_details.getLat()), Double.parseDouble(newDetailsResp.clinic_details.getLng())));
        }
        try {
            Intent placeIntent = builder.build(getActivity());
            startActivityForResult(placeIntent, PLACE_PICKER_REQUEST);
        } catch (Exception ex) {
            // Google Play services is not available...
        }
    }


    private void setImageData() {
        galleryPhotoListWrap.removeAllViews();
        // List<ClinicFilesItem> clinicFiles = newDetailsResp.clinic_details.getClinicFiles();
        ArrayList<ImageSlide> slideArrayList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(galleryImages)) {
            for (String galleryImage : galleryImages) {
                ImageSlide imageSlide = new ImageSlide();
                imageSlide.setLarge(galleryImage);
                slideArrayList.add(imageSlide);
            }
        }

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


                circleImageView.setOnClickListener(v -> showBigImgPopup(slideArrayList, position));


                galleryPhotoListWrap.addView(preview);
            }
        }


    }


    //TODO Send thumnail image
    private void sendThumbnailImageNetworkcall() {


    }


    //TODO Location

    private void locationData() {


    }


    private void showBigImgPopup(ArrayList<ImageSlide> slideArrayList, int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("images", slideArrayList);
        bundle.putSerializable("position", position);
        bundle.putBoolean(ConstantUtils.ADDITIONAL_DATA, true);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
        newFragment.setArguments(bundle);
        newFragment.show(ft, TAG);
    }


    @OnClick({R.id.add_photo_sec, R.id.location_wrap, R.id.addMorePhotoImgBtn})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.add_more_photo_sec:
                openImagePicker();
                break;
            case R.id.location_wrap:
                //showPlacePicker();
                break;
            case R.id.addMorePhotoImgBtn:
                openImagePicker();
                break;


        }


    }


    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if ((requestCode == PLACE_PICKER_REQUEST) && (resultCode == RESULT_OK)) {
//         //   Place place = PingPlacePicker.getPlace(data);
//            if (place != null) {
//           //     updateLocation(place);
//            }
//        } else


        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {
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

                System.out.print(e.getStackTrace());
                System.out.print("Im getting message " + e.getMessage());
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


}


