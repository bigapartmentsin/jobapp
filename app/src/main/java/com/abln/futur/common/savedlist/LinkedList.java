//package com.abln.futur.common.savedlist;
//
//import android.annotation.SuppressLint;
//import android.app.IntentService;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.os.SystemClock;
//import android.view.View;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.loader.app.LoaderManager;
//import androidx.loader.content.AsyncTaskLoader;
//import androidx.loader.content.Loader;
//
//import com.abln.futur.common.searchjob.SearchResult;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Enumeration;
//import java.util.HashMap;
//
//public class LinkedList extends AppCompatActivity {
//
//
//
//    LinkedList activity=null;
//    int progress=0;
//
//    private void getLinkedList(){
//
//
//
//    }
//
//
//    private void listgetList(){
//
//
//        Collections.addAll();
//
//        HashMap<String, String > value = new HashMap<String, String >();
//
//
//
//
//    }
//
//
//    private void dataModifer(ArrayList<SearchResult> searchresults){
//
//
//    }
//
//
//    private void AccessStates(Boolean values){
//
//    }
//
//
//    private void ChangeAdapters(ArrayList<String> list, ArrayList<String> grid, ArrayList<String> allocateMemory,ArrayList<SearchResult> postion){
//
//        java.util.LinkedList list = getLinkedList();
//
//        boolean add = list.add(dataModifer(postion));
//
//
//    }
//
//
//
//    private class MyTask extends AsyncTask<String, Void, String> {
//
//        private MyTask task=null;
//
//        task=(MyTask) getLastNonConfigurationInstance();
//
//        @Override
//        protected String doInBackground(String... params) {
//            String url = params[0];
//            return doSomeWork(url);
//        }
//
//        private String doSomeWork(String url) {
//        }
//
//        @SuppressLint("WrongThread")
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            // do something with result
//
//
//            for (int i=0;i<20;i++) {
//                SystemClock.sleep(500);
//                publishProgress();
//            }
//            //TODO need to implement datacaching to control buffer overflow
//
//
//        }
//
//
//
//        public Object onRetainNonConfigurationInstance() {
//            task.detach();
//
//
//
//            return(task);
//        }
//    }
//
//
//
//
//    @Override
//    public Loader onCreateLoader(int id, Bundle args) {
//
//
//        return new MyLoader(LinkedList.this);
//    }
//
//    @Override
//    public void onLoadFinished(Loader loader, Object data) {
//
//    }
//
//    @Override
//    public void onLoaderReset(Loader loader) {
//
//        //TODO reset the main thread here :
//        //TODO once the grid view changed to list view
//
//    }
//
//
//
//
//
//    private class MyLoaderCallbacks implements LoaderManager.LoaderCallbacks {
//
//        @Override
//        public Loader onCreateLoader(int id, Bundle args) {
//            return new MyLoader(LinkedList.this);
//        }
//
//        @Override
//        public void onLoadFinished(Loader loader, Object data) {
//
//            //TODO load the dataStructure and finish the call back functions
//
//        }
//
//        @Override
//        public void onLoaderReset(Loader loader) {
//
//
//            //TODO reset the whole data from network and apply it
//
//        }
//    }
//
//    private class MyLoader extends AsyncTaskLoader {
//
//        public MyLoader(Context context) {
//            super(context);
//        }
//
//        @Override
//        public Object loadInBackground() {
//            return someWorkToDo();
//        }
//
//        private Object someWorkToDo() {
//
//
//            // TODO push the stack to main job and clear the thread once it is finshed
//        }
//
//    }
//
//
//    public class Grid extends Service {
//
//        @Override
//        public int onStartCommand(Intent intent, int flags, int startId) {
//            doSomeLongProccesingWork();
//            stopSelf();
//
//            return START_NOT_STICKY;
//        }
//
//        private void doSomeLongProccesingWork() {
//
//            //TODO implement Grid function callback to the Service thread
//
//        }
//
//        @Nullable
//        @Override
//        public IBinder onBind(Intent intent) {
//            return null;
//        }
//    }
//
//
//
//    public class List extends IntentService {
//
//        public List() {
//            super("ExampleService");
//        }
//
//        @Override
//        protected void onHandleIntent(Intent intent) {
//            doSomeShortWork();
//        }
//
//        private void doSomeShortWork() {
//
//            //TODO implement List fucntion to callback to the intent service
//
//        }
//    }
//
//
//
//    private void onAppliedButton(){
//
//
//
//        getMainExecutor().execute();
//
//    }
//
//
//    private void unsavedbutton(){
//
//
//
//
//
//
//    }
//
//    private void callBackOnapplied(){
//
//
//    }
//
//    private void onRefreshData(){
//
//
//    }
//
//    private void onSavedHandlers(){
//
//
//
//    }
//
//
//
//    public interface  viewHolder{
//        void showData();
//        void showApplied();
//        void showUndone();
//
//        void showNumnberOfthreads();
//
//    }
//
//
//
//}



