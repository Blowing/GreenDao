package com.wujie.greendao.nohttp;

import com.wujie.greendao.cons.Constants;
import com.yanzhenjie.nohttp.Binary;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * Created by wujie on 2017/2/22.
 *
 */
public class NohttpRequestManager {

    public static void requestCookie(String password, OnResponseListener<String> callback) {

        Request<String> request = NoHttp.createStringRequest(Constants.USB_IP + "/cgi-bin/luci/admin/datamanager/stok", RequestMethod.POST);
        request.add("username", "admin");
        request.add("password", password);
        //request.setMultipartFormEnable(true);
        NoHttp.getRequestQueueInstance().add(0, request, callback);

    }

    public static Request<String> requestUpload(int what, Binary binary, OnResponseListener<String> callback) {

        String [] s = Constants.USB_STOKE.split(";");
        String url = "";
        if(s.length == 2){
            url = Constants.USB_IP+"/upload/;"+s[1];
        } else {
            url = Constants.USB_IP+"/upload/;stok=";
        }
        String target = "/usb/sda1/Camera";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);

        request.addHeader("aaaaa", request.getContentType());

        request.add("file", binary);
        request.add("target", target);
        request.add("filename", binary.getFileName());
        NoHttp.getRequestQueueInstance().add(what, request,callback);
        return request;
    }
}
