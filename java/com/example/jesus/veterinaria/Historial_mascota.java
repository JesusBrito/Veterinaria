package com.example.jesus.veterinaria;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Historial_mascota extends AppCompatActivity implements Response.Listener<JSONArray>, Response.ErrorListener {
    String idMascota;
    TextView txt_rfc_cliente, txt_nombre, txt_especie, txt_raza, txt_color, txt_tamaño, txt_senia, txt_fecha;
    ProgressDialog dialog;
    RequestQueue request;
    JsonArrayRequest jsonArrayRequest;
    String rfc;
    Button btnEditar;
    ImageButton btnAgregar, btn_regresar, btn_home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_mascota);
        Intent i = getIntent();
        idMascota = i.getStringExtra("id");
        inicializaComponentes();
        System.out.println("ID DE LA MASCOTA:"+idMascota);
        btn_regresar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent back = new Intent(getApplicationContext(), Mascotas.class);
                        startActivity(back);
                        overridePendingTransition(R.anim.right_in, R.anim.right_out);
                    }
                }
        );

        btn_home.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent home = new Intent(getApplicationContext(), Principal.class);
                        startActivity(home);
                        overridePendingTransition(R.anim.up_in, R.anim.up_out);
                    }
                }
        );
        btnEditar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent editar = new Intent(getApplicationContext(), Modificar_mascota.class);
                        editar.putExtra("id", idMascota);
                        startActivity(editar);
                        overridePendingTransition(R.anim.left_out, R.anim.left_in);
                    }
                }
        );

        btnAgregar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent agregar = new Intent(getApplicationContext(), registro_servicio.class);
                        agregar.putExtra("rfc", idMascota);
                        startActivity(agregar);
                        overridePendingTransition(R.anim.left_out, R.anim.left_in);
                    }
                }
        );
        request = Volley.newRequestQueue(this);
        cargarWebService();
    }
    private void inicializaComponentes(){
        txt_nombre = (TextView) findViewById(R.id.activity_historial_mascota_lblNombre);
        txt_raza = (TextView) findViewById(R.id.activity_historial_mascota_lblRaza);
        txt_especie = (TextView) findViewById(R.id.activity_historial_mascota_lblEspecie);
        txt_fecha = (TextView) findViewById(R.id.activity_historial_mascota_lblFecha);
        txt_color = (TextView) findViewById(R.id.activity_historial_mascota_lblColor);
        txt_tamaño = (TextView) findViewById(R.id.activity_historial_mascota_lblTamaño);
        txt_rfc_cliente = (TextView) findViewById(R.id.activity_historial_mascota_lblDueño);

        btn_regresar = (ImageButton) findViewById(R.id.activity_historial_mascota_btnRegresar);
        btn_home  = (ImageButton) findViewById(R.id.activity_historial_mascota_btnHome);
        btnAgregar = (ImageButton) findViewById(R.id.activity_historial_mascota_btnServicioAdd);
        btnEditar =(Button) findViewById(R.id.activity_historial_mascota_btnEditar);
    }
    private void cargarWebService() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Cargando...");
        dialog.show();
        String url = "https://veterinary-clinic-ws.herokuapp.com/mascotas/" + idMascota;
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonArrayRequest);
    }

    @Override
    public void onResponse(JSONArray response) {
        try {
            JSONObject jsonObject=null;
            jsonObject = response.getJSONObject(0);

            JSONObject fields = jsonObject.getJSONObject("fields");
            String rfc = fields.optString("rfc_cliente");
            String nombre = fields.optString("nombre_mascota");
            String especie = fields.optString("especie_mascota");
            String raza = fields.optString("raza_mascota");
            String color = fields.optString("color_mascota");
            String tamanio = fields.optString("tamaño_mascota");
            String senia = fields.optString("señapart_mascota");
            String fecha = fields.optString("fechanac_mascota");


            txt_nombre.setText(nombre+"--"+idMascota);
            txt_raza.setText(raza);
            txt_especie.setText(especie) ;
            txt_fecha.setText(fecha);
            txt_color.setText(color);
            txt_tamaño.setText(tamanio+"--"+senia);
            txt_rfc_cliente.setText(rfc);
            dialog.hide();

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "No se ha podido establecer conexión con el servidor" +
                    " "+response, Toast.LENGTH_LONG).show();
            dialog.hide();
            Log.d("ERROR: ", e.toString());
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
        System.out.println();
        Log.e("ERROR: ", error.toString());
        dialog.hide();
    }
}
