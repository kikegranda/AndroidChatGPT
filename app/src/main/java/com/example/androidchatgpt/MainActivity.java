package com.example.androidchatgpt;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText message_EditText;
    ImageButton sendButton;
    ArrayList<Message> messageArrayList;
    Adapter adapter;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageArrayList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerview);
        message_EditText = findViewById(R.id.message_edittext);
        sendButton = findViewById(R.id.enviar);

        adapter = new Adapter(messageArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        sendButton.setOnClickListener((view) -> {
            String message = message_EditText.getText().toString().trim();
            //Toast.makeText(this,message,Toast.LENGTH_LONG).show();
            addMessageToChat(message,Message.SENT_HUMAN);
            message_EditText.setText("");
            callAPI(message);
        });
    }

    void addMessageToChat(String message, String sender){
        runOnUiThread(() -> {
            messageArrayList.add(new Message(message, sender));
            adapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(adapter.getItemCount());
        });
    }

    void callAPI(String message) {
        messageArrayList.add(new Message(getString(R.string.escribiendo), Message.SENT_GPT));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("model", "text-davinci-003");
            jsonObject.put("prompt",message);
            jsonObject.put("max_tokens", 4000);
            jsonObject.put("temperature",0);
        } catch (JSONException e) {
           e.printStackTrace();
        }
        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .header("Authorization","Bearer "+getString(R.string.apiKey))
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                messageArrayList.remove(messageArrayList.size()-1);
                addMessageToChat(getString(R.string.error)+ e, Message.SENT_GPT);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    JSONObject jsonObjectResponse;
                    try {
                        jsonObjectResponse = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObjectResponse.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0).getString("text");
                        messageArrayList.remove(messageArrayList.size()-1);
                        addMessageToChat(result.trim(), Message.SENT_GPT);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    messageArrayList.remove(messageArrayList.size()-1);
                    addMessageToChat(getString(R.string.error)+response.body().toString(), Message.SENT_GPT);
                }
            }
        });
    }
}