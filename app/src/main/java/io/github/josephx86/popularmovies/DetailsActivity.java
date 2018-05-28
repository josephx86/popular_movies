package io.github.josephx86.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.josephx86.popularmovies.data.DetailsPagerAdapter;
import io.github.josephx86.popularmovies.data.IWaitForReviews;
import io.github.josephx86.popularmovies.data.Movie;
import io.github.josephx86.popularmovies.data.Review;
import io.github.josephx86.popularmovies.data.TMDBHelper;
import io.github.josephx86.popularmovies.data.Utils;

public class DetailsActivity extends AppCompatActivity implements IWaitForReviews {


    @BindView(R.id.movie_pager)
    ViewPager pager;

    @BindView(R.id.titles_tl)
    TabLayout titlesTabLayout;

    @BindView(R.id.backdrop_iv)
    ImageView backdropImageView;

    DetailsPagerAdapter pagerAdapter;

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

        pagerAdapter = new DetailsPagerAdapter(this);
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(0, true);
        titlesTabLayout.setupWithViewPager(pager, true);


        // Get the movie data from intent extra.
        Intent intent = getIntent();
        if (intent != null) {
            String key = getString(R.string.movie_object_key);
            Movie movie = intent.getParcelableExtra(key);
            setBackdrop(movie.getBackdropPath());
            if (pagerAdapter != null) {
                pagerAdapter.setMovie(movie);
            }

            // Get reviews
            Review.setMovieId(movie.getId());
            TMDBHelper.getReviews(this, this, movie.getId(), true);
        }
    }

    private void setBackdrop(String backdropPath) {
        // Show the backdrop in the background
        String imageUrl = Utils.getBackdropUrl(backdropPath);
        Picasso
                .get()
                .load(imageUrl)
                .into(backdropImageView);
    }

    @Override
    public void processReceivedReviews(List<Review> reviews) {
        if (pagerAdapter != null) {
            pagerAdapter.addReviews(reviews, this);
        }
    }
}
