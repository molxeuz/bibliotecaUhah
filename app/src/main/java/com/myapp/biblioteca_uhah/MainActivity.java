package com.myapp.biblioteca_uhah;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Declaracion de variables
    EditText email_admin, password_admin;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Declaracion de datos
        email_admin = findViewById(R.id.etemail_admin);
        password_admin = findViewById(R.id.etpassword_admin);
        login = findViewById(R.id.btnlogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = email_admin.getText().toString();
                String password = password_admin.getText().toString();

                if (email.equals("admin") &&  password.equals("123")) {

                    Intent intent = new Intent(getApplicationContext(), MainActivity_options.class);

                    startActivity(intent);

                    Limpiar_campos();

                    Toast.makeText(getApplicationContext(), "Ingreso exitoso, bienvenido!", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(getApplicationContext(), "Ingreso no exitoso, verifique!", Toast.LENGTH_SHORT).show();

                    Limpiar_campos();

                }

            }
        });

    }

    // Funcion para limpiar campos en registro de usuario
    private void Limpiar_campos(){

        email_admin.setText("");
        password_admin.setText("");
        email_admin.requestFocus();

    }

}