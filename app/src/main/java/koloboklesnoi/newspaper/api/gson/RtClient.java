package koloboklesnoi.newspaper.api.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import koloboklesnoi.newspaper.api.gson.converter.MediaMetadatumGsonConverter;
import koloboklesnoi.newspaper.api.gson.converter.MediumGsonConverter;
import koloboklesnoi.newspaper.api.MostPopular;
import koloboklesnoi.newspaper.api.gson.converter.ResultGsonConverter;
import koloboklesnoi.newspaper.model.MediaMetadatum;
import koloboklesnoi.newspaper.model.Medium;
import koloboklesnoi.newspaper.model.Result;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RtClient {

    private static final String URL_ROOT = "https://api.nytimes.com/";

    private static Retrofit getRetrofitInstance(){

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Result.class, new ResultGsonConverter());
        gsonBuilder.registerTypeAdapter(Medium.class, new MediumGsonConverter());
        gsonBuilder.registerTypeAdapter(MediaMetadatum.class, new MediaMetadatumGsonConverter());
        Gson gson = gsonBuilder.create();

        return new Retrofit.Builder()
                .baseUrl(URL_ROOT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static MostPopular getMostPopular(){
        return getRetrofitInstance().create(MostPopular.class);
    }

}
