package com.example.eventhub;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    private List<ClubsListModelClass> userList;

    public Adapter(List<ClubsListModelClass> userList, RecyclerViewInterface recyclerViewInterface){
        this.userList = userList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design,parent,false);
        return new ViewHolder(view, recyclerViewInterface);
//        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        // helps to bind data from activity to recycler view.
        String partOfSpeech = userList.get(position).getTextView1();
        String line = userList.get(position).getDivider();
        holder.setData(partOfSpeech, line);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textV1,textV2;
        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            textV1 = itemView.findViewById(R.id.textView1);
//            textV2 = itemView.findViewById(R.id.textView2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface !=null){
                        int posi = getAdapterPosition();
                        if(posi!=RecyclerView.NO_POSITION){
                            recyclerViewInterface.onClickForListItem(posi);
                        }
                    }
                }
            });

        }

        public void setData(String partOfSpeech, String line){
            textV1.setText(partOfSpeech);
//            textV2.setText(line);
        }


    }
}
