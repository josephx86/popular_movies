package io.github.josephx86.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.josephx86.popularmovies.data.IWaitForMovies;
import io.github.josephx86.popularmovies.data.Movie;
import io.github.josephx86.popularmovies.data.MoviesAdapter;
import io.github.josephx86.popularmovies.data.TMDBHelper;
import io.github.josephx86.popularmovies.data.Utils;

public class MainActivity extends AppCompatActivity implements IWaitForMovies {

    @BindView(R.id.movies_rv)
    RecyclerView moviesRecyclerView;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    MoviesAdapter adapter;

    Menu appMenu = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initializeRecyclerView();
    }

    private void initializeRecyclerView() {
        int columnCount = Utils.calculateGridLayoutColumns(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, columnCount);
        moviesRecyclerView.setLayoutManager(layoutManager);
        adapter = new MoviesAdapter(this);
        moviesRecyclerView.setAdapter(adapter);
        getMovies();
    }

    @Override
    public void getMovies() {
        if (adapter.getItemCount() == 0) {
            progressBar.setVisibility(View.VISIBLE);
            moviesRecyclerView.setVisibility(View.GONE);
        }
        TMDBHelper.discoverMovies(this, this);
    }

    @Override
    public void processReceivedMovies(List<Movie> movies) {
        adapter.addMovies(movies);
        if (adapter.getItemCount() > 0) {
            moviesRecyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        appMenu = menu;
        setSortOrderMenuItem();
        return true;
    }

    private void setSortOrderMenuItem() {
        // Set the saved sort order.
        if (appMenu != null) {
            MoviesAdapter.SortType sortType = Utils.getSavedSortOrder(this);
            MenuItem popularSortMenuItem = appMenu.findItem(R.id.action_sort_popularity);
            MenuItem ratingSortMenuItem = appMenu.findItem(R.id.action_sort_rating);
            if ((popularSortMenuItem != null) && (ratingSortMenuItem != null)) {
                if (sortType == MoviesAdapter.SortType.Popularity) {
                    popularSortMenuItem.setCheckable(true);
                    popularSortMenuItem.setChecked(true);
                    ratingSortMenuItem.setCheckable(false);
                } else if (sortType == MoviesAdapter.SortType.Rating) {
                    ratingSortMenuItem.setCheckable(true);
                    ratingSortMenuItem.setChecked(true);
                    popularSortMenuItem.setCheckable(false);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (adapter == null) {
            return super.onOptionsItemSelected(item);
        }

        // When the different sort order is selected, there is a possibility that a movie with a
        // higher sort order index (using new order) might not be in the list.
        // To fix that, clear movies and fetch with new order.
        switch (item.getItemId()) {
            case R.id.action_sort_popularity:
                Utils.saveSortOrder(this, MoviesAdapter.SortType.Popularity);
                setSortOrderMenuItem();
                adapter.clearMovies();
                getMovies();
                return true;
            case R.id.action_sort_rating:
                Utils.saveSortOrder(this, MoviesAdapter.SortType.Rating);
                setSortOrderMenuItem();
                adapter.clearMovies();
                getMovies();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


