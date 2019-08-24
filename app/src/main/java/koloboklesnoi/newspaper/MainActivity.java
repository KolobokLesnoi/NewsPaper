package koloboklesnoi.newspaper;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;
import koloboklesnoi.newspaper.adapter.ResultAdapter;
import koloboklesnoi.newspaper.api.MostPopular;
import koloboklesnoi.newspaper.api.gson.RtClient;
import koloboklesnoi.newspaper.database.DatabaseManager;
import koloboklesnoi.newspaper.model.Article;
import koloboklesnoi.newspaper.model.Result;
import koloboklesnoi.newspaper.utility.InternetConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String apiKey = "5wfPDNXd4va7YmzmsopkI6PjlUBzlYtS";

    private TabHost tabHost;

    private ListView emailedListView;
    private ListView sharedListView;
    private ListView viewedListView;
    private ListView favoritesListView;

    private List<Result> favoritesResultList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        fillFavoritesResultList();
        tabInitialize();
        listViewInitialize();
        resultListInitialize();
        fillListsViewAndCheckOnFavorites(favoritesListView,favoritesResultList);

    }

    public void tabInitialize() {

        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setContent(R.id.tab1);
        tabSpec.setIndicator("Most Emailed");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setContent(R.id.tab2);
        tabSpec.setIndicator("Most Shared");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setContent(R.id.tab3);
        tabSpec.setIndicator("Most Viewed");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag4");
        tabSpec.setContent(R.id.tab4);
        tabSpec.setIndicator("", ContextCompat.getDrawable(this,R.drawable.star_on));
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTab(0);

    }

    private void fillFavoritesResultList(){
        //Получение данных из базы
        try {
            DatabaseManager databaseManager = new DatabaseManager(this, "FAVORITES", null, 1);
            DataBaseAsyncTask dataBaseAsyncTask = new DataBaseAsyncTask();
            dataBaseAsyncTask.execute(databaseManager);
            favoritesResultList = dataBaseAsyncTask.get();
        }catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void listViewInitialize(){
        emailedListView = (ListView) findViewById(R.id.emailedListView);
        sharedListView = (ListView) findViewById(R.id.sharedListView);
        viewedListView = (ListView) findViewById(R.id.viewedListView);
        favoritesListView = (ListView) findViewById(R.id.favoritesListView);
    }

    private void resultListInitialize(){
        try {

            // Если есть интернет - Получение Джейсона и заполнение списков
            if (InternetConnection.checkConnection(this)) {

                MostPopular api = RtClient.getMostPopular();
                Call<Article> emailedCall = api.getEmailed(apiKey);
                emailedCall.enqueue(new Callback<Article>() {
                    @Override
                    public void onResponse(Call<Article> call, Response<Article> response) {
                        fillListsViewAndCheckOnFavorites(emailedListView,response.body().getResults());
                    }

                    @Override
                    public void onFailure(Call<Article> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                    }
                });

                Call<Article> sharedCall = api.getShared(apiKey);
                sharedCall.enqueue(new Callback<Article>() {
                    @Override
                    public void onResponse(Call<Article> call, Response<Article> response) {
                        fillListsViewAndCheckOnFavorites(sharedListView,response.body().getResults());
                    }

                    @Override
                    public void onFailure(Call<Article> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                    }
                });

                Call<Article> viewedCall = api.getViewed(apiKey);
                viewedCall.enqueue(new Callback<Article>() {
                    @Override
                    public void onResponse(Call<Article> call, Response<Article> response) {
                        fillListsViewAndCheckOnFavorites(viewedListView,response.body().getResults());
                    }

                    @Override
                    public void onFailure(Call<Article> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                    }
                });

            } else {
                Toast.makeText(this, "No internet", Toast.LENGTH_LONG).show();
            }

        }catch (Exception e){
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    private void fillListsViewAndCheckOnFavorites(ListView listView, List<Result> resultList){

        checkOnFavorites(resultList, favoritesResultList);
        setOnListViewOnItemClickListener(listView, resultList);
        setAdapter(listView, resultList);

    }

    private void setAdapter(ListView listView, List<Result> resultList){
        if(listView != null & resultList != null) {
            ResultAdapter adapter = new ResultAdapter(this, resultList);
            listView.setAdapter(adapter);
        }
    }

    private void checkOnFavorites(List<Result> resultList, List<Result> favoritesList){
        if(resultList != null & favoritesList != null) {
            if (resultList.size() > 0) {
                if(favoritesList.size() > 0){
                    for (Result result : resultList) {
                        for (Result favorites : favoritesList) {
                            if (result.getId() == favorites.getId()) {
                                result.setFavorites(true);
                            } else {
                                result.setFavorites(false);
                            }
                        }
                    }
                }else {
                    for (Result result : resultList) {
                        result.setFavorites(false);
                    }
                }
            }
        }
    }

    private void setOnListViewOnItemClickListener(ListView listView, final List<Result> resultList){
        if(listView != null & resultList != null) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                    Result result = resultList.get((int) id);
                    if (result.getPhotoURL() == null) {
                        result.setPhotoURL(result.getMedia().get(0).getMediaMetadata().get(2).getUrl());
                    }
                    intent.putExtra("result", result);
                    startActivity(intent);


                }
            });
        }
    }




    private class DataBaseAsyncTask extends AsyncTask<DatabaseManager,Void,List<Result>>{


        @Override
        protected List<Result> doInBackground(DatabaseManager... databaseManagers) {
            List<Result> resultList = new ArrayList<>();
            SQLiteDatabase database = databaseManagers[0].getReadableDatabase();
            Cursor cursor = database.query(DatabaseManager.TABLE_NAME, null, null,
                    null, null, null, null);
            if (cursor.moveToFirst()) {
                int id = cursor.getColumnIndex(DatabaseManager.COLUMN_ID);
                int section = cursor.getColumnIndex(DatabaseManager.COLUMN_SECTION);
                int title = cursor.getColumnIndex(DatabaseManager.COLUMN_TITLE);
                int abstract_ = cursor.getColumnIndex(DatabaseManager.COLUMN_ABSTRACT);
                int photo = cursor.getColumnIndex(DatabaseManager.COLUMN_PHOTO);
                int publish_date = cursor.getColumnIndex(DatabaseManager.COLUMN_PUBLISH_DATE);
                int source = cursor.getColumnIndex(DatabaseManager.COLUMN_SOURCE);

                do {
                    Result result = new Result();
                    result.setId(cursor.getLong(id));
                    result.setSection(cursor.getString(section));
                    result.setTitle(cursor.getString(title));
                    result.setAbstract(cursor.getString(abstract_));
                    result.setPhotoURL(cursor.getString(photo));
                    result.setPublishedDate(cursor.getString(publish_date));
                    result.setSource(cursor.getString(source));
//                    result.setFavorites(true);

                    resultList.add(result);
                }while (cursor.moveToNext());

            }

            database.close();

            return resultList;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        fillFavoritesResultList();
        fillListsViewAndCheckOnFavorites(favoritesListView,favoritesResultList);
        ResultAdapter resultAdapter;

        resultAdapter  = (ResultAdapter) emailedListView.getAdapter();
        fillListsViewAndCheckOnFavorites(emailedListView, resultAdapter.getResultList());

        resultAdapter  = (ResultAdapter) sharedListView.getAdapter();
        fillListsViewAndCheckOnFavorites(sharedListView, resultAdapter.getResultList());

        resultAdapter  = (ResultAdapter) viewedListView.getAdapter();
        fillListsViewAndCheckOnFavorites(viewedListView, resultAdapter.getResultList());

    }

}



