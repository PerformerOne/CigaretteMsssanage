package me.kareluo.imaging.core.file;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by felix on 2017/12/26 下午3:07.
 */

public class IMGFileDecoder extends IMGDecoder {

    public IMGFileDecoder(Uri uri) {
        super(uri);
    }

    @Override
    public Bitmap decode(BitmapFactory.Options options) {
        Uri uri = getUri();
        if (uri == null) {
            return null;
        }

        String path = uri.getPath();
        if (TextUtils.isEmpty(path)) {
            return null;
        }

        File file = new File(path);
        if (file.exists()) {
            return BitmapFactory.decodeFile(path, options);
        }else if (path.contains("camera_photos")){
            String result = path.substring(14, path.length());
            File resultFile = new File(result);
            if (resultFile.exists()){
                return BitmapFactory.decodeFile(result, options);
            }
        }

        return null;
    }
}
