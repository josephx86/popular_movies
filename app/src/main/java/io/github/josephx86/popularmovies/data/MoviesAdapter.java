package io.github.josephx86.popularmovies.data;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.github.josephx86.popularmovies.R;

public class MoviesAdapter extends RecyclerView.Adapter<MovieViewHolder> {
    private List<Movie> movies = new ArrayList<>();
    private List<Integer> moviesLoaded = new ArrayList<>();
    private IWaitForMovies waiter;

    public MoviesAdapter(IWaitForMovies waiter) {
        this.waiter = waiter;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_movie_item, parent, false);
       if (waiter != null) {
           waiter.setMovieItemSelectedListener(view);
       }

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if (movies == null) {
            movies = new ArrayList<>();
        }
        int lastItemPosition = getItemCount() - 1;
        if (position <= lastItemPosition) {
            Movie m = movies.get(position);
            holder.setPoster(m.getPosterPath());
            holder.setTitle(m.getTitle());
            holder.setPopularity(m.getPopularity());
            holder.setRating(m.getVoteAverage());
            holder.setParentViewTag(m.getId());

            if (position == lastItemPosition) {
                // If last item is displayed, we are at (or close to the bottom).
                // This is a perfect time to fetch more movies to display.
                waiter.getMovies();
            }
        }
    }

    @Override
    public int getItemCount() {
        if (movies == null) {
            movies = new ArrayList<>();
        }
        return movies.size();
    }

    public void addMovies(List<Movie> newMovies) {
        if (movies == null) {
            movies = new ArrayList<>();
        }
        if (newMovies == null) {
            return;
        }

        // Don't add existing movies
        if (moviesLoaded == null) {
            moviesLoaded = new ArrayList<>();
        }
        int moviesAdded = 0;
        for (Movie m : newMovies) {
            int movieId = m.getId();
            if (!moviesLoaded.contains(movieId)) {
                movies.add(m);
                moviesLoaded.add(movieId);
                moviesAdded++;
            }
        }
        if (moviesAdded > 0) {
            notifyDataSetChanged();
        }
    }

    public void clearMovies() {
        if (movies == null) {
            movies = new ArrayList<>();
        } else {
            movies.clear();
            moviesLoaded.clear();
        }
        TMDBHelper.resetCurrentMovieListPage();
        notifyDataSetChanged();
    }

    public Movie findMovieById(int id) {
        Movie movie = null;

        if (movies != null) {
            for (Movie m : movies) {
                if (m.getId() == id) {
                    movie = m;
                    break;
                }
            }
        }
        return movie;
    }

    public enum SortType {
        Rating,
        Popularity
    }
}
