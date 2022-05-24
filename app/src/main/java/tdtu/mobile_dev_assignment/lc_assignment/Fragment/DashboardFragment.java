package tdtu.mobile_dev_assignment.lc_assignment.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tdtu.mobile_dev_assignment.lc_assignment.Adapter.HotelHorizontalAdapter;
import tdtu.mobile_dev_assignment.lc_assignment.Adapter.HotelVerticalAdapter;
import tdtu.mobile_dev_assignment.lc_assignment.Models.Hotel;
import tdtu.mobile_dev_assignment.lc_assignment.Models.User;
import tdtu.mobile_dev_assignment.lc_assignment.R;
import tdtu.mobile_dev_assignment.lc_assignment.Screen.ItemActivity;

public class DashboardFragment extends Fragment implements HotelHorizontalAdapter.onHotelListener, HotelVerticalAdapter.onHotelListener{

    ViewPager2 recyclerView;
    RecyclerView recyclerView2;
    FragmentActivity context;
    View view;
    HotelHorizontalAdapter adapter;
    HotelVerticalAdapter adapter2;
    List<Hotel> listHotel;
    RecyclerView.RecycledViewPool sharedPool;
    FirebaseDatabase mDatabase;
    DatabaseReference hotelList;
    SharedPreferences sp;
    ShimmerFrameLayout shimmerFrameLayout, shimmerFrameLayoutHorizontal;
    ConstraintLayout constraintLayout;
    Gson gson = new Gson();
    CircleImageView imageView;
    LinearLayoutManager linearLayoutManager;
    ConstraintSet constraintSet = new ConstraintSet();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_dashboard, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerView = view.findViewById(R.id.horizontal_hotel_rv);
        recyclerView2 = view.findViewById(R.id.vertical_hotel_rv);

        sharedPool = new RecyclerView.RecycledViewPool();
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        shimmerFrameLayout = view.findViewById(R.id.shimmerLayout);
        shimmerFrameLayoutHorizontal = view.findViewById(R.id.shimmerLayoutHorizontal);
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayoutHorizontal.startShimmer();

        recyclerView2.setRecycledViewPool(sharedPool);
        recyclerView2.setLayoutManager(linearLayoutManager);
        recyclerView2.setItemViewCacheSize(20);
        recyclerView2.setHasFixedSize(true);

        mDatabase = FirebaseDatabase.getInstance();
        hotelList = mDatabase.getReference().child("Hotels");

        imageView = view.findViewById(R.id.profile_image);

        constraintLayout = view.findViewById(R.id.fg_dashboard_constraint);
        constraintSet.clone(constraintLayout);


        try {
            String userString = sp.getString("User", "");
            User user = gson.fromJson(userString, User.class);
            Log.i("User",  user.getName());
            Picasso.get().load(user.getAvatar()).fit().centerCrop().into(imageView);
        } catch (Exception e) {
            Log.i("ErrorDashboard", e.toString());
        }


        getHotelList();
    }

    public void getHotelList() {
        listHotel = new ArrayList<>();

        hotelList.limitToFirst(15).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Hotel hotelItem = dataSnapshot.getValue(Hotel.class);

                        listHotel.add(hotelItem);
                    }

                    adapter = new HotelHorizontalAdapter(context, listHotel, DashboardFragment.this);

                    constraintSet.connect(recyclerView.getId(), ConstraintSet.BOTTOM, R.id.labelNearest, ConstraintSet.TOP);
                    constraintSet.connect(R.id.labelNearest, ConstraintSet.TOP, R.id.horizontal_hotel_rv, ConstraintSet.BOTTOM);
                    constraintSet.applyTo(constraintLayout);
                    shimmerFrameLayoutHorizontal.stopShimmer();
                    shimmerFrameLayoutHorizontal.setVisibility(View.GONE);

                    recyclerView.setVisibility(View.VISIBLE);


                    recyclerView.setAdapter(adapter);
                    recyclerView.setClipToPadding(false);
                    recyclerView.setClipChildren(false);
                    recyclerView.setOffscreenPageLimit(3);
                    recyclerView.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

                    CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
                    compositePageTransformer.addTransformer(new MarginPageTransformer(40));
                    compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                        @Override
                        public void transformPage(@NonNull View page, float position) {
                            float r = 1 - Math.abs(position);
                            page.setScaleY(0.95f + r * 0.05f);
                        }
                    });

                    recyclerView.setPageTransformer(compositePageTransformer);

                    adapter2 = new HotelVerticalAdapter(context, listHotel, DashboardFragment.this);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    recyclerView2.setVisibility(View.VISIBLE);
                    recyclerView2.setNestedScrollingEnabled(false);
                    recyclerView2.setAdapter(adapter2);

                } catch (Exception e) {
                    Log.d("ErrorParse", e.toString());
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                sp = context.getSharedPreferences("HotelPrefs", Context.MODE_PRIVATE);
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sp = context.getSharedPreferences("HotelPrefs", Context.MODE_PRIVATE);
    }

    @Override
    public void onHotelClick(int position) {
        Hotel hotel = listHotel.get(position);
        Intent itemDetail = new Intent(getContext(), ItemActivity.class);
        itemDetail.putExtra("itemDetail", gson.toJson(hotel));
        startActivity(itemDetail);
    }
}