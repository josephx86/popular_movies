package io.github.josephx86.popularmovies.data;

import java.util.List;

public interface IWaitForMovies {
    void getMovies();

    void processReceivedMovies(List<Movie> nowPlayingResults);
}
