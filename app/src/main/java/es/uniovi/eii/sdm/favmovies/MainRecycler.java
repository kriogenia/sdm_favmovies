package es.uniovi.eii.sdm.favmovies;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import es.uniovi.eii.sdm.favmovies.data.ActorsDataSource;
import es.uniovi.eii.sdm.favmovies.data.MoviesDataSource;
import es.uniovi.eii.sdm.favmovies.data.StaffMoviesDataSource;
import es.uniovi.eii.sdm.favmovies.model.Actor;
import es.uniovi.eii.sdm.favmovies.model.Category;
import es.uniovi.eii.sdm.favmovies.model.Movie;
import es.uniovi.eii.sdm.favmovies.model.MovieStaff;

public class MainRecycler extends AppCompatActivity {

	public static final String MOVIE_SELECTED = "movie_selected";
	public static final String NEW_MOVIE = "new_movie";

	public static final int MANAGE_NEW_MOVIE = 1;

	public static String categoryFilter = null;

	private RecyclerView rvMovies;

	private NotificationCompat.Builder mBuilder;
	private NotificationManager mNotificationManager;

	private List<Movie> movieList;
	private List<Actor> staffList;
	private List<MovieStaff> movieStaffList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_recycler);
	}

	@Override
	protected void onResume() {
		super.onResume();
		buildNotification("FavMovies", "Cargada la base de datos");
		DownloadFilesTask task = new DownloadFilesTask();
		task.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main_recycler, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intentSettingsActivity = new Intent(MainRecycler.this, SettingsActivity.class);
			startActivity(intentSettingsActivity);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == MANAGE_NEW_MOVIE) {
			if (resultCode == RESULT_OK) {
				assert data != null;
				final Movie movie = data.getParcelableExtra(NEW_MOVIE);
				if (movie != null) {
					movieList.add(movie);
					MoviesDataSource moviesDataSource = new MoviesDataSource(getApplicationContext());
					moviesDataSource.open();
					moviesDataSource.createMovie(movie);
					moviesDataSource.close();
					MovieListAdapter mlAdapter = new MovieListAdapter(movieList,
							new MovieListAdapter.OnItemClickListener() {
								@Override
								public void onItemClick(Movie item) {
									clickOnItem(item);
								}
							});
					rvMovies.setAdapter(mlAdapter);
				}
			}
			if (resultCode == RESULT_CANCELED) {
				Snackbar.make(findViewById(R.id.mainLayout), R.string.msg_canceled_action, Snackbar.LENGTH_LONG).show();
			}
		}
	}

	private void clickOnItem(Movie item) {
		Intent intent = new Intent(MainRecycler.this, ShowMovieActivity.class);
		intent.putExtra(MOVIE_SELECTED, item);
		startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
	}

	private void getMovies() {
		MoviesDataSource moviesDataSource = new MoviesDataSource(getApplicationContext());
		moviesDataSource.open();
		categoryFilter = PreferenceManager.getDefaultSharedPreferences(this)
				.getString("keyCategory", null);
		if (categoryFilter == null || categoryFilter.equals("Sin definir"))
			movieList = moviesDataSource.getAll();
		else
			movieList = moviesDataSource.getByCategory(categoryFilter);
		moviesDataSource.close();
	}

	private void loadView() {
		getMovies();
		rvMovies = findViewById(R.id.rvMovies);
		rvMovies.setHasFixedSize(true);

		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
		rvMovies.setLayoutManager(layoutManager);

		MovieListAdapter mlAdapter = new MovieListAdapter(movieList,
				new MovieListAdapter.OnItemClickListener() {
					@Override
					public void onItemClick(Movie item) {
						clickOnItem(item);
					}
				});
		rvMovies.setAdapter(mlAdapter);

		FloatingActionButton fab = findViewById(R.id.fabAdd);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainRecycler.this, NewMovieActivity.class);
				startActivity(intent);
			}
		});
		fab.setEnabled(false);
	}

	private void createNotificationChannel() {
		// Create the NotificationChannel, but only on API 26+ because
		// the NotificationChannel class is new and not in the support library
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			CharSequence name = "channel";
			String description = "channel_description";
			int importance = NotificationManager.IMPORTANCE_DEFAULT;
			NotificationChannel channel = new NotificationChannel("M_CH_ID", name, importance);
			channel.setDescription(description);
			// Register the channel with the system; you can't change the importance
			// or other notification behaviors after this
			NotificationManager notificationManager = getSystemService(NotificationManager.class);
			notificationManager.createNotificationChannel(channel);
		}
	}

	public void buildNotification(String title, String content) {
		createNotificationChannel();
		mNotificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(getApplicationContext(), "M_CH_ID");
		mBuilder.setSmallIcon(R.drawable.ic_baseline_notifications_24)
				.setContentTitle(title)
				.setContentText(content);
	}


	private class DownloadFilesTask extends AsyncTask<Void, Integer, String> {

		private ProgressDialog progressDialog;

		private float linesToRead = 0.0f;
		private float readLines = 0.0f;

		@Override
		protected String doInBackground(Void... voids) {
			String message = "";
			try {
				loadMovies(categoryFilter);
				loadStaff();
				loadMoviesStaff();
				mNotificationManager.notify(001, mBuilder.build());
				message = "Lista de películas actualizada";
			} catch (Exception e) {
				message = "Error en la actualización de la lista de películas";
			}
			return message;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.progressDialog = new ProgressDialog(MainRecycler.this);
			this.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			this.progressDialog.setCancelable(false);
			this.progressDialog.show();

			linesToRead = (float) fileLines("movies.csv");
			linesToRead += (float) fileLines("movies-staff.csv");
			linesToRead += (float) fileLines("staff.csv");

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			progressDialog.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(String s) {
			this.progressDialog.dismiss();
			Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
			loadView();
		}

		protected int fileLines(String filename) {
			InputStream file = null;
			InputStreamReader reader = null;
			BufferedReader bufferedReader = null;
			int lines = 0;
			try {
				file = getAssets().open(filename);
				reader = new InputStreamReader(file);
				bufferedReader = new BufferedReader(reader);
				while (bufferedReader.readLine() != null)
					lines++;
				bufferedReader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return lines;
		}

		protected void loadMovies(String filter) {
			String defaultCover = "https://image.tmdb.org/t/p/original/jnFCk7qGGWop2DgfnJXeKLZFuBq.jpg\n";
			String defaultBg = "https://image.tmdb.org/t/p/original/xJWPZIYOEFIjZpBL7SVBGnzRYXp.jpg\n";
			String defaultTrailer = "https://www.youtube.com/watch?v=lpEJVgysiWs\n";

			Movie movie;
			movieList = new ArrayList<>();
			InputStream file = null;
			InputStreamReader reader = null;
			BufferedReader bufferedReader = null;
			try {
				file = getAssets().open("movies.csv");
				reader = new InputStreamReader(file);
				bufferedReader = new BufferedReader(reader);

				String line = null;
				bufferedReader.readLine();
				readLines++;

				while ((line = bufferedReader.readLine()) != null) {
					movie = null;
					String[] data = line.split(";");
					if (data.length >= 5) {
						if (filter == null || filter.equals("Sin definir")) {
							if (data.length == 9) {
								movie = new Movie(data[0], data[1], data[2],
										new Category(data[3], ""), data[4],
										data[5], data[6], data[7], data[8]);
							} else {
								movie = new Movie(data[0], data[1], data[2],
										new Category(data[3], ""), data[4],
										data[5], defaultCover, defaultBg, defaultTrailer);
							}
						}
						else {
							if (data[3].equals(filter)) {
								if (data.length == 9) {
									movie = new Movie(data[0], data[1], data[2],
											new Category(data[3], ""), data[4],
											data[5], data[6], data[7], data[8]);
								} else {
									movie = new Movie(data[0], data[1], data[2],
											new Category(data[3], ""), data[4],
											data[5], defaultCover, defaultBg, defaultTrailer);
								}
							}
						}
						if (movie != null) {
							Log.d("loadMovies", movie.toString());
							movieList.add(movie);
							// We add the movie to the DB
							MoviesDataSource moviesDataSource = new MoviesDataSource(getApplicationContext());
							moviesDataSource.open();
							moviesDataSource.createMovie(movie);
							moviesDataSource.close();
						}
						readLines++;
						publishProgress((int)((readLines / linesToRead)*100));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (bufferedReader != null) {
					try {
						bufferedReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		protected void loadStaff() {
			Actor actor = null;
			staffList = new ArrayList<Actor>();
			InputStream file = null;
			InputStreamReader reader = null;
			BufferedReader bufferedReader = null;

			try {
				file = getAssets().open("staff.csv");
				reader = new InputStreamReader(file);
				bufferedReader = new BufferedReader(reader);

				String line = null;
				bufferedReader.readLine();
				readLines++;

				while ((line = bufferedReader.readLine()) != null) {
					String[] data = line.split(";");
					if (data.length == 4) {
						actor = new Actor(Integer.parseInt(data[0]), data[1], data[2], data[3]);
					}

					assert actor != null;
					Log.d("loadStaff", actor.toString());
					staffList.add(actor);

					// Metemos actor en la BD
					ActorsDataSource actorsDataSource = new ActorsDataSource(getApplicationContext());
					actorsDataSource.open();
					actorsDataSource.createActor(actor);
					actorsDataSource.close();

					readLines++;
					publishProgress((int)((readLines / linesToRead)*100));
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (bufferedReader != null) {
					try {
						bufferedReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		protected void loadMoviesStaff() {
			MovieStaff movieStaff = null;
			movieStaffList = new ArrayList<>();
			InputStream file = null;
			InputStreamReader reader = null;
			BufferedReader bufferedReader = null;

			try {
				file = getAssets().open("movies-staff.csv");
				reader = new InputStreamReader(file);
				bufferedReader = new BufferedReader(reader);

				String line = null;
				bufferedReader.readLine();
				readLines++;

				while ((line = bufferedReader.readLine()) != null) {
					String[] data = line.split(";");
					if (data.length == 3) {
						movieStaff = new MovieStaff(Integer.parseInt(data[0]), Integer.parseInt(data[1]), data[2]);
					}

					assert movieStaff != null;
					Log.d("loadMoviesStaff", movieStaff.toString());
					movieStaffList.add(movieStaff);

					StaffMoviesDataSource repartoPeliDataSource = new StaffMoviesDataSource(getApplicationContext());
					repartoPeliDataSource.open();
					repartoPeliDataSource.createMovieStaff(movieStaff);
					repartoPeliDataSource.close();

					readLines++;
					publishProgress((int)((readLines / linesToRead)*100));
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (bufferedReader != null) {
					try {
						bufferedReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}