package com.abln.chat.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class IMUtils {
    public static final String TAG = IMUtils.class.getSimpleName();
    public static HashMap<String,Boolean> syncedChannels;

    // check for is Empty
    public static boolean isNullOrEmpty(String s) {
        if (s == null || s.length() == 0) {
            return true;
        } else if (s.matches("^\\s*$")) {
            return true;
        } else {
            return false;
        }
    }

    public static String getEmptyStringIfValueNull(String Char) {
        if (!IMUtils.isNullOrEmpty(Char)) {
            return Char;
        }
        return "";
    }

    public static String joinString(List<String> list) {
        return Arrays.toString(list.toArray()).substring(1, Arrays.toString(list.toArray()).length() - 1);
    }

    public static String formatPubNubTimeStamp(long timeStamp) {
//        timeStamp = timeStamp / 10000;

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);

        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");

        DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
        // OVERRIDE SOME symbols WHILE RETAINING OTHERS
        symbols.setAmPmStrings(new String[]{"AM", "PM"});
        simpleDateFormat.setDateFormatSymbols(symbols);

        return simpleDateFormat.format(calendar.getTime());
    }

    //Format a time in milliseconds to 'mm:ss' format
    public static String formatTimeToMMSS(long millis) {
        return String.format(Locale.US, "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }


    public static String formatPubNubTimeStampWithMonthName(long timeStamp) {
//        timeStamp = timeStamp / 10000;

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);

        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, hh:mm a");

        DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
        // OVERRIDE SOME symbols WHILE RETAINING OTHERS
        symbols.setAmPmStrings(new String[]{"AM", "PM"});
        simpleDateFormat.setDateFormatSymbols(symbols);

        return simpleDateFormat.format(calendar.getTime());
    }

    public static long getDeviceTimeInMillis() {
        Date currentTime = Calendar.getInstance().getTime();
        return currentTime.getTime();
    }

    public static DisplayImageOptions getImageDisplayOptions(int placeholderImage) {
        return new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true).build();
    }

    public static DisplayImageOptions getRoundedImageDisplayOptions(int placeholderImage) {
        return new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(1000))
                .showStubImage(placeholderImage)
                .showImageOnLoading(placeholderImage)
                .bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true).build();
    }

    public static void registerBroadCast(Context context, BroadcastReceiver broadcastReceiver, String broadCastAction) {
        LocalBroadcastManager myLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
        IntentFilter intentFilter = new IntentFilter(broadCastAction);
        myLocalBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    public static void unregisterBroadCast(Context context, BroadcastReceiver broadcastReceiver) {
        LocalBroadcastManager myLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
        myLocalBroadcastManager.unregisterReceiver(broadcastReceiver);
    }

    public static void setImageLoaderLibraryConfiguration(Context context) {
        //Initialize image loader
        File cacheDir = StorageUtils.getCacheDirectory(context);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(3) // default
                .threadPriority(Thread.NORM_PRIORITY - 2) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default
//                .diskCache(new LimitedAgeDiskCache(cacheDir, 24 * 60 * 60)) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(context)) // default
                .imageDecoder(new BaseImageDecoder(true)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }

    public static void setSyncedChannels(HashMap<String,Boolean> channelList){
        syncedChannels=new HashMap<>();
        syncedChannels.putAll(channelList);
    }

    public static HashMap<String, Boolean> getChannelsSyncTable(){
      if(syncedChannels!=null)
      return syncedChannels;
      else
          return null;
    }

    public static void updateSyncedChannels(String channelNo, Boolean value){
        syncedChannels.put(channelNo,value);

    }



}
