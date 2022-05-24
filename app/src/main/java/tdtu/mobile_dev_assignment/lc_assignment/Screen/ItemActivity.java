package tdtu.mobile_dev_assignment.lc_assignment.Screen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.razorpay.PaymentResultListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import tdtu.mobile_dev_assignment.lc_assignment.Adapter.HotelRoomAdapter;
import tdtu.mobile_dev_assignment.lc_assignment.Adapter.SliderAdapter;
import tdtu.mobile_dev_assignment.lc_assignment.Models.Hotel;
import tdtu.mobile_dev_assignment.lc_assignment.Models.Room;
import tdtu.mobile_dev_assignment.lc_assignment.Models.Transaction;
import tdtu.mobile_dev_assignment.lc_assignment.Models.User;
import tdtu.mobile_dev_assignment.lc_assignment.R;

public class ItemActivity extends AppCompatActivity implements View.OnClickListener, HotelRoomAdapter.onRoomListener, PaymentResultListener {

    TextView tvItemTitle, tvAddress, tvRate, tvPrice, tvDescription, tvEmail, tvCategory, tvCountry;
    Toolbar toolbar;
    Hotel hotel;
    AppBarLayout appBarLayout;
    ImageView imageView;
    SliderView sliderView;
    FirebaseDatabase mDatabase;
    DatabaseReference ref;
    CollapsingToolbarLayout collapsingToolbarLayout;
    HotelRoomAdapter hotelRoomAdapter;
    RecyclerView roomRecyclerView;
    LinearLayoutManager linearLayoutManager;
    List<Room> listRoom = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        String extras = getIntent().getStringExtra("itemDetail");
        Gson gson = new Gson();
        hotel = gson.fromJson(extras, Hotel.class);

        init();

        List<String> imageUrls = hotel.getImages();

        Picasso.get().load(imageUrls.get(0)).into(imageView);

        sliderView = findViewById(R.id.imageGallery);
        SliderAdapter sliderAdapter = new SliderAdapter(imageUrls);

        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(hotel.getName());
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });

        tvItemTitle.setText(hotel.getName());
        tvDescription.setText(hotel.getContent());
        if (TextUtils.isEmpty(hotel.getAddress())) {
            tvAddress.setText("Selbyville, Delaware(DE), 19975");
        } else {
            tvAddress.setText(hotel.getAddress());
        }
        tvCountry.setText(hotel.getCountry());
        if (TextUtils.isEmpty(hotel.getEmail())) {
            tvEmail.setText("cccuong@newai.com.vn");
        } else {
            tvEmail.setText(hotel.getEmail());
        }
        tvCategory.setText(hotel.getCategories() + " hotel");
        tvPrice.setText(String.valueOf(hotel.getPrice()) + "/night");

        DecimalFormat df = new DecimalFormat("0.0");
        double randomVote = ThreadLocalRandom.current().nextDouble(0, 1000);

        if (TextUtils.isEmpty(String.valueOf(hotel.getRate()))) {
            double randomRate = ThreadLocalRandom.current().nextDouble(0, 5);
            tvRate.setText(String.valueOf(df.format(randomRate)) + "(" + df.format(randomVote) + ")");
        } else {
            tvRate.setText(Double.toString(hotel.getRate()) + "(" + df.format(randomVote) + ")");
        }

        listRoom.addAll(hotel.getRoom());
        hotelRoomAdapter = new HotelRoomAdapter(ItemActivity.this, listRoom, ItemActivity.this);
        roomRecyclerView.setNestedScrollingEnabled(false);
        roomRecyclerView.setAdapter(hotelRoomAdapter);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemBookmark:
                Toast.makeText(ItemActivity.this, "Bookmark clicked", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        Animatoo.animateSlideLeft(ItemActivity.this); //fire the slide left animation
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_bar_menu, menu);
        return true;
    }

    public void init() {
        tvItemTitle = findViewById(R.id.tvItemTitle);
        tvAddress = findViewById(R.id.tvAddress);
        tvRate = findViewById(R.id.tvRate);
        tvPrice = findViewById(R.id.tvPrice);
        tvEmail = findViewById(R.id.tvEmail);
        tvCategory = findViewById(R.id.tvCategory);
        tvCountry = findViewById(R.id.tvCountry);
        tvPrice = findViewById(R.id.tvPrice);
        tvDescription = findViewById(R.id.tvDescription);
        imageView = findViewById(R.id.ivThumbnail);
        toolbar =  findViewById(R.id.toolBar);
        appBarLayout = findViewById(R.id.appBarLayout);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        linearLayoutManager = new LinearLayoutManager(ItemActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        roomRecyclerView = findViewById(R.id.roomRv);
        roomRecyclerView.setLayoutManager(linearLayoutManager);
        roomRecyclerView.setItemViewCacheSize(3);
        roomRecyclerView.setHasFixedSize(true);

        mDatabase = FirebaseDatabase.getInstance();
        ref = mDatabase.getReference().child("Users");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bookBtn:

        }
    }

    @Override
    public void onRoomClick(int position) {
        Toast.makeText(ItemActivity.this, "Clicked On", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(ItemActivity.this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(ItemActivity.this, s, Toast.LENGTH_LONG).show();
    }
}