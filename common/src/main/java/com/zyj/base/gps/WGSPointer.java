package com.zyj.base.gps;

import android.os.Parcel;
import android.os.Parcelable;

public class WGSPointer extends GeoPointer implements Cloneable, Parcelable {

    public WGSPointer() {
    }

    public WGSPointer(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public GCJPointer toGCJPointer() {
        if (TransformUtil.outOfChina(this.latitude, this.longitude)) {
            return new GCJPointer(this.latitude, this.longitude);
        }
        double[] delta = TransformUtil.delta(this.latitude, this.longitude);
        return new GCJPointer(this.latitude + delta[0], this.longitude + delta[1]);
    }

    @Override
    public WGSPointer clone() throws CloneNotSupportedException {
        return (WGSPointer) super.clone();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected WGSPointer(Parcel in) {
        super(in);
    }

    public static final Creator<WGSPointer> CREATOR = new Creator<WGSPointer>() {
        @Override
        public WGSPointer createFromParcel(Parcel source) {
            return new WGSPointer(source);
        }

        @Override
        public WGSPointer[] newArray(int size) {
            return new WGSPointer[size];
        }
    };
}
