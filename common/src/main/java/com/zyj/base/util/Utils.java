package com.zyj.base.util;


import com.zyj.base.gps.GCJPointer;
import com.zyj.base.gps.WGSPointer;

/**
 * Created by lan on 2018-01-03.
 */

public class Utils {
    /*
     * 高德、谷歌中国坐标转标准坐标
     * @param lat 经度
     * @param lon 纬度
     */
    public static WGSPointer gdgps2Gps(double lat, double lon) {
        GCJPointer gcjPointer  = new GCJPointer(lat, lon);

        return gcjPointer.toWGSPointer();
    }
    /*
    * 标准坐标转高德、谷歌中国坐标
    * @param lat 经度
    * @param lon 纬度
    */
    public GCJPointer gps2Gdgps2(double lat, double lon)  {
        WGSPointer wgsPointer = new WGSPointer(lat, lon);

        return wgsPointer.toGCJPointer();
    }
}
