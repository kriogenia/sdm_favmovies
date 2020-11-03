package es.uniovi.eii.sdm.favmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import es.uniovi.eii.sdm.favmovies.model.Movie;
import es.uniovi.eii.sdm.favmovies.ui.ActorsFragment;
import es.uniovi.eii.sdm.favmovies.ui.InfoFragment;
import es.uniovi.eii.sdm.favmovies.ui.PlotFragment;
import es.uniovi.eii.sdm.favmovies.util.AppConnection;

public class ShowMovieActivity extends AppCompatActivity {

	private Movie movie;

	private ImageView backgroundImage;
	private CollapsingToolbarLayout toolbarLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_movie);

		Intent intentMovie = getIntent();
		movie = intentMovie.getParcelableExtra(MainRecycler.MOVIE_SELECTED);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
		toolbarLayout.setTitle(getTitle());

		BottomNavigationView botNavView = findViewById(R.id.nav_view);
		botNavView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

		backgroundImage = findViewById(R.id.backgroundImage);

		if (movie != null) {
			showData(movie);
		}

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showTrailer(movie.getUrlTrailer());
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_show_movie, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		/*if (id == R.id.action_settings)
			return true;*/
		if (id == R.id.share) {
			AppConnection connection = new AppConnection(getApplicationContext());
			if (connection.checkConnection()) {
				shareMovie();
				return true;
			} else {
				Toast.makeText(getApplicationContext(), R.string.no_connection, Toast.LENGTH_LONG).show();
			}
		}
		return super.onOptionsItemSelected(item);
	}

	private void shareMovie() {
		Intent itSend = new Intent(Intent.ACTION_SEND);
		itSend.setType("text/plain");
		itSend.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_share) + " " + movie.getTitle());
		itSend.putExtra(Intent.EXTRA_TEXT, getString(R.string.title)
				+ ": " + movie.getTitle() + "\n" +
				getString(R.string.content)
				+ ": " + movie.getArgument());
		Intent shareIntent = Intent.createChooser(itSend, null);
		startActivity(shareIntent);
	}

	public void showData(Movie movie) {
		if (!movie.getTitle().isEmpty()) {
			String dateMovie = movie.getDate();
			toolbarLayout.setTitle(movie.getTitle() + " (" + dateMovie.substring(dateMovie.lastIndexOf("/") + 1) + ")");
			Picasso.get().load(movie.getUrlBackground()).into(backgroundImage);
			launchFragmentInfo();
		}
	}

	private void showTrailer(String url) {
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
	}

	private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
			= new BottomNavigationView.OnNavigationItemSelectedListener() {
		@Override
		public boolean onNavigationItemSelected(@NonNull MenuItem item) {
			if (movie != null) {
				switch (item.getItemId()) {
					case R.id.navigation_info:
						launchFragmentInfo();
						return true;
					case R.id.navigation_actors:
						launchFragmentActors();
						return true;
					case R.id.navigation_plot:
						launchFragmentPlot();
						return true;
				}
			}
			return false;
		}
	};

	private void launchFragmentInfo() {
		InfoFragment info = new InfoFragment();
		Bundle args = new Bundle();
		args.putString(InfoFragment.PREMIERE, movie.getDate());
		args.putString(InfoFragment.DURATION, movie.getDuration());
		args.putString(InfoFragment.COVER, movie.getUrlCover());
		info.setArguments(args);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, info).commit();
	}

	private void launchFragmentActors() {
		ActorsFragment actors = new ActorsFragment();
		Bundle args = new Bundle();
		args.putInt(ActorsFragment.MOVIE_ID, movie.getId());
		actors.setArguments(args);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, actors).commit();
	}

	private void launchFragmentPlot() {
		PlotFragment plot = new PlotFragment();
		Bundle args = new Bundle();
		args.putString(PlotFragment.PLOT, movie.getArgument());
		plot.setArguments(args);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, plot).commit();
	}
}