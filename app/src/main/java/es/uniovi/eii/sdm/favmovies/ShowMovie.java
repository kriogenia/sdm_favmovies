package es.uniovi.eii.sdm.favmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import es.uniovi.eii.sdm.favmovies.model.Movie;
import es.uniovi.eii.sdm.favmovies.util.AppConnection;

public class ShowMovie extends AppCompatActivity {

	private Movie movie;

	private ImageView backgroundImage;
	private ImageView coverImage;
	private CollapsingToolbarLayout toolbarLayout;
	private TextView category;
	private TextView date;
	private TextView duration;
	private TextView argument;

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

		backgroundImage = findViewById(R.id.backgroundImage);
		coverImage = findViewById(R.id.coverImage);
		category = findViewById(R.id.categoria);
		date = findViewById(R.id.date);
		duration = findViewById(R.id.duration);
		argument = findViewById(R.id.argument);

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
		if (id == R.id.action_settings)
			return true;
		if (id == R.id.share) {
			AppConnection connection = new AppConnection(getApplicationContext());
			if (connection.checkConnection()) {
				shareMovie();
				return true;
			} else {
				Toast.makeText(getApplicationContext(), R.string.no_connection, Toast.LENGTH_LONG).show();
			}
		}
		return false;
	}

	private void shareMovie() {
		Intent itSend = new Intent(Intent.ACTION_SEND);
		itSend.setType("text/plain");
		itSend.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_share) + " " + movie.getTitle());
		itSend.putExtra(Intent.EXTRA_TEXT, getString(R.string.title)
				+ ": " + movie.getTitle() + "\n" +
				getString(R.string.content)
				+ ": " + movie.getArgument());
		Intent shareIntent=Intent.createChooser(itSend, null);
		startActivity(shareIntent);
	}

	public void showData(Movie movie) {
		if (!movie.getTitle().isEmpty()) {
			String dateMovie = movie.getDate();
			toolbarLayout.setTitle(movie.getTitle() + " (" + dateMovie.substring(dateMovie.lastIndexOf("/") + 1) + ")");

			Picasso.get().load(movie.getUrlCover()).into(coverImage);
			Picasso.get().load(movie.getUrlBackground()).into(backgroundImage);

			category.setText(movie.getCategory().getName());
			date.setText(movie.getDate());
			duration.setText(movie.getDuration());
			argument.setText(movie.getArgument());
		}
	}

	private void showTrailer(String url) {
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
	}
}