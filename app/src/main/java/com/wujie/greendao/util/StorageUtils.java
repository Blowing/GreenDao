package com.wujie.greendao.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by wujie on 2017/1/9.
 * 这是一个存储对象到sd卡上的工具类
 */
public class StorageUtils {

    /**
     *
     * @param file 文件名如“username.txt”
     * @param object 对象
     */
    public static void WriteObject(String file, Object object) {
        try {
            String sdPath = Utils.getSdPath()+"/TendaWifi/download";
            File fileSd = new File(sdPath);
            if (!fileSd.exists())
                fileSd.mkdirs();

            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(sdPath+"/"+file));
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param file 文件名如“username.txt”
     * @return
     */
    public static Object ReadObject(String file) {
        Object object;
        try {
            String sdPath = Utils.getSdPath()+"/TendaWifi/download";

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(sdPath+"/"+file));
            object = ois.readObject();
            return object;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }
}
