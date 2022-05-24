package tdtu.mobile_dev_assignment.lc_assignment.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.google.gson.JsonObject;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import tdtu.mobile_dev_assignment.lc_assignment.Models.Room;
import tdtu.mobile_dev_assignment.lc_assignment.Models.Transaction;
import tdtu.mobile_dev_assignment.lc_assignment.Models.User;
import tdtu.mobile_dev_assignment.lc_assignment.R;
import tdtu.mobile_dev_assignment.lc_assignment.Screen.ItemActivity;

public class HotelRoomAdapter extends RecyclerView.Adapter<HotelRoomAdapter.ViewHolder>  {

    private List<Room> listRoom;
    private Context mContext;
    private HotelRoomAdapter.onRoomListener onRoomListener;


    public HotelRoomAdapter(@NonNull Context context, @NonNull List<Room> listRoom, HotelRoomAdapter.onRoomListener onRoomListener) {
        this.mContext = context;
        this.listRoom = listRoom;
        this.onRoomListener = onRoomListener;
    }

    @NonNull
    @Override
    public HotelRoomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_vertical_card_item, parent, false);
        ViewHolder holder = new HotelRoomAdapter.ViewHolder(v, this.onRoomListener);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HotelRoomAdapter.ViewHolder holder, int position) {
            Room room = listRoom.get(position);

            holder.tvTitle.setText(room.getName());
            holder.tvPeople.setText(String.valueOf(room.getAdult()) + " people");
            holder.tvCheck.setText(room.getCheckIn() + " - " + room.getCheckOut());
            holder.tvPrice.setText(String.valueOf(room.getPrice()) + "/night");
            holder.tvRoomId.setText("Room ID: " + String.valueOf(room.getRoomId()));
            holder.tvDescription.setText(room.getDescription());
            Picasso.get().load(room.getImages().get(0)).fit().centerCrop().into(holder.shapeableImageView);

            boolean isExpanded = listRoom.get(position).isExpanded();


            holder.benefitLL.removeAllViewsInLayout();
            Typeface typeface = mContext.getResources().getFont(R.font.poppins_regular);
            for (String benefit : room.getBenefits()) {
                TextView textView = new TextView(mContext);
                textView.setText(Html.fromHtml("&#8226; " + benefit));
                textView.setTypeface(typeface);
                textView.setTextColor(mContext.getResources().getColor(R.color.text));
                holder.benefitLL.addView(textView);
            }

            holder.constraintLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        if (listRoom != null) {
            return listRoom.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitle, tvPeople, tvCheck, tvPrice, tvRoomId, tvDescription;
        ConstraintLayout constraintLayout;
        LinearLayout benefitLL;
        ShapeableImageView shapeableImageView;
        onRoomListener onRoomListener;
        Button readBtn, bookBtn;

        public ViewHolder(@NonNull View itemView, onRoomListener onRoomListener) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.roomTitle);
            tvPeople = itemView.findViewById(R.id.roomAdult);
            tvCheck = itemView.findViewById(R.id.roomCheck);
            tvPrice = itemView.findViewById(R.id.roomPrice);
            shapeableImageView = itemView.findViewById(R.id.roomThumbnail);
            tvRoomId = itemView.findViewById(R.id.roomId);
            constraintLayout = itemView.findViewById(R.id.expandedLayout);
            benefitLL = itemView.findViewById(R.id.benefitLL);
            tvDescription = itemView.findViewById(R.id.roomDescription);
            readBtn = itemView.findViewById(R.id.readBtn);
            bookBtn = itemView.findViewById(R.id.bookBtn);

            readBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Room room = listRoom.get(getAdapterPosition());
                    room.setExpanded(!room.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });

            bookBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Room room = listRoom.get(getAdapterPosition());
                    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference userRef = mDatabase.getReference().child("Users");
                    DatabaseReference transaction = mDatabase.getReference().child("Transactions");
                    SharedPreferences sp = mContext.getSharedPreferences("HotelPrefs", Context.MODE_PRIVATE);
                    String userString = sp.getString("User", "");
                    Gson gson = new Gson();
                    User user = gson.fromJson(userString, User.class);
                    Checkout.preload(mContext);
                    Checkout checkout = new Checkout();
                    checkout.setKeyID("rzp_test_WNqkoYCajhw1xr");

                    userRef.child(user.getUser_id()).child("bookedHotel").child(String.valueOf(room.getHotelId())).setValue(true, new DatabaseReference.CompletionListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            Toast.makeText(mContext, "Process Your Order", Toast.LENGTH_LONG).show();

                            checkout.setImage(R.drawable.logo);

                            JSONObject obj = new JSONObject();

                            try {
                                JSONObject options = new JSONObject();

                                options.put("name", "LC Booking");
                                options.put("description", "Reference No. #123456");
                                options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
                                options.put("theme.color", "#3399cc");
                                options.put("currency", "INR");
                                options.put("amount", String.valueOf(room.getPrice() * 100));//pass amount in currency subunits
                                options.put("prefill.email", "gaurav.kumar@example.com");
                                options.put("prefill.contact","9988776655");
                                JSONObject retryObj = new JSONObject();
                                retryObj.put("enabled", true);
                                retryObj.put("max_count", 4);
                                options.put("retry", retryObj);
//

                                checkout.open((Activity) mContext, options);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                            LocalDateTime now = LocalDateTime.now();
                            String transactionId = UUID.randomUUID().toString();
                            Transaction transactionDetail = new Transaction(transactionId, room.getPrice(), "Netbanking", "success", room.getRoomId(), dtf.format(now), 0);
                            transaction.child(transactionId).setValue(transactionDetail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(mContext, "Transaction successfully", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                }
            });



            this.onRoomListener = onRoomListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onRoomListener.onRoomClick(getAdapterPosition());
        }
    }

    public interface onRoomListener {
        void onRoomClick(int position);
    }


}
