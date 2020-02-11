package com.abln.futur.activites;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.abln.futur.R;

import java.text.DateFormat;
import java.util.Date;

public class TestDateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_view);


           Date date = new Date();
          String information =   DateFormat.getDateInstance(DateFormat.FULL).format(date);

          String res[] = information.split(",");

        String day = res[0];
        String mDate = res[1];
        String finalweek =   day.substring(0,3);
        String month[] = mDate.split("\\s+");
        String finalmonth = month[1].substring(0,3);





        System.out.println("------------");
        System.out.println(finalweek+" "+month[2]+" "+finalmonth);System.out.println("-------------");


    }
}
