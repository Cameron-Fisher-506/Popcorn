package za.co.popcorn.obj;

import android.util.Log;
import za.co.popcorn.utils.ConstantUtils;
import za.co.popcorn.utils.GeneralUtils;
import org.json.JSONObject;

import java.util.Comparator;

public class Movie implements Comparable<Movie>
{

    private Long id;
    private String title;
    private String torrentURL;
    private Long year;
    private Double rating;
    private String coverImage;
    private String description;

    public Movie()
    {

    }

    public Movie(Long id, String title, String torrentURL, Long year, Double rating, String coverImage, String description)
    {
        this.id = id;
        this.title = title;
        this.torrentURL = torrentURL;
        this.year = year;
        this.rating = rating;
        this.coverImage = coverImage;
        this.description = description;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTorrentURL() {
        return torrentURL;
    }

    public void setTorrentURL(String torrentURL) {
        this.torrentURL = torrentURL;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JSONObject getJSONObject()
    {
        JSONObject toReturn = null;

        try
        {
            toReturn = new JSONObject();
            toReturn.put("id", this.id);
            toReturn.put("title", this.title);
            toReturn.put("torrentURL", this.torrentURL);
            toReturn.put("year", this.year);
            toReturn.put("rating", this.rating);
            toReturn.put("coverImage", this.coverImage);
            toReturn.put("description", this.description);

        }catch(Exception e)
        {
            String message = "\n\nError Message: " + e.getMessage() +
                    "\nMethod: getJSONObject" +
                    "\nClass: Movie" +
                    "\nCreatedTime: " + GeneralUtils.getCurrentDateTime();
            Log.d(ConstantUtils.TAG, message);
        }



        return toReturn;
    }

    @Override
    public int compareTo(Movie o) {
        return o.getYear().compareTo(this.getYear());
    }

    public static Comparator<Movie> sortByTitleAscending = new Comparator<Movie>() {
        @Override
        public int compare(Movie o1, Movie o2) {
            return o1.getTitle().compareTo(o2.getTitle());
        }
    };

    public static Comparator<Movie> sortByTitleDescending = new Comparator<Movie>() {
        @Override
        public int compare(Movie o1, Movie o2) {
            return o2.getTitle().compareTo(o1.getTitle());
        }
    };

    public static Comparator<Movie> sortByYearAscending = new Comparator<Movie>() {
        @Override
        public int compare(Movie o1, Movie o2) {
            return o1.getYear().compareTo(o2.getYear());
        }
    };

    public static Comparator<Movie> sortByYearDescending = new Comparator<Movie>() {
        @Override
        public int compare(Movie o1, Movie o2) {
            return o2.getYear().compareTo(o1.getYear());
        }
    };

    public static Comparator<Movie> sortByRatingAscending = new Comparator<Movie>() {
        @Override
        public int compare(Movie o1, Movie o2) {
            return o1.getRating().compareTo(o2.getRating());
        }
    };

    public static Comparator<Movie> sortByRatingDescending = new Comparator<Movie>() {
        @Override
        public int compare(Movie o1, Movie o2) {
            return o2.getRating().compareTo(o1.getRating());
        }
    };
}
