package com.example.newspaper;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardView extends RecyclerView.ViewHolder {

    private TextView articleCategory;
    private ImageView articleImage;
    private TextView articleTitle;
    private TextView articleDescription;

    private Article article;

    public CardView(@NonNull View itemView) {
        super(itemView);
        articleImage = itemView.findViewById(R.id.articleImage);
        articleTitle = itemView.findViewById(R.id.articleTitle);
        articleCategory = itemView.findViewById(R.id.articleCategory);
        articleDescription = itemView.findViewById(R.id.articleDescription);
    }

    public void setArticle(Article article){
        this.article = article;
    }

    public TextView getArticleCategory() {
        return articleCategory;
    }

    public ImageView getArticleImage() {
        return articleImage;
    }

    public TextView getArticleTitle() {
        return articleTitle;
    }

    public TextView getArticleDescription() {
        return articleDescription;
    }

}
