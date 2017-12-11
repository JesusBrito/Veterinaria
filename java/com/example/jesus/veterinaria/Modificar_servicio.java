package com.example.jesus.veterinaria;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Modificar_servicio extends AppCompatActivity {
    EditText txt_nombre,txt_cve, txt_precio;
    RequestQueue request;
    JsonArrayRequest jsonClienteArrayRequest,jsonArrayGuardarRequest;
    String id_servicio;
    Button btnGuardar, btnEliminar;
    ImageButton btn_regresar, btn_home;
    CheckBox chEditar;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_servicio);
        Intent i = getIntent();
        id_servicio = i.getStringExtra("id");
        inicializaComponentes();
        btn_regresar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent back = new Intent(getApplicationContext(), Servicios.class);
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
        chEditar.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (chEditar.isChecked()){
                            txt_nombre.setEnabled(true);
                            txt_precio.setEnabled(true);
                            btnGuardar.setEnabled(true);
                        }else{
                            txt_nombre.setEnabled(false);
                            txt_precio.setEnabled(false);
                            btnGuardar.setEnabled(false);
                        }
                    }
                }
        );
        btnGuardar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cargarWebServiceGuardar();
                    }
                }
        );
        request = Volley.newRequestQueue(this);

        cargarWebService();
    }

    private void inicializaComponentes(){
        txt_cve = (EditText) findViewById(R.id.detalle_servicio_txt_cve);
        txt_precio = (EditText) findViewById(R.id.detalle_servicio_txt_precio);
        txt_nombre = (EditText) findViewById(R.id.detalle_servicio_txt_nombre);

        btn_regresar = (ImageButton) findViewById(R.id.activityDetalleServicioBtnRegresar);
        btn_home  = (ImageButton) findViewById(R.id.activityDetalleServicioBtnHome);
        chEditar= (CheckBox) findViewById(R.id.activityDetalleServicioChEditar);
        btnGuardar =(Button) findViewById(R.id.activityDetalleServicioBtnGuardar);

    }
    private void cargarWebServiceGuardar() {
        if(txt_precio.getText().toString().equals("")||txt_nombre.getText().toString().equals("")){
            Toast.makeText(this,"Todos los campos deben de estan correctamente llenados",Toast.LENGTH_SHORT);
        }else{
            dialog = new ProgressDialog(this);
            dialog.setMessage("Cargando...");
            dialog.show();
            String url = "https://veterinary-clinic-ws.herokuapp.com/servicios/"+txt_cve.getText().toString()+"/update" +
                    "/"+txt_nombre.getText().toString()+"/"+txt_precio.getText().toString()+"/";
            url= url.replace(" ", "%20");
            jsonArrayGuardarRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Toast.makeText(getApplicationContext(),"Registro modificado exitosamente",Toast.LENGTH_SHORT).show();
                            dialog.hide();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
                            System.out.println();
                            Log.e("ERROR: ", error.toString());
                            dialog.hide();
                        }
                    }
            );
            request.add(jsonArrayGuardarRequest);
        }
    }
    private void cargarWebService() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Cargando datos...");
        dialog.show();
        String url = "https://veterinary-clinic-ws.herokuapp.com/servicios/" + id_servicio;
        jsonClienteArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject jsonObject = null;
                            jsonObject = response.getJSONObject(0);
                            JSONObject fields = jsonObject.getJSONObject("fields");
                            String name = fields.optString("descripcion_servicio");
                            String precio = fields.optString("precio_servicio");

                            txt_cve.setText(id_servicio);
                            txt_nombre.setText(name);
                            txt_precio.setText(precio);
                            dialog.hide();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "No se ha podido establecer conexión con el servidor response", Toast.LENGTH_LONG).show();
                            dialog.hide();
                            Log.d("ERROR: ", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
                System.out.println();
                Log.e("ERROR: ", error.toString());
                dialog.hide();
            }
        }
        );
        request.add(jsonClienteArrayRequest);
    }
}
