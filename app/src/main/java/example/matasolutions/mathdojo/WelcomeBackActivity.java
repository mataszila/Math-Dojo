/* CSC3095 Portfolio Part 2
 * 2019-05-06
 * Author : Matas Zilaitis
 */

package example.matasolutions.mathdojo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

// This is the screen that the user sees when they log in with the application.
// Main activity of the application, allows user to access all pages from here

public class WelcomeBackActivity extends AppCompatActivity {


    TextView welcome_back_user;
    Button proceed;

    ImageView dojo_image;

    FirebaseUser currentUser;

    FirebaseDatabase database;
    DatabaseReference myRef;

    FirebaseAuth mAuth;

    Profile profile;

    LinearLayout layout;

    RelativeLayout loading;



    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_back);

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        layout = findViewById(R.id.welcome_back_activity_layout);
        loading = findViewById(R.id.welcome_back_loading);

            ReadData(new MyProfileCallback() {
                @Override
                public void onProfileCallBack(Profile profileReturned) {

                    profile = profileReturned;
                    SetupActivity();


                }
            });

        }





    void SetupActivity(){

        loading.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);

        dojo_image = findViewById(R.id.welcome_back_imageView);

        Picasso.get()
                .load("https://i.imgur.com/H6C61ON.jpg")
                .resize(720, 540)
                .centerCrop()
                .into(dojo_image);



        welcome_back_user = findViewById(R.id.welcome_back_user_textView);

        welcome_back_user.setText("Welcome, " + profile.username);

        proceed = findViewById(R.id.button_proceed);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),DojoActivity.class));


            }
        });

        if(currentUser != null){

            SetupRecyclerView();

        }

        else{
            RecyclerView recyclerView = findViewById(R.id.welcome_back_recycler_view);
            recyclerView.setVisibility(View.INVISIBLE);
        }

        SetupBottomNavigationView();
    }

    private void SetupBottomNavigationView(){

        bottomNavigationView = findViewById(R.id.welcome_back_bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int menuId = menuItem.getItemId();

                switch(menuId){

                    case R.id.welcome_back_nav_profile:

                        Intent intent_profile = new Intent(getApplicationContext(),ProfileActivity.class);
                        intent_profile.putExtra("profile",profile);
                        startActivity(intent_profile);

                        break;
                    case R.id.welcome_back_nav_high_scores:
                        startActivity(new Intent(getApplicationContext(),HighScoresActivity.class));


                        break;
                    case R.id.welcome_back_nav_high_logout:
                        mAuth.signOut();
                        finish();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        break;
                }

                return false;
            }
        });

    }

    void SetupRecyclerView(){

        ArrayList<Level> levels = new ArrayList<>();

        levels.add(profile.levels.playerLevel);
        levels.add(profile.levels.skill_add_level);
        levels.add(profile.levels.skill_subtract_level);
        levels.add(profile.levels.skill_multiplication_level);


        RecyclerView recyclerView = findViewById(R.id.welcome_back_recycler_view);
        MyProfileAdapter myAdapter = new MyProfileAdapter(levels,getApplicationContext());

        LinearLayoutManager linearLayoutManager = new  LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(myAdapter);


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
