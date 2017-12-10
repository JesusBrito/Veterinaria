package com.example.jesus.veterinaria;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class Medicos extends AppCompatActivity  implements Response.Listener<JSONArray>, Response.ErrorListener  {
    ListView listView;
    ProgressDialog dialog;
    RequestQueue request;
    JsonArrayRequest jsonArrayRequest;
    ArrayList<PersonaModelo> lista;
    ImageButton btnMas;
    Intent cargar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicos);

        listView = (ListView) findViewById(R.id.listView);
        request = Volley.newRequestQueue(this);

        cargarWebService();


        lista = new ArrayList<PersonaModelo>();

        btnMas = (ImageButton) findViewById(R.id.principalMedicoAdd);

        btnMas.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        agregarCliente();
                    }
                }
        );
    }

    private void agregarCliente() {

        cargar = new Intent(this, registro_medico.class);
        this.startActivity(cargar);
    }

    private void cargarWebService() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Consultando Imagenes");
        dialog.show();

        String url = "https://veterinary-clinic-ws.herokuapp.com/medicos/";
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonArrayRequest);
//        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);
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
        PersonaModelo cliente = null;

        try {
            for (int i=0;i<response.length();i++){
                JSONObject jsonObject=null;
                jsonObject = response.getJSONObject(i);

                String rfc = jsonObject.optString("pk");
                JSONObject fields = jsonObject.getJSONObject("fields");
                String name = fields.optString("nombre_medico");
                cliente=new PersonaModelo(rfc, name);

                lista.add(cliente);
            }
            dialog.hide();
            MedicoAdapter adapter=new MedicoAdapter(this, lista);
            listView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "No se ha podido establecer conexión con el servidor" +
                    " "+response, Toast.LENGTH_LONG).show();
            dialog.hide();
            Log.d("ERROR: ", e.toString());
        }
    }
}
