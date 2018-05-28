package io.github.josephx86.popularmovies.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.josephx86.popularmovies.R;
import io.github.josephx86.popularmovies.data.DetailsPagerFragment;
import io.github.josephx86.popularmovies.data.IWaitForReviews;
import io.github.josephx86.popularmovies.data.Review;
import io.github.josephx86.popularmovies.data.ReviewsAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsFragment extends DetailsPagerFragment {

    @BindView(R.id.reviews_rv)
    RecyclerView reviewsRecyclerView;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @BindView(R.id.no_reviews_tv)
    TextView noReviewsTextView;

    private ReviewsAdapter adapter = new ReviewsAdapter();

    public ReviewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reviews, container, false);
        ButterKnife.bind(this, view);
        setupRecyclerView();
        updateUI();
        return view;
    }

    private void updateUI() {
        // Update the UI accordingly
        progressBar.setVisibility(View.GONE);
        int reviewCount = adapter.getItemCount();
        if (reviewCount == 0) {
            reviewsRecyclerView.setVisibility(View.GONE);
            noReviewsTextView.setVisibility(View.VISIBLE);
        } else {
            reviewsRecyclerView.setVisibility(View.VISIBLE);
            noReviewsTextView.setVisibility(View.GONE);
        }
    }

    private void setupRecyclerView() {
        if (reviewsRecyclerView != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(reviewsRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
            reviewsRecyclerView.setLayoutManager(layoutManager);
            reviewsRecyclerView.setAdapter(adapter);
        }
    }

    public void addReviews(List<Review> reviews, IWaitForReviews caller) {
        // NOTE: This method may be called before onCreateView()
        if (adapter != null) {
            adapter.addReviews(reviews, caller);
        }
    }

    @Override
    protected String getTitle(Context context) {
        return context.getString(R.string.reviews);
    }
}
