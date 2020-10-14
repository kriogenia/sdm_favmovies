package es.uniovi.eii.sdm.favmovies;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import es.uniovi.eii.sdm.favmovies.model.Movie;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Movie item);
    }

    private List<Movie> listMovies;
    private final OnItemClickListener listener;

    public MovieListAdapter(List<Movie> movies, OnItemClickListener listener) {
        this.listMovies = movies;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_recycler_view_item, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = listMovies.get(position);
        Log.i("List", movie.toString());
        holder.bindUser(movie, listener);
    }

    @Override
    public int getItemCount() {
        return listMovies.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView date;
        private ImageView image;

        public MovieViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvMovieName);
            date = itemView.findViewById(R.id.tvMovieDate);
            image = itemView.findViewById(R.id.ivMovie);
        }

        public void bindUser(final Movie movie, final OnItemClickListener listener) {
            title.setText(movie.getTitle());
            date.setText(movie.getDate());
            Picasso.get().load(movie.getUrlCover()).into(image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Listener", movie.getTitle());
                    listener.onItemClick(movie);
                }
            });
        }
    }

}
