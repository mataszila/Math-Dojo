package example.matasolutions.mathdojo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SummaryActivity extends AppCompatActivity {

    GameStatistics stats;

    TextView total_points_textview;
    TextView summary_action_textview;

    FirebaseUser user;
    FirebaseAuth mAuth;

    FirebaseDatabase database;
    DatabaseReference myRef;

    Profile profile;

    Button startAgainButton;
    Button highScoresButton;

    TextView userStatsTextView;


    LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();


        ReadData(new MyProfileCallback() {
            @Override
            public void onProfileCallBack(Profile returnProfile) {

                profile = returnProfile;

                stats = getIntent().getParcelableExtra("gameStats");


                SetupViews();

                AssignXP();

            }
        });




    }

    public void AssignXP(){

        int ans = profile.levels.playerLevel.totalXP;

        profile.levels.playerLevel.addXP(stats.totalCount);
        profile.levels.skill_add_level.addXP(stats.addition_correct_count);
        profile.levels.skill_subtract_level.addXP(stats.subtraction_correct_count);
        profile.levels.skill_multiplication_level.addXP(stats.multiplication_correct_count);

        profile.levels.playerLevel.xpLeft -= stats.totalCount;
        profile.levels.skill_add_level.xpLeft -= stats.addition_correct_count;
        profile.levels.skill_subtract_level.xpLeft -= stats.subtraction_correct_count;
        profile.levels.skill_multiplication_level.xpLeft -= stats.multiplication_correct_count;

        myRef.child("user_data").child(mAuth.getUid()).setValue(profile);


        ScoreEntry this_total_score = new ScoreEntry(mAuth.getUid(),user.getEmail(),stats.totalCount);


        if(profile.username != null){
             this_total_score = new ScoreEntry(mAuth.getUid(),profile.username,stats.totalCount);

        }




        myRef.child("high_scores").child("total_points").child(myRef.push().getKey()).setValue(this_total_score);



    }


    public void SetupViews(){

        layout = findViewById(R.id.summary_activity_layout);
        layout.setVisibility(View.VISIBLE);
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);



        total_points_textview = findViewById(R.id.total_points_textview);

        total_points_textview.setText("You scored a total of " + stats.totalCount +  " points");





        summary_action_textview = findViewById(R.id.summary_action_textview);

        summary_action_textview.setText(SetSummaryText());

        startAgainButton = findViewById(R.id.summary_start_again_button);

        startAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 Intent intent = new Intent(getApplicationContext(),DojoActivity.class);
                 startActivity(intent);


            }
        });

        highScoresButton = findViewById(R.id.summary_high_scores_button);

        highScoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),HighScoresActivity.class);
                startActivity(intent);

            }
        });


        userStatsTextView = findViewById(R.id.summary_user_stats);
        userStatsTextView.setText(FormatStatsText());




    }

    private String FormatStatsText(){

        StringBuilder sb = new StringBuilder();

        sb.append("Overall level: " + profile.levels.playerLevel.levelNumber + " \n");
        sb.append(profile.levels.playerLevel.totalXP+stats.totalCount + "/" + profile.levels.playerLevel.totalXP_until_next_level + "XP" + " \n");

        sb.append("Addition level: " + profile.levels.skill_add_level.levelNumber + " \n");
        sb.append(profile.levels.skill_add_level.totalXP+stats.addition_correct_count + "/" + profile.levels.skill_add_level.totalXP_until_next_level + "XP" + " \n");

        sb.append("Subtraction level: " + profile.levels.skill_subtract_level.levelNumber + " \n");
        sb.append(profile.levels.skill_subtract_level.totalXP+stats.subtraction_correct_count + "/" + profile.levels.skill_subtract_level.totalXP_until_next_level + "XP" + " \n");

        sb.append("Multiplication level: " + profile.levels.skill_multiplication_level.levelNumber + " \n");
        sb.append(profile.levels.skill_multiplication_level.totalXP+stats.multiplication_correct_count + "/" + profile.levels.skill_multiplication_level.totalXP_until_next_level + "XP");

        return sb.toString();
    }


    void ReadData(final MyProfileCallback myCallback){

        myRef.child("user_data").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Profile thisProfile =  dataSnapshot.getValue(Profile.class);

                Levels thisLevels = null;
                myCallback.onProfileCallBack(thisProfile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private Profile ConvertSnapshot(DataSnapshot snapshot){

        profile =  snapshot.getValue(Profile.class);

        for (DataSnapshot snap: snapshot.getChildren()) {
            profile.levels = (Levels) snap.child("levels").getValue();
            profile.userID = (String) snap.child("userID").getValue();
        }

        return profile;

    }



    private String SetSummaryText(){

        StringBuilder sb = new StringBuilder();

        sb.append("Addition: " + stats.addition_correct_count + "\n");
        sb.append("Subtraction: " + stats.subtraction_correct_count + "\n");
        sb.append("Multiplication: " + stats.multiplication_correct_count + "\n");



        return  sb.toString();
    }

}
