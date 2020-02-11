package com.abln.chat.ui.helper;

import android.content.Context;
import android.util.Log;

import com.amazonaws.auth.AWSAbstractCognitoDeveloperIdentityProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.abln.chat.IMConfiguration;
import com.abln.chat.utils.IMUtils;

public class AWSClientHelper {
    private static final String TAG = "AWSClientHelper";

    private static AWSClientHelper mAWSClientHelper;

    private IMConfiguration mIMConfiguration;
    private TransferUtility mTransferUtility;


    public synchronized static AWSClientHelper getInstance() {
        if (null == mAWSClientHelper) {
            Log.i(TAG, " is not initialized, call initAWSClientHelper(...) method first.");
        }

        return mAWSClientHelper;
    }

    private static synchronized void stashAWSClientHelper() {
        mAWSClientHelper = null;
    }

    public static synchronized void initAWSClientHelper(Context context, IMConfiguration configuration) {
        if (null == mAWSClientHelper) {
            mAWSClientHelper = new AWSClientHelper(context, configuration);
        }
    }

    private AWSClientHelper(Context context, IMConfiguration configuration) {
        this.mIMConfiguration = configuration;

        if (!IMUtils.isNullOrEmpty(configuration.cognitoToken)) {
            initAWSEssentialsWithToken(context);

        } else {
            initAWSEssentialsWithSecretKey(context);
        }
    }



    private void initAWSEssentialsWithToken(Context context) {
        DeveloperAuthenticationProvider developerAuthenticationProvider = new DeveloperAuthenticationProvider();

        CognitoCachingCredentialsProvider cognitoCachingCredentialsProvider =
                new CognitoCachingCredentialsProvider(context, developerAuthenticationProvider, Regions.AP_SOUTH_1);

        AmazonS3Client amazonS3Client = new AmazonS3Client(cognitoCachingCredentialsProvider);
        amazonS3Client.setEndpoint(mIMConfiguration.s3EndPoint);

        mTransferUtility = new TransferUtility(amazonS3Client, context);
    }

    private void initAWSEssentialsWithSecretKey(Context context) {
        if(mIMConfiguration.s3EndPoint.equalsIgnoreCase("--"))//TODO configure s3
            return;
//        if(1==1)return;
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(mIMConfiguration.s3AccessKey, mIMConfiguration.s3SecretKey);
        AmazonS3 amazonS3 = new AmazonS3Client(basicAWSCredentials);
        amazonS3.setEndpoint(mIMConfiguration.s3EndPoint);
        mTransferUtility = new TransferUtility(amazonS3, context);
    }


    public TransferUtility getTransferUtility() {
        return mTransferUtility;
    }


    public void updateCongnitoTokenDetails(Context applicationContext, String cognitoIdentityID, String cognitoToken) {
        mIMConfiguration.congnitoIdentityId = cognitoIdentityID;
        mIMConfiguration.cognitoToken = cognitoToken;
        initAWSEssentialsWithToken(applicationContext);
    }

    private class DeveloperAuthenticationProvider extends AWSAbstractCognitoDeveloperIdentityProvider {

        DeveloperAuthenticationProvider() {
            super(mIMConfiguration.s3AccountId, mIMConfiguration.cognitoPoolId, Regions.AP_SOUTH_1);
        }

        @Override
        public String getProviderName() {
            return mIMConfiguration.s3ProvideName;
        }

        @Override
        public String refresh() {
            setToken(null);
            update(mIMConfiguration.congnitoIdentityId, mIMConfiguration.cognitoToken);
            return mIMConfiguration.cognitoToken;
        }

        @Override
        public String getIdentityId() {
            return mIMConfiguration.congnitoIdentityId;
        }
    }

}
