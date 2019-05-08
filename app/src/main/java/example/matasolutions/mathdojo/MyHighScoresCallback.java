package example.matasolutions.mathdojo;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface MyHighScoresCallback {
    public void onHighScoresCallback(ArrayList<ScoreEntry> scoreEntries, ArrayList<Profile> profiles);
}
