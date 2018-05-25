package io.github.josephx86.popularmovies.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import io.github.josephx86.popularmovies.R;

public class TMDBHelper {
    private static final String API_BASE_URL = "https://api.themoviedb.org/3/";
    private static final String CONFIG_URL = API_BASE_URL + "configuration";
    private static String baseImageUrl = null;
    private static String posterSize = null, backdropSize = null;
    private static int currentPage = 0;
    private static int totalPages = 1;

    private static void setConfiguration(Context context) {
        // https://developers.themoviedb.org/3/configuration/get-api-configuration

        String json = "";
        String key = context.getString(R.string.tmdb_api_key);
        String address = String.format(Locale.US, "%s?api_key=%s", CONFIG_URL, key);
        URL url;
        try {
            url = new URL(address);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\a");
            if (scanner.hasNext()) {
                json = scanner.next();
            }

            // Set the image base url
            JSONObject jsonObject = new JSONObject(json);
            JSONObject imagesObject = jsonObject.getJSONObject("images");
            baseImageUrl = imagesObject.getString("secure_base_url");

            // Select the poster size that is nearest to the width of the poster ImageView width.
            int imageViewWidth = (int) Utils.getPosterImageViewWidth(context);
            int nearestWidth = Integer.MAX_VALUE;
            JSONArray posterSizeArray = imagesObject.getJSONArray("poster_sizes");
            for (int i = 0; i < posterSizeArray.length(); i++) {
                String sizeString = posterSizeArray.getString(i);
                int size = Utils.parseFirstPositiveIntegerFromString(sizeString);
                int difference = Math.abs(imageViewWidth - size);
                if (difference < nearestWidth) {
                    nearestWidth = difference;
                    posterSize = sizeString;
                }
            }

            // Select backdrop image width that is nearest to device width
            int deviceWidth = context.getResources().getDisplayMetrics().widthPixels;
            nearestWidth = Integer.MAX_VALUE;
            for (int i = 0; i < posterSizeArray.length(); i++) {
                String sizeString = posterSizeArray.getString(i);
                int size = Utils.parseFirstPositiveIntegerFromString(sizeString);
                int difference = Math.abs(deviceWidth - size);
                if (difference < nearestWidth) {
                    nearestWidth = difference;
                    backdropSize = sizeString;
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String getBaseImageUrl() {
        return baseImageUrl;
    }

    public static String getPosterSize() {
        return posterSize;
    }

    public static String getBackdropSize() {
        return backdropSize;
    }

    public static void resetCurrentPage() {
        currentPage = 0;
        totalPages = 1;
    }

    public static List<Movie> discoverMovies(final IWaitForMovies caller, final Context context) {

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, List<Movie>> task = new AsyncTask<Void, Void, List<Movie>>() {

            @Override
            protected List<Movie> doInBackground(Void... voids) {
                List<Movie> results = new ArrayList<>();
                try {
                    // Fetching next page;
                    currentPage++;
                    if (currentPage > totalPages) {
                        // No need to make unnecessary HTTP call when all pages have already been fetched.
                        return results;
                    }

                    // First get the api configuration
                    setConfiguration(context);

                    String json = "";
                    String key = context.getString(R.string.tmdb_api_key);
                    String path = "";
                    MoviesAdapter.SortType sortBy = Utils.getSavedSortOrder(context);
                    if (sortBy == MoviesAdapter.SortType.Rating) {
                        path = "movie/top_rated";
                    } else if (sortBy == MoviesAdapter.SortType.Popularity) {
                        path = "movie/popular";
                    }
                    String apiEndpoint = String.format(Locale.US, "%s%s?api_key=%s&language=en-US&page=%d",
                            API_BASE_URL, path, key, currentPage);
                    URL url = new URL(apiEndpoint);
                    URLConnection connection = url.openConnection();
                    InputStream inputStream = connection.getInputStream();
                    Scanner scanner = new Scanner(inputStream);
                    scanner.useDelimiter("\\a");
                    if (scanner.hasNext()) {
                        json = scanner.next();
                    }

                    // Parse JSON data
                    if (!json.isEmpty()) {
                        JSONObject jsonObject = new JSONObject(json);
                        currentPage = jsonObject.getInt("page");
                        totalPages = jsonObject.getInt("total_pages");

                        // Get all movies in JSON
                        JSONArray movieArray = jsonObject.getJSONArray("results");
                        for (int i = 0; i < movieArray.length(); i++) {
                            Movie movie = new Movie();
                            JSONObject movieObject = movieArray.getJSONObject(i);

                            int id = movieObject.getInt("id");
                            movie.setId(id);

                            int votes = movieObject.getInt("vote_count");
                            movie.setVoteCount(votes);

                            boolean video = movieObject.getBoolean("video");
                            movie.setVideo(video);

                            double voteAverage = movieObject.getDouble("vote_average");
                            movie.setVoteAverage(voteAverage);

                            String title = movieObject.getString("title");
                            movie.setTitle(title);

                            double popularity = movieObject.getDouble("popularity");
                            movie.setPopularity(popularity);

                            String posterPath = movieObject.getString("poster_path");
                            movie.setPosterPath(posterPath);

                            String originalTitle = movieObject.getString("original_title");
                            movie.setOriginalTitle(originalTitle);

                            String originalLanguage = movieObject.getString("original_language");
                            movie.setOriginalLanguage(originalLanguage);

                            String backdropPath = movieObject.getString("backdrop_path");
                            movie.setBackdropPath(backdropPath);

                            boolean adult = movieObject.getBoolean("adult");
                            movie.setAdult(adult);

                            String overview = movieObject.getString("overview");
                            movie.setOverview(overview);

                            String releaseDate = movieObject.getString("release_date");
                            movie.setReleaseDate(Utils.parseDate(releaseDate));

                            results.add(movie);
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return results;
            }

            @Override
            protected void onPostExecute(List<Movie> results) {
                super.onPostExecute(results);
                caller.processReceivedMovies(results);
            }
        };

        List<Movie> movies = null;
        try {
            movies = task.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return movies;
    }

}
