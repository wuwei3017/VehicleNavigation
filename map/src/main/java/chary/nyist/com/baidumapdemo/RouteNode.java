package chary.nyist.com.baidumapdemo;

/**
 * Created by LuakyChay on 2018/5/29.
 */

public class RouteNode {

    public double longitude = 0;
    public double latitude = 0;
    public String name;

    public RouteNode(double longitude, double latitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

}
