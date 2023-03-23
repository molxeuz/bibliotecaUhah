package com.myapp.biblioteca_uhah;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity_user extends AppCompatActivity {

    // Declaracion de las variables
    Button log_out, user_list, go_back, save_user, edit_user, delete_user, search_user;
    EditText identification_user, name_user, password_user;
    Switch status_user;

    // Instanciar la clase DbLibrary para los difierentes botones
    DbLibrary Library_user = new DbLibrary(this, "dblibrary6", null, 1);

    String old_identification_user = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        // Declaracion de los datos
        save_user = findViewById(R.id.btnsave_user);
        edit_user = findViewById(R.id.btnedit_user);
        delete_user = findViewById(R.id.btndelete_user);
        search_user = findViewById(R.id.btnsearch_user);
        log_out = findViewById(R.id.btnlog_out);
        user_list = findViewById(R.id.btnuser_list);
        go_back = findViewById(R.id.btngo_back);
        identification_user= findViewById(R.id.etidentification_user);
        name_user = findViewById(R.id.etname_user);
        password_user = findViewById(R.id.etpassword_user);
        status_user = findViewById(R.id.swstatus_user);

        // botones desactivados
        delete_user.setEnabled(false);
        edit_user.setEnabled(false);

        // Metodo Onclick para guardar usuario
        save_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!identification_user.getText().toString().isEmpty() && !name_user.getText().toString().isEmpty() && !password_user.getText().toString().isEmpty()){

                    // Buscar el email en la tabla user
                    SQLiteDatabase sdLibraryread = Library_user.getReadableDatabase();

                    String query = "SELECT identification_user FROM User WHERE identification_user = '" + identification_user.getText().toString() + "'";

                    Cursor cUser = sdLibraryread.rawQuery(query, null);

                    if (!cUser.moveToFirst()){ // No encuentra el email ingresado

                        // Instanciar la clase de SQLiteDatabase en modo escritura
                        SQLiteDatabase sdLibrary = Library_user.getWritableDatabase();

                        // Contenedor de valores
                        ContentValues cvUser = new ContentValues();

                        cvUser.put("identification_user", identification_user.getText().toString());
                        cvUser.put("name_user", name_user.getText().toString());
                        cvUser.put("password_user", password_user.getText().toString());
                        cvUser.put("status_user", status_user.isChecked() ? true : false);

                        sdLibrary.insert("User",null, cvUser);

                        sdLibrary.close();

                        delete_user.setEnabled(false);
                        edit_user.setEnabled(false);
                        search_user.setEnabled(true);

                        Limpiar_campos();

                        Toast.makeText(getApplicationContext(),"User saved successfully!",Toast.LENGTH_SHORT).show();

                    }else{

                        Toast.makeText(getApplicationContext(),"The user is already saved, try another one!",Toast.LENGTH_SHORT).show();
                        
                    }

                }else{

                    Toast.makeText(getApplicationContext(),"You must fill all the fields to save!",Toast.LENGTH_SHORT).show();

                }

            }

        });

        // Metodo Onclick para buscar usuario
        search_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase sdLibraryread = Library_user.getReadableDatabase();

                String query = "SELECT name_user, status_user FROM User WHERE identification_user = '" + identification_user.getText().toString() + "'";

                Cursor cursorUsuario = sdLibraryread.rawQuery(query, null);

                if (cursorUsuario.moveToFirst()) {

                    name_user.setText(cursorUsuario.getString(0));

                    status_user.setChecked(cursorUsuario.getInt(1) == 1 ? true : false);

                    delete_user.setEnabled(true);
                    edit_user.setEnabled(true);

                    old_identification_user = identification_user.getText().toString();

                    sdLibraryread.close();

                    Toast.makeText(getApplicationContext(), "User found successfully!", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(getApplicationContext(), "User wasn't found!", Toast.LENGTH_SHORT).show();

                }

            }

        });

        // Metodo Onclick para borrar usuario
        delete_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!identification_user.getText().toString().isEmpty() && !name_user.getText().toString().isEmpty() && !password_user.getText().toString().isEmpty()) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity_user.this);

                    alertDialogBuilder.setMessage("You are about to delete the user!");

                    alertDialogBuilder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            SQLiteDatabase obde = Library_user.getWritableDatabase();

                            obde.execSQL("DELETE FROM User WHERE identification_user = '"+identification_user.getText().toString()+"'");

                            Toast.makeText(getApplicationContext(),"User deleted successfully!",Toast.LENGTH_SHORT).show();

                            Limpiar_campos();

                        }

                    });

                    alertDialogBuilder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            Toast.makeText(getApplicationContext(),"User wasn't deleted", Toast.LENGTH_SHORT).show();

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
        edit_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase sdLibrary = Library_user.getWritableDatabase();

                //int newRole = user.isChecked() ? 0 : 1;
                if (identification_user.getText().toString().equals(old_identification_user)){

                    sdLibrary.execSQL("UPDATE User SET name_user = '" + name_user.getText().toString() + "" + "', status_user = " + (status_user.isChecked() ? 1 : 0) + " WHERE identification_user = '" + old_identification_user + "'");

                    Toast.makeText(getApplicationContext(),"User edited successfully!",Toast.LENGTH_SHORT).show();

                } else {

                    SQLiteDatabase sdLibraryread = Library_user.getReadableDatabase();

                    String query = "SELECT identification_user FROM User WHERE identification_user = '" + identification_user.getText().toString() + "'";

                    Cursor cUser = sdLibraryread.rawQuery(query, null);

                    if (!cUser.moveToFirst()) { // No encuentra el email ingresado

                        sdLibrary.execSQL("UPDATE User SET identification_user = '" + identification_user.getText().toString() + "', name_user = '" + name_user.getText().toString() + "" + "', status_user = " + (status_user.isChecked() ? 1 : 0) + " WHERE identification_user = '" + old_identification_user + "'");

                        Toast.makeText(getApplicationContext(),"User edited successfully!",Toast.LENGTH_SHORT).show();

                    }else{

                        Toast.makeText(getApplicationContext(),"The new identification is already assigned to an identification!",Toast.LENGTH_SHORT).show();

                    }

                }

            }

        });

        // Metodo que lleva a lista de usuario
        user_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),MainActivity_user_list.class));

                Toast.makeText(getApplicationContext() ,"Welcome to the users list!", Toast.LENGTH_SHORT).show();

            }

        });

        // Metodo que regresa a la anterior interfaz
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }

        });

        user_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ir = new Intent(getApplicationContext(),MainActivity_user_list.class);

                startActivity(ir);

            }

        });

    }

    // Funcion para limpiar campos en registro de usuario
    private void Limpiar_campos(){

        identification_user.setText("");
        name_user.setText("");
        password_user.setText("");
        status_user.setChecked(false);
        identification_user.requestFocus();

    }

}