package io.github.josephx86.popularmovies.data;

import java.util.List;

public interface IWaitForReviews {

    void processReceivedReviews(List<Review> reviews);
}
