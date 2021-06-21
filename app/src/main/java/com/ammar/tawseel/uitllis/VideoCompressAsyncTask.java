package com.ammar.tawseel.uitllis;

import android.content.Context;
import android.os.AsyncTask;

import com.iceteck.silicompressorr.SiliCompressor;

import java.io.File;
import java.net.URISyntaxException;

public class VideoCompressAsyncTask extends AsyncTask<String, String, String> {

    Context mContext;

    public VideoCompressAsyncTask(Context context) {
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... paths) {
        String filePath = null;
        try {
            filePath = SiliCompressor.with(mContext).compressVideo(paths[0], paths[1]);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return filePath;

    }

    @Override
    protected void onPostExecute(String compressedFilePath) {
        super.onPostExecute(compressedFilePath);

        File videoFile = new File(compressedFilePath);

    }
}