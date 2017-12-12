package com.example.jesus.veterinaria;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Clientes extends AppCompatActivity {
    ListView listView;
    ProgressDialog dialog;
    RequestQueue request;
    ArrayList<PersonaModelo> lista;
    ImageButton btnMas, btnHome, btnRegresar ;
    Button btnBuscar;
    EditText txtRfc;
    String Rfc="";
    Intent cargar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);
        inicializaComponentes();
        request = Volley.newRequestQueue(this);
        cargarWebService();
        lista = new ArrayList<PersonaModelo>();
        btnMas.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        agregarCliente();
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
                        try {
                            if (txtRfc.getText().toString().equals("")){
                                cargarWebService();
                            }else{
                                cargarWebServiceRfc();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }
    private void cargaHome() {
        cargar = new Intent(this, Principal.class);
        this.startActivity(cargar);
        overridePendingTransition(R.anim.up_in, R.anim.up_out);
    }

    private void inicializaComponentes(){
        listView = (ListView) findViewById(R.id.listView);
        btnMas = (ImageButton) findViewById(R.id.principalClientesAdd);
        btnRegresar= (ImageButton) findViewById(R.id.activityClientesBtnRegresar);
        btnHome=(ImageButton) findViewById(R.id.activityClientesBtnHome);
        btnBuscar=(Button) findViewById(R.id.activityClientesBtnBuscar);
        txtRfc=(EditText) findViewById(R.id.activityClientestxtRfc);
    }

    private void agregarCliente() {
        cargar = new Intent(this, registro_cliente.class);
        this.startActivity(cargar);
    }

    private void cargarWebServiceRfc() throws JSONException {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Consultando Imagenes");
        dialog.show();
        Rfc=txtRfc.getText().toString();
        String url = "https://veterinary-clinic-ws.herokuapp.com/clientes/";
        Activity activity = this;
        final Activity finalActivity = activity;

        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PersonaModelo cliente = null;
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

                                String rfc = jsonObject.optString("rfc_cliente");
                                String name = jsonObject.optString("nombre_cliente");
                                cliente=new PersonaModelo(rfc, name);

                                lista.add(cliente);
                            }
                            dialog.hide();
                            PersonaAdapter adapter = new PersonaAdapter(finalActivity, lista);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(
                                    new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Intent i = new Intent(getApplicationContext(), Historial_cliente.class);
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
                        Toast.makeText(getApplicationContext(), "El rfc no se encuentra registrado "+error.toString(), Toast.LENGTH_LONG).show();
                        System.out.println();
                        Log.e("ERROR: ", error.toString());
                        dialog.hide();
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
                        postParam.put("rfc", Rfc);
                        return postParam;
                    }
                };
        request.add(jsonObjRequest);
    }
    private void cargarWebService() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Consultando Imagenes");
        dialog.show();
        Rfc=txtRfc.getText().toString();
        String url = "https://veterinary-clinic-ws.herokuapp.com/clientes/";
        Activity activity = this;
        final Activity finalActivity = activity;
        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PersonaModelo cliente = null;
                        JSONObject responseJSON = null;
                        lista.clear();
                        try {
                            responseJSON = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            //JSONArray results = responseJSON.optJSONArray("results");
                            JSONArray results = responseJSON.optJSONArray("objects");
                            for (int i=0;i<results.length();i++){
                                JSONObject jsonObject=null;
                                jsonObject = results.getJSONObject(i);

                                String rfc = jsonObject.optString("rfc_cliente");
                                String name = jsonObject.optString("nombre_cliente");
                                cliente=new PersonaModelo(rfc, name);

                                lista.add(cliente);
                            }
                            dialog.hide();
                            PersonaAdapter adapter = new PersonaAdapter(finalActivity, lista);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(
                                    new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Intent i = new Intent(getApplicationContext(), Historial_cliente.class);
                                            i.putExtra("id", lista.get(position).getId());
                                            startActivity(i);
                                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                                        }
                                    }
                            );
                        }catch (JSONException e) {
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
        });
        request.add(jsonObjRequest);
    }
}

