package com.zyj.base.gps;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;

public class GeoPointer implements Cloneable, Parcelable {
    static DecimalFormat df = new DecimalFormat("0.000000");
    double longitude;
    double latitude;

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

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else {
            if (other instanceof GeoPointer) {
                GeoPointer otherPointer = (GeoPointer) other;
                return df.format(latitude).equals(df.format(otherPointer.latitude))
                        && df.format(longitude).equals(df.format(otherPointer.longitude));
            } else {
                return false;
            }
        }
    }

    public String toString() {
        return longitude + "," + latitude;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public double distance(GeoPointer target) {
        double earthR = 6371000;
        double x =
                Math.cos(this.latitude * Math.PI / 180) * Math.cos(target.latitude * Math.PI / 180)
                        * Math.cos((this.longitude - target.longitude) * Math.PI / 180);
        double y = Math.sin(this.latitude * Math.PI / 180) * Math.sin(target.latitude * Math.PI / 180);
        double s = x + y;
        if (s > 1) {
            s = 1;
        }
        if (s < -1) {
            s = -1;
        }
        double alpha = Math.acos(s);
        double distance = alpha * earthR;
        return distance;
    }

    public GeoPointer() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
    }

    protected GeoPointer(Parcel in) {
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
    }

    public static final Creator<GeoPointer> CREATOR = new Creator<GeoPointer>() {
        @Override
        public GeoPointer createFromParcel(Parcel source) {
            return new GeoPointer(source);
        }

        @Override
        public GeoPointer[] newArray(int size) {
            return new GeoPointer[size];
        }
    };
}
