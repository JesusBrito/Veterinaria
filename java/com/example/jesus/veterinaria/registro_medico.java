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

public class registro_medico extends AppCompatActivity implements Response.Listener<JSONArray>, Response.ErrorListener{
    Button btnGuardar, btnCancelar;
    ImageButton btnRegresar, btnHome,btnImagen;
    EditText txtNombre, txtApellidos, txtRfcMedico, txtDireccion, txtTelefono, txtEmail;
    Intent cargar;
    ProgressDialog progreso;
    RequestQueue request;
    JsonArrayRequest JsonArrayRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_medico);
        request= Volley.newRequestQueue(this);
        inicializaComponentes();

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
        btnImagen.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }
        );
    }
    private void cargarWebService() {
        if(txtRfcMedico.getText().toString().equals("")||
                txtNombre.getText().toString().equals("")||
                txtDireccion.getText().toString().equals("")||
                txtTelefono.getText().toString().equals("")||
                txtEmail.getText().toString().equals("")||
                txtApellidos.getText().toString().equals("")){
            Toast.makeText(this,"Todos los campos deben de estan correctamente llenados",Toast.LENGTH_SHORT);
        }else{
            progreso= new ProgressDialog(this);
            progreso.setMessage("Cargando...");
            progreso.show();

            String url ="https://veterinary-clinic-ws.herokuapp.com/medicos/create/"+txtRfcMedico.getText().toString()+
                    "/"+txtNombre.getText().toString()+" "+txtApellidos.getText().toString()+"/"+
                    txtDireccion.getText().toString()+"/"+txtTelefono.getText().toString()+"/"+txtEmail.getText().toString()+"/";

            url= url.replace(" ", "%20");
            JsonArrayRequest= new JsonArrayRequest(Request.Method.GET,url,null,this,this);
            request.add(JsonArrayRequest);
        }
    }

    private void inicializaComponentes() {
        btnRegresar=(ImageButton) findViewById(R.id.activityRegistroMedicoBtnRegresar);
        btnHome=(ImageButton) findViewById(R.id.activityRegistroMedicoBtnHome);
        btnGuardar=(Button) findViewById(R.id.activity_registro_medico_btnGuardar);
        btnCancelar=(Button) findViewById(R.id.activity_registro_medico_btnCancelar);
        txtDireccion=(EditText) findViewById(R.id.activity_registro_medico_txtDirec);
        txtRfcMedico=(EditText) findViewById(R.id.activity_registro_medico_txtRFC);
        txtApellidos=(EditText) findViewById(R.id.activity_registro_medico_txtApe);
        txtTelefono=(EditText) findViewById(R.id.activity_registro_medico_txtTel);
        txtEmail=(EditText) findViewById(R.id.activity_registro_medico_txtEmail);
        txtNombre=(EditText) findViewById(R.id.activity_registro_medico_txtNom);
        btnImagen=(ImageButton) findViewById(R.id.activity_medicos_btn_img_cargar);
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
        txtEmail.setText("");
        txtTelefono.setText("");
        txtApellidos.setText("");
        txtDireccion.setText("");
        txtRfcMedico.setText("");
    }
    private void cargaHome() {
        cargar = new Intent(this, Principal.class);
        this.startActivity(cargar);
        overridePendingTransition(R.anim.up_in, R.anim.up_out);
    }

    private void cargaMascotas() {
        cargar = new Intent(this, Medicos.class);
        this.startActivity(cargar);
    }
}
