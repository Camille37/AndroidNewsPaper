package com.example.newspaper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread t = new Thread(new DownloadArticlesThreads(MainActivity.this));
        t.start();

        //recyclerView = findViewById(R.id.recyclerView);
        //adapter = new NewsAdapter();
        //recyclerView.setAdapter(adapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void finishDownloadUI(List<Article> data){
        recyclerView = findViewById(R.id.recyclerView);
        //ProgressBar pb = findViewById(R.id.recyclerView);
        //pb.setVisibility(View.INVISIBLE);
        NewsAdapter adapter = new NewsAdapter(this, data);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void startDownloadUI(){
        Log.i("dowload","Start dowloading");
        //ProgressBar pb = findViewById(R.id.main_pb_list_planets);
        //pb.setVisibility(View.VISIBLE);
    }

}