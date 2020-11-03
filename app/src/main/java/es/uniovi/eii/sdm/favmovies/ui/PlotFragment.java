package es.uniovi.eii.sdm.favmovies.ui;

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
public class PlotFragment extends Fragment {

	public static final String PLOT = "plot";

	public PlotFragment() {	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_plot, container, false);

		final TextView tvPlot = root.findViewById(R.id.tvPlot);

		Bundle args = getArguments();
		if(args != null) {
			tvPlot.setText(args.getString(PLOT));
		}
		return root;
	}
}