package com.example.jesus.veterinaria;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.List;

public class Modificar_mascota extends AppCompatActivity {
    EditText txtCve, txt_rfc_cliente, txt_nombre, txt_especie, txt_raza, txt_color, txt_tamaño, txt_senia, txt_fecha;
    RequestQueue request;
    JsonArrayRequest jsonClienteArrayRequest,jsonArrayGuardarRequest;
    JsonObjectRequest jsonEliminarObjectRequest;
    String idMascota;
    Button btnGuardar, btnEliminar;
    ImageButton btn_regresar, btn_home;
    CheckBox chEditar;
    ProgressDialog dialog;
    Spinner spTamaño;
    String Tamaño;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_mascota);

        Intent i = getIntent();
        idMascota = i.getStringExtra("id");
        inicializaComponentes();
        List<String> tamaños=new ArrayList<>();

        tamaños.add("Pequeño");
        tamaños.add("Mediano");
        tamaños.add("Grande");
        llenarSpinner(tamaños,spTamaño);

        spTamaño.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tV= (TextView) view;
                Tamaño=tV.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btn_regresar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent back = new Intent(getApplicationContext(), Historial_mascota.class);
                        back.putExtra("id", idMascota);
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
                            txt_especie.setEnabled(true);
                            txt_raza.setEnabled(true);
                            txt_color.setEnabled(true);
                            txt_senia.setEnabled(true);
                            txt_fecha.setEnabled(true);
                            btnGuardar.setEnabled(true);
                            spTamaño.setEnabled(true);
                        }else{
                            txt_nombre.setEnabled(false);
                            txt_especie.setEnabled(false);
                            txt_raza.setEnabled(false);
                            txt_color.setEnabled(false);
                            txt_senia.setEnabled(false);
                            txt_fecha.setEnabled(false);
                            btnGuardar.setEnabled(false);
                            spTamaño.setEnabled(false);
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
        btnEliminar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cargaWebServiceEliminar();
                    }
                }
        );
        request = Volley.newRequestQueue(this);

        cargarWebService();
    }

    private void inicializaComponentes() {
        txtCve = (EditText) findViewById(R.id.detalle_mascota_txt_cve);
        txt_rfc_cliente = (EditText) findViewById(R.id.detalle_mascota_txt_rfc_cliente);
        txt_nombre = (EditText) findViewById(R.id.detalle_mascota_txt_nombre);
        txt_especie = (EditText) findViewById(R.id.detalle_mascota_txt_especie);
        txt_raza = (EditText) findViewById(R.id.detalle_mascota_txt_raza);
        txt_color = (EditText) findViewById(R.id.detalle_mascota_txt_color);
        txt_tamaño = (EditText) findViewById(R.id.detalle_mascota_txt_tamanio);
        txt_senia = (EditText) findViewById(R.id.detalle_mascota_txt_senia);
        txt_fecha = (EditText) findViewById(R.id.detalle_mascota_txt_fecha);
        btn_regresar = (ImageButton) findViewById(R.id.activityDetalleMascotaBtnRegresar);
        btn_home = (ImageButton) findViewById(R.id.activityDetalleMascotaBtnHome);
        chEditar = (CheckBox) findViewById(R.id.activityDetalleMascotaChEditar);
        btnGuardar = (Button) findViewById(R.id.activity_detalle_mascotaBtnGuardar);
        btnEliminar = (Button) findViewById(R.id.activity_detalle_mascotaBtnEliminar);
        spTamaño=(Spinner) findViewById(R.id.activity_detalle_mascota_spTamanio);

    }
    private void cargarWebServiceGuardar() {
        if(txt_especie.getText().toString().equals("")||
                txt_nombre.getText().toString().equals("")||
                txt_raza.getText().toString().equals("")||
                txt_color.getText().toString().equals("")||
                txt_tamaño.getText().toString().equals("")||
                txt_senia.getText().toString().equals("")||
                txt_fecha.getText().toString().equals("")){
            Toast.makeText(this,"Todos los campos deben de estan correctamente llenados",Toast.LENGTH_SHORT);
        }else{
            dialog = new ProgressDialog(this);
            dialog.setMessage("Cargando...");
            dialog.show();
            String url = "https://veterinary-clinic-ws.herokuapp.com/mascotas/"+idMascota+"/update"+"/"
                    +txt_nombre.getText().toString()+"/"
                    +txt_especie.getText().toString()+"/"
                    +txt_raza.getText().toString()+"/"+txt_color.getText().toString()+"/"
                    +Tamaño+"/"+txt_senia.getText().toString()+"/"
                    +txt_fecha.getText().toString()+"/";
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
        String url = "https://veterinary-clinic-ws.herokuapp.com/mascotas/" + idMascota;
        jsonClienteArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject jsonObject = null;
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

                            txtCve.setText(idMascota);
                            txt_nombre.setText(nombre);
                            txt_raza.setText(raza);
                            txt_especie.setText(especie) ;
                            txt_fecha.setText(fecha);
                            txt_color.setText(color);
                            txt_tamaño.setText(tamanio);
                            txt_senia.setText(senia);
                            txt_rfc_cliente.setText(rfc);
                            ArrayAdapter myAdapter = (ArrayAdapter) spTamaño.getAdapter();
                            int spinnerPosition = myAdapter.getPosition(tamanio);
                            spTamaño.setSelection(spinnerPosition);
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
    private void cargaWebServiceEliminar(){
        dialog = new ProgressDialog(this);
        dialog.setMessage("Eliminando...");
        dialog.show();

        String url = "https://veterinary-clinic-ws.herokuapp.com/mascotas/"+idMascota+"/delete/";
        jsonEliminarObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent back = new Intent(getApplicationContext(), Mascotas.class);
                        startActivity(back);
                        overridePendingTransition(R.anim.right_in, R.anim.right_out);
                        Toast.makeText(getApplicationContext(), "Borrado exitoso ", Toast.LENGTH_LONG).show();
                        System.out.println();
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
        request.add(jsonEliminarObjectRequest);
    }

    //Apaptador
    public void llenarSpinner(List<String> items, Spinner spinner){
        ArrayAdapter<String> adp= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        spinner.setAdapter(adp);
    }
}
