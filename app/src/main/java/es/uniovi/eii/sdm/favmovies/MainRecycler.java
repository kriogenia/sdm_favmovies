package es.uniovi.eii.sdm.favmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import es.uniovi.eii.sdm.favmovies.model.Category;
import es.uniovi.eii.sdm.favmovies.model.Movie;

public class MainRecycler extends AppCompatActivity {

    public static final String MOVIE_SELECTED = "movie_selected";
    private List<Movie> movieList;
    private RecyclerView rvMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recycler);

        fillList();

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
    }

    private void fillList() {
        movieList = new ArrayList<>();
        Category category = new Category ("Acción", "PelisAccion");
        Movie movie= new Movie("Tenet","Una acción épica que gira en torno al espionaje internacional, los viajes en el tiempo y la evolución, en la que un agente secreto debe prevenir la Tercera Guerra Mundial.",
                category,"150","26/8/2020");
        movieList.add(movie);
    }

    private void clickOnItem(Movie item) {
        Intent intent = new Intent(MainRecycler.this, MainActivity.class);
        intent.putExtra(MOVIE_SELECTED, item);
        startActivity(intent);
    }
}