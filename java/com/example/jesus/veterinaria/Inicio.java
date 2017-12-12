package com.example.jesus.veterinaria;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Inicio extends AppCompatActivity {
    ImageButton btnCargarInicio;
    MediaPlayer media = new MediaPlayer();
    Handler handler = new Handler();
        handler. postDelayed(new Inicio() {
        @Override
        public void run() {
            Intent cargar = new Intent(getApplicationContext(), Login.class);
            startActivity(cargar);
        }
    }, 2000);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        if (media!=null){
            media.release();
            media= MediaPlayer.create(this, R.raw.dogsout);
            media.seekTo(0);
            media.start();
        }
        btnCargarInicio=(ImageButton) findViewById(R.id.activityInicioBtnCargaLogin);
        btnCargarInicio.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cargaLogin();
                    }
                }
        );
    }
    private void cargaLogin() {
        media.stop();
        Intent cargar = new Intent(this, Principal.class);
        this.startActivity(cargar);
    }
}
