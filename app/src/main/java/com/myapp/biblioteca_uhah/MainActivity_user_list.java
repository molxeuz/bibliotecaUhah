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

public class MainActivity_user_list extends AppCompatActivity {

    // Declaracion de las variables
    Button log_out, register_user, go_back;
    ListView listUsers;

    ArrayList<String> listado;

    // Cargar los usuarios en el arrayList arrUsers
    DbLibrary Library_user = new DbLibrary(this, "dblibrary6", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user_list);

        // Declaracion de los datos
        log_out = findViewById(R.id.btnlog_out);
        register_user = findViewById(R.id.btnregister_user);
        go_back = findViewById(R.id.btngo_back);

        listUsers = findViewById(R.id.lvUsers);

        CargarListado();

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(intent);

            }
        });

        register_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity_user.class);

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

        listado = ListaUsuarios();

        // Generar el adaptador que pasara los datos al ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, listado);

        listUsers.setAdapter(adapter);

    }

    private ArrayList<String> ListaUsuarios() {

        ArrayList<String> datos = new ArrayList<String>();

        SQLiteDatabase sdlibraryread = Library_user.getReadableDatabase();

        String qAllUsers = "SELECT identification_user, name_user, password_user, status_user FROM User";

        Cursor cUsers = sdlibraryread.rawQuery(qAllUsers, null);

        if (cUsers.moveToFirst()) {

            do {

                // Generar un string para almacenar toda la informacion de cada usuario y guardarlo en el arrayList
                String mStatus = cUsers.getInt(3) == 0 ? "Inactive" : "Active";

                String recUser = " |   " + cUsers.getString(0) + "   |   " + cUsers.getString(1) + "   |   " + mStatus + "   | ";
                datos.add(recUser);

            } while (cUsers.moveToNext());

        }

        sdlibraryread.close();
        return datos;

    }

}