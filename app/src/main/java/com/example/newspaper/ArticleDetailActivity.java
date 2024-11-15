package com.example.newspaper;


import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

public class ArticleDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        // Retrieve the article
        NewsArticle article = (NewsArticle) getIntent().getSerializableExtra("article");

        ImageView detailImage = findViewById(R.id.detailImage);
        TextView detailTitle = findViewById(R.id.detailTitle);
        TextView detailDescription = findViewById(R.id.detailDescription);

        // Fulfill Views with data from the article
        if (article != null) {
            detailTitle.setText(article.getTitle());
            detailDescription.setText(article.getDescription());
            Glide.with(this).load(article.getImageUrl()).into(detailImage);
        }

        // Trouver la Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);  // Définir la Toolbar comme ActionBar

        // Activer la flèche de retour (Up button)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();  // Gérer le retour à l'activité précédente
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

