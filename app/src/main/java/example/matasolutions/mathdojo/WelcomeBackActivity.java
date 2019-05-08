package example.matasolutions.mathdojo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeBackActivity extends AppCompatActivity {


    TextView welcome_back_user;
    Button proceed;
    Button logout;
    Button high_scores;

    FirebaseUser currentUser;

    FirebaseAuth mAuth;

    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_back);

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();


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




    }
}
