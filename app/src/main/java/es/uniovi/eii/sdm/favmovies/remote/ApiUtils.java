package es.uniovi.eii.sdm.favmovies.remote;

import retrofit2.Retrofit;

public class ApiUtils {

    public static final String LANGUAGE = "es-ES";
    public static final String API_KEY = "4b85abb5259e4cd67239696c3ffeff3b";

    public static TheMovieDBApi createTheMovieDBApi() {
        Retrofit retrofit= RetrofitClient.getClient(TheMovieDBApi.BASE_URL);
        return retrofit.create(TheMovieDBApi.class);
    }


}
