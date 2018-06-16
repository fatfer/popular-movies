package com.udacity.popularmovies.Utils;

import android.support.annotation.NonNull;
import com.udacity.popularmovies.Model.Movie;
import com.udacity.popularmovies.Model.Review;
import com.udacity.popularmovies.Model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class Json {
    private static final String JSON_RESULTS_KEY = "results";
    private static final String JSON_POSTER_PATH_KEY = "poster_path";
    private static final String JSON_ADULT_KEY = "adult";
    private static final String JSON_OVERVIEW_KEY = "overview";
    private static final String JSON_RELEASE_DATE_KEY = "release_date";
    private static final String JSON_GENRE_IDS_KEY = "genre_ids";
    private static final String JSON_ID_KEY = "id";
    private static final String JSON_ORIGINAL_TITLE_KEY = "original_title";
    private static final String JSON_ORIGINAL_LANGUAGE_KEY = "original_language";
    private static final String JSON_TITLE_KEY = "title";
    private static final String JSON_BACKDROP_PATH_KEY = "backdrop_path";
    private static final String JSON_POPULARITY_KEY = "popularity";
    private static final String JSON_VOTE_COUNT_KEY = "vote_count";
    private static final String JSON_VIDEO_KEY = "video";
    private static final String JSON_VOTE_AVERAGE_KEY = "vote_average";

    private static final String JSON_ISO_639_1_KEY = "iso_639_1";
    private static final String JSON_ISO_3166_1 = "iso_3166_1";
    private static final String JSON_KEY_KEY = "key";
    private static final String JSON_NAME_KEY = "name";
    private static final String JSON_SITE_KEY = "site";
    private static final String JSON_SIZE_KEY = "size";
    private static final String JSON_TYPE_KEY = "type";

    private static final String JSON_AUTHOR = "author";
    private static final String JSON_CONTENT = "content";


    public static List<Movie> parseMoviesJson(String json) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(json);
        JSONArray results = jsonObject.getJSONArray(JSON_RESULTS_KEY);

        for( int i = 0; i < results.length(); i++){
            JSONObject result = results.getJSONObject(i);
            Movie movie = getMovie(result);
            movies.add(movie);
        }

        return movies;
    }

    public static List<Trailer> parseTrailersJson(String json) throws JSONException {
        List<Trailer> trailers = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(json);
        JSONArray results = jsonObject.getJSONArray(JSON_RESULTS_KEY);

        for( int i = 0; i < results.length(); i++){
            JSONObject result = results.getJSONObject(i);
            Trailer trailer = getTrailer(result);
            trailers.add(trailer);
        }

        return trailers;
    }

    public static List<Review> parseReviewsJson(String json) throws JSONException {
        List<Review> reviews = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(json);
        JSONArray results = jsonObject.getJSONArray(JSON_RESULTS_KEY);

        for( int i = 0; i < results.length(); i++){
            JSONObject result = results.getJSONObject(i);
            Review review = getReview(result);
            reviews.add(review);
        }

        return reviews;
    }

    @NonNull
    private static Movie getMovie(JSONObject result) throws JSONException {
        Movie movie = new Movie();

        movie.setmPosterPath(result.optString(JSON_POSTER_PATH_KEY));
        movie.setmAdult(result.optBoolean(JSON_ADULT_KEY));
        movie.setOverview(result.optString(JSON_OVERVIEW_KEY));
        movie.setmReleaseDate(result.optString(JSON_RELEASE_DATE_KEY));
        movie.setmGenreIDs(jsonArrayToList(result.optJSONArray(JSON_GENRE_IDS_KEY)));
        movie.setId(result.optString(JSON_ID_KEY));
        movie.setOriginalTitle(result.optString(JSON_ORIGINAL_TITLE_KEY));
        movie.setOriginalLanguage(result.optString(JSON_ORIGINAL_LANGUAGE_KEY));
        movie.setTitle(result.optString(JSON_TITLE_KEY));
        movie.setmBackdropPath(result.optString(JSON_BACKDROP_PATH_KEY));
        movie.setmPopularity(result.optInt(JSON_POPULARITY_KEY));
        movie.setmVoteCount(result.getInt(JSON_VOTE_COUNT_KEY));
        movie.setVideo(result.optBoolean(JSON_VIDEO_KEY));
        movie.setmVoteAverage(result.optInt(JSON_VOTE_AVERAGE_KEY));

        return movie;
    }

    @NonNull
    private static Trailer getTrailer(JSONObject result) throws JSONException {
        Trailer trailer = new Trailer();

        trailer.setId(result.optString(JSON_ID_KEY));
        trailer.setmIso_639_1(result.optString(JSON_ISO_639_1_KEY));
        trailer.setmIso_3166_1(result.optString(JSON_ISO_3166_1));
        trailer.setmKey(result.optString(JSON_KEY_KEY));
        trailer.setName(result.optString(JSON_NAME_KEY));
        trailer.setmSite(result.optString(JSON_SITE_KEY));
        trailer.setmSize(result.optInt(JSON_SIZE_KEY));
        trailer.setType(result.getString(JSON_TYPE_KEY));

        return trailer;
    }

    @NonNull
    private static Review getReview(JSONObject result) {
        Review review = new Review();

        review.setmAuthor(result.optString(JSON_AUTHOR));
        review.setmContent(result.optString(JSON_CONTENT));

        return review;
    }

    private static List<String> jsonArrayToList(JSONArray array) throws JSONException {
        ArrayList<String> list = new ArrayList<>();

        for( int i = 0; i < array.length(); i++){
            list.add(array.getString(i));
        }

        return list;
    }
}
