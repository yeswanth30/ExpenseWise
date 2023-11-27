package com.expensetracker.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.expensetracker.models.Expense;

import java.util.ArrayList;
import java.util.List;

public class dbhelper extends SQLiteOpenHelper {
    private Context context;

    private static final String DATABASE_NAME = "expense_tracker";
    private static final int DATABASE_VERSION = 6;

    // User table
    public static final String TABLE_USER = "user";
    public static final String USER_ID = "userid";
    public static final String USER_EMAIL = "email";
    public static final String USER_PASSWORD = "password";
    public static final String USER_FULLNAME = "fullname";
    public static final String USER_TIME = "time";


    // Expense table
    public static final String TABLE_EXPENSE = "expenses";
    public static final String EXPENSE_ID = "expenseid";
    public static final String EXPENSE_USER_ID = "userid";
    public static final String EXPENSE_CATEGORY_NAME = "categoryname";
    public static final String EXPENSE_AMOUNT = "amount";
    public static final String EXPENSE_DATE = "expensedate";
    public static final String EXPENSE_TIME = "time1";

    //create statement for user
    private static final String CREATE_TABLE_USER =
            "CREATE TABLE " + TABLE_USER + " (" +
                    USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    USER_EMAIL + " TEXT, " +
                    USER_PASSWORD + " TEXT, " +
                    USER_FULLNAME + " TEXT, " +
                    USER_TIME + " TEXT)";

    private static final String CREATE_TABLE_EXPENSE =
            "CREATE TABLE " + TABLE_EXPENSE + "("+
                    EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    EXPENSE_USER_ID + " TEXT, " +
                    EXPENSE_CATEGORY_NAME + " TEXT, " +
                    EXPENSE_DATE + " TEXT," +
                    EXPENSE_AMOUNT + " TEXT," +
                    EXPENSE_TIME + " TEXT)";


    public dbhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_EXPENSE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);
        onCreate(db);

    }
    public void register(String fullname, String email, String password, String formats) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_FULLNAME, fullname);
        values.put(USER_EMAIL, email);
        values.put(USER_PASSWORD, password);
        values.put(USER_TIME,formats);
        db.insert(TABLE_USER, null, values);
        db.close();
    }
    public String login(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String userRole = null;
        String sql = "SELECT * FROM user WHERE email = '" + email + "' AND password = '" + password + "' ;";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            userRole = cursor.getString(cursor.getColumnIndexOrThrow("userid"));
        }
        return userRole;
    }

    public long insertExpense(String category, double amount, String date, Context context,String formats) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        SharedPreferences sharedpreferences = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        String userid = sharedpreferences.getString("userid", "");

        values.put(dbhelper.EXPENSE_USER_ID, userid);

        values.put(dbhelper.EXPENSE_CATEGORY_NAME, category);
        values.put(dbhelper.EXPENSE_AMOUNT, amount);
        values.put(dbhelper.EXPENSE_DATE, date);
        values.put(EXPENSE_TIME,formats);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(dbhelper.TABLE_EXPENSE, null, values);
        db.close();
        return newRowId;
    }



    public List<Expense> getAllExpenses() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Expense> expenses = new ArrayList<>();

        String[] projection = {
                EXPENSE_CATEGORY_NAME,
                EXPENSE_AMOUNT,
                EXPENSE_DATE };

        Cursor cursor = db.query(
                TABLE_EXPENSE,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String category = cursor.getString(cursor.getColumnIndexOrThrow(EXPENSE_CATEGORY_NAME));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(EXPENSE_AMOUNT));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(EXPENSE_DATE));

            Expense expense = new Expense(category, amount, date);
            expenses.add(expense);

            cursor.moveToNext();
        }

        cursor.close();
        db.close();
        return expenses;
    }
    public List<Expense> getAllExpenseData(String id) {
        String sql = " SELECT * FROM expenses WHERE userid = '"+ id +"' ;";

        SQLiteDatabase SQLiteDatabase = this.getReadableDatabase();
        List<Expense> storeexpense = new ArrayList<>();
        Cursor cursor = SQLiteDatabase.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
               // String expense = cursor.getString(cursor.getColumnIndexOrThrow("expense"));
                double amount = Double.parseDouble(cursor.getString(4));
                String category = cursor.getString(2);
                String date = cursor.getString(3);

                storeexpense.add(new Expense(  category,amount, date));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return storeexpense;
    }
    public double getTotalExpenses() {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalExpenses = 0;

        Cursor cursor = db.rawQuery("SELECT SUM(" + EXPENSE_AMOUNT + ") FROM " + TABLE_EXPENSE, null);
        if (cursor.moveToFirst()) {
            totalExpenses = cursor.getDouble(0);
        }

        cursor.close();
        db.close();
        return totalExpenses;
    }

    public double getDailyTotalExpenses(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        double dailyTotalExpenses = 0;

        Cursor cursor = db.rawQuery("SELECT SUM(" + EXPENSE_AMOUNT + ") FROM " + TABLE_EXPENSE +
                " WHERE " + EXPENSE_DATE + " = ?", new String[]{date});
        if (cursor.moveToFirst()) {
            dailyTotalExpenses = cursor.getDouble(0);
        }

        cursor.close();
        db.close();
        return dailyTotalExpenses;
    }



    public double getMonthlyTotalExpenses(String year, String month) {
        SQLiteDatabase db = this.getReadableDatabase();
        double monthlyTotalExpenses = 0;

        // Ensure that month and year are correctly formatted for SQLite strftime
        String formattedMonth = String.format("%02d", Integer.parseInt(month));
        String formattedYear = String.valueOf(year);

        Cursor cursor = db.rawQuery("SELECT SUM(" + EXPENSE_AMOUNT + ") FROM " + TABLE_EXPENSE +
                        " WHERE strftime('%m-%Y', " + EXPENSE_DATE + ") = ?",
                new String[]{formattedMonth + "-" + formattedYear});

        if (cursor.moveToFirst()) {
            monthlyTotalExpenses = cursor.getDouble(0);
        }

        cursor.close();
        db.close();
        return monthlyTotalExpenses;
    }

}




