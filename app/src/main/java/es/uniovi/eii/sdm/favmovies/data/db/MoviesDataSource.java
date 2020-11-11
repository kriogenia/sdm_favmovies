package es.uniovi.eii.sdm.favmovies.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import es.uniovi.eii.sdm.favmovies.model.Category;
import es.uniovi.eii.sdm.favmovies.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MoviesDataSource {

	private SQLiteDatabase database;

	private MyDBHelper dbHelper;

	private final String[] allColumns = {
			MyDBHelper.COLUMN_ID_PELICULAS,
			MyDBHelper.COLUMN_TITULO_PELICULAS,
			MyDBHelper.COLUMN_ARGUMENTO_PELICULAS,
			MyDBHelper.COLUMN_CATEGORIA_PELICULAS,
			MyDBHelper.COLUMN_DURACION_PELICULAS,
			MyDBHelper.COLUMN_FECHA_PELICULAS,
			MyDBHelper.COLUMN_CARATULA_PELICULAS,
			MyDBHelper.COLUMN_FONDO_PELICULAS,
			MyDBHelper.COLUMN_TRAILER_PELICULAS
	};

	/**
	 * Constructor.
	 *
	 * @param context
	 */
	public MoviesDataSource(Context context) {
		dbHelper = new MyDBHelper(context, null);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public long createMovie(Movie movieToInsert) {
		ContentValues values = new ContentValues();

		values.put(MyDBHelper.COLUMN_ID_PELICULAS, movieToInsert.getId());
		values.put(MyDBHelper.COLUMN_TITULO_PELICULAS, movieToInsert.getTitle());
		values.put(MyDBHelper.COLUMN_ARGUMENTO_PELICULAS, movieToInsert.getArgument());
		values.put(MyDBHelper.COLUMN_CATEGORIA_PELICULAS, movieToInsert.getCategory().getName());
		values.put(MyDBHelper.COLUMN_DURACION_PELICULAS, movieToInsert.getDuration());
		values.put(MyDBHelper.COLUMN_FECHA_PELICULAS, movieToInsert.getDate());
		values.put(MyDBHelper.COLUMN_CARATULA_PELICULAS, movieToInsert.getUrlCover());
		values.put(MyDBHelper.COLUMN_FONDO_PELICULAS, movieToInsert.getUrlBackground());
		values.put(MyDBHelper.COLUMN_TRAILER_PELICULAS, movieToInsert.getUrlTrailer());

		return database.insert(MyDBHelper.TABLE_PELICULAS, null, values);
	}

	public List<Movie> getAll() {
		List<Movie> movieList = new ArrayList<>();

		Cursor cursor = database.query(MyDBHelper.TABLE_PELICULAS, allColumns,
				null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			final Movie movie = new Movie();
			movie.setId(cursor.getInt(0));
			movie.setTitle(cursor.getString(1));
			movie.setArgument(cursor.getString(2));
			movie.setCategory(new Category(cursor.getString(3), ""));
			movie.setDuration(cursor.getString(4));
			movie.setDate(cursor.getString(5));
			movie.setUrlCover("https://image.tmdb.org/t/p/original/" + cursor.getString(6));
			movie.setUrlBackground("https://image.tmdb.org/t/p/original/" + cursor.getString(7));
			movie.setUrlTrailer("https://youtu.be/" + cursor.getString(8));

			movieList.add(movie);
			cursor.moveToNext();
		}
		cursor.close();
		return movieList;
	}

	public List<Movie> getByCategory(String filter) {
		List<Movie> movieList = new ArrayList<>();
		Cursor cursor = database.rawQuery("Select * " +
				" FROM " + MyDBHelper.TABLE_PELICULAS +
				" WHERE " + MyDBHelper.TABLE_PELICULAS + "." +
				MyDBHelper.COLUMN_CATEGORIA_PELICULAS + " = \"" + filter + "\"", null);
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			final Movie pelicula = new Movie();
			pelicula.setId(cursor.getInt(0));
			pelicula.setTitle(cursor.getString(1));
			pelicula.setArgument(cursor.getString(2));
			pelicula.setCategory(new Category(cursor.getString(3), ""));
			pelicula.setDuration(cursor.getString(4));
			pelicula.setDate(cursor.getString(5));
			pelicula.setUrlCover("https://image.tmdb.org/t/p/original/" + cursor.getString(6));
			pelicula.setUrlBackground("https://image.tmdb.org/t/p/original/" + cursor.getString(7));
			pelicula.setUrlTrailer("https://youtu.be/" + cursor.getString(8));

			movieList.add(pelicula);
			cursor.moveToNext();
		}
		cursor.close();
		return movieList;
	}
}