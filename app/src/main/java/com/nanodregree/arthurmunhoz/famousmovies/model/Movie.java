package com.nanodregree.arthurmunhoz.famousmovies.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by Arthur on 01/03/2018.
 */

@Entity(tableName = "movie_table")
public class Movie implements Parcelable
{
    /* Definindo atributos */

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String  id;

    private String  runtime;
    private Boolean  videos;
    private String voteAverage;
    private String  title;
    private String  popularity;
    private String posterPath;
    private String originalTitle;
    private String genreIds;
    private String backdropPath;
    private String  adult;
    private String  overview;
    private String releaseDate;
    private String voteCount;
    private Boolean isFavorite;


    protected Movie(Parcel in)
    {
        id = in.readString();
        runtime = in.readString();
        byte tmpVideos = in.readByte();
        videos = tmpVideos == 0 ? null : tmpVideos == 1;
        voteAverage = in.readString();
        title = in.readString();
        popularity = in.readString();
        posterPath = in.readString();
        originalTitle = in.readString();
        genreIds = in.readString();
        backdropPath = in.readString();
        adult = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        voteCount = in.readString();
        byte tmpIsFavorite = in.readByte();
        isFavorite = tmpIsFavorite == 0 ? null : tmpIsFavorite == 1;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>()
    {
        @Override
        public Movie createFromParcel(Parcel in)
        {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size)
        {
            return new Movie[size];
        }
    };

    public Boolean isFavorite()
    {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite)
    {
        isFavorite = favorite;
    }

    public Movie()
    {
        this.isFavorite = false;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(String voteCount) {
        this.voteCount = voteCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getVideos() {
        return videos;
    }

    public void setVideos(boolean videos) {
        this.videos = videos;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(String genreIds) {
        this.genreIds = genreIds;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getAdult() {
        return adult;
    }

    public void setAdult(String adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(id);
        dest.writeString(runtime);
        dest.writeByte((byte) (videos == null ? 0 : videos ? 1 : 2));
        dest.writeString(voteAverage);
        dest.writeString(title);
        dest.writeString(popularity);
        dest.writeString(posterPath);
        dest.writeString(originalTitle);
        dest.writeString(genreIds);
        dest.writeString(backdropPath);
        dest.writeString(adult);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(voteCount);
        dest.writeByte((byte) (isFavorite == null ? 0 : isFavorite ? 1 : 2));
    }
}
