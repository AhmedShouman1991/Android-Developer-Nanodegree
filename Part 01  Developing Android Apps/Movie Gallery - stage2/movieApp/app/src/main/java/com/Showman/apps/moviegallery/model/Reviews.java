package com.Showman.apps.moviegallery.model;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;

public class Reviews implements Parcelable {

    private String author;
    private String content;


    public static Creator<Reviews> CREATOR = new Creator<Reviews>() {
        public Reviews createFromParcel(Parcel parcel) {
            return new Reviews(parcel);
        }

        public Reviews[] newArray(int size) {
            return new Reviews[size];
        }
    };

    public Reviews(Parcel in) {
        author = in.readString();
        content = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
    }


    public Reviews(String author, String content) {
        this.author = author.trim();
        this.content = content.trim();
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
