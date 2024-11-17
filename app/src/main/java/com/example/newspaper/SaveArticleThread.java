package com.example.newspaper;

import android.util.Log;

import com.example.exceptions.AuthenticationError;

import java.util.Properties;

public class SaveArticleThread implements Runnable{

    private ArticleDetailActivity aa;
    private int articleID;
    private final String service_url="https://sanger.dia.fi.upm.es/pui-rest-news/";
    private final String login_user="DEV_TEAM_06";
    private final String login_pwd = "123456@06";

    private Article article;

    private String imageBase64;

    public SaveArticleThread (ArticleDetailActivity aa, int articleID, String imageBase64) {
        this.aa = aa;
        this.articleID = articleID;
        this.imageBase64 = imageBase64;
    }

    @Override
    public void run() {

        Properties prop = new Properties();
        prop.setProperty(ModelManager.ATTR_LOGIN_USER, login_user);
        prop.setProperty(ModelManager.ATTR_LOGIN_PASS, login_pwd);
        prop.setProperty(ModelManager.ATTR_SERVICE_URL, service_url);
        prop.setProperty(ModelManager.ATTR_REQUIRE_SELF_CERT, "TRUE");

        ModelManager mm = null;

        try{
            mm = new ModelManager(prop);
            article = mm.getArticle(articleID);
            Image newImage = new Image(mm, 1, "new image", articleID, imageBase64);
            int i = mm.saveImage(newImage);
        }catch (AuthenticationError e) {
            Log.e("authentication error", e.getMessage());
            System.exit(-1);
        }
    }
}
