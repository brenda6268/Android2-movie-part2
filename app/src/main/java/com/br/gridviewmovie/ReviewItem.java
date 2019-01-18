package com.br.gridviewmovie;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class ReviewItem implements Parcelable {
    private String author;
    private String content;


    public ReviewItem() {
        super();
    }

public ReviewItem(String author, String content){
        this.author=author;
        this.content=content;
}


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }



    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private ReviewItem(Parcel rin) {

        author = rin.readString();
        content = rin.readString();


    }
    public static final Creator<ReviewItem> CREATOR = new Creator<ReviewItem>() {
        @Override
        public ReviewItem createFromParcel(Parcel rin) {
            return new ReviewItem(rin);
        }

        @Override
        public ReviewItem[] newArray(int size) {
            return new ReviewItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(author);
        parcel.writeString(content);


    }

}
