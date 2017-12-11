package com.example.jesus.veterinaria;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {
    EditText txtUsuario, txtContrasenia;
    Button btnIniciar;
    Intent cargar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inicializaComponentes();

        btnIniciar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cargaInicio();
                    }
                }
        );
    }
    private void cargaInicio() {
        cargar = new Intent(this, Principal.class);
        this.startActivity(cargar);
    }

    private void inicializaComponentes() {
        txtUsuario=(EditText) findViewById(R.id.MainActivityTxtUsuario);
        txtContrasenia=(EditText) findViewById(R.id.MainActivityTxtContrasenia);
        btnIniciar=(Button) findViewById(R.id.MainActivityBtnEntrar);
    }
}
