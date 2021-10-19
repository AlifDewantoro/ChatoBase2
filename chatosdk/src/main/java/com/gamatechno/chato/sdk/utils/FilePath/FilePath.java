package com.gamatechno.chato.sdk.utils.FilePath;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by root on 11/10/17.
 */

public class FilePath {

    public static boolean isFile(Uri uri){
        String isUri = uri.toString().split(":")[0];
        if(isUri.equals("file"))
            return true;
        return false;
    }

    public static String uriFileOrOther(Context context, Uri uri){
        String path = "";
        if (isFile(uri)) {
            return uri.getPath();
        } else {
            String[] projection = {MediaStore.Video.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA));
                    if (path == null)
                        path = getPath(context, uri);
                } else {
                    path = getPath(context, uri);
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
                return path;
            }
        }
    }

    public static String getPath(final Context context, final Uri uri) {

        // check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/"
                                + split[1];
                    }
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[] { split[1] };

                    return getDataColumn(context, contentUri, selection,
                            selectionArgs);
                }
                else if ("file".equalsIgnoreCase(uri.getScheme())) {
                    return uri.getPath();
                }
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri) || isGoogleContentProvider(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context
     *            The context.
     * @param uri
     *            The Uri to query.
     * @param selection
     *            (Optional) Filter used in the query.
     * @param selectionArgs
     *            (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

    public static boolean isGoogleContentProvider(Uri uri) {
        return "com.google.android.apps.photos.contentprovider".equals(uri
                .getAuthority());
    }

    public static String getImageRealPath(ContentResolver contentResolver, Uri uri, String whereClause) {
        String ret = "";


//        TODO : 2
//        Cursor cursor = contentResolver.query(uri, new String[]{"_display_name", "_size"}, null, null, null);
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                try {
//                    ret = cursor.getString(cursor.getColumnIndexOrThrow("_display_name"));

                    // Get columns name by uri type.
                    String columnName = MediaStore.Images.Media.DATA;

                    if( uri==MediaStore.Images.Media.EXTERNAL_CONTENT_URI )
                    {
                        columnName = MediaStore.Images.Media.DATA;
                    }else if( uri==MediaStore.Audio.Media.EXTERNAL_CONTENT_URI )
                    {
                        columnName = MediaStore.Audio.Media.DATA;
                    }else if( uri==MediaStore.Video.Media.EXTERNAL_CONTENT_URI )
                    {
                        columnName = MediaStore.Video.Media.DATA;
                    }

                    // Get column index.
                    int imageColumnIndex = cursor.getColumnIndex(columnName);

                    // Get column value which is the uri related file local path.
                    ret = cursor.getString(imageColumnIndex);
                } catch (IllegalArgumentException e3) {

                }
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        /*// Query the uri with condition.
        Cursor cursor = contentResolver.query(uri, null, whereClause, null, null);
        if(cursor!=null)
        {
            boolean moveToFirst = cursor.moveToFirst();
            if(moveToFirst)
            {

                // Get columns name by uri type.
                String columnName = MediaStore.Images.Media.DATA;

                if( uri==MediaStore.Images.Media.EXTERNAL_CONTENT_URI )
                {
                    columnName = MediaStore.Images.Media.DATA;
                }else if( uri==MediaStore.Audio.Media.EXTERNAL_CONTENT_URI )
                {
                    columnName = MediaStore.Audio.Media.DATA;
                }else if( uri==MediaStore.Video.Media.EXTERNAL_CONTENT_URI )
                {
                    columnName = MediaStore.Video.Media.DATA;
                }

                // Get column index.
                int imageColumnIndex = cursor.getColumnIndex(columnName);

                // Get column value which is the uri related file local path.
                ret = cursor.getString(imageColumnIndex);
            }
        }*/

        return ret;
    }

    public static String getFilePathFromURI(Context context, Uri contentUri) {
        //copy file and send new file path
        File fileDir = context.getFilesDir();
        String fileName = getFileName(context, contentUri);
        if (!TextUtils.isEmpty(fileName)) {
            File copyFile = new File(fileDir + File.separator + fileName);
            copy(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    public static String getFileName(Context context, Uri uri) {
        String fileName = null;
        Cursor cursor = context.getContentResolver().query(uri, new String[]{"_display_name", "_size"}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                try {
                    fileName = cursor.getString(cursor.getColumnIndexOrThrow("_display_name"));
                } catch (IllegalArgumentException e3) {

                }
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        /*if (uri == null) return null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }*/



        if(fileName != null){
            if (fileName.endsWith(".jpg")||
                    fileName.endsWith(".pdf")||
                    fileName.endsWith(".jpeg")||
                    fileName.endsWith(".png")||
                    fileName.endsWith(".gif")||
                    fileName.endsWith(".xls")||
                    fileName.endsWith(".pptx")||
                    fileName.endsWith(".ppt")||
                    fileName.endsWith(".docx")||
                    fileName.endsWith(".doc")||
                    fileName.endsWith(".xlsx")||
                    fileName.endsWith(".doc")){
                Log.d("myuri20", "getFileName: "+fileName);
                return fileName;
            } else {
                ContentResolver cR = context.getContentResolver();
                MimeTypeMap mime = MimeTypeMap.getSingleton();
                String type = mime.getExtensionFromMimeType(cR.getType(uri));
                Log.d("myuri20", "getFileName: "+fileName+"."+type);
                return fileName+"."+type;
            }
        } else {
            if(uri.toString().split("/").length > 1){
                return uri.toString().split("/")[uri.toString().split("/").length-1];
            } else {
                return "";
            }
        }
    }

    public static void copy(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            IOUtils.copy(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getMimeType(Context context, Uri uri){
        if(uri.getScheme() == ContentResolver.SCHEME_CONTENT){
            return context.getContentResolver().getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            if (fileExtension != null) {
                return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
            } else {
                return "application/octet-stream";
            }
        }
    }
}
