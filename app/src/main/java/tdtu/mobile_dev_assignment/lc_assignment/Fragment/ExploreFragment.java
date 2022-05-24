package tdtu.mobile_dev_assignment.lc_assignment.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.Slider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import tdtu.mobile_dev_assignment.lc_assignment.Adapter.HotelHorizontalAdapter;
import tdtu.mobile_dev_assignment.lc_assignment.Adapter.HotelVerticalAdapter;
import tdtu.mobile_dev_assignment.lc_assignment.Models.Hotel;
import tdtu.mobile_dev_assignment.lc_assignment.Models.User;
import tdtu.mobile_dev_assignment.lc_assignment.R;
import tdtu.mobile_dev_assignment.lc_assignment.Screen.ItemActivity;

public class ExploreFragment extends Fragment implements HotelVerticalAdapter.onHotelListener {
    RecyclerView recyclerView2;
    FragmentActivity context;
    View view;
    HotelVerticalAdapter adapter2;
    List<Hotel> listHotel;
    RecyclerView.RecycledViewPool sharedPool;
    FirebaseDatabase mDatabase;
    DatabaseReference hotelList;
    Gson gson = new Gson();
    CircleImageView imageView;
    LinearLayoutManager linearLayoutManager;
    Button filterBtn;
    ShimmerFrameLayout shimmerFrameLayout;
    TextView hotelLabel;
    AutoCompleteTextView sortBy;
    ChipGroup chipGroup;
    boolean alreadyLoadedAllData = false;


    private final String[] countries = { "Vietnam", "French", "Dubai", "Thailand", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy", "Argentina", "America", "Singapore" };
    private final String[] facilities = { "Spa", "Semi open & outdoor restaurant", "Poolside bar", "Car parking", "Swimming pool/ Jacuzzi", "Public computer", "Disable rooms & Interconnecting rooms", "24 Hour security" };
    private final String[] categories = {"Business", "Airport", "Suite", "Extended", "Resort", "Homestays", "Casino", "Pension", "City", "Motels"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_explore, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        init();

        getHotelList(2, 0.0);

        onFilterClicked();

    }

    public void loadMore() {
        Toast.makeText(getContext(), "Load More", Toast.LENGTH_LONG).show();
        alreadyLoadedAllData = true;
    }

    public void init() {
        recyclerView2 = view.findViewById(R.id.vertical_hotel_rv);
        sharedPool = new RecyclerView.RecycledViewPool();
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        shimmerFrameLayout = view.findViewById(R.id.shimmerLayout);
        shimmerFrameLayout.startShimmer();

        recyclerView2.setRecycledViewPool(sharedPool);
        recyclerView2.setLayoutManager(linearLayoutManager);
        recyclerView2.setItemViewCacheSize(20);
        recyclerView2.setHasFixedSize(true);

        recyclerView2.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.i("Scrolling New Stated", "Scrolling Listen New State: ");

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.i("Scrolling", "Scrolling Listen: " + dx);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                Log.i("ScrollingLLM", String.valueOf(linearLayoutManager.findLastCompletelyVisibleItemPosition()));
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() >= listHotel.size()) {
                    loadMore();
                }
            }
        });

        filterBtn = view.findViewById(R.id.filterBtn);
        hotelLabel = view.findViewById(R.id.hotelLabel);

        mDatabase = FirebaseDatabase.getInstance();
        hotelList = mDatabase.getReference().child("Hotels");

        imageView = view.findViewById(R.id.profile_image);

    }

    public void onFilterClicked() {
        try {
            filterBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                            getContext(), R.style.BottomSheetDialogTheme
                    );
                    View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.layout_bottom_sheet, view.findViewById(R.id.bottomSheetContainer));
                    bottomSheetDialog.setContentView(bottomSheetView);
                    bottomSheetDialog.show();

                    sortBy = bottomSheetDialog.findViewById(R.id.sortByAC);
                    ArrayAdapter<String> adapterCountries = new ArrayAdapter<String>(getContext(),R.layout.dropdown_list_item,countries);
                    sortBy.setThreshold(1);
                    sortBy.setAdapter(adapterCountries);

                    chipGroup = bottomSheetDialog.findViewById(R.id.chipGroup);

                    for (String category : categories) {

                        Chip tagChip = (Chip) getLayoutInflater().inflate(R.layout.chip_layout, chipGroup, false);
                        tagChip.setText(category);
                        chipGroup.addView(tagChip);
                    }

                    try {

                        bottomSheetDialog.findViewById(R.id.applyFilterBtn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                RatingBar ratingBar = bottomSheetDialog.findViewById(R.id.rateStart);
                                assert ratingBar != null;
                                getHotelList(1, Math.round(ratingBar.getRating()));
                                bottomSheetDialog.dismiss();
                            }
                        });
                    } catch (Exception e) {
                        Log.e("ErrorFilter", e.toString());
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getHotelList(int filterCase, double value) {

        listHotel = new ArrayList<>();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                listHotel.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Hotel hotelItem = dataSnapshot.getValue(Hotel.class);
                    listHotel.add(hotelItem);
                }
                hotelLabel.setText("Hotels (" + String.valueOf(listHotel.size()) + ")");

               if (filterCase == 2) {
                    adapter2 = new HotelVerticalAdapter(context, listHotel, ExploreFragment.this);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    recyclerView2.setVisibility(View.VISIBLE);
                    recyclerView2.setNestedScrollingEnabled(false);
                    recyclerView2.setAdapter(adapter2);
                } else {
                   adapter2 = new HotelVerticalAdapter(context, listHotel, ExploreFragment.this);
                   recyclerView2.setNestedScrollingEnabled(false);
                   recyclerView2.setAdapter(adapter2);
                   adapter2.notifyDataSetChanged();
               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("ErrorFirebase", "loadPost:onCancelled", databaseError.toException());
            }
        };

        if (filterCase == 1) {
            hotelList.orderByChild("rate").startAt(value).addListenerForSingleValueEvent(postListener);
        } else {
            hotelList.addListenerForSingleValueEvent(postListener);
        }
    }

    @Override
    public void onHotelClick(int position) {
        Hotel hotel = listHotel.get(position);
        Intent itemDetail = new Intent(getContext(), ItemActivity.class);
        itemDetail.putExtra("itemDetail", gson.toJson(hotel));
        startActivity(itemDetail);
    }
}