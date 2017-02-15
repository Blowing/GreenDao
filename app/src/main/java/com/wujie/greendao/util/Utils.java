package com.wujie.greendao.util;

import android.os.Build;
import android.util.Log;

import java.lang.reflect.Method;

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
}
