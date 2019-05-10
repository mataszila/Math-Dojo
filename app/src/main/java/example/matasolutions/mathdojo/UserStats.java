/* CSC3095 Portfolio Part 2
 * 2019-05-09
 * Author : Matas Zilaitis
 */


package example.matasolutions.mathdojo;

import android.os.Parcel;
import android.os.Parcelable;

public class UserStats implements Parcelable {

    //Responsible for storing user records and stats in the database;

    public int total_points;
    public int total_points_global_ranking;
    public int total_points_local_ranking;

    public UserStats(){

        total_points = 0;


    }
    protected UserStats(Parcel in) {
        total_points = in.readInt();
        total_points_global_ranking = in.readInt();
        total_points_local_ranking = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(total_points);
        dest.writeInt(total_points_global_ranking);
        dest.writeInt(total_points_local_ranking);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<UserStats> CREATOR = new Parcelable.Creator<UserStats>() {
        @Override
        public UserStats createFromParcel(Parcel in) {
            return new UserStats(in);
        }

        @Override
        public UserStats[] newArray(int size) {
            return new UserStats[size];
        }
    };


}
