package com.br.gridviewmovie.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {


    public static final String CONTENT_AUTHORITY = "com.br.gridviewmovie";
    public static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String MOVIES_PATH = "Mymovies";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(MOVIES_PATH).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + MOVIES_PATH;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + MOVIES_PATH;

        public static final String TABLE_NAME = "Mymovies";
        public static final String MOVIE_ID = "id";
        public static final String MOVIE_TITLE = "original_title";
        public static final String MOVIE_OVERVIEW = "overview";
        public static final String MOVIE_RELEASE_DATE = "release_date";
        public static final String MOVIE_POSTER = "poster_path";
        public static final String MOVIE_VOTE_AVERAGE = "vote_average";



        public static final String SORT_ORDER ="_id desc";


        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }




}
