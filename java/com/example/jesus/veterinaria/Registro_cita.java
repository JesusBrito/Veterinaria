package com.example.jesus.veterinaria;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Registro_cita extends AppCompatActivity {
    RequestQueue request;
    JsonArrayRequest mascotasRequest, medicosRequest, serviciosRequest, detalleCitaRequest;
    ProgressDialog dialog;
    Spinner spinnerServicios, spinnerMascotas, spinnerMedicos;
    ListView listView;
    ScrollView scroll;
    ArrayList<ServicioModelo> arrayListServicios, serviciosSeleccionados;
    ArrayList<MascotaModelo> arrayListMascotas;
    ArrayList<PersonaModelo> arrayListMedicos;
    List<String> listServicios, listMascotas, listMedicos;
    ImageButton principalServicioAdd;
    int positionSpinnerServicios, positionSpinnerMascotas, positionSpinnerMedicos;
    ImageButton btnGuardar, btnCancelar;
    String fecha, hora, pkCita, idMascota, idMedico;
    SimpleDateFormat simpleDateFormat, simpleHourFormat;
    Calendar calendar;
    float total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_cita);

        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        simpleHourFormat = new SimpleDateFormat("HH:mm");

        spinnerServicios = (Spinner) findViewById(R.id.spinnerServicios);
        spinnerMascotas = (Spinner) findViewById(R.id.spinnerMascotas);
        spinnerMedicos = (Spinner) findViewById(R.id.spinnerMedicos);
        listView = (ListView) findViewById(R.id.listView);
        principalServicioAdd = (ImageButton) findViewById(R.id.principalServicioAdd);
        btnGuardar = (ImageButton) findViewById(R.id.activity_registro_cita_btnGuardar);
        btnCancelar = (ImageButton) findViewById(R.id.activity_registro_cita_btnCancelar);
        scroll = (ScrollView) findViewById(R.id.miScroll);
        arrayListServicios = new ArrayList<>();
        arrayListMascotas = new ArrayList<>();
        arrayListMedicos = new ArrayList<>();
        serviciosSeleccionados = new ArrayList<>();
        listServicios = new ArrayList<>();
        listMascotas = new ArrayList<>();
        listMedicos = new ArrayList<>();

        final ServicioAdapter servicioAdapter = new ServicioAdapter(this, serviciosSeleccionados);
        listView.setAdapter(servicioAdapter);

        principalServicioAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "Agregar", Toast.LENGTH_SHORT).show();
                        serviciosSeleccionados.add(arrayListServicios.get(positionSpinnerServicios));
                        servicioAdapter.notifyDataSetChanged();
                    }
                }
        );

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            //Posible error
                            builder = new AlertDialog.Builder(Registro_cita.this, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(Registro_cita.this);
                        }
                        builder.setTitle("Eliminar servicio")
                                .setMessage("¿Desea eliminar el servicio " + serviciosSeleccionados.get(position).getName() + "?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        serviciosSeleccionados.remove(position);
                                        servicioAdapter.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {}
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }
        );

        spinnerServicios.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        positionSpinnerServicios = position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        spinnerMascotas.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        positionSpinnerMascotas = position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        spinnerMedicos.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        positionSpinnerMedicos = position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        btnGuardar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (serviciosSeleccionados.size() == 0) {
                            Toast.makeText(Registro_cita.this, "Selecciona algún servicio", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog = new ProgressDialog(Registro_cita.this);
                            dialog.setMessage("Guardando datos...");
                            dialog.show();

                            idMascota = arrayListMascotas.get(positionSpinnerMascotas).getId();
                            idMedico = arrayListMedicos.get(positionSpinnerMedicos).getId();

                            calendar = Calendar.getInstance();
                            fecha = simpleDateFormat.format(calendar.getTime());
                            hora = simpleHourFormat.format(calendar.getTime());

                            for (int i=0; i < serviciosSeleccionados.size(); i++) {
                                total += Float.parseFloat(serviciosSeleccionados.get(i).getPrecio());
                            }

                            guardarCita();

                            dialog.hide();
                            Toast.makeText(Registro_cita.this, "Cita guardada", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        scroll.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                findViewById(R.id.listView).getParent()
                        .requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });
        listView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        request = Volley.newRequestQueue(this);
        solicitarDatos();
    }

    private void solicitarDatos() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Obteniendo datos...");
        dialog.show();

        String url = "https://veterinary-clinic-ws.herokuapp.com/mascotas/";
        mascotasRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                String id = jsonObject.optString("pk");
                                JSONObject fields = jsonObject.getJSONObject("fields");
                                String nombre = fields.optString("nombre_mascota");

                                MascotaModelo mascota = new MascotaModelo(id, nombre);

                                arrayListMascotas.add(mascota);
                                listMascotas.add(nombre);
                            }


                            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_color , listMascotas);
                            spinnerMascotas.setAdapter(spinnerAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "No se ha podido establecer conexión con el servidor" +
                                    " "+response, Toast.LENGTH_LONG).show();
                            dialog.hide();
                            Log.d("ERROR: ", e.toString());
                        }
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
                });

        url = "https://veterinary-clinic-ws.herokuapp.com/medicos/";
        medicosRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                String id = jsonObject.optString("pk");
                                JSONObject fields = jsonObject.getJSONObject("fields");
                                String nombre = fields.optString("nombre_medico");

                                PersonaModelo medico = new PersonaModelo(id, nombre);

                                arrayListMedicos.add(medico);
                                listMedicos.add(nombre);
                            }

                            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_color, listMedicos);
                            spinnerMedicos.setAdapter(spinnerAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "No se ha podido establecer conexión con el servidor" +
                                    " "+response, Toast.LENGTH_LONG).show();
                            dialog.hide();
                            Log.d("ERROR: ", e.toString());
                        }
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
                });

        url = "https://veterinary-clinic-ws.herokuapp.com/servicios/";
        serviciosRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                String clave = jsonObject.optString("pk");
                                JSONObject fields = jsonObject.getJSONObject("fields");
                                String descripcion = fields.optString("descripcion_servicio");
                                String precio = fields.optString("precio_servicio");
                                ServicioModelo servicio = new ServicioModelo(clave, descripcion, precio);

                                arrayListServicios.add(servicio);
                                listServicios.add(descripcion);
                            }

                            ArrayAdapter<String> spinnerServiciosAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_color, listServicios);
                            spinnerServicios.setAdapter(spinnerServiciosAdapter);

                            dialog.hide();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "No se ha podido establecer conexión con el servidor" +
                                    " "+response, Toast.LENGTH_LONG).show();
                            dialog.hide();
                            Log.d("ERROR: ", e.toString());
                        }
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
                });

        request.add(mascotasRequest);
        request.add(medicosRequest);
        request.add(serviciosRequest);
    }

    private void guardarCita() {
        String url = "https://veterinary-clinic-ws.herokuapp.com/citas/create/" + fecha + "/" + idMedico + "/" + idMascota + "/" + hora +"/esta%20mal/" + total;
        detalleCitaRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(0);
                            pkCita = jsonObject.getString("pk");
                            for (int i=0; i < serviciosSeleccionados.size(); i++) {
                                guardarServicio(serviciosSeleccionados.get(i));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "No se ha podido establecer conexión con el servidor" +
                                    " "+response, Toast.LENGTH_LONG).show();
                            dialog.hide();
                            Log.d("ERROR: ", e.toString());
                        }
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
                });

        request.add(detalleCitaRequest);
    }
    private void guardarServicio(ServicioModelo servicio) {
        String url = "https://veterinary-clinic-ws.herokuapp.com/detalle_citas/create/" + pkCita + "/" + fecha + "/" + servicio.getId() + "/1/" + servicio.getPrecio();
        detalleCitaRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "No se ha podido establecer conexión con el servidor" +
                                    " "+response, Toast.LENGTH_LONG).show();
                            dialog.hide();
                            Log.d("ERROR: ", e.toString());
                        }
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
                });

        request.add(detalleCitaRequest);
    }
}