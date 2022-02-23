package com.aks.doggydoo.utils.fileUtils;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Video;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileFilter;
import java.text.DecimalFormat;
import java.util.Comparator;


public class FileUtils {
    private static final boolean DEBUG = false;
    public static final String HIDDEN_PREFIX = ".";
    public static final String MIME_TYPE_APP = "application/*";
    public static final String MIME_TYPE_AUDIO = "audio/*";
    public static final String MIME_TYPE_IMAGE = "image/*";
    public static final String MIME_TYPE_TEXT = "text/*";
    public static final String MIME_TYPE_VIDEO = "video/*";
    static final String TAG = "FileUtils";
    public static Comparator<File> sComparator = new Comparator<File>() {
        public int compare(File f1, File f2) {
            return f1.getName().toLowerCase().compareTo(f2.getName().toLowerCase());
        }
    };
    public static FileFilter sDirFilter = new FileFilter() {
        public boolean accept(File file) {
            return file.isDirectory() && !file.getName().startsWith(FileUtils.HIDDEN_PREFIX);
        }
    };
    public static FileFilter sFileFilter = new FileFilter() {
        public boolean accept(File file) {
            return file.isFile() && !file.getName().startsWith(FileUtils.HIDDEN_PREFIX);
        }
    };

    private FileUtils() {
    }

    public static String getExtension(String uri) {
        if (uri == null) {
            return null;
        }
        int dot = uri.lastIndexOf(HIDDEN_PREFIX);
        if (dot >= 0) {
            return uri.substring(dot);
        }
        return "";
    }

    public static boolean isLocal(String url) {
        if (url == null || url.startsWith("http://") || url.startsWith("https://")) {
            return false;
        }
        return true;
    }

    public static boolean isMediaUri(Uri uri) {
        return "media".equalsIgnoreCase(uri.getAuthority());
    }

    public static Uri getUri(File file) {
        if (file != null) {
            return Uri.fromFile(file);
        }
        return null;
    }

    public static File getPathWithoutFilename(File file) {
        if (file == null) {
            return null;
        }
        if (file.isDirectory()) {
            return file;
        }
        String filename = file.getName();
        String filepath = file.getAbsolutePath();
        String pathwithoutname = filepath.substring(0, filepath.length() - filename.length());
        if (pathwithoutname.endsWith("/")) {
            pathwithoutname = pathwithoutname.substring(0, pathwithoutname.length() - 1);
        }
        return new File(pathwithoutname);
    }

    public static String getMimeType(File file) {
        String extension = getExtension(file.getName());
        if (extension.length() > 0) {
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.substring(1));
        }
        return "application/octet-stream";
    }

    public static String getMimeType(Context context, Uri uri) {
        return getMimeType(new File(getPath(context, uri)));
    }

    public static boolean isLocalStorageDocument(Uri uri) {
        return LocalStorageProvider.AUTHORITY.equals(uri.getAuthority());
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /* JADX INFO: finally extract failed */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String str = "_data";
        String str2 = "_data";
        try {
            Cursor cursor2 = context.getContentResolver().query(uri, new String[]{str2}, selection, selectionArgs, null);
            if (cursor2 == null || !cursor2.moveToFirst()) {
                if (cursor2 != null) {
                    cursor2.close();
                }
                return null;
            }
            String string = cursor2.getString(cursor2.getColumnIndexOrThrow(str2));
            if (cursor2 != null) {
                cursor2.close();
            }
            return string;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    public static String getPath(Context context, Uri uri) {
        if (!(VERSION.SDK_INT >= 19) || !DocumentsContract.isDocumentUri(context, uri)) {
            if (!"content".equalsIgnoreCase(uri.getScheme())) {
                if ("file".equalsIgnoreCase(uri.getScheme())) {
                    return uri.getPath();
                }
            } else if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            } else {
                return getDataColumn(context, uri, null, null);
            }
        } else if (isLocalStorageDocument(uri)) {
            return DocumentsContract.getDocumentId(uri);
        } else {
            String str = ":";
            if (isExternalStorageDocument(uri)) {
                String[] split = DocumentsContract.getDocumentId(uri).split(str);
                if ("primary".equalsIgnoreCase(split[0])) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(Environment.getExternalStorageDirectory());
                    sb.append("/");
                    sb.append(split[1]);
                    return sb.toString();
                }
            } else if (isDownloadsDocument(uri)) {
                return getDataColumn(context, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(DocumentsContract.getDocumentId(uri)).longValue()), null, null);
            } else if (isMediaDocument(uri)) {
                String[] split2 = DocumentsContract.getDocumentId(uri).split(str);
                String type = split2[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String str2 = "_id=?";
                return getDataColumn(context, contentUri, "_id=?", new String[]{split2[1]});
            }
        }
        return null;
    }

    public static File getFile(Context context, Uri uri) {
        if (uri != null) {
            String path = getPath(context, uri);
            if (path != null && isLocal(path)) {
                return new File(path);
            }
        }
        return null;
    }

    public static String getReadableFileSize(int size) {
        DecimalFormat dec = new DecimalFormat("###.#");
        String str = " KB";
        String str2 = " MB";
        String str3 = " GB";
        float fileSize = 0.0f;
        String suffix = " KB";
        if (size > 1024) {
            fileSize = (float) (size / 1024);
            if (fileSize > 1024.0f) {
                fileSize /= 1024.0f;
                if (fileSize > 1024.0f) {
                    fileSize /= 1024.0f;
                    suffix = " GB";
                } else {
                    suffix = " MB";
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append(dec.format((double) fileSize));
        sb.append(suffix);
        return String.valueOf(sb.toString());
    }

    public static Bitmap getThumbnail(Context context, File file) {
        return getThumbnail(context, getUri(file), getMimeType(file));
    }

    public static Bitmap getThumbnail(Context context, Uri uri) {
        return getThumbnail(context, uri, getMimeType(context, uri));
    }


    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static Bitmap getThumbnail(Context r10, Uri r11, String r12) {
        throw new UnsupportedOperationException("Method not decompiled: in.app.wedpronew.utils.FileUtils.getThumbnail(android.content.Context, android.net.Uri, java.lang.String):android.graphics.Bitmap");
    }

    public static Intent createGetContentIntent() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("*/*");
        intent.addCategory("android.intent.category.OPENABLE");
        return intent;
    }
}
