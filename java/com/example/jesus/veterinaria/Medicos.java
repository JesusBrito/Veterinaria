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

public class Medicos extends AppCompatActivity{
    ListView listView;
    ProgressDialog dialog;
    RequestQueue request;
    JsonArrayRequest jsonArrayRequest;
    ArrayList<PersonaModelo> lista;
    ImageButton btnMas, btnRegresar, btnHome;
    Intent cargar;
    Button btnBuscar;
    EditText txtRfc;
    String Rfc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicos);
        inicializaComponentes();
        request = Volley.newRequestQueue(this);
        cargarWebService();
        lista = new ArrayList<PersonaModelo>();

        btnMas.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        agregarMedico();
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

    private void cargaHome() {
        cargar = new Intent(this, Principal.class);
        this.startActivity(cargar);
    }

    private void inicializaComponentes(){
        listView = (ListView) findViewById(R.id.listView);
        btnMas = (ImageButton) findViewById(R.id.principalMedicoAdd);
        btnRegresar= (ImageButton) findViewById(R.id.activityMedicosBtnRegresar);
        btnHome=(ImageButton) findViewById(R.id.activityMedicosBtnHome);
        btnBuscar=(Button) findViewById(R.id.activityMedicosBtnBuscar);
        txtRfc=(EditText) findViewById(R.id.activityMedicosRfc);
    }
    private void agregarMedico() {

        cargar = new Intent(this, registro_medico.class);
        this.startActivity(cargar);
    }

    private void cargarWebServiceRfc() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Consultando Imagenes");
        dialog.show();
        Rfc= txtRfc.getText().toString();
        String url = "https://veterinary-clinic-ws.herokuapp.com/medicos/"+Rfc;
        Activity activity = this;
        final Activity finalActivity = activity;
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        PersonaModelo cliente = null;
                        lista.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = null;
                                jsonObject = response.getJSONObject(i);

                                String rfc = jsonObject.optString("pk");
                                JSONObject fields = jsonObject.getJSONObject("fields");
                                String name = fields.optString("nombre_medico");
                                cliente = new PersonaModelo(rfc, name);

                                lista.add(cliente);
                            }
                            dialog.hide();
                            MedicoAdapter adapter = new MedicoAdapter(finalActivity, lista);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(
                                    new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Intent i = new Intent(getApplicationContext(), Modificar_medico.class);
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
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "El medico no se encuentra registrado "+error.toString(), Toast.LENGTH_LONG).show();
                        System.out.println();
                        Log.e("ERROR: ", error.toString());
                        dialog.hide();
                    }
                }
        );
        request.add(jsonArrayRequest);
    }

    private void cargarWebService() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Consultando Imagenes");
        dialog.show();

        String url = "https://veterinary-clinic-ws.herokuapp.com/medicos/";
        Activity activity = this;
        final Activity finalActivity = activity;
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        PersonaModelo cliente = null;
                        lista.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = null;
                                jsonObject = response.getJSONObject(i);

                                String rfc = jsonObject.optString("pk");
                                JSONObject fields = jsonObject.getJSONObject("fields");
                                String name = fields.optString("nombre_medico");
                                cliente = new PersonaModelo(rfc, name);

                                lista.add(cliente);
                            }
                            dialog.hide();
                            MedicoAdapter adapter = new MedicoAdapter(finalActivity, lista);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(
                                    new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Intent i = new Intent(getApplicationContext(), Modificar_medico.class);
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
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error en la consulta "+error.toString(), Toast.LENGTH_LONG).show();
                System.out.println();
                Log.e("ERROR: ", error.toString());
                dialog.hide();
            }
        }
        );
        request.add(jsonArrayRequest);
    }
}
