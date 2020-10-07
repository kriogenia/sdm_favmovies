package es.uniovi.eii.sdm.favmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private String title;
    private String argument;
    private Category category;
    private String duration;
    private String date;

    public Movie(String title, String argument, Category category, String duration, String date) {
        this.title = title;
        this.argument = argument;
        this.category = category;
        this.duration = duration;
        this.date = date;
    }

    protected Movie(Parcel in) {
        title = in.readString();
        argument = in.readString();
        category = in.readParcelable(Category.class.getClassLoader());
        duration = in.readString();
        date = in.readString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(argument);
        dest.writeParcelable(category, flags);
        dest.writeString(duration);
        dest.writeString(date);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
