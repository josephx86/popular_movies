package io.github.josephx86.popularmovies;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.josephx86.popularmovies.data.DetailsPagerFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class TrailersFragment extends DetailsPagerFragment {


    public TrailersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trailers, container, false);
    }

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.trailers);
    }
}
