/*
This adapter will be responsible for:

Populating the RecyclerView with NewsArticle objects
Binding the data to the CardView layout
Handling item clicks (if desired)
 */

package com.example.newspaper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<Article> articles;
    private MainActivity ma;

    public NewsAdapter(MainActivity ma, List<Article> articles) {
        this.ma = ma;
        this.articles = articles;

        // Chronologic sort of the articles before display
        Collections.sort(articles, new Comparator<Article>() {
            @Override
            public int compare(Article art1, Article art2) {
                return -(art1.getPublicationDate().compareTo(art2.getPublicationDate())); // Compare les dates
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article article = articles.get(position);

        // Bind the data to the UI components
        if(article.getTitle()!=null) {
            holder.articleTitle.setText(Utils.insertHtmlText(article.getTitle()));
        }

        if(article.getCategory()!=null) {
            holder.articleCategory.setText(article.getCategory());
        }

        if(article.getAbstractText()!=null) {
            holder.articleDescription.setText(Utils.insertHtmlText(article.getAbstractText()));
        }

        if(article.getPublicationDate()!=null){
            Date date = article.getPublicationDate();
            holder.articleDate.setText(Utils.dateToString(date));
        }

        // Convert image from string b64 to Bitmap
        if (article.getImage() !=null){
            String image_thumbnail = article.getImage().getImage();
            if(image_thumbnail!=null && !image_thumbnail.isEmpty()) {
                Bitmap image = Utils.base64StringToImg(image_thumbnail);
                if(image!=null) {
                    holder.articleImage.setImageBitmap(image);
                }
            }
        }

        // Handle the click event
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ArticleDetailActivity.class);
                intent.putExtra("article", article);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView articleCategory;
        ImageView articleImage;
        TextView articleTitle;
        TextView articleDescription;

        TextView articleDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            articleImage = itemView.findViewById(R.id.articleImage);
            articleTitle = itemView.findViewById(R.id.articleTitle);
            articleCategory = itemView.findViewById(R.id.articleCategory);
            articleDescription = itemView.findViewById(R.id.articleDescription);
            articleDate = itemView.findViewById(R.id.articleDate);
        }
    }

    public void updateData(List<Article> articles, String category) {
        List<Article> filteredArticles = new ArrayList<>();
        if (category.equals("All")) {
            filteredArticles.addAll(articles);
        } else {
            for (Article article : articles) {
                if (article.getCategory().equalsIgnoreCase(category)) {
                    filteredArticles.add(article);
                }
            }
        }
        this.articles = filteredArticles; // Update internal list
        notifyDataSetChanged(); // Notify adapter data changed
    }
}
