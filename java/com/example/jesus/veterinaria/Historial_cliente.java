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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Historial_cliente extends AppCompatActivity{
    TextView txt_rfc, txt_nombre, txt_direccion, txt_telefono, txt_email;
    ProgressDialog dialog;
    RequestQueue request;
    JsonArrayRequest jsonArrayRequest;
    String rfc;
    Button btnEditar;
    ImageButton btnAgregar, btn_regresar, btn_home;
    ArrayList<ListaMascotasModelo> lista;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_cliente);
        lista = new ArrayList<ListaMascotasModelo>();
        Intent i = getIntent();
        rfc = i.getStringExtra("id");
        inicializaComponentes();
        btn_regresar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent back = new Intent(getApplicationContext(), Clientes.class);
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
                        Intent editar = new Intent(getApplicationContext(), Modificar_cliente.class);
                        editar.putExtra("id", txt_rfc.getText().toString());
                        startActivity(editar);
                        overridePendingTransition(R.anim.left_out, R.anim.left_in);
                    }
                }
        );

        btnAgregar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent agregar = new Intent(getApplicationContext(), registro_mascota.class);
                        agregar.putExtra("rfc", txt_rfc.getText().toString());
                        startActivity(agregar);
                        overridePendingTransition(R.anim.left_out, R.anim.left_in);
                    }
                }
        );
        request = Volley.newRequestQueue(this);
        cargarWebService();
        cargarWebServiceMascotas();
    }

    private void inicializaComponentes(){
        txt_direccion = (TextView) findViewById(R.id.activity_historial_cliente_lblDireccion);
        txt_email = (TextView) findViewById(R.id.activity_historial_cliente_lblCorreo);
        txt_nombre = (TextView) findViewById(R.id.activity_historial_cliente_lblNombre);
        txt_rfc = (TextView) findViewById(R.id.activity_historial_cliente_lblRfc);
        txt_telefono = (TextView) findViewById(R.id.activity_historial_cliente_lblTelefono);
        btn_regresar = (ImageButton) findViewById(R.id.activityHistorialClienteBtnRegresar);
        btn_home  = (ImageButton) findViewById(R.id.activityHistorialClienteBtnHome);
        btnEditar =(Button) findViewById(R.id.activity_historial_cliente_btnEditar);
        btnAgregar = (ImageButton) findViewById(R.id.activity_historial_cliente_btnAddMascota);
        listView = (ListView) findViewById(R.id.listView);
    }
    private void cargarWebService() {
        String url = "https://veterinary-clinic-ws.herokuapp.com/clientes/";
        final Activity activity =this;
        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseJSON = null;
                            responseJSON = new JSONObject(response);
                            JSONArray results = responseJSON.optJSONArray("objects");
                            JSONObject jsonObject = results.optJSONObject(0);

                            String name = jsonObject.optString("nombre_cliente");
                            String address = jsonObject.optString("direccion_cliente");
                            String phone = jsonObject.optString("telefono_cliente");
                            String email = jsonObject.optString("email_cliente");
                            txt_rfc.setText(rfc);
                            txt_nombre.setText(name);
                            txt_direccion.setText(address);
                            txt_telefono.setText(phone);
                            txt_email.setText(email);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(activity, "No se ha podido establecer conexión con el servidor" +
                                    " "+response, Toast.LENGTH_LONG).show();
                            Log.d("ERROR: ", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
                        System.out.println();
                        Log.e("ERROR: ", error.toString());
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
                postParam.put("rfc", rfc);
                return postParam;
            }
        };
        request.add(jsonObjRequest);
    }
    private void cargarWebServiceMascotas() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Consultando Mascotas");
        dialog.show();
        String url = "https://veterinary-clinic-ws.herokuapp.com/clientes/mascotas/";
        Activity activity = this;
        final Activity finalActivity = activity;
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ListaMascotasModelo mascota = null;
                        lista.clear();
                        JSONObject responseJSON = null;
                        lista.clear();
                        try {
                            responseJSON = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            JSONArray results = responseJSON.optJSONArray("objects");
                            for (int i=0;i<results.length();i++){
                                JSONObject jsonObject=null;
                                jsonObject = results.getJSONObject(i);
                                String id = jsonObject.optString("id_mascota");
                                String name = jsonObject.optString("nombre_mascota");
                                String raza = jsonObject.optString("raza_mascota");
                                String color = jsonObject.optString("color_mascota");
                                String fecha = jsonObject.optString("fechanac_mascota");
                                mascota = new ListaMascotasModelo(name, raza,color,fecha,id);
                                lista.add(mascota);
                            }
                            dialog.hide();
                            ListaMascotasAdapter adapter = new ListaMascotasAdapter(finalActivity, lista);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(
                                    new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Intent i = new Intent(getApplicationContext(), Historial_mascota.class);
                                            i.putExtra("id", lista.get(position).getPk());
                                            System.out.println("ID DE LA MASCOTA:"+lista.get(position).getPk());
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
                Toast.makeText(getApplicationContext(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
                System.out.println();
                Log.e("ERROR: ", error.toString());
                dialog.hide();
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> postParam = new HashMap<String, String>();
                postParam.put("rfc", rfc);
                return postParam;
            }
        };
        request.add(jsonArrayRequest);
    }
}
