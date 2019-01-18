package com.br.gridviewmovie;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import static android.media.CamcorderProfile.get;

public class TrailerAdapter extends ArrayAdapter<TrailerItem> {
    private static final String TAG = TrailerAdapter.class.getSimpleName();
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<TrailerItem> trailers = new ArrayList<TrailerItem>();
    ImageView iv_youtube_thumnail,iv_play;


    public TrailerAdapter(Context mContext, int layoutResourceId, ArrayList<TrailerItem>  trailers) {
        super(mContext, layoutResourceId,  trailers);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.trailers = trailers;
    }


    // Updates review data and refresh .

    public  void setTrailerData(ArrayList<TrailerItem> trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }


    @Override
      public TrailerItem getItem(int position) {
          return trailers.get(position);
      }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();

           // holder.TNameTextView = (TextView) row.findViewById(R.id.trailer_name);
            holder.TrailerView=(ImageView) row.findViewById(R.id.iv_youtube_thumnail);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

           TrailerItem trailerItem=trailers.get(position);
        Log.d(TAG,"position"+trailerItem);

        // holder.titleTextView.setText(item.getTitle());//2good
       // holder.TNameTextView.setText(trailerItem.getTrailerName());
       // Log.d(TAG,"IN adapter getContent"+trailerItem.getTrailerName());
        ////////////////
        String videoId=trailerItem.getKey();

        Log.e("VideoId is->","" + videoId);

        String img_url="http://img.youtube.com/vi/"+videoId+"/0.jpg"; // this is link which will give u thumnail image of that video

        // picasso jar file download image for u and set image in imagview

        Picasso.with(mContext)
                .load(img_url)
                .placeholder(R.mipmap.placeholder)
                .into(holder.TrailerView);


               return row;
    }

    static class ViewHolder {
        // TextView titleTextView;//2good
       // TextView authorTextView;
       // TextView TNameTextView;
        ImageView TrailerView;
    }



    public void clear() {
        // clear a list of movies
        trailers.clear();
        notifyDataSetChanged();
    }



}
