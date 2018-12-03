package com.work.nostalgia.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "feedsTable")
public class FeedModel {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "author_name")
    public String Author;

    public FeedModel() {
    }

    public FeedModel(String Author, String PostedBy, String Description, String time, byte[] feedimage) {
        this.Author = Author;
        this.PostedBy = PostedBy;
        this.Description = Description;
        this.time = time;
        this.feedimage = feedimage;

    }

    @ColumnInfo(name = "posted_by")
    public String PostedBy;

    @ColumnInfo(name = "description")
    public String Description;

    @ColumnInfo(name = "time_post")
    public String time;

    @ColumnInfo(name = "feed_image")
    public byte[] feedimage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getFeedimage() {
        return feedimage;
    }

    public void setFeedimage(byte[] feedimage) {
        this.feedimage = feedimage;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getPostedBy() {
        return PostedBy;
    }

    public void setPostedBy(String postedBy) {
        PostedBy = postedBy;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
