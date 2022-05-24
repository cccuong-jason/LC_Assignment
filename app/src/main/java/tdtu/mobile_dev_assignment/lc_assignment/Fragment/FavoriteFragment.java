package tdtu.mobile_dev_assignment.lc_assignment.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import tdtu.mobile_dev_assignment.lc_assignment.Adapter.HotelBookingAdapter;
import tdtu.mobile_dev_assignment.lc_assignment.Adapter.HotelHorizontalAdapter;
import tdtu.mobile_dev_assignment.lc_assignment.Adapter.HotelVerticalAdapter;
import tdtu.mobile_dev_assignment.lc_assignment.Models.Hotel;
import tdtu.mobile_dev_assignment.lc_assignment.Models.User;
import tdtu.mobile_dev_assignment.lc_assignment.R;
import tdtu.mobile_dev_assignment.lc_assignment.Screen.ItemActivity;

public class FavoriteFragment extends Fragment implements HotelBookingAdapter.onHotelListener {

    View view;
    RecyclerView recyclerView;
    HotelBookingAdapter adapter;
    FirebaseDatabase mDatabase;
    DatabaseReference ref;
    SharedPreferences sp;
    List<Hotel> listHotel;
    FragmentActivity context;
    RecyclerView.RecycledViewPool sharedPool;
    Gson gson = new Gson();
    LinearLayoutManager linearLayoutManager;
    Button deleteBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorite, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();

        getBookmarkedHotel();
    }

    public void init() {
        deleteBtn = view.findViewById(R.id.editBtn);
        recyclerView = view.findViewById(R.id.booking_hotel_rv);
        sharedPool = new RecyclerView.RecycledViewPool();
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setRecycledViewPool(sharedPool);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setHasFixedSize(true);

        mDatabase = FirebaseDatabase.getInstance();
        ref = mDatabase.getReference();

        listHotel = new ArrayList<>();

    }

    public void getBookmarkedHotel() {

        String userString = sp.getString("User", "");
        User user = gson.fromJson(userString, User.class);

        for (String key : user.getbookedHotel().keySet()) {

            Query query = ref.child("Hotels").orderByChild("id").equalTo(Integer.parseInt(key));
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Hotel hotel = dataSnapshot.getValue(Hotel.class);
                        assert hotel != null;
                        listHotel.add(hotel);
                    }

                    adapter = new HotelBookingAdapter(requireActivity(), listHotel, FavoriteFragment.this);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sp = context.getSharedPreferences("HotelPrefs", Context.MODE_PRIVATE);
    }

    @Override
    public void onHotelClick(int position) {
    }
}
