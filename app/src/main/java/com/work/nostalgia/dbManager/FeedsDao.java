package com.work.nostalgia.dbManager;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.work.nostalgia.model.FeedModel;

import java.util.List;

@Dao
public interface FeedsDao {

    @Query("SELECT * FROM feedsTable")
    List<FeedModel> getAll();

    @Query("SELECT COUNT(*) from feedsTable")
    int countFeeds();

    @Insert
    void insertAll(FeedModel... feeds);

    @Insert
    void insert(FeedModel feed);

    @Delete
    void delete(FeedModel feed);
}
