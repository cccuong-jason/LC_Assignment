package tdtu.mobile_dev_assignment.lc_assignment.Screen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.Objects;

import tdtu.mobile_dev_assignment.lc_assignment.Models.User;
import tdtu.mobile_dev_assignment.lc_assignment.R;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout usernameLayout, passwordLayout;
    TextInputEditText usernameEditText, passwordEditText;
    Button forgetButton, registerButton, loginButton;
    SharedPreferences sp;
    DatabaseReference userInformation;
    FirebaseDatabase mDatabase;
    Gson gson;
    FirebaseAuth mAuth;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen_layout);

        init();

    }

    public void init() {
        usernameLayout = findViewById(R.id.username);
        passwordLayout = findViewById(R.id.password);
        usernameEditText = findViewById(R.id.username_et);
        passwordEditText = findViewById(R.id.password_et);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        userInformation = mDatabase.getReference().child("Users");
        gson = new Gson();
        sp = getApplicationContext().getSharedPreferences("HotelPrefs", Context.MODE_PRIVATE);
        forgetButton = findViewById(R.id.forgetBtn);
        registerButton = findViewById(R.id.registerBtn);
        loginButton = findViewById(R.id.loginBtn);

        forgetButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
    }

    private void exitFromApp() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginBtn:
                login();
                break;
            case R.id.forgetBtn:
                break;
            case R.id.registerBtn:
                Intent register = new Intent(LoginScreen.this, RegisterScreen.class);
                startActivity(register);
                Animatoo.animateSlideLeft(LoginScreen.this);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            exitFromApp();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public void getUserInformation(String userID) {
        userInformation.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {

                    Log.i("Snapshot", snapshot.toString());
                    User user = snapshot.getValue(User.class);

                    Log.i("UserHere", user.getName());
                    @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sp.edit();
                    editor.putString("User", gson.toJson(user));
                    editor.apply();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void login() {
//        String email = usernameEditText.getText().toString();
//        String password = passwordEditText.getText().toString();

        String email = "bi4n123456@gmail.com";
        String password = "123456";

        if (TextUtils.isEmpty(email)) {
            usernameLayout.setError("Email can't be empty");
            usernameEditText.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            passwordLayout.setError("Password can't be empty");
            passwordEditText.requestFocus();
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent homeScreen = new Intent(LoginScreen.this, HomeScreen.class);
                                assert user != null;
                                getUserInformation(user.getUid());
                                startActivity(homeScreen);
                                Animatoo.animateFade(LoginScreen.this);
                            } else {
                                Toast.makeText(LoginScreen.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}