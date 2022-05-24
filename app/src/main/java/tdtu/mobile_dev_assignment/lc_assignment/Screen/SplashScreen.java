package tdtu.mobile_dev_assignment.lc_assignment.Screen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Objects;

import tdtu.mobile_dev_assignment.lc_assignment.R;

public class SplashScreen extends AppCompatActivity {

    Animation topAnim, botAnim, blinkAnim;
    LottieAnimationView image;
    TextView title, slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
//        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar
        setContentView(R.layout.splash_screen_layout);

        init();

        image.playAnimation();
        title.setAnimation(botAnim);
        slogan.setAnimation(botAnim);

        int SPLASH_SCREEN = 4000;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent dashboard = new Intent(SplashScreen.this, LoginScreen.class);
                Pair[] pairs = new Pair[2];

                pairs[0] = new Pair<View, String>(image, "logo_image");
                pairs[1] = new Pair<View, String>(title, "logo_text");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this, pairs);
                startActivity(dashboard, options.toBundle());
            }
        }, SPLASH_SCREEN);
    }

    public void init() {
        topAnim = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.top_animation);
        botAnim = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.bottom_animation);
        blinkAnim = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.blink_animation);
        image = findViewById(R.id.ivSplashImage);
        title = findViewById(R.id.tvSplashTitle);
        slogan = findViewById(R.id.tvSplashSlogan);
    }

}