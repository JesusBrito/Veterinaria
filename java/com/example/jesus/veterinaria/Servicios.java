package com.example.jesus.veterinaria;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
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

import java.util.ArrayList;

public class Servicios extends AppCompatActivity{

    ListView listView;
    ProgressDialog dialog;
    RequestQueue request;
    JsonArrayRequest jsonArrayRequest;
    ArrayList<ServicioModelo> lista;
    ImageButton btnMas, btnRegresar, btnHome;
    Intent cargar;
    Button btnBuscar;
    EditText txtCve;
    String Cve="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicios);
        inicializaComponentes();
        request = Volley.newRequestQueue(this);
        cargarWebService();
        lista = new ArrayList<ServicioModelo>();

        btnMas.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        agregarMascota();
                    }
                }
        );

        btnRegresar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cargaHome();
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
        btnBuscar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cargarWebServiceCve();
                    }
                }
        );
    }

    private void cargaHome() {
        cargar = new Intent(this, Principal.class);
        this.startActivity(cargar);
    }

    private void inicializaComponentes(){
        listView = (ListView) findViewById(R.id.listView);
        btnMas = (ImageButton) findViewById(R.id.principalServicioAdd);
        btnRegresar= (ImageButton) findViewById(R.id.activityServiciosBtnRegresar);
        btnHome=(ImageButton) findViewById(R.id.activityServiciosBtnHome);
        txtCve=(EditText) findViewById(R.id.activityServiciosCve);
        btnBuscar=(Button) findViewById(R.id.activityServiciosBtnBuscar);
    }

    private void agregarMascota() {

        cargar = new Intent(this, registro_servicio.class);
        this.startActivity(cargar);
    }

    private void cargarWebService() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Consultando Imagenes");
        dialog.show();

        String url = "https://veterinary-clinic-ws.herokuapp.com/servicios/";
        Activity activity = this;
        final Activity finalActivity = activity;
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ServicioModelo servicio = null;
                        lista.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = null;
                                jsonObject = response.getJSONObject(i);
                                //TODO
                                String id = jsonObject.optString("pk");
                                JSONObject fields = jsonObject.getJSONObject("fields");
                                String name = fields.optString("descripcion_servicio");
                                String precio = fields.optString("precio_servicio");
                                servicio = new ServicioModelo(id, name, precio);
                                lista.add(servicio);
                            }
                            dialog.hide();
                            ServicioAdapter adapter = new ServicioAdapter(finalActivity, lista);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(
                                    new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Intent i = new Intent(getApplicationContext(), Modificar_servicio.class);
                                            i.putExtra("id", lista.get(position).getId());
                                            startActivity(i);
                                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                                        }
                                    }
                            );

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "No se ha podido establecer conexión con el servidor" +
                                    " " + response, Toast.LENGTH_LONG).show();
                            dialog.hide();
                            Log.d("ERROR: ", e.toString());
                        }
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
        request.add(jsonArrayRequest);
//        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void cargarWebServiceCve() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Consultando Imagenes");
        dialog.show();
        Cve=txtCve.getText().toString();
        String url = "https://veterinary-clinic-ws.herokuapp.com/servicios/"+Cve;
        Activity activity = this;
        final Activity finalActivity = activity;
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ServicioModelo servicio = null;
                        lista.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = null;
                                jsonObject = response.getJSONObject(i);
                                //TODO
                                String id = jsonObject.optString("pk");
                                JSONObject fields = jsonObject.getJSONObject("fields");
                                String name = fields.optString("descripcion_servicio");
                                String precio = fields.optString("precio_servicio");
                                servicio = new ServicioModelo(id, name, precio);
                                lista.add(servicio);
                            }
                            dialog.hide();
                            ServicioAdapter adapter = new ServicioAdapter(finalActivity, lista);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(
                                    new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Intent i = new Intent(getApplicationContext(), Modificar_servicio.class);
                                            i.putExtra("id", lista.get(position).getId());
                                            startActivity(i);
                                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                                        }
                                    }
                            );

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "No se ha podido establecer conexión con el servidor" +
                                    " " + response, Toast.LENGTH_LONG).show();
                            dialog.hide();
                            Log.d("ERROR: ", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "El servicio no esta registrado "+error.toString(), Toast.LENGTH_LONG).show();
                        System.out.println();
                        Log.e("ERROR: ", error.toString());
                        dialog.hide();
                    }
                }
        );
        request.add(jsonArrayRequest);
//        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);
    }

}
