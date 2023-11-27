package com.expensetracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.expensetracker.dbhelper.dbhelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class insert extends AppCompatActivity {
    private EditText editTextCategory, editTextAmount, editTextDate;
    private Button btnAddExpense;
    dbhelper dbhelper;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert);

        editTextCategory = findViewById(R.id.editTextCategory);
        editTextAmount = findViewById(R.id.editTextAmount);
        editTextDate = findViewById(R.id.editTextDate);
        btnAddExpense = findViewById(R.id.btnAddExpense);

        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = editTextCategory.getText().toString();
                double amount = Double.parseDouble(editTextAmount.getText().toString());
                String date = editTextDate.getText().toString();
                dbhelper = new dbhelper(insert.this);
                SharedPreferences sharedpreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
                userid = sharedpreferences.getString("userid","");
                long times = System.currentTimeMillis();
                SimpleDateFormat dates = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
                String formats = dates.format(new Date(times));

                long insertId = dbhelper.insertExpense(category, amount, date, getApplicationContext(), formats);


                if (insertId != -1) {
                    Toast.makeText(insert.this, "Expense added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(insert.this, "Error adding expense", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}


