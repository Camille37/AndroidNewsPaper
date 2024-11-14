/*
Class to represent each article
 */

package com.example.newspaper;

import java.io.Serializable;

public class NewsArticle implements Serializable {
    private String title;
    private String description;
    private String imageUrl;

    public NewsArticle(String title, String description,String Image){
        this.title = title;
        this.description = description;
        this.imageUrl = Image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
