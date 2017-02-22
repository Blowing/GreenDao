package com.wujie.greendao.util;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

/**
 * Created by wujie on 2017/2/22.
 *
 */
public class WifiClient {
    /**
     * 获取网关地址
     * @param context
     * @return
     */
    public static String getGayWay(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
        if(dhcpInfo == null) {
            return  null;
        }
        return Formatter.formatIpAddress(dhcpInfo.gateway);
    }
}
