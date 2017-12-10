package com.example.jesus.veterinaria;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class DetailActivity extends AppCompatActivity implements Response.Listener<JSONArray>, Response.ErrorListener {
    TextView txt_rfc, txt_nombre, txt_direccion, txt_telefono, txt_email;
    ProgressDialog dialog;
    RequestQueue request;
    JsonArrayRequest jsonArrayRequest;
    String rfc;
    Button btn_regresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent i = getIntent();
        rfc = i.getStringExtra("id");

        txt_direccion = (TextView) findViewById(R.id.txt_direccion);
        txt_email = (TextView) findViewById(R.id.txt_email);
        txt_nombre = (TextView) findViewById(R.id.txt_nombre);
        txt_rfc = (TextView) findViewById(R.id.txt_rfc);
        txt_telefono = (TextView) findViewById(R.id.txt_telefono);
        btn_regresar = (Button) findViewById(R.id.btn_regresar);

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

        request = Volley.newRequestQueue(this);

        cargarWebService();
    }

    private void cargarWebService() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Obteniendo datos...");
        dialog.show();

        String url = "https://veterinary-clinic-ws.herokuapp.com/clientes/" + rfc;
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
        try {
            JSONObject jsonObject=null;
            jsonObject = response.getJSONObject(0);

            JSONObject fields = jsonObject.getJSONObject("fields");
            String name = fields.optString("nombre_cliente");
            String address = fields.optString("direccion_cliente");
            String phone = fields.optString("telefono_cliente");
            String email = fields.optString("email_cliente");

            txt_rfc.setText(rfc);
            txt_nombre.setText(name);
            txt_direccion.setText(address);
            txt_telefono.setText(phone);
            txt_email.setText(email);

            dialog.hide();

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "No se ha podido establecer conexión con el servidor" +
                    " "+response, Toast.LENGTH_LONG).show();
            dialog.hide();
            Log.d("ERROR: ", e.toString());
        }

    }
}