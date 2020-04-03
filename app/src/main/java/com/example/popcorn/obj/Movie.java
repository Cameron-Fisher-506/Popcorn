package com.example.popcorn.obj;

import android.util.Log;

import com.example.popcorn.utils.ConstantUtils;
import com.example.popcorn.utils.GeneralUtils;

import org.json.JSONObject;

public class Movie {

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
}
