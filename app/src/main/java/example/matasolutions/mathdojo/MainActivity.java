package example.matasolutions.mathdojo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;

    com.google.android.material.button.MaterialButton login_button;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private com.rengwuxian.materialedittext.MaterialEditText email;
    private com.rengwuxian.materialedittext.MaterialEditText password;


    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.login_email_input);
        password = findViewById(R.id.login_password_input);

        register = findViewById(R.id.textView_register);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();

        login_button = findViewById(R.id.login);

        if(currentUser == null){

            login_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (view == login_button){
                        LoginUser();
                    }

                }
            });

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(),SignupActivity.class));
                }
            });

        }

        else{

            Toast.makeText(this, "Welcome back " + currentUser.getEmail() , Toast.LENGTH_LONG);
            finish();
            startActivity(new Intent(getApplicationContext(),WelcomeBackActivity.class));
        }

    }

    public void LoginUser() {
        String Email = email.getText().toString().trim();
        String Password = password.getText().toString().trim();
        mAuth.signInWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            currentUser = mAuth.getCurrentUser();
                            finish();
                            startActivity(new Intent(getApplicationContext(),
                                    WelcomeBackActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "couldn't login",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }





}
