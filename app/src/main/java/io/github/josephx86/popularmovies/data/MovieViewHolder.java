package io.github.josephx86.popularmovies.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.josephx86.popularmovies.R;


public class MovieViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.poster_iv)
    ImageView posterImageView;

    @BindView(R.id.popularity_tv)
    TextView popularityTextView;

    @BindView(R.id.movie_name_tv)
    TextView movieNameTextView;

    @BindView(R.id.rating_ll)
    LinearLayout ratingLayout;

    private View parentView;

    MovieViewHolder(View itemView) {
        super(itemView);
        parentView = itemView;
        ButterKnife.bind(this, itemView);
    }

    public void setParentViewTag(int movieId) {
        if (parentView != null) {
            parentView.setTag(movieId);
        }
    }

    void setPoster(String posterPath) {
        String imageUrl = Utils.getPosterUrl(posterPath);
        Picasso
                .get()
                .load(imageUrl)
                .placeholder(R.drawable.movie_poster_placeholder)
                .into(posterImageView);
    }

    void setTitle(String title) {
        if (title == null) {
            title = "";
        }
        title = title.trim();
        movieNameTextView.setText(title);
    }

    void setPopularity(double popularity) {
        Context context = popularityTextView.getContext();
        String text = context.getString(R.string.popularity_score_label) + Integer.toString((int) popularity);
        popularityTextView.setText(text);
    }

    void setRating(double rating) {
        Utils.setRating(ratingLayout, rating);
    }

}
