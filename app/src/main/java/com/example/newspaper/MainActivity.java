package com.example.newspaper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private List<Article> allArticles = new ArrayList<>(); // Liste globale pour tous les articles

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Télécharger les articles
        Thread t = new Thread(new DownloadArticlesThreads(MainActivity.this));
        t.start();

        // Configurer les boutons de filtre
        Button buttonAll = findViewById(R.id.buttonAll);
        Button buttonNational = findViewById(R.id.buttonNational);
        Button buttonEconomy = findViewById(R.id.buttonEconomy);
        Button buttonSports = findViewById(R.id.buttonSports);
        Button buttonTechnology = findViewById(R.id.buttonTechnology);

        buttonAll.setOnClickListener(v -> filterArticlesByCategory("All"));
        buttonNational.setOnClickListener(v -> filterArticlesByCategory("National"));
        buttonEconomy.setOnClickListener(v -> filterArticlesByCategory("Economy"));
        buttonSports.setOnClickListener(v -> filterArticlesByCategory("Sports"));
        buttonTechnology.setOnClickListener(v -> filterArticlesByCategory("Technology"));
    }

    public void finishDownloadUI(List<Article> data) {
        recyclerView = findViewById(R.id.recyclerView);

        allArticles.clear();
        allArticles.addAll(data);

        if (adapter == null) {
            adapter = new NewsAdapter(this, allArticles);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            adapter.updateData(allArticles,"ALL");
        }
    }

    public void startDownloadUI(){
        Log.i("dowload","Start dowloading");
        //ProgressBar pb = findViewById(R.id.main_pb_list_planets);
        //pb.setVisibility(View.VISIBLE);
    }

    private void filterArticlesByCategory(String category) {
        adapter.updateData(allArticles, category);
    }
}