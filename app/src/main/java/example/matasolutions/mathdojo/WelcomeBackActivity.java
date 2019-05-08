package example.matasolutions.mathdojo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomeBackActivity extends AppCompatActivity {


    TextView welcome_back_user;
    Button proceed;
    Button logout;
    Button high_scores;

    FirebaseUser currentUser;

    FirebaseDatabase database;
    DatabaseReference myRef;

    FirebaseAuth mAuth;

    Profile profile;

    LinearLayout layout;

    TextView user_level_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_back);

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();



        ReadData(new MyProfileCallback() {
            @Override
            public void onProfileCallBack(Profile profileReturned) {

                profile = profileReturned;
                SetupActivity();

            }
        });


    }

    void SetupActivity(){


        welcome_back_user = findViewById(R.id.welcome_back_user_textView);

        welcome_back_user.setText("Welcome, " + currentUser.getEmail());


        logout = findViewById(R.id.button_logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        proceed = findViewById(R.id.button_proceed);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),DojoActivity.class);

                startActivity(intent);

            }
        });

        high_scores =  findViewById(R.id.high_scores_button);
        high_scores.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(getApplicationContext(),HighScoresActivity.class));


                    }
                }
        );

        SetupStats();
    }


    void SetupStats(){

        user_level_text = findViewById(R.id.welcome_back_user_level_progress_text);

        user_level_text.setText(FormatStatsText(profile));




    }

    public String FormatStatsText(Profile profile){

        StringBuilder sb = new StringBuilder();

        sb.append("Overall level: " + profile.levels.playerLevel.levelNumber + " \n");
        sb.append(profile.levels.playerLevel.totalXP + "/" + profile.levels.playerLevel.totalXP_until_next_level + "XP" + " \n");

        sb.append("Addition level: " + profile.levels.skill_add_level.levelNumber + " \n");
        sb.append(profile.levels.skill_add_level.totalXP + "/" + profile.levels.skill_add_level.totalXP_until_next_level + "XP" + " \n");

        sb.append("Subtraction level: " + profile.levels.skill_subtract_level.levelNumber + " \n");
        sb.append(profile.levels.skill_subtract_level.totalXP + "/" + profile.levels.skill_subtract_level.totalXP_until_next_level + "XP" + " \n");

        sb.append("Multiplication level: " + profile.levels.skill_multiplication_level.levelNumber + " \n");
        sb.append(profile.levels.skill_multiplication_level.totalXP + "/" + profile.levels.skill_multiplication_level.totalXP_until_next_level + "XP");

        return sb.toString();
    }



    void ReadData(final MyProfileCallback myCallback){

        myRef.child("user_data").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Profile thisProfile =  dataSnapshot.getValue(Profile.class);

                myCallback.onProfileCallBack(thisProfile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
