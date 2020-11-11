package es.uniovi.eii.sdm.favmovies.remote;

import es.uniovi.eii.sdm.favmovies.data.api.movielist.MovieListResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDBApi {

	public static final String BASE_URL = "https://api.themoviedb.org/3/";

	@GET("movie/{list}")
	Call<MovieListResult> getMovieList(
			@Path("list") String list,
			@Query("api_key") String apiKey,
			@Query("language") String language,
			@Query("page") int page
	);

}
