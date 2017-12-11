package com.example.jesus.veterinaria;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class Mascotas extends AppCompatActivity implements Response.Listener<JSONArray>, Response.ErrorListener {

    ListView listView;
    ProgressDialog dialog;
    RequestQueue request;
    JsonArrayRequest jsonArrayRequest;
    ArrayList<MascotaModelo> lista;
    ImageButton btnMas, btnHome, btnRegresar ;
    Intent cargar;

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
    }

    private void agregarMascota() {

        cargar = new Intent(this, registro_mascota.class);
        this.startActivity(cargar);
    }

    private void cargarWebService() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Consultando Imagenes");
        dialog.show();

        String url = "https://veterinary-clinic-ws.herokuapp.com/mascotas/";
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonArrayRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
        System.out.println();
        Log.e("ERROR: ", error.toString());
        dialog.hide();
    }

    @Override
    public void onResponse(JSONArray response) {
        MascotaModelo mascota = null;

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
            MascotaAdapter adapter=new MascotaAdapter(this, lista);
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
            Toast.makeText(this, "No se ha podido establecer conexión con el servidor" +
                    " "+response, Toast.LENGTH_LONG).show();
            dialog.hide();
            Log.d("ERROR: ", e.toString());
        }

    }
}
