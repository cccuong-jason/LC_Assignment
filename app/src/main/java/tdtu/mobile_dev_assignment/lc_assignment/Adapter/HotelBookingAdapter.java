package tdtu.mobile_dev_assignment.lc_assignment.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import tdtu.mobile_dev_assignment.lc_assignment.Models.Hotel;
import tdtu.mobile_dev_assignment.lc_assignment.Models.User;
import tdtu.mobile_dev_assignment.lc_assignment.R;

public class HotelBookingAdapter extends RecyclerView.Adapter<HotelBookingAdapter.ViewHolder> {

    private List<Hotel> listHotel;
    private Context mContext;
    private onHotelListener onHotelListener;

    public HotelBookingAdapter(@NonNull Context context, @NonNull List<Hotel> listTodo, onHotelListener onHotelListener) {
        this.mContext = context;
        this.listHotel = listTodo;
        this.onHotelListener = onHotelListener;
    }

    @NonNull
    @Override
    public HotelBookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item, parent, false);
        ViewHolder holder = new HotelBookingAdapter.ViewHolder(v, this.onHotelListener);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Hotel hotel = listHotel.get(position);

        holder.tvTitle.setText(hotel.getName());

        if (TextUtils.isEmpty(hotel.getAddress())) {
            holder.tvAddress.setText("Selbyville, Delaware(DE), 19975");
        } else {
            holder.tvAddress.setText(hotel.getAddress());
        }

//        holder.tvCheckIn.setText(hotel.getCheckIn() + " - " + hotel.getCheckOut());
        holder.tvId.setText(String.valueOf(hotel.getId()));

        Picasso.get().load(hotel.getImages().get(0)).fit().centerCrop().into(holder.shapeableImageView);

        try {
            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("ClickedDeleted", "Deleted");
                    new AlertDialog.Builder(mContext)
                            .setTitle(R.string.app_name)
                            .setMessage("Do you want to delete Booked ID: " + hotel.getId())
                            .setPositiveButton("Delete it", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    SharedPreferences sp = mContext.getSharedPreferences("HotelPrefs", Context.MODE_PRIVATE);
                                    Gson gson = new Gson();
                                    String userString = sp.getString("User", "");
                                    User user = gson.fromJson(userString, User.class);

                                    DatabaseReference userRef = database.getReference("Users").child(user.getUser_id()).child("bookedHotel").child(String.valueOf(hotel.getId()));
                                    userRef.removeValue(new DatabaseReference.CompletionListener() {
                                        @SuppressLint("NotifyDataSetChanged")
                                        @Override
                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                            Toast.makeText(mContext, "Delete data sucessfully", Toast.LENGTH_LONG).show();
                                            listHotel.remove(hotel);
                                            notifyDataSetChanged();
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        if (listHotel != null) {
            return listHotel.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        TextView tvTitle, tvAddress, tvCheckIn, tvId;
        onHotelListener onHotelListener;
        ShapeableImageView shapeableImageView;
        Button editBtn, deleteBtn;

        public ViewHolder(@NonNull View itemView, onHotelListener onHotelListener) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.bkItemTitle);
            tvAddress = itemView.findViewById(R.id.bkAddress);
            tvCheckIn = itemView.findViewById(R.id.bkCheckIn);
            shapeableImageView = itemView.findViewById(R.id.bkThumbNail);
            tvId = itemView.findViewById(R.id.bkId);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);

            this.onHotelListener = onHotelListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
                onHotelListener.onHotelClick(getAdapterPosition());
        }
    }

    public interface onHotelListener {
        void onHotelClick(int position);
    }
}


