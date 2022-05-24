package tdtu.mobile_dev_assignment.lc_assignment.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import tdtu.mobile_dev_assignment.lc_assignment.Models.Hotel;
import tdtu.mobile_dev_assignment.lc_assignment.R;

public class HotelHorizontalAdapter extends RecyclerView.Adapter<HotelHorizontalAdapter.ViewHolder> {

    private List<Hotel> listHotel;
    private Context mContext;
    private onHotelListener onHotelListener;

    public HotelHorizontalAdapter(@NonNull Context context, @NonNull List<Hotel> listTodo, onHotelListener onHotelListener) {
        this.mContext = context;
        this.listHotel = listTodo;
        this.onHotelListener = onHotelListener;
    }

    @NonNull
    @Override
    public HotelHorizontalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_horizontal_card_item, parent, false);
        ViewHolder holder = new HotelHorizontalAdapter.ViewHolder(v, this.onHotelListener);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Hotel hotel = listHotel.get(position);
        DecimalFormat df = new DecimalFormat("0.0");

        holder.tvTitle.setText(hotel.getName());

        if (TextUtils.isEmpty(hotel.getAddress())) {
            holder.tvAddress.setText("Selbyville, Delaware(DE), 19975");
        } else {
            holder.tvAddress.setText(hotel.getAddress());
        }

        if (TextUtils.isEmpty(String.valueOf(hotel.getRate()))) {
            double random = ThreadLocalRandom.current().nextDouble(0, 5);
            holder.tvRate.setText(String.valueOf(df.format(random)));
        } else {
            holder.tvRate.setText(Double.toString(hotel.getRate()));
        }

        List<String> imageUrls = hotel.getImages();

        Picasso.get().load(imageUrls.get(0)).into(holder.kenBurnsView);
    }

    @Override
    public int getItemCount() {
        if (listHotel != null) {
            return listHotel.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        CardView cardView;
        TextView tvTitle, tvAddress, tvRate;
        KenBurnsView kenBurnsView;
        onHotelListener onHotelListener;

        public ViewHolder(@NonNull View itemView, onHotelListener onHotelListener) {
            super(itemView);

            cardView = itemView.findViewById(R.id.hz_hotel_card);
            tvTitle = itemView.findViewById(R.id.hz_hotel_title);
            tvAddress = itemView.findViewById(R.id.hz_hotel_address);
            tvRate = itemView.findViewById(R.id.hz_hotel_rate);
            kenBurnsView = itemView.findViewById(R.id.hz_hotel_thumbnail);

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
