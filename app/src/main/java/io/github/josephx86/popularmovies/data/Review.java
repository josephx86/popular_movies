package io.github.josephx86.popularmovies.data;

public class Review {
    private String author = "", content = "";

    // Reviews will only ever be shown in the details activity in reference to one movie.
    // So it is safe to use static variables for data that will be common to all reviews.
    private static int totalReviews = 0;
    private static int movieId = 0;

    public Review(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public static int getTotalReviews() {
        return totalReviews;
    }

    public static void setTotalReviews(int total) {
        totalReviews = total;
    }

    public static int getMovieId() {
        return movieId;
    }

    public static void setMovieId(int movieId) {
        Review.movieId = movieId;
    }
}
