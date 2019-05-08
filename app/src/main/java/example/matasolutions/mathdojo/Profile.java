package example.matasolutions.mathdojo;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile {

    FirebaseAuth mAuth;
    FirebaseUser user;

    public Levels levels;

    public String userID;

    public String selected_country;


    public String username;

    public Profile(){

    }


    public Profile(String userID,String username, String selected_country){

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        this.userID = userID;

        levels = new Levels(true);

        this.selected_country  = selected_country;

        this.username = username;

    }

}
