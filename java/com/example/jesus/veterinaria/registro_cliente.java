package com.example.jesus.veterinaria;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.HashMap;
import java.util.Map;

public class registro_cliente extends AppCompatActivity {
    Button btnGuardar, btnCancelar;
    ImageButton btnRegresar, btnHome, btnImagen;
    EditText txtNom, txtApe, txtTel, txtDirec, txtEmail, txtRFC;
    Intent cargar;
    ProgressDialog progreso;
    RequestQueue request;
    JsonArrayRequest JsonArrayRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro__cliente);
        inicializaComponentes();
        request= Volley.newRequestQueue(this);
        btnRegresar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cargaClientes();
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

        btnCancelar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cargaHome();
                        overridePendingTransition(R.anim.up_in, R.anim.up_out);
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
        //INICIAR WEB SERVICE
        if(txtRFC.getText().toString().equals("")||txtNom.getText().toString().equals("")||txtApe.getText().toString().equals("")||
                txtDirec.getText().toString().equals("")||txtTel.getText().toString().equals("")||
                txtEmail.getText().toString().equals("")){
            Toast.makeText(this,"Todos los campos deben de estan correctamente llenados",Toast.LENGTH_SHORT);
        }else{
            progreso= new ProgressDialog(this);
            progreso.setMessage("Cargando...");
            progreso.show();
            String url ="https://veterinary-clinic-ws.herokuapp.com/clientes/create/";

            url= url.replace(" ", "%20");
            StringRequest JsonArrayRequest= new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progreso.hide();
                            Toast.makeText(getApplicationContext(), "Registro agregado exitosamente", Toast.LENGTH_SHORT).show();
                            txtNom.setText("");
                            txtApe.setText("");
                            txtRFC.setText("");
                            txtDirec.setText("");
                            txtTel.setText("");
                            txtEmail.setText("");
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progreso.hide();
                            Toast.makeText(getApplicationContext(),"No se pudo registrar"+error.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> postParam = new HashMap<String, String>();
                    postParam.put("rfc", txtRFC.getText().toString());
                    postParam.put("nombre", txtNom.getText().toString());
                    postParam.put("direccion", txtDirec.getText().toString());
                    postParam.put("telefono", txtTel.getText().toString());
                    postParam.put("email", txtEmail.getText().toString());
                    postParam.put("imagen","");
                    return postParam;
                }
            };
            request.add(JsonArrayRequest);
        }
    }

    private void inicializaComponentes() {
        btnRegresar=(ImageButton) findViewById(R.id.activityRegistroClientesBtnRegresar);
        btnHome=(ImageButton) findViewById(R.id.activityRegistroClientesBtnHome);
        btnGuardar=(Button) findViewById(R.id.activity_registro_cliente_btnGuardar);
        btnCancelar=(Button) findViewById(R.id.activity_registro_cliente_btnCancelar);
        txtNom=(EditText) findViewById(R.id.activity_registro_cliente_txtNom);
        txtApe=(EditText) findViewById(R.id.activity_registro_cliente_txtApe);
        txtTel=(EditText) findViewById(R.id.activity_registro_cliente_txtTel);
        txtDirec=(EditText) findViewById(R.id.activity_registro_cliente_txtDirec);
        txtEmail=(EditText) findViewById(R.id.activity_registro_cliente_txtEmail);
        txtRFC=(EditText) findViewById(R.id.activity_registro_cliente_txtRFC);
        btnImagen=(ImageButton) findViewById(R.id.activity_cliente_btn_img_cargar);
    }
    private void cargaHome() {
        cargar = new Intent(this, Principal.class);
        this.startActivity(cargar);
    }

    private void cargaClientes() {
        cargar = new Intent(this, Clientes.class);
        this.startActivity(cargar);
    }
}
