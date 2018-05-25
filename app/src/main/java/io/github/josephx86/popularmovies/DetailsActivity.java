package io.github.josephx86.popularmovies;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.josephx86.popularmovies.data.Movie;
import io.github.josephx86.popularmovies.data.Utils;

public class DetailsActivity extends AppCompatActivity {
    @BindView(R.id.backdrop_iv)
    ImageView backdropImageView;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        // Set tool bar title
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.details_activity_title));
        }

        // Get the movie id from intent extra.
        Intent intent = getIntent();
        if (intent != null) {
            String key = getString(R.string.movie_object_key);
            Movie movie = intent.getParcelableExtra(key);
            showMovieDetails(movie);
        }
    }

    private void showMovieDetails(Movie movie) {
        // Show the backdrop in the background
        String imageUrl = Utils.getBackdropUrl(movie.getBackdropPath());
        Picasso
                .get()
                .load(imageUrl)
                .into(backdropImageView);

        // The poster.
        imageUrl = Utils.getPosterUrl(movie.getPosterPath());
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
