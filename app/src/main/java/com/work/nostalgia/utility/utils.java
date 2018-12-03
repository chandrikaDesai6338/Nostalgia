package com.work.nostalgia.utility;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static android.content.ContentValues.TAG;

public class utils {

    private static utils sInstance = null;

    public static utils getInstance() {
        if (sInstance == null)
            sInstance = new utils();
        return sInstance;
    }

    // convert from bitmap to byte array
    public byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    //Compress Image
    public Bitmap CompressImage(Context context, Uri contentUri) {

        String path = getRealPathFromURI(context, contentUri);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = calculateInSampleSize(options, 500, 500);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        //Bitmap.createScaledBitmap(bitmap, 200, 200, false);
        return bitmap;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {

        String ret = "";

        // Get uri related document id.
        String documentId = DocumentsContract.getDocumentId(contentUri);
        // Get uri authority.
        String uriAuthority = contentUri.getAuthority();

        if (isMediaDoc(uriAuthority)) {
            String idArr[] = documentId.split(":");
            if (idArr.length == 2) {
                // First item is document type.
                String docType = idArr[0];

                // Second item is document real id.
                String realDocId = idArr[1];

                // Get content uri by document type.
                Uri mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                if ("image".equals(docType)) {
                    mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(docType)) {
                    mediaContentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(docType)) {
                    mediaContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                // Get where clause with real document id.
                String whereClause = MediaStore.Images.Media._ID + " = " + realDocId;

                ret = getImageRealPath(context.getContentResolver(), mediaContentUri, whereClause);
            }
        } else if (isDownloadDoc(uriAuthority)) {
            // Build download uri.
            Uri downloadUri = Uri.parse("content://downloads/public_downloads");

            // Append download document id at uri end.
            Uri downloadUriAppendId = ContentUris.withAppendedId(downloadUri, Long.valueOf(documentId));

            ret = getImageRealPath(context.getContentResolver(), downloadUriAppendId, null);

        } else if (isExternalStoreDoc(uriAuthority)) {
            String idArr[] = documentId.split(":");
            if (idArr.length == 2) {
                String type = idArr[0];
                String realDocId = idArr[1];

                if ("primary".equalsIgnoreCase(type)) {
                    ret = Environment.getExternalStorageDirectory() + "/" + realDocId;
                }
            }
        }
        return ret;
    }

    /* Check whether this document is provided by ExternalStorageProvider. */
    private boolean isExternalStoreDoc(String uriAuthority) {
        boolean ret = false;

        if ("com.android.externalstorage.documents".equals(uriAuthority)) {
            ret = true;
        }

        return ret;
    }

    /* Check whether this document is provided by DownloadsProvider. */
    private boolean isDownloadDoc(String uriAuthority) {
        boolean ret = false;

        if ("com.android.providers.downloads.documents".equals(uriAuthority)) {
            ret = true;
        }

        return ret;
    }

    /* Check whether this document is provided by MediaProvider. */
    private boolean isMediaDoc(String uriAuthority) {
        boolean ret = false;

        if ("com.android.providers.media.documents".equals(uriAuthority)) {
            ret = true;
        }

        return ret;
    }

    /* Return uri represented document file real local path.*/
    private String getImageRealPath(ContentResolver contentResolver, Uri uri, String whereClause) {
        String ret = "";

        // Query the uri with condition.
        Cursor cursor = contentResolver.query(uri, null, whereClause, null, null);

        if (cursor != null) {
            boolean moveToFirst = cursor.moveToFirst();
            if (moveToFirst) {

                // Get columns name by uri type.
                String columnName = MediaStore.Images.Media.DATA;

                if (uri == MediaStore.Images.Media.EXTERNAL_CONTENT_URI) {
                    columnName = MediaStore.Images.Media.DATA;
                } else if (uri == MediaStore.Audio.Media.EXTERNAL_CONTENT_URI) {
                    columnName = MediaStore.Audio.Media.DATA;
                } else if (uri == MediaStore.Video.Media.EXTERNAL_CONTENT_URI) {
                    columnName = MediaStore.Video.Media.DATA;
                }

                // Get column index.
                int imageColumnIndex = cursor.getColumnIndex(columnName);

                // Get column value which is the uri related file local path.
                ret = cursor.getString(imageColumnIndex);
            }
        }

        return ret;
    }
}
