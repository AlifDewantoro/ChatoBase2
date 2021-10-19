package com.gamatechno.chato.sdk.utils.FilePath;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import androidx.annotation.RequiresApi;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.gamatechno.chato.sdk.data.DAO.Chat.Chat;

//import org.apache.commons.codec.binary.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;


public class IOUtils {
    private static final int BUFFER_SIZE = 1024 * 2;

    private IOUtils() {
        // Utility class.
    }

    public static int copy(InputStream input, OutputStream output) throws Exception, IOException {
        byte[] buffer = new byte[BUFFER_SIZE];

        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        BufferedOutputStream out = new BufferedOutputStream(output, BUFFER_SIZE);
        int count = 0, n = 0;
        try {
            while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, n);
                count += n;
            }
            out.flush();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                Log.e(e.getMessage(), ""+e);
            }
            try {
                in.close();
            } catch (IOException e) {
                Log.e(e.getMessage(), ""+e);
            }
        }
        return count;
    }

    public static InputStream getInputStreamForVirtualFile(Context context, Uri uri, String mimeTypeFilter) throws FileNotFoundException {
        ContentResolver resolver = context.getContentResolver();

        String[] openableMimeTypes = resolver.getStreamTypes(uri, mimeTypeFilter);
        if (openableMimeTypes == null || openableMimeTypes.length == 0) {

        }

        try {
            return resolver.openTypedAssetFileDescriptor(uri, openableMimeTypes[0], null).createInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static InputStream getInputStream(Context context, Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isVirtualFile(context, uri)) {
                try {
                    return getInputStreamForVirtualFile(context, uri, "*/*");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            return context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean isVirtualFile(Context context, Uri uri){
        if (!DocumentsContract.isDocumentUri(context, uri)) {
            return false;
        }

        Cursor cursor = context.getContentResolver().query(uri, new String[]{DocumentsContract.Document.COLUMN_FLAGS}, null, null, null);
        int flags = 0;

        if (cursor.moveToFirst()) {
            flags = cursor.getInt(0);
        }
        cursor.close();

        return (flags & DocumentsContract.Document.FLAG_VIRTUAL_DOCUMENT) != 0;
    }

    public static byte[] readAllBytes(InputStream inputStream) throws IOException {
        final int bufLen = 4 * 0x400; // 4KB
        byte[] buf = new byte[bufLen];
        int readLen;
        IOException exception = null;

        try {
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                while ((readLen = inputStream.read(buf, 0, bufLen)) != -1)
                    outputStream.write(buf, 0, readLen);

                return outputStream.toByteArray();
            }
        } catch (IOException e) {
            exception = e;
            throw e;
        } finally {
            if (exception == null) inputStream.close();
            else try {
                inputStream.close();
            } catch (IOException e) {
                exception.addSuppressed(e);
            }
        }
    }

    public static String convertToBase64(InputStream inputStream){
        byte[] bytes = new byte[0];
        try {
            bytes = readAllBytes(inputStream);
//            return Base64Util.encodeBytes(bytes);
            Base64.Encoder enc = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                enc = Base64.getEncoder();
                return new String(enc.encodeToString(bytes));
            } else {
                return android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT);
            }

//            return new String(Base64.encodeBase64(bytes)).replace('+','-').replace('/','_');

//            return Base64.encodeBase64String(bytes);
//            return Base64.encodeBase64String((bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Boolean isFileExist(String uri){
        File f = new File(uri);
        if(f.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean isDirectiryExist(String uri){
        File f = new File(uri);
        if(f.isDirectory()) {
            return true;
        } else {
            return false;
        }
    }

    public static void openFile(Context context, File url) throws IOException {
        // Create URI
        File file = url;
        Uri uri = Uri.fromFile(file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if (url.toString().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/x-wav");
        } else if (url.toString().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if (url.toString().contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if (url.toString().contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            //if you want you can also define the intent type for any other file
            //additionally use else clause below, to manage other unknown extensions
            //in this case, Android will show all applications installed on the device
            //so you can choose which application to use
            intent.setDataAndType(uri, "*/*");
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static String getTypeFile(Context context, Uri uri){
        String type = "";
        if(FilePath.isFile(uri)){
            for(String x : uri.getPath().split(".")){
                type = x;
            }
        }else{
            ContentResolver cR = context.getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            Log.d("asd", "getTypeFile: "+uri.getPath());
            type = mime.getExtensionFromMimeType(cR.getType(uri));
        }
        Log.d("asd", "getTypeFile: "+type);
        switch (type){
            case "pdf":
                type = "application/pdf";
                break;
            case "docx":
                type = "application/msword";
                break;
            case "doc":
                type = "application/msword";
                break;
            case "jpg":
                type = "image/jpg";
                break;
            case "png":
                type = "image/png";
                break;
            case "xls":
                type = "application/vnd.ms-excel";
                break;
            case "xlsx":
                type = "application/vnd.ms-excel";
                break;
            case "jpeg":
                type = "image/jpg";
                break;
        }
        return type;
    }

    public static void savefile(Uri sourceuri, String type){
        String sourceFilename= sourceuri.getPath();
        String destinationFilename = "";
        switch (type){
            case Chat.chat_type_image:
                destinationFilename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/Chato/Images/";
                break;
            case Chat.chat_type_file:
                destinationFilename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/Chato/Document/";
                break;
            case Chat.chat_type_video:
                destinationFilename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/Chato/Video/";
                break;
        }

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while(bis.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void savefile(Uri sourceuri, String type, String filename, Context context){
        String sourceFilename= sourceuri.getPath();
        String destinationFilename = "";
        switch (type){
            case Chat.chat_type_image:
                destinationFilename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/Chato/Images/"+filename;
                break;
            case Chat.chat_type_file:
                destinationFilename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/Chato/Document/"+filename;
                break;
            case Chat.chat_type_video:
                destinationFilename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/Chato/Video/"+filename;
                break;
        }

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while(bis.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File directory = new File(destinationFilename);
        File from      = new File(directory, FilePath.getFileName(context, sourceuri));
        File to        = new File(directory, filename);
        from.renameTo(to);
    }

    public static void copyFile(String real_name, String type, String filename){
        String destinationFilename = "";
        switch (type){
            case Chat.chat_type_image:
                destinationFilename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/Chato/Images/";
                break;
            case Chat.chat_type_file:
                destinationFilename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/Chato/Document/";
                break;
            case Chat.chat_type_video:
                destinationFilename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/Chato/Video/";
                break;
        }
        File directory = new File(destinationFilename);
        File from      = new File(directory, real_name);
        File to        = new File(directory, filename);
        from.renameTo(to);
    }
}
