package com.example.jesus.veterinaria;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class registro_medico extends AppCompatActivity {
    Button btnGuardar, btnCancelar;
    ImageButton btnRegresar, btnHome;
    Intent cargar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_medico);

        inicializaComponentes();

        btnRegresar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cargaMascotas();
                    }
                }
        );

        btnHome.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cargaHome();
                    }
                }
        );

        btnCancelar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cargaHome();
                    }
                }
        );

        btnGuardar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cargarWebService();
                    }
                }
        );
    }

    private void cargarWebService() {
    }

    private void inicializaComponentes() {
        btnRegresar=(ImageButton) findViewById(R.id.activityRegistroMedicoBtnRegresar);
        btnHome=(ImageButton) findViewById(R.id.activityRegistroMedicoBtnHome);
        btnGuardar=(Button) findViewById(R.id.activity_registro_medico_btnGuardar);
        btnCancelar=(Button) findViewById(R.id.activity_registro_medico_btnCancelar);
    }
    private void cargaHome() {
        cargar = new Intent(this, Principal.class);
        this.startActivity(cargar);
    }

    private void cargaMascotas() {
        cargar = new Intent(this, Medicos.class);
        this.startActivity(cargar);
    }
}
