package com.example.newspaper;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ArticleDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        // Retrieve the article
        Article article = (Article) getIntent().getSerializableExtra("article");

        ImageView detailImage = findViewById(R.id.detailImage);
        TextView detailTitle = findViewById(R.id.detailTitle);
        TextView detailDescription = findViewById(R.id.detailDescription);

        // Fulfill Views with data from the article
        if (article != null) {
            if (article.getTitle()!=null) {
                detailTitle.setText(article.getTitle());
            }
            if (article.getAbstractText()!=null) {
                detailDescription.setText(article.getAbstractText());
            }

            // Convert image from string b64 to Bitmap
            if (article.getImage() !=null){
                String image_thumbnail = article.getImage().getImage();
                if(image_thumbnail!=null && !image_thumbnail.isEmpty()) {
                    Bitmap image = Utils.base64StringToImg(image_thumbnail);
                    if(image!=null) {
                        detailImage.setImageBitmap(image);
                    }
                }
            }
        }

        // Find toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Activate return home option
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();  // Goes back to previous activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

