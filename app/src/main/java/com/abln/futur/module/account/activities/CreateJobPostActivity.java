package com.abln.futur.module.account.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.abln.chat.ui.Network.DataParser;
import com.abln.futur.BuildConfig;
import com.abln.futur.R;
import com.abln.futur.activites.BaseActivity;
import com.abln.futur.common.FLog;
import com.abln.futur.common.FuturApiClient;
import com.abln.futur.common.FuturProgressDialog;
import com.abln.futur.common.NetworkConfig;
import com.abln.futur.common.PrefManager;
import com.abln.futur.common.UIUtility;
import com.abln.futur.interfaces.NetworkOperation;
import com.abln.futur.interfaces.TaskCompleteListener;
import com.abln.futur.listeners.ProgressRequestBody;
import com.abln.futur.module.account.datamodel.GetLoginResponse;
import com.abln.futur.services.NetworkOperationService;
import com.abln.futur.utils.FuturNotificationHandler;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.abln.futur.common.UIUtility.dateFormatFun;

public class CreateJobPostActivity extends BaseActivity implements TaskCompleteListener, ProgressRequestBody.UploadCallbacks {

    private static final String TAG = "CreateJobPostActivity";
    private static int REQUEST_CODE_FOR_ON_ACTIVITY_RESULT = 2019;
    private static int REQUEST_CODE_FOR_ON_ACTIVITY_RESULT_PDF = 2020;
    private static int MAP_REQUEST_CODE = 421;
    @BindView(R.id.job_title_text)
    EditText etJobTitle;
    @BindView(R.id.cname_edit_text)
    EditText etCompanyName;
    @BindView(R.id.jobPostImage)
    ImageView ivJobPost;
    @BindView(R.id.uploadJobPost)
    ImageView ivJobPdfImage;
    @BindView(R.id.jobLocation)
    ImageView ivJobLocation;
    @BindView(R.id.upload)
    TextView UploadButton;
    @BindView(R.id.rangeSeekbarEXP)
    CrystalRangeSeekbar rangeSeekbar;
    @BindView(R.id.tvExpYrs)
    TextView tvExp;
    int REQUEST_CAMERA = 0, SELECT_FILE = 2;
    private String pictureImagePath = "";
    private String picturePath = "";
    private Context mContext;
    private PrefManager prefManager = new PrefManager();
    private String lat = "";
    private String lon = "";
    private String selectedEXPDate = "";

    private ArrayList<String> filePaths = new ArrayList<>();
    private String pdfFilePath = "";

    private ArrayList<String> files = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_job_post);
        mContext = this;
        ButterKnife.bind(this);
        setTaskCompleteListener(this);


        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvExp.setText(minValue + " - " + maxValue + " Yrs");
            }
        });
