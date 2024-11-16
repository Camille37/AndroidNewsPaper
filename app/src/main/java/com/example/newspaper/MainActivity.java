package com.example.newspaper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private List<Article> allArticles = new ArrayList<>();

    private String[] categories = {"All", "National", "Economy","Sports", "Technology"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Download articles
        Thread t = new Thread(new DownloadArticlesThreads(MainActivity.this));
        t.start();

        // Create dropdown button to select categories
        Spinner spinner = findViewById(R.id.category_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,categories);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void finishDownloadUI(List<Article> data) {
        recyclerView = findViewById(R.id.recyclerView);

        this.allArticles.clear();
        this.allArticles.addAll(data);

        adapter = new NewsAdapter(this, allArticles);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Link filtering with dropdown button to select categories
        Spinner spinner = findViewById(R.id.category_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("category",categories[position]);
                filterArticlesByCategory(allArticles, categories[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filterArticlesByCategory(allArticles,"ALL");
            }
        });
    }

    public void startDownloadUI(){
        Log.i("download","Start downloading");
        //ProgressBar pb = findViewById(R.id.main_pb_list_planets);
        //pb.setVisibility(View.VISIBLE);
    }

    private void filterArticlesByCategory(List<Article> allArticles, String category) {
        adapter.updateData(allArticles, category);
    }
}