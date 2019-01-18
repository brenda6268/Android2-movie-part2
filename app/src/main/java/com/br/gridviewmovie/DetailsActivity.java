package com.br.gridviewmovie;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;

import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.br.gridviewmovie.data.MovieContract;
import com.br.gridviewmovie.mode.MyGridView;
import com.br.gridviewmovie.mode.MyListView;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.widget.AdapterView;

import static android.view.View.NO_ID;

public class DetailsActivity extends AppCompatActivity {
    public static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";
    public static final String PARAM_KEY_API = "api_key";
    static final String TAG = DetailsActivity.class.getSimpleName();
    public ArrayList<TrailerItem> trailerList;
    private TextView titleTextView;
    private TextView averrateTextView;
    private TextView overviewTextView;
    private TextView releasedateTextView;

    private ImageView imageView;
    private GridItem movie;
    private GridItem movietest;
///////////////////
    private ScrollView mScrollView;
    String CURRENT_SCROLL_POSITION="scrollPosition";
private int sx;
private int sy;
    //////////////
    //like Collection
    private ImageView CollectionLike;
    public long movieID;
    //review
    private MyListView mReviewView;
    private ArrayList<ReviewItem> reviews;
    private String FEED_URL;
    private ReviewAdapter mReviewAdapter;
    private TextView noConnectReview;
    //trailer
    private MyGridView mTrailerView;
    private String TrailerKey;
    private ArrayList<TrailerItem> trailers;
    private String Trailer_URL;
    private TrailerAdapter mTrailerAdapter;
    private TextView noConnectTrailer;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting details screen layout
        setContentView(R.layout.activity_details_view);
        titleTextView = (TextView) findViewById(R.id.grid_item_title);
        averrateTextView = (TextView) findViewById(R.id.grid_item_averrate);
        overviewTextView = (TextView) findViewById(R.id.grid_item_overview);
        releasedateTextView = (TextView) findViewById(R.id.grid_item_releasedate);
        imageView = (ImageView) findViewById(R.id.grid_item_image);
        CollectionLike = (ImageView) findViewById(R.id.fav_heart);

        mScrollView=(ScrollView)findViewById(R.id.main_background);

       ////review
        noConnectReview=(TextView) findViewById(R.id.empty_review);
        reviews = new ArrayList<ReviewItem>();
        mReviewAdapter = new ReviewAdapter(this,  R.layout.review_item, reviews);
        MyListView mReviewView=(MyListView) findViewById(R.id.rv_reviews);
        mReviewView.setFocusable(false);
       ////trailer
        noConnectTrailer=(TextView) findViewById(R.id.empty_trailers);
        trailers=new ArrayList<>();
        mTrailerAdapter=new TrailerAdapter(this,R.layout.trailer_item,trailers);
        MyGridView mTrailerView=(MyGridView) findViewById(R.id.rv_trailers);
        mTrailerView.setFocusable(false);



        //
       //////
        // Restore the scroll position
if(savedInstanceState!=null){
    final int[] position = savedInstanceState.getIntArray("ARTICLE_SCROLL_POSITION");
    //final int positionX=savedInstanceState.getInt("xxsingle");
   // final int positionY=savedInstanceState.getInt("yysingle");
    // Log.d(TAG,"xxsingleout"+String.valueOf(positionX));
   // Log.d(TAG,"nullyysingleout"+String.valueOf(positionY));
    if(position != null)
        mScrollView.post(new Runnable() {
            public void run() {
                //mScrollView.scrollTo(positionX, positionY);
               // Log.d(TAG,"IMyysingleout"+String.valueOf(positionY));

                 mScrollView.scrollTo(position[0], position[1]);

            }
        });

}

        mScrollView.smoothScrollTo(0,-1);

        //////////////////////////////////
//Set Adapter
        //reviews
        //   mReviewView.setFocusable(false);
        mReviewView.setAdapter(mReviewAdapter);

        //trailers
        mTrailerView.setAdapter(mTrailerAdapter);


        //////focus
       // mScrollView.smoothScrollTo(0,20);
       // mScrollView.setFocusable(true);

        //trailer click to youtube
        mTrailerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                TrailerItem trailerItem = (TrailerItem) parent.getItemAtPosition(position);
                String trailerKey=trailerItem.getKey();
               // Log.d(TAG, "clicked key"+trailerKey);

                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailerKey));
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_BASE_URL + trailerKey));

               // Verify that the intent will resolve to an activity
                if (appIntent.resolveActivity(getPackageManager()) != null) {
                    // Open Youtube client
                    startActivity(appIntent);
                }
                else {
                    // Default to Web browser
                    startActivity(webIntent);
                }


               // Intent intentTrailer = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_BASE_URL + trailerKey));
               // System.out.println("intent = " + YOUTUBE_BASE_URL + trailerKey);
               // startActivity(intentTrailer);
            }
        });

////////


///////////////////////////////////////////////////////////////////////////////////
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //retrieves the moviedetails data    Get the data from intent if there are any.
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            movie=new GridItem();


            if(bundle.containsKey(getString(R.string.pItem))) {
                //Log.d(TAG, "into bundle contain key");
              // movieID = getIntent().getExtras().getLong(getString(R.string.pItem_id));
                movie = getIntent().getParcelableExtra(getString(R.string.pItem));
                movieID=movie.getId();

                //Log.d(TAG,movie.getTitle() );
                titleTextView.setText(movie.getTitle());
                averrateTextView.setText(movie.getAverrate());
                overviewTextView.setText(movie.getOverview());
                releasedateTextView.setText(movie.getReleasedate());

                String image= movie.getImage();
                String URL1 = getString(R.string.MOVIE_PATH) + image;


                Picasso.with(this).load(URL1)
                        .placeholder(R.mipmap.placeholder)
                        .into(imageView);

            } else {
                titleTextView.setText(R.string.no_data);
                averrateTextView.setText(R.string.no_data);
                overviewTextView.setText(R.string.no_data);
                releasedateTextView.setText(R.string.no_data);
                imageView.setImageResource(R.mipmap.placeholder);}
        }

            ////////////


        //set like heart initial
        setIconCollection();

       //set like heart by dynamic
        CollectionLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean Collection = checkCollection(movie.getId());
                if (Collection) {
                    deleteCollection(movieID);
                } else {
                    addToCollection(movieID);
                }
                setIconCollection();
            }
        });






        /////////////////////////////////////////////////////
        //review  URI and Adapter and start AsynTask

        Uri FEED_URI= Uri.parse(getString(R.string.MOVIE_DIS)).buildUpon()
                .appendPath(String.valueOf( movieID))
                .appendPath("reviews")
                .appendQueryParameter("api_key", BuildConfig.API_KEY)
                .build();
        FEED_URL= FEED_URI.toString();

        mReviewAdapter.clear();

        new DAsyncHttpTask().execute(FEED_URL);
        mReviewView.setFocusable(false);


        //trailer URI and Adapter and start AsynTask
        Uri Trailer_URI= Uri.parse(getString(R.string.MOVIE_DIS)).buildUpon()
                .appendPath(String.valueOf( movieID))
                .appendPath("videos")
                .appendQueryParameter("api_key", BuildConfig.API_KEY)
                .build();
        Trailer_URL= Trailer_URI.toString();

       // Log.d(TAG, "Trailer_URL"+Trailer_URL);

        mTrailerAdapter.clear();

        new TAsyncHttpTask().execute(Trailer_URL);

    mTrailerView.setFocusable(false);

    }


