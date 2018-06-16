package com.udacity.popularmovies.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Trailer implements Parcelable {

    private String mId;
    private String mIso_639_1;
    private String mIso_3166_1;
    private String mKey;
    private String mName;
    private String mSite;
    private int mSize;
    private String mType;

    public Trailer() { }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getmIso_639_1() {
        return mIso_639_1;
    }

    public void setmIso_639_1(String mIso_639_1) {
        this.mIso_639_1 = mIso_639_1;
    }

    public String getmIso_3166_1() {
        return mIso_3166_1;
    }

    public void setmIso_3166_1(String mIso_3166_1) {
        this.mIso_3166_1 = mIso_3166_1;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getmSite() {
        return mSite;
    }

    public void setmSite(String mSite) {
        this.mSite = mSite;
    }

    public int getmSize() {
        return mSize;
    }

    public void setmSize(int mSize) {
        this.mSize = mSize;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    private Trailer(Parcel in) {
        mId = in.readString();
        mIso_639_1 = in.readString();
        mIso_3166_1 = in.readString();
        mKey = in.readString();
        mName = in.readString();
        mSite = in.readString();
        mSize = in.readInt();
        mType = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mIso_639_1);
        parcel.writeString(mIso_3166_1);
        parcel.writeString(mKey);
        parcel.writeString(mName);
        parcel.writeString(mSite);
        parcel.writeInt(mSize);
        parcel.writeString(mType);
    }
}
