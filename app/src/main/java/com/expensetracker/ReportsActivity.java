package com.expensetracker;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.expensetracker.dbhelper.dbhelper;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class ReportsActivity extends AppCompatActivity {
    private dbhelper dbHelper;
    private TextView totalExpensesTextView;
    private Button selectDateButton, selectMonthButton;
    private String selectedDate, selectedMonth;
    private SimpleDateFormat indianDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reports);

        dbHelper = new dbhelper(this);
        totalExpensesTextView = findViewById(R.id.totalExpensesTextView);
        selectDateButton = findViewById(R.id.selectDateButton);
        selectMonthButton = findViewById(R.id.selectMonthButton);

        indianDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        indianDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show a date picker dialog
                showDatePickerDialog();
            }
        });

        selectMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show a month picker dialog
                showMonthPickerDialog();
            }
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        calendar.set(selectedYear, selectedMonth, selectedDay);
                        selectedDate = indianDateFormat.format(calendar.getTime());

                        double totalExpenses = dbHelper.getDailyTotalExpenses(selectedDate);
                        totalExpensesTextView.setText("Total Expenses: " + totalExpenses);
                    }
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }

    private void showMonthPickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(selectedYear, selectedMonth, 1);

                        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM", Locale.US);
                        monthFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

                        String selectedMonths = monthFormat.format(selectedCalendar.getTime());

                        double totalExpenses = dbHelper.getMonthlyTotalExpenses(selectedMonths, String.valueOf(selectedYear));
                        totalExpensesTextView.setText("Total Expenses: $" + totalExpenses);
                    }
                },
                year,
                month,
                1
        );

        datePickerDialog.show();
    }

}