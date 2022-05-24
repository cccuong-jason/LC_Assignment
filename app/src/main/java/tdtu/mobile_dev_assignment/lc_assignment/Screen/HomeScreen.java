package tdtu.mobile_dev_assignment.lc_assignment.Screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;
import tdtu.mobile_dev_assignment.lc_assignment.Fragment.AccountFragment;
import tdtu.mobile_dev_assignment.lc_assignment.Fragment.DashboardFragment;
import tdtu.mobile_dev_assignment.lc_assignment.Fragment.ExploreFragment;
import tdtu.mobile_dev_assignment.lc_assignment.Fragment.FavoriteFragment;
import tdtu.mobile_dev_assignment.lc_assignment.Models.User;
import tdtu.mobile_dev_assignment.lc_assignment.R;

public class HomeScreen extends AppCompatActivity {

    SmoothBottomBar smoothBottomBar;
    ConstraintLayout constraintLayout;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        init();

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new DashboardFragment()).commit();

        smoothBottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                switch (i) {
                    case 0:
                        replace(new DashboardFragment());
                        break;
                    case 1:
                        replace(new ExploreFragment());
                        break;
                    case 2:
                        replace(new FavoriteFragment());
                        break;
                    case 3:
                        replace(new AccountFragment());
                        break;
                }
                return true;
            }
        });
    }

    public void init() {
        smoothBottomBar = findViewById(R.id.bottomAppBar);
        constraintLayout = findViewById(R.id.homeScreen);
    }

    public void replace(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }

    private void exitFromApp() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }


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
}