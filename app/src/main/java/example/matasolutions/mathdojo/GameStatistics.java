package example.matasolutions.mathdojo;

import android.os.Parcel;
import android.os.Parcelable;

public class GameStatistics implements Parcelable {

    public int addition_correct_count;
    public int subtraction_correct_count;
    public int multiplication_correct_count;
    public int division_correct_count;

    public int totalCount;


    public void correct_addition(){
        addition_correct_count++;
        totalCount++;
    }

    public void correct_subtraction(){
        subtraction_correct_count++;
        totalCount++;

    }
    public void correct_multiplication(){
        multiplication_correct_count++;
        totalCount++;

    }

    public void correct_division(){
        //division_correct_count++;
        totalCount++;
    }



    public GameStatistics(boolean isNewGame){


    }

    protected GameStatistics(Parcel in) {
        addition_correct_count = in.readInt();
        subtraction_correct_count = in.readInt();
        multiplication_correct_count = in.readInt();
        division_correct_count = in.readInt();
        totalCount = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(addition_correct_count);
        dest.writeInt(subtraction_correct_count);
        dest.writeInt(multiplication_correct_count);
        dest.writeInt(division_correct_count);
        dest.writeInt(totalCount);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<GameStatistics> CREATOR = new Parcelable.Creator<GameStatistics>() {
        @Override
        public GameStatistics createFromParcel(Parcel in) {
            return new GameStatistics(in);
        }

        @Override
        public GameStatistics[] newArray(int size) {
            return new GameStatistics[size];
        }
    };


}
