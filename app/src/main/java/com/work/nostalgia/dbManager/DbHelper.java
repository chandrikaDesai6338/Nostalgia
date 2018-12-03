package com.work.nostalgia.dbManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.work.nostalgia.R;
import com.work.nostalgia.model.FeedModel;
import com.work.nostalgia.utility.utils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "FeedDB.db";
    public static final String FEEDS_TABLE_NAME = "feedsTable";

    public static final String ID = "id";
    public static final String AUTHOR_NAME = "author_name";
    public static final String POSTED_BY = "posted_by";
    public static final String DESCRIPTION = "description";
    public static final String TIME_POST = "time_post";
    public static final String FEED_IMAGE = "feed_image";
    public static final int DB_VERSION = 1;

    SQLiteDatabase sqLiteDatabase;
    Cursor ResultSet, ResultSet1;
    private boolean isDBOpen;
    private Context context;

    private static final String CREATE_TABLE = "CREATE TABLE " + FEEDS_TABLE_NAME + "( "+ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+AUTHOR_NAME+" VARCHAR, "+ POSTED_BY+" VARCHAR, "+DESCRIPTION+" VARCHAR, "+TIME_POST+" VARCHAR, "+FEED_IMAGE+" BLOB);";


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        this.context = context;
        if (sqLiteDatabase == null) {
            //_database = getDatabase();
            sqLiteDatabase = this.getWritableDatabase();
            isDBOpen = true;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
        PreInsertedFeed();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // on upgrade drop older tables
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FEEDS_TABLE_NAME);

        // create new table
        onCreate(sqLiteDatabase);
    }

    public void InsertItems(String author_name, String posted_by,String description, String time, byte[] feedimage){
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(AUTHOR_NAME, author_name);
        contentValues.put(POSTED_BY,posted_by );
        contentValues.put(DESCRIPTION, description);
        contentValues.put(TIME_POST, time);
        contentValues.put(FEED_IMAGE, feedimage);
        sqLiteDatabase.insert(FEEDS_TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();

    }

   /* public ArrayList<FeedModel> getAllItem(){
        sqLiteDatabase = this.getReadableDatabase();
        String selectQuery = " select * from "+FEEDS_TABLE_NAME;
        Cursor ResultSet = sqLiteDatabase.rawQuery(selectQuery, null);
        ArrayList<FeedModel> list = new ArrayList<FeedModel>();
        if (ResultSet != null && ResultSet.getCount() > 0) {
            ResultSet.moveToFirst();
            while (!ResultSet.isAfterLast()) {
                int id = ResultSet.getInt(ResultSet.getColumnIndex(ID));
                String author = ResultSet.getString(ResultSet.getColumnIndex(AUTHOR_NAME));
                String postedBy = ResultSet.getString(ResultSet.getColumnIndex(POSTED_BY));
                String description = ResultSet.getString(ResultSet.getColumnIndex(DESCRIPTION));
                String time = ResultSet.getString(ResultSet.getColumnIndex(TIME_POST));
                byte[] feedimage  = ResultSet.getBlob(ResultSet.getColumnIndex(FEED_IMAGE));


                FeedModel dto = new FeedModel();
                dto.setId(id);
                dto.setAuthor(author);
                dto.setPostedBy(postedBy);
                dto.setDescription(description);
                dto.setTime(time);
                dto.setFeedimage(feedimage);

                list.add(dto);
                ResultSet.moveToNext();
            }
        }
        return list;
    }*/
    private void PreInsertedFeed() {

        // get image from drawable
        Bitmap image1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.feed1);
        Bitmap image2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.feed1);
        Bitmap image3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.feed1);
        Bitmap image4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.feed1);
        Bitmap image5 = BitmapFactory.decodeResource(context.getResources(), R.drawable.feed1);
        Bitmap image6 = BitmapFactory.decodeResource(context.getResources(), R.drawable.feed1);
        Bitmap image7 = BitmapFactory.decodeResource(context.getResources(), R.drawable.feed1);


        InsertItems("John","William","","Mon Jan 2018 05:16", utils.getInstance().getBytes(image1) );
        InsertItems("Mary","Ron","","Sat Feb 2018 05:16", utils.getInstance().getBytes(image2) );
        InsertItems("Harry","John","","Thur Jan 2018 05:16", utils.getInstance().getBytes(image3) );
        InsertItems("Seamus","Chu Qiao","","Mon Jan 2018 05:16", utils.getInstance().getBytes(image4) );
        InsertItems("John","Sana","","Mon Jun 2018 05:16", utils.getInstance().getBytes(image5) );
        InsertItems("Sam","Tania","","Tue Nov 2018 05:16", utils.getInstance().getBytes(image6) );
        InsertItems("Arman","Lily","","Mon Jan 2018 05:16", utils.getInstance().getBytes(image7) );

    }
}
