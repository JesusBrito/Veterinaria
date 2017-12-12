package com.example.jesus.veterinaria;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.HashMap;
import java.util.Map;

public class Modificar_medico extends AppCompatActivity {
    EditText txt_rfc, txt_nombre, txt_direccion, txt_telefono, txt_email;
    RequestQueue request;
    JsonArrayRequest jsonClienteArrayRequest,jsonArrayGuardarRequest;
    JsonObjectRequest jsonEliminarObjectRequest;
    String rfc;
    Button btnGuardar, btnEliminar;
    ImageButton btn_regresar, btn_home;
    CheckBox chEditar;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_medico);

        Intent i = getIntent();
        rfc = i.getStringExtra("id");
        inicializaComponentes();
        btn_regresar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent back = new Intent(getApplicationContext(), Medicos.class);
                        back.putExtra("id", txt_rfc.getText().toString());
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
                            txt_direccion.setEnabled(true);
                            txt_telefono.setEnabled(true);
                            txt_email.setEnabled(true);
                            btnGuardar.setEnabled(true);
                        }else{
                            txt_nombre.setEnabled(false);
                            txt_direccion.setEnabled(false);
                            txt_telefono.setEnabled(false);
                            txt_email.setEnabled(false);
                            btnGuardar.setEnabled(false);
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

    private void inicializaComponentes(){
        txt_direccion = (EditText) findViewById(R.id.detalle_medico_txt_direccion);
        txt_email = (EditText) findViewById(R.id.detalle_medico_txt_email);
        txt_nombre = (EditText) findViewById(R.id.detalle_medico_txt_nombre);
        txt_rfc = (EditText) findViewById(R.id.detalle_medico_txt_rfc);
        txt_telefono = (EditText) findViewById(R.id.detalle_medico_txt_telefono);
        btn_regresar = (ImageButton) findViewById(R.id.activityDetalleMedicoBtnRegresar);
        btn_home  = (ImageButton) findViewById(R.id.activityDetalleMedicoBtnHome);
        chEditar= (CheckBox) findViewById(R.id.activityDetalleMedicoChEditar);
        btnGuardar =(Button) findViewById(R.id.activityDetalleMedicoBtnGuardar);
        btnEliminar =(Button) findViewById(R.id.activityDetalleMedicoBtnEliminar);

    }
    private void cargarWebServiceGuardar() {
        if(txt_rfc.getText().toString().equals("")||txt_nombre.getText().toString().equals("")||
                txt_direccion.getText().toString().equals("")||txt_telefono.getText().toString().equals("")||
                txt_email.getText().toString().equals("")){
            Toast.makeText(this,"Todos los campos deben de estan correctamente llenados",Toast.LENGTH_SHORT);
        }else{
            dialog = new ProgressDialog(this);
            dialog.setMessage("Cargando...");
            dialog.show();
            String url = "https://veterinary-clinic-ws.herokuapp.com/medicos/update/";
            url= url.replace(" ", "%20");
            StringRequest jsonArrayGuardarRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
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
            ){
                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> postParam = new HashMap<String, String>();
                    postParam.put("rfc", txt_rfc.getText().toString());
                    postParam.put("nombre", txt_nombre.getText().toString());
                    postParam.put("direccion", txt_direccion.getText().toString());
                    postParam.put("telefono", txt_telefono.getText().toString());
                    postParam.put("email", txt_email.getText().toString());
                    return postParam;
                }
            };
            request.add(jsonArrayGuardarRequest);
        }
    }
    private void cargarWebService() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Cargando datos...");
        dialog.show();
        String url = "https://veterinary-clinic-ws.herokuapp.com/medicos/";
        StringRequest jsonClienteArrayRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseJSON = null;
                            responseJSON = new JSONObject(response);
                            JSONArray results = responseJSON.optJSONArray("objects");
                            JSONObject jsonObject = results.optJSONObject(0);

                            String name = jsonObject.optString("nombre_medico");
                            String address = jsonObject.optString("direccion_medico");
                            String phone = jsonObject.optString("telefono_medico");
                            String email = jsonObject.optString("email_medico");

                            txt_rfc.setText(rfc);
                            txt_nombre.setText(name);
                            txt_direccion.setText(address);
                            txt_telefono.setText(phone);
                            txt_email.setText(email);
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
        request.add(jsonClienteArrayRequest);
    }
    private void cargaWebServiceEliminar(){
        dialog = new ProgressDialog(this);
        dialog.setMessage("Eliminando...");
        dialog.show();

        String url = "https://veterinary-clinic-ws.herokuapp.com/medicos/delete/";
        StringRequest jsonEliminarObjectRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent back = new Intent(getApplicationContext(), Medicos.class);
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
        request.add(jsonEliminarObjectRequest);
    }
}
