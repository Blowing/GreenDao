package com.wujie.greendao.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by wujie on 2017/2/15.
 */
public class Utils {

    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
    private static final String TAG = "xiaomi";

    private static final String KEY_EMUI_API_LEVEL = "ro.build.hw_emui_api_level";
    private static final String HTAG = "huawei";
    /**
     *判断是不是MIUI系统
     */
    public static boolean isMIUI() {

        final BuildProperties properties = BuildProperties.newInstrance();

        try {

            Log.i(TAG, "code: "+properties.getProperty(KEY_MIUI_VERSION_CODE, null)
            +"\n + name:"+properties.getProperty(KEY_MIUI_VERSION_NAME, null)
                    +"\n + storage:" + properties.getProperty(KEY_MIUI_INTERNAL_STORAGE, null));
            return properties.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || properties.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || properties.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
        } catch (Exception e) {
            return  false;
        }
    }
    /**
     * 判断是不是EMUI
     */

    public static boolean isEMUI() {
        final BuildProperties properties = BuildProperties.newInstrance();

        try {

            Log.i(HTAG, "api_level: "+properties.getProperty(KEY_EMUI_API_LEVEL, null));
            return properties.getProperty(KEY_EMUI_API_LEVEL, null) != null;
        } catch (Exception e) {
            return  false;
        }
    }

    /**
     * 判断是不是魅族系统，对于Android5.1无效
     * @return
     */
    public static boolean isFlyme() {
        try {
            final Method method = Build.class.getMethod("hasSmartBar");
            if(method != null) {
                Log.i("meizu", "hasSmartBar" + (method != null));
            }
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }

    /**
     * 获取SD上的存储路径
     *
     * @return path
     */

    public static String getSdPath() {
        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file1 = Environment.getExternalStorageDirectory();
        if (!file1.canRead() || !file1.canWrite()) {
            File file2 = new File("/storage/sdcard1");
            if (file2.canRead() && file2.canWrite()) {
                sdPath = "/storage/sdcard1";
            }
        }

        return sdPath;
    }

    /**
     * dp 转化成px
     *
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     *
     * px 转化成dp
     */
    public static int px2dip(Context context, float pxValue) {
        final  float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 浏览手机sd上存储的照片
     * @param context
     */
//    private void getImages(final Context context) {
//        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
//            Toast.makeText(context, "没有sd卡", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                ContentResolver mContentResolver = context.getContentResolver();
//
//                //获取jpg,和png图片
//                List<String[]> list = new ArrayList<String[]>();
//                list.add(new String[] {"image/jpg","image/jpeg"});
//                list.add(new String[] {"image/gif","image/png"});
//                list.add(new String[] {"image/bmp", "image/wbmp"});
//                for(int i = 0 ; i < list.size(); i++) {
//                    Cursor mCursor = mContentResolver.query(mImageUri, null,
//                            MediaStore.Images.Media.MIME_TYPE + "=? or "
//                                    + MediaStore.Images.Media.MIME_TYPE + "=?",
//                            list.get(i), MediaStore.Images.Media.DATE_MODIFIED);
//
//                    while (mCursor.moveToNext()) {
//
//                        String path = mCursor.getString(mCursor
//                                .getColumnIndex(MediaStore.Images.Media.DATA));
//
//
//                        String parentName = new File(path).getParentFile().getName();
//
//
//
//                        if (!mGruopMap.containsKey(parentName)) {
//                            List<String> chileList = new ArrayList<String>();
//                            chileList.add(path);
//                            mGruopMap.put(parentName, chileList);
//                        } else {
//                            mGruopMap.get(parentName).add(path);
//                        }
//                    }
//
//                    mCursor.close();
//                }
//
//
//
//
//                mHandler.sendEmptyMessage(SCAN_OK);
//
//            }
//        }).start();
//
//    }

    /**
     * 获取手机上的所有相片
     * @param context
     * @return
     */
//    public static HashMap<String, List<String>> getSystemPhotoList(Context context)
//    {
//        List<String> result = new ArrayList<String>();
//        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//        HashMap<String, List<String>> mGroupMap = new HashMap<>();
//        ContentResolver contentResolver = context.getContentResolver();
//        Cursor cursor = contentResolver.query(uri, null, null, null, null);
//        if (cursor == null || cursor.getCount() <= 0) return null; // 没有图片
//        while (cursor.moveToNext())
//        {
//
//            int index = cursor
//                    .getColumnIndex(MediaStore.Images.Media.DATA);
//            String path = cursor.getString(index); // 文件地址
//            File file = new File(path);
//            if (file.exists())
//            {
//                Log.i("path", path);
//                String parentName = file.getParentFile().getAbsolutePath();
//
//                if(mGroupMap.containsKey(parentName)) {
//                    mGroupMap.get(parentName).add(path);
//                } else {
//                    List<String> childList = new ArrayList<>();
//                    childList.add(path);
//                    mGroupMap.put(parentName, childList);
//                }
//            }
//        }
//        cursor.close();
//        return mGroupMap ;
//    }

//    /**
//     * 获取手机上的视频地址
//     * @param context
//     * @return
//     */
//    public static HashMap<String, List<String>> getSystemVideoList(Context context)
//    {
//        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//        HashMap<String, List<String>> mGroupMap = new HashMap<>();
//        ContentResolver contentResolver = context.getContentResolver();
//        Cursor cursor = contentResolver.query(uri, null, null, null, null);
//        if (cursor == null || cursor.getCount() <= 0) return null; // 没有图片
//        while (cursor.moveToNext())
//        {
//
//            int index = cursor
//                    .getColumnIndex(MediaStore.Video.Media.DATA);
//
//            String path = cursor.getString(index); // 文件地址
//            File file = new File(path);
//            if (file.exists())
//            {
//                Log.i("path", path);
//                String parentName = file.getParentFile().getAbsolutePath();
//
//                if(mGroupMap.containsKey(parentName)) {
//                    mGroupMap.get(parentName).add(path);
//                } else {
//                    List<String> childList = new ArrayList<>();
//                    childList.add(path);
//                    mGroupMap.put(parentName, childList);
//                }
//            }
//
//        }
//        cursor.close();
//        return mGroupMap ;
//    }

    public static Observable<HashMap<String, List<String>>> getSystemVideoList(final Context context) {
        return Observable.create(new Observable.OnSubscribe<HashMap<String, List<String>>>() {
            @Override
            public void call(Subscriber<? super HashMap<String, List<String>>> subscriber) {
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                HashMap<String, List<String>> mGroupMap = new HashMap<>();
                ContentResolver contentResolver = context.getContentResolver();
                Cursor cursor = contentResolver.query(uri, null, null, null, null);
                if (cursor == null || cursor.getCount() <= 0)  {
                    subscriber.onNext(null);
                    subscriber.onCompleted();
                }
                while (cursor.moveToNext())
                {

                    int index = cursor
                            .getColumnIndex(MediaStore.Video.Media.DATA);

                    String path = cursor.getString(index); // 文件地址
                    File file = new File(path);
                    if (file.exists())
                    {
                        Log.i("path", path);
                        String parentName = file.getParentFile().getAbsolutePath();

                        if(mGroupMap.containsKey(parentName)) {
                            mGroupMap.get(parentName).add(path);
                        } else {
                            List<String> childList = new ArrayList<>();
                            childList.add(path);
                            mGroupMap.put(parentName, childList);
                        }
                    }

                }
                cursor.close();
                subscriber.onNext(mGroupMap);
                subscriber.onCompleted();
            }
        });
    }

    public static Observable<HashMap<String, List<String>>> getSystemPhotoList(final Context context)
    {
        return Observable.create(new Observable.OnSubscribe<HashMap<String, List<String>>>() {
            @Override
            public void call(Subscriber<? super HashMap<String, List<String>>> subscriber) {
                Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                HashMap<String, List<String>> mGroupMap = new HashMap<>();
                ContentResolver contentResolver = context.getContentResolver();
                Cursor cursor = contentResolver.query(uri, null, null, null, null);
                if (cursor == null || cursor.getCount() <= 0) {
                        subscriber.onNext(null);
                    subscriber.onCompleted();
                }

                while (cursor.moveToNext())
                {

                    int index = cursor
                            .getColumnIndex(MediaStore.Images.Media.DATA);
                    String path = cursor.getString(index); // 文件地址
                    File file = new File(path);
                    if (file.exists())
                    {
                        Log.i("path", Uri.fromFile(file).toString());
                        String parentName = file.getParentFile().getAbsolutePath();

                        if(mGroupMap.containsKey(parentName)) {
                            mGroupMap.get(parentName).add(path);
                        } else {
                            List<String> childList = new ArrayList<>();
                            childList.add(path);
                            mGroupMap.put(parentName, childList);
                        }
                    }
                }
                cursor.close();
                subscriber.onNext(mGroupMap);
                subscriber.onCompleted();
            }
        });
    }


    public static int calculateInSampleSize(BitmapFactory.Options op, int reqHeight, int reqWidth) {
        final int height = op.outHeight;
        final int width = op.outWidth;
        int inSampleSize = 1;
        if(height > reqHeight || width > reqHeight) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > 100 && (halfWidth / inSampleSize) > 100) {
                inSampleSize *= 2;
            }
        }
        return  inSampleSize;
    }

}
