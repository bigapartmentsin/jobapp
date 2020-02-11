package com.abln.chat.ui.helper;

import android.Manifest.permission;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.amazonaws.util.IOUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.abln.chat.IMInstance;
import com.abln.chat.R;
import com.abln.chat.core.model.IMChatMessage;
import com.abln.chat.core.model.IMChatMessage.IMMultimediaInfo;
import com.abln.chat.ui.activities.IMChatMultiMediaSlideActivity;
import com.abln.chat.ui.gcm.IMChatMultiMediaUploadDownloadService;
import com.abln.chat.utils.IMConstants;
import com.abln.chat.utils.IMUIUtils;
import com.abln.chat.utils.IMUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.nio.channels.NonReadableChannelException;
import java.nio.channels.NonWritableChannelException;
import java.util.ArrayList;


public class ChatMultiMediaHelper {
    public static final String TAG = ChatMultiMediaHelper.class.getSimpleName();

    public static int REQUEST_TAKE_GALLERY_IMAGE_OR_VIDEO = 1000;
    public static int REQUEST_TAKE_CAMERA_IMAGE = 2000;
    public static int REQUEST_TAKE_CAMERA_VIDEO = 3000;
    public static final int REQUEST_CAMERA_ACCESS = 4000;
    public static final int REQUEST_STORAGE_ACCESS_FOR_CAMERA = 5000;
    public static final int REQUEST_GALLERY_ACCESS = 6000;
    public static final int REQUEST_LOCATION_ACCESS = 8000;
    public static boolean isCameraUri = false;

    private static String getImageThumbBytes(String imagePath) {
        int MAX_IMAGE_SIZE = 5000;

        Bitmap bitmap = resizedImage(imagePath);
        int streamLength = MAX_IMAGE_SIZE;
        int compressQuality = 39;
        byte[] bmpPicByteArray = null;

        while (streamLength >= MAX_IMAGE_SIZE && compressQuality > 11) {
            compressQuality = compressQuality - 5;
            ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
            bmpPicByteArray = bmpStream.toByteArray();
            streamLength = bmpPicByteArray.length;
            Log.d(TAG, "Size: " + streamLength);
        }

        return Base64.encodeToString(bmpPicByteArray, Base64.DEFAULT);
    }

    private static String getVideoThumbBytes(String videoPath) {
        int MAX_IMAGE_SIZE = 5000;

        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.MINI_KIND);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, 180, 180, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        String base64String = null;
        if (null != bitmap) {

            int streamLength = MAX_IMAGE_SIZE;
            int compressQuality = 39;
            byte[] bmpPicByteArray = null;

            while (streamLength >= MAX_IMAGE_SIZE && compressQuality > 11) {
                compressQuality = compressQuality - 5;
                ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
                bmpPicByteArray = bmpStream.toByteArray();
                streamLength = bmpPicByteArray.length;
                Log.d(TAG, "Size: " + streamLength);
            }
            base64String = Base64.encodeToString(bmpPicByteArray, Base64.DEFAULT);
        }

        return base64String;
    }

    private static Bitmap resizedImage(String imagePath) {
        int scaleSize = 320;

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);

        int srcWidth = bitmap.getWidth();
        int srcHeight = bitmap.getHeight();

        if (srcWidth > scaleSize || srcHeight > scaleSize) {
            Bitmap resizedBitmap = null;
            int originalWidth = bitmap.getWidth();
            int originalHeight = bitmap.getHeight();
            int newWidth = -1;
            int newHeight = -1;
            float multFactor = -1.0F;
            if (originalHeight > originalWidth) {
                newHeight = scaleSize;
                multFactor = (float) originalWidth / (float) originalHeight;
                newWidth = (int) (newHeight * multFactor);
            } else if (originalWidth > originalHeight) {
                newWidth = scaleSize;
                multFactor = (float) originalHeight / (float) originalWidth;
                newHeight = (int) (newWidth * multFactor);
            } else if (originalHeight == originalWidth) {
                newHeight = scaleSize;
                newWidth = scaleSize;
            }
            resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
            return resizedBitmap;
        } else {
            return bitmap;
        }
    }

    public static String getMimeType(Context context, Uri uri) {
        String type = null;
        String filePath = getGalleryFilePath(context, uri.toString());
        if (null != filePath) {
            String extension = MimeTypeMap.getFileExtensionFromUrl(filePath.replace(" ", ""));
            if (extension != null) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            }
        }
        return type;
    }

    public static String getExistingImageFromPLivePath(String fileName) {
        String filePath = getPLiveImagePath() + "/" + fileName + ".jpg";
        File file = new File(filePath);
        if (!file.exists()) {
            filePath = null;
        }
        return filePath;
    }

    public static String getExistingVideoFromPLivePath(String fileName) {
        String filePath = getPLiveVideoPath() + "/" + fileName + ".mp4";
        File file = new File(filePath);
        if (!file.exists()) {
            filePath = null;
        }
        return filePath;
    }

    public static String getExistingSentImageFromPLivePath(String fileName) {
        String filePath = getPLiveSentImagePath() + "/" + fileName + ".jpg";
        File file = new File(filePath);
        if (!file.exists()) {
            filePath = null;
        }
        return filePath;
    }

    public static String getExistingSentVideoFromPLivePath(String fileName) {
        String filePath = getPLiveSentVideoPath() + "/" + fileName + ".mp4";
        File file = new File(filePath);
        if (!file.exists()) {
            filePath = null;
        }
        return filePath;
    }

    public static boolean copySentImageToPLivePath(String inputFilePath, String fileName) {
        String outFilePath = getPLiveSentImagePath() + "/" + fileName + ".jpg";
        File file = new File(outFilePath);

        boolean bool = false;
        if (!file.exists()) {
            bool = copyFile(new File(inputFilePath), new File(outFilePath));
        }

        return bool;
    }

    public static boolean getFileSizeCompatibility(String filePath) {
        File file = new File(filePath);
        double bytes = 0;
        if (file.exists()) {
            bytes = file.length();
        }
        double kilobytes = (bytes / 1024);
        double megabytes = (kilobytes / 1024);
        if (megabytes > 200)
            return false;
        else return true;

    }

    public static boolean copySentVideoToPLivePath(String inputFilePath, String fileName) {
        String outFilePath = getPLiveSentVideoPath() + "/" + fileName + ".mp4";
        File file = new File(outFilePath);

        boolean bool = false;
        if (!file.exists()) {
            bool = copyFile(new File(inputFilePath), new File(outFilePath));
        }

        return bool;
    }

    private static boolean copyFile(File src, File dst) {
        boolean returnValue = true;

        FileChannel inChannel = null;
        FileChannel outChannel = null;

        try {
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(dst).getChannel();

        } catch (FileNotFoundException ex) {
            Log.d(TAG, "inChannel/outChannel FileNotFoundException");
            ex.printStackTrace();
            return false;
        }

        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);

        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "TransferTo IllegalArgumentException");
            ex.printStackTrace();
            returnValue = false;

        } catch (NonReadableChannelException ex) {
            Log.d(TAG, "TransferTo NonReadableChannelException");
            ex.printStackTrace();
            returnValue = false;

        } catch (NonWritableChannelException ex) {
            Log.d(TAG, "TransferTo NonWritableChannelException");
            ex.printStackTrace();
            returnValue = false;

        } catch (ClosedByInterruptException ex) {
            Log.d(TAG, "TransferTo ClosedByInterruptException");
            ex.printStackTrace();
            returnValue = false;

        } catch (AsynchronousCloseException ex) {
            Log.d(TAG, "TransferTo AsynchronousCloseException");
            ex.printStackTrace();
            returnValue = false;

        } catch (ClosedChannelException ex) {
            Log.d(TAG, "TransferTo ClosedChannelException");
            ex.printStackTrace();
            returnValue = false;

        } catch (IOException ex) {
            Log.d(TAG, "TransferTo IOException");
            ex.printStackTrace();
            returnValue = false;


        } finally {

            if (inChannel != null)
                try {
                    inChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            if (outChannel != null)
                try {
                    outChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return returnValue;
    }

    public static String getPLiveSentImagePath() {
        File file = new File(getPLiveImagePath(), "Sent");
        if (!file.exists()) {
            file.mkdirs();
        }

        return file.getAbsolutePath();
    }

    public static String getPLiveSentVideoPath() {
        File file = new File(getPLiveVideoPath(), "Sent");
        if (!file.exists()) {
            file.mkdirs();
        }

        return file.getAbsolutePath();
    }

    private static String getPLiveImagePath() {
        File file = new File(getPLiveMultiMediaPath(), "PLive Images");
        if (!file.exists()) {
            file.mkdirs();
        }

        return file.getAbsolutePath();
    }

    private static String getPLiveVideoPath() {
        File file = new File(getPLiveMultiMediaPath(), "PLive Videos");
        if (!file.exists()) {
            file.mkdirs();
        }

        return file.getAbsolutePath();
    }


    private static String getPLiveMultiMediaPath() {
        File file = new File(Environment.getExternalStorageDirectory(), "PLive");
        if (!file.exists()) {
            file.mkdirs();
        }

        return file.getAbsolutePath();
    }


    public static String getGalleryFilePath(final Context context, final String filePath) {
        Uri uri = Uri.parse(filePath);
        final boolean isKitKatOrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKatOrAbove && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }

            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }

            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }

        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            if (isCameraUri)
                return getFilePathFromURI(context, uri);
            else
                return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        } else
            return getRealPathFromURIDB(context, uri);

        return null;
    }


    private static String getRealPathFromURIDB(Context context, Uri contentUri) {
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String realPath = cursor.getString(index);
            cursor.close();
            return realPath;
        }
    }


    public static String getFilePathFromURI(Context context, Uri contentUri) {
        //copy file and send new file path
        String fileName = getFileName(contentUri);
        if (!TextUtils.isEmpty(fileName)) {
            File copyFile = new File(context.getCacheDir() + File.separator + fileName);
            copy(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    public static void copy(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            IOUtils.copy(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }


    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    public static String getVideoDuration(String videoFilePath) {
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(Uri.fromFile(new File(videoFilePath)).getPath());
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            return IMUtils.formatTimeToMMSS(Long.parseLong(time));

        } catch (Exception ex) {
            ex.printStackTrace();
            return "00:00";
        }
    }

    public static Bitmap modifyOrientation(Bitmap bitmap, String imageAbsolutePath) throws IOException {
        ExifInterface ei = new ExifInterface(imageAbsolutePath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    private static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static String getSenderImageFilePath(Context context, IMChatMessage chatMessage) {
        String path;
        if (null == chatMessage.imageOrVideoFilePath) {
            path = getExistingSentImageFromPLivePath(chatMessage.msgId);

        } else {
            path = getGalleryFilePath(context, chatMessage.imageOrVideoFilePath);
        }

        return path;
    }

    public static String getSenderVideoFilePath(Context context, IMChatMessage chatMessage) {
        String path;
        if (null == chatMessage.imageOrVideoFilePath) {
            path = getExistingSentVideoFromPLivePath(chatMessage.msgId);

        } else {
            path = getGalleryFilePath(context, chatMessage.imageOrVideoFilePath);
        }

        return path;
    }

    public static void uploadImageToS3Bucket(Context context, ImageView imageView, IMChatMessage chatMessage) {
        String imageFilePath = getSenderImageFilePath(context, chatMessage);

        if (null == chatMessage.msgMultimediaInfo) {
            chatMessage.msgMultimediaInfo = new IMMultimediaInfo();
        }
        chatMessage.msgMultimediaInfo.imageOrVideoThumb = getImageThumbBytes(imageFilePath);
        chatMessage.msgMultimediaInfo.imageHeight = imageView.getMeasuredHeight() + "";
        chatMessage.msgMultimediaInfo.imageWidth = imageView.getMeasuredWidth() + "";
        chatMessage.msgMultimediaInfo.videoDuration = "";
        startMultiMediaUploadDownloadService(context, chatMessage, imageFilePath, IMConstants.FILE_TYPE_IMAGE,
                IMConstants.TRANSFER_TYPE_UPLOAD);
    }

    public static void uploadVideoToS3Bucket(Context context, IMChatMessage chatMessage) {
        String videoFilePath = getSenderVideoFilePath(context, chatMessage);

        if (null == chatMessage.msgMultimediaInfo) {
            chatMessage.msgMultimediaInfo = new IMMultimediaInfo();
        }
        chatMessage.msgMultimediaInfo.imageOrVideoThumb = getVideoThumbBytes(videoFilePath);
        chatMessage.msgMultimediaInfo.imageHeight = "";
        chatMessage.msgMultimediaInfo.imageWidth = "";
        chatMessage.msgMultimediaInfo.videoDuration = getVideoDuration(videoFilePath);

        startMultiMediaUploadDownloadService(context, chatMessage, videoFilePath,
                IMConstants.FILE_TYPE_VIDEO, IMConstants.TRANSFER_TYPE_UPLOAD);
    }

    public static void downloadImageFromS3Bucket(Context context, IMChatMessage chatMessage) {
        String imageFilePath = getPLiveImagePath() + "/" + chatMessage.msgId + ".jpg";
        startMultiMediaUploadDownloadService(context, chatMessage, imageFilePath,
                IMConstants.FILE_TYPE_IMAGE, IMConstants.TRANSFER_TYPE_DOWNLOAD);
    }

    public static void downloadVideoFromS3Bucket(Context context, IMChatMessage chatMessage) {
        String videoFilePath = getPLiveVideoPath() + "/" + chatMessage.msgId + ".mp4";
        startMultiMediaUploadDownloadService(context, chatMessage, videoFilePath,
                IMConstants.FILE_TYPE_VIDEO, IMConstants.TRANSFER_TYPE_DOWNLOAD);
    }




    private static void startMultiMediaUploadDownloadService(Context context, IMChatMessage chatMessage,
                                                             String filePath, String fileType, String transferType) {
        Intent intent = new Intent(context, IMChatMultiMediaUploadDownloadService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(IMConstants.MULTIMEDIA_CHAT_MSG, chatMessage);
        bundle.putString(IMConstants.FILE_PATH, filePath);
        bundle.putString(IMConstants.FILE_TYPE, fileType);
        bundle.putString(IMConstants.TRANSFER_TYPE, transferType);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    public static void loadImageFromLocalStoragePath(Context context, String path, ImageView imageView) {
        if (null != path) {
            File imageFilePath = new File(path);
            String uri = Uri.fromFile(imageFilePath).toString();
            String decodedImgUri = Uri.decode(uri);

            String tag = null;
            Object obj = imageView.getTag();

            if (null != obj) {
                tag = obj.toString();
            }

            if (!TextUtils.equals(decodedImgUri, tag)) {
                ImageView chatImageView = setImageWithWidthAndHeight(context, path, imageView);
                ImageLoader.getInstance().displayImage(decodedImgUri,
                        new ImageViewAware(chatImageView, false), IMUtils.getImageDisplayOptions(0));
                chatImageView.setTag(decodedImgUri);
            }
        }
    }

    public static void loadVideoThumbnail(String videoPath, ImageView imageView) {
        if (null != videoPath) {
            String tag = null;
            Object obj = imageView.getTag();

            if (obj != null) {
                tag = obj.toString();
            }

            if (!TextUtils.equals(videoPath, tag)) {
                Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.MINI_KIND);
                bitmap = ThumbnailUtils.extractThumbnail(bitmap, 180, 180, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                imageView.setImageBitmap(bitmap);
                imageView.setTag(videoPath);
            }
        }
    }

    public static void loadBase64Thumbnail(Context context, ImageView imageView, IMChatMessage chatMessage, boolean isImageMsg) {
        byte[] decodedString = Base64.decode(chatMessage.msgMultimediaInfo.imageOrVideoThumb, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        if (isImageMsg) {
            if (Integer.valueOf(chatMessage.msgMultimediaInfo.imageWidth) > 180) {
                imageView.getLayoutParams().width = IMUIUtils.convertPixelsToDp(context, 180f);
            }
            if (Integer.valueOf(chatMessage.msgMultimediaInfo.imageHeight) > 180) {
                imageView.getLayoutParams().height = IMUIUtils.convertPixelsToDp(context, 180f);
            }
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.requestLayout();
        }
        imageView.setImageBitmap(decodedByte);
    }

    public static ImageView setImageWithWidthAndHeight(Context context, String path, ImageView imageView) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = context.getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;

        int max = (int) (dpHeight * 39.65) / 100;
        max = (int) (max * density);
        int min = (int) (dpHeight * 16.89) / 100;
        min = (int) (min * density);

        int maxWidth = max;
        int maxHeight = max;
        int minWidth = min;
        int minHeight = min;
        int displayImageHeight, displayImageWidth;

        /*int maxWidth = (int) Res.dimen(R.dimen.p_235_t_235);
        int maxHeight = (int) Res.dimen(R.dimen.p_235_t_235);
        int minWidth = (int) Res.dimen(R.dimen.p_100_t_100);
        int minHeight = (int) Res.dimen(R.dimen.p_100_t_100);*/


        int actualImageWidth = getLocalIMGWidth(path);
        int actualImageHeight = getLocalIMGHeight(path);

        if (actualImageHeight == 0 || actualImageWidth == 0) {
            displayImageHeight = maxHeight;
            displayImageWidth = maxWidth;
        } else {
            if (minWidth < actualImageWidth && actualImageWidth < maxWidth) {
                displayImageWidth = actualImageWidth;
            } else if (actualImageWidth <= minWidth) {
                displayImageWidth = minWidth;
            } else {
                displayImageWidth = maxWidth;
            }

            if (minHeight < actualImageHeight && actualImageHeight < maxHeight) {
                displayImageHeight = actualImageHeight;
            } else if (actualImageHeight <= minHeight) {
                displayImageHeight = minHeight;
            } else {
                displayImageHeight = maxHeight;
            }
        }

        imageView.getLayoutParams().height = displayImageHeight;
        imageView.getLayoutParams().width = displayImageWidth;

        return imageView;
    }

    private static int getLocalIMGHeight(String uri) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(uri, options);
        return options.outHeight;

    }

    private static int getLocalIMGWidth(String uri) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(uri, options);
        return options.outWidth;
    }

    public static ArrayList<String> getMultimediaFilesForParticularRoom(String roomId) {
        ArrayList<String> imageList = new ArrayList<>();

        ArrayList<IMChatMessage> chatMessageList = new ArrayList<>(IMInstance.getInstance().getIMDatabase()
                .getIMChatMessageDao().getChatMessageListByRoomId(roomId));

        for (int i = 0; i < chatMessageList.size(); i++) {
            if (!chatMessageList.get(i).msgType.equalsIgnoreCase(IMConstants.CHAT_MESSAGE_TYPE_TEXT)) {

                if (null != getExistingImageFromPLivePath(chatMessageList.get(i).msgId)) {
                    imageList.add(getExistingImageFromPLivePath(chatMessageList.get(i).msgId));

                } else if (null != getExistingSentImageFromPLivePath(chatMessageList.get(i).msgId)) {
                    imageList.add(getExistingSentImageFromPLivePath(chatMessageList.get(i).msgId));

                } else if (null != getExistingVideoFromPLivePath(chatMessageList.get(i).msgId)) {
                    imageList.add(getExistingVideoFromPLivePath(chatMessageList.get(i).msgId));

                } else if (null != getExistingSentVideoFromPLivePath(chatMessageList.get(i).msgId)) {
                    imageList.add(getExistingSentVideoFromPLivePath(chatMessageList.get(i).msgId));
                }
            }
        }
        return imageList;
    }

    public static void showPopupMenuForMultiMediaUploadOptions(final AppCompatActivity appCompatActivity,
                                                               View view, final Uri cameraImageUri,
                                                               final Uri cameraVideoUri) {
        if (!isStoragePermissionGranted(appCompatActivity) ||
                !isCameraPermissionGranted(appCompatActivity)) {
            requestCameraPermission(appCompatActivity);
            requestStoragePermission(appCompatActivity);
        }

        PopupMenu popup = new PopupMenu(appCompatActivity, view);
        popup.getMenuInflater().inflate(R.menu.im_menu_chat_multimedia_options, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.im_menu_camera_photo) {
                    isCameraUri = true;
                    openCameraToTakePhoto(appCompatActivity, cameraImageUri);
                } else if (item.getItemId() == R.id.im_menu_camera_video) {
                    isCameraUri = true;
                    openCameraToTakeVideo(appCompatActivity, cameraVideoUri);
                } else if (item.getItemId() == R.id.im_menu_gallery) {
                    isCameraUri = false;
                    openGalleryToSelectImageOrVideo(appCompatActivity);
                }
                return true;
            }
        });

        popup.show();
    }


    public static boolean checkMediaPermission_camera(final AppCompatActivity appCompatActivity) {
        if (!isCameraPermissionGranted(appCompatActivity)) {
            requestCameraPermission(appCompatActivity);
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkMediaPermission_storage(final AppCompatActivity appCompatActivity) {
        if (!isStoragePermissionGranted(appCompatActivity)) {
            requestStoragePermission(appCompatActivity);
            return false;
        } else {
            return true;
        }
    }



    public static boolean checkLocationPermission(final AppCompatActivity appCompatActivity) {
        if (!isLocationPermissionGranted(appCompatActivity)) {
            requestLocationPermission(appCompatActivity);
            return false;
        } else {
            return true;
        }
    }

    public static void openGallery(final AppCompatActivity appCompatActivity) {
        isCameraUri = false;
        openGalleryToSelectImageOrVideo(appCompatActivity);
    }

    public static void takePhoto(final AppCompatActivity appCompatActivity, final Uri cameraImageUri) {
        isCameraUri = true;
        openCameraToTakePhoto(appCompatActivity, cameraImageUri);
    }

    public static void takeVideo(final AppCompatActivity appCompatActivity, final Uri cameraVideoUri) {
        isCameraUri = true;
        openCameraToTakeVideo(appCompatActivity, cameraVideoUri);
    }

    public static void handlePermissionsForMultiMedia(AppCompatActivity appCompatActivity, int requestCode,
                                                      int[] grantResults, Uri cameraImageUri, Uri cameraVideoUri) {
        if (requestCode == REQUEST_CAMERA_ACCESS) {
            if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(appCompatActivity, appCompatActivity.getResources().getString(R.string.im_camera_permission_msg),
                        Toast.LENGTH_LONG).show();
            } else {
//                showCameraOptions(appCompatActivity, cameraImageUri, cameraVideoUri);
            }

        } else if (requestCode == REQUEST_STORAGE_ACCESS_FOR_CAMERA) {
            if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(appCompatActivity, appCompatActivity.getResources().
                        getString(R.string.im_storage_permission_for_multi_media_msg), Toast.LENGTH_LONG).show();
            } else {
                //showCameraOptions(appCompatActivity, cameraImageUri, cameraVideoUri);
            }

        } else if (requestCode == REQUEST_GALLERY_ACCESS) {
            if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(appCompatActivity, appCompatActivity.getResources().getString(R.string.im_gallery_permission_msg),
                        Toast.LENGTH_LONG).show();
            } else {
                //openGalleryToSelectImageOrVideo(appCompatActivity);
            }
        } else if (requestCode == REQUEST_LOCATION_ACCESS) {
            if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(appCompatActivity, appCompatActivity.getResources().getString(R.string.im_location_permission_msg),
                        Toast.LENGTH_LONG).show();
            } else {
                //openGalleryToSelectImageOrVideo(appCompatActivity);
            }
        }

    }

    public static void showCameraOptions(final AppCompatActivity appCompatActivity,
                                         final Uri cameraImageUri, final Uri cameraVideoUri) {
        String[] itemValue = appCompatActivity.getResources().getStringArray(R.array.im_chat_camera_options);

        if (isStoragePermissionGranted(appCompatActivity)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(appCompatActivity);
            builder.setTitle(appCompatActivity.getResources().getString(R.string.im_camera));
            ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(appCompatActivity,
                    R.layout.im_item_multi_media_spinner, itemValue);
            builder.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (which == 0) {
                                openCameraToTakePhoto(appCompatActivity, cameraImageUri);
                            } else {
                                openCameraToTakeVideo(appCompatActivity, cameraVideoUri);
                            }
                        }
                    }
            );

            builder.create().show();

        } else {
            requestStoragePermissionForCamera(appCompatActivity);
        }
    }

    private static boolean isCameraPermissionGranted(Context context) {
        return ContextCompat.checkSelfPermission(context, permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private static boolean isStoragePermissionGranted(Context context) {
        return (ActivityCompat.checkSelfPermission(context, permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private static boolean isLocationPermissionGranted(Context context) {
        return (ActivityCompat.checkSelfPermission(context, permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    private static void requestCameraPermission(AppCompatActivity appCompatActivity) {
        ActivityCompat.requestPermissions(appCompatActivity, new String[]{permission.CAMERA},
                REQUEST_CAMERA_ACCESS);
    }

    private static void requestStoragePermissionForCamera(AppCompatActivity appCompatActivity) {
        ActivityCompat.requestPermissions(appCompatActivity, new String[]{permission.READ_EXTERNAL_STORAGE,
                permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_ACCESS_FOR_CAMERA);
    }

    private static void requestStoragePermission(AppCompatActivity appCompatActivity) {
        ActivityCompat.requestPermissions(appCompatActivity, new String[]{permission.READ_EXTERNAL_STORAGE,
                permission.WRITE_EXTERNAL_STORAGE}, REQUEST_GALLERY_ACCESS);
    }

    private static void requestLocationPermission(AppCompatActivity appCompatActivity) {
        ActivityCompat.requestPermissions(appCompatActivity, new String[]{permission.ACCESS_FINE_LOCATION, permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_ACCESS);
    }


    public static void openGalleryToSelectImageOrVideo(AppCompatActivity appCompatActivity) {
        if (Build.VERSION.SDK_INT < 24) {
            Intent pickIntent = new Intent(Intent.ACTION_PICK);
            pickIntent.setType("image/* video/*");
            appCompatActivity.startActivityForResult(Intent.createChooser(pickIntent,
                    appCompatActivity.getResources().getString(R.string.im_select_img_video)),
                    REQUEST_TAKE_GALLERY_IMAGE_OR_VIDEO);
        } else {
            Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
            pickIntent.setType("image/* video/*");
            appCompatActivity.startActivityForResult(Intent.createChooser(pickIntent,
                    appCompatActivity.getResources().getString(R.string.im_select_img_video)),
                    REQUEST_TAKE_GALLERY_IMAGE_OR_VIDEO);
        }


    }

    private static void openCameraToTakePhoto(AppCompatActivity appCompatActivity, Uri imageUri) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        appCompatActivity.startActivityForResult(cameraIntent, REQUEST_TAKE_CAMERA_IMAGE);
    }

    private static void openCameraToTakeVideo(AppCompatActivity appCompatActivity, Uri videoUri) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
        appCompatActivity.startActivityForResult(cameraIntent, REQUEST_TAKE_CAMERA_VIDEO);
    }

    public static void openChatMultimediaSlideActivity(Context context, String roomId, String msgId) {
        Intent intent = new Intent(context, IMChatMultiMediaSlideActivity.class);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(IMConstants.CHAT_ROOM_ID, roomId);
        intent.putExtra(IMConstants.CHAT_MESSAGE_ID, msgId);
        context.startActivity(intent);
    }

    public static void openChatVideoPlayer(Context context, String videoPath) {
        if (null != videoPath) {
            try {
                Uri uri = FileProvider.getUriForFile(context,
                        context.getApplicationContext()
                                .getPackageName() + ".provider",
                        new File(videoPath));
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(uri, "video/*");
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
