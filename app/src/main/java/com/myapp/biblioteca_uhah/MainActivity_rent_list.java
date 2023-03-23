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

public class MainActivity_rent_list extends AppCompatActivity {

    // Declaracion de las variables
    Button log_out, register_rent, go_back;
    ListView listRents;

    ArrayList<String> listado;

    // Cargar los usuarios en el arrayList arrUsers
    DbLibrary Library_rent = new DbLibrary(this, "dblibrary6", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_rent_list);

        // Declaracion de los datos
        log_out = findViewById(R.id.btnlog_out);
        register_rent = findViewById(R.id.btnregister_rent);
        go_back = findViewById(R.id.btngo_back);

        listRents = findViewById(R.id.lvRents);

        CargarListado();

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

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

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

    }

    private void CargarListado(){

        listado = ListaRentas();

        // Generar el adaptador que pasara los datos al ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, listado);

        listRents.setAdapter(adapter);

    }

    private ArrayList<String> ListaRentas() {

        ArrayList<String> datos = new ArrayList<String>();

        SQLiteDatabase sdlibraryread = Library_rent.getReadableDatabase();

        String qAllRents = "SELECT identification_rent, identification_user, identification_book, rent_date FROM Rent";

        Cursor cRents = sdlibraryread.rawQuery(qAllRents, null);

        if (cRents.moveToFirst()) {

            do {

                String recRent = " |   " + cRents.getString(0) + "   |   " + cRents.getString(1) + "   |   " + cRents.getString(2) + "   |   " + cRents.getString(3) + "   | ";
                datos.add(recRent);

            } while (cRents.moveToNext());

        }

        sdlibraryread.close();
        return datos;

    }

}