package es.uniovi.eii.sdm.favmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import es.uniovi.eii.sdm.favmovies.model.MovieStaff;

import java.util.ArrayList;
import java.util.List;

public class StaffMoviesDataSource {

	private SQLiteDatabase database;

	private MyDBHelper dbHelper;

	private final String[] allColumns = {
			MyDBHelper.COLUMN_ID_REPARTO,
			MyDBHelper.COLUMN_ID_PELICULAS,
			MyDBHelper.COLUMN_PERSONAJE
	};

	/**
	 * Constructor.
	 *
	 * @param context
	 */
	public StaffMoviesDataSource(Context context) {
		dbHelper = new MyDBHelper(context, null);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public long createMovieStaff(MovieStaff toInsert) {
		ContentValues values = new ContentValues();

		values.put(MyDBHelper.COLUMN_ID_REPARTO, toInsert.getIdStaff());
		values.put(MyDBHelper.COLUMN_ID_PELICULAS, toInsert.getIdMovie());
		values.put(MyDBHelper.COLUMN_PERSONAJE, toInsert.getCharacter());

		return database.insert(MyDBHelper.TABLE_PELICULAS_REPARTO, null, values);
	}

	public List<MovieStaff> getAll() {
		List<MovieStaff> dataList = new ArrayList<>();
		Cursor cursor = database.query(MyDBHelper.TABLE_PELICULAS_REPARTO, allColumns,
				null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			final MovieStaff movieStaff = new MovieStaff();
			movieStaff.setIdStaff(cursor.getInt(0));
			movieStaff.setIdMovie(cursor.getInt(1));
			movieStaff.setCharacter(cursor.getString(2));

			dataList.add(movieStaff);
			cursor.moveToNext();
		}
		cursor.close();
		return dataList;
	}
}