package com.abln.futur;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.abln.futur.activites.BaseActivity;
import com.abln.futur.activites.DashboardActivity;
import com.abln.futur.common.BaseResponse;
import com.abln.futur.common.GlobalSingleCallback;
import com.abln.futur.common.NewBaseActivity;
import com.abln.futur.utils.BitmapUtils;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RandomAct extends NewBaseActivity {


    @BindView(R.id.jobtitle)
    TextView jobtitle;

    @BindView(R.id.companyNameTextview)
    TextView cname;


    @BindView(R.id.experience_range)
    TextView exp;


    @BindView(R.id.posteddatetv)
    TextView posteddate;




    @Override
    public int getLayoutId() {
        return R.layout.poster_thumb_nail_;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = this.getIntent().getExtras();
        String title = extras.getString("title");
        String companyname = extras.getString("comName");
        String experince = extras.getString("exper");
        String key = extras.getString("fkey");
        String pdate = extras.getString("date");
        //System.out.println("Value Data"+ key);
        jobtitle.setText(title);
        cname.setText(companyname);
        exp.setText(experince);
        posteddate.setText(pdate);
        findViewById(R.id.btn).setOnClickListener(view1 -> {


            View view = findViewById(R.id.imageWithoutFrame);
            Bitmap bitmap = BitmapUtils.getBitmapFromCacheView(view);

            updateImages(key,bitmap);


            Toast.makeText(getApplicationContext(), "Image:" + bitmap, Toast.LENGTH_LONG).show();
            Log.d("Image", "" + bitmap);

        });
    }



    private void updateImages(String key , Bitmap imageBitmap){


        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        imageBitmap.recycle();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "image_" + timeStamp;

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);

        MultipartBody.Part body = MultipartBody.Part.createFormData("s10", imageFileName, requestFile);

        Map<String, RequestBody> partMap = new HashMap<>();
        partMap.put("apikey", RequestBody.create(MediaType.parse("text/plain"), key));


        // add another part within the multipart request
        compositeDisposable.add((apiService.sendthubmnail(partMap, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<JsonObject>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {


                       finish();





                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                })));




    }
}
