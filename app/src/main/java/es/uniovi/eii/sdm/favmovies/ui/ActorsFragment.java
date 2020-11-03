package es.uniovi.eii.sdm.favmovies.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import es.uniovi.eii.sdm.favmovies.R;
import es.uniovi.eii.sdm.favmovies.data.ActorsDataSource;
import es.uniovi.eii.sdm.favmovies.model.Actor;

public class ActorsFragment extends Fragment {

	public static final String MOVIE_ID = "movie_id";

	public ActorsFragment() {}

	public View onCreateView(@NonNull LayoutInflater inflater,
							 ViewGroup container, Bundle savedInstanceState){

		View root = inflater.inflate(R.layout.fragment_actors, container, false);

		final RecyclerView recycleActors = root.findViewById(R.id.recyclerViewActors);
		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(root.getContext());
		recycleActors.setLayoutManager(layoutManager);
		recycleActors.setHasFixedSize(true);

		Bundle args = getArguments();
		assert args != null;
		int movieId = args.getInt(MOVIE_ID);

		ActorsDataSource actorsDataSource = new ActorsDataSource(root.getContext());
		actorsDataSource.open();
		List<Actor> actorList = actorsDataSource.starredActors(movieId);
		actorsDataSource.close();

		ActorListAdapter laAdapter = new ActorListAdapter(actorList, new ActorListAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(Actor actor) {
				ActorsFragment.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(actor.getUrlImdb())));
			}
		});
		recycleActors.setAdapter(laAdapter);

		return root;
	}
}