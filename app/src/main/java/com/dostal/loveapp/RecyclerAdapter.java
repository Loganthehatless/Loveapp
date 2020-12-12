package com.dostal.loveapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private ArrayList<Messages> messagesArrayList;
    private CircleImageView ImageView;



    public RecyclerAdapter(ArrayList<Messages> messagesArrayList) {
        this.messagesArrayList = messagesArrayList;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView message;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            ImageView = itemView.findViewById(R.id.chatimage);

            //((LinearLayoutManager)myRecyclerView.getLayoutManager()).setStackFromEnd(true);

        }
    }


    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        holder.setIsRecyclable(false);//verhindert das Recyclen lol

        String message = messagesArrayList.get(position).getMessage();
        String userid = messagesArrayList.get(position).getSentuserId();
        if (userid.equals("jTHOsXTcn23zs6W7pEKZ")){
            ImageView.setImageResource(R.drawable.lukas);
            holder.message.setText(message);
        }
        if (userid.equals("5Ch9GWqsiknebkY7SNNf")){
            ImageView.setImageResource(R.drawable.naruto);
            holder.message.setText(message);
        }
        if (userid.equals("Fi4jdMOGaszUNEdqW3nQ")){
            ImageView.setImageResource(R.drawable.maik);
            holder.message.setText(message);
        }
        if (userid.equals("4Qe1Fh8n2va6YPoxThRt")){
            ImageView.setImageResource(R.drawable.sandra);
            holder.message.setText(message);
        }
        if (userid.equals("C0DgRDU9m8tUKbMww3YV")){
            ImageView.setImageResource(R.drawable.dennis);
            holder.message.setText(message);
        }


    }


    @Override
    public int getItemCount() {

        return messagesArrayList.size();
    }
}
