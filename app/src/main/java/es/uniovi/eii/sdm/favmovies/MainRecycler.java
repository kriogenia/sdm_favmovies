package es.uniovi.eii.sdm.favmovies;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.uniovi.eii.sdm.favmovies.model.Category;
import es.uniovi.eii.sdm.favmovies.model.Movie;

public class MainRecycler extends AppCompatActivity {

	public static final String MOVIE_SELECTED = "movie_selected";
	public static final String NEW_MOVIE = "new_movie";

	public static final int MANAGE_NEW_MOVIE = 1;

	private List<Movie> movieList;
	private RecyclerView rvMovies;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_recycler);
		loadMovies();

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
				Intent intent = new Intent(MainRecycler.this, NewMovie.class);
				startActivity(intent);
			}
		});
		fab.setEnabled(false);
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
					Objects.requireNonNull(rvMovies.getAdapter()).notifyItemInserted(movieList.size());
				}
			}
			if (resultCode == RESULT_CANCELED) {
				Snackbar.make(findViewById(R.id.mainLayout), R.string.msg_canceled_action, Snackbar.LENGTH_LONG).show();
			}
		}
	}

	private void clickOnItem(Movie item) {
		Intent intent = new Intent(MainRecycler.this, ShowMovie.class);
		intent.putExtra(MOVIE_SELECTED, item);
		startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
	}

	protected void loadMovies() {
		String defaultCover = "https://image.tmdb.org/t/p/original/jnFCk7qGGWop2DgfnJXeKLZFuBq.jpg\n";
		String defaultBg = "https://image.tmdb.org/t/p/original/xJWPZIYOEFIjZpBL7SVBGnzRYXp.jpg\n";
		String defaultTrailer = "https://www.youtube.com/watch?v=lpEJVgysiWs\n";
		Movie movie;
		movieList = new ArrayList<>();
		InputStream file = null;
		InputStreamReader reader = null;
		BufferedReader bufferedReader = null;
		try {
			file = getAssets().open("movie_list_url_utf8.csv");
			reader = new InputStreamReader(file);
			bufferedReader = new BufferedReader(reader);

			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				String[] data = line.split(";");
				if (data.length >= 5) {
					if (data.length == 8) {
						movie = new Movie(data[0], data[1], new Category(data[2], ""), data[3], data[4],
								data[5], data[6], data[7]);
					} else {
						movie = new Movie(data[0], data[1], new Category(data[2], ""), data[3], data[4],
								defaultCover, defaultBg, defaultTrailer);
					}
					Log.d("loadMovies", movie.toString());
					movieList.add(movie);
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

}