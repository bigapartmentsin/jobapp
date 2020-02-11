package com.abln.futur.common;

public class NetworkConfig {

    public static final String RESPONSE_BODY = "com.abln.futur.RESPONSE_BODY";
    public static final String BASE_URL = "http://docapi.medinin.com/api";
    //*******************Actions*******************//
    public static final String TASK_COMPLETE = "com.abln.futur.TASK_COMPLETE";
    public static final String DOWNLOAD_COMPLETE = "com.abln.futur.DOWNLOAD_COMPLETE";
    public static final String LOCK_COMPLETE = "com.abln.futur.LOCK_COMPLETE";
    public static final String API_CALLED_COMPLETELY = "com.abln.futur.API_CALLED_COMPLETELY";
    public static final String NETWORK_STATE = "android.net.conn.CONNECTIVITY_CHANGE";
    public static final boolean ENABLE_ROOT_CHECK = false;
    //*******************Intent Key's *******************//
    public static final String API_URL = "ApiUrl";
    public static final String HEADER_MAP = "HeaderMap";
    public static final String INPUT_BODY = "InputBody";
    public static final String REQUEST_TYPE = "RequestType";
    public static String APP_ID = "";
    public static String _domain = "http://docapi.medinin.com/api/";
    public static String _domain_v1 = "https://prod.medinin.in/v1/";
    public static String _domain_v2 = "https://prod.medinin.in/v2/";
    public static String _domain_v3 = "https://prod.medinin.in/v3/";


    public static String getAllPatient = _domain_v1 + "get-doc-patient-list";


    // Amazon configurations for chat backup server
    public static String S3_EP_URL = "--";
    public static String S3_S_KEY = "--";
    public static String S3_A_KEY = "--";
    public static String S3_IMAGES = "--";
    public static String S3_VIDEOS = "--";
    public static String S3_THUMBNAILS = "--";
    public static String S3_DOC = "--";


    // Futur API
    public static String domain_futur = "https://futur.medinin.in/v1/";
    public static String SendJobInvite = _domain + "sendJobInvite";

    public static String check_mobile = domain_futur + "check";
    public static String login = domain_futur + "login";
    public static String user_signUp = domain_futur + "user-signup";
    public static String verifiy_otp = domain_futur + "verifyotpsignup";
    public static String user_info = domain_futur + "userinfo";
    public static String get_user_data = domain_futur + "getuserdata";
    public static String otp = domain_futur + "otp";
    public static String getAllUser = domain_futur + "getalluser";
    public static String getcountriesList = domain_futur + "countries";
    public static String addJobPost = domain_futur + "addpost";
    public static String getUserJobPost = domain_futur + "getuserpost";
    public static String getAllPost = domain_futur + "getallpost";
    public static String updateLocation = domain_futur + "update-location";
    public static String updatebasicInfo = domain_futur + "basic-info";
    public static String updateprofessionalinfo = domain_futur + "professional-info";
    public static String updateuser_avatar = domain_futur + "user-avatar";
    public static String dp = domain_futur + "delete-post";
    public static String time = domain_futur + "timeago";
    public static String date = domain_futur + "expdate";
    public static String diffdate = domain_futur + "get-expdate";
    public static String searchresult = domain_futur + "ssearch";

    public static String recent = domain_futur + "recently";

    public static  String userdatainfo = domain_futur + "getuserdata2" ;

    public static String update = domain_futur +"";


}

