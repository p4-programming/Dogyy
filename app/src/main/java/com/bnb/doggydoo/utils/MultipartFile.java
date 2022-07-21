package com.bnb.doggydoo.utils;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.bnb.doggydoo.utils.fileUtils.FileUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MultipartFile {

    @NonNull
    public static MultipartBody.Part prepareFilePart(Context context, String partName, Uri fileUri) {
        if (fileUri == null) {
            RequestBody requestFile = RequestBody.create("", MultipartBody.FORM);

            // MultipartBody.Part is used to send also the actual file name
            return MultipartBody.Part.createFormData(partName, "", requestFile);

        } else {
            File file = FileUtils.getFile(context, fileUri);
            String path = FileUtils.getPath(context, fileUri);
            // create RequestBody instance from file
            assert path != null;
            RequestBody requestFile = RequestBody.create(file, MediaType.parse(path));

            // MultipartBody.Part is used to send also the actual file name
            return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);

        }
    }

    public static RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(descriptionString, MultipartBody.FORM);
    }
}
