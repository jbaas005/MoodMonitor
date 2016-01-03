package baasj005.ivmd.moodmonitor.tasks;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.IOException;

import baasj005.ivmd.moodmonitor.models.Mood;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MoodPostTask extends AsyncTask<Mood, Void, Response> {
    private OkHttpClient client;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String URL = "http://128.199.63.135:8080/mood";

    public MoodPostTask() {
        client = new OkHttpClient();
    }

    @Override
    protected Response doInBackground(Mood... params) {
        RequestBody body = RequestBody.create(JSON, new Gson().toJson(params[0]));
        Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

}
