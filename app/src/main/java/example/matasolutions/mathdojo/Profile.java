package example.matasolutions.mathdojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile implements  Parcelable{

    FirebaseAuth mAuth;
    FirebaseUser user;

    public Levels levels;

    public String userID;

    public String selected_country;

    public UserStats user_stats;

    public String username;

    public Profile(){

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        this.user_stats = new UserStats();

    }


    public Profile(String userID,String username, String selected_country){


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        this.userID = userID;

        levels = new Levels(true);

        this.selected_country  = selected_country;

        this.username = username;

    }

    protected Profile(Parcel in) {
        levels = (Levels) in.readValue(Levels.class.getClassLoader());
        userID = in.readString();
        selected_country = in.readString();
        user_stats = (UserStats) in.readValue(UserStats.class.getClassLoader());
        username = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(levels);
        dest.writeString(userID);
        dest.writeString(selected_country);
        dest.writeValue(user_stats);
        dest.writeString(username);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Profile> CREATOR = new Parcelable.Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

}
