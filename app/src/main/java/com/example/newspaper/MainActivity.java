package com.example.newspaper;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.recyclerView);
        adapter = new NewsAdapter(getNewsData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<NewsArticle> getNewsData() {
        // Fetch news data from your data source

        List<NewsArticle> articles = new ArrayList<>();
        articles.add(new NewsArticle("Headline 1", "Description 1", "https://example.com/image1.jpg"));
        articles.add(new NewsArticle("Headline 2", "Description 2", "https://example.com/image2.jpg"));
        articles.add(new NewsArticle("Headline 3", "Description 3", "https://example.com/image3.jpg"));

        return articles;
    }

}