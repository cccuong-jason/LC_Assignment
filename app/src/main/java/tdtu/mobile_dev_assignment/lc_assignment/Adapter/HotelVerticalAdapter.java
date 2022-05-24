package tdtu.mobile_dev_assignment.lc_assignment.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class HotelVerticalAdapter extends RecyclerView.Adapter<HotelVerticalAdapter.ViewHolder> {

    private List<Hotel> listHotel;
    private Context mContext;
    private onHotelListener onHotelListener;

    public HotelVerticalAdapter(@NonNull Context context, @NonNull List<Hotel> listTodo, onHotelListener onHotelListener) {
        this.mContext = context;
        this.listHotel = listTodo;
        this.onHotelListener = onHotelListener;
    }

    @NonNull
    @Override
    public HotelVerticalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_vertical_card_item, parent, false);
        ViewHolder holder = new HotelVerticalAdapter.ViewHolder(v, this.onHotelListener);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Hotel hotel = listHotel.get(position);
        DecimalFormat df = new DecimalFormat("0.0");
        double randomVote = ThreadLocalRandom.current().nextDouble(0, 1000);

        holder.tvTitle.setText(hotel.getName());
        holder.tvPrice.setText(String.valueOf(hotel.getPrice()) + "/night");

        if (TextUtils.isEmpty(hotel.getAddress())) {
            holder.tvAddress.setText("Selbyville, Delaware(DE), 19975");
        } else {
            holder.tvAddress.setText(hotel.getAddress());
        }

        if (TextUtils.isEmpty(String.valueOf(hotel.getRate()))) {
            double randomRate = ThreadLocalRandom.current().nextDouble(0, 5);
            holder.tvRate.setText(String.valueOf(df.format(randomRate)) + "(" + df.format(randomVote) + ")");
        } else {
            holder.tvRate.setText(Double.toString(hotel.getRate()) + "(" + df.format(randomVote) + ")");
        }

        Picasso.get().load(hotel.getImages().get(0)).fit().centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (listHotel != null) {
            return listHotel.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        TextView tvTitle, tvAddress, tvRate, tvPrice;
        ImageView imageView;
        onHotelListener onHotelListener;

        public ViewHolder(@NonNull View itemView, onHotelListener onHotelListener) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.vcItemTitle);
            tvAddress = itemView.findViewById(R.id.vcAddress);
            tvRate = itemView.findViewById(R.id.vcRate);
            tvPrice = itemView.findViewById(R.id.vcPrice);
            imageView = itemView.findViewById(R.id.vcThumbNail);

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

