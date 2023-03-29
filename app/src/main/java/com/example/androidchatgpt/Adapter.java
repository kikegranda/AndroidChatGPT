package com.example.androidchatgpt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ChatViewHolder> {

    ArrayList<Message> messagesArrayList;
    public Adapter(ArrayList<Message> messagesArrayList) {
        this.messagesArrayList = messagesArrayList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chatView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ai_message, null);
        ChatViewHolder chatViewHolder = new ChatViewHolder(chatView);
        return chatViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Message message = messagesArrayList.get(position);
        if(message.getSentBy().equals(Message.SENT_HUMAN)){
            holder.leftMssg.setVisibility(View.GONE);
            holder.rightMssg.setVisibility(View.VISIBLE);
            holder.rightMssgTextView.setText(message.getMessage());
        }else{
            holder.rightMssg.setVisibility(View.GONE);
            holder.leftMssg.setVisibility(View.VISIBLE);
            holder.leftMssgTextView.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftMssg, rightMssg;
        TextView leftMssgTextView, rightMssgTextView;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            leftMssg = itemView.findViewById(R.id.ai_mssg_view);
            rightMssg = itemView.findViewById(R.id.human_mssg_view);
            leftMssgTextView = itemView.findViewById(R.id.ai_mssg);
            rightMssgTextView = itemView.findViewById(R.id.human_mssg);
        }
    }
}
