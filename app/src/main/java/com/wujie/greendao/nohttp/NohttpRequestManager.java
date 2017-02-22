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
        NoHttp.getRequestQueueInstance().add(0, request, callback);

    }

    public static Request<String> requestUpload( String url, String target, Binary binary, String Cookie, OnResponseListener<String> callback) {

        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);

        request.addHeader("aaaaa", request.getContentType());

        request.add("file", binary);
        request.add("target", target);
        request.add("filename", binary.getFileName());
        NoHttp.getRequestQueueInstance().add(0, request,callback);
        //CallServer.getRequestInstance().add(context, 0, request, callback, true, false);
        return request;
    }
}
