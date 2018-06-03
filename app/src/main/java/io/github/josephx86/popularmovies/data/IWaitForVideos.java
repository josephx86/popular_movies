package io.github.josephx86.popularmovies.data;

import java.util.List;

public interface IWaitForVideos {
    void processReceivedVideos(List<Video> videos);
}
