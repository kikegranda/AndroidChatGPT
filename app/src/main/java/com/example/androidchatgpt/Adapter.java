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
        View chatView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message, parent, false);
        return new ChatViewHolder(chatView);
    }

    /**
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Message message = messagesArrayList.get(position);
        if (message.getSentBy().equals(Message.SENT_HUMAN)) {
            holder.ai_message_Layout.setVisibility(View.GONE);
            holder.human_message_Layout.setVisibility(View.VISIBLE);
            holder.human_message_TextView.setText(message.getMessage());
        } else {
            holder.human_message_Layout.setVisibility(View.GONE);
            holder.ai_message_Layout.setVisibility(View.VISIBLE);
            holder.ai_message_TextView.setText(message.getMessage());
        }
    }

    /**
     * @return Total number of messages (Size of array list)
     */
    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ai_message_Layout, human_message_Layout;
        TextView ai_message_TextView, human_message_TextView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            ai_message_Layout = itemView.findViewById(R.id.ai_mssg_layout);
            human_message_Layout = itemView.findViewById(R.id.human_mssg_layout);
            ai_message_TextView = itemView.findViewById(R.id.ai_mssg_view);
            human_message_TextView = itemView.findViewById(R.id.human_mssg_view);
        }
    }
}
