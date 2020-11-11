package es.uniovi.eii.sdm.favmovies.data;

import java.util.ArrayList;
import java.util.List;

import es.uniovi.eii.sdm.favmovies.data.api.movielist.MovieData;
import es.uniovi.eii.sdm.favmovies.model.Category;
import es.uniovi.eii.sdm.favmovies.model.Movie;

public class ApiDataMapper {

	private static final String BASE_URL_IMG= "https://image.tmdb.org/t/p/";
	private static final String IMG_W342= "w342";
	private static final String IMG_ORIGINAL= "original";

	public static List<Movie> convertMovieListToDomain(List<MovieData> movieDataList) {
		ArrayList<Movie> movieList= new ArrayList<Movie>();
		for (MovieData movieData: movieDataList) {
			String urlCover;
			String urlBackground;

			urlCover = (movieData.getPosterPath() == null)
					? "" : BASE_URL_IMG + IMG_W342 + movieData.getPosterPath();
			urlBackground = (movieData.getBackdropPath()==null) ?
					"" : BASE_URL_IMG + IMG_ORIGINAL + movieData.getBackdropPath();

			movieList.add( new Movie(
					String.valueOf(movieData.getId()),
					movieData.getTitle(),
					movieData.getOverview(),
					new Category("",""),
					"",
					movieData.getReleaseDate(),
					urlCover,
					urlBackground,
					""
			));
		}
		return movieList;
	}
}
