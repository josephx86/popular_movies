package io.github.josephx86.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Movie implements Parcelable {
    private int voteCount, id;
    private boolean video, adult;
    private double voteAverage, popularity;
    private String title, posterPath, originalLanguage, originalTitle, backdropPath, overview;
    private Date releaseDate;

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public double getPopularity() {
        return popularity;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getOverview() {
        return overview;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    Movie() {
    }

    private Movie(Parcel parcel) {
        int[] integers = new int[2];
        parcel.readIntArray(integers);
        id = integers[0];
        voteCount = integers[1];

        boolean[] booleans = new boolean[2];
        parcel.readBooleanArray(booleans);
        video = booleans[0];
        adult = booleans[1];

        double[] doubles = new double[2];
        parcel.readDoubleArray(doubles);
        voteAverage = doubles[0];
        popularity = doubles[1];

        String[] strings = new String[6];
        parcel.readStringArray(strings);
        title = strings[0];
        posterPath = strings[1];
        originalLanguage = strings[2];
        originalTitle = strings[3];
        backdropPath = strings[4];
        overview = strings[5];

        long dateLong = parcel.readLong();
        releaseDate = new Date(dateLong);

    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeIntArray(new int[]{id, voteCount});
        dest.writeBooleanArray(new boolean[]{video, adult});
        dest.writeDoubleArray(new double[]{voteAverage, popularity});
        dest.writeStringArray(new String[]{title, posterPath, originalLanguage, originalTitle, backdropPath, overview});
        dest.writeLong(releaseDate.getTime());
    }
}
