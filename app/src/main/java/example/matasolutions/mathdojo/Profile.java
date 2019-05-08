package example.matasolutions.mathdojo;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile {

    FirebaseAuth mAuth;
    FirebaseUser user;

    public Levels levels;

    public String userID;


    public String username;

    public Profile(){

    }


    public Profile(String userID,String username){

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        this.userID = userID;

        levels = new Levels(true);

        this.username = username;

    }

}
