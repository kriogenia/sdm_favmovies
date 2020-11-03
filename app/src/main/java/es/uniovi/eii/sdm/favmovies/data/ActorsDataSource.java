package es.uniovi.eii.sdm.favmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import es.uniovi.eii.sdm.favmovies.model.Actor;
import java.util.ArrayList;
import java.util.List;

public class ActorsDataSource {

	private SQLiteDatabase database;

	private MyDBHelper dbHelper;

	private final String[] allColumns = { MyDBHelper.COLUMN_ID_REPARTO, MyDBHelper.COLUMN_NOMBRE_ACTOR,
			MyDBHelper.COLUMN_IMAGEN_ACTOR, MyDBHelper.COLUMN_URL_IMDB};

	/**
	 * Constructor.
	 *
	 * @param context
	 */
	public ActorsDataSource(Context context) {
		dbHelper = new MyDBHelper(context, null);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public long createActor(Actor actorToInsert) {
		ContentValues values = new ContentValues();

		values.put(MyDBHelper.COLUMN_ID_REPARTO, actorToInsert.getId());
		values.put(MyDBHelper.COLUMN_NOMBRE_ACTOR, actorToInsert.getName());
		values.put(MyDBHelper.COLUMN_IMAGEN_ACTOR, actorToInsert.getImage());
		values.put(MyDBHelper.COLUMN_URL_IMDB, actorToInsert.getUrlImdb());

		return database.insert(MyDBHelper.TABLE_REPARTO, null, values);
	}

	public List<Actor> getAll() {
		List<Actor> actorList = new ArrayList<>();

		Cursor cursor = database.query(MyDBHelper.TABLE_REPARTO, allColumns,
				null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			final Actor actor = new Actor();
			actor.setId(cursor.getInt(0));
			actor.setName(cursor.getString(1));
			actor.setImage("https://image.tmdb.org/t/p/original/" + cursor.getString(2));
			actor.setUrlImdb("https://www.imdb.com/name/" + cursor.getString(3));

			actorList.add(actor);
			cursor.moveToNext();
		}
		cursor.close();
		return actorList;
	}

	public List<Actor> starredActors(int movieId) {
		List<Actor> staff = new ArrayList<>();

		Cursor cursor = database.rawQuery("SELECT " +
				MyDBHelper.TABLE_REPARTO + "." + MyDBHelper.COLUMN_NOMBRE_ACTOR + ", " +
				MyDBHelper.TABLE_PELICULAS_REPARTO + "." + MyDBHelper.COLUMN_PERSONAJE + ", " +
				MyDBHelper.TABLE_REPARTO + "." + MyDBHelper.COLUMN_IMAGEN_ACTOR + ", " +
				MyDBHelper.TABLE_REPARTO + "." + MyDBHelper.COLUMN_URL_IMDB +
				" FROM " + MyDBHelper.TABLE_PELICULAS_REPARTO +
				" JOIN " + MyDBHelper.TABLE_REPARTO + " ON " +
				MyDBHelper.TABLE_PELICULAS_REPARTO + "." + MyDBHelper.COLUMN_ID_REPARTO +
				" = " + MyDBHelper.TABLE_REPARTO + "." + MyDBHelper.COLUMN_ID_REPARTO +
				" WHERE " + MyDBHelper.TABLE_PELICULAS_REPARTO + "." + MyDBHelper.COLUMN_ID_PELICULAS + " = " + movieId, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			staff.add(new Actor(
					cursor.getString(0),
					cursor.getString(1),
					"https://image.tmdb.org/t/p/original/" + cursor.getString(2),
					"https://www.imdb.com/name/" + cursor.getString(3)));
			cursor.moveToNext();
		}
		cursor.close();
		return staff;
	}
}