package com.myapp.biblioteca_uhah;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity_book_list extends AppCompatActivity {

    // Declaracion de las variables
    Button log_out, register_book, go_back;
    ListView listBooks;

    ArrayList<String> listado_book;

    // Cargar los usuarios en el arrayList arrUsers
    DbLibrary Library_book = new DbLibrary(this, "dblibrary6", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_book_list);

        // Declaracion de los datos
        log_out = findViewById(R.id.btnlog_out);
        register_book = findViewById(R.id.btnregister_book);
        go_back = findViewById(R.id.btngo_back);

        listBooks = findViewById(R.id.lvBooks);

        CargarListado();

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

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

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }

        });

    }

    private void CargarListado(){

        listado_book = ListaLibros();

        // Generar el adaptador que pasara los datos al ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, listado_book);

        listBooks.setAdapter(adapter);

    }

    private ArrayList<String> ListaLibros() {

        ArrayList<String> datos = new ArrayList<String>();

        SQLiteDatabase sdlibraryread = Library_book.getReadableDatabase();

        String qAllBooks = "SELECT identification_book, name_book, book_price, availability_book FROM Book";

        Cursor cBooks = sdlibraryread.rawQuery(qAllBooks, null);

        if (cBooks.moveToFirst()) {

            do {

                // Generar un string para almacenar toda la informacion de cada libros y guardarlo en el arrayList
                String mAvailability = cBooks.getInt(3) == 0 ? "Unavailable" : "Available";

                String recBooks = " |    " + cBooks.getString(0) + "   |   " + cBooks.getString(1) + "   |   " + mAvailability + "   | ";
                datos.add(recBooks);

            } while (cBooks.moveToNext());

        }

        sdlibraryread.close();
        return datos;

    }

}