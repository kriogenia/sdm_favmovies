package es.uniovi.eii.sdm.favmovies;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import es.uniovi.eii.sdm.favmovies.model.Category;
import es.uniovi.eii.sdm.favmovies.model.Movie;

public class NewMovieActivity extends AppCompatActivity {

	public static final String SELECTED_CATEGORY_POSITION = "sel_cat_pos";
	public static final String SELECTED_CATEGORY = "sel_cat";
	public static final String MODIFIED_CATEGORY = "mod_cat";

	private static final int MANAGE_CATEGORY = 1;

	private ArrayList<Category> categories;
	private Snackbar msgCreateCategory;
	private Spinner spinner;

	private EditText title;
	private EditText argument;
	private EditText duration;
	private EditText date;

	private boolean creatingCategory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_movie);
		setTitle(R.string.newMovie);

		categories = new ArrayList<>();
		categories.add(new Category("Action", "pium pium pium"));
		spinner = (Spinner) findViewById(R.id.spinnerCategory);
		introListSpinner(spinner, categories);

		title = findViewById(R.id.editTextTitle);
		argument = findViewById(R.id.editTextMultiArgument);
		duration = findViewById(R.id.editTextDuration);
		date = findViewById(R.id.editTextDate);

		Button btnSave = (Button) findViewById(R.id.buttonSave);
		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int validation = validateFields();
				if (validation != -1) {
					Snackbar.make(findViewById(R.id.mainLayout), validateFields(), Snackbar.LENGTH_LONG).show();
				} else {
					Movie movieOut = new Movie("0", title.getText().toString(), argument.getText().toString(), categories.get(spinner.getSelectedItemPosition()-1), duration.getText().toString(), date.getText().toString(), "", "", "");
					Intent intent = new Intent();
					intent.putExtra(MainRecycler.NEW_MOVIE, movieOut);
					setResult(RESULT_OK, intent);
					finish();
				}
			}
		});

		ImageButton btnMModifyCategory = (ImageButton) findViewById(R.id.imageButtonCategory);
		btnMModifyCategory.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Spinner spinner = (Spinner) findViewById(R.id.spinnerCategory);
				if (spinner.getSelectedItemPosition() == 0) {
					msgCreateCategory = Snackbar.make(findViewById(R.id.mainLayout), R.string.msg_create_new_category, Snackbar.LENGTH_LONG);
				} else {
					msgCreateCategory = Snackbar.make(findViewById(R.id.mainLayout), R.string.msg_create_modify_category, Snackbar.LENGTH_LONG);
				}

				msgCreateCategory.setAction(android.R.string.ok, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Snackbar.make(findViewById(R.id.mainLayout), R.string.msg_completed_action, Snackbar.LENGTH_LONG).show();
						modifyCategory();
					}
				});

				msgCreateCategory.show();
			}
		});
		btnMModifyCategory.setVisibility(View.GONE);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == MANAGE_CATEGORY) {
			if (resultCode == RESULT_OK) {
				assert data != null;
				final Category category = data.getParcelableExtra(MODIFIED_CATEGORY);
				assert category != null;
				Log.d("FavMovies.MainActivity", category.toString());

				if (creatingCategory) {
					categories.add(category);
					introListSpinner(spinner, categories);
				}
				else {
					for (Category cat: categories) {
						if (cat.getName().equals(category.getName())) {
							cat.setDescription(category.getDescription());
							Log.d("FavMovies.MainActivity", "Modified description of " + cat.getName().toString());
							break;
						}
					}
				}
			}
			if (resultCode == RESULT_CANCELED) {
				Snackbar.make(findViewById(R.id.mainLayout), R.string.msg_canceled_action, Snackbar.LENGTH_LONG).show();
			}
		}
	}

	private int validateFields() {
		if (title.getText().length() == 0) {
			return R.string.invalid_title;
		}
		if (argument.getText().length() < 20) {
			return R.string.invalid_argument;
		}
		try {
			if (Integer.parseInt(duration.getText().toString()) == 0)
				return R.string.invalid_duration;
		} catch (NumberFormatException e) {
			return R.string.invalid_duration;
		}
		if (date.getText().length() < 5) {
			return R.string.invalid_date;
		}
		if (spinner.getSelectedItemPosition() == 0) {
			return R.string.select_a_category;
		}
		return -1;
	}

	private void modifyCategory() {
		Intent categoryIntent = new Intent(NewMovieActivity.this, CategoryActivity.class);
		categoryIntent.putExtra(SELECTED_CATEGORY_POSITION, spinner.getSelectedItemPosition());
		creatingCategory = true;
		if (spinner.getSelectedItemPosition() > 0) {
			creatingCategory = false;
			categoryIntent.putExtra(SELECTED_CATEGORY, categories.get(spinner.getSelectedItemPosition() - 1));
		}
		startActivityForResult(categoryIntent, MANAGE_CATEGORY);
	}

	private void introListSpinner(Spinner spinner, List<Category> list) {
		ArrayList<String> names = new ArrayList<>();
		names.add("Undefined");
		for (Category c : list) {
			names.add((c.getName()));
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}
}