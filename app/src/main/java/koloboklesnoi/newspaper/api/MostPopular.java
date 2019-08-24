package koloboklesnoi.newspaper.api;

import koloboklesnoi.newspaper.model.Article;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MostPopular {

    @GET("svc/mostpopular/v2/emailed/1.json")
    Call<Article> getEmailed(@Query("api-key") String key);

    @GET("svc/mostpopular/v2/shared/1/facebook.json")
    Call<Article> getShared(@Query("api-key") String key);

    @GET("svc/mostpopular/v2/viewed/1.json")
    Call<Article> getViewed(@Query("api-key") String key);

}
