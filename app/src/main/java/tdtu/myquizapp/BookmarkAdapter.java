package tdtu.myquizapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.Viewholder> {
    private List<QuestionModel> list;

    public BookmarkAdapter(List<QuestionModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_item,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.setData(list.get(position).getQuestion(),list.get(position).getCorrectANS(),position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{
        private ImageButton delete;
        private TextView question,answer;
        private ImageView imagebookmark;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            imagebookmark = itemView.findViewById(R.id.imagebookmark);
            question = itemView.findViewById(R.id.question);
            answer=itemView.findViewById(R.id.answer);
            delete=itemView.findViewById(R.id.delete);
        }
        private void setData(String question,String answer,final int position){

            if(list.get(position).getUrl()==null){
                imagebookmark.setVisibility(View.GONE);

            }
            else {
                imagebookmark.setVisibility(View.VISIBLE);


                Glide.with(itemView.getContext())
                        .load(list.get(position).getUrl())
                        .into(imagebookmark);
            }
            this.question.setText(question);
            this.answer.setText(answer);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    list.remove(position);
                    notifyItemRemoved(position);
                }
            });
        }
    }

}
