/* CSC3095 Portfolio Part 2
 * 2019-05-07
 * Author : Matas Zilaitis
 */

package example.matasolutions.mathdojo;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

// This class is stored in the database as all user's level related data.

public class Levels implements Parcelable,Serializable {

    public Level playerLevel;

    public Level  skill_add_level;

    public Level skill_subtract_level;

    public Level skill_multiplication_level;



    public Levels(boolean newPlayer){


        if(newPlayer){

            playerLevel = new Level(LevelType.OVERALL);
            skill_add_level = new Level(LevelType.SKILL_ADD);
            skill_subtract_level = new Level(LevelType.SKILL_SUBTRACT);
            skill_multiplication_level = new Level(LevelType.SKILL_MULTIPLICATION);
        }
    }

    public Levels(){

    }

    protected Levels(Parcel in) {
        playerLevel = (Level) in.readValue(Level.class.getClassLoader());
        skill_add_level = (Level) in.readValue(Level.class.getClassLoader());
        skill_subtract_level = (Level) in.readValue(Level.class.getClassLoader());
        skill_multiplication_level = (Level) in.readValue(Level.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(playerLevel);
        dest.writeValue(skill_add_level);
        dest.writeValue(skill_subtract_level);
        dest.writeValue(skill_multiplication_level);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Levels> CREATOR = new Parcelable.Creator<Levels>() {
        @Override
        public Levels createFromParcel(Parcel in) {
            return new Levels(in);
        }

        @Override
        public Levels[] newArray(int size) {
            return new Levels[size];
        }
    };


}
