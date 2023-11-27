package com.expensetracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import com.expensetracker.dbhelper.dbhelper;

public class login extends AppCompatActivity {
    EditText text1,text2;

    Button login;
    dbhelper dbhelper;
    TextView register;
    String name1,password1;
    private AppBarConfiguration mAppBarConfiguration;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        text1 =findViewById(R.id.text1);
        text2 =findViewById(R.id.text2);
        login=findViewById(R.id.login);
        register=findViewById(R.id.register);

        dbhelper = new dbhelper(login.this);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name1 = text1.getText().toString();
                password1= text2.getText().toString();
                if(name1.isEmpty() && password1.isEmpty()){
                    Toast.makeText(login.this, "Please Enter Values", Toast.LENGTH_LONG).show();

                } else {
                    String data = "";
                    data = dbhelper.login(name1, password1);
                    if (data.isEmpty()) {
                        Toast.makeText(login.this, "username not found. Please sign up", Toast.LENGTH_LONG).show();
                    } else {
                        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
                        SharedPreferences.Editor myedit = sharedPreferences.edit();
                        myedit.putString("userid",data);
                        myedit.commit();
                        Toast.makeText(login.this , "Successfully login", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(login.this, MainActivity.class);
                        startActivity(intent);

                    }
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(login.this, register.class);
                startActivity(intent3);
            }

        });


    }


}