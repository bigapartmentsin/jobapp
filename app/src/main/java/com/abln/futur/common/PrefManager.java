package com.abln.futur.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.abln.futur.activites.Data;
import com.abln.futur.activites.ModelData;

import java.io.ByteArrayOutputStream;


public class PrefManager {
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String KEY_SERVER_VERSION = "serverVersion";
    public static final String IS_TC_ACCEPTED = "isAcceptedTC";
    public static final String IS_FIRST_TIME_LOGIN = "isFirstTimeLogin";
    public static final String METADATA = "metaData";
    public static final String MY_ROLE = "myRole";
    public static final String MY_PASSCODE = "myPasscode";
    public static final String SIGNUP_ROLE = "signupRole";
    public static final String MOBILENUMBER = "mobileNumber";
    public static final String DOB = "dob";
    public static final String GENDER = "gender";
    public static final String IMAGEURL = "profileUrl";
    public static final String LAT = "lat";
    public static final String LON = "lon";
    public static final String QUALIFICATION = "qualitifcation";
    public static final String REGNO = "regno";
    public static final String EXPERINCE = "exp";
    public static final String SPECIALITYID = "spID";
    public static final String COUNTRYCODE = "countryCode";
    public static final String APIKEY = "apikey";
    public static final String DOC_MODULE = "docModule";
    public static final String CLINIC_MODULE = "clinicModule";
    public static final String HOSPITAL_MODULE = "hospitalModule";
    public static final String LAB_MODULE = "labModule";
    public static final String PATIENT_MODULE = "patModule";
    public static final String PHARM_MODULE = "pharmModule";
    public static final String USER_LOGGEDIN_ROLE = "userRole";
    public static final String USER_NAME = "username";
    public static final String USER_GENDER = "usergender";
    public static final String USER_INDUSTRY = "userindus";
    public static final String USER_OCCUPATION = "useroccupation";
    public static final String USER_EXPERINECE = "userexp";
    public static final String USER_UNIVERSITY = "useruniv";
    public static final String SELECTED_LAT = "latitude";
    public static final String SELECTED_LON = "longitude";
    public static final String MYLOCATION_LAT = "latitude";
    public static final String MYLOCATION_LON = "longitude";
    public static final String MYADDRESS = "address";
    public static final String LINKEDINURL = "url";
    public static final String OTHER1 = "url1";
    public static final String OTHER2 = "url2";
    public static final String OTHER3 = "url3";
    public static final String USER_PHOTO = "";
    public static final String testurl = "URL";

    public static final String A = "A";
    public static final String B = "B";
    public static final String C = "C";
    public static final String D = "D";
    public static final String E  = "E";
    public static final String F = "F";
    public static final String G = "G";
    public static final String H = "H";
    public static final String I = "I";
    public static final String J = "J";
    public static final String K = "K";
    public static final String L = "L";
    public static final String M = "M";
    public static final String N = "N";
    public static final String O = "O";
    public static final String P = "P";
    public static final String Q = "Q";
    public static final String R = "R";
    public static final String S = "S";
    public static final String T = "T";
    public static final String U = "U";
    public static final String V = "V";
    public static final String W = "W";
    public static final String X = "X";
    public static final String Y = "Y";
    public static final String Z = "Z";



    public static final String AA = "AA";
    public static final String BB= "BB";
    public static final String CC = "CC";
    public static final String DD = "DD";
    public static final String EE  = "EE";
    public static final String FF = "FF";
    public static final String GG = "GG";
    public static final String HH = "HH";
    public static final String II = "II";
    public static final String JJ = "JJ";
    public static final String KK = "KK";
    public static final String LL = "LL";
    public static final String MM = "MM";
    public static final String NN = "NN";
    public static final String OO = "OO";
    public static final String PP = "PP";
    public static final String QQ = "QQ";
    public static final String RR = "RR";
    public static final String SS = "SS";
    public static final String TT = "TT";
    public static final String UU = "UU";
    public static final String VV = "VV";
    public static final String WW = "WW";
    public static final String XX = "XX";
    public static final String YY = "YY";
    public static final String ZZ = "ZZ";






    public static Data da = new Data();
    public static final String version = "v1";

    public static final String a = "thumbnail";       //da.a;
    public static final String b =    "s1"; //da.b;
    public static final String c =   "s1" ; //da.c;
    public static final String d = "addpost"  ; //da.d;
    public static final String e = "s1"  ; //da.e;
    public static final String f = "s2" ; //da.f;
    public static final String g = "s3" ;  //da.g;
    public static final String h = "s4" ; //da.h;
    public static final String i = "s5" ; //da.i;
    public static final String j ="s6";// da.j;
    public static final String k = "s7d" ; //a.k;
    public static final String l = "s8" ; // da.l;
    public static final String m ="full-data";// da.m;
    public static final String n = "saved" ; //da.n;
    public static final String o = "can-rec-apply" ; //da.o;
    public static final String p ="s9";// da.p;
    public static final String q = "s10" ;// da.q;
    public static  final String r  = "addpostnormal"; //   da.r;
    public static final String s ="msaved" ; // da.s;
    public static final String t = "ssearch" ; //da.t;
    public static final String u = "title" ;// da.u;
    public static final String v = "get-resume" ; //da.v;
    public static final String w =  "experience" ; //  da.w;
    public static final String x = "update-resume" ; //da.x;
    public static final String y =   "getsaved"  ; //da.y;
    public static final String z = "short-postinfo" ; //da.z;




    public static final String aa = "title" ; //da.aa;
    public static final String ab = "gettotalpost" ; //da.bb;
    public static final String ac = "one-accoun"; //da.cc;
    public static final String ad  ="two-account" ; // da.dd;
    public static final String ae= "three-account" ; // da.ee;

    public static  final String af = "four-account"; //da.ff;
    public static  final String ag ="cpost"; // da.gg;
    public static  final String ah = "getuserdata2"; //da.hh;
    public static  final String ai ="basic-info"; // da.ii;
    public static  final  String aj = "post-images" ;//da.jj;
    public static final  String ak = "getuserpost" ; //da.kk;
    public static  final String al = "delete-post" ; //da.ll;
    public static final String am ="post-images" ; // da.mm;
    public static  final String an ="stories-status"; // da.nn;
    public static final String ao = "can-accept" ; //da.oo;
    public static  final String ap ="can-reject" ; // da.pp;
    public static final String aq = "can-reviewed" ; //da.qq;
    public static final String ar = "rec-can-chat" ; //da.rr;
    public static final String as = "get-applied"; //da.ss;
    public static final String at = "rec-can-view "; //"da.tt;
    public static final String au = "mobile-applications"; //da.uu;
    public static final String av = "revert-status"; //da.vv;
    public static  final String aw = "get-info2" ; // da.ww;
    public static final String ax = "user-avatar"; //da.xx;
    public static final String ay = "get-avatar"; //da.yy;


    public static  ModelData data = new ModelData();



    public static  final  String testdata = data.thumbdata;




    private static final String TAG = PrefManager.class.getSimpleName();
    // Shared preferences file name
    private static final String PREF_NAME = PrefManager.class.getSimpleName();
    private static PrefManager prefManager;
    // shared pref mode
    int PRIVATE_MODE = 0;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;

    public PrefManager() {
        this._context = AppConfig.getInstance();
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static PrefManager getInstance() {
        if (prefManager == null) {
            prefManager = new PrefManager();
        }
        return prefManager;
    }

    public void saveAccessToken(String accessToken) {
        editor.putString(ACCESS_TOKEN, accessToken).commit();
    }

    public String getAccessToken() {
        return pref.getString(ACCESS_TOKEN, null);
    }

    public Float getServerVersions() {
        return pref.getFloat(KEY_SERVER_VERSION, 0);
    }

    public void saveServerVersion(Float tenantIds) {
        editor.putFloat(KEY_SERVER_VERSION, tenantIds).commit();
    }

    public void clearSession() {
        editor.clear().apply();
    }

    public void logout() {
        clearSession();
    }

    public boolean getTCstatus() {
        return pref.getBoolean(IS_TC_ACCEPTED, false);
    }

    public void setTCstatus(boolean status) {
        editor.putBoolean(IS_TC_ACCEPTED, status).commit();
    }

    public boolean isFirstTimeLogin() {
        return pref.getBoolean(IS_FIRST_TIME_LOGIN, false);
    }

    public void setIsFirstTimeLogin(boolean status) {
        editor.putBoolean(IS_FIRST_TIME_LOGIN, status).commit();
    }

    public String getMobilenumber() {
        return pref.getString(MOBILENUMBER, "");
    }

    public void setMobilenumber(String number) {
        editor.putString(MOBILENUMBER, number).commit();
    }

    public String getCountrycodewithPlus() {
        return pref.getString(COUNTRYCODE, "+91");
    }

    public void setCountrycodewithPlus(String countrycode) {
        editor.putString(COUNTRYCODE, countrycode).commit();
    }

    public String getApikey() {
        return pref.getString(APIKEY, " ");
    }

    public void setApikey(String apikey) {
        editor.putString(APIKEY, apikey).commit();
    }



    public String getTesturl(){
        return pref.getString(testurl,"");

    }

    public void setTesturl(String url ){
        editor.putString(testurl,url).commit();
    }



    public String getA(){
        return pref.getString(A,"");

    }

    public void setA(String a ){
        editor.putString(A,a).commit();
    }


    public String getB(){
        return pref.getString(B,"");

    }

    public void setB(String b){

        editor.putString(B,b).commit();
    }

    public String getC(){
        return pref.getString(C,"");

    }


    public void setC(String c){
        editor.putString(C,c).commit();
    }


    public String getD(){
        return pref.getString(D,"");
    }


    public void setD(String d){
        editor.putString(D,d).commit();
    }

    public String getE(){
        return pref.getString(E,"");
    }


    public void setE(String e){
        editor.putString(E,e).commit();
    }


    public String getF(){
        return pref.getString(F,"");
    }


    public void setF(String f){
        editor.putString(F,f).commit();
    }


    public String getG(){
        return pref.getString(G,"");
    }


    public void setG(String g){
        editor.putString(G,g).commit();
    }


    public String getH(){
        return pref.getString(H,"");
    }


    public void setH(String h){
        editor.putString(H,h).commit();
    }



    public String getI(){
        return pref.getString(I,"");
    }

    public void setI(String i){
        editor.putString(I,i).commit();
    }


    public String getJ(){
        return pref.getString(I,"");
    }

    public void setJ(String j){
        editor.putString(J,j).commit();
    }


    public String getK(){
        return pref.getString(K,"");
    }


    public void setK(String k){
        editor.putString(K,k).commit();
    }



    public String getL(){
        return pref.getString(L,"");
    }

    public void setL(String l){
        editor.putString(L,l).commit();
    }


    public String getM(){
        return pref.getString(M,"");
    }


    public void setM(String m){
        editor.putString(M,m).commit();
    }


    public String getN(){
        return pref.getString(N,"");
    }

    public void setN(String n){
        editor.putString(N,m).commit();
    }

    public String getO(){
        return pref.getString(O,"");
    }


    public void setO(String n){
        editor.putString(O,o).commit();
    }

    public String getP(){
        return pref.getString(P,"");
    }


    public void setP(String p){
        editor.putString(P,p).commit();
    }


    public String getQ(){
        return pref.getString(Q,"");
    }


    public void setQ(String q){
        editor.putString(Q,q).commit();
    }

    public String getR(){
        return pref.getString(R,"");
    }

    public void setR(String r){
        editor.putString(R,r).commit();
    }


    public String getS(){
        return pref.getString(S,"");
    }

    public void setS(String s){
        editor.putString(S,s).commit();
    }


    public String getT(){
        return pref.getString(T,"");
    }

    public void setT(String t){
        editor.putString(T,t).commit();
    }


    public String getU(){
        return pref.getString(U,"");
    }

    public void setU(String u){
        editor.putString(U,u).commit();
    }


    public String getV(){
        return pref.getString(V,"");
    }


    public void setV(String v){
        editor.putString(V,v).commit();
    }


    public String getW(){
        return pref.getString(W,"");
    }

    public void setW(String w){
        editor.putString(W,w).commit();
    }


    public String getX(){
        return pref.getString(X,"");
    }



    public void setX(String x){
        editor.putString(X,x).commit();
    }


    public String getY(){
        return pref.getString(Y,"");
    }


    public void setY(String y){
        editor.putString(Y,y).commit();
    }



    public String getZ(){
        return pref.getString(Z,"");
    }


    public void setZ(String z){
        editor.putString(Z,z).commit();
    }

    //DOUBLE FUNCTION TO ADDHERE


    public String getAA(){
        return pref.getString(AA,"");

    }

    public void setAA(String a ){
        editor.putString(AA,a).commit();
    }


    public String getBB(){
        return pref.getString(BB,"");

    }

    public void setBB(String b){

        editor.putString(BB,b).commit();
    }

    public String getCC(){
        return pref.getString(CC,"");

    }


    public void setCC(String c){
        editor.putString(CC,c).commit();
    }


    public String getDD(){
        return pref.getString(DD,"");
    }


    public void setDD(String d){
        editor.putString(DD,d).commit();
    }

    public String getEE(){
        return pref.getString(EE,"");
    }


    public void setEE(String e){
        editor.putString(EE,e).commit();
    }


    public String getFF(){
        return pref.getString(FF,"");
    }


    public void setFF(String f){
        editor.putString(FF,f).commit();
    }


    public String getGG(){
        return pref.getString(GG,"");
    }


    public void setGG(String g){
        editor.putString(GG,g).commit();
    }


    public String getHH(){
        return pref.getString(HH,"");
    }


    public void setHH(String h){
        editor.putString(HH,h).commit();
    }



    public String getII(){
        return pref.getString(II,"");
    }

    public void setII(String i){
        editor.putString(II,i).commit();
    }


    public String getJJ(){
        return pref.getString(JJ,"");
    }

    public void setJJ(String j){
        editor.putString(JJ,j).commit();
    }


    public String getKK(){
        return pref.getString(KK,"");
    }


    public void setKK(String k){
        editor.putString(KK,k).commit();
    }



    public String getLL(){
        return pref.getString(LL,"");
    }

    public void setLL(String l){
        editor.putString(LL,l).commit();
    }


    public String getMM(){
        return pref.getString(MM,"");
    }


    public void setMM(String m){
        editor.putString(MM,m).commit();
    }


    public String getNN(){
        return pref.getString(N,"");
    }

    public void setNN(String n){
        editor.putString(NN,m).commit();
    }

    public String getOO(){
        return pref.getString(OO,"");
    }


    public void setOO(String n){
        editor.putString(OO,o).commit();
    }

    public String getPP(){
        return pref.getString(PP,"");
    }


    public void setPP(String p){
        editor.putString(PP,p).commit();
    }


    public String getQQ(){
        return pref.getString(QQ,"");
    }


    public void setQQ(String q){
        editor.putString(QQ,q).commit();
    }

    public String getRR(){
        return pref.getString(RR,"");
    }

    public void setRR(String r){
        editor.putString(RR,r).commit();
    }


    public String getSS(){
        return pref.getString(SS,"");
    }

    public void setSS(String s){
        editor.putString(SS,s).commit();
    }


    public String getTT(){
        return pref.getString(TT,"");
    }

    public void setTT(String t){
        editor.putString(TT,t).commit();
    }


    public String getUU(){
        return pref.getString(UU,"");
    }

    public void setUU(String u){
        editor.putString(UU,u).commit();
    }


    public String getVV(){
        return pref.getString(VV,"");
    }


    public void setVV(String v){
        editor.putString(VV,v).commit();
    }


    public String getWW(){
        return pref.getString(WW,"");
    }

    public void setWW(String w){
        editor.putString(WW,w).commit();
    }


    public String getXX(){
        return pref.getString(XX,"");
    }



    public void setXX(String x){
        editor.putString(XX,x).commit();
    }


    public String getYY(){
        return pref.getString(YY,"");
    }


    public void setYY(String y){
        editor.putString(YY,y).commit();
    }



    public String getZZ(){
        return pref.getString(ZZ,"");
    }


    public void setZZ(String z){
        editor.putString(ZZ,z).commit();
    }




//    public void setName (String name){
//        editor.putString(NAME, name).commit();
//    }
//
//    public String getName(){
//        return pref.getString(NAME,"");
//    }

    public String getLat() {
        return pref.getString(LAT, "");
    }

    public void setLat(String lat) {
        editor.putString(LAT, lat).commit();
    }

    public String getLon() {
        return pref.getString(LON, "");
    }

    public void setLon(String lon) {
        editor.putString(LON, lon).commit();
    }


    ///futur

    public String getUserName() {
        return pref.getString(USER_NAME, "");
    }

    public void setUserName(String name) {
        editor.putString(USER_NAME, name).commit();
    }

    public String getGender() {
        return pref.getString(USER_GENDER, "");
    }

    public void setGender(String gender) {
        editor.putString(USER_GENDER, gender).commit();
    }

    public String getUserIndustry() {
        return pref.getString(USER_INDUSTRY, "");
    }

    public void setUserIndustry(String name) {
        editor.putString(USER_INDUSTRY, name).commit();
    }

    public String getUserOccupation() {
        return pref.getString(USER_OCCUPATION, "");
    }

    public void setUserOccupation(String name) {
        editor.putString(USER_OCCUPATION, name).commit();
    }

    public String getUserExperinece() {
        return pref.getString(USER_EXPERINECE, "");
    }

    public void setUserExperinece(String name) {
        editor.putString(USER_EXPERINECE, name).commit();
    }

    public String getUserUniversity() {
        return pref.getString(USER_UNIVERSITY, "");
    }

    public void setUserUniversity(String name) {
        editor.putString(USER_UNIVERSITY, name).commit();
    }

    public String getSelectedLat() {
        return pref.getString(SELECTED_LAT, "");
    }

    public void setSelectedLat(String selectedLat) {
        editor.putString(SELECTED_LAT, selectedLat).commit();
    }


    public String getSelectedLon() {
        return pref.getString(SELECTED_LON, "");
    }

    public void setSelectedLon(String selectedLat) {
        editor.putString(SELECTED_LON, selectedLat).commit();
    }


    public String getMylocationLat() {
        return pref.getString(MYLOCATION_LAT, "");
    }

    public void setMylocationLat(String selectedLat) {
        editor.putString(MYLOCATION_LAT, selectedLat).commit();
    }


    public String getMylocationLon() {
        return pref.getString(MYLOCATION_LON, "");
    }

    public void setMylocationLon(String selectedLat) {
        editor.putString(MYLOCATION_LON, selectedLat).commit();
    }

    public String getLinkedinurl() {
        return pref.getString(LINKEDINURL, "");
    }

    public void setLinkedinurl(String url) {
        editor.putString(LINKEDINURL, url).commit();
    }


    public String getOther1() {
        return pref.getString(OTHER1, "");
    }

    public void setOther1(String url) {
        editor.putString(OTHER1, url).commit();
    }


    public String getOther2() {
        return pref.getString(OTHER2, "");
    }

    public void setOther2(String url) {
        editor.putString(OTHER2, url).commit();
    }


    public String getOther3() {
        return pref.getString(OTHER3, "");
    }

    public void setOther3(String url) {
        editor.putString(OTHER3, url).commit();
    }

    public String getMyaddress() {
        return pref.getString(MYADDRESS, "");
    }

    public void setMyaddress(String myaddress) {
        editor.putString(MYADDRESS, myaddress).commit();
    }

    public Bitmap getUserPhoto() {
        String img = pref.getString(USER_PHOTO, "");
        if (!img.equalsIgnoreCase("")) {
            byte[] b = Base64.decode(img, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            return bitmap;
        } else
            return null;
    }

    public void setUserPhoto(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        editor.putString(USER_PHOTO, encodedImage).commit();
    }


}