////////////////////////
@Override
protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putIntArray("ARTICLE_SCROLL_POSITION",
            new int[]{ mScrollView.getScrollX(), mScrollView.getScrollY()});
    outState.putInt("xxsingle",mScrollView.getScrollX());
    outState.putInt("yysingle",mScrollView.getScrollY());
    Log.d(TAG,"xxsingle"+String.valueOf(mScrollView.getScrollX()));
    Log.d(TAG,"yysingle"+String.valueOf(mScrollView.getScrollY()));
    Log.d(TAG,"xxxxx"+String.valueOf(mScrollView.getScrollX()));
    Log.d(TAG,"yyyyy"+String.valueOf(mScrollView.getScrollY()));
}

   /** protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int[] position = savedInstanceState.getIntArray("ARTICLE_SCROLL_POSITION");
        final int positionX=savedInstanceState.getInt("xxsingle");
        final int positionY=savedInstanceState.getInt("yysingle");
       // Log.d(TAG,"xxsingleout"+String.valueOf(positionX));
        Log.d(TAG,"yysingleout"+String.valueOf(positionY));
        if(position != null)
            mScrollView.post(new Runnable() {
                public void run() {
                    mScrollView.scrollTo(positionX, positionY);
                    Log.d(TAG,"IMyysingleout"+String.valueOf(positionY));

                   // mScrollView.scrollTo(position[0], position[1]);

                }
            });

        Log.d(TAG,"outxxxxx"+String.valueOf(mScrollView.getScrollX()));
        Log.d(TAG,"outyyyyy"+String.valueOf(mScrollView.getScrollY()));
    }*/
    ////////////////////////

    //trailers AsyncTask

    public class TAsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            try {
                // Create Apache HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse httpResponse = httpclient.execute(new HttpGet(params[0]));
                int statusCode = httpResponse.getStatusLine().getStatusCode();

                // 200 represents HTTP, this can check the internet connection.
                //if no net, statuscode!==200, the int result=0,leading onPostExecute show Toast.
                if (statusCode == 200) {
                    String response = streamToString(httpResponse.getEntity().getContent());
                    //Log.d(TAG, response);
                    parseTrailer(response);
                    result = 1; // Successful

                    //Log.d(TAG, "make sure have already read 200");

                } else {
                    result = 0; //Failed
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
//Log.d(TAG, "200 read out"+String.valueOf(result));
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. update UI

            if (result == 1) {
                //Log.d(TAG, "Trailer onPostExcecute ");
                if (trailers.isEmpty()){
                    noConnectTrailer.setVisibility(View.VISIBLE);
                }

                mTrailerAdapter.setTrailerData(trailers);
            } else {

                //if there is no Network,  the reviews and trailer is empty,set the text mention to open network

                noConnectTrailer.setText(R.string.Net_unconnect_T);
                noConnectTrailer.setVisibility(View.VISIBLE);
            }


        }
    }

    ////////////////////////////////////////
    //trailer Json
    private  ArrayList<TrailerItem> parseTrailer(String result) {

        //parsing json from API. movie review.
        try{
            JSONObject response = new JSONObject(result);
            JSONArray trailerResults= response.optJSONArray("results");
            TrailerItem trailerItem;
            for (int i = 0; i < trailerResults.length(); i++) {
                JSONObject post = trailerResults.optJSONObject(i);
                String trailerKey = post.optString("key");//2
                String trailerName = post.optString("name");
                // Log.d(TAG,author+content);
                trailerItem = new TrailerItem();

                //reviewItem.set values;
                trailerItem.setKey(trailerKey);
                trailerItem.setTrailerName(trailerName);

                trailers.add(trailerItem);
                trailerList=trailers;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return trailerList;
    }


    ///////////////////////////////////////////////////////////////////////////////////reviewAsyn
    public class DAsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            try {
                // Create Apache HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse httpResponse = httpclient.execute(new HttpGet(params[0]));
                int statusCode = httpResponse.getStatusLine().getStatusCode();

                // 200 represents HTTP, this can check the internet connection.
                //if no net, statuscode!==200, the int result=0,leading onPostExecute show Toast.
                if (statusCode == 200) {
                    String response = streamToString(httpResponse.getEntity().getContent());
                    //Log.d(TAG, response);
                    parseReview(response);
                    result = 1; // Successful

                    //Log.d(TAG, "make sure have already read 200");
                } else {
                    result = 0; //Failed
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. update UI

            if (result == 1) {
                //Log.d(TAG, "into onPostExcecute ");
                if (reviews.isEmpty()){
                    noConnectReview.setVisibility(View.VISIBLE);
                }
                mReviewAdapter.setReviewsData(reviews);
                // mReviewAdapter.notifyDataSetChanged();
            } else {
               ////if there is no Network,  the reviews and trailer is empty,set the text mention to open network
                noConnectReview.setText(R.string.Net_unconnect_R);
                noConnectReview.setVisibility(View.VISIBLE);
            }


        }
    }


    String streamToString(InputStream stream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        // Close stream
        if (null != stream) {
            stream.close();
        }
        // Log.d(TAG, "streamToString"+line);
        return result;
    }



    // Parsing the feed results and get the movie list

    private void parseReview(String result) {

        //parsing json from API. movie review.
        try{
            JSONObject response = new JSONObject(result);
            JSONArray reviewResults= response.optJSONArray("results");
            ReviewItem reviewItem;
            for (int i = 0; i < reviewResults.length(); i++) {
                JSONObject post = reviewResults.optJSONObject(i);
                String author = post.optString("author");//2
                String content = post.optString("content");
                if (content.equals("")){int Noresult=2;}
                // Log.d(TAG,author+content);
                reviewItem = new ReviewItem();

                //reviewItem.set values;
                reviewItem.setAuthor(author);
                reviewItem.setContent(content);
                //Log.d(TAG, "Grid set review Item"+String.valueOf(reviewItem.getContent()));
                reviews.add(reviewItem);
               // Log.d(TAG, "Grid set review Item"+String.valueOf(reviews));
            }
        }


        catch (JSONException e) {
            e.printStackTrace();
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////
    //content provider part
    // Add movie to the database

    private void addToCollection(Long movieID) {

        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        ContentResolver resolver = this.getContentResolver();
        ContentValues values = new ContentValues();
        values.clear();

        values.put(MovieContract.MovieEntry.MOVIE_ID, movieID);
        values.put(MovieContract.MovieEntry.MOVIE_TITLE, movie.getTitle());
        values.put(MovieContract.MovieEntry.MOVIE_OVERVIEW, movie.getOverview());
        values.put(MovieContract.MovieEntry.MOVIE_RELEASE_DATE, movie.getReleasedate());
        values.put(MovieContract.MovieEntry.MOVIE_POSTER, movie.getImage());
        values.put(MovieContract.MovieEntry.MOVIE_VOTE_AVERAGE, movie.getAverrate());

                 resolver.insert(uri, values);
        Toast.makeText(getApplicationContext(), "Movie added to My Favorites!", Toast.LENGTH_LONG).show();
    }


    // Delete movie from the database
    private void deleteCollection(Long movieID) {

        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        ContentResolver resolver = this.getContentResolver();


                resolver.delete(uri,
                MovieContract.MovieEntry.MOVIE_ID + " = ? ",
                new String[]{movieID + ""});

        Toast.makeText(getApplicationContext(), "Movie removed from My Favorites!", Toast.LENGTH_LONG).show();

    }


    // Check if the movie is already in the database
    private boolean checkCollection(Long movieID) {

        Uri uri = MovieContract.MovieEntry.buildMovieUri(movieID);
        ContentResolver resolver = this.getContentResolver();
        Cursor cursor = null;

        try {

            cursor = resolver.query(uri, null, null, null, null);
            if (cursor.moveToFirst())
                return true;

        } finally {

            if (cursor != null)
                cursor.close();

        }

        return false;
    }

    // Set Collection icon
    private void setIconCollection() {
        boolean Collection = checkCollection(movie.getId());
        ImageView addToCollection = findViewById(R.id.fav_heart);

        if (Collection) {
            addToCollection.setImageResource(R.drawable.heart_full);
        } else {
            addToCollection.setImageResource(R.drawable.heart_empty);
        }
    }

}
