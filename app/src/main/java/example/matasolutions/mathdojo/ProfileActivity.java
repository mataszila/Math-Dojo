package example.matasolutions.mathdojo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.FormatFlagsConversionMismatchException;

public class ProfileActivity extends AppCompatActivity {

    Profile profile;
    FirebaseAuth mAuth;
    FirebaseUser user;

    FirebaseDatabase database;
    DatabaseReference myRef;

    TextView profile_ranking;

    ArrayList<Profile> profile_list;
    ArrayList<ScoreEntry> score_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        setTitle("Profile");

        ReadData(new MyHighScoresCallback() {
            @Override
            public void onHighScoresCallback(ArrayList<ScoreEntry> scoreEntries, ArrayList<Profile> profiles) {

                profile_list = profiles;
                score_list = scoreEntries;
                SetupActivity();
            }
        });


    }

    private void SetupActivity(){

        profile = getIntent().getParcelableExtra("profile");


        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();

        SetupViews();



    }

    private void DetermineUserRanking(){

        Collections.sort(score_list, new Comparator<ScoreEntry>() {
            @Override
            public int compare(ScoreEntry o1, ScoreEntry o2) {

                Double p1 = new Double(o1.score);
                Double p2 = new Double(o2.score);

                return Double.compare(p2,p1);


            }
        });

        for(int i=0;i<score_list.size();i++){

            if(score_list.get(i).userID .equals(mAuth.getUid())){

                profile.user_stats.total_points_global_ranking = i+1;
                break;
            }
        }


    }


    private void SetupViews(){

        profile_ranking = findViewById(R.id.profile_ranking);

        DetermineUserRanking();


        profile_ranking.setText(FormatRankingText());


        SetupRecyclerView();



    }

    private String FormatRankingText(){

        StringBuilder sb = new StringBuilder();

        sb.append("Personal best: " + profile.user_stats.total_points + "\n");
        sb.append("Global ranking: " + profile.user_stats.total_points_global_ranking);

        return sb.toString();
    }


    private void SetupRecyclerView(){
        ArrayList<Level> levels = new ArrayList<>();

        levels.add(profile.levels.playerLevel);
        levels.add(profile.levels.skill_add_level);
        levels.add(profile.levels.skill_subtract_level);
        levels.add(profile.levels.skill_multiplication_level);


        RecyclerView recyclerView = findViewById(R.id.profile_recycler_view);
        MyProfileAdapter myAdapter = new MyProfileAdapter(levels,getApplicationContext());

        LinearLayoutManager linearLayoutManager = new  LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(myAdapter);

    }

    void ReadData(final MyHighScoresCallback myCallback){

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<ScoreEntry> scoreEntriesReturned =  ConvertSnapshot(dataSnapshot.child("high_scores").child("total_points"));

                ArrayList<Profile> profileArrayListReturned = ConvertProfileSnapshot(dataSnapshot.child("user_data"));

                myCallback.onHighScoresCallback(scoreEntriesReturned,profileArrayListReturned);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public ArrayList<ScoreEntry> ConvertSnapshot(DataSnapshot snapshot){

        ArrayList<ScoreEntry> list = new ArrayList<>();

        for(DataSnapshot snap : snapshot.getChildren()){

            list.add(snap.getValue(ScoreEntry.class));



        }
        return list;


    }


    public ArrayList<Profile> ConvertProfileSnapshot(DataSnapshot snapshot){

        ArrayList<Profile> list = new ArrayList<>();

        for(DataSnapshot snap : snapshot.getChildren()){

            list.add(snap.getValue(Profile.class));



        }
        return list;


    }



}
