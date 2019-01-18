package com.br.gridviewmovie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;


import android.content.ContentResolver;

import android.content.Intent;
import android.database.Cursor;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.br.gridviewmovie.data.MovieContract;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.System.out;

public class GridViewActivity extends AppCompatActivity {
    private static final String TAG = GridViewActivity.class.getSimpleName();

    private GridView mGridView;
    private ProgressBar mProgressBar;

    private GridViewAdapter mGridAdapter;
    private ArrayList<GridItem> mGridData;
    private String FEED_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridview);


        mGridView = (GridView) findViewById(R.id.gridView);
       // mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
       //int index=0;// = mGridView.getFirstVisiblePosition();
        //Initialize with empty data
        mGridData = new ArrayList<>();
        mGridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, mGridData);
////////////////////////////////////////////////////////

        if (savedInstanceState == null) {



            Uri FEED_URI= Uri.parse(getString(R.string.MOVIE_DIS)).buildUpon()
                    .appendPath(getString(R.string.MOVIE_SORT_popular))
                    .appendQueryParameter("api_key", BuildConfig.API_KEY)
                    .build();

            FEED_URL= FEED_URI.toString();
            Log.d(TAG, "begin url"+FEED_URL);
            new AsyncHttpTask().execute(FEED_URL);
            mGridView.setAdapter(mGridAdapter);
        } else {
            // Get data from local resources
            mGridView.smoothScrollToPosition(savedInstanceState.getInt("gindex"));
           //int currentScrollPosition = savedInstanceState.getInt("CURRENT_SCROLL_POSITION", 0);
            Parcelable[] parcelable = savedInstanceState.
                    getParcelableArray(getString(R.string.myparcelable));

            if (parcelable != null) {
                int numMovieObjects = parcelable.length;
                GridItem[] movies = new GridItem[numMovieObjects];
                for (int i = 0; i < numMovieObjects; i++) {
                    movies[i] = (GridItem) parcelable[i];
                }
                for (GridItem movie : movies) {
                    if (movie != null) {
                        Log.d(TAG, "parcelable" + movie);
                        mGridAdapter.add(movie);
                    }
                }
                // Load movie objects into view
                mGridView.setAdapter(mGridAdapter);
               // mGridView.smoothScrollToPosition(index);
            }
        }


    ////////////////////////////////////////////////////////////////////////
       //* mGridView.setAdapter(mGridAdapter);

        //Grid view click event
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                GridItem item = (GridItem) parent.getItemAtPosition(position);
                Log.d(TAG, "click take item");

                Intent intent = new Intent(GridViewActivity.this, DetailsActivity.class);
               //put data into intent passing to DetailsActivity.class
               /** intent.putExtra("title", item.getTitle()).
                       putExtra("image", item.getImage()).
                        putExtra("averrate", item.getAverrate()).
                        putExtra("overview", item.getOverview()).
                        putExtra("releasedate", item.getReleasedate()).
                        putExtra("id",item.getId()).
                        putExtra(getString(R.string.pItem), item);*/

                intent.putExtra(getString(R.string.pItem), item);

                 //Start details activity
                startActivity(intent);
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.sort_popular);

        //mProgressBar.setVisibility(View.VISIBLE);
    }
/////////////////////////////////////
@Override
protected void onSaveInstanceState(Bundle outState) {
    //outState.putInt("CURRENT_SCROLL_POSITION", mGridView.findFirstVisibleItemPosition());
   // int index=mGridView.getFirstVisiblePosition();
    int gindex=mGridView.getFirstVisiblePosition();
    int numMovieObjects = mGridView.getCount();
    if (numMovieObjects > 0) {
        // Get Movie objects from gridview
        GridItem[] movies = new GridItem[numMovieObjects];
        for (int i = 0; i < numMovieObjects; i++) {
            movies[i] = (GridItem) mGridView.getItemAtPosition(i);
        }

        // Save Movie objects to bundle
        outState.putParcelableArray(getString(R.string.myparcelable), movies);
        outState.putString("FEED_URL", FEED_URL);
        outState.putInt("gindex",gindex);
    }

    super.onSaveInstanceState(outState);
}


 ////////////////////////////////////
    //sort_menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //choose sort_menu to get different FEED_URL
        if (id == R.id.sort_popular ) {


            Uri FEED_URI= Uri.parse(getString(R.string.MOVIE_DIS)).buildUpon()
                    .appendPath(getString(R.string.MOVIE_SORT_popular))
                    .appendQueryParameter("api_key", BuildConfig.API_KEY)
                    .build();

            FEED_URL= FEED_URI.toString();
           // Log.d(TAG, "popular url"+FEED_URL);


            mGridAdapter.clear();
            new AsyncHttpTask().execute(FEED_URL);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(R.string.sort_popular);
            return true;
        }


        else if(id ==R.id.sort_rate){


            Uri FEED_URI= Uri.parse(getString(R.string.MOVIE_DIS)).buildUpon()
                    .appendPath(getString(R.string.MOVIE_SORT_rate))
                    .appendQueryParameter("api_key", BuildConfig.API_KEY)
                    .build();
            FEED_URL= FEED_URI.toString();

           // Toast.makeText(getApplicationContext(), FEED_URL, Toast.LENGTH_LONG).show();
           // Log.d(TAG, "rate url"+FEED_URL);
            mGridAdapter.clear();
        new AsyncHttpTask().execute(FEED_URL);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(R.string.sort_rate);
        return true;
    }

        else if(id ==R.id.sort_like){

            showLikes();
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(R.string.sort_like);
            return true;
        }

    else return super.onOptionsItemSelected(item);
}


    //Downloading data asynchronously
    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

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
                    parseResult(response);
                    result = 1; // Successful
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
                mGridAdapter.setGridData(mGridData);
            } else {
                Toast.makeText(GridViewActivity.this, R.string.Net_unconnect, Toast.LENGTH_SHORT).show();

            }

            //Hide progressbar
           // mProgressBar.setVisibility(View.GONE);
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
        Log.d(TAG, result);
        return result;
    }

    // Parsing the feed results and get the movie list

    private void parseResult(String result) {


        //parsing json from API. movie information .
        try{
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("results");
            GridItem item;
            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                String title = post.optString("title");//2
                String averrate = post.optString("vote_average");
                String overview=post.optString("overview");
                String releasedate=post.optString("release_date");
                //used for review and trail

                Long id=post.optLong("id");
              //Log.d(TAG, "Grid get id"+String.valueOf(id));

                item = new GridItem();
                String poster_path=post.optString("poster_path");



                //item.set values;
                item.setImage(poster_path);
                item.setTitle(title);
                item.setAverrate(averrate);
                item.setOverview(overview);
                item.setReleasedate(releasedate);
                item.setId(id);
               // Log.d(TAG, "Grid set id"+String.valueOf(item.getId()));


                mGridData.add(item);

            }
        }


        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showLikes() {
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        ContentResolver resolver = getContentResolver();
        Cursor cursor = null;

        try {

            cursor = resolver.query(
                    uri,
                    null,
                    null,
                    null,
                    null);

            mGridAdapter.clear();

            if (cursor.moveToFirst()) {
                do {
                   GridItem movie = new GridItem(cursor.getInt(1), cursor.getString(2),
                            cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));

                            mGridAdapter.add(movie);
                } while (cursor.moveToNext());
            }

        } finally {

            if (cursor != null)
                cursor.close();

        }
    }

}