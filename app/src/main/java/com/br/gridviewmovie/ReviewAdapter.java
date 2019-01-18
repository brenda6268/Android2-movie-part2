package com.br.gridviewmovie;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

public class ReviewAdapter extends ArrayAdapter<ReviewItem> {
    private static final String TAG = ReviewAdapter.class.getSimpleName();
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<ReviewItem> reviews = new ArrayList<ReviewItem>();



    public ReviewAdapter(Context mContext, int layoutResourceId, ArrayList<ReviewItem>  reviews) {
        super(mContext, layoutResourceId,  reviews);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.reviews = reviews;
    }


    // Updates review data and refresh .

    public  void setReviewsData(ArrayList<ReviewItem> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            // holder.titleTextView = (TextView) row.findViewById(R.id.grid_item_title);//2good

             holder.authorTextView = (TextView) row.findViewById(R.id.re_author);
             holder.contentTextView=(TextView)row.findViewById(R.id.re_content);

           // holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        ReviewItem reviewitem = reviews.get(position);
        // holder.titleTextView.setText(item.getTitle());//2good

        holder.authorTextView.setText(reviewitem.getAuthor());
        Log.d(TAG,"IN adapter getContent"+reviewitem.getContent());
        holder.contentTextView.setText(reviewitem.getContent());

        return row;
    }

    static class ViewHolder {
        // TextView titleTextView;//2good
         TextView authorTextView;
         TextView contentTextView;

       // ImageView imageView;
    }


  //  public void clear() {
        // clear a list of movies
      //  reviews.clear();
       // notifyDataSetChanged();
  //  }

    public void clear() {
        // clear a list of movies
        reviews.clear();
        notifyDataSetChanged();
    }

    // adding elements in list.
    public void add(ReviewItem review) {
        if (review != null) {
            reviews.add(review);
           // notifyItemInserted(reviews.size() - 1);
        }
    }


}
