package koloboklesnoi.newspaper;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import koloboklesnoi.newspaper.database.DatabaseManager;
import koloboklesnoi.newspaper.model.Result;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ArticleActivity extends AppCompatActivity {

    private Result result;

    private TextView articleSection;
    private TextView articleTitle;
    private TextView articleAbstract;
    private ImageView articlePhoto;
    private TextView articlePublishedDate;
    private TextView articleSource;

    private ImageButton favoritesButton;
    private boolean isFavorites;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_view);

        result = getIntent().getParcelableExtra("result");

        favoritesButton = (ImageButton) findViewById(R.id.favoritesButton);
        isFavorites = result.isFavorites();
        if(isFavorites) favoritesButton.setImageResource(R.drawable.star_on);

        articleSection = (TextView) findViewById(R.id.articleSection);
        articleTitle = (TextView) findViewById(R.id.articleTitle);
        articleAbstract = (TextView) findViewById(R.id.articleAbstract);
        articlePhoto = (ImageView) findViewById(R.id.articlePhoto);
        articlePublishedDate = (TextView) findViewById(R.id.articlePublishedDate);
        articleSource = (TextView) findViewById(R.id.articleSource);


        articleSection.setText(result.getSection());
        articleTitle.setText(result.getTitle());
        articleAbstract.setText(result.getAbstract());


        if(isFavorites){
            try {
                InputStream inputStream = openFileInput(result.getId()+".png");
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                articlePhoto.setImageBitmap(bitmap);
            }catch (Exception e){
                Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
            }
        }else {
            Picasso.with(this).load(result.getPhotoURL()).into(articlePhoto);
        }

        articlePublishedDate.setText(result.getPublishedDate());
        articleSource.setText(result.getSource());


    }

    public void onClickFavoritesButton(View view) {
        DatabaseManager databaseManager = new DatabaseManager(this, "FAVORITES", null, 1);
        DataBaseAsyncTask dataBaseAsyncTask = new DataBaseAsyncTask();
        if (isFavorites) {
            favoritesButton.setImageResource(R.drawable.star_off);
            dataBaseAsyncTask.execute(databaseManager);
            isFavorites = false;
        } else {
            favoritesButton.setImageResource(R.drawable.star_on);
            dataBaseAsyncTask.execute(databaseManager);
            isFavorites = true;
        }
    }

    private class DataBaseAsyncTask extends AsyncTask<DatabaseManager, Void, Void> {

        @Override
        protected Void doInBackground(DatabaseManager... databaseManagers) {
            SQLiteDatabase database = databaseManagers[0].getWritableDatabase();
            if (isFavorites) {
                database.insert(DatabaseManager.TABLE_NAME, null, getContentValues());
                try {
                    FileOutputStream fos = openFileOutput(result.getId()+".png",MODE_PRIVATE);
                    Bitmap bitmap = ((BitmapDrawable) articlePhoto.getDrawable()).getBitmap();
                    bitmap.compress(Bitmap.CompressFormat.PNG,0, fos);
                    fos.flush();
                    fos.close();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                }
            } else {
                database.delete(DatabaseManager.TABLE_NAME, "ID = " + result.getId(), null);
                deleteFile(result.getId()+".png");
            }
            database.close();
            return null;
        }

        private ContentValues getContentValues() {
            ContentValues values = new ContentValues();
            values.put(DatabaseManager.COLUMN_ID, result.getId());
            values.put(DatabaseManager.COLUMN_SECTION, result.getSection());
            values.put(DatabaseManager.COLUMN_TITLE, result.getTitle());
            values.put(DatabaseManager.COLUMN_ABSTRACT, result.getAbstract());
            values.put(DatabaseManager.COLUMN_PHOTO, result.getPhotoURL());
            values.put(DatabaseManager.COLUMN_PUBLISH_DATE, result.getPublishedDate());
            values.put(DatabaseManager.COLUMN_SOURCE, result.getSource());

            return values;
        }
    }
}
