package tdtu.myquizapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class ListTrafficSignAdapter extends RecyclerView.Adapter<ListTrafficSignAdapter.viewholder> {
    private List<TrafficSignListModel> ListTrafficSignModel;

    public ListTrafficSignAdapter(List<TrafficSignListModel> ListTrafficSignModel) {
        this.ListTrafficSignModel = ListTrafficSignModel;
    }
    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_traffic_sign_item,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        holder.setData(ListTrafficSignModel.get(position).getUrl(),ListTrafficSignModel.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return ListTrafficSignModel.size();
    }

    class viewholder extends  RecyclerView.ViewHolder{

        private ImageView imagetrafficsign;
        private TextView description;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            imagetrafficsign=itemView.findViewById(R.id.imagetrafficsign);
            description=itemView.findViewById(R.id.description);
        }
        private void setData(final String url,final String description){
            this.description.setText(description);
            Glide.with(itemView.getContext())
                    .load(url)
                    .into(imagetrafficsign);

        }
    }

}
