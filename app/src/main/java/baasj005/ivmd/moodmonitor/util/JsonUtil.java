package baasj005.ivmd.moodmonitor.util;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import baasj005.ivmd.moodmonitor.models.Mood;

/**
 * Created by Jesse on 3-1-2016.
 */
public class JsonUtil {
    public static ArrayList<Mood> getMoodFromInputStream(InputStream is) {
        Gson gson = new Gson();
        Mood[] moods = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
            String line;
            while ((line = reader.readLine()) != null) {
                moods = gson.fromJson(line, Mood[].class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return new ArrayList<Mood>(Arrays.asList(moods != null ? moods : new Mood[0]));
    }
}
