package io.github.josephx86.popularmovies.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.github.josephx86.popularmovies.R;

public class Utils {
    public static Date parseDate(String dateString) {
        // Parses date in yyyy-mm-dd format to Date object.
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static float getPosterImageViewWidth(Context context) {
        return context.getResources().getDimension(R.dimen.movie_poster_width);
    }

    public static int parseFirstPositiveIntegerFromString(String stringWithLeadingDigits) {
        int result = -1;
        // Gets the integer value of first digits found in a string
        if (stringWithLeadingDigits == null) {
            return result; // -1
        }
        StringBuilder buffer = new StringBuilder();
        boolean digitsFound = false;
        for (int i = 0; i < stringWithLeadingDigits.length(); i++) {
            char c = stringWithLeadingDigits.charAt(i);
            if (Character.isDigit(c)) {
                buffer.append(c);
                digitsFound = true;
            } else if (digitsFound) {
                // Some digits have already been found before this non-digit char.
                break;// Exit loop
            }
        }
        try {
            result = Integer.parseInt(buffer.toString());
        } catch (NumberFormatException nex) {
            // NumberFormatException will be thrown of string has 'int' that would be overflow 32 bits.
        }
        return result;
    }

    public static int calculateGridLayoutColumns(Context context) {
        float posterWidth = getPosterImageViewWidth(context);
        int deviceWidth = context.getResources().getDisplayMetrics().widthPixels;
        return (int) (deviceWidth / posterWidth);
    }

    public static String getPosterUrl(String imagePath) {
        if (imagePath == null) {
            imagePath = "";
        }
        return String.format(Locale.US, "%s%s%s",
                TMDBHelper.getBaseImageUrl(),
                TMDBHelper.getPosterSize(),
                imagePath);
    }

    public static String getBackdropUrl(String imagePath) {
        if (imagePath == null) {
            imagePath = "";
        }
        return String.format(Locale.US, "%s%s%s",
                TMDBHelper.getBaseImageUrl(),
                TMDBHelper.getBackdropSize(),
                imagePath);
    }

    public static void setRating(LinearLayout ratingLayout, double rating) {
        boolean hasHalfStar = false;
        boolean hasEmptyStar = false;
        int wholeStarCount = (int) rating;
        if (wholeStarCount != 10) {
            int fractionStarValue = (int) ((rating - wholeStarCount) * 10);
            hasHalfStar = fractionStarValue >= 5;
            hasEmptyStar = fractionStarValue < 5;
        }
        Context context = ratingLayout.getContext();
        if (ratingLayout.getChildCount() > 0) {
            ratingLayout.removeAllViews();
        }
        for (int i = 0; i < wholeStarCount; i++) {
            ImageView star = new ImageView(context);
            star.setImageResource(R.drawable.ic_star_12dp);
            ratingLayout.addView(star);
        }
        if (hasHalfStar) {
            ImageView halfStar = new ImageView(context);
            halfStar.setImageResource(R.drawable.ic_star_half_12dp);
            ratingLayout.addView(halfStar);
        }

        if (hasEmptyStar) {
            ImageView emptyStar = new ImageView(context);
            emptyStar.setImageResource(R.drawable.ic_star_border_12dp);
            ratingLayout.addView(emptyStar);
        }

    }

    public static MoviesAdapter.SortType getSavedSortOrder(Context context) {
        String key = context.getString(R.string.pref_sort);
        String orderByPopularity = context.getString(R.string.pref_sort_by_popularity);
        String orderByRating = context.getString(R.string.pref_sort_by_rating);
        String order = PreferenceManager.getDefaultSharedPreferences(context).getString(key, orderByPopularity);

        if (order.equals(orderByPopularity)) {
            return MoviesAdapter.SortType.Popularity;
        } else if (order.equals(orderByRating)) {
            return MoviesAdapter.SortType.Rating;
        } else {
            return MoviesAdapter.SortType.Popularity;
        }
    }

    public static void saveSortOrder(Context context, MoviesAdapter.SortType sortType) {
        String key = context.getString(R.string.pref_sort);
        String orderByPopularity = context.getString(R.string.pref_sort_by_popularity);
        String orderByRating = context.getString(R.string.pref_sort_by_rating);
        String order = orderByPopularity; // Default.
        if (sortType == MoviesAdapter.SortType.Rating) {
            order = orderByRating;
        }

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(key, order);
        editor.apply();
    }
}
