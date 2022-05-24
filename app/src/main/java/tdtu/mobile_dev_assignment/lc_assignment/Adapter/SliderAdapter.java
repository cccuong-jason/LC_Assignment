package tdtu.mobile_dev_assignment.lc_assignment.Adapter;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import tdtu.mobile_dev_assignment.lc_assignment.R;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.ViewHolder> {

    List<String> images;

    public SliderAdapter(List<String> images) {
        this.images = images;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        Picasso.get().load(images.get(position)).into(viewHolder.imageView);
//        viewHolder.imageView.setImageResource(images[position]);

    }

    @Override
    public int getCount() {
        return images.size();
    }

    public class ViewHolder extends SliderViewAdapter.ViewHolder {

        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.sliderImage);
        }
    }
}
