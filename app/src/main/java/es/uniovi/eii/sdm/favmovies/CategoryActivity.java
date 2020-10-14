package es.uniovi.eii.sdm.favmovies;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import es.uniovi.eii.sdm.favmovies.model.Category;

public class CategoryActivity extends AppCompatActivity {

    private Category category;

    private EditText nameText;
    private EditText descText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        nameText = findViewById(R.id.editTextCategoryName);
        descText = findViewById(R.id.editTextCategoryDesc);

        Intent intent = getIntent();
        int creation = intent.getIntExtra(MainActivity.SELECTED_CATEGORY_POSITION, -1);
        category = null;
        if (creation > 0) {
            TextView nameTitleText = findViewById(R.id.textCategoryName);
            nameTitleText.setText(R.string.ModificateCategory);
            category = intent.getParcelableExtra(MainActivity.SELECTED_CATEGORY);
            assert category != null;
            nameText.setText(category.getName());
            nameText.setEnabled(false);
            descText.setText(category.getDescription());
        }

        Button btnOk = findViewById(R.id.buttonOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category catOut = new Category(nameText.getText().toString(), descText.getText().toString());
                Intent intent = new Intent();
                intent.putExtra(MainActivity.MODIFIED_CATEGORY, catOut);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        Button btnCancel = findViewById(R.id.buttonCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }
}