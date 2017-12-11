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

public class Mascotas extends AppCompatActivity{

    ListView listView;
    ProgressDialog dialog;
    RequestQueue request;
    JsonArrayRequest jsonArrayRequest;
    ArrayList<MascotaModelo> lista;
    ImageButton btnMas, btnHome, btnRegresar ;
    Intent cargar;
    Button btnBuscar;
    EditText txtCve;
    String cveMascota;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mascotas);
        inicializaComponentes();
        request = Volley.newRequestQueue(this);
        cargarWebService();
        lista = new ArrayList<MascotaModelo>();
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
                        overridePendingTransition(R.anim.up_in, R.anim.up_out);
                    }
                }

        );
        btnHome.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cargaHome();
                        overridePendingTransition(R.anim.up_in, R.anim.up_out);
                    }
                }
        );

        btnBuscar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cargarWebServiceRfc();
                    }
                }
        );
    }

    private void cargarWebServiceRfc() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Consultando BD");
        dialog.show();
        cveMascota= txtCve.getText().toString();
        String url = "https://veterinary-clinic-ws.herokuapp.com/mascotas/"+cveMascota;
        Activity activity = this;
        final Activity finalActivity = activity;
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        MascotaModelo mascota = null;
                        lista.clear();
                        try {
                            for (int i=0;i<response.length();i++){
                                JSONObject jsonObject=null;
                                jsonObject = response.getJSONObject(i);
                                //TODO
                                String rfc = jsonObject.optString("pk");
                                JSONObject fields = jsonObject.getJSONObject("fields");
                                String id = fields.optString("rfc_cliente");
                                String name = fields.optString("nombre_mascota");
                                mascota=new MascotaModelo(id, name,rfc);
                                lista.add(mascota);
                            }
                            dialog.hide();
                            MascotaAdapter adapter=new MascotaAdapter(finalActivity, lista);
                            listView.setAdapter(adapter);

                            listView.setOnItemClickListener(
                                    new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Intent i = new Intent(getApplicationContext(), Historial_mascota.class);
                                            i.putExtra("id", lista.get(position).getRfc());
                                            startActivity(i);
                                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                                        }
                                    }
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "No se ha podido establecer conexión con el servidor" +
                                    " "+response, Toast.LENGTH_LONG).show();
                            dialog.hide();
                            Log.d("ERROR: ", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "La mascota no se encuentra registrada "+error.toString(), Toast.LENGTH_LONG).show();
                        System.out.println();
                        Log.e("ERROR: ", error.toString());
                        dialog.hide();
                    }
                });
        request.add(jsonArrayRequest);
    }

    private void cargaHome() {
        cargar = new Intent(this, Principal.class);
        this.startActivity(cargar);
    }

    private void inicializaComponentes(){
        listView = (ListView) findViewById(R.id.listView);
        btnMas = (ImageButton) findViewById(R.id.principalMascotasAdd);
        btnRegresar= (ImageButton) findViewById(R.id.activityMascotassBtnRegresar);
        btnHome=(ImageButton) findViewById(R.id.activityMascotasBtnHome);
        btnBuscar=(Button)findViewById(R.id.activityMascotasBtnBuscar);
        txtCve=(EditText)findViewById(R.id.activityMascotastxtIdMascota);
    }

    private void agregarMascota() {
        cargar = new Intent(this, registro_mascota.class);
        this.startActivity(cargar);
    }

    private void cargarWebService() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Consultando Imagenes");
        dialog.show();
        cveMascota= txtCve.getText().toString();
        String url = "https://veterinary-clinic-ws.herokuapp.com/mascotas/";
        Activity activity = this;
        final Activity finalActivity = activity;
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        MascotaModelo mascota = null;
                        lista.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = null;
                                jsonObject = response.getJSONObject(i);
                                //TODO
                                String rfc = jsonObject.optString("pk");
                                JSONObject fields = jsonObject.getJSONObject("fields");
                                String id = fields.optString("rfc_cliente");
                                String name = fields.optString("nombre_mascota");
                                mascota = new MascotaModelo(id, name, rfc);
                                lista.add(mascota);
                            }
                            dialog.hide();
                            MascotaAdapter adapter = new MascotaAdapter(finalActivity, lista);
                            listView.setAdapter(adapter);

                            listView.setOnItemClickListener(
                                    new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Intent i = new Intent(getApplicationContext(), Historial_mascota.class);
                                            i.putExtra("id", lista.get(position).getRfc());
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
                }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error en la consulta  "+error.toString(), Toast.LENGTH_LONG).show();
                            System.out.println();
                            Log.e("ERROR: ", error.toString());
                            dialog.hide();
                        }
                });
        request.add(jsonArrayRequest);
    }
}
