package com.twiscode.movie_stage1.Model;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Andree on 8/7/17.
 */

public class MovieContract {

    public static final String AUTHORITY = "com.twiscode.movie_stage1.movies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIES = "movies";



    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        // MIME TYPE
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.twiscode.movies";
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd/twiscode.movies";

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIEDB_ID = "movieDbID";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_IMG_URL = "imgUrl";
        public static final String COLUMN_BACKDROP_URL = "backdropUrl";

    }
}
