package baasj005.ivmd.moodmonitor.tasks;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MoodGetTask extends AsyncTask<Void, Void, Response> {
    private OkHttpClient client;

    public MoodGetTask() {
        client = new OkHttpClient();
    }

    @Override
    protected Response doInBackground(Void... params) {
        Request request = new Request.Builder()
                .url(MoodPostTask.URL)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(response);
        return response;
    }

    @Override
    protected void onPostExecute(Response result){
        // do something with the result
        System.out.println(result);
    }
}
