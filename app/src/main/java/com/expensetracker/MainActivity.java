package com.expensetracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.expensetracker.adapters.ExpenseAdapter;
import com.expensetracker.dbhelper.dbhelper;
import com.expensetracker.models.Expense;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.expensetracker.databinding.ActivityMainBinding;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private ExpenseAdapter expenseAdapter;  // Declaration in the class

    dbhelper dbhelper;
    SQLiteDatabase database;
    Button buttonInsert,Total;

    String userid;
    ImageView logout;
    RecyclerView recyclerView;
    private TextView amountTextView;
    private SeekBar amountSeekBar;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonInsert = findViewById(R.id.buttonInsert);
        logout = findViewById(R.id.logoutButton);
        Total = findViewById(R.id.Total);
        amountTextView = findViewById(R.id.amountTextView1);
        amountSeekBar = findViewById(R.id.amountSeekBar);

        amountSeekBar.setMax(100000);




        dbhelper = new dbhelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SharedPreferences sharedpreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        String userid = sharedpreferences.getString("userid","");
        List<Expense> expenses = dbhelper.getAllExpenseData(userid);
        expenseAdapter = new ExpenseAdapter(expenses);
        recyclerView.setAdapter(expenseAdapter);

        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(MainActivity.this, insert.class);
                startActivity(intent3);
            }

        });
        Total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(MainActivity.this, ReportsActivity.class);
                startActivity(intent3);
            }

        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedpreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.apply();
                Intent intent34 = new Intent(MainActivity.this, login.class);
                startActivity(intent34);
                finish();
            }
        });
        amountSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateAmountText(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not needed for your case
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Not needed for your case
            }
        });
    }

    private void updateAmountText(int progress) {
        amountTextView.setText(String.format("Amount: %d rs", progress));
    }
    }
