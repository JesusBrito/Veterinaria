package com.example.jesus.veterinaria;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Principal extends AppCompatActivity {
    Button btnClientes, btnMascotas, btnMedicos, btnServicios;
    Intent Cargar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        inicializaComponentes();

        btnClientes.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cargaClientes();
                    }
                }
        );
        btnMascotas.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cargaMascotas();
                    }
                }
        );
        btnMedicos.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cargaMedicos();
                    }
                }
        );
        btnServicios.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cargaServicios();
                    }
                }
        );
    }

    private void cargaServicios() {
        Cargar = new Intent(this, Servicios.class);
        this.startActivity(Cargar);
    }
    private void cargaMedicos() {
        Cargar = new Intent(this, Medicos.class);
        this.startActivity(Cargar);
    }
    private void cargaMascotas() {
        Cargar = new Intent(this, Mascotas.class);
        this.startActivity(Cargar);
    }
    private void cargaClientes() {
        Cargar = new Intent(this, Clientes.class);
        this.startActivity(Cargar);
    }

    private void inicializaComponentes() {
        btnClientes=(Button) findViewById(R.id.activity_principal_clientes);
        btnMascotas=(Button) findViewById(R.id.activity_principal_mascotas);
        btnMedicos=(Button) findViewById(R.id.activity_principal_medicos);
        btnServicios=(Button) findViewById(R.id.activity_principal_servicios);
    }
}
