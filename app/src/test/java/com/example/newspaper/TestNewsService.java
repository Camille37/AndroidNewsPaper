package com.example.newspaper;

import org.junit.Test;
import static org.junit.Assert.*;

import android.util.Log;

import java.util.List;
import java.util.Properties;
import java.io.IOException;

import com.example.newspaper.Article;
import com.example.newspaper.Image;
import com.example.newspaper.ModelManager;
import com.example.newspaper.Utils;
import com.example.exceptions.AuthenticationError;
import com.example.exceptions.ServerCommunicationError;

public class TestNewsService {

    @Test
    public void first_test() throws AuthenticationError, ServerCommunicationError, IOException {

        Properties prop = new Properties();
        prop.setProperty(ModelManager.ATTR_LOGIN_USER, "DEV_TEAM_06");
        prop.setProperty(ModelManager.ATTR_LOGIN_PASS, "123456@06");
        prop.setProperty(ModelManager.ATTR_SERVICE_URL, "https://sanger.dia.fi.upm.es/pui-rest-news/");
        prop.setProperty(ModelManager.ATTR_REQUIRE_SELF_CERT, "TRUE");

        // Log in
        ModelManager mm = null;
        try {
            mm = new ModelManager(prop);
        } catch (AuthenticationError e) {

            System.exit(-1);
        }

        // get list of articles for logged user
        List<Article> res = mm.getArticles();
        for (Article article : res) {
            System.out.println(article);
        }

        try {
            //mm.deleteImage(12037);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        // create one article and save in server
		/*
		Article myArticle = null;
		myArticle = new Article(mm, "Categoria","Titulo","Resumen articulo mio", "Cuerpo de texto", "Pie del articulo", true, new java.util.Date());
		myArticle.save();
		*/

        // save one image to an article (through the article service)
		/*
		myArticle.addImage(Utils.encodeImage(Utils.captureScreen()), "escritorio");
		myArticle.save();
		*/

        // save one image to an article (through image service)
        // Utils.captureScreen() doesn't exists in mobile, should use Utils methods from one Bitmap
		/*
		Image ii = myArticle.addImage( Utils.captureScreen(), "testing image "+ lIm.size()+1);
		ii.save();
		ii.delete();
		*/


    }
}
