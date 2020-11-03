package es.uniovi.eii.sdm.favmovies.ui;

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

import es.uniovi.eii.sdm.favmovies.R;
import es.uniovi.eii.sdm.favmovies.model.Actor;

public class ActorListAdapter extends RecyclerView.Adapter<ActorListAdapter.ActorViewHolder> {
	private List<Actor> actorList;
	private final ActorListAdapter.OnItemClickListener listener;

	public ActorListAdapter(List<Actor> actorList, OnItemClickListener listener) {
		this.actorList = actorList;
		this.listener = listener;
	}

	@NonNull
	@Override
	public ActorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.actor_recycler_view_item, parent, false);
		return new ActorListAdapter.ActorViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(@NonNull ActorViewHolder holder, int position) {
		Actor actor = actorList.get(position);
		Log.i("Actor List","Showing: "+actor);
		holder.bindUser(actor, listener);
	}

	@Override
	public int getItemCount() {
		return actorList.size();
	}

	/**************************************************************************************************/
	public interface OnItemClickListener{
		void onItemClick(Actor item);
	}

	public static class ActorViewHolder extends RecyclerView.ViewHolder{
		private TextView actorName;
		private TextView characterName;
		private ImageView characterImage;

		public ActorViewHolder(View itemView) {
			super(itemView);
			this.actorName = itemView.findViewById(R.id.actor_name);
			this.characterName = itemView.findViewById(R.id.character_name);
			this.characterImage = itemView.findViewById(R.id.actor_image);
		}

		public void bindUser(final Actor actor, final OnItemClickListener listener){
			actorName.setText(actor.getName());
			characterName.setText(actor.getCharacter());
			Picasso.get().load(actor.getImage()).into(characterImage);

			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onItemClick(actor);
				}
			});
		}
	}
}
