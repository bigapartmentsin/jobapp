package com.abln.futur.activites;

import android.os.Bundle;
import android.widget.Switch;

import com.abln.futur.R;
import com.abln.futur.common.NewBaseActivity;
import com.abln.futur.common.UIUtility;
import com.abln.futur.customViews.ViewRotation;

public class DemoImageRotation extends NewBaseActivity {
    private ViewRotation wheelMenu;

    @Override
    public int getLayoutId() {
        return R.layout.test_image_rotaion;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        wheelMenu = (ViewRotation) findViewById(R.id.imageView1);

        wheelMenu.setDivCount(18);
        wheelMenu.setWheelImage(R.drawable.ic_circle);
        wheelMenu.setAlternateTopDiv(1);

       // selectedPositionText = (TextView) findViewById(R.id.selected_position_text);
       // selectedPositionText.setText("selected: " + (wheelMenu.getSelectedPosition() + 1));

        wheelMenu.setWheelChangeListener(new ViewRotation.WheelChangeListener(){
            @Override
            public void onSelectionChange(int selectedPosition) {

              //  UIUtility.showToastMsg_withAlertInfoShort(getApplicationContext(), String.valueOf(selectedPosition+1));


                int value = selectedPosition+1;

                switch (value){
                    case 1:
                        break;
                    case 2:
                        UIUtility.showToastMsg_withAlertInfoShort(getApplicationContext(),"8");
                        break;
                    case 3:
                        UIUtility.showToastMsg_withAlertInfoShort(getApplicationContext(),"7");
                        break;
                    case 4:
                        UIUtility.showToastMsg_withAlertInfoShort(getApplicationContext(),"6");
                        break;
                    case 5:
                        UIUtility.showToastMsg_withAlertInfoShort(getApplicationContext(),"5");
                        break;
                    case 6:
                        UIUtility.showToastMsg_withAlertInfoShort(getApplicationContext(),"4");
                        break;
                    case 7:
                        UIUtility.showToastMsg_withAlertInfoShort(getApplicationContext(),"3");
                        break;
                    case 8:
                        UIUtility.showToastMsg_withAlertInfoShort(getApplicationContext(),"2");
                        break;
                    case 9:
                        UIUtility.showToastMsg_withAlertInfoShort(getApplicationContext(),"1");
                        break;
                    case 10:
                        UIUtility.showToastMsg_withAlertInfoShort(getApplicationContext(),"18");
                        break;
                    case 11:
                        UIUtility.showToastMsg_withAlertInfoShort(getApplicationContext(),"17");
                        break;
                    case 12:
                        UIUtility.showToastMsg_withAlertInfoShort(getApplicationContext(),"16");
                        break;
                    case 13:
                        UIUtility.showToastMsg_withAlertInfoShort(getApplicationContext(),"15");
                        break;
                    case 14:
                        UIUtility.showToastMsg_withAlertInfoShort(getApplicationContext(),"14");
                        break;
                    case 15:
                        UIUtility.showToastMsg_withAlertInfoShort(getApplicationContext(),"13");
                        break;
                    case 16:
                        UIUtility.showToastMsg_withAlertInfoShort(getApplicationContext(),"12");
                        break;
                    case 17:
                        UIUtility.showToastMsg_withAlertInfoShort(getApplicationContext(),"11");

                        break;
                    case 18:
                        UIUtility.showToastMsg_withAlertInfoShort(getApplicationContext(),"10");
                        break;
                    case 19:
                        break;
                    case 20:
                        break;








                }


               // selectedPositionText.setText("selected: " + (selectedPosition + 1));
            }
        });
    }
}
