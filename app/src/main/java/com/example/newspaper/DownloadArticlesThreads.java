package com.example.newspaper;

import android.util.Log;

import com.example.exceptions.AuthenticationError;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class DownloadArticlesThreads implements Runnable{
        MainActivity ma;
        private final String service_url="https://sanger.dia.fi.upm.es/pui-rest-news/";
        private final String login_user="DEV_TEAM_06";
        private final String login_pwd = "123456@06";

        public DownloadArticlesThreads(MainActivity ma){
            this.ma = ma;
        }

        @Override
        public void run() {
            ma.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ma.startDownloadUI();
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
            List<Article> article_list = mm.getArticles();
            for (Article article : article_list) {
                System.out.println(article);
                Log.i("authentification error", article.toString());
            }
            ma.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ma.finishDownloadUI(article_list);
                }
            });
        }
}
