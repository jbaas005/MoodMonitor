package baasj005.ivmd.moodmonitor.util;

import java.util.Comparator;

import baasj005.ivmd.moodmonitor.models.Mood;

/**
 * Created by Jesse on 4-1-2016.
 */
public class MoodDateComparator implements Comparator<Mood> {
    @Override
    public int compare(Mood lhs, Mood rhs) {
        return lhs.compareDate(rhs);
    }
}
