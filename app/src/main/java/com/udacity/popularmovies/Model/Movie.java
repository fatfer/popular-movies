package com.udacity.popularmovies.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Movie implements Parcelable {

    private String mPosterPath;
    private boolean mAdult;
    private String mOverview;
    private String mReleaseDate;
    private List mGenreIDs;
    private String mId;
    private String mOriginalTitle;
    private String mOriginalLanguage;
    private String mTitle;
    private String mBackdropPath;
    private int mPopularity;
    private int mVoteCount;
    private boolean mVideo;
    private int mVoteAverage;

    /**
     * No args constructor for use in serialization or in Parceler library
     */
    public Movie() {}


    public String getmPosterPath() {
        return mPosterPath;
    }

    public void setmPosterPath(String mPosterPath) {
        this.mPosterPath = mPosterPath;
    }

    public boolean ismAdult() {
        return mAdult;
    }

    public void setmAdult(boolean mAdult) {
        this.mAdult = mAdult;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        this.mOverview = overview;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public List getmGenreIDs() {
        return mGenreIDs;
    }

    public void setmGenreIDs(List mGenreIDs) {
        this.mGenreIDs = mGenreIDs;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.mOriginalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return mOriginalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.mOriginalLanguage = originalLanguage;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getmBackdropPath() {
        return mBackdropPath;
    }

    public void setmBackdropPath(String mBackdropPath) {
        this.mBackdropPath = mBackdropPath;
    }

    public int getmPopularity() {
        return mPopularity;
    }

    public void setmPopularity(int mPopularity) {
        this.mPopularity = mPopularity;
    }

    public int getmVoteCount() {
        return mVoteCount;
    }

    public void setmVoteCount(int mVoteCount) {
        this.mVoteCount = mVoteCount;
    }

    public boolean isVideo() {
        return mVideo;
    }

    public void setVideo(boolean video) {
        this.mVideo = video;
    }

    public int getmVoteAverage() {
        return mVoteAverage;
    }

    public void setmVoteAverage(int mVoteAverage) {
        this.mVoteAverage = mVoteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mPosterPath);
        parcel.writeByte((byte) (mAdult ? 1 : 0));
        parcel.writeString(mOverview);
        parcel.writeString(mReleaseDate);
        parcel.writeList(mGenreIDs);
        parcel.writeString(mId);
        parcel.writeString(mOriginalTitle);
        parcel.writeString(mOriginalLanguage);
        parcel.writeString(mTitle);
        parcel.writeString(mBackdropPath);
        parcel.writeInt(mPopularity);
        parcel.writeInt(mVoteCount);
        parcel.writeByte((byte) (mVideo ? 1 : 0));
        parcel.writeInt(mVoteAverage);
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private Movie(Parcel in) {
        mPosterPath = in.readString();
        mAdult = in.readByte() != 0;
        mOverview = in.readString();
        mReleaseDate = in.readString();
        mGenreIDs = new ArrayList<>();
        in.readList(mGenreIDs, String.class.getClassLoader());
        mId = in.readString();
        mOriginalTitle = in.readString();
        mOriginalLanguage = in.readString();
        mTitle = in.readString();
        mBackdropPath = in.readString();
        mPopularity = in.readInt();
        mVoteCount = in.readInt();
        mVideo = in.readByte() != 0;
        mVoteAverage = in.readInt();
    }

}
