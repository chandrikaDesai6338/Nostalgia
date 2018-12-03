package com.work.nostalgia.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.work.nostalgia.R;
import com.work.nostalgia.dbManager.AppDatabase;
import com.work.nostalgia.dbManager.FeedsDao;
import com.work.nostalgia.model.FeedModel;
import com.work.nostalgia.utility.utils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.work.nostalgia.dbManager.AppDatabase.destroyInstance;

public class AddPostActivity extends AppCompatActivity {


    // imports
    private ImageButton imageBtn;
    private static final int GALLERY_REQUEST_CODE = 2;
    private Uri uri = null;
    private EditText textAuthor;
    private EditText textPostedBy;
    private EditText textDesc;
    private Button postBtn;
    FeedsDao feedsDao;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        // initializing objects
        postBtn = (Button) findViewById(R.id.postBtn);
        textDesc = (EditText) findViewById(R.id.textDesc);
        textAuthor = (EditText) findViewById(R.id.textAuthor);
        textPostedBy = findViewById(R.id.textPostedBy);
        imageBtn = (ImageButton) findViewById(R.id.imageBtn);


        //picking image from gallery
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveTask();


            }
        });
    }

    private void saveTask() {
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (isValid()) {
            class SaveTask extends AsyncTask<Void, Void, Void> {

                @Override
                protected Void doInBackground(Void... voids) {

                    //creating a feed
                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                    FeedModel feed = new FeedModel();
                    feed.setAuthor(textAuthor.getText().toString());
                    feed.setDescription(textDesc.getText().toString());
                    feed.setPostedBy(textPostedBy.getText().toString());
                    feed.setTime(currentDateTimeString);

                    feed.setFeedimage(utils.getInstance().getBytes(bitmap));

                    //Get Database Instance
                    AppDatabase.getAppDatabase(getApplicationContext())
                            .feedsDao()
                            .insert(feed);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    finish();
                    Intent intent = new Intent(AddPostActivity.this, ViewPost.class);
                    Toast.makeText(getApplicationContext(), "Posted", Toast.LENGTH_LONG).show();
                    destroyInstance();
                    startActivity(intent);
                }
            }

            SaveTask st = new SaveTask();
            st.execute();
        }
    }

    private boolean isValid() {
        if (textDesc.getText().toString().trim().isEmpty()) {
            textDesc.setError("Description required");
            textDesc.requestFocus();
            return false;
        }
        if (textAuthor.getText().toString().trim().isEmpty()) {
            textAuthor.setError("Author name required");
            textAuthor.requestFocus();
            return false;
        }
        if (textPostedBy.getText().toString().trim().isEmpty()) {
            textPostedBy.setError("Poster name required");
            textPostedBy.requestFocus();
            return false;
        }
        if (bitmap == null) {
            Toast.makeText(this, "Please add a image!!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    // image from gallery result
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            uri = data.getData();

            File file = new File(utils.getInstance().getRealPathFromURI(this, uri));
            long length = file.length() / 1024;
            if (length > 700) {
                Toast.makeText(this, "Please add a image less than 700kb!!", Toast.LENGTH_LONG).show();
            } else {
                 //bitmap = utils.getInstance().CompressImage(this, uri);
                Glide
                        .with(this)
                        .load(file)
                        .into(imageBtn);
            }
        }
    }

}
