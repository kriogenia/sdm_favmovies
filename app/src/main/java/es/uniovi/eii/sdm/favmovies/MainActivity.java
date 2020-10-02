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

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import es.uniovi.eii.sdm.favmovies.model.Category;

public class MainActivity extends AppCompatActivity {

    public static final String SELECTED_CATEGORY_POSITION = "sel_cat_pos";
    public static final String SELECTED_CATEGORY = "sel_cat";
    public static final String MODIFIED_CATEGORY = "mod_cat";

    private static final int MANAGE_CATEGORY = 1;

    private ArrayList<Category> categories;
    private Snackbar msgCreateCategory;
    private Spinner spinner;

    private boolean creatingCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.app_name);

        categories = new ArrayList<>();
        categories.add(new Category("Acción", "Explosiones, luchas, vehículos, armas..."));
        categories.add(new Category("Romance", "Historias de amor"));

        // Spinner inicialization
        spinner = (Spinner) findViewById(R.id.spinnerCategory);
        introListSpinner(spinner, categories);

        // Define the button observer
        Button btnSave = (Button) findViewById(R.id.buttonSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(findViewById(R.id.mainLayout), validateFields(), Snackbar.LENGTH_LONG).show();
            }
        });

        // Category button OnClick event
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MANAGE_CATEGORY) {
            if (resultCode == RESULT_OK) {
                final Category category = data.getParcelableExtra(MODIFIED_CATEGORY);
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
        EditText title = findViewById(R.id.editTextTitle);
        if (title.getText().length() == 0) {
            return R.string.invalid_title;
        }
        EditText argument = findViewById(R.id.editTextMultiArgument);
        if (argument.getText().length() < 20) {
            return R.string.invalid_argument;
        }
        EditText duration = findViewById(R.id.editTextDuration);
        try {
            if (Integer.parseInt(duration.getText().toString()) == 0)
                return R.string.invalid_duration;
        } catch (NumberFormatException e) {
            return R.string.invalid_duration;
        }
        EditText date = findViewById(R.id.editTextDate);
        if (date.getText().length() < 5) {
            return R.string.invalid_date;
        }
        return R.string.saveMessage;
    }

    private void modifyCategory() {
        Intent categoryIntent = new Intent(MainActivity.this, CategoryActivity.class);
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
        names.add("Sin definir");
        for (Category c : list) {
            names.add((c.getName()));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}