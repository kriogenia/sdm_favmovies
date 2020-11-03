package es.uniovi.eii.sdm.favmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "movies.db";
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_PELICULAS = "tabla_peliculas";
	public static final String COLUMN_ID_PELICULAS = "id_pelicula";
	public static final String COLUMN_TITULO_PELICULAS = "titulo_pelicula";
	public static final String COLUMN_ARGUMENTO_PELICULAS = "argumento_pelicula";
	public static final String COLUMN_CATEGORIA_PELICULAS = "categoria_pelicula";
	public static final String COLUMN_DURACION_PELICULAS = "duracion_pelicula";
	public static final String COLUMN_FECHA_PELICULAS = "fecha_pelicula";
	public static final String COLUMN_CARATULA_PELICULAS = "URL_caratula_pelicula";
	public static final String COLUMN_FONDO_PELICULAS = "URL_fondo_pelicula";
	public static final String COLUMN_TRAILER_PELICULAS = "URL_trailer_pelicula";

	public static final String TABLE_PELICULAS_REPARTO = "tabla_peliculas_reparto";
	public static final String COLUMN_PERSONAJE = "nombre_personaje";

	public static final String TABLE_REPARTO = "tabla_reparto";
	public static final String COLUMN_ID_REPARTO = "id_reparto";
	public static final String COLUMN_NOMBRE_ACTOR = "nombre_actor";
	public static final String COLUMN_IMAGEN_ACTOR = "URL_imagen_actor";
	public static final String COLUMN_URL_IMDB = "URL_imdb_actor";

	private static final String DATABASE_DROP_PELICULAS = "DROP TABLE IF EXISTS " + TABLE_PELICULAS;
	private static final String DATABASE_DROP_PELICULAS_REPARTO = "DROP TABLE IF EXISTS " + TABLE_PELICULAS_REPARTO;
	private static final String DATABASE_DROP_REPARTO = "DROP TABLE IF EXISTS " + TABLE_REPARTO;

	/**
	 * Script para crear la base datos en SQL
	 */
	private static final String CREATE_TABLA_PELICULAS = "create table if not exists " + TABLE_PELICULAS
			+ "( " +
			COLUMN_ID_PELICULAS + " integer primary key, " +
			COLUMN_TITULO_PELICULAS + " text not null, " +
			COLUMN_ARGUMENTO_PELICULAS + " text, " +
			COLUMN_CATEGORIA_PELICULAS + " text not null, " +
			COLUMN_DURACION_PELICULAS + " text, " +
			COLUMN_FECHA_PELICULAS + " text, " +
			COLUMN_CARATULA_PELICULAS + " text, " +
			COLUMN_FONDO_PELICULAS + " text, " +
			COLUMN_TRAILER_PELICULAS + " text" +
			");";

	private static final String CREATE_TABLA_PELICULAS_REPARTO = "create table if not exists " + TABLE_PELICULAS_REPARTO
			+ "(" +
			COLUMN_ID_REPARTO + " integer, " +
			COLUMN_ID_PELICULAS + " integer, " +
			COLUMN_PERSONAJE + " text, " +
			" primary key (" + COLUMN_ID_REPARTO + ", " + COLUMN_ID_PELICULAS + ")" +
			");";

	private static final String CREATE_TABLA_REPARTO = "create table if not exists " + TABLE_REPARTO
			+ "(" +
			COLUMN_ID_REPARTO + " integer primary key, " +
			COLUMN_NOMBRE_ACTOR + " text, " +
			COLUMN_IMAGEN_ACTOR + " text, " +
			COLUMN_URL_IMDB + " text" +
			");";

	public MyDBHelper(Context context, SQLiteDatabase.CursorFactory factory) {
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLA_PELICULAS);
		db.execSQL(CREATE_TABLA_PELICULAS_REPARTO);
		db.execSQL(CREATE_TABLA_REPARTO);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DATABASE_DROP_PELICULAS);
		db.execSQL(DATABASE_DROP_PELICULAS_REPARTO);
		db.execSQL(DATABASE_DROP_REPARTO);
	}
}
