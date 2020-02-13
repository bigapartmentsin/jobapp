package com.abln.futur.common;


import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.util.Pair;
import androidx.viewpager.widget.ViewPager;

import com.abln.futur.BuildConfig;
import com.abln.futur.R;
import com.abln.futur.RandomAct;
import com.abln.futur.common.postjobs.BaseBottomSheetDialogFragment;
import com.abln.futur.common.postjobs.GetNormalpost;
import com.abln.futur.common.postjobs.post;
import com.abln.futur.interfaces.NetworkOperation;
import com.abln.futur.module.account.datamodel.GetLoginRequest;
import com.abln.futur.module.global.activities.SearchAdapter;
import com.abln.futur.services.NetworkOperationService;
import com.abln.futur.utils.BitmapUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;
import com.rtchagas.pingplacepicker.PingPlacePicker;

import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static com.abln.chat.ui.helper.ChatMultiMediaHelper.getFilePathFromURI;
import static com.abln.futur.common.UIUtility.dateFormatFun;


///uploadJobPost   : click event has to happen .

public class NewJobPost extends BaseBottomSheetDialogFragment implements LocationAdapter.oneventclick {


    private static final int PICK_IMAGE_MULTIPLE = 10;
    private static int REQUEST_CODE_FOR_ON_ACTIVITY_RESULT_PDF = 2020;
    private static int PICK_SINGLE_LOGO = 15;
    private static int PICK_POSTER_THUBM = 9;
    @BindView(R.id.add_photo_sec)
    LinearLayout addPhotoSec;


    @BindView(R.id.galleryPhotoListWrap)
    LinearLayout galleryPhotoListWrap;

    @BindView(R.id.addMorePhotoImgBtn)
    ImageView addMorePhotoImgBtn;

    @BindView(R.id.add_more_photo_sec)
    RelativeLayout addMorePhotoSec;

    @BindView(R.id.lyt_upload)
    LinearLayout send;

    @BindView(R.id.jobPostImage)
    ImageView jobPostImage;

    @BindView(R.id.uploadJobPost)
    ImageView uploadjobpostimg;

//    @BindView(R.id.expiryDate)
//    EditText etExpiryDate;
    @BindView(R.id.job_title_text)
    AutoCompleteTextView jobtitletext;

    @BindView(R.id.cname_edit_text)
    EditText cname_edit_text;

    @BindView(R.id.rangeSeekbarEXP)
    CrystalRangeSeekbar rangeSeekbar;

    @BindView(R.id.tvExpYrs)
    TextView tvExp;

    @BindView(R.id.jobLocation)
    TextView joblocation;

    @BindView(R.id.inputspinner)
    Spinner inputspinner;

    @BindView(R.id.image_logo)
    ImageView logo;

    @BindView(R.id.l5)
    LinearLayout locationWrap;




    int SELECT_FILE = 2;
    int PLACE_PICKER_REQUEST = 1;
    private Addinfo addinfo;
    private String picturePath = "";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
    private AlertDialog alertPageExitDialog;
    private Calendar startTime = Calendar.getInstance();

///Date 30 days madatory ;
    private Calendar endTime = Calendar.getInstance();
    private ArrayList<String> galleryImages = new ArrayList<>();
    private NewJobDetailsData newDetailsResp;
    private String finalkey;
    private String lat = "", lag = "", revgeo = "";
    private String selectedEXPDate = "";
    private String tvvalue;
    private String pdfFilePath = "";
    private String logo_string = "";
    private String thumb_string = "";
    private String min, max;
    private String inputSpinner, output;
    private String Name;

    public static NewJobPost newInstance() {
        NewJobPost fragment = new NewJobPost();
        return fragment;
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_create_job_post;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<StringWithTag> spinnerList = new ArrayList<StringWithTag>();
        spinnerList.add(new StringWithTag("5 Days", "5"));
        spinnerList.add(new StringWithTag("10 Days", "10"));
        spinnerList.add(new StringWithTag("15 Days", "15"));
        spinnerList.add(new StringWithTag("20 Days", "20"));
        spinnerList.add(new StringWithTag("25 Days", "25"));
        spinnerList.add(new StringWithTag("30 Days", "30"));
        bindSpinnerDropDown(inputspinner, spinnerList);

        getdata();
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {

                tvvalue = minValue + " - " + maxValue + " Yrs";


                min = String.valueOf(minValue);
                max = String.valueOf(maxValue);




                if (min.equalsIgnoreCase(max)) {
                    tvExp.setText("12+");
                    min = "12";
                    max = "30";

                } else {
                    tvExp.setText(minValue + " - " + maxValue + " Yrs");
                }


            }
        });


        addPhotoSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // openImagePicker();

                checkForPermissionAndTakeMultipleImagePdf();
            }
        });

        locationWrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPlacePicker();
                //dataLocation();

            }
        });

        addMorePhotoImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inputSpinner = SpinnerDropDown.getSpinnerItem(inputspinner);



                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Calendar c = Calendar.getInstance();
                c.setTime(new Date()); // Now use today date.
                c.add(Calendar.DATE, Integer.parseInt(inputSpinner)); // Adding 5 days
                output = sdf.format(c.getTime());
                System.out.println(output);




                if (!CollectionUtils.isEmpty(galleryImages) || !pdfFilePath.equalsIgnoreCase("")) {

                    checkforErrorin();

                } else {


                    UIUtility.showToastMsg_withErrorShort(getActivity(), "Add PDF to upload  or images");


                }


            }
        });


        jobPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtility.showToastMsg_withAlertInfoShort(getActivity(), "Upload Logo or PosterImage");
                handleImagefiles();

            }
        });


    }

    private void sendthumbnail(String value, String data) {


        File file = new File(data);
        RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("s11", file.getName(), requestFile);
        Map<String, RequestBody> partMap = new HashMap<>();
        partMap.put("apikey", RequestBody.create(MediaType.parse("text/plain"), value));


        compositeDisposable.add((apiService.s1(partMap, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<JsonObject>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {

                        dismissProgress();
                        if (baseResponse.statuscode == 0) {
                            UIUtility.showToastMsg_withErrorShort(getActivity(), baseResponse.statusMessage);
                        } else if (baseResponse.statuscode == 1) {

                            UIUtility.showToastMsg_withSuccessShort(getActivity(), baseResponse.statusMessage);

                        }

                    }

                    @Override
                    public void onFailure(Throwable e) {


                        UIUtility.showToastMsg_withErrorShort(getActivity(), "Opp's something happened ");


                    }
                })));


    }

    private void checkforErrorin() {


        if (
                jobtitletext.getText().toString().equalsIgnoreCase("")) {

            UIUtility.showToastMsg_withErrorShort(getActivity(), "Missing Job title ");

        } else if (cname_edit_text.getText().toString().equalsIgnoreCase("")) {

            UIUtility.showToastMsg_withErrorShort(getActivity(), "Missing  Company Name ");


        } else {
            checkLocationError();

        }
    }

    private void checkLocationError() {


        if (revgeo.equalsIgnoreCase("")) {
            UIUtility.showToastMsg_withErrorShort(getActivity(), "Add Location ");
        } else {


            if (!pdfFilePath.equalsIgnoreCase("")) {


                UIUtility.showToastMsg_withSuccessShort(getActivity(), "Upload job post  with pdf ");

                uploadJobPost(jobtitletext.getText().toString(), cname_edit_text.getText().toString(), tvvalue, lat, lag, output, pdfFilePath, min, max, revgeo);










            } else if (!CollectionUtils.isEmpty(galleryImages)) {


                UIUtility.showToastMsg_withSuccessShort(getActivity(), "Upload job post  with images ");

                normalJobPost(jobtitletext.getText().toString(), cname_edit_text.getText().toString(), tvvalue, lat, lag, min, max);


            } else {

                UIUtility.showToastMsg_withErrorShort(getActivity(), "Error in uploading job post try again ");

            }


        }


    }

    public void bindSpinnerDropDown(Spinner spinner, ArrayList<StringWithTag> list) {
        ArrayAdapter<StringWithTag> spinnerArrayAdapter = new ArrayAdapter<StringWithTag>(getActivity(), android.R.layout.simple_spinner_item, list);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }

    private void imagefunction(String key) {


        if (!CollectionUtils.isEmpty(galleryImages)) {


            //value = true ;
            //index = 10;


            System.out.println("Getting the total number of image added"+galleryImages.size());
            System.out.println("Moving the adding new posts");

            System.out.println("Taking the data to the background thread");


            System.out.println("Ma");



            for (int i = 0; i < galleryImages.size(); i++) {


                if (i == 0) {



                    s1(key, galleryImages.get(i));


                } else if (i == 1) {



                    s2(key, galleryImages.get(i));

                } else if (i == 2) {




                    s3(key, galleryImages.get(i));

                } else if (i == 3) {


                    s4(key, galleryImages.get(i));

                } else if (i == 4) {




                    s5(key, galleryImages.get(i));

                } else if (i == 5) {




                    s6(key, galleryImages.get(i));

                } else if (i == 6) {





                    s7(key, galleryImages.get(i));


                } else if (i == 7) {



                    s8(key, galleryImages.get(i));

                } else if (i == 8) {


                    s9(key, galleryImages.get(i));

                } else if (i == 9) {

                    s10(key, galleryImages.get(i));

                }
            }


        }


    }

    private void showPlacePicker() {
        PingPlacePicker.IntentBuilder builder = new PingPlacePicker.IntentBuilder();
        builder.setAndroidApiKey("AIzaSyAcbb9wwo1yWoCwvPB1fq7LxKgFaFFZw4M")


                .setMapsApiKey("AIzaSyAcbb9wwo1yWoCwvPB1fq7LxKgFaFFZw4M");


        if (newDetailsResp != null && newDetailsResp.clinic_details != null) {


            builder.setLatLng(new LatLng(
                    Double.parseDouble(newDetailsResp.clinic_details.getLat()), Double.parseDouble(newDetailsResp.clinic_details.getLng())));


        }


        try {
            Intent placeIntent = builder.build(getActivity());
            startActivityForResult(placeIntent, PLACE_PICKER_REQUEST);
        } catch (Exception ex) {
            // Google Play services is not available...
        }
    }




    private void setLogo() {


        if (logo_string.equalsIgnoreCase("")) {


        } else {


            Glide.with(getContext()).load(logo_string)

                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(14)))
                    .into(logo);

        }


    }

    private void setThumbposter() {

        //   Glide.with(getContext()).load()

    }

    private void setImageData() {
        galleryPhotoListWrap.removeAllViews();

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


                Glide.with(getContext()).load(imageSlide.getLarge())

                        .into(circleImageView);

                int position = i;
                circleImageView.setTag(imageSlide);
                galleryPhotoListWrap.addView(preview);
            }
        }


    }

    private void openImagePicker() {


        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);


    }

    private void information(Place place) {


        String address = place.getAddress();
        joblocation.setText(address);


        revgeo = address;

        LatLng latLng = place.getLatLng();
        lat = String.valueOf(latLng.latitude);
        lag = String.valueOf(latLng.longitude);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if ((requestCode == PLACE_PICKER_REQUEST) && (resultCode == RESULT_OK)) {
            Place place = PingPlacePicker.getPlace(data);
            if (place != null) {
                information(place);
            }
        } else if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {
            try {
                addPhotoSec.setVisibility(View.GONE);
                addMorePhotoSec.setVisibility(View.VISIBLE);
                if (data.getData() != null) {


                    Uri uri = data.getData();
                    String uriString = uri.toString();
                    File myFile = new File(uriString);


                    String path = getFilePathFromURI(getActivity(), uri);
                    galleryImages.add(path);
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

                Toast.makeText(getActivity(), "Something went wrong in fetching image ", Toast.LENGTH_LONG).show();
            }
        }


        if (requestCode == REQUEST_CODE_FOR_ON_ACTIVITY_RESULT_PDF) {
            if (null != data) { // checking empty selection
                if (null != data.getClipData()) { // checking multiple selection or not
                    for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                        Uri uri = data.getClipData().getItemAt(i).getUri();
                        FLog.d("Uri", "Multiple " + uri.getPath());
                        pdfFilePath = data.getClipData().getItemAt(i).getUri().toString();
                    }
                } else {
                    Uri uri = data.getData();
                    FLog.d("Uri", "Single " + uri.getPath());
                    pdfFilePath = data.getData().getPath();
                    uploadjobpostimg.setImageResource(0);
                    uploadjobpostimg.setBackground(getActivity().getDrawable(R.drawable.ic_view_job_pdf));


                }


                Uri uri = data.getData();
                String uriString = uri.toString();
                File myFile = new File(uriString);

                String path = getFilePathFromURI(getActivity(), uri);

                pdfFilePath = path;
            }

        }


        if (requestCode == PICK_SINGLE_LOGO) {


            if (null != data) {


                jobPostImage.setVisibility(View.GONE);
                logo.setVisibility(View.VISIBLE);

                Uri uri = data.getData();
                String uriString = uri.toString();
                File myFile = new File(uriString);

                logo_string = getFilePathFromURI(getActivity(), uri);

                setLogo();

            }


        }

        if (resultCode == PICK_POSTER_THUBM) {

            if (null != data) {

                jobPostImage.setVisibility(View.GONE);
                logo.setVisibility(View.VISIBLE);

                Uri uri = data.getData();
                String uriString = uri.toString();
                File myFile = new File(uriString);

                thumb_string = getFilePathFromURI(getActivity(), uri);

                setLogo();

            }
        }


        if (requestCode == SELECT_FILE) {


            Uri uri = data.getData();
            String uriString = uri.toString();
            File myFile = new File(uriString);
            String path = getFilePathFromURI(getActivity(), uri);
            if (myFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(myFile.getAbsolutePath());
                jobPostImage.setBackgroundDrawable(null);
                jobPostImage.setImageBitmap(myBitmap);
            }


        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getImageFilePath_AsString(Uri uri) {

        File file = new File(uri.getPath());
        String imagePath = "";
        String[] filePath = file.getPath().split(":");
        String image_id = filePath[filePath.length - 1];
        Cursor cursor = getActivity().getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
        if (cursor != null) {
            cursor.moveToFirst();
            imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
        }
        return imagePath;
    }

    private void addNewImages(ArrayList<String> imageSlides, final String clinicId) {
        compositeDisposable.add(Observable.fromIterable(imageSlides)
                .flatMap((Function<String, ObservableSource<Pair<String, BaseResponse<JsonObject>>>>) path -> {
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                    String imageFileName = "image_" + timeStamp;

                    File file = new File(path);


                    // create RequestBody instance from file
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);

                    MultipartBody.Part body = MultipartBody.Part.createFormData("s1", file.getName(), requestFile);

                    // add another part within the multipart request
                    RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "f8ae8f50a40f0b5aa4f6c77100a51d4c");

                    return Observable.zip(Observable.just(path), apiService.addClinicFiles("v1/s1",description, body).toObservable(), (addPrescDosageReq, jsonObjectBaseResponse) -> new Pair<>(addPrescDosageReq, jsonObjectBaseResponse));
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<List<Pair<String, BaseResponse<JsonObject>>>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {


                        Log.d("TAG", baseResponse.statusMessage);

                    }

                    @Override
                    public void onDataSuccess(List<Pair<String, BaseResponse<JsonObject>>> pairs) {
                        Toast.makeText(getActivity(), "Updated successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                }));
    }

    private void s1(String value, String data) {

///data/user/0/com.abln.futur/cache/download.jpeg

        File file = new File(data);
        RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("s1", file.getName(), requestFile);
        Map<String, RequestBody> partMap = new HashMap<>();
        partMap.put("apikey", RequestBody.create(MediaType.parse("text/plain"), value));


        compositeDisposable.add((apiService.s1(partMap, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<JsonObject>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {

                        dismissProgress();
                        if (baseResponse.statuscode == 0) {
                            UIUtility.showToastMsg_withErrorShort(getActivity(), baseResponse.statusMessage);
                        } else if (baseResponse.statuscode == 1) {

                            UIUtility.showToastMsg_withSuccessShort(getActivity(), baseResponse.statusMessage);

                        }

                    }

                    @Override
                    public void onFailure(Throwable e) {


                        UIUtility.showToastMsg_withErrorShort(getActivity(), "Opp's something happened ");


                    }
                })));


    }

    private void s2(String value, String data) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_" + timeStamp;
        File file = new File(data);
        RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("s2", imageFileName + file.getName(), requestFile);
        Map<String, RequestBody> partMap = new HashMap<>();
        partMap.put("apikey", RequestBody.create(MediaType.parse("text/plain"), value));

        compositeDisposable.add((apiService.s2(partMap, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<JsonObject>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {


                        dismissProgress();

                        if (baseResponse.statuscode == 0) {
                            UIUtility.showToastMsg_withErrorShort(getActivity(), baseResponse.statusMessage);
                        } else if (baseResponse.statuscode == 1) {

                            UIUtility.showToastMsg_withSuccessShort(getActivity(), baseResponse.statusMessage);

                        }

                    }

                    @Override
                    public void onFailure(Throwable e) {


                        UIUtility.showToastMsg_withErrorShort(getActivity(), "Opp's something happened ");


                    }
                })));


    }

    private void s3(String value, String data) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_" + timeStamp;
        File file = new File(data);
        RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("s3", imageFileName + file.getName(), requestFile);
        Map<String, RequestBody> partMap = new HashMap<>();
        partMap.put("apikey", RequestBody.create(MediaType.parse("text/plain"), value));


        compositeDisposable.add((apiService.s3(partMap, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<JsonObject>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {
                        dismissProgress();

                        if (baseResponse.statuscode == 0) {
                            UIUtility.showToastMsg_withErrorShort(getActivity(), baseResponse.statusMessage);
                        } else if (baseResponse.statuscode == 1) {

                            UIUtility.showToastMsg_withSuccessShort(getActivity(), baseResponse.statusMessage);

                        }

                    }

                    @Override
                    public void onFailure(Throwable e) {


                        UIUtility.showToastMsg_withErrorShort(getActivity(), "Opp's something happened ");


                    }
                })));

    }

    private void s4(String value, String data) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_" + timeStamp;
        File file = new File(data);
        RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("s4", imageFileName + file.getName(), requestFile);
        Map<String, RequestBody> partMap = new HashMap<>();
        partMap.put("apikey", RequestBody.create(MediaType.parse("text/plain"), value));


        compositeDisposable.add((apiService.s4(partMap, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<JsonObject>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {

                        dismissProgress();
                        if (baseResponse.statuscode == 0) {
                            UIUtility.showToastMsg_withErrorShort(getActivity(), baseResponse.statusMessage);
                        } else if (baseResponse.statuscode == 1) {

                            UIUtility.showToastMsg_withSuccessShort(getActivity(), baseResponse.statusMessage);

                        }

                    }

                    @Override
                    public void onFailure(Throwable e) {


                        UIUtility.showToastMsg_withErrorShort(getActivity(), "Opp's something happened ");


                    }
                })));

    }

    private void s5(String value, String data) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_" + timeStamp;
        File file = new File(data);
        RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("s5", imageFileName + file.getName(), requestFile);
        Map<String, RequestBody> partMap = new HashMap<>();
        partMap.put("apikey", RequestBody.create(MediaType.parse("text/plain"), value));


        compositeDisposable.add((apiService.s5(partMap, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<JsonObject>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {

                        dismissProgress();
                        if (baseResponse.statuscode == 0) {
                            UIUtility.showToastMsg_withErrorShort(getActivity(), baseResponse.statusMessage);
                        } else if (baseResponse.statuscode == 1) {

                            UIUtility.showToastMsg_withSuccessShort(getActivity(), baseResponse.statusMessage);

                        }

                    }

                    @Override
                    public void onFailure(Throwable e) {


                        UIUtility.showToastMsg_withErrorShort(getActivity(), "Opp's something happened ");


                    }
                })));

    }

    private void s6(String value, String data) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_" + timeStamp;
        File file = new File(data);
        RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("s6", imageFileName + file.getName(), requestFile);
        Map<String, RequestBody> partMap = new HashMap<>();
        partMap.put("apikey", RequestBody.create(MediaType.parse("text/plain"), value));


        compositeDisposable.add((apiService.s6(partMap, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<JsonObject>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {

                        dismissProgress();
                        if (baseResponse.statuscode == 0) {
                            UIUtility.showToastMsg_withErrorShort(getActivity(), baseResponse.statusMessage);
                        } else if (baseResponse.statuscode == 1) {

                            UIUtility.showToastMsg_withSuccessShort(getActivity(), baseResponse.statusMessage);

                        }

                    }

                    @Override
                    public void onFailure(Throwable e) {


                        UIUtility.showToastMsg_withErrorShort(getActivity(), "Opp's something happened ");


                    }
                })));
    }

    private void s7(String value, String data) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_" + timeStamp;
        File file = new File(data);
        RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("s7", imageFileName + file.getName(), requestFile);
        Map<String, RequestBody> partMap = new HashMap<>();
        partMap.put("apikey", RequestBody.create(MediaType.parse("text/plain"), value));


        compositeDisposable.add((apiService.s7(partMap, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<JsonObject>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {


                        if (baseResponse.statuscode == 0) {
                            UIUtility.showToastMsg_withErrorShort(getActivity(), baseResponse.statusMessage);
                        } else if (baseResponse.statuscode == 1) {

                            UIUtility.showToastMsg_withSuccessShort(getActivity(), baseResponse.statusMessage);

                        }

                    }

                    @Override
                    public void onFailure(Throwable e) {


                        UIUtility.showToastMsg_withErrorShort(getActivity(), "Opp's something happened ");


                    }
                })));
    }

    private void s8(String value, String data) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_" + timeStamp;
        File file = new File(data);
        RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("s8", imageFileName + file.getName(), requestFile);
        Map<String, RequestBody> partMap = new HashMap<>();
        partMap.put("apikey", RequestBody.create(MediaType.parse("text/plain"), value));


        compositeDisposable.add((apiService.s8(partMap, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<JsonObject>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {
                        dismissProgress();

                        if (baseResponse.statuscode == 0) {
                            UIUtility.showToastMsg_withErrorShort(getActivity(), baseResponse.statusMessage);
                        } else if (baseResponse.statuscode == 1) {

                            UIUtility.showToastMsg_withSuccessShort(getActivity(), baseResponse.statusMessage);

                        }

                    }

                    @Override
                    public void onFailure(Throwable e) {


                        UIUtility.showToastMsg_withErrorShort(getActivity(), "Opp's something happened ");


                    }
                })));

    }

    @Override
    public void dismissProgress() {
        super.dismissProgress();

    }

    private void s9(String value, String data) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_" + timeStamp;
        File file = new File(data);
        RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("s9", file.getName(), requestFile);
        Map<String, RequestBody> partMap = new HashMap<>();
        partMap.put("apikey", RequestBody.create(MediaType.parse("text/plain"), value));


        compositeDisposable.add((apiService.s9(partMap, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<JsonObject>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {

                        dismissProgress();
                        if (baseResponse.statuscode == 0) {
                            UIUtility.showToastMsg_withErrorShort(getActivity(), baseResponse.statusMessage);
                        } else if (baseResponse.statuscode == 1) {

                            UIUtility.showToastMsg_withSuccessShort(getActivity(), baseResponse.statusMessage);

                        }

                    }

                    @Override
                    public void onFailure(Throwable e) {


                        UIUtility.showToastMsg_withErrorShort(getActivity(), "Opp's something happened ");


                    }
                })));

    }

    private void s10(String value, String data) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_" + timeStamp;
        File file = new File(data);
        RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("s10", imageFileName + file.getName(), requestFile);
        Map<String, RequestBody> partMap = new HashMap<>();
        partMap.put("apikey", RequestBody.create(MediaType.parse("text/plain"), value));


        compositeDisposable.add((apiService.s10(partMap, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<JsonObject>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {
                        dismissProgress();

                        if (baseResponse.statuscode == 0) {
                            UIUtility.showToastMsg_withErrorShort(getActivity(), baseResponse.statusMessage);
                        } else if (baseResponse.statuscode == 1) {

                            UIUtility.showToastMsg_withSuccessShort(getActivity(), baseResponse.statusMessage);

                        }

                    }

                    @Override
                    public void onFailure(Throwable e) {


                        UIUtility.showToastMsg_withErrorShort(getActivity(), "Opp's something happened ");


                    }
                })));

    }

    private void uploadJobPost(String jt, String jc, String je, String lat, String log, String expdate, String data, String min, String max, String revgeo) {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "pdf_" + timeStamp;
        File file = new File(data);
        RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("pdf", file.getName(), requestFile);
        Map<String, RequestBody> partMap = new HashMap<>();
        partMap.put("apikey", RequestBody.create(MediaType.parse("text/plain"), prefManager.getApikey()));
        partMap.put("job_title", RequestBody.create(MediaType.parse("text/plain"), jt));
        partMap.put("company_name", RequestBody.create(MediaType.parse("text/plain"), jc));
        partMap.put("job_experience", RequestBody.create(MediaType.parse("text/plain"), je));
        partMap.put("lat", RequestBody.create(MediaType.parse("text/plain"), lat));
        partMap.put("lng", RequestBody.create(MediaType.parse("text/plain"), log));
        partMap.put("expdate", RequestBody.create(MediaType.parse("text/plain"), expdate));
        partMap.put("job_experience_from", RequestBody.create(MediaType.parse("text/plain"), min));
        partMap.put("job_experience_to", RequestBody.create(MediaType.parse("text/plain"), max));
        partMap.put("reversegeo", RequestBody.create(MediaType.parse("text/plain"), revgeo));
        partMap.put("dcount", RequestBody.create(MediaType.parse("text/plain"), inputSpinner));

        
        compositeDisposable.add((apiService.addNewPost(partMap, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<GetNormalpost>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {


                        if (baseResponse.statuscode == 0) {


                            UIUtility.showToastMsg_withErrorShort(getActivity(), baseResponse.statusMessage);


                        } else if (baseResponse.statuscode == 1) {

                            //  baseBottomDissmiss();


                            GetNormalpost gpo = (GetNormalpost) baseResponse.data;


                               finalkey = gpo.refer_number;

                            if (!logo_string.equalsIgnoreCase("")) {
                                sendthumbnail(finalkey, logo_string);
                            }

//todo handling exception for the day to life ;
                            try {
                                movingToNext(je);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                        }

                    }

                    @Override
                    public void onFailure(Throwable e) {


                        UIUtility.showToastMsg_withErrorShort(getActivity(), "Opp's something happened ");


                    }
                })));
    }



    private void movingToNext(String je ) throws ParseException {

        Date date = new Date();
        String currentDate = new SimpleDateFormat("E dd MMM yyyy", Locale.getDefault()).format(new Date());
        System.out.println("Checking new date fromat"+currentDate);

        String newers[] = currentDate.split("\\s+");

        for (String a :newers
             ) {

            System.out.println("new breaking string"+a);

        }



                                    String posted = newers[0]+" "+newers[1]+" "+newers[2];
                            Intent i = new Intent(getActivity(),RandomAct.class);
                            i.putExtra("title", jobtitletext.getText().toString());
                            i.putExtra("comName", cname_edit_text.getText().toString());
                            i.putExtra("exper",je) ;
                            i.putExtra("fkey",finalkey);
                            i.putExtra("date",posted);
                            //time function for show in formate //
                            startActivity(i);
















//https://stackoverflow.com/questions/17192776/get-value-of-day-month-from-date-object-in-android









//
//        2020-02-12 18:06:05.453 26629-26629/com.abln.futur I/System.out: 17-02-2020
//        2020-02-12 18:06:08.560 26629-26629/com.abln.futur I/System.out: Error here Wednesday
//        2020-02-12 18:06:08.560 26629-26629/com.abln.futur I/System.out: Err February 12
//        2020-02-12 18:06:08.561 26629-26629/com.abln.futur I/System.out: MonthFebruary



//
//                            String posted = finalweek+" "+month[2]+" "+finalmonth;
//                            Intent i = new Intent(getActivity(),RandomAct.class);
//                            i.putExtra("title", jobtitletext.getText().toString());
//                            i.putExtra("comName", cname_edit_text.getText().toString());
//                            i.putExtra("exper",je) ;
//                            i.putExtra("fkey",finalkey);
//                            i.putExtra("date",posted);
//                            //time function for show in formate //
//                            startActivity(i);



    }

    private void normalJobPost(String jt, String jc, String tva, String lat, String lag, String rangfrom, String rangto) {

        post data = new post();
        data.apikey = prefManager.getApikey();
        data.job_title = jt;
        data.company_name = jc;
        data.job_experience = tva;
        data.lat = lat;
        data.lng = lag;
        data.job_experience_from = rangfrom;
        data.job_experience_to = rangto;
        data.expdate = output;
        data.dcount = inputSpinner;

        compositeDisposable.add(apiService.normal("v1/addpostnormal",data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<GetNormalpost>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {

                        if (baseResponse.statuscode == 1) {


                            GetNormalpost gopost = (GetNormalpost) baseResponse.data;


                            finalkey = gopost.refer_number;

                            System.out.println("refernceNumber" + finalkey);


                            imagefunction(finalkey);


                            //TODO temprory commenting





                            if (!logo_string.equalsIgnoreCase("")) {
                                sendthumbnail(finalkey, logo_string);
                            }


                        } else if (baseResponse.statuscode == 0) {

                            UIUtility.showToastMsg_withErrorShort(getActivity(), ":You have reached the maximum no of post");
                            UIUtility.showToastMsg_withErrorShort(getActivity(), "Please Delete the old post");

                        }


                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                }));


    }

    private void addDatapost(String job_title, String job_location, String job_company, String lat, String lng, String apikey) {


        post data = new post();
        data.recently = "";
        data.starred_status = "";
        data.postid = "";
        data.radius = "";
        data.company_name = "";
        data.canid = "";
        data.recruiterid = "";
        data.query = "";


        data.starred_status = "";
        data.expdate = "";
        data.experience = "";


    }


    ///Error Handling .
    /// logo not manditory ;
    // date not manditory

    private void postThumbnail(String value, String data) {

        //addthumbnail


    }

    private void checkForPermissionAndTakeMultipleImagePdf() {
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        selectUploadFile();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void selectUploadFile() {
        final CharSequence[] items = {"Choose Image (max 10)", "Choose Pdf"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Upload Job Details");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = UIUtility.checkPermission(getActivity());

                if (items[item].equals("Choose Image (max 10)")) {
                    if (result)


                        openImagePicker();

                } else if (items[item].equals("Choose Pdf")) {
                    if (result)
                        openFilePickerToUploadJobPost_Pdf();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    // for company logo and image poster .
    private void handleImagefiles() {
        final CharSequence[] ls = {"Company Logo", "Poster Thumbnail"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Select (Optional)");
        builder.setItems(ls, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = UIUtility.checkPermission(getActivity());

                if (ls[item].equals("Company Logo")) {
                    if (result)


                        //TODO handel company logo :


                        openImagPickerForLogo();


                    //  openImagePicker();
                    // galleryIntent();

                } else if (ls[item].equals("Poster Thumbnail")) {
                    if (result)


                        //Handel poster images
                        openImagPickerForLogo();
                    //  openImagePickerForthumbNail();


                    //galleryIntent();

                } else if (ls[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();


    }

    private void openImagPickerForLogo() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_SINGLE_LOGO);


    }

    private void openImagePickerForthumbNail() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_POSTER_THUBM);

    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void openFilePickerToUploadJobPost_Pdf() {
        Intent filesIntent;
        filesIntent = new Intent(Intent.ACTION_GET_CONTENT);
        filesIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        filesIntent.addCategory(Intent.CATEGORY_OPENABLE);
        filesIntent.setType("application/pdf");
        startActivityForResult(filesIntent, REQUEST_CODE_FOR_ON_ACTIVITY_RESULT_PDF);
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void openDatePickerDialog() {


        Calendar calendar = Calendar.getInstance();  // this is default system date

        // DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

//        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());  //set min date                 // set today's date as min date
//        calendar.add(Calendar.DAY_OF_MONTH, 30); // add date to 30 days later
//        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis()); //set max date


        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dob_select_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Calendar now = Calendar.getInstance();

        final DatePicker datePicker = dialog.findViewById(R.id.datePicker);

        datePicker.setMinDate((long) (System.currentTimeMillis() + (8.64e+7)));

        // datePicker.setMaxDate((long ) (System.currentTimeMillis() + (4*7*24*60*60*1000)));
        //  datePicker.setMaxDate((long)  (System.currentTimeMillis() +  (2.592e+6))  );


        TextView doneBtn = dialog.findViewById(R.id.doneBtn);


        doneBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectedEXPDate = (datePicker.getMonth() + 1) + "/" + datePicker.getDayOfMonth() + "/" + datePicker.getYear();
                String _dateStr = dateFormatFun(datePicker.getDayOfMonth() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear());
                //   etExpiryDate.setText(_dateStr);
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void dataLocation() {
        post data = new post();
        data.apikey = prefManager.getApikey();
        compositeDisposable.add(apiService.listofaddress("v1/msaved",data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<Address>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {


                        Address address = (Address) baseResponse.data;
                        ArrayList<Addinfo> address_list = address.address;
                        if (baseResponse.statuscode == 1) {

                            openLocationInfo(address_list);


                        } else if (baseResponse.statuscode == 0 ){

                    showPlacePicker();
                        }


                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                }));
    }

    private void openLocationInfo(ArrayList<Addinfo> info) {
        View view = null;
        final Dialog dialog;

        dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.location_dialog_popup);

        ViewPager viewPager = dialog.findViewById(R.id.scroller);
        TabLayout pointer = dialog.findViewById(R.id.pointer);

        TextView textViewno = dialog.findViewById(R.id.no_text);
        TextView textViewyes = dialog.findViewById(R.id.yes_text);
        LocationAdapter adapter = new LocationAdapter(getActivity(), info, this);
        viewPager.setAdapter(adapter);
        pointer.setupWithViewPager(viewPager, true);
        int ctm = viewPager.getCurrentItem();


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


//
        textViewyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendData();


                dialog.dismiss();

            }
        });
//
//
//
        textViewno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPlacePicker();
                dialog.dismiss();

            }
        });

        dialog.show();


    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }


    @Override
    public void onClickyes(String addinfo) {

        setName(addinfo);

    }

    @Override
    public void sendData() {

        String value = getName();
        joblocation.setText(value);

        revgeo = value;

    }





    private void checkusertitle(){



    }


    private void getdata() {
        compositeDisposable.add(apiService.getfulldata("v1/post-title")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<Title>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {
                        if (baseResponse.statuscode == 1) {
                            Title data = (Title) baseResponse.data;
                            ArrayList<Name> suggestion = data.result;
                            SearchAdapter adapter = new SearchAdapter(getActivity(), suggestion);
                            jobtitletext.setThreshold(1);
                            jobtitletext.setAdapter(adapter);

                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                }));
    }



    private  void makegetUser(){

        GetLoginRequest getLoginRequest = new GetLoginRequest();
        getLoginRequest.setMobile("");
        HashMap<String ,String > headermap = new HashMap<>();
        headermap.put("Content-type","application/json");

        Intent intent = new Intent(getActivity(), NetworkOperationService.class);

        intent.putExtra(NetworkConfig.API_URL,NetworkConfig.login);
        intent.putExtra(NetworkConfig.HEADER_MAP, headermap);
        intent.putExtra(NetworkConfig.INPUT_BODY, getLoginRequest);
        getActivity().startService(intent);

        intent.putExtra(NetworkConfig.REQUEST_TYPE,"title");
        intent.putExtra(NetworkConfig.RESPONSE_BODY,"apikey");
        intent.putExtra(NetworkConfig.API_URL,"data");







    }






}
