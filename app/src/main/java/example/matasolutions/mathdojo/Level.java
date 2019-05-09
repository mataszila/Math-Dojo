package example.matasolutions.mathdojo;

import android.os.Parcel;
import android.os.Parcelable;

public class Level implements Parcelable {

    public int levelNumber;
    public LevelType levelType;

    public Double totalXP;

    public Double totalXP_until_next_level;

    public Double xpLeft;

    public Double starting_xpLeft;

    public Double previous_xpLeft;

    public Double previous_totalXP_until_next_level;

    public Level(){

        levelNumber = 1;
        totalXP = new Double(0);

        previous_totalXP_until_next_level = new Double(0);

        totalXP_until_next_level = new Double(10);

        starting_xpLeft = new Double(10);

        previous_xpLeft = starting_xpLeft;

        xpLeft = starting_xpLeft;

    }

    public void LevelUp(){


        if(totalXP >= totalXP_until_next_level){

            levelNumber++;

            previous_totalXP_until_next_level = totalXP_until_next_level;

            Double newXp =  (previous_xpLeft)  + ((previous_xpLeft) * 1.1);

            previous_xpLeft = newXp;

            totalXP_until_next_level = totalXP + newXp.intValue();

            xpLeft = totalXP_until_next_level - totalXP;

        }



    }

    public void addXP(double add){
        totalXP += add;
        LevelUp();


    }

    protected Level(Parcel in) {
        levelNumber = in.readInt();
        levelType = (LevelType) in.readValue(LevelType.class.getClassLoader());
        totalXP = in.readByte() == 0x00 ? null : in.readDouble();
        totalXP_until_next_level = in.readByte() == 0x00 ? null : in.readDouble();
        xpLeft = in.readByte() == 0x00 ? null : in.readDouble();
        starting_xpLeft = in.readByte() == 0x00 ? null : in.readDouble();
        previous_xpLeft = in.readByte() == 0x00 ? null : in.readDouble();
        previous_totalXP_until_next_level = in.readByte() == 0x00 ? null : in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(levelNumber);
        dest.writeValue(levelType);
        if (totalXP == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(totalXP);
        }
        if (totalXP_until_next_level == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(totalXP_until_next_level);
        }
        if (xpLeft == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(xpLeft);
        }
        if (starting_xpLeft == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(starting_xpLeft);
        }
        if (previous_xpLeft == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(previous_xpLeft);
        }
        if (previous_totalXP_until_next_level == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(previous_totalXP_until_next_level);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Level> CREATOR = new Parcelable.Creator<Level>() {
        @Override
        public Level createFromParcel(Parcel in) {
            return new Level(in);
        }

        @Override
        public Level[] newArray(int size) {
            return new Level[size];
        }
    };






}

