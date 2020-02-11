package com.abln.futur.sweet;

import android.content.Context;

import com.abln.futur.R;
import com.pnikosis.materialishprogress.ProgressWheel;

public class ProgressHelper {
    private ProgressWheel mProgressWheel;
    private boolean mToSpin;
    private float mSpinSpeed;
    private int mBarWidth;
    private int mBarColor;
    private int mRimWidth;
    private int mRimColor;
    private boolean mIsInstantProgress;
    private float mProgressVal;
    private int mCircleRadius;

    public ProgressHelper(Context ctx) {
        mToSpin = true;
        mSpinSpeed = 0.75f;
        mBarWidth = ctx.getResources().getDimensionPixelSize(R.dimen.common_circle_width) + 1;
        mBarColor = ctx.getResources().getColor(R.color.success_stroke_color);
        mRimWidth = 0;
        mRimColor = 0x00000000;
        mIsInstantProgress = false;
        mProgressVal = -1;
        mCircleRadius = ctx.getResources().getDimensionPixelOffset(R.dimen.progress_circle_radius);
    }

    public ProgressWheel getProgressWheel() {
        return mProgressWheel;
    }

    public void setProgressWheel(ProgressWheel progressWheel) {
        mProgressWheel = progressWheel;
        updatePropsIfNeed();
    }

    private void updatePropsIfNeed() {
        if (mProgressWheel != null) {
            if (!mToSpin && mProgressWheel.isSpinning()) {
                mProgressWheel.stopSpinning();
            } else if (mToSpin && !mProgressWheel.isSpinning()) {
                mProgressWheel.spin();
            }
            if (mSpinSpeed != mProgressWheel.getSpinSpeed()) {
                mProgressWheel.setSpinSpeed(mSpinSpeed);
            }
            if (mBarWidth != mProgressWheel.getBarWidth()) {
                mProgressWheel.setBarWidth(mBarWidth);
            }
            if (mBarColor != mProgressWheel.getBarColor()) {
                mProgressWheel.setBarColor(mBarColor);
            }
            if (mRimWidth != mProgressWheel.getRimWidth()) {
                mProgressWheel.setRimWidth(mRimWidth);
            }
            if (mRimColor != mProgressWheel.getRimColor()) {
                mProgressWheel.setRimColor(mRimColor);
            }
            if (mProgressVal != mProgressWheel.getProgress()) {
                if (mIsInstantProgress) {
                    mProgressWheel.setInstantProgress(mProgressVal);
                } else {
                    mProgressWheel.setProgress(mProgressVal);
                }
            }
            if (mCircleRadius != mProgressWheel.getCircleRadius()) {
                mProgressWheel.setCircleRadius(mCircleRadius);
            }
        }
    }

    public void resetCount() {
        if (mProgressWheel != null) {
            mProgressWheel.resetCount();
        }
    }

    public boolean isSpinning() {
        return mToSpin;
    }

    public void spin() {
        mToSpin = true;
        updatePropsIfNeed();
    }

    public void stopSpinning() {
        mToSpin = false;
        updatePropsIfNeed();
    }

    public float getProgress() {
        return mProgressVal;
    }

    public void setProgress(float progress) {
        mIsInstantProgress = false;
        mProgressVal = progress;
        updatePropsIfNeed();
    }

    public void setInstantProgress(float progress) {
        mProgressVal = progress;
        mIsInstantProgress = true;
        updatePropsIfNeed();
    }

    public int getCircleRadius() {
        return mCircleRadius;
    }

    /**
     * @param circleRadius units using pixel
     **/
    public void setCircleRadius(int circleRadius) {
        mCircleRadius = circleRadius;
        updatePropsIfNeed();
    }

    public int getBarWidth() {
        return mBarWidth;
    }

    public void setBarWidth(int barWidth) {
        mBarWidth = barWidth;
        updatePropsIfNeed();
    }

    public int getBarColor() {
        return mBarColor;
    }

    public void setBarColor(int barColor) {
        mBarColor = barColor;
        updatePropsIfNeed();
    }

    public int getRimWidth() {
        return mRimWidth;
    }

    public void setRimWidth(int rimWidth) {
        mRimWidth = rimWidth;
        updatePropsIfNeed();
    }

    public int getRimColor() {
        return mRimColor;
    }

    public void setRimColor(int rimColor) {
        mRimColor = rimColor;
        updatePropsIfNeed();
    }

    public float getSpinSpeed() {
        return mSpinSpeed;
    }

    public void setSpinSpeed(float spinSpeed) {
        mSpinSpeed = spinSpeed;
        updatePropsIfNeed();
    }


    /*
    *
    *
    * 2019-12-24 12:34:13.643 18741-18741/com.abln.futur D/getContactsList: Dummy---9945495089 -- 156 -- null
2019-12-24 12:37:47.789 28433-28433/? I/[SC]PackageUtil: getContactsPkgName: com.samsung.android.contacts
2019-12-24 12:37:47.790 28433-28433/? I/[SC]PackageUtil: getContactsPkgName: com.samsung.android.contacts
2019-12-24 12:37:47.807 28433-28433/? I/[SC]PackageUtil: getContactsPkgName: com.samsung.android.contacts
2019-12-24 12:37:50.167 28223-28223/com.abln.futur D/getContactsList: 0180-0266121---0180-0266121 -- 448 -- null
2019-12-24 12:37:50.970 28223-28223/com.abln.futur D/getContactsList: 7 AM Landline---+91 80 4866 2270 -- 14228 -- null
2019-12-24 12:37:55.543 28223-28223/com.abln.futur D/getContactsList: 7AM HDFC---75270638 -- 2261 -- null
2019-12-24 12:37:55.550 28223-28223/com.abln.futur D/getContactsList: AL * dial---*321# -- 3050802 -- null
2019-12-24 12:37:55.552 28223-28223/com.abln.futur D/getContactsList: AL cricket---543212601 -- 3050800 -- null
2019-12-24 12:37:55.554 28223-28223/com.abln.futur D/getContactsList: AL entertainment---543212 -- 3050794 -- null
2019-12-24 12:37:55.556 28223-28223/com.abln.futur D/getContactsList: AL friendz chat---543216 -- 3050798 -- null
2019-12-24 12:37:55.558 28223-28223/com.abln.futur D/getContactsList: AL hello tunes---543211 -- 3050792 -- null
2019-12-24 12:37:55.560 28223-28223/com.abln.futur D/getContactsList: AL on demand---543213 -- 3050796 -- null
2019-12-24 12:37:55.562 28223-28223/com.abln.futur D/getContactsList: ASHA Gowda---09986427031 -- 3038745 -- null
2019-12-24 12:37:55.564 28223-28223/com.abln.futur D/getContactsList: Aazad---+919959954708 -- 14875 -- null
2019-12-24 12:37:55.567 28223-28223/com.abln.futur D/getContactsList: Abhinav---(812) 516-0389 -- 14304 -- null
2019-12-24 12:37:55.571 28223-28223/com.abln.futur D/getContactsList: Abhinav Kandula---(903) 240-2777 -- 13032 -- null
2019-12-24 12:37:55.576 28223-28223/com.abln.futur D/getContactsList: Abhiram---+919848194583 -- 2996392 -- null
2019-12-24 12:37:55.579 28223-28223/com.abln.futur D/getContactsList: Abhiram House Listing---+919176664903 -- 2998315 -- null
2019-12-24 12:37:55.581 28223-28223/com.abln.futur D/getContactsList: Abhishek Kumar Linkidn---07042252076 -- 3034599 -- null
2019-12-24 12:37:55.582 28223-28223/com.abln.futur D/getContactsList: Abik De---8277-404615 -- 14511 -- null
2019-12-24 12:37:55.585 28223-28223/com.abln.futur D/getContactsList: Ac Repair Guy---+919008661027 -- 2998404 -- null
2019-12-24 12:37:55.587 28223-28223/com.abln.futur D/getContactsList: Adil---+1 (267) 844-6939 -- 3032172 -- null
2019-12-24 12:37:55.589 28223-28223/com.abln.futur D/getContactsList: Adit Goud---+918197405589 -- 14559 -- content://com.android.contacts/contacts/621640/photo
2019-12-24 12:37:55.596 28223-28223/com.abln.futur D/getContactsList: Adithya tennis---+919704013204 -- 2996377 -- null
2019-12-24 12:37:55.600 28223-28223/com.abln.futur D/getContactsList: Aditya House Lis---+919963168589 -- 2995940 -- null
2019-12-24 12:37:55.605 28223-28223/com.abln.futur D/getContactsList: Afroz---+91 8792-317086 -- 13480 -- null
2019-12-24 12:37:55.612 28223-28223/com.abln.futur D/getContactsList: Ahalya Kmc---+91 98-86-486288 -- 12801 -- content://com.android.contacts/display_photo/227
2019-12-24 12:37:55.614 28223-28223/com.abln.futur D/getContactsList: Ahlam Commune Works---+919845184213 -- 3034715 -- null
2019-12-24 12:37:55.615 28223-28223/com.abln.futur D/getContactsList: Airtel Money---*400# -- 3050810 -- null
2019-12-24 12:37:55.616 28223-28223/com.abln.futur D/getContactsList: Aishwarya Nandini Friend---+1 (215) 452-9779 -- 1305 -- null
2019-12-24 12:37:55.622 28223-28223/com.abln.futur D/getContactsList: Aishwaryika Tinder---+918291744854 -- 1289 -- null
2019-12-24 12:37:55.624 28223-28223/com.abln.futur D/getContactsList: Ajay FSM---+919885166652 -- 2995820 -- null
2019-12-24 12:37:55.626 28223-28223/com.abln.futur D/getContactsList: Ajeev---+91 92066 02030 -- 1358 -- null
2019-12-24 12:37:55.633 28223-28223/com.abln.futur D/getContactsList: Ajya Itc Chef Fabbel---+91 87545 00752 -- 3025605 -- null
2019-12-24 12:37:55.635 28223-28223/com.abln.futur D/getContactsList: Akash Chail---+918801754035 -- 14965 -- content://com.android.contacts/display_photo/196
2019-12-24 12:37:55.637 28223-28223/com.abln.futur D/getContactsList: Akash Pillai---+919573188680 -- 13933 -- null
2019-12-24 12:37:55.639 28223-28223/com.abln.futur D/getContactsList: Akhil Babu---+919160640400 -- 2995361 -- null
2019-12-24 12:37:55.642 28223-28223/com.abln.futur D/getContactsList: Akka---+919246772522 -- 2996127 -- null
2019-12-24 12:37:55.644 28223-28223/com.abln.futur D/getContactsList: Akkainaudu---+91 99-48-533382 -- 13380 -- null
2019-12-24 12:37:55.646 28223-28223/com.abln.futur D/getContactsList: Alan Graphic Designer---+91 70347 25530 -- 3055856 -- null
2019-12-24 12:37:55.647 28223-28223/com.abln.futur D/getContactsList: Alex Shen---+14145220497 -- 13678 -- null
2019-12-24 12:37:55.649 28223-28223/com.abln.futur D/getContactsList: Altaf Kormangla Agent Runnr---+91 94499 33983 -- 1345 -- null
2019-12-24 12:37:55.650 28223-28223/com.abln.futur D/getContactsList: Altamash K---+919920484799 -- 2995880 -- null
2019-12-24 12:37:55.651 28223-28223/com.abln.futur D/getContactsList: Aman---+91 96-42-264793 -- 13612 -- null
2019-12-24 12:37:55.652 28223-28223/com.abln.futur D/getContactsList: Aman Bike Stunt Professional---07780224529 -- 3025559 -- null
2019-12-24 12:37:55.653 28223-28223/com.abln.futur D/getContactsList: Ame Bumble---08413939378 -- 3005693 -- null
2019-12-24 12:37:55.654 28223-28223/com.abln.futur D/getContactsList: Anand Interiors---+919704940296 -- 2995666 -- null
2019-12-24 12:37:55.654 28223-28223/com.abln.futur D/getContactsList: Anandram Garu Powersquare Company---+919886082294 -- 2213 -- null
2019-12-24 12:37:55.655 28223-28223/com.abln.futur D/getContactsList: Andhra Mess---+919448152118 -- 2996337 -- null
2019-12-24 12:37:55.656 28223-28223/com.abln.futur D/getContactsList: Angad Chopra---+91 97-03-133007 -- 225 -- null
2019-12-24 12:37:55.657 28223-28223/com.abln.futur D/getContactsList: Anish Hyderabad Chef/Consultant---+919618160508 -- 2998371 -- null
2019-12-24 12:37:55.657 28223-28223/com.abln.futur D/getContactsList: Anjali UC Cincinnati---+919167255437 -- 13997 -- null
2019-12-24 12:37:55.658 28223-28223/com.abln.futur D/getContactsList: Ankit---(814) 202-6930 -- 14025 -- null
2019-12-24 12:37:55.659 28223-28223/com.abln.futur D/getContactsList: Ankita Tanga---+919962650879 -- 15068 -- null
2019-12-24 12:37:55.660 28223-28223/com.abln.futur D/getContactsList: Anoob Spice Route---08792727602 -- 3003471 -- null
2019-12-24 12:37:55.660 28223-28223/com.abln.futur D/getContactsList: Anshuman---+919008771001 -- 2996277 -- null
2019-12-24 12:37:55.661 28223-28223/com.abln.futur D/getContactsList: Anshuman Sambhavi---+91 99-16-307113 -- 13124 -- null
2019-12-24 12:37:55.662 28223-28223/com.abln.futur D/getContactsList: Anupum Menon---+918147006161 -- 2995079 -- content://com.android.contacts/display_photo/237
2019-12-24 12:37:55.663 28223-28223/com.abln.futur D/getContactsList: Anuradha House Listing---+91 99392 63446 -- 2234 -- null
2019-12-24 12:37:55.663 28223-28223/com.abln.futur D/getContactsList: Anvit Bendre---+919327920927 -- 2996142 -- null
2019-12-24 12:37:55.665 28223-28223/com.abln.futur D/getContactsList: Apala Raju---+919866142853 -- 2996197 -- null
2019-12-24 12:37:55.665 28223-28223/com.abln.futur D/getContactsList: Appal Naidu---+91 8801-333029 -- 14448 -- null
2019-12-24 12:37:55.666 28223-28223/com.abln.futur D/getContactsList: Appal Naidu Bandari---+917660866666 -- 3000687 -- null
2019-12-24 12:37:55.667 28223-28223/com.abln.futur D/getContactsList: Apple Inc.---1-800-MY-APPLE -- 14205 -- content://com.android.contacts/display_photo/184
2019-12-24 12:37:55.667 28223-28223/com.abln.futur D/getContactsList: Arjav Shah Tennis---(732) 406-6060 -- 1007 -- null
2019-12-24 12:37:55.668 28223-28223/com.abln.futur D/getContactsList: Arjun Foodpanda---+91 88926 88964 -- 1369 -- null
2019-12-24 12:37:55.668 28223-28223/com.abln.futur D/getContactsList: Arul Chef---+91 99010 59113 -- 2998411 -- null
2019-12-24 12:37:55.669 28223-28223/com.abln.futur D/getContactsList: Arun Driver Cheffeaur---+917569434881 -- 3038294 -- null
2019-12-24 12:37:55.669 28223-28223/com.abln.futur D/getContactsList: Asad Sharif---+919440138873 -- 2996342 -- null
2019-12-24 12:37:55.670 28223-28223/com.abln.futur D/getContactsList: Ascent Fitness---+919705227444 -- 2995672 -- null
2019-12-24 12:37:55.671 28223-28223/com.abln.futur D/getContactsList: Asfton Fitness---09246823123 -- 3025283 -- null
2019-12-24 12:37:55.671 28223-28223/com.abln.futur D/getContactsList: Asish---+919535678073 -- 2995536 -- content://com.android.contacts/display_photo/232
2019-12-24 12:37:55.672 28223-28223/com.abln.futur D/getContactsList: Asish Hyd---+917093005711 -- 2994922 -- null
2019-12-24 12:37:55.673 28223-28223/com.abln.futur D/getContactsList: Asish USA---+1 (917) 312-8247 -- 15038 -- content://com.android.contacts/display_photo/220
2019-12-24 12:37:55.674 28223-28223/com.abln.futur D/getContactsList: Auto---0820-2575070 -- 13589 -- null
2019-12-24 12:37:55.675 28223-28223/com.abln.futur D/getContactsList: Avatar Sing---+91 98-71-564377 -- 14897 -- null
2019-12-24 12:37:55.675 28223-28223/com.abln.futur D/getContactsList: Avinash Surya Friend---+91 96188 69362 -- 3003321 -- null
2019-12-24 12:37:55.676 28223-28223/com.abln.futur D/getContactsList: Aviral Gupta---+919035180823 -- 2996287 -- null
2019-12-24 12:37:55.676 28223-28223/com.abln.futur D/getContactsList: Azaad New---+91 7893-454576 -- 12808 -- null
2019-12-24 12:37:55.677 28223-28223/com.abln.futur D/getContactsList: Azad---+918105005515 -- 14483 -- null
2019-12-24 12:37:55.677 28223-28223/com.abln.futur D/getContactsList: Babai C, i---+919440796058 -- 3025720 -- null
2019-12-24 12:37:55.677 28223-28223/com.abln.futur D/getContactsList: Babai C.i---+91 94-40-110668 -- 127 -- null
2019-12-24 12:37:55.679 28223-28223/com.abln.futur D/getContactsList: Babji Rao Uncle Dad Friend---+919290441338 -- 2204 -- null
2019-12-24 12:37:55.679 28223-28223/com.abln.futur D/getContactsList: Babji Sir---+919885224132 -- 2995830 -- null
2019-12-24 12:37:55.680 28223-28223/com.abln.futur D/getContactsList: Babu Manager 7 AM---+917975869394 -- 2186 -- null
2019-12-24 12:37:55.680 28223-28223/com.abln.futur D/getContactsList: Babu chef---+91 81237 58712 -- 1356 -- null
2019-12-24 12:37:55.681 28223-28223/com.abln.futur D/getContactsList: Bala---+919008745676 -- 14994 -- null
2019-12-24 12:37:55.681 28223-28223/com.abln.futur D/getContactsList: BalanceEnquiry---125 -- 13553 -- null
2019-12-24 12:37:55.682 28223-28223/com.abln.futur D/getContactsList: Barghav ( Babji Uncle Son In Law---09963543213 -- 3056930 -- null
2019-12-24 12:37:55.682 28223-28223/com.abln.futur D/getContactsList: Barghav R Icas---+919740527189 -- 13911 -- null
2019-12-24 12:37:55.682 28223-28223/com.abln.futur D/getContactsList: Baskar Gitam AO---08500464748 -- 2989674 -- null
2019-12-24 12:37:55.683 28223-28223/com.abln.futur D/getContactsList: Baskar Niranjan Lawyer Employee---+91 86868 95589 -- 3014360 -- null
2019-12-24 12:37:55.683 28223-28223/com.abln.futur D/getContactsList: Bbnl Internet Support - People S Hub---09606276442 -- 3038759 -- null
2019-12-24 12:37:55.683 28223-28223/com.abln.futur D/getContactsList: Bharat Maya---9502253335 -- 2998454 -- null
2019-12-24 12:37:55.684 28223-28223/com.abln.futur D/getContactsList: Bharat Usa---00 1 (939) 276-2647 -- 14625 -- null
2019-12-24 12:37:55.684 28223-28223/com.abln.futur D/getContactsList: Bharath Android BLR---+91 99459 91556 -- 3051113 -- null
2019-12-24 12:37:55.685 28223-28223/com.abln.futur D/getContactsList: Bharath-usa---(614) 804-3293 -- 13567 -- null
2019-12-24 12:37:55.685 28223-28223/com.abln.futur D/getContactsList: Bharatwaj---+918885284844 -- 15219 -- null
2019-12-24 12:37:55.685 28223-28223/com.abln.futur D/getContactsList: Bhavani Jsw---+919112448297 -- 1315 -- null
2019-12-24 12:37:55.686 28223-28223/com.abln.futur D/getContactsList: Bhupal R---08374-697698 -- 14787 -- null
2019-12-24 12:37:55.686 28223-28223/com.abln.futur D/getContactsList: Bobby---+91 8886-329282 -- 13408 -- null
2019-12-24 12:37:55.687 28223-28223/com.abln.futur D/getContactsList: Bokam Srinivas Pharma---09866339733 -- 3025535 -- null
2019-12-24 12:37:55.687 28223-28223/com.abln.futur D/getContactsList: Brandon---+91 7829-521092 -- 15061 -- content://com.android.contacts/display_photo/221
2019-12-24 12:37:55.687 28223-28223/com.abln.futur D/getContactsList: Brandon Us---+12672539537 -- 2999713 -- null
2019-12-24 12:37:55.688 28223-28223/com.abln.futur D/getContactsList: Bujji Uncle Dad Friend---+919396777399 -- 3045661 -- null
2019-12-24 12:37:55.688 28223-28223/com.abln.futur D/getContactsList: C R K Chatanya---+91 94-94-179087 -- 15185 -- content://com.android.contacts/display_photo/215
2019-12-24 12:37:55.689 28223-28223/com.abln.futur D/getContactsList: Cass---(984) 212-3688 -- 1106 -- null
2019-12-24 12:37:55.689 28223-28223/com.abln.futur D/getContactsList: Chaman Baid Garu---08913263096 -- 3003348 -- null
2019-12-24 12:37:55.690 28223-28223/com.abln.futur D/getContactsList: Chandan House Mate---+91 98206 68677 -- 2998528 -- null
2019-12-24 12:37:55.690 28223-28223/com.abln.futur D/getContactsList: Chatanya schoo---+918019412349 -- 13349 -- null
2019-12-24 12:37:55.691 28223-28223/com.abln.futur D/getContactsList: Chiranjeevi Praveen Stanford Healthcare---+15104568283 -- 3056983 -- null
2019-12-24 12:37:55.692 28223-28223/com.abln.futur D/getContactsList: Choudhary Garu C. I POLICE---+918555831415 -- 3025362 -- null
2019-12-24 12:37:55.692 28223-28223/com.abln.futur D/getContactsList: Chrishma Manager Hustle Hub---+919108624982 -- 3051091 -- null
2019-12-24 12:37:55.692 28223-28223/com.abln.futur D/getContactsList: Commune Co Works Manager---08880500175 -- 3034701 -- null
2019-12-24 12:37:55.692 28223-28223/com.abln.futur D/getContactsList: Conquor---+919246866779 -- 2995401 -- null
2019-12-24 12:37:55.693 28223-28223/com.abln.futur D/getContactsList: Cr1---(754) 214-7736 -- 1111 -- null
2019-12-24 12:37:55.693 28223-28223/com.abln.futur D/getContactsList: Crk---+91 99-72-036130 -- 14188 -- null
2019-12-24 12:37:55.694 28223-28223/com.abln.futur D/getContactsList: Crk Uk---+44 7440 057115 -- 13670 -- null
2019-12-24 12:37:55.694 28223-28223/com.abln.futur D/getContactsList: D Vasu---+91 96-66-796418 -- 13904 -- null
2019-12-24 12:37:55.694 28223-28223/com.abln.futur D/getContactsList: DNR Rao---+919966600688 -- 3003361 -- null
2019-12-24 12:37:55.695 28223-28223/com.abln.futur D/getContactsList: Dad---+91 98-66-021399 -- 891 -- content://com.android.contacts/display_photo/52
2019-12-24 12:37:55.695 28223-28223/com.abln.futur D/getContactsList: Dad Friend Vijay U.S---+91 99-59-299905 -- 13473 -- null
2019-12-24 12:37:55.696 28223-28223/com.abln.futur D/getContactsList: Daga---+919035111214 -- 2995326 -- content://com.android.contacts/display_photo/211
2019-12-24 12:37:55.696 28223-28223/com.abln.futur D/getContactsList: Dalnaidu---+917989119178 -- 292 -- null
2019-12-24 12:37:55.697 28223-28223/com.abln.futur D/getContactsList: Dc Ab---1338 -- 14361 -- null
2019-12-24 12:37:55.698 28223-28223/com.abln.futur D/getContactsList: Deepak Patil---07019178572 -- 3026588 -- null
2019-12-24 12:37:55.698 28223-28223/com.abln.futur D/getContactsList: Deepu Sudarshna---+918095956133 -- 2995019 -- content://com.android.contacts/display_photo/195
2019-12-24 12:37:55.699 28223-28223/com.abln.futur D/getContactsList: Demudu Tand---+919346212111 -- 2995427 -- null
2019-12-24 12:37:55.700 28223-28223/com.abln.futur D/getContactsList: Deva Atme PHP BACKEND---+91 88848 83125 -- 3055924 -- null
2019-12-24 12:37:55.700 28223-28223/com.abln.futur D/getContactsList: Dhiraj Rajaram---+91 98804 30500 -- 3050767 -- null
2019-12-24 12:37:55.700 28223-28223/com.abln.futur D/getContactsList: Dialer Tunes---56500 -- 13437 -- null
2019-12-24 12:37:55.701 28223-28223/com.abln.futur D/getContactsList: Dilip Varma---(903) 004-7685 -- 15199 -- null
2019-12-24 12:37:55.701 28223-28223/com.abln.futur D/getContactsList: Dimaan Das---+91 94-35-399179 -- 12928 -- null
2019-12-24 12:37:55.702 28223-28223/com.abln.futur D/getContactsList: Dinesh---+919035210236 -- 2996297 -- content://com.android.contacts/display_photo/233
2019-12-24 12:37:55.702 28223-28223/com.abln.futur D/getContactsList: Distress Number---112 -- 2 -- null
2019-12-24 12:37:55.702 28223-28223/com.abln.futur D/getContactsList: Divya Garu Vijayatha Hospitl Admin---08179253818 -- 3025654 -- null
2019-12-24 12:37:55.703 28223-28223/com.abln.futur D/getContactsList: Doctor Archit---+918008265366 -- 2994977 -- null
2019-12-24 12:37:55.703 28223-28223/com.abln.futur D/getContactsList: Doctor Barani---+918008263099 -- 2980059 -- null
2019-12-24 12:37:55.703 28223-28223/com.abln.futur D/getContactsList: Doctor Lok Nath---+919849113400 -- 2980042 -- null
2019-12-24 12:37:55.704 28223-28223/com.abln.futur D/getContactsList: Doctor Ram Chandra---+919848498488 -- 2980040 -- null
2019-12-24 12:37:55.704 28223-28223/com.abln.futur D/getContactsList: Doctor Sowjnya---+91 96551 32156 -- 2980129 -- null
2019-12-24 12:37:55.704 28223-28223/com.abln.futur D/getContactsList: Dr Viren Raheja Doctor---+91 98441 20113 -- 2219 -- null
2019-12-24 12:37:55.705 28223-28223/com.abln.futur D/getContactsList: Dr. Huda Khan Prague---+420722908041 -- 3056967 -- null
2019-12-24 12:37:55.706 28223-28223/com.abln.futur D/getContactsList: Dr. Kanka Raj Maya---+919390770249 -- 3059578 -- null
2019-12-24 12:37:55.706 28223-28223/com.abln.futur D/getContactsList: Dr. Neha Katar---09916912037 -- 3057216 -- null
2019-12-24 12:37:55.706 28223-28223/com.abln.futur D/getContactsList: Dr. Venkat Cousin---+1 (425) 295-1277 -- 3038778 -- null
2019-12-24 12:37:55.706 28223-28223/com.abln.futur D/getContactsList: Drexel---00 1 (215) 895-2400 -- 14744 -- null
2019-12-24 12:37:55.707 28223-28223/com.abln.futur D/getContactsList: Driver Ramana---+919177748854 -- 2996317 -- null
2019-12-24 12:37:55.707 28223-28223/com.abln.futur D/getContactsList: Droom. In Raj Shekar---+919654835600 -- 2998462 -- null
2019-12-24 12:37:55.708 28223-28223/com.abln.futur D/getContactsList: Dubai---00971554946364 -- 12780 -- null
2019-12-24 12:37:55.708 28223-28223/com.abln.futur D/getContactsList: Electrcian Shan---+91 95357 68989 -- 1371 -- null
2019-12-24 12:37:55.708 28223-28223/com.abln.futur D/getContactsList: Electrician Koramagala---+91 99006 51737 -- 1363 -- null
2019-12-24 12:37:55.709 28223-28223/com.abln.futur D/getContactsList: Esha Anshu---+91 8884-224142 -- 15377 -- null
2019-12-24 12:37:55.709 28223-28223/com.abln.futur D/getContactsList: Eswar Maya---+919293274621 -- 2996122 -- null
2019-12-24 12:37:55.710 28223-28223/com.abln.futur D/getContactsList: Evangl Stefan Ex---+91 89717 41808 -- 1330 -- null
2019-12-24 12:37:55.710 28223-28223/com.abln.futur D/getContactsList: Foodpanda Customer Care---018601213663 -- 2184 -- null
2019-12-24 12:37:55.711 28223-28223/com.abln.futur D/getContactsList: Gandhi K K Garu Patent In Cancer Research---+919912930003 -- 3025651 -- null
2019-12-24 12:37:55.711 28223-28223/com.abln.futur D/getContactsList: Ganesh Briq Medinin---+91 72592 32058 -- 2989691 -- null
2019-12-24 12:37:55.711 28223-28223/com.abln.futur D/getContactsList: Ganesh Tennis---+91 90-59-980906 -- 15046 -- null
2019-12-24 12:37:55.712 28223-28223/com.abln.futur D/getContactsList: Gaurav Datta---+91 8564-917477 -- 13844 -- null
2019-12-24 12:37:55.712 28223-28223/com.abln.futur D/getContactsList: Gayathri---+918977870624 -- 2995260 -- null
2019-12-24 12:37:55.713 28223-28223/com.abln.futur D/getContactsList: Gayatri Cousin Accenture---+918328014747 -- 3055900 -- null
2019-12-24 12:37:55.713 28223-28223/com.abln.futur D/getContactsList: Goutham---+91 94-93-888667 -- 14251 -- content://com.android.contacts/display_photo/205
2019-12-24 12:37:55.713 28223-28223/com.abln.futur D/getContactsList: Goutham Dora---(944) 071-0036 -- 12749 -- null
2019-12-24 12:37:55.714 28223-28223/com.abln.futur D/getContactsList: Goutham Spark---+919701096096 -- 12822 -- null
2019-12-24 12:37:55.715 28223-28223/com.abln.futur D/getContactsList: Gouthum Gosh---08912701307 -- 14664 -- null
2019-12-24 12:37:55.715 28223-28223/com.abln.futur D/getContactsList: Grand Parents---(897) 821-3065 -- 13889 -- null
2019-12-24 12:37:55.715 28223-28223/com.abln.futur D/getContactsList: HDFC Vizag Relationship Manager---+919704306458 -- 3052449 -- null
2019-12-24 12:37:55.716 28223-28223/com.abln.futur D/getContactsList: Hanish M---+917699475809 -- 602 -- null
2019-12-24 12:37:55.716 28223-28223/com.abln.futur D/getContactsList: Hanoku Kampa---+919866184755 -- 3000624 -- null
2019-12-24 12:37:55.716 28223-28223/com.abln.futur D/getContactsList: Hans---00 1 (210) 781-9773 -- 14290 -- content://com.android.contacts/display_photo/218
2019-12-24 12:37:55.717 28223-28223/com.abln.futur D/getContactsList: Harley Davidson Rent---+91 94-22-635717 -- 1224 -- null
2019-12-24 12:37:55.717 28223-28223/com.abln.futur D/getContactsList: Harley Rental---+918622000000 -- 2995169 -- null
2019-12-24 12:37:55.718 28223-28223/com.abln.futur D/getContactsList: Harsha Dad Friend---+919848193555 -- 2995759 -- null
2019-12-24 12:37:55.718 28223-28223/com.abln.futur D/getContactsList: Harsha House Mate---+919538417123 -- 3000616 -- null
2019-12-24 12:37:55.719 28223-28223/com.abln.futur D/getContactsList: Harsha L---+919885548319 -- 2996397 -- null
2019-12-24 12:37:55.719 28223-28223/com.abln.futur D/getContactsList: Harsha L Gorri---+919700542958 -- 2996167 -- null
2019-12-24 12:37:55.719 28223-28223/com.abln.futur D/getContactsList: Harsha Vardhan---+918284854590 -- 15083 -- null
2019-12-24 12:37:55.720 28223-28223/com.abln.futur D/getContactsList: Harshit jain---+919291243890 -- 13117 -- null
2019-12-24 12:37:55.720 28223-28223/com.abln.futur D/getContactsList: Harshith H Byju Surya Friend---09493232302 -- 3025550 -- null
2019-12-24 12:37:55.720 28223-28223/com.abln.futur D/getContactsList: Haseeb---+91 96-32-542511 -- 15356 -- null
2019-12-24 12:37:55.721 28223-28223/com.abln.futur D/getContactsList: Hassan---(810) 658-4210 -- 13451 -- null
2019-12-24 12:37:55.721 28223-28223/com.abln.futur D/getContactsList: Hassan Delhi---+91 7731-036101 -- 264 -- null
2019-12-24 12:37:55.722 28223-28223/com.abln.futur D/getContactsList: Hassen---+919666968422 -- 2995611 -- null
2019-12-24 12:37:55.722 28223-28223/com.abln.futur D/getContactsList: Hdfc Credit Card---+917729910484 -- 3000609 -- null
2019-12-24 12:37:55.723 28223-28223/com.abln.futur D/getContactsList: Hdfc P---71354750 -- 3005558 -- null
2019-12-24 12:37:55.723 28223-28223/com.abln.futur D/getContactsList: Hdfc Ramesh---+919390955566 -- 1297 -- null
2019-12-24 12:37:55.723 28223-28223/com.abln.futur D/getContactsList: Help Line---121 -- 40 -- null
2019-12-24 12:37:55.724 28223-28223/com.abln.futur D/getContactsList: Hemaraju---+919581023366 -- 14602 -- null
2019-12-24 12:37:55.724 28223-28223/com.abln.futur D/getContactsList: Hitesh---+918019208209 -- 14986 -- null
2019-12-24 12:37:55.725 28223-28223/com.abln.futur D/getContactsList: Hot Chix Manipal---0820-2575757 -- 13560 -- null
2019-12-24 12:37:55.725 28223-28223/com.abln.futur D/getContactsList: Hriday---+918374929766 -- 13925 -- null
2019-12-24 12:37:55.726 28223-28223/com.abln.futur D/getContactsList: Hsr Quality W---+91 88928 46008 -- 3026010 -- null
2019-12-24 12:37:55.726 28223-28223/com.abln.futur D/getContactsList: Hunger Box Agent---+919164322279 -- 2230 -- null
2019-12-24 12:37:55.726 28223-28223/com.abln.futur D/getContactsList: Hustlehub Co Working---+918056829251 -- 3034708 -- null
2019-12-24 12:37:55.727 28223-28223/com.abln.futur D/getContactsList: Hyderbad Adda---+918880976873 -- 2996062 -- null
2019-12-24 12:37:55.727 28223-28223/com.abln.futur D/getContactsList: I N Y Prasad Garu Babji Rao Uncle---09440623459 -- 3056986 -- null
2019-12-24 12:37:55.728 28223-28223/com.abln.futur D/getContactsList: ID---358057/08/100864/2,0 -- 8 -- null
2019-12-24 12:37:55.728 28223-28223/com.abln.futur D/getContactsList: Icas Direcror---+919880105868 -- 2995815 -- null
2019-12-24 12:37:55.728 28223-28223/com.abln.futur D/getContactsList: Icas Of---0820-2924026 -- 13334 -- null
2019-12-24 12:37:55.729 28223-28223/com.abln.futur D/getContactsList: Icici Id---532-472809 -- 822 -- null
2019-12-24 12:37:55.729 28223-28223/com.abln.futur D/getContactsList: Imitaz Swiggy---+91 99596 04243 -- 3001032 -- null
2019-12-24 12:37:55.729 28223-28223/com.abln.futur D/getContactsList: Imran Fridge Fix---+919740455973 -- 2995687 -- null
2019-12-24 12:37:55.730 28223-28223/com.abln.futur D/getContactsList: Imtiaz ✌️🍀 Water Tank---+91 99861 66837 -- 2180 -- null
2019-12-24 12:37:55.731 28223-28223/com.abln.futur D/getContactsList: Innokios Kiosk Machine Nithyanand---09845366995 -- 3005683 -- null
2019-12-24 12:37:55.731 28223-28223/com.abln.futur D/getContactsList: Instanthelp 24X7---*121# -- 3050808 -- null
2019-12-24 12:37:55.731 28223-28223/com.abln.futur D/getContactsList: Internet---*122*1400# -- 15090 -- null
2019-12-24 12:37:55.731 28223-28223/com.abln.futur D/getContactsList: Jade Tinder---(910) 300-9521 -- 1130 -- null
2019-12-24 12:37:55.732 28223-28223/com.abln.futur D/getContactsList: Jaffa's Biriyani Stuff---+91 90660 03945 -- 3025997 -- null
2019-12-24 12:37:55.732 28223-28223/com.abln.futur D/getContactsList: Jagannadh---+918121288877 -- 15125 -- null
2019-12-24 12:37:55.732 28223-28223/com.abln.futur D/getContactsList: Jaggi---+919008773672 -- 2995270 -- null
2019-12-24 12:37:55.733 28223-28223/com.abln.futur D/getContactsList: Jaya Maya Doctor---+919390028859 -- 2243 -- null
2019-12-24 12:37:55.733 28223-28223/com.abln.futur D/getContactsList: Jayanth Land Rover---+91 90004 20701 -- 2985733 -- null
2019-12-24 12:37:55.733 28223-28223/com.abln.futur D/getContactsList: Jayender Sai---+919885862595 -- 2996402 -- null
2019-12-24 12:37:55.734 28223-28223/com.abln.futur D/getContactsList: Jonny Johnathan---+1 (443) 433-2994 -- 3053354 -- null
2019-12-24 12:37:55.734 28223-28223/com.abln.futur D/getContactsList: Joshi Co-working HSR---08197648374 -- 3038711 -- null
2019-12-24 12:37:55.735 28223-28223/com.abln.futur D/getContactsList: Joshna Reddy---+919502118376 -- 2996357 -- content://com.android.contacts/contacts/621793/photo
2019-12-24 12:37:55.735 28223-28223/com.abln.futur D/getContactsList: Kalangi New---+91 99-59-437667 -- 13239 -- null
2019-12-24 12:37:55.735 28223-28223/com.abln.futur D/getContactsList: Kalyan Android Hcl---+919493907683 -- 3055888 -- null
2019-12-24 12:37:55.735 28223-28223/com.abln.futur D/getContactsList: Kalyan Brother---+91 99491 74890 -- 3055880 -- null
2019-12-24 12:37:55.736 28223-28223/com.abln.futur D/getContactsList: Kalyan Garu - Babji Rao Garu---+918523073889 -- 3026189 -- null
2019-12-24 12:37:55.736 28223-28223/com.abln.futur D/getContactsList: Kamal Garu Acn Healthcare Catering Vizag Radha Krishna---+919845099851 -- 2995739 -- null
2019-12-24 12:37:55.736 28223-28223/com.abln.futur D/getContactsList: Kamesh Garu Hyd Babji Roa Friend---+919440051980 -- 3057033 -- null
2019-12-24 12:37:55.737 28223-28223/com.abln.futur D/getContactsList: Kamlesh Motive Prime Pvt Ltd---+91 90712 17390 -- 15320 -- null
2019-12-24 12:37:55.737 28223-28223/com.abln.futur D/getContactsList: Kannam Naidu Garu---+91 79-89761657 -- 1325 -- null
2019-12-24 12:37:55.737 28223-28223/com.abln.futur D/getContactsList: Karan Soni---+917898773790 -- 13858 -- null
2019-12-24 12:37:55.738 28223-28223/com.abln.futur D/getContactsList: Kasu Pini---+919032340199 -- 12735 -- null
2019-12-24 12:37:55.738 28223-28223/com.abln.futur D/getContactsList: Kathir chef---+91 98803 53406 -- 1365 -- null
2019-12-24 12:37:55.739 28223-28223/com.abln.futur D/getContactsList: Kelsey Tinder---(760) 582-0430 -- 1128 -- null
2019-12-24 12:37:55.739 28223-28223/com.abln.futur D/getContactsList: Kent sajid---+91 98446 00701 -- 2176 -- null
2019-12-24 12:37:55.739 28223-28223/com.abln.futur D/getContactsList: Keshav---+919035137610 -- 2996312 -- content://com.android.contacts/display_photo/212
2019-12-24 12:37:55.740 28223-28223/com.abln.futur D/getContactsList: Kimidi---08913252330 -- 13131 -- content://com.android.contacts/display_photo/214
2019-12-24 12:37:55.740 28223-28223/com.abln.futur D/getContactsList: Kiran Gutta Bandari Friend---07032905388 -- 3005597 -- null
2019-12-24 12:37:55.740 28223-28223/com.abln.futur D/getContactsList: Kiran Tennis---+91 8897-323364 -- 13693 -- null
2019-12-24 12:37:55.740 28223-28223/com.abln.futur D/getContactsList: Kormangla 7 AM Owner---+919845609481 -- 2194 -- null
2019-12-24 12:37:55.741 28223-28223/com.abln.futur D/getContactsList: Koushik---+91 7411-240565 -- 15454 -- null
2019-12-24 12:37:55.741 28223-28223/com.abln.futur D/getContactsList: Koushik Vijaywada---+91 8143-432679 -- 15468 -- null
2019-12-24 12:37:55.741 28223-28223/com.abln.futur D/getContactsList: Kr Rohit---+918978722339 -- 14282 -- null
2019-12-24 12:37:55.742 28223-28223/com.abln.futur D/getContactsList: Krishanu---+91 90-08-752773 -- 14588 -- null
2019-12-24 12:37:55.742 28223-28223/com.abln.futur D/getContactsList: Krishna Mohan IBM DR. KANKA RAJ---+918309539410 -- 3059585 -- null
2019-12-24 12:37:55.742 28223-28223/com.abln.futur D/getContactsList: Krishna Saloon---+919494973364 -- 2995516 -- null
2019-12-24 12:37:55.743 28223-28223/com.abln.futur D/getContactsList: Krithi---+919951538882 -- 2995925 -- content://com.android.contacts/display_photo/222
2019-12-24 12:37:55.743 28223-28223/com.abln.futur D/getContactsList: Kunal---+918884294020 -- 2996257 -- content://com.android.contacts/display_photo/242
2019-12-24 12:37:55.744 28223-28223/com.abln.futur D/getContactsList: Kushal---+919966236211 -- 2995950 -- null
2019-12-24 12:37:55.744 28223-28223/com.abln.futur D/getContactsList: L Bumble---09591177373 -- 3005616 -- null
2019-12-24 12:37:55.744 28223-28223/com.abln.futur D/getContactsList: Lahari P---+919948841212 -- 2995900 -- null
2019-12-24 12:37:55.744 28223-28223/com.abln.futur D/getContactsList: Latest Scheme---1213 -- 14808 -- null
2019-12-24 12:37:55.745 28223-28223/com.abln.futur D/getContactsList: Laure Tinder---(940) 567-4015 -- 1095 -- null
2019-12-24 12:37:55.745 28223-28223/com.abln.futur D/getContactsList: Lee---(917) 826-1506 -- 951 -- null
2019-12-24 12:37:55.745 28223-28223/com.abln.futur D/getContactsList: Lnta Siddaraj---+919743060741 -- 2995713 -- null
2019-12-24 12:37:55.746 28223-28223/com.abln.futur D/getContactsList: MSG91---1800-1212-911 -- 3034657 -- content://com.android.contacts/display_photo/188
2019-12-24 12:37:55.746 28223-28223/com.abln.futur D/getContactsList: Madhav Varma Makemyclinic---+919866698777 -- 2225 -- null
2019-12-24 12:37:55.746 28223-28223/com.abln.futur D/getContactsList: Mahesh Hiremath MACHINE LEARNING---+919742936555 -- 3000650 -- null
2019-12-24 12:37:55.746 28223-28223/com.abln.futur D/getContactsList: Maid Banglore---+918123783029 -- 2995059 -- null
2019-12-24 12:37:55.747 28223-28223/com.abln.futur D/getContactsList: Maid Sabina Bommanahalli---+91 73381 67260 -- 3003440 -- null
2019-12-24 12:37:55.747 28223-28223/com.abln.futur D/getContactsList: Malli Garu---+919449493302 -- 2995494 -- null
2019-12-24 12:37:55.747 28223-28223/com.abln.futur D/getContactsList: Manas---+919777428297 -- 2996387 -- null
2019-12-24 12:37:55.748 28223-28223/com.abln.futur D/getContactsList: Manikandan Andr---09880600490 -- 3051100 -- null
2019-12-24 12:37:55.748 28223-28223/com.abln.futur D/getContactsList: Manish Varma---+91 8885-885555 -- 14004 -- null
2019-12-24 12:37:55.748 28223-28223/com.abln.futur D/getContactsList: Manisha---+919866840063 -- 15313 -- null
2019-12-24 12:37:55.749 28223-28223/com.abln.futur D/getContactsList: Manit Sabhral---+919731569732 -- 13947 -- null
2019-12-24 12:37:55.749 28223-28223/com.abln.futur D/getContactsList: Manit U.S---+12155126654 -- 2999668 -- null
2019-12-24 12:37:55.749 28223-28223/com.abln.futur D/getContactsList: Manjunath Kiosk Manufacturers---08095851597 -- 3010258 -- null
2019-12-24 12:37:55.749 28223-28223/com.abln.futur D/getContactsList: Manoj Kumar Garage Listing---+919945665447 -- 2995890 -- null
2019-12-24 12:37:55.750 28223-28223/com.abln.futur D/getContactsList: Marta Tinder---(814) 240-6102 -- 1178 -- null
2019-12-24 12:37:55.750 28223-28223/com.abln.futur D/getContactsList: Martin---+918106111000 -- 2996032 -- content://com.android.contacts/display_photo/239
2019-12-24 12:37:55.750 28223-28223/com.abln.futur D/getContactsList: Martin New---+917730808000 -- 2994937 -- null
2019-12-24 12:37:55.751 28223-28223/com.abln.futur D/getContactsList: Maurya---+917893019237 -- 2996027 -- null
2019-12-24 12:37:55.752 28223-28223/com.abln.futur D/getContactsList: Maya P---+91 8179-524902 -- 699 -- null
2019-12-24 12:37:55.753 28223-28223/com.abln.futur D/getContactsList: Mayank---+91 8143-368716 -- 14765 -- null
2019-12-24 12:37:55.753 28223-28223/com.abln.futur D/getContactsList: Mayank Gupta---(930) 326-2838 -- 13097 -- null
2019-12-24 12:37:55.754 28223-28223/com.abln.futur D/getContactsList: Mayank Sing---+918147005222 -- 2996047 -- null
2019-12-24 12:37:55.754 28223-28223/com.abln.futur D/getContactsList: Medinin Hdfc Mom---112944634 -- 2993600 -- null
2019-12-24 12:37:55.754 28223-28223/com.abln.futur D/getContactsList: Meghana Killi---+918609674363 -- 13715 -- content://com.android.contacts/display_photo/231
2019-12-24 12:37:55.755 28223-28223/com.abln.futur D/getContactsList: Micheal Uber---+91 89717 91413 -- 2211 -- null
2019-12-24 12:37:55.755 28223-28223/com.abln.futur D/getContactsList: Mithul PIRSQ---+91 90358 27347 -- 1361 -- null
2019-12-24 12:37:55.756 28223-28223/com.abln.futur D/getContactsList: Mithun---+919912719933 -- 2995870 -- null
2019-12-24 12:37:55.756 28223-28223/com.abln.futur D/getContactsList: Mo Weed---+1 (973) 525-7928 -- 3003176 -- null
2019-12-24 12:37:55.756 28223-28223/com.abln.futur D/getContactsList: Mohan House Owner---+91 98457 35350 -- 1332 -- null
2019-12-24 12:37:55.757 28223-28223/com.abln.futur D/getContactsList: Mohan New---+91 96762 98989 -- 14196 -- content://com.android.contacts/display_photo/208
2019-12-24 12:37:55.758 28223-28223/com.abln.futur D/getContactsList: Mohit Manopathra---+919742704882 -- 13232 -- content://com.android.contacts/display_photo/200
2019-12-24 12:37:55.758 28223-28223/com.abln.futur D/getContactsList: Mom---+91 97-01-089314 -- 3053396 -- null
2019-12-24 12:37:55.758 28223-28223/com.abln.futur D/getContactsList: Monique---+1 (617) 894-7741 -- 3026151 -- null
2019-12-24 12:37:55.758 28223-28223/com.abln.futur D/getContactsList: Monoj---+919035187281 -- 2996292 -- null
2019-12-24 12:37:55.759 28223-28223/com.abln.futur D/getContactsList: Mouli---+919966660404 -- 1337 -- null
2019-12-24 12:37:55.759 28223-28223/com.abln.futur D/getContactsList: Mummy---+91 7386-490001 -- 12949 -- content://com.android.contacts/display_photo/197
2019-12-24 12:37:55.759 28223-28223/com.abln.futur D/getContactsList: Munna Amith---(897) 754-0986 -- 14610 -- null
2019-12-24 12:37:55.760 28223-28223/com.abln.futur D/getContactsList: Muthu 7AM---+91 97896 87274 -- 2985807 -- null
2019-12-24 12:37:55.760 28223-28223/com.abln.futur D/getContactsList: My offer---12131 -- 3050806 -- null
2019-12-24 12:37:55.760 28223-28223/com.abln.futur D/getContactsList: Nag Driver---+91 96-40-321376 -- 14708 -- null
2019-12-24 12:37:55.761 28223-28223/com.abln.futur D/getContactsList: Nagaraj Hsr Electrician---+919743833205 -- 2998535 -- null
2019-12-24 12:37:55.761 28223-28223/com.abln.futur D/getContactsList: Nagendra Assist---+918801771255 -- 12984 -- null
2019-12-24 12:37:55.761 28223-28223/com.abln.futur D/getContactsList: Naidu Jaguar Land Rover Vijaywada---+918919718522 -- 3001054 -- null
2019-12-24 12:37:55.762 28223-28223/com.abln.futur D/getContactsList: Naidu Maya Tandoorinn Takeaway---+917136149926 -- 2980012 -- null
2019-12-24 12:37:55.762 28223-28223/com.abln.futur D/getContactsList: Naidu Muthyala Home---+919490773604 -- 3057221 -- null
2019-12-24 12:37:55.762 28223-28223/com.abln.futur D/getContactsList: Naman Kushal---+91 8884-854525 -- 13306 -- null
2019-12-24 12:37:55.762 28223-28223/com.abln.futur D/getContactsList: Nan Photon---+919246426624 -- 2996332 -- null
2019-12-24 12:37:55.763 28223-28223/com.abln.futur D/getContactsList: Nancy Sameul---+91 97413 02222 -- 39813 -- null
2019-12-24 12:37:55.763 28223-28223/com.abln.futur D/getContactsList: Nandi Fri---+91 8790-163495 -- 12879 -- null
2019-12-24 12:37:55.764 28223-28223/com.abln.futur D/getContactsList: Nandini Veda---+91 90-00-681471 -- 13327 -- content://com.android.contacts/display_photo/181
2019-12-24 12:37:55.764 28223-28223/com.abln.futur D/getContactsList: Nandu Spiceroute Legal---+91 80959 99770 -- 3003454 -- null
2019-12-24 12:37:55.764 28223-28223/com.abln.futur D/getContactsList: Nanduni Friend---+917207231606 -- 15206 -- null
2019-12-24 12:37:55.764 28223-28223/com.abln.futur D/getContactsList: Narayana- Murthy Garu Assistant---+918912552200 -- 2989673 -- null
2019-12-24 12:37:55.765 28223-28223/com.abln.futur D/getContactsList: Naren---+91 8142-325072 -- 13145 -- null
2019-12-24 12:37:55.765 28223-28223/com.abln.futur D/getContactsList: Narendra Assist---+919441482702 -- 2995484 -- null
2019-12-24 12:37:55.766 28223-28223/com.abln.futur D/getContactsList: Nationals U-19---(944) 168-3390 -- 12900 -- null
2019-12-24 12:37:55.766 28223-28223/com.abln.futur D/getContactsList: Nehal---+919392889789 -- 12921 -- content://com.android.contacts/display_photo/243
2019-12-24 12:37:55.767 28223-28223/com.abln.futur D/getContactsList: Net Connect---(939) 059-3523 -- 14729 -- null
2019-12-24 12:37:55.767 28223-28223/com.abln.futur D/getContactsList: Nidhi Bangalore---+919686448496 -- 2996002 -- null
2019-12-24 12:37:55.767 28223-28223/com.abln.futur D/getContactsList: Niharika Tennis---+918123898065 -- 2996042 -- null
2019-12-24 12:37:55.767 28223-28223/com.abln.futur D/getContactsList: Nikhil Stayzilla Ux---+91 88677 69666 -- 2993602 -- null
2019-12-24 12:37:55.768 28223-28223/com.abln.futur D/getContactsList: Nileash Grandfather Ass---+91 94-89-611170 -- 13582 -- null
2019-12-24 12:37:55.768 28223-28223/com.abln.futur D/getContactsList: Nilesh---+91 8861-667723 -- 14528 -- content://com.android.contacts/display_photo/207
2019-12-24 12:37:55.769 28223-28223/com.abln.futur D/getContactsList: Niranjan Sir Lawyer---+91 98481 97588 -- 3003418 -- null
2019-12-24 12:37:55.769 28223-28223/com.abln.futur D/getContactsList: Nisar Visual Connections---+918951613234 -- 3034078 -- null
2019-12-24 12:37:55.769 28223-28223/com.abln.futur D/getContactsList: Niserg---+91 97-31-569817 -- 13626 -- null
2019-12-24 12:37:55.770 28223-28223/com.abln.futur D/getContactsList: Nishad Jr---+91 8550-995800 -- 13596 -- null
2019-12-24 12:37:55.770 28223-28223/com.abln.futur D/getContactsList: Nishant Bonda---(738) 227-5328 -- 14490 -- null
2019-12-24 12:37:55.770 28223-28223/com.abln.futur D/getContactsList: Nishant Mudkhe---+91 77024 86930 -- 3059693 -- content://com.android.contacts/display_photo/228
2019-12-24 12:37:55.771 28223-28223/com.abln.futur D/getContactsList: Nithin---+918801333458 -- 14836 -- null
2019-12-24 12:37:55.772 28223-28223/com.abln.futur D/getContactsList: Nizam Restaurant Sayed---+91 90361 65022 -- 3000712 -- null
2019-12-24 12:37:55.772 28223-28223/com.abln.futur D/getContactsList: Npn Hdfc---0528 -- 3053427 -- null
2019-12-24 12:37:55.772 28223-28223/com.abln.futur D/getContactsList: Ojes C---+918123340276 -- 2995054 -- null
2019-12-24 12:37:55.772 28223-28223/com.abln.futur D/getContactsList: Opel Ramesh Uncle---+919985123346 -- 2989651 -- null
2019-12-24 12:37:55.773 28223-28223/com.abln.futur D/getContactsList: Pallavi---+919663700122 -- 2995596 -- null
2019-12-24 12:37:55.773 28223-28223/com.abln.futur D/getContactsList: Pandu---(817) 910-8558 -- 13836 -- null
2019-12-24 12:37:55.773 28223-28223/com.abln.futur D/getContactsList: Pankaj ATAL INCUBATION---+919971182364 -- 3025810 -- null
2019-12-24 12:37:55.773 28223-28223/com.abln.futur D/getContactsList: Pankaj Apollo General Manager---09849640258 -- 3025475 -- null
2019-12-24 12:37:55.774 28223-28223/com.abln.futur D/getContactsList: Pankaj ✌️ 🍀 Weed---+91 88928 17378 -- 2188 -- null
2019-12-24 12:37:55.774 28223-28223/com.abln.futur D/getContactsList: Park Gym---+91 99-66-077468 -- 12836 -- null
2019-12-24 12:37:55.774 28223-28223/com.abln.futur D/getContactsList: Pavan---+91 97899 09109 -- 2980033 -- null
2019-12-24 12:37:55.775 28223-28223/com.abln.futur D/getContactsList: Phani H---+1 (217) 381-5958 -- 956 -- content://com.android.contacts/contacts/622116/photo
2019-12-24 12:37:55.776 28223-28223/com.abln.futur D/getContactsList: Phani Uncle Iron---+919666999234 -- 3051107 -- null
2019-12-24 12:37:55.776 28223-28223/com.abln.futur D/getContactsList: Photon 2---+919246626624 -- 2996117 -- null
2019-12-24 12:37:55.776 28223-28223/com.abln.futur D/getContactsList: Photon Subash---+918121638777 -- 2996037 -- null
2019-12-24 12:37:55.777 28223-28223/com.abln.futur D/getContactsList: Pichi Vamsi---(964) 212-1992 -- 12742 -- null
2019-12-24 12:37:55.777 28223-28223/com.abln.futur D/getContactsList: Piyush House Listing---+917080141127 -- 2238 -- null
2019-12-24 12:37:55.777 28223-28223/com.abln.futur D/getContactsList: Plumber Ramakri---+919901801514 -- 3034597 -- null
2019-12-24 12:37:55.778 28223-28223/com.abln.futur D/getContactsList: Plumber Salim HSR---+91 98805 20753 -- 3025256 -- null
2019-12-24 12:37:55.778 28223-28223/com.abln.futur D/getContactsList: Poornima Hustle Hub---09108193602 -- 3051083 -- null
2019-12-24 12:37:55.778 28223-28223/com.abln.futur D/getContactsList: Pr---+919416677989 -- 2996152 -- null
2019-12-24 12:37:55.778 28223-28223/com.abln.futur D/getContactsList: Prabhakar Km Bro---+918977752225 -- 13524 -- null
2019-12-24 12:37:55.779 28223-28223/com.abln.futur D/getContactsList: Prabu Arts History Job Seeking---08489043498 -- 3010307 -- null
2019-12-24 12:37:55.779 28223-28223/com.abln.futur D/getContactsList: Pradeep Jai Script Writer---+91 78291 03202 -- 3000795 -- null
2019-12-24 12:37:55.779 28223-28223/com.abln.futur D/getContactsList: Pradeep Sra---+919885082374 -- 2996212 -- null
2019-12-24 12:37:55.779 28223-28223/com.abln.futur D/getContactsList: Pradeep Tattoo---+919019918800 -- 3000774 -- null
2019-12-24 12:37:55.780 28223-28223/com.abln.futur D/getContactsList: Prakash Pawar---+919822999990 -- 2995728 -- null
2019-12-24 12:37:55.780 28223-28223/com.abln.futur D/getContactsList: Prakesh Rao Treebo---+91 97393 68095 -- 3000719 -- null
2019-12-24 12:37:55.780 28223-28223/com.abln.futur D/getContactsList: Prasad Ios---+91 80882 41903 -- 3057026 -- null
2019-12-24 12:37:55.781 28223-28223/com.abln.futur D/getContactsList: Prashant reddy---+91 90-32-779999 -- 14773 -- null
2019-12-24 12:37:55.781 28223-28223/com.abln.futur D/getContactsList: Pratap Director Babji Rao Friend---09246639409 -- 3056976 -- null
2019-12-24 12:37:55.781 28223-28223/com.abln.futur D/getContactsList: Pratap Domain---+918333033333 -- 2995114 -- null
2019-12-24 12:37:55.782 28223-28223/com.abln.futur D/getContactsList: Prateek---08912755177 -- 15141 -- null
2019-12-24 12:37:55.782 28223-28223/com.abln.futur D/getContactsList: Prathiban Golden---+91 97380 12897 -- 3000705 -- null
2019-12-24 12:37:55.782 28223-28223/com.abln.futur D/getContactsList: Praveen Icici---+919701365647 -- 2995646 -- null
2019-12-24 12:37:55.783 28223-28223/com.abln.futur D/getContactsList: Preanthi---+919573769004 -- 2995987 -- null
2019-12-24 12:37:55.783 28223-28223/com.abln.futur D/getContactsList: Preethi---+91 96 66 822229 -- 571 -- null
2019-12-24 12:37:55.784 28223-28223/com.abln.futur D/getContactsList: Preeti Tinder---+919148032093 -- 2985031 -- null
2019-12-24 12:37:55.784 28223-28223/com.abln.futur D/getContactsList: Prem Raj---(801) 991-4291 -- 15228 -- null
2019-12-24 12:37:55.784 28223-28223/com.abln.futur D/getContactsList: Prithvi Voda---+919160927372 -- 13167 -- null
2019-12-24 12:37:55.785 28223-28223/com.abln.futur D/getContactsList: Priyanka---+919008776049 -- 2996102 -- null
2019-12-24 12:37:55.785 28223-28223/com.abln.futur D/getContactsList: Priyanka Karekar---(215) 512-2640 -- 13990 -- content://com.android.contacts/display_photo/225
2019-12-24 12:37:55.785 28223-28223/com.abln.futur D/getContactsList: Pruthvi Anna---+919703603601 -- 2995651 -- null
2019-12-24 12:37:55.786 28223-28223/com.abln.futur D/getContactsList: Pruthvi Raj---+91 70130 45330 -- 3025381 -- null
2019-12-24 12:37:55.786 28223-28223/com.abln.futur D/getContactsList: Pruvthi haas---+919849850567 -- 2995789 -- null
2019-12-24 12:37:55.787 28223-28223/com.abln.futur D/getContactsList: Ptr Promodh---+918500343635 -- 13430 -- null
2019-12-24 12:37:55.787 28223-28223/com.abln.futur D/getContactsList: Punam Pixel Coders---+91 78991 85102 -- 3000806 -- null
2019-12-24 12:37:55.787 28223-28223/com.abln.futur D/getContactsList: Puneet Msoe---+919620049919 -- 15169 -- null
2019-12-24 12:37:55.787 28223-28223/com.abln.futur D/getContactsList: RK uncle---07702229696 -- 2998590 -- null
2019-12-24 12:37:55.788 28223-28223/com.abln.futur D/getContactsList: Radhakrishna---+918106711249 -- 14354 -- null
2019-12-24 12:37:55.788 28223-28223/com.abln.futur D/getContactsList: Radhika School---+919502595824 -- 13365 -- null
2019-12-24 12:37:55.788 28223-28223/com.abln.futur D/getContactsList: Raghav Raju---+919986567090 -- 15097 -- null
2019-12-24 12:37:55.789 28223-28223/com.abln.futur D/getContactsList: Raghavendra- Visual Connections---+919741607573 -- 2985714 -- null
2019-12-24 12:37:55.789 28223-28223/com.abln.futur D/getContactsList: Raghu Vamsi Belfrics Blockchain---+919916055322 -- 3005672 -- null
2019-12-24 12:37:55.789 28223-28223/com.abln.futur D/getContactsList: Rahim Hustle Hub---09148978118 -- 3051145 -- null
2019-12-24 12:37:55.790 28223-28223/com.abln.futur D/getContactsList: Rahul Das---+919885177138 -- 2995825 -- null
2019-12-24 12:37:55.790 28223-28223/com.abln.futur D/getContactsList: Rahul Friend Hammer Fitness---07760042270 -- 3025929 -- null
2019-12-24 12:37:55.790 28223-28223/com.abln.futur D/getContactsList: Rahul Redekar---07892396348 -- 3026591 -- null
2019-12-24 12:37:55.790 28223-28223/com.abln.futur D/getContactsList: Rahul Somani---+919010195566 -- 12787 -- null
2019-12-24 12:37:55.791 28223-28223/com.abln.futur D/getContactsList: Rahul Varma---(944) 062-0323 -- 14822 -- null
2019-12-24 12:37:55.791 28223-28223/com.abln.futur D/getContactsList: Rajeev Foodpanda---+91 98997 30034 -- 2227 -- null
2019-12-24 12:37:55.791 28223-28223/com.abln.futur D/getContactsList: Rajesh Home---+91 70362 96094 -- 3024818 -- null
2019-12-24 12:37:55.792 28223-28223/com.abln.futur D/getContactsList: Rajiv Business---+91 94-93-306060 -- 13025 -- null
2019-12-24 12:37:55.792 28223-28223/com.abln.futur D/getContactsList: Rajkanya Cowrks- Chandan---+919706343403 -- 3034608 -- null
2019-12-24 12:37:55.792 28223-28223/com.abln.futur D/getContactsList: Rajkumar 3d Holo---+919177394622 -- 2206 -- null
2019-12-24 12:37:55.793 28223-28223/com.abln.futur D/getContactsList: Raju---(994) 804-7904 -- 14011 -- null
2019-12-24 12:37:55.793 28223-28223/com.abln.futur D/getContactsList: Raju Kirchen Ware---+919666649682 -- 1283 -- null
2019-12-24 12:37:55.793 28223-28223/com.abln.futur D/getContactsList: Raju M Chauffeur---09701412849 -- 3003334 -- null
2019-12-24 12:37:55.794 28223-28223/com.abln.futur D/getContactsList: Ram Tej---+919441013708 -- 15031 -- null
2019-12-24 12:37:55.794 28223-28223/com.abln.futur D/getContactsList: Rama---(800) 820-9611 -- 14347 -- content://com.android.contacts/display_photo/238
2019-12-24 12:37:55.794 28223-28223/com.abln.futur D/getContactsList: Ramesh sir---+918341084060 -- 13255 -- null
2019-12-24 12:37:55.795 28223-28223/com.abln.futur D/getContactsList: Ramji---+917676565656 -- 1327 -- null
2019-12-24 12:37:55.795 28223-28223/com.abln.futur D/getContactsList: Rashid---+918523827866 -- 2995159 -- null
2019-12-24 12:37:55.795 28223-28223/com.abln.futur D/getContactsList: Ratnam---(703) 334-6103 -- 935 -- null
2019-12-24 12:37:55.796 28223-28223/com.abln.futur D/getContactsList: Ravi Garu Jsw---+917719897999 -- 3000563 -- null
2019-12-24 12:37:55.796 28223-28223/com.abln.futur D/getContactsList: Ravi Jsw---+919949170600 -- 2995905 -- null
2019-12-24 12:37:55.796 28223-28223/com.abln.futur D/getContactsList: Ravi Prakash---+919448215078 -- 2995489 -- null
2019-12-24 12:37:55.797 28223-28223/com.abln.futur D/getContactsList: Ravi Teja Lawyer Nirajan Son---09000450004 -- 3005638 -- null
2019-12-24 12:37:55.797 28223-28223/com.abln.futur D/getContactsList: Raymonde Tinder---(765) 303-4301 -- 1142 -- null
2019-12-24 12:37:55.797 28223-28223/com.abln.futur D/getContactsList: Raynold---+1 (215) 954-7674 -- 3026148 -- null
2019-12-24 12:37:55.797 28223-28223/com.abln.futur D/getContactsList: Recharge---123 -- 14780 -- null
2019-12-24 12:37:55.797 28223-28223/com.abln.futur D/getContactsList: RechargeMobile---124 -- 12956 -- null
2019-12-24 12:37:55.798 28223-28223/com.abln.futur D/getContactsList: Reetu---(703) 589-7030 -- 1051 -- null
2019-12-24 12:37:55.798 28223-28223/com.abln.futur D/getContactsList: Renuka Boni---+918501033549 -- 13196 -- null
2019-12-24 12:37:55.798 28223-28223/com.abln.futur D/getContactsList: Reshib---+91 93-13-879986 -- 15349 -- content://com.android.contacts/display_photo/219
2019-12-24 12:37:55.799 28223-28223/com.abln.futur D/getContactsList: Rishab Interiors---+918328415235 -- 37978 -- null
2019-12-24 12:37:55.799 28223-28223/com.abln.futur D/getContactsList: Ritesh---+918867614000 -- 13160 -- content://com.android.contacts/display_photo/216
2019-12-24 12:37:55.799 28223-28223/com.abln.futur D/getContactsList: Rohan---+919740569061 -- 2995697 -- null
2019-12-24 12:37:55.800 28223-28223/com.abln.futur D/getContactsList: Rohit---+919059559184 -- 2995341 -- null
2019-12-24 12:37:55.800 28223-28223/com.abln.futur D/getContactsList: Rohit Kimidi---+91 92-97-452948 -- 13686 -- content://com.android.contacts/display_photo/226
2019-12-24 12:37:55.801 28223-28223/com.abln.futur D/getContactsList: Rohit Tennis---+918971903251 -- 14112 -- null
2019-12-24 12:37:55.801 28223-28223/com.abln.futur D/getContactsList: Rolex Munawar Manager---+91 99011 27243 -- 2232 -- null
2019-12-24 12:37:55.801 28223-28223/com.abln.futur D/getContactsList: Roshini Rajendra---+917624840550 -- 3026654 -- null
2019-12-24 12:37:55.802 28223-28223/com.abln.futur D/getContactsList: Rp Nagar Weed---+919886839413 -- 2996407 -- null
2019-12-24 12:37:55.802 28223-28223/com.abln.futur D/getContactsList: Runnr Agent---+917022544584 -- 1343 -- null
2019-12-24 12:37:55.802 28223-28223/com.abln.futur D/getContactsList: Rupa G---+919902009997 -- 2192 -- null
2019-12-24 12:37:55.803 28223-28223/com.abln.futur D/getContactsList: S P---+91 96-76-636699 -- 13787 -- null
2019-12-24 12:37:55.803 28223-28223/com.abln.futur D/getContactsList: Sachin B Digital Marketing---09945854228 -- 3056942 -- null
2019-12-24 12:37:55.803 28223-28223/com.abln.futur D/getContactsList: Safe Express---+919343261495 -- 2996137 -- null
2019-12-24 12:37:55.804 28223-28223/com.abln.futur D/getContactsList: Sagayam Chef---+91 80509 78878 -- 37473 -- null
2019-12-24 12:37:55.804 28223-28223/com.abln.futur D/getContactsList: Sahil Nileash---+919994777773 -- 2996232 -- null
2019-12-24 12:37:55.804 28223-28223/com.abln.futur D/getContactsList: Sahil Server Idea Stack Mumbai---+918779170196 -- 3024824 -- null
2019-12-24 12:37:55.804 28223-28223/com.abln.futur D/getContactsList: Sahitya---+919049058093 -- 2996422 -- null
2019-12-24 12:37:55.805 28223-28223/com.abln.futur D/getContactsList: Sahu Sir Jsw---+917770011799 -- 1317 -- null
2019-12-24 12:37:55.805 28223-28223/com.abln.futur D/getContactsList: Sai Akruth---+919133573332 -- 2996322 -- null
2019-12-24 12:37:55.805 28223-28223/com.abln.futur D/getContactsList: Sai Charen---+919030989497 -- 13738 -- null
2019-12-24 12:37:55.806 28223-28223/com.abln.futur D/getContactsList: Sai Nihar---+917382049501 -- 2994927 -- null
2019-12-24 12:37:55.806 28223-28223/com.abln.futur D/getContactsList: Sai Sandeep Tennis---+919542322363 -- 2995551 -- null
2019-12-24 12:37:55.806 28223-28223/com.abln.futur D/getContactsList: Sai Saran---+919666627016 -- 2996367 -- null
2019-12-24 12:37:55.807 28223-28223/com.abln.futur D/getContactsList: Sai Satish---+919989578270 -- 2996417 -- null
2019-12-24 12:37:55.807 28223-28223/com.abln.futur D/getContactsList: Sai Teja---+919676079097 -- 13867 -- null
2019-12-24 12:37:55.807 28223-28223/com.abln.futur D/getContactsList: Saiba---0820-6524802 -- 12893 -- null
2019-12-24 12:37:55.807 28223-28223/com.abln.futur D/getContactsList: Sajith---+918008535353 -- 2996242 -- null
2019-12-24 12:37:55.808 28223-28223/com.abln.futur D/getContactsList: Sajjan Sm Innokios Kiosk Manufacturers---+919741111441 -- 3025217 -- null
2019-12-24 12:37:55.808 28223-28223/com.abln.futur D/getContactsList: Samhitha Elugu---+919177632233 -- 980 -- null
2019-12-24 12:37:55.809 28223-28223/com.abln.futur D/getContactsList: Sandeep Bangalore---+918106604604 -- 2995039 -- null
2019-12-24 12:37:55.809 28223-28223/com.abln.futur D/getContactsList: Sandeep Kalangi Uk---+447466550722 -- 14220 -- content://com.android.contacts/display_photo/234
2019-12-24 12:37:55.809 28223-28223/com.abln.futur D/getContactsList: Sandeep Nai---875-426-3379 -- 13918 -- null
2019-12-24 12:37:55.810 28223-28223/com.abln.futur D/getContactsList: Sandeep Sr---+18147777443 -- 13001 -- content://com.android.contacts/display_photo/198
2019-12-24 12:37:55.810 28223-28223/com.abln.futur D/getContactsList: Sandy---+918801285111 -- 14153 -- content://com.android.contacts/display_photo/223
2019-12-24 12:37:55.811 28223-28223/com.abln.futur D/getContactsList: Sandy Care Hospital Vizag President Son---9618886185 -- 3025566 -- null
2019-12-24 12:37:55.811 28223-28223/com.abln.futur D/getContactsList: Sanjay G---+918977788337 -- 13546 -- content://com.android.contacts/display_photo/241
2019-12-24 12:37:55.812 28223-28223/com.abln.futur D/getContactsList: Sanjukta ios Candidate---+91 72052 94910 -- 3050816 -- null
2019-12-24 12:37:55.812 28223-28223/com.abln.futur D/getContactsList: Sanmati---(605) 690-1319 -- 14913 -- null
2019-12-24 12:37:55.812 28223-28223/com.abln.futur D/getContactsList: Santosh Raju Visual---+918088024263 -- 2995013 -- null
2019-12-24 12:37:55.813 28223-28223/com.abln.futur D/getContactsList: Santosh Sign Board---+919866349206 -- 2995799 -- null
2019-12-24 12:37:55.813 28223-28223/com.abln.futur D/getContactsList: Sarth Kr---81216669090470 -- 15288 -- null
2019-12-24 12:37:55.813 28223-28223/com.abln.futur D/getContactsList: Sashank Randev 100X VC---+919967104706 -- 3057187 -- null
2019-12-24 12:37:55.814 28223-28223/com.abln.futur D/getContactsList: Sashank Sr---+91 8971-036148 -- 13313 -- null
2019-12-24 12:37:55.814 28223-28223/com.abln.futur D/getContactsList: Sashidhar Lounge 47---09900268686 -- 3057218 -- null
2019-12-24 12:37:55.814 28223-28223/com.abln.futur D/getContactsList: Satish---+919885347765 -- 2995835 -- null
2019-12-24 12:37:55.814 28223-28223/com.abln.futur D/getContactsList: Satish Garu JSW---+919949189555 -- 2995910 -- null
2019-12-24 12:37:55.815 28223-28223/com.abln.futur D/getContactsList: Satvik---+918912790266 -- 15134 -- null
2019-12-24 12:37:55.815 28223-28223/com.abln.futur D/getContactsList: Satya Kranthi---+919845853574 -- 13262 -- content://com.android.contacts/contacts/621868/photo
2019-12-24 12:37:55.815 28223-28223/com.abln.futur D/getContactsList: Selvee---09790582180 -- 3025542 -- null
2019-12-24 12:37:55.816 28223-28223/com.abln.futur D/getContactsList: Shahana---+919642936622 -- 2995581 -- null
2019-12-24 12:37:55.816 28223-28223/com.abln.futur D/getContactsList: Shantaram Doctor---09848191010 -- 3005662 -- null
2019-12-24 12:37:55.816 28223-28223/com.abln.futur D/getContactsList: Shanthi Aka---+918886785544 -- 1285 -- null
2019-12-24 12:37:55.816 28223-28223/com.abln.futur D/getContactsList: Shanti Tinder---+917013686584 -- 1291 -- null
2019-12-24 12:37:55.816 28223-28223/com.abln.futur D/getContactsList: Sharda Tinder---(985) 464-4122 -- 1180 -- null
2019-12-24 12:37:55.817 28223-28223/com.abln.futur D/getContactsList: Sharen Doctor---+919573915220 -- 2980071 -- null
2019-12-24 12:37:55.817 28223-28223/com.abln.futur D/getContactsList: Sharma Tennis---+91 91-60-482112 -- 14371 -- null
2019-12-24 12:37:55.817 28223-28223/com.abln.futur D/getContactsList: Shikar---+918105601495 -- 15341 -- null
2019-12-24 12:37:55.818 28223-28223/com.abln.futur D/getContactsList: Shipa Khanna Aarti---+91 98-20-290631 -- 3052640 -- null
2019-12-24 12:37:55.818 28223-28223/com.abln.futur D/getContactsList: Shruthika Tinder Ahmadabad---+91 90-16-496533 -- 1228 -- null
2019-12-24 12:37:55.818 28223-28223/com.abln.futur D/getContactsList: Shubash Pradhma Hospital---08886655697 -- 3025557 -- null
2019-12-24 12:37:55.818 28223-28223/com.abln.futur D/getContactsList: Shubham Nagota Linkidn---09827656400 -- 3034601 -- null
2019-12-24 12:37:55.819 28223-28223/com.abln.futur D/getContactsList: Sibani Icici Relationship Manager---+917995007278 -- 3038718 -- null
2019-12-24 12:37:55.819 28223-28223/com.abln.futur D/getContactsList: Siddardh Rao---+919731571168 -- 2996187 -- content://com.android.contacts/display_photo/199
2019-12-24 12:37:55.819 28223-28223/com.abln.futur D/getContactsList: Siddardh Rao USA---+19377081474 -- 984 -- null
2019-12-24 12:37:55.819 28223-28223/com.abln.futur D/getContactsList: Sindhu Ml---+919901583342 -- 2996012 -- null
2019-12-24 12:37:55.820 28223-28223/com.abln.futur D/getContactsList: Siresha---+918712287154 -- 14105 -- null
2019-12-24 12:37:55.820 28223-28223/com.abln.futur D/getContactsList: Sisir---+919959799598 -- 2996222 -- null
2019-12-24 12:37:55.820 28223-28223/com.abln.futur D/getContactsList: Skandha---+919441466099 -- 2996347 -- content://com.android.contacts/display_photo/9
2019-12-24 12:37:55.820 28223-28223/com.abln.futur D/getContactsList: Skandha Chinta---+17472836161 -- 3026069 -- content://com.android.contacts/display_photo/66
2019-12-24 12:37:55.821 28223-28223/com.abln.futur D/getContactsList: Smaran---+918971026335 -- 2996267 -- null
2019-12-24 12:37:55.821 28223-28223/com.abln.futur D/getContactsList: Smaran Hyd---+91 90-30-999766 -- 14143 -- content://com.android.contacts/display_photo/202
2019-12-24 12:37:55.821 28223-28223/com.abln.futur D/getContactsList: Smruti Manipal Tinder---+917829069339 -- 1168 -- null
2019-12-24 12:37:55.821 28223-28223/com.abln.futur D/getContactsList: Snak Shack---0820-2575129 -- 14497 -- null
2019-12-24 12:37:55.822 28223-28223/com.abln.futur D/getContactsList: Sneha---+918867628882 -- 15054 -- null
2019-12-24 12:37:55.822 28223-28223/com.abln.futur D/getContactsList: Sneha Ubereats Vizag---07842831119 -- 3000887 -- null
2019-12-24 12:37:55.822 28223-28223/com.abln.futur D/getContactsList: Snoha Tinder---+91 98-33-343967 -- 1226 -- null
2019-12-24 12:37:55.822 28223-28223/com.abln.futur D/getContactsList: Soumya Gupta---+919008753329 -- 2996097 -- null
2019-12-24 12:37:55.823 28223-28223/com.abln.futur D/getContactsList: Sp New---+91 90-30-033777 -- 13299 -- null
2019-12-24 12:37:55.823 28223-28223/com.abln.futur D/getContactsList: Sp Ooty---+919498139813 -- 1354 -- null
2019-12-24 12:37:55.823 28223-28223/com.abln.futur D/getContactsList: Spice And Ice---+919902441234 -- 2996217 -- null
2019-12-24 12:37:55.823 28223-28223/com.abln.futur D/getContactsList: Sravan Snr---+91 99-45-596529 -- 13488 -- null
2019-12-24 12:37:55.824 28223-28223/com.abln.futur D/getContactsList: Sree Harsha---+918801126688 -- 2996252 -- null
2019-12-24 12:37:55.824 28223-28223/com.abln.futur D/getContactsList: Sri Hari---+918500929828 -- 2995144 -- null
2019-12-24 12:37:55.824 28223-28223/com.abln.futur D/getContactsList: Sri Uncle---+91 8497-966666 -- 624 -- null
2019-12-24 12:37:55.825 28223-28223/com.abln.futur D/getContactsList: Sridhar -Bujji G---09845033077 -- 3045727 -- null
2019-12-24 12:37:55.825 28223-28223/com.abln.futur D/getContactsList: Srikanth A BABJI RAO UNCLE---09966109898 -- 3056999 -- null
2019-12-24 12:37:55.825 28223-28223/com.abln.futur D/getContactsList: Srikanth Sir---+919849917184 -- 2996192 -- null
2019-12-24 12:37:55.825 28223-28223/com.abln.futur D/getContactsList: Srikar---+91 91006 09129 -- 3052432 -- null
2019-12-24 12:37:55.826 28223-28223/com.abln.futur D/getContactsList: Srinivas Garu Shimadhri---09848190782 -- 3025351 -- null
2019-12-24 12:37:55.826 28223-28223/com.abln.futur D/getContactsList: Srinivas Vignesh---+919840864627 -- 2995733 -- null
2019-12-24 12:37:55.826 28223-28223/com.abln.futur D/getContactsList: Srinu Uncle---+919849090060 -- 2995774 -- null
2019-12-24 12:37:55.827 28223-28223/com.abln.futur D/getContactsList: Srishti CR---+918884293323 -- 13766 -- content://com.android.contacts/display_photo/206
2019-12-24 12:37:55.827 28223-28223/com.abln.futur D/getContactsList: Srishti Vikram---+91 8867-603111 -- 14551 -- content://com.android.contacts/display_photo/204
2019-12-24 12:37:55.827 28223-28223/com.abln.futur D/getContactsList: Srujana---+919160178550 -- 2995356 -- null
2019-12-24 12:37:55.828 28223-28223/com.abln.futur D/getContactsList: Stefan---+917995881662 -- 2994967 -- null
2019-12-24 12:37:55.829 28223-28223/com.abln.futur D/getContactsList: Stp---+91 95-35-676954 -- 12963 -- null
2019-12-24 12:37:55.829 28223-28223/com.abln.futur D/getContactsList: Stuff Vizag Gambler 🍀---07702283295 -- 3025463 -- null
2019-12-24 12:37:55.829 28223-28223/com.abln.futur D/getContactsList: Subway Manipal---0820-2574144 -- 13009 -- null
2019-12-24 12:37:55.829 28223-28223/com.abln.futur D/getContactsList: Sudheer Tennis---+918143540540 -- 2995069 -- null
2019-12-24 12:37:55.830 28223-28223/com.abln.futur D/getContactsList: Sufiyan Uber For Invoices---+91 90353 04500 -- 2647028 -- null
2019-12-24 12:37:55.830 28223-28223/com.abln.futur D/getContactsList: Suhas Zomato---+91 95389 90090 -- 2178 -- null
2019-12-24 12:37:55.831 28223-28223/com.abln.futur D/getContactsList: Sukanya---+919008748157 -- 2996272 -- null
2019-12-24 12:37:55.831 28223-28223/com.abln.futur D/getContactsList: Sukesh---+91 8179-093050 -- 15024 -- null
2019-12-24 12:37:55.831 28223-28223/com.abln.futur D/getContactsList: Sukesh House Listing---+91 861-8722080 -- 2236 -- null
2019-12-24 12:37:55.832 28223-28223/com.abln.futur D/getContactsList: Sumanth New---+91 80-88029434 -- 14083 -- null
2019-12-24 12:37:55.832 28223-28223/com.abln.futur D/getContactsList: Sumanth Reddy---+91 8197-438700 -- 14905 -- null
2019-12-24 12:37:55.832 28223-28223/com.abln.futur D/getContactsList: Sumith Gowda Pinlab---+918431457727 -- 3052465 -- null
2019-12-24 12:37:55.833 28223-28223/com.abln.futur D/getContactsList: Sunil---+919160744042 -- 13320 -- null
2019-12-24 12:37:55.833 28223-28223/com.abln.futur D/getContactsList: Sunil 7AM Assistant Manager---+91 84659 84101 -- 51446 -- null
2019-12-24 12:37:55.833 28223-28223/com.abln.futur D/getContactsList: Sunil Patil---09844945843 -- 3026585 -- null
2019-12-24 12:37:55.833 28223-28223/com.abln.futur D/getContactsList: Sunkura---+919035211798 -- 2996107 -- null
2019-12-24 12:37:55.834 28223-28223/com.abln.futur D/getContactsList: Supreeth---08197-612352 -- 14846 -- null
2019-12-24 12:37:55.834 28223-28223/com.abln.futur D/getContactsList: Suraj System Architect---+919380593839 -- 3051196 -- null
2019-12-24 12:37:55.834 28223-28223/com.abln.futur D/getContactsList: Suresh Garu Shimadri, MD---09848190781 -- 3025344 -- null
2019-12-24 12:37:55.834 28223-28223/com.abln.futur D/getContactsList: Suresh I2r Design---+919845203424 -- 2221 -- null
2019-12-24 12:37:55.835 28223-28223/com.abln.futur D/getContactsList: Suresh Jaguar Land Rover Vizag Dealer---+91 79979 91379 -- 2998283 -- null
2019-12-24 12:37:55.835 28223-28223/com.abln.futur D/getContactsList: Suresh Pattu - Big ass lier---+91 96320 08592 -- 2985755 -- null
2019-12-24 12:37:55.835 28223-28223/com.abln.futur D/getContactsList: Surya Abhinanyu---+917396067903 -- 2994932 -- null
2019-12-24 12:37:55.835 28223-28223/com.abln.futur D/getContactsList: Surya Gitam AO---+919014791917 -- 2989684 -- null
2019-12-24 12:37:55.835 28223-28223/com.abln.futur D/getContactsList: Surya Vasupalli---+91 8978-271399 -- 15280 -- null
2019-12-24 12:37:55.836 28223-28223/com.abln.futur D/getContactsList: Suveer zomato---+91 81234 76769 -- 1367 -- null
2019-12-24 12:37:55.836 28223-28223/com.abln.futur D/getContactsList: Swamy Auditor---+919542286666 -- 2995546 -- null
2019-12-24 12:37:55.836 28223-28223/com.abln.futur D/getContactsList: Swamy Auditor Office Number---+916281261952 -- 2980141 -- null
2019-12-24 12:37:55.837 28223-28223/com.abln.futur D/getContactsList: Swaroop Tikona---+919293734313 -- 2995422 -- null
2019-12-24 12:37:55.837 28223-28223/com.abln.futur D/getContactsList: Taban---+91 8527-871334 -- 13373 -- content://com.android.contacts/display_photo/209
2019-12-24 12:37:55.837 28223-28223/com.abln.futur D/getContactsList: Tandoorinn Hot---08912784785 -- 14455 -- null
2019-12-24 12:37:55.838 28223-28223/com.abln.futur D/getContactsList: Tandoorinn Take away---+918916652777 -- 2980004 -- null
2019-12-24 12:37:55.838 28223-28223/com.abln.futur D/getContactsList: Tarun Satya---+919989518666 -- 13808 -- null
2019-12-24 12:37:55.838 28223-28223/com.abln.futur D/getContactsList: Tathya---+919849298132 -- 2996007 -- null
2019-12-24 12:37:55.838 28223-28223/com.abln.futur D/getContactsList: Tathya Padha---+919705532825 -- 2996382 -- null
2019-12-24 12:37:55.838 28223-28223/com.abln.futur D/getContactsList: Tessa Desserts---+91 97392 11869 -- 2217 -- null
2019-12-24 12:37:55.839 28223-28223/com.abln.futur D/getContactsList: Thomas Patak---+91 99-47-662217 -- 15334 -- content://com.android.contacts/display_photo/230
2019-12-24 12:37:55.839 28223-28223/com.abln.futur D/getContactsList: Tim Wilson---(703) 344-1723 -- 948 -- null
2019-12-24 12:37:55.839 28223-28223/com.abln.futur D/getContactsList: Tirthik Saha---+919008774288 -- 2996282 -- content://com.android.contacts/display_photo/194
2019-12-24 12:37:55.840 28223-28223/com.abln.futur D/getContactsList: Tisha 3m---+919035180642 -- 12857 -- content://com.android.contacts/display_photo/217
2019-12-24 12:37:55.840 28223-28223/com.abln.futur D/getContactsList: Tit For Tattoo Hsr---08884443473 -- 3000788 -- null
2019-12-24 12:37:55.840 28223-28223/com.abln.futur D/getContactsList: Tulsi Chef Vijaywada---+91 98668 26662 -- 2998440 -- null
2019-12-24 12:37:55.840 28223-28223/com.abln.futur D/getContactsList: Turky---00447924073526 -- 14476 -- null
2019-12-24 12:37:55.840 28223-28223/com.abln.futur D/getContactsList: UIDAI---1800-300-1947 -- 4 -- null
2019-12-24 12:37:55.841 28223-28223/com.abln.futur D/getContactsList: Udai Electronics---(929) 151-3655 -- 14244 -- null
2019-12-24 12:37:55.841 28223-28223/com.abln.futur D/getContactsList: Unknown G---+91 91-64-703389 -- 13724 -- null
2019-12-24 12:37:55.841 28223-28223/com.abln.futur D/getContactsList: Upen Reddy---+919391600439 -- 2995449 -- null
2019-12-24 12:37:55.841 28223-28223/com.abln.futur D/getContactsList: Uttam UI UX---+91 98401 79998 -- 2998447 -- null
2019-12-24 12:37:55.841 28223-28223/com.abln.futur D/getContactsList: V J Reddy---+918884296360 -- 2996067 -- null
2019-12-24 12:37:55.842 28223-28223/com.abln.futur D/getContactsList: V Tyagi---+919008774203 -- 14632 -- content://com.android.contacts/display_photo/203
2019-12-24 12:37:55.842 28223-28223/com.abln.futur D/getContactsList: V Varun---07207-717308 -- 13648 -- null
2019-12-24 12:37:55.842 28223-28223/com.abln.futur D/getContactsList: VAS Helpline---1214 -- 15111 -- null
2019-12-24 12:37:55.842 28223-28223/com.abln.futur D/getContactsList: Vaatsala 7 AM House Owner.---+91 99019 95808 -- 2182 -- null
2019-12-24 12:37:55.843 28223-28223/com.abln.futur D/getContactsList: Vamsi Friend Shiva Shivani---+919871022346 -- 3026078 -- null
2019-12-24 12:37:55.843 28223-28223/com.abln.futur D/getContactsList: Vamsi Kasina---07795-773308 -- 13203 -- null
2019-12-24 12:37:55.843 28223-28223/com.abln.futur D/getContactsList: Vamsi Krishna---+919963782783 -- 2995945 -- null
2019-12-24 12:37:55.843 28223-28223/com.abln.futur D/getContactsList: Vamsi Maduri---(850) 066-9919 -- 14958 -- null
2019-12-24 12:37:55.844 28223-28223/com.abln.futur D/getContactsList: Vani M---+918987598927 -- 2996082 -- null
2019-12-24 12:37:55.844 28223-28223/com.abln.futur D/getContactsList: Vara---+16146193919 -- 2998843 -- content://com.android.contacts/display_photo/201
2019-12-24 12:37:55.844 28223-28223/com.abln.futur D/getContactsList: Vara Prasad Rao Surya Hospital---+919640496966 -- 3005669 -- null
2019-12-24 12:37:55.844 28223-28223/com.abln.futur D/getContactsList: Vardell Tinder---(850) 304-0157 -- 1118 -- null
2019-12-24 12:37:55.845 28223-28223/com.abln.futur D/getContactsList: Varun Git Tennis---08143-419044 -- 13210 -- content://com.android.contacts/display_photo/235
2019-12-24 12:37:55.845 28223-28223/com.abln.futur D/getContactsList: Vasu---+91 99-49-252425 -- 14394 -- null
2019-12-24 12:37:55.845 28223-28223/com.abln.futur D/getContactsList: Vedika---+918884292830 -- 14671 -- null
2019-12-24 12:37:55.846 28223-28223/com.abln.futur D/getContactsList: Vedika USA---+1-414-204-3487 -- 15422 -- null
2019-12-24 12:37:55.846 28223-28223/com.abln.futur D/getContactsList: Venkat Teja---+918008550826 -- 2994983 -- null
2019-12-24 12:37:55.846 28223-28223/com.abln.futur D/getContactsList: Venkat Varun---(984) 817-3454 -- 14504 -- null
2019-12-24 12:37:55.846 28223-28223/com.abln.futur D/getContactsList: Venket Rao Stove---+919246624213 -- 2995396 -- null
2019-12-24 12:37:55.846 28223-28223/com.abln.futur D/getContactsList: Venu Barghav---+44 7466 550883 -- 13897 -- content://com.android.contacts/display_photo/240
2019-12-24 12:37:55.847 28223-28223/com.abln.futur D/getContactsList: Venu New---+91 90-35-188629 -- 13502 -- content://com.android.contacts/display_photo/236
2019-12-24 12:37:55.847 28223-28223/com.abln.futur D/getContactsList: Victoria Bohorquez Ex---+12024468023 -- 3059576 -- null
2019-12-24 12:37:55.847 28223-28223/com.abln.futur D/getContactsList: Vidhya---+919902481532 -- 2995855 -- null
2019-12-24 12:37:55.848 28223-28223/com.abln.futur D/getContactsList: Vidushi Kameni VC---+912261944111 -- 3056939 -- null
2019-12-24 12:37:55.848 28223-28223/com.abln.futur D/getContactsList: Vijay Gym---+91 91-60-787971 -- 14098 -- null
2019-12-24 12:37:55.848 28223-28223/com.abln.futur D/getContactsList: Vijay I.n pathru---+91 98-66-666568 -- 846 -- null
2019-12-24 12:37:55.848 28223-28223/com.abln.futur D/getContactsList: Vijendra - People S Hub Owner---+919880566550 -- 3038766 -- null
2019-12-24 12:37:55.849 28223-28223/com.abln.futur D/getContactsList: Vijju---+91 7200-262736 -- 13218 -- null
2019-12-24 12:37:55.849 28223-28223/com.abln.futur D/getContactsList: Vikas A.u Uni,,,,class Friend---+918374051474 -- 2995124 -- null
2019-12-24 12:37:55.849 28223-28223/com.abln.futur D/getContactsList: Vikrant Msoe---+919728884373 -- 2996177 -- null
2019-12-24 12:37:55.849 28223-28223/com.abln.futur D/getContactsList: Vineela School---+919032614551 -- 13510 -- null
2019-12-24 12:37:55.850 28223-28223/com.abln.futur D/getContactsList: Vinod Driver---+917995395653 -- 2985015 -- null
2019-12-24 12:37:55.850 28223-28223/com.abln.futur D/getContactsList: Vinod Kumar Police Control Room---09912211612 -- 3025370 -- null
2019-12-24 12:37:55.850 28223-28223/com.abln.futur D/getContactsList: Vinod U. S---+18168032398 -- 2998968 -- null
2019-12-24 12:37:55.851 28223-28223/com.abln.futur D/getContactsList: Vinutha APIS Manager---+919989699146 -- 3026611 -- null
2019-12-24 12:37:55.851 28223-28223/com.abln.futur D/getContactsList: Vishal---+91 90-52-910950 -- 14167 -- null
2019-12-24 12:37:55.851 28223-28223/com.abln.futur D/getContactsList: Vishal Mahi---+918143162910 -- 12765 -- null
2019-12-24 12:37:55.851 28223-28223/com.abln.futur D/getContactsList: Vishawajith Reddy Usa---00 1 (414) 446-1735 -- 14136 -- null
2019-12-24 12:37:55.852 28223-28223/com.abln.futur D/getContactsList: Vishnu SS Equipments BLR---+91 70908 83883 -- 2998483 -- null
2019-12-24 12:37:55.852 28223-28223/com.abln.futur D/getContactsList: Vivek---+919652222349 -- 2995586 -- null
2019-12-24 12:37:55.852 28223-28223/com.abln.futur D/getContactsList: Vivek Marketing & Sales---+919731433993 -- 3025666 -- null
2019-12-24 12:37:55.852 28223-28223/com.abln.futur D/getContactsList: Waseem Sahil---+919502040590 -- 648 -- null
2019-12-24 12:37:55.853 28223-28223/com.abln.futur D/getContactsList: Winny Patro Bandari Fintech Vizag---+91 92471 75247 -- 3025894 -- null
2019-12-24 12:37:55.853 28223-28223/com.abln.futur D/getContactsList: You Broadband Suresh---+919347364665 -- 1295 -- null
2019-12-24 12:37:55.853 28223-28223/com.abln.futur D/getContactsList: Youbriodband Gouthan---+918374061999 -- 2980022 -- null
2019-12-24 12:37:55.853 28223-28223/com.abln.futur D/getContactsList: Zomato Soahil---+919035004770 -- 1352 -- null
2019-12-24 12:37:55.854 28223-28223/com.abln.futur D/getContactsList: Zumar. live In Castle Boss---+91 99956 59186 -- 3000668 -- content://com.android.contacts/contacts/621557/photo
2019-12-24 12:37:55.854 28223-28223/com.abln.futur D/getContactsList: airtel info---171111 -- 3050804 -- null
2019-12-24 12:37:55.854 28223-28223/com.abln.futur D/getContactsList: airtel live (AL)---54321 -- 3050790 -- null
2019-12-24 12:37:55.854 28223-28223/com.abln.futur D/getContactsList: ambulance---102 -- 3050786 -- null
2019-12-24 12:37:55.854 28223-28223/com.abln.futur D/getContactsList: anirudh---+919490093939 -- 2995500 -- null
2019-12-24 12:37:55.854 28223-28223/com.abln.futur D/getContactsList: fire---101 -- 3050784 -- null
2019-12-24 12:37:55.854 28223-28223/com.abln.futur D/getContactsList: ganesh gupta---+918106498203 -- 12721 -- null
2019-12-24 12:37:55.855 28223-28223/com.abln.futur D/getContactsList: ganesh gupta.---+917416796935 -- 14794 -- null
2019-12-24 12:37:55.855 28223-28223/com.abln.futur D/getContactsList: laxmi narayan---+919964072471 -- 2996227 -- null
2019-12-24 12:37:55.855 28223-28223/com.abln.futur D/getContactsList: my airtel---*121# -- 3050780 -- null
2019-12-24 12:37:55.855 28223-28223/com.abln.futur D/getContactsList: police---100 -- 3050782 -- null
2019-12-24 12:37:55.855 28223-28223/com.abln.futur D/getContactsList: railway inquiry---139 -- 3050788 -- null
2019-12-24 12:37:55.855 28223-28223/com.abln.futur D/getContactsList: varun Venkat---8179236540 -- 14868 -- null
    * */
}
