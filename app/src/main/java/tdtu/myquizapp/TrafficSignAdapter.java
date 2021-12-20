package tdtu.myquizapp;

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

public class TrafficSignAdapter extends RecyclerView.Adapter<TrafficSignAdapter.viewholder> {
    private List<TrafficSignModel> TrafficSignModelList;

    public TrafficSignAdapter(List<TrafficSignModel> TrafficSignModelList) {
        this.TrafficSignModelList = TrafficSignModelList;
    }
    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.traffic_sign_item,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        holder.setData(TrafficSignModelList.get(position).getName(),TrafficSignModelList.get(position).getSets());
    }

    @Override
    public int getItemCount() {
        return TrafficSignModelList.size();
    }

    class viewholder extends  RecyclerView.ViewHolder{

        private TextView title;
        public viewholder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.title);
        }
        private void setData(final String title,final int sets){

            this.title.setText(title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent listtrafficIntent = new Intent(itemView.getContext(),ListTrafficSignActivity.class);
                    listtrafficIntent.putExtra("title",title);
                    listtrafficIntent.putExtra("sets",sets);

                    itemView.getContext().startActivity(listtrafficIntent);
                }
            });
        }
    }

}
