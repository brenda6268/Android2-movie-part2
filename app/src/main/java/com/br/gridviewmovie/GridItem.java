package com.br.gridviewmovie;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class GridItem implements Parcelable{
    private String image;
    private String title;
    private String averrate;
    private String overview;
    private String releasedate;
    //for review and trail
    private long id;

    public GridItem() {
       super();
    }

 public GridItem(long id, String title, String overview, String releasedate, String image, String averrate)

  {

      this.id = id;
      this.title = title;
      this.overview=overview;
      this.releasedate = releasedate;
      this.image= image;
      this.averrate = averrate;


  }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {        return title;    }

    public void setTitle(String title) {        this.title = title;    }

    public String getAverrate() {        return averrate;    }

    public void setAverrate(String averrate) {        this.averrate = averrate;    }

    public String getOverview() {        return overview;    }

    public void setOverview(String overview) {        this.overview = overview;    }

    public String getReleasedate() {        return releasedate;    }

    public void setReleasedate(String releasedate) {        this.releasedate = releasedate;    }

    private GridItem(Parcel in) {
        id = in.readLong();
        title = in.readString();
        overview = in.readString();
        releasedate = in.readString();
        image = in.readString();
        averrate = in.readString();

    }
    public static final Creator<GridItem> CREATOR = new Creator<GridItem>() {
        @Override
        public GridItem createFromParcel(Parcel in) {
            return new GridItem(in);
        }

        @Override
        public GridItem[] newArray(int size) {
            return new GridItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeString(releasedate);
        parcel.writeString(image);
        parcel.writeString(averrate);

    }
}
