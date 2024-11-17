package com.example.newspaper;

import android.util.Log;
import com.example.exceptions.AuthenticationError;
import java.util.Properties;

public class DownloadOneArticleThreads implements Runnable{
    private ArticleDetailActivity aa;

    private int articleId;
    private final String service_url="https://sanger.dia.fi.upm.es/pui-rest-news/";
    private final String login_user="DEV_TEAM_06";
    private final String login_pwd = "123456@06";

    public DownloadOneArticleThreads(ArticleDetailActivity aa, int articleId){
        this.aa = aa;
        this.articleId = articleId;
    }

    @Override
    public void run() {
        aa.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                aa.startDownloadUI();
            }
        });

        Properties prop = new Properties();
        prop.setProperty(ModelManager.ATTR_LOGIN_USER, login_user);
        prop.setProperty(ModelManager.ATTR_LOGIN_PASS, login_pwd);
        prop.setProperty(ModelManager.ATTR_SERVICE_URL, service_url);
        prop.setProperty(ModelManager.ATTR_REQUIRE_SELF_CERT, "TRUE");

        ModelManager mm = null;

        try{
            mm = new ModelManager(prop);
        }catch (AuthenticationError e) {
            Log.e("authentification error", e.getMessage());
            System.exit(-1);
        }

        // get list of articles for logged user
        Article article = mm.getArticle(articleId);
        Log.i("detail_article", article.toString());
        aa.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                aa.finishDownloadUI(article);
            }
        });
    }
}
