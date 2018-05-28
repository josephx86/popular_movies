package io.github.josephx86.popularmovies.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.josephx86.popularmovies.R;
import io.github.josephx86.popularmovies.data.DetailsPagerFragment;
import io.github.josephx86.popularmovies.data.Movie;
import io.github.josephx86.popularmovies.data.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class OverviewFragment extends DetailsPagerFragment {

    @BindView(R.id.poster_iv)
    ImageView posterImageView;

    @BindView(R.id.title_tv)
    TextView titleTextView;

    @BindView(R.id.release_date_tv)
    TextView releaseDateTextView;

    @BindView(R.id.rating_ll)
    LinearLayout ratingLayout;

    @BindView(R.id.overview_tv)
    TextView overviewTextView;

    private Movie movie;

    public OverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        ButterKnife.bind(this, view);

        setMovieDetails();

        return view;
    }

    private void setMovieDetails() {
        if (movie != null) {
            // The poster.
            String imageUrl = Utils.getPosterUrl(movie.getPosterPath());
            Picasso
                    .get()
                    .load(imageUrl)
                    .placeholder(R.drawable.movie_poster_placeholder)
                    .into(posterImageView);

            String originalTitle = movie.getOriginalTitle();
            String title = movie.getTitle();
            titleTextView.setText(movie.getOriginalTitle());
            if (!originalTitle.equals(title)) {
                titleTextView.append("\n(" + title + ")");
            }
            String releaseDate = DateFormat.format("MMMM dd, yyyy", movie.getReleaseDate()).toString();
            releaseDateTextView.setText(releaseDate);
            Utils.setRating(ratingLayout, movie.getVoteAverage());

            overviewTextView.setText(movie.getOverview());
        }
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @Override
    protected String getTitle(Context context) {
        return context.getString(R.string.overview);
    }
}
