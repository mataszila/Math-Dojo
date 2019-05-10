/* CSC3095 Portfolio Part 2
 * 2019-05-07
 * Author : Matas Zilaitis
 */

package example.matasolutions.mathdojo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

public class SummaryActivity extends AppCompatActivity {

    GameStatistics stats;

    TextView total_points_textview;

    FirebaseUser user;
    FirebaseAuth mAuth;

    FirebaseDatabase database;
    DatabaseReference myRef;

    Profile profile;

    Button startAgainButton;

    TextView last_question_textview;

    CurrentQuestion lastQuestion;

    BottomNavigationView bottomNavigationView;

    LinearLayout layout;

    //This class is displayed when the user loses the game.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        lastQuestion = getIntent().getParcelableExtra("lastQuestion");

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

    private void SetupBottomNavigationView(){

        bottomNavigationView = findViewById(R.id.summary_bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int menuId = menuItem.getItemId();

                switch(menuId){

                    case R.id.summary_nav_high_scores:
                        Intent intent = new Intent(getApplicationContext(),HighScoresActivity.class);
                        startActivity(intent);

                        break;
                    case R.id.summary_nav_profile:
                        finish();
                        Intent intent_profile = new Intent(getApplicationContext(),ProfileActivity.class);
                        intent_profile.putExtra("profile",profile);
                        startActivity(intent_profile);
                        break;
                    case R.id.summary_nav_home:
                        finish();
                        Intent home = new Intent(getApplicationContext(),WelcomeBackActivity.class);
                        home.putExtra("profile",profile);
                        startActivity(home);
                        break;
                }

                return false;
            }
        });

    }



    //Adds user XP and updates the entry in the database.

    public void AssignXP(){


        profile.levels.playerLevel.addXP(stats.totalCount*0.25);
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

        if(this_total_score.score > 0) {

            myRef.child("high_scores").child("total_points").child(myRef.push().getKey()).setValue(this_total_score);
        }

        if(this_total_score.score > profile.user_stats.total_points ){

            profile.user_stats.total_points = this_total_score.score;

            myRef.child("user_data").child(mAuth.getUid()).child("user_stats").child("total_points").setValue(profile.user_stats.total_points);

        }



    }


    public void SetupViews(){

        layout = findViewById(R.id.summary_activity_layout);
        layout.setVisibility(View.VISIBLE);
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);


        last_question_textview = findViewById(R.id.last_question_textview);

        total_points_textview = findViewById(R.id.total_points_textview);

        total_points_textview.setText("You scored a total of " + stats.totalCount +  " points");



        last_question_textview.setText(SetLastQuestionText());

        startAgainButton = findViewById(R.id.summary_start_again_button);

        startAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                 Intent intent = new Intent(getApplicationContext(),DojoActivity.class);
                 startActivity(intent);


            }
        });



        SetupRecyclerView();
        SetupBottomNavigationView();

    }

    private void SetupRecyclerView(){

            ArrayList<Level> levels = new ArrayList<>();

            levels.add(profile.levels.playerLevel);
            levels.add(profile.levels.skill_add_level);
            levels.add(profile.levels.skill_subtract_level);
            levels.add(profile.levels.skill_multiplication_level);


            RecyclerView recyclerView = findViewById(R.id.summary_recycler_view);
            MyProfileAdapter myAdapter = new MyProfileAdapter(levels,getApplicationContext());

            LinearLayoutManager linearLayoutManager = new  LinearLayoutManager(this);

            recyclerView.setLayoutManager(linearLayoutManager);

            recyclerView.setAdapter(myAdapter);


    }

    private String SetLastQuestionText(){
        StringBuilder sb = new StringBuilder();

        sb.append("Last question was:" + "\n");
        sb.append(lastQuestion.GetQuestionString() + "\n");
        sb.append("Correct answer was: " + String.valueOf(lastQuestion.correct_answer) + "\n");
        sb.append("You selected : " + String.valueOf(lastQuestion.selected_answer));


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
