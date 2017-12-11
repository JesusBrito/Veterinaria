package com.example.jesus.veterinaria;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

public class registro_mascota extends AppCompatActivity implements Response.Listener<JSONArray>, Response.ErrorListener {
    Button btnGuardar, btnCancelar;
    ImageButton btnRegresar, btnHome;
    EditText txtNombre, txtEspecie, txtRaza, txtRfcCliente, txtColor, txtTamaño, txtSenia, txtNacimiento;
    String cveMascota="";
    Intent cargar;
    String rfc;
    ProgressDialog progreso;
    RequestQueue request;
    JsonArrayRequest JsonArrayRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        rfc = i.getStringExtra("rfc");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_mascota);
        request= Volley.newRequestQueue(this);
        inicializaComponentes();

        txtRfcCliente.setText(rfc);

        btnRegresar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cargaMascotas();
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

        btnCancelar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cargaHome();
                    }
                }
        );

        btnGuardar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cargarWebService();
                    }
                }
        );
    }

    private void cargarWebService() {
        if(txtEspecie.getText().toString().equals("")||
                txtNombre.getText().toString().equals("")||
                txtRaza.getText().toString().equals("")||
                txtRfcCliente.getText().toString().equals("")||
                txtColor.getText().toString().equals("")||
                txtTamaño.getText().toString().equals("")||
                txtSenia.getText().toString().equals("")||
                txtNacimiento.getText().toString().equals("")){
                Toast.makeText(this,"Todos los campos deben de estan correctamente llenados",Toast.LENGTH_SHORT);
        }else{
            progreso= new ProgressDialog(this);
            progreso.setMessage("Cargando...");
            progreso.show();
            String rfcCliente=txtRfcCliente.getText().toString();
            String nombreMascota=txtNombre.getText().toString();
            String nombreMa=nombreMascota.replace(" ","");
            rfcCliente=rfcCliente.replace(" ","");
            for (int i=0; i<3;i++){
                cveMascota+=nombreMa.charAt(i);
            }
            cveMascota+="_";
            for (int i=0; i<3;i++){
                cveMascota+=rfcCliente.charAt(i);
            }

            String url ="https://veterinary-clinic-ws.herokuapp.com/mascotas/create/"+cveMascota+"/"+rfcCliente+
                    "/"+nombreMascota+"/"+txtEspecie.getText().toString()+"/"+txtRaza.getText().toString()+
                    "/"+txtColor.getText().toString()+"/"+txtTamaño.getText().toString()+"/"+txtSenia.getText().toString()+
                    "/"+txtNacimiento.getText().toString()+"";

            url= url.replace(" ", "%20");
            JsonArrayRequest= new JsonArrayRequest(Request.Method.GET,url,null,this,this);
            request.add(JsonArrayRequest);
            cveMascota="";
        }
    }

    private void inicializaComponentes() {
        btnRegresar=(ImageButton) findViewById(R.id.activityRegistroMascotasBtnRegresar);
        btnHome=(ImageButton) findViewById(R.id.activityRegistroMascotasBtnHome);
        btnGuardar=(Button) findViewById(R.id.activity_registro_mascota_btnGuardar);
        btnCancelar=(Button) findViewById(R.id.activity_registro_mascota_btnCancelar);
        txtNombre=(EditText) findViewById(R.id.activity_registro_mascota_txtNom);
        txtColor=(EditText) findViewById(R.id.activity_registro_mascota_txtcolor);
        txtEspecie=(EditText) findViewById(R.id.activity_registro_mascota_txtespecie);
        txtRaza=(EditText) findViewById(R.id.activity_registro_mascota_txtraza);
        txtRfcCliente=(EditText) findViewById(R.id.activity_registro_mascota_txtRfcCliente);
        txtTamaño=(EditText) findViewById(R.id.activity_registro_mascota_txtTamanio);
        txtSenia=(EditText) findViewById(R.id.activity_registro_mascota_txtsena);
        txtNacimiento=(EditText) findViewById(R.id.activity_registro_mascota_txtNacimiento);
    }
    private void cargaHome() {
        cargar = new Intent(this, Principal.class);
        this.startActivity(cargar);
        overridePendingTransition(R.anim.up_in, R.anim.up_out);
    }

    private void cargaMascotas() {
        cargar = new Intent(this, Mascotas.class);
        this.startActivity(cargar);
    }

    //======Métodos de Volley
    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        Toast.makeText(this,"No se pudo registrar"+error.toString(),Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResponse(JSONArray response) {
        progreso.hide();
        Toast.makeText(this,"Registro agregado exitosamente",Toast.LENGTH_SHORT).show();
        txtNombre.setText("");
        txtColor.setText("");
        txtNacimiento.setText("");
        txtSenia.setText("");
        txtRaza.setText("");
        txtEspecie.setText("");
        txtTamaño.setText("");
        txtRfcCliente.setText("");
    }
}