//        openBottomSheetDialouge();

    }


    @OnClick({R.id.jobPostImage, R.id.uploadJobPost, R.id.jobLocation, R.id.upload})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.jobPostImage:
                checkForPermissionAndTakeImage();
                break;
            case R.id.uploadJobPost:
                checkForPermissionAndTakeMultipleImagePdf();
                break;

            case R.id.jobLocation:


                //   Intent intent = new Intent(mContext, MapsActivity.class);
                //  startActivityForResult(intent,MAP_REQUEST_CODE);
                break;


            case R.id.upload:
                uploadJobPostToServer();
                break;


        }
    }


    private void uploadJobPostToServer() {
        if (etJobTitle.getText().toString().length() < 3) {
            UIUtility.showToastMsg_withErrorShort(mContext, "Enter Job Title");
            return;
        }

        if (etCompanyName.getText().toString().length() < 3) {
            UIUtility.showToastMsg_withErrorShort(mContext, "Enter Company Name");
            return;
        }

        if (lat.equalsIgnoreCase("") || lon.equalsIgnoreCase("")) {
            UIUtility.showToastMsg_withErrorShort(mContext, "Select Job Location");
            return;
        }

//        makeNetWorkCall();
        UIUtility.showToastMsg_withAlertInfoShort(mContext, "Your New Job post will be created soon");
        startUploadNewThread();
        finish();
    }

    private void makeNetWorkCall() {
        FuturProgressDialog.show(mContext, false);
        CreateJobRequest createJobRequest = new CreateJobRequest();
        createJobRequest.setApiKey(prefManager.getApikey());
        createJobRequest.setCompanyName(etCompanyName.getText().toString());
        createJobRequest.setJobTitle(etJobTitle.getText().toString());
        createJobRequest.setJobExperience(tvExp.getText().toString());
        createJobRequest.setExpdate(selectedEXPDate);
        createJobRequest.setLat(lat);
        createJobRequest.setLng(lon);
        createJobRequest.setImg1(picturePath);
        createJobRequest.setPdf(pictureImagePath);


        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");

        Intent intent = new Intent(mContext, NetworkOperationService.class);

        intent.putExtra(NetworkConfig.API_URL, NetworkConfig.addJobPost);
        intent.putExtra(NetworkConfig.HEADER_MAP, headerMap);
        intent.putExtra(NetworkConfig.INPUT_BODY, createJobRequest);
        mContext.startService(intent);
    }


    public void startUploadNewThread() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                uploadMultiPartFiles();
            }
        });
        thread.start();
    }


    private void uploadMultiPartFiles() {

        FuturNotificationHandler.showNotification(mContext, prefManager.getApikey(), "Job Post", "Creating Job Post");

        if (files.size() < 10) {

        }


        File[] filesToUpload = new File[files.size()];
        for (int i = 0; i < files.size(); i++) {
            filesToUpload[i] = new File(files.get(i));
        }


        File imageFile = new File(pictureImagePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/pdf"), imageFile);
        MultipartBody.Part pdfFile = MultipartBody.Part.createFormData("pdf", "pdfDoc", requestFile);

        RequestBody apiKey = RequestBody.create(okhttp3.MultipartBody.FORM, prefManager.getApikey());
        RequestBody company_name = RequestBody.create(okhttp3.MultipartBody.FORM, etCompanyName.getText().toString());
        RequestBody job_title = RequestBody.create(okhttp3.MultipartBody.FORM, etJobTitle.getText().toString());
        RequestBody job_experience = RequestBody.create(okhttp3.MultipartBody.FORM, tvExp.getText().toString());
        RequestBody expdate = RequestBody.create(okhttp3.MultipartBody.FORM, selectedEXPDate);
        RequestBody latt = RequestBody.create(okhttp3.MultipartBody.FORM, lat);
        RequestBody lang = RequestBody.create(okhttp3.MultipartBody.FORM, lon);

        List<MultipartBody.Part> parts = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            RequestBody requestFileImg = RequestBody.create(MediaType.parse("multipart/form-data"), filesToUpload[i]);
            parts.add(MultipartBody.Part.createFormData("s" + (i + 1), filesToUpload[i].getName(), requestFileImg));
            FLog.d(TAG, "Upload" + (i + 1) + "Path " + filesToUpload[i].getPath());
        }


        NetworkOperation apiService = FuturApiClient.getClient2().create(NetworkOperation.class);
        Call<Object> call = apiService.uploadMultipleFilesDynamic(apiKey, company_name, job_title, job_experience, expdate, latt, lang, pdfFile, parts.get(0), parts.get(1),
                parts.get(2), parts.get(3), parts.get(4), parts.get(5), parts.get(6), parts.get(7), parts.get(8), parts.get(9));

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                try {
                    // UIUtility.showToastMsg_withSuccessShort(mContext,"Resume updated successfully");
                    FLog.d(TAG, "Upload success" + response.toString());
//                    file.delete();//TODO handle delete
                    FuturNotificationHandler.removeAllNotifications();
                    FuturNotificationHandler.showNotification(mContext, prefManager.getApikey(), "Done !", "Job Post created successfully");
                    requestForFileDeletion();
                } catch (Exception ex) {
                    //  UIUtility.showToastMsg_withErrorShort(mContext,"Not Uploaded");
                    FLog.d(TAG, "Upload fail" + ex.toString());
                    FuturNotificationHandler.removeAllNotifications();
                    FuturNotificationHandler.showNotification(mContext, prefManager.getApikey(), "Failed !", "Job Post Not created");
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                // UIUtility.showToastMsg_withErrorShort(mContext,"Upload Failed");
                FLog.d(TAG, "Upload error" + t.toString());
                FuturNotificationHandler.removeAllNotifications();
                FuturNotificationHandler.showNotification(mContext, prefManager.getApikey(), "Failed !", "Job Post failed");
            }
        });
    }

    private void requestForFileDeletion() {
        if (!pictureImagePath.equalsIgnoreCase("")) {
            File deleteImg = new File(pictureImagePath);
            if (deleteImg.exists()) {
                deleteImg.delete();
            }
        }
    }


    private void checkForPermissionAndTakeImage() {
        Dexter.withActivity((Activity) mContext)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        openStorage();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void checkForPermissionAndTakeMultipleImagePdf() {
        Dexter.withActivity((Activity) mContext)
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




    /*

    private void openBottomSheetDialouge() {
        View view = getLayoutInflater().inflate(R.layout.activity_create_job_post, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
       CoordinatorLayout bottomSheet = (CoordinatorLayout) view.findViewById(R.id.bottomSheet);
        BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
        dialog.setContentView(view);

        CrystalRangeSeekbar rangeSeekbar = view.findViewById(R.id.rangeSeekbarEXP);

        TextView tvExp = view.findViewById(R.id.tvExpYrs);


        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvExp.setText(String.valueOf(minValue) + " - "+String.valueOf(maxValue) + " Yrs");
            }
        });

        profile_pic = view.findViewById(R.id.jobPostImage);
        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity((Activity)mContext)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                openStorage();
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                if (response.isPermanentlyDenied()) {
                                    // open device settings when the permission is
                                    // denied permanently
                                    openSettings();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }

        });

        ImageView uploadJobPost = view.findViewById(R.id.uploadJobPost);
        uploadJobPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity((Activity)mContext)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                selectUploadFile(); }
                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                if (response.isPermanentlyDenied()) {
                                    openSettings(); } }

                            @Override
                            public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest(); }
                        }).check();
            }
        });

        ImageView ivJobLocation = view.findViewById(R.id.jobLocation);
        ivJobLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MapsActivity.class);
                startActivity(intent);
            }
        });
        dialog.show();
    }



    */

    private void openStorage() {
        selectImage();
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CreateJobPostActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = UIUtility.checkPermission(CreateJobPostActivity.this);

                if (items[item].equals("Take Photo")) {
                    if (result)
                        checkCameraPermission();

                } else if (items[item].equals("Choose from Library")) {
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void checkCameraPermission() {
        Dexter.withActivity((Activity) mContext)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        openBackCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
//                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }


    private void openBackCamera() {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".jpg";
        File storageDir;
        if (Build.VERSION.SDK_INT >= 24) {
            storageDir = new File(android.os.Environment.getExternalStorageDirectory(),
                    "Futur/");
        } else if (Build.VERSION.SDK_INT > 19) {
            storageDir = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOWNLOADS);
        } else {
            storageDir = new File(Environment.getExternalStorageDirectory(), "");
        }
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        File file = new File(pictureImagePath);
        Uri outputFileUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, REQUEST_CAMERA);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                picturePath = getImageFilePath_AsString(data.getData());
                File imgFile = new File(picturePath);
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ivJobPost.setBackgroundDrawable(null);
                    ivJobPost.setImageBitmap(myBitmap);
                }
            }
        }

        if (requestCode == REQUEST_CAMERA) {
            File imgFile = new File(pictureImagePath);
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ivJobPost.setBackgroundDrawable(null);
                ivJobPost.setImageBitmap(myBitmap);
            }
        }

        if (requestCode == REQUEST_CODE_FOR_ON_ACTIVITY_RESULT) {
            if (null != data) { // checking empty selection
                if (null != data.getClipData()) { // checking multiple selection or not
                    for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                        Uri uri = data.getClipData().getItemAt(i).getUri();
                        FLog.d("Uri", "Multiple " + uri.getPath());
                        getImageFilePath(uri);
                    }
                } else {
                    Uri uri = data.getData();
                    FLog.d("Uri", "Single " + uri.getPath());
                    UIUtility.showToastMsg_withAlertInfoShort(mContext, "You can select upto 10 images by long pressing on the images");
                }
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
                }
            }

        }

        if (requestCode == MAP_REQUEST_CODE) {
            lat = prefManager.getSelectedLat();
            lon = prefManager.getSelectedLon();
        }
    }

    public void getImageFilePath(Uri uri) {

        File file = new File(uri.getPath());
        String[] filePath = file.getPath().split(":");
        String image_id = filePath[filePath.length - 1];
        Cursor cursor = getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
        if (cursor != null) {
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            files.add(imagePath);
            cursor.close();
        }
    }

    public String getImageFilePath_AsString(Uri uri) {

        File file = new File(uri.getPath());
        String imagePath = "";
        String[] filePath = file.getPath().split(":");
        String image_id = filePath[filePath.length - 1];
        Cursor cursor = getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
        if (cursor != null) {
            cursor.moveToFirst();
            imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
        }
        return imagePath;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void selectUploadFile() {
        final CharSequence[] items = {"Choose Image", "Choose Pdf", "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CreateJobPostActivity.this);
        builder.setTitle("Choose Job Post");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = UIUtility.checkPermission(CreateJobPostActivity.this);

                if (items[item].equals("Choose Image")) {
                    if (result)
                        openFilePickerToUploadJobPost_Image();

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


    @Override
    public void onTaskCompleted(Context context, Intent intent) {
        String requestType = intent.getStringExtra(NetworkConfig.REQUEST_TYPE);
        String apiUrl = intent.getStringExtra(NetworkConfig.API_URL);
        String responseString = intent.getStringExtra(NetworkConfig.RESPONSE_BODY);

        if (responseString != null && apiUrl.equals(NetworkConfig.addJobPost)) {
            FuturProgressDialog.dismissDialog();
            GetLoginResponse data = DataParser.parseJson(responseString, GetLoginResponse.class);
            if (data.getStatuscode() == 0) {
                UIUtility.showToastMsg_withErrorShort(mContext, "Failed to create Job Post ..!");
                return;
            } else {
                if (data.getStatuscode() == 1) {
                    UIUtility.showToastMsg_withSuccessShort(mContext, "Job post created successfully");
                    prefManager.setSelectedLon("");
                    prefManager.setSelectedLat("");
                    finish();
                    return;
                }
            }
        }
    }


    private void openDatePickerDialog() {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dob_select_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final DatePicker datePicker = dialog.findViewById(R.id.datePicker);
        datePicker.setMinDate((long) (System.currentTimeMillis() + (8.64e+7)));
        TextView doneBtn = dialog.findViewById(R.id.doneBtn);


        doneBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectedEXPDate = (datePicker.getMonth() + 1) + "/" + datePicker.getDayOfMonth() + "/" + datePicker.getYear();
                String _dateStr = dateFormatFun(datePicker.getDayOfMonth() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear());
                //    etExpiryDate.setText(_dateStr);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onProgressUpdate(int percentage) {
        FLog.d(TAG, "Progress" + percentage);
    }

    @Override
    public void onError() {
        FLog.d(TAG, "Error");
    }

    @Override
    public void onFinish() {
        FLog.d(TAG, "Progress onFinish");
    }
}
