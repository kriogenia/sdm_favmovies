package es.uniovi.eii.sdm.favmovies;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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

	private List<Movie> movieList;
	private List<Actor> staffList;
	private List<MovieStaff> movieStaffList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_recycler);
		loadMovies(categoryFilter);
		loadStaff();
		loadMoviesStaff();
	}

	@Override
	protected void onResume() {
		super.onResume();

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

	private void getMovies() {
		MoviesDataSource moviesDataSource = new MoviesDataSource(getApplicationContext());
		moviesDataSource.open();
		if (categoryFilter == null || categoryFilter.equals("Sin definir"))
			movieList = moviesDataSource.getAll();
		else
			movieList = moviesDataSource.getByCategory(categoryFilter);
		moviesDataSource.close();
	}

}