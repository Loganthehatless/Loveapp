package com.dostal.loveapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private ArrayList<Messages> messagesArrayList;
    private CircleImageView circleImageView;


    public RecyclerAdapter(ArrayList<Messages>messagesArrayList)
    {
        this.messagesArrayList=messagesArrayList;

    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView message;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            message= itemView.findViewById(R.id.message);
            circleImageView=itemView.findViewById(R.id.chatimage);

            //((LinearLayoutManager)myRecyclerView.getLayoutManager()).setStackFromEnd(true);

        }
    }



    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        String message=messagesArrayList.get(position).getMessage();
        holder.message.setText(message);
    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }
}
