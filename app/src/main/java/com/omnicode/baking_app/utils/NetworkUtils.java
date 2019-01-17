package com.omnicode.baking_app.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import timber.log.Timber;

public final class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    //URL to get access to popular movies and top rated movies
    private static final String BAKING_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
    private static final String BAKING_FILE = "baking.json";

    //URL to get Images
    private static final String TMDB_IMAGE_URL = "http://image.tmdb.org/t/p/";

    final static String SIZE_PARAM = "w780";
    final static String PARAM_API_KEY = "api_key";
    final static String VALUE_API_KEY = "";
    final static String PATH_TRAILERS = "videos";
    final static String PATH_REVIEWS = "reviews";

    //URL for youtube intent
    final static String YOUTUBE_VIDEO_URL = "https://www.youtube.com/watch";
    final static String YOUTUBE_VIDEO_KEY = "v";


    public static URL buildUrlLoadRecipes() {
        //DONE: Hier Pfad zu JSON Datei einbauen
        Uri finalUri = Uri.parse(BAKING_BASE_URL).buildUpon()
                .appendPath(BAKING_FILE)
                //.appendQueryParameter(PARAM_API_KEY, VALUE_API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(finalUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Timber.d("XX Final URI " + url);

        return url;
    }

    /* imagePath e.g.  */
    public static URL buildUrlLoadImage(String imagePath) {
        Uri finalUri = Uri.parse(TMDB_IMAGE_URL).buildUpon()
                .appendPath(SIZE_PARAM)
                .appendEncodedPath(imagePath)
                .build();
        URL url = null;
        try {
            url = new URL(finalUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "ImagePath " + url);

        return url;
    }

    //api_key necessary!
    public static URL buildUrlLoadTrailers(int movieId) {
        Uri finalUri = Uri.parse(BAKING_BASE_URL).buildUpon()
                .appendPath(Integer.toString(movieId))
                .appendPath(PATH_TRAILERS)
                .appendQueryParameter(PARAM_API_KEY, VALUE_API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(finalUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Final URI Trailers " + url);

        return url;
    }

    //api_key necessary!
    public static URL buildUrlLoadReviews(int movieId) {
        Uri finalUri = Uri.parse(BAKING_BASE_URL).buildUpon()
                .appendPath(Integer.toString(movieId))
                .appendPath(PATH_REVIEWS)
                .appendQueryParameter(PARAM_API_KEY, VALUE_API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(finalUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Final URI Reviews " + url);

        return url;
    }

    //create url for playing trailers in youtube app or webbrowser
    public static Uri buildUriPlayVideo(String movieId) {
        Uri finalUri = Uri.parse(YOUTUBE_VIDEO_URL).buildUpon()
                .appendQueryParameter(YOUTUBE_VIDEO_KEY, movieId)
                .build();
        /*URL url = null;
        try {
            url = new URL(finalUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }*/
        Log.v(TAG, "Youtube video URI " + finalUri);

        return finalUri;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getJSONDataFromUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static boolean InternetAvailable (Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
        /*try {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
            sock.close();
            return true;
        } catch (IOException e) { return false; }*/
    }

}
