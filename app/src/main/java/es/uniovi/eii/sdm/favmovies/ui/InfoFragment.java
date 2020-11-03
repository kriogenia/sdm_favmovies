package es.uniovi.eii.sdm.favmovies.ui;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import es.uniovi.eii.sdm.favmovies.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class InfoFragment extends Fragment {

	public static final String PREMIERE = "premiere";
	public static final String DURATION = "duration";
	public static final String COVER = "cover";

	public InfoFragment() {	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_info, container, false);

		final TextView tvDuration = root.findViewById(R.id.tvDuration);
		final TextView tvDate = root.findViewById(R.id.tvDate);
		ImageView ivCover = root.findViewById(R.id.coverView);

		Bundle args = getArguments();
		if(args != null) {
			tvDate.setText(args.getString(PREMIERE));
			tvDuration.setText(args.getString(DURATION));
			Picasso.get().
					load(args.getString(COVER)).into(ivCover);
		}

		return root;
	}
}