package tdtu.mobile_dev_assignment.lc_assignment.Screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import tdtu.mobile_dev_assignment.lc_assignment.Models.User;
import tdtu.mobile_dev_assignment.lc_assignment.R;

public class RegisterScreen extends AppCompatActivity {

    TextInputLayout usernameLayout, passwordLayout, fullnameLayout;
    TextInputEditText usernameEditText, passwordEditText, fullnameEditText;
    Button registerButton;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

        mAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSlideRight(RegisterScreen.this);
    }

    public void init() {
        usernameLayout = findViewById(R.id.rg_username);
        passwordLayout = findViewById(R.id.rg_password);
        fullnameLayout = findViewById(R.id.rg_fullname);
        fullnameEditText = findViewById(R.id.rg_fullname_et);
        usernameEditText = findViewById(R.id.rg_username_et);
        passwordEditText = findViewById(R.id.rg_password_et);
        registerButton = findViewById(R.id.registerBtn_rg);
        mDatabase = FirebaseDatabase.getInstance();
        ref = mDatabase.getReference().child("Users");
    }

    public void register() {
        String email = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String fullName = fullnameEditText.getText().toString();

        if (TextUtils.isEmpty(email)) {
            usernameLayout.setError("Email can't be empty");
            usernameEditText.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            passwordLayout.setError("Password can't be empty");
            passwordEditText.requestFocus();
        }
        else if (TextUtils.isEmpty(fullName)) {
            fullnameLayout.setError("Full Name can't be empty");
            fullnameEditText.requestFocus();
        } else {
            try {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Successfully Register", "createUserWithEmail:success");
                            FirebaseUser user = task.getResult().getUser();

                            assert user != null;
                            User currentUser = new User(fullName, email, user.getUid());
                            ref.child(user.getUid()).setValue(currentUser);

                            Toast.makeText(RegisterScreen.this, "Register Successfully.",
                                    Toast.LENGTH_SHORT).show();
                            Intent login = new Intent(RegisterScreen.this, LoginScreen.class);
                            startActivity(login);
                            Animatoo.animateSlideRight(RegisterScreen.this);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Failed Register", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterScreen.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e) {
                Log.e("Error", e.toString());
            }

        }
    }
}