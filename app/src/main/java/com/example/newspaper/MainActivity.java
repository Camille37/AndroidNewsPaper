package com.example.newspaper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newspaper.ui.login.LoginActivity;

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

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);


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
        // Hide progressBar
        ProgressBar pb = findViewById(R.id.main_pb);
        pb.setVisibility(View.INVISIBLE);

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

        // Show progressBar
        ProgressBar pb = findViewById(R.id.main_pb);
        pb.setVisibility(View.VISIBLE);
    }

    private void filterArticlesByCategory(List<Article> allArticles, String category) {
        adapter.updateData(allArticles, category);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.action_login) {
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}