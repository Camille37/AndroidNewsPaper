package com.example.newspaper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.example.newspaper.ui.login.LoginActivity;

public class ArticleDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // Retrieve the article
        Article article = (Article) getIntent().getSerializableExtra("article");

        ImageView detailImage = findViewById(R.id.detailImage);
        TextView detailTitle = findViewById(R.id.detailTitle);
        TextView detailDescription = findViewById(R.id.detailDescription);

        // Fulfill Views with data from the article
        if (article != null) {
            if (article.getTitle()!=null) {
                detailTitle.setText(Utils.insertHtmlText(article.getTitle()));
            }
            if (article.getAbstractText()!=null) {
                detailDescription.setText(Utils.insertHtmlText(article.getAbstractText()));
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
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_24px);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_article, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.action_back) {
            startActivity(new Intent(this, MainActivity.class));
            return true;
        } else if (itemId == R.id.action_login) {
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

