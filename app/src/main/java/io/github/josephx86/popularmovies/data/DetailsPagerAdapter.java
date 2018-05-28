package io.github.josephx86.popularmovies.data;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import io.github.josephx86.popularmovies.fragments.ReviewsFragment;
import io.github.josephx86.popularmovies.TrailersFragment;
import io.github.josephx86.popularmovies.fragments.OverviewFragment;

public class DetailsPagerAdapter extends FragmentPagerAdapter {
    private DetailsPagerFragment[] fragments = new DetailsPagerFragment[]{
            new OverviewFragment(),
            new TrailersFragment(),
            new ReviewsFragment()
    };

    private Context context;

    public DetailsPagerAdapter(FragmentActivity activity) {
        super(activity.getSupportFragmentManager());
        context = activity;
    }

    public void setMovie(Movie movie) {
        OverviewFragment overviewFragment = (OverviewFragment) fragments[0];
        if (overviewFragment != null) {
            overviewFragment.setMovie(movie);
        }
    }

    public void addReviews(List<Review> reviews, IWaitForReviews caller) {
        ReviewsFragment reviewsFragment = (ReviewsFragment) fragments[2];
        if (reviewsFragment != null) {
            reviewsFragment.addReviews(reviews, caller);
        }
    }

    @Override
    public Fragment getItem(int position) {
        // Keep position in range of array.
        position = position % fragments.length;
        return fragments[position];
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        DetailsPagerFragment pagerFragment = (DetailsPagerFragment) getItem(position);
        String title = "";
        if (pagerFragment != null) {
            title = pagerFragment.getTitle(context);
        }
        return title;
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}