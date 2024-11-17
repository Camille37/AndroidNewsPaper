package com.example.newspaper;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;

import com.example.exceptions.AuthenticationError;
import com.example.newspaper.ui.login.LoginActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class ArticleDetailActivity extends AppCompatActivity {

    private TextView detailCategory;
    private ImageView detailImage;
    private TextView detailTitle;
    private TextView detailSubTitle;
    private TextView detailDescription;
    private TextView detailBody;
    private TextView detailUserId;
    private TextView detailDate;

    private View detailFooterBar;

    private static final int REQUEST_GALLERY_PERMISSION = 100;
    private static final int REQUEST_CAMERA_PERMISSION = 200;

    private static final int GALLERY_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;

    private Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set status bar color (which display the level of battery, the hour, etc ...) to black
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.black));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // Import all the view elements
        detailCategory = findViewById(R.id.detailCategory);
        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        detailSubTitle = findViewById(R.id.detailSubTitle);
        detailDescription = findViewById(R.id.detailDescription);
        detailBody = findViewById(R.id.detailBody);
        detailUserId = findViewById(R.id.detailUserId);
        detailDate = findViewById(R.id.detailDate);
        detailFooterBar = findViewById(R.id.detailFooterBar);

        // Retrieve the article
        int article_id = (int) getIntent().getExtras().get("article_id");

        // Download articles
        Thread t = new Thread(new DownloadOneArticleThreads(ArticleDetailActivity.this, article_id));
        t.start();

        // Find toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Activate return home option
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_24px);
    }

    public void finishDownloadUI(Article article){
        // Hide progressBar
        ProgressBar pb = findViewById(R.id.detail_pb);
        pb.setVisibility(View.INVISIBLE);

        // Fulfill Views with data from the article
        if (article != null) {
            String userId = "User Id :" + article.getIdUser();
            detailUserId.setText(userId);
            //detailUserId.setVisibility(View.VISIBLE);
            detailFooterBar.setVisibility(View.VISIBLE);
            if (article.getCategory()!=null) {
                detailCategory.setText((article.getCategory()));
                detailCategory.setVisibility(View.VISIBLE);
            }
            if (article.getTitle()!=null) {
                detailTitle.setText(Utils.insertHtmlText(article.getTitle()));
                //detailTitle.setVisibility(View.VISIBLE);
            }
            if (article.getSubtitle()!=null) {
                detailSubTitle.setText(Utils.insertHtmlText(article.getSubtitle()));
                //detailSubTitle.setVisibility(View.VISIBLE);
            }
            if (article.getAbstractText()!=null) {
                detailDescription.setText(Utils.insertHtmlText(article.getAbstractText()));
                //detailDescription.setVisibility(View.VISIBLE);
            }
            if(article.getBodyText()!=null){
                detailBody.setText(Utils.insertHtmlText(article.getBodyText()));
                //detailBody.setVisibility(View.VISIBLE);
            }
            if (article.getPublicationDate()!=null) {
                Date date = article.getPublicationDate();
                detailDate.setText(Utils.dateToString(date));
                //detailDate.setVisibility(View.VISIBLE);
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
            detailImage.setVisibility(View.VISIBLE);

        }

        detailImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                showPictureDialog();
                //imageChooser();
            }
        });
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                requestGalleryPermission();
                                break;
                            case 1:
                                requestCameraPermission();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void requestGalleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) { // Android 14 (API level 34) or higher
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED}, REQUEST_GALLERY_PERMISSION);
            } else {
                // Permission already granted, proceed with accessing the gallery
                choosePhotoFromGallery();
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 (API level 33)
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO}, REQUEST_GALLERY_PERMISSION);
            } else {
                // Permission already granted, proceed with accessing the gallery
                choosePhotoFromGallery();
            }
        } else { // Android 12L (API level 32) or lower
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_GALLERY_PERMISSION);
            } else {
                // Permission already granted, proceed with accessing the gallery
                choosePhotoFromGallery();
            }
        }
    }

    private void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            takePhotoFromCamera();
        }
    }

    private void takePhotoFromCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQUEST_CODE) {
                Uri selectedImageUri = data.getData();

                Bitmap selectedImageBitmap = null;
                try {
                    selectedImageBitmap
                            = MediaStore.Images.Media.getBitmap(
                            this.getContentResolver(),
                            selectedImageUri);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                ImageView artImage = findViewById(R.id.detailImage);
                artImage.setImageBitmap(selectedImageBitmap);
                String imageBase64 = convertBitmapToBase64(selectedImageBitmap);
                // Save image & article
                int article_id = (int) getIntent().getExtras().get("article_id");
                Thread t = new Thread(new SaveArticleThread(ArticleDetailActivity.this, article_id, imageBase64 ));
                t.start();
                //article.addImage(convertBitmapToBase64(selectedImageBitmap),selectedImageUri.toString());

            } else if (requestCode == CAMERA_REQUEST_CODE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                String imageBase64 = convertBitmapToBase64(imageBitmap);
                detailImage.setImageBitmap(imageBitmap);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_GALLERY_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    choosePhotoFromGallery();
                } else {
                    // Permission denied, show a message to the user
                    Toast.makeText(ArticleDetailActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhotoFromCamera();
                } else {
                    // Permission denied, show a message to the user
                    Toast.makeText(ArticleDetailActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void startDownloadUI(){
        Log.i("download","Start downloading");

        // Set view elements on INVISIBLE to avoid strange displays while loading
        detailCategory.setVisibility(View.INVISIBLE);
        detailImage.setVisibility(View.INVISIBLE);
        //detailTitle.setVisibility(View.INVISIBLE);
        //detailSubTitle.setVisibility(View.INVISIBLE);
        //detailDescription.setVisibility(View.INVISIBLE);
        //detailBody.setVisibility(View.INVISIBLE);
        //detailUserId.setVisibility(View.INVISIBLE);
        //detailDate.setVisibility(View.INVISIBLE);
        detailFooterBar.setVisibility(View.INVISIBLE);

        // Show progressBar
        ProgressBar pb = findViewById(R.id.detail_pb);
        pb.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_article, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.action_login) {
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static String convertBitmapToBase64(Bitmap bitmap) {
        String base64Image = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            // Compress the bitmap into the output stream
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

            // Get the byte array from the output stream
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            // Convert the byte array to Base64 string
            base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                byteArrayOutputStream.close(); // Close the stream
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return base64Image;
    }

}

