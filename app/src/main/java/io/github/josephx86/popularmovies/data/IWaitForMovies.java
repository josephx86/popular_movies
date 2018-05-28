package io.github.josephx86.popularmovies.data;

import android.view.View;

import java.util.List;

public interface IWaitForMovies {
    void getMovies();

    void processReceivedMovies(List<Movie> nowPlayingResults);

    void setMovieItemSelectedListener(View view);
}
