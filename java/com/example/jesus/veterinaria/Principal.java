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
