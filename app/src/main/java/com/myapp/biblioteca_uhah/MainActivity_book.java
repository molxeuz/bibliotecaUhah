package com.myapp.biblioteca_uhah;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity_book extends AppCompatActivity {

    // Declaracion de las variables
    Button log_out, book_list, go_back, save_book, edit_book, delete_book, search_book;
    EditText identification_book, name_book, price_book;
    Switch availability_book;

    // Instanciar la clase DbLibrary para los difierentes botones
    DbLibrary Library_book = new DbLibrary(this, "dblibrary6", null, 1);

    String old_identification_book = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_book);

        // Declaracion de los datos
        log_out = findViewById(R.id.btnlog_out);
        book_list = findViewById(R.id.btnbook_list);
        go_back = findViewById(R.id.btngo_back);

        save_book = findViewById(R.id.btnsave_book);
        edit_book = findViewById(R.id.btnedit_book);
        delete_book = findViewById(R.id.btndelete_book);
        search_book = findViewById(R.id.btnsearch_book);
        log_out = findViewById(R.id.btnlog_out);
        book_list = findViewById(R.id.btnbook_list);
        go_back = findViewById(R.id.btngo_back);
        identification_book = findViewById(R.id.etidentification_book);
        name_book = findViewById(R.id.etname_book);
        price_book = findViewById(R.id.etprice_book);
        availability_book = findViewById(R.id.swavailability_book);

        // botones desactivados
        delete_book.setEnabled(false);
        edit_book.setEnabled(false);

        // Metodo Onclick para guardar libro
        save_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!identification_book.getText().toString().isEmpty() && !name_book.getText().toString().isEmpty() && !price_book.getText().toString().isEmpty()){

                    // Buscar la identificacion en la tabla book
                    SQLiteDatabase sdLibraryread = Library_book.getReadableDatabase();

                    String query = "SELECT identification_book FROM Book WHERE identification_book = '" + identification_book.getText().toString() + "'";

                    Cursor cBook = sdLibraryread.rawQuery(query, null);

                    if (!cBook.moveToFirst()){ // No encuentra la identificacion del libro ingresado

                        // Instanciar la clase de SQLiteDatabase en modo escritura
                        SQLiteDatabase sdLibrary = Library_book.getWritableDatabase();

                        // Contenedor de valores
                        ContentValues cvBook = new ContentValues();

                        cvBook.put("identification_book", identification_book.getText().toString());
                        cvBook.put("name_book", name_book.getText().toString());
                        cvBook.put("book_price", price_book.getText().toString());
                        cvBook.put("availability_book", availability_book.isChecked() ? true : false);

                        sdLibrary.insert("Book",null, cvBook);

                        sdLibrary.close();

                        delete_book.setEnabled(false);
                        edit_book.setEnabled(false);
                        search_book.setEnabled(true);

                        Limpiar_campos();

                        Toast.makeText(getApplicationContext(),"Book saved successfully!",Toast.LENGTH_SHORT).show();

                    }else{

                        Toast.makeText(getApplicationContext(),"The book is already saved, try another one!",Toast.LENGTH_SHORT).show();

                    }

                }else{

                    Toast.makeText(getApplicationContext(),"You must fill all the fields to save!",Toast.LENGTH_SHORT).show();

                }

            }

        });

        // Metodo Onclick para buscar libro
        search_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase sdLibraryread = Library_book.getReadableDatabase();

                String query = "SELECT name_book, book_price, availability_book FROM Book WHERE identification_book = '" + identification_book.getText().toString() + "'";

                Cursor cursorLibro = sdLibraryread.rawQuery(query, null);

                if (cursorLibro.moveToFirst()) {

                    name_book.setText(cursorLibro.getString(0));
                    price_book.setText(cursorLibro.getString(1));

                    availability_book.setChecked(cursorLibro.getInt(2) == 1 ? true : false);

                    delete_book.setEnabled(true);
                    edit_book.setEnabled(true);

                    old_identification_book = identification_book.getText().toString();

                    sdLibraryread.close();

                    Toast.makeText(getApplicationContext(), "Book found successfully!", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(getApplicationContext(), "Book wasn't found!", Toast.LENGTH_SHORT).show();

                }

            }

        });

        // Metodo Onclick para borrar libro
        delete_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!identification_book.getText().toString().isEmpty() && !name_book.getText().toString().isEmpty() && !price_book.getText().toString().isEmpty()) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity_book.this);

                    alertDialogBuilder.setMessage("You are about to delete the book!");

                    alertDialogBuilder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            SQLiteDatabase obde = Library_book.getWritableDatabase();

                            obde.execSQL("DELETE FROM Book WHERE identification_book = '"+identification_book.getText().toString()+"'");

                            Toast.makeText(getApplicationContext(),"Book deleted successfully!",Toast.LENGTH_SHORT).show();

                            Limpiar_campos();

                        }

                    });

                    alertDialogBuilder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            Toast.makeText(getApplicationContext(),"Book wasn't deleted", Toast.LENGTH_SHORT).show();

                        }

                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();

                    alertDialog.show();

                } else {

                    Toast.makeText(getApplicationContext(),"You must fill all the fields to delete!", Toast.LENGTH_SHORT).show();

                }

            }

        });

        // Metodo Onclick para editar un usuario
        edit_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase sdLibrary = Library_book.getWritableDatabase();

                if (identification_book.getText().toString().equals(old_identification_book)){

                    sdLibrary.execSQL("UPDATE Book SET name_book = '" + name_book.getText().toString() + "', book_price = '" + price_book.getText().toString() + "" + "', availability_book = " + (availability_book.isChecked() ? 1 : 0) + " WHERE identification_book = '" + old_identification_book + "'");

                    Toast.makeText(getApplicationContext(),"Book edited successfully!",Toast.LENGTH_SHORT).show();

                } else {

                    SQLiteDatabase sdLibraryread = Library_book.getReadableDatabase();

                    String query = "SELECT identification_book FROM Book WHERE identification_book = '" + identification_book.getText().toString() + "'";

                    Cursor cBook = sdLibraryread.rawQuery(query, null);

                    if (!cBook.moveToFirst()) {

                        sdLibrary.execSQL("UPDATE Book SET identification_book = '" + identification_book.getText().toString() + "', name_book = '" + name_book.getText().toString() +  "', book_price = '" + price_book.getText().toString() + "" + "', availability_book = " + (availability_book.isChecked() ? 1 : 0) + " WHERE identification_book = '" + old_identification_book + "'");

                        Toast.makeText(getApplicationContext(),"Book edited successfully!",Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(getApplicationContext(),"The new identification is already assigned to an identification!",Toast.LENGTH_SHORT).show();

                    }

                }

            }

        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(intent);

            }

        });

        book_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity_book_list.class);

                startActivity(intent);

                Toast.makeText(getApplicationContext() ,"Welcome to the books list!", Toast.LENGTH_SHORT).show();

            }

        });

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }

        });

    }

    // Funcion para limpiar campos en registro de usuario
    private void Limpiar_campos(){

        identification_book.setText("");
        name_book.setText("");
        price_book.setText("");
        availability_book.setChecked(false);
        identification_book.requestFocus();

    }

}