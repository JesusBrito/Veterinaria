package com.example.jesus.veterinaria;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class registro_servicio extends AppCompatActivity {
    Button btnGuardar, btnCancelar;
    ImageButton btnRegresar, btnHome,btnImagen;
    Intent cargar;
    EditText txtNombre, txtPrecio;

    ProgressDialog progreso;
    RequestQueue request;
    JsonArrayRequest JsonArrayRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_servicio);
        inicializaComponentes();
        request= Volley.newRequestQueue(this);
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
        btnImagen.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }
        );
    }

    private void cargarWebService() {
        if(txtNombre.getText().toString().equals("")||
           txtPrecio.getText().toString().equals("")){
            Toast.makeText(this,"Todos los campos deben de estan correctamente llenados",Toast.LENGTH_SHORT);
        }else{
            progreso= new ProgressDialog(this);
            progreso.setMessage("Cargando...");
            progreso.show();
            String url = "https://veterinary-clinic-ws.herokuapp.com/servicios/create/";
            url= url.replace(" ", "%20");
            StringRequest jsonArrayRequest = new  StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(),"Registro agregado exitosamente",Toast.LENGTH_SHORT).show();
                            progreso.hide();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
                            System.out.println();
                            Log.e("ERROR: ", error.toString());
                            progreso.hide();
                        }
                    }
            ){
                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> postParam = new HashMap<String, String>();
                    postParam.put("descipcion", txtNombre.getText().toString());
                    postParam.put("precio", txtPrecio.getText().toString());
                    return postParam;
                }
            };
            request.add(jsonArrayRequest);
        }
    }

    private void inicializaComponentes() {
        btnRegresar=(ImageButton) findViewById(R.id.activityRegistroServicioBtnRegresar);
        btnHome=(ImageButton) findViewById(R.id.activityRegistroServicioBtnHome);
        btnGuardar=(Button) findViewById(R.id.activity_registro_servicio_btnGuardar);
        btnCancelar=(Button) findViewById(R.id.activity_registro_servicio_btnCancelar);
        txtPrecio=(EditText) findViewById(R.id.activity_registro_servicio_txtPrecio);
        txtNombre=(EditText) findViewById(R.id.activity_registro_servicio_txtNombre);
        btnImagen=(ImageButton) findViewById(R.id.activity_servicios_btn_img_cargar);
    }
    private void cargaHome() {
        cargar = new Intent(this, Principal.class);
        this.startActivity(cargar);
        overridePendingTransition(R.anim.up_in, R.anim.up_out);
    }

    private void cargaMascotas() {
        cargar = new Intent(this, Servicios.class);
        this.startActivity(cargar);
    }
}
