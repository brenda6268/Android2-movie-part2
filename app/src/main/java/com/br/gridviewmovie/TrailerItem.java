package com.br.gridviewmovie;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class TrailerItem implements Parcelable {
    private String Movieid;
    private String key;
    private String trailerName;



    public TrailerItem() {
        super();
    }

    public String getTrailerName() {
        return trailerName;
    }


    public TrailerItem(String Movieid, String key){
        this.Movieid=Movieid;
        this.key=key;

    }

    public void setTrailerName(String trailerName) {
        this.trailerName =trailerName;
    }


    public String getMovieid() {
        return Movieid;
    }

    public void setMovieid(String Movieid) {
        this.Movieid = Movieid;
    }



    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
///////////////////

    private TrailerItem(Parcel tin) {

        Movieid = tin.readString();
        key = tin.readString();


    }
    public static final Creator<TrailerItem> CREATOR = new Creator<TrailerItem>() {
        @Override
        public TrailerItem createFromParcel(Parcel tin) {
            return new TrailerItem(tin);
        }

        @Override
        public TrailerItem[] newArray(int size) {
            return new TrailerItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(Movieid);
        parcel.writeString(key);


    }





}

