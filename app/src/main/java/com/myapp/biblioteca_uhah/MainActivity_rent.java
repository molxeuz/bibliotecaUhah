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

public class MainActivity_rent extends AppCompatActivity {

    // Declaracion de las variables
    Button log_out, rent_list, go_back, save_rent, edit_rent, delete_rent, search_rent;
    EditText identification_rent, identification_book, identification_user, date_rent;

    // Instanciar la clase DbLibrary para los difierentes botones
    DbLibrary Library_rent = new DbLibrary(this, "dblibrary6", null, 1);

    String old_identification_rent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_rent);

        // Declaracion de los datos
        save_rent = findViewById(R.id.btnsave_rent);
        edit_rent = findViewById(R.id.btnedit_rent);
        delete_rent = findViewById(R.id.btndelete_rent);
        search_rent = findViewById(R.id.btnsearch_rent);
        log_out = findViewById(R.id.btnlog_out);
        rent_list = findViewById(R.id.btnrent_list);
        go_back = findViewById(R.id.btngo_back);
        identification_rent = findViewById(R.id.etidentification_rent);
        identification_user = findViewById(R.id.etidentification_user);
        identification_book = findViewById(R.id.etidentification_book);
        date_rent = findViewById(R.id.etdate_rent);

        // Recibir los datos desde usuario y libro para hacer la comparacion
        

        // botones desactivados
        delete_rent.setEnabled(false);
        edit_rent.setEnabled(false);

        // Metodo Onclick para guardar usuario
        save_rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!identification_rent.getText().toString().isEmpty() && !identification_user.getText().toString().isEmpty() && !identification_book.getText().toString().isEmpty() && !identification_rent.getText().toString().isEmpty()){

                    // Buscar la identificacion en la tabla renta
                    SQLiteDatabase sdLibraryread = Library_rent.getReadableDatabase();

                    String query = "SELECT identification_rent FROM Rent WHERE identification_rent = '" + identification_rent.getText().toString() + "'";

                    Cursor cRent = sdLibraryread.rawQuery(query, null);

                    if (!cRent.moveToFirst()){ // No encuentra la identificacion de la renta ingresada

                        SQLiteDatabase sdLibraryread_condicion_user = Library_rent.getReadableDatabase();

                        String query_condicion_user = "SELECT identification_user FROM User WHERE identification_user = '" + identification_user.getText().toString() + "' AND status_user = 1";

                        Cursor cRent_condicion_user = sdLibraryread_condicion_user.rawQuery(query_condicion_user, null);

                        if (cRent_condicion_user.moveToFirst()){

                            SQLiteDatabase sdLibraryread_condicion_book = Library_rent.getReadableDatabase();

                            String query_condicion_book = "SELECT identification_book FROM Book WHERE identification_book = '" + identification_book.getText().toString() + "' AND availability_book = 1";

                            Cursor cRent_condicion_book = sdLibraryread_condicion_book.rawQuery(query_condicion_book, null);

                            if (cRent_condicion_book.moveToFirst()){

                                SQLiteDatabase sdLibraryread_condicion_book_two = Library_rent.getReadableDatabase();

                                String query_condicion_book_two = "SELECT identification_book FROM Rent WHERE identification_book = '" + identification_book.getText().toString() + "'";

                                Cursor cRent_condicion_book_two = sdLibraryread_condicion_book_two.rawQuery(query_condicion_book_two, null);

                                if (!cRent_condicion_book_two.moveToFirst()){

                                    // Instanciar la clase de SQLiteDatabase en modo escritura
                                    SQLiteDatabase sdLibrary = Library_rent.getWritableDatabase();

                                    // Contenedor de valores
                                    ContentValues cvRent = new ContentValues();

                                    cvRent.put("identification_rent", identification_rent.getText().toString());
                                    cvRent.put("identification_user", identification_user.getText().toString());
                                    cvRent.put("identification_book", identification_book.getText().toString());
                                    cvRent.put("rent_date", date_rent.getText().toString());

                                    sdLibrary.insert("Rent",null, cvRent);

                                    sdLibrary.close();

                                    delete_rent.setEnabled(false);
                                    edit_rent.setEnabled(false);
                                    search_rent.setEnabled(true);

                                    Limpiar_campos();

                                    Toast.makeText(getApplicationContext(),"Rent saved successfully!",Toast.LENGTH_SHORT).show();

                                }else{

                                    Toast.makeText(getApplicationContext(),"The book is aleady borrowed!",Toast.LENGTH_SHORT).show();

                                }

                            }else{

                                Toast.makeText(getApplicationContext(),"The book is not register or unavailable!",Toast.LENGTH_SHORT).show();

                            }

                        }else{

                            Toast.makeText(getApplicationContext(),"The user is not register or inactive!",Toast.LENGTH_SHORT).show();

                        }

                    }else{

                        Toast.makeText(getApplicationContext(),"The rent is already saved, try another one!",Toast.LENGTH_SHORT).show();

                    }

                }else{

                    Toast.makeText(getApplicationContext(),"You must fill all the fields to save!",Toast.LENGTH_SHORT).show();

                }

            }

        });


        // Metodo Onclick para buscar usuario
        search_rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase sdLibraryread = Library_rent.getReadableDatabase();

                String query = "SELECT identification_user, identification_book, rent_date FROM Rent WHERE identification_rent = '" + identification_rent.getText().toString() + "'";

                Cursor cursorRenta = sdLibraryread.rawQuery(query, null);

                if (cursorRenta.moveToFirst()) {

                    identification_user.setText(cursorRenta.getString(0));
                    identification_book.setText(cursorRenta.getString(1));
                    date_rent.setText(cursorRenta.getString(2));

                    delete_rent.setEnabled(true);
                    edit_rent.setEnabled(true);

                    old_identification_rent = identification_rent.getText().toString();

                    sdLibraryread.close();

                    Toast.makeText(getApplicationContext(), "Rent found successfully!", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(getApplicationContext(), "Rent wasn't found!", Toast.LENGTH_SHORT).show();

                }

            }

        });


        // Metodo Onclick para borrar renta
        delete_rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!identification_rent.getText().toString().isEmpty() && !identification_user.getText().toString().isEmpty() && !identification_book.getText().toString().isEmpty() && !date_rent.getText().toString().isEmpty()) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity_rent.this);

                    alertDialogBuilder.setMessage("You are about to delete the rent!");

                    alertDialogBuilder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            SQLiteDatabase obde = Library_rent.getWritableDatabase();

                            obde.execSQL("DELETE FROM Rent WHERE identification_rent = '"+identification_rent.getText().toString()+"'");

                            Toast.makeText(getApplicationContext(),"Rent deleted successfully!",Toast.LENGTH_SHORT).show();

                            Limpiar_campos();

                        }

                    });

                    alertDialogBuilder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            Toast.makeText(getApplicationContext(),"Rent wasn't deleted", Toast.LENGTH_SHORT).show();

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
        edit_rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase sdLibrary = Library_rent.getWritableDatabase();

                //int newRole = user.isChecked() ? 0 : 1;
                if (identification_rent.getText().toString().equals(old_identification_rent)){

                    sdLibrary.execSQL("UPDATE Rent SET identification_user = '" + identification_user.getText().toString() + "', identification_book = '" + identification_book.getText().toString() + "" + "', rent_date = " + date_rent.getText().toString() + " WHERE identification_rent = '" + old_identification_rent + "'");

                    Toast.makeText(getApplicationContext(),"Rent edited successfully!",Toast.LENGTH_SHORT).show();

                } else {

                    SQLiteDatabase sdLibraryread = Library_rent.getReadableDatabase();

                    String query = "SELECT identification_rent FROM Rent WHERE identification_rent = '" + identification_rent.getText().toString() + "'";

                    Cursor cRent = sdLibraryread.rawQuery(query, null);

                    if (!cRent.moveToFirst()) { // No encuentra la identificacion de la renta ingresada

                        sdLibrary.execSQL("UPDATE Rent SET identification_rent = '" + identification_rent.getText().toString() + "', identification_user = '" + identification_user.getText().toString() +  "', identification_book = '" + identification_book.getText().toString() + "" + "', rent_date = " + date_rent.getText().toString() + " WHERE identification_rent = '" + old_identification_rent + "'");

                        Toast.makeText(getApplicationContext(),"Rent edited successfully!",Toast.LENGTH_SHORT).show();

                    }else{

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

        rent_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity_rent_list.class);

                startActivity(intent);

                Toast.makeText(getApplicationContext() ,"Welcome to the rent list!", Toast.LENGTH_SHORT).show();

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

        identification_rent.setText("");
        identification_user.setText("");
        identification_book.setText("");
        date_rent.setText("");
        identification_rent.requestFocus();

    }

}