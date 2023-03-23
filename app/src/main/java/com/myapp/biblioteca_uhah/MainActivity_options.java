package com.myapp.biblioteca_uhah;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity_options extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_options);

        // Declaracion de variable
        Button log_out, register_user, user_list, register_book, book_list, register_rent, rent_list;

        // Declaracion de datos
        log_out = findViewById(R.id.btnlog_out);
        register_user = findViewById(R.id.btnregister_user);
        register_book = findViewById(R.id.btnregister_book);
        register_rent = findViewById(R.id.btnregister_rent);
        user_list = findViewById(R.id.btnuser_list);
        book_list = findViewById(R.id.btnbook_list);
        rent_list = findViewById(R.id.btnrent_list);

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        register_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity_user.class);

                startActivity(intent);

            }
        });

        register_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity_book.class);

                startActivity(intent);

            }
        });

        register_rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity_rent.class);

                startActivity(intent);

            }
        });

        user_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity_user_list.class);

                startActivity(intent);

            }
        });

        book_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity_book_list.class);

                startActivity(intent);

            }
        });

        rent_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity_rent_list.class);

                startActivity(intent);

            }
        });

    }
}