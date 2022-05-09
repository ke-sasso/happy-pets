package com.example.happypetsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivityNuevaCita extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{

    Spinner Mascotas, Servicios, Horas;
    TextView txtFecha;
    ImageView Calendar;
    Button btnGuardar;

    ArrayList<Integer> Id =new ArrayList<>();
    ArrayList<String> Nombre =new ArrayList<>();

    ArrayList<Integer> IdServicio =new ArrayList<>();
    ArrayList<String> Servicio =new ArrayList<>();

    ArrayList<String> Hora =new ArrayList<>();

    Variables v = new Variables();
    RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nueva_cita);

        Mascotas = (Spinner)findViewById(R.id.Mascota);
        Servicios = (Spinner)findViewById(R.id.Servicio);
        Horas = (Spinner)findViewById(R.id.Hora);
        txtFecha = (TextView)findViewById(R.id.txtFecha);
        Calendar = (ImageView)findViewById(R.id.Calendar);
        btnGuardar = (Button)findViewById(R.id.btnGuardar);

        txtFecha.setText(ObtenerFecha());

        rq = Volley.newRequestQueue(this);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AgendarCita();
            }
        });

        Calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        ListarMascotas();
        ListarServicios();
        ListarHoras(ObtenerFecha());
    }

    public void AgendarCita(){
        String url ="http://pets.jmanza.com/api/appointments";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivityNuevaCita.this, "Cita agendada", Toast.LENGTH_SHORT).show();
                MainActivityNuevaCita.this.finish();
                Intent intent = new Intent(MainActivityNuevaCita.this, MainActivityCitasClientes.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivityNuevaCita.this, "Error al agendar cita", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("date", txtFecha.getText().toString() + " " + Horas.getSelectedItem().toString());
                params.put("status", "pendiente");
                params.put("user_id", String.valueOf(v.getId_usuario()));
                params.put("doctor_id", "2");
                params.put("pet_id", Id.get(Mascotas.getSelectedItemPosition()).toString());
                params.put("service_id", IdServicio.get(Servicios.getSelectedItemPosition()).toString());
                return params;
            }
        };
        rq.add(request);
    }

    public String ObtenerFecha(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                NumberFormat f = new DecimalFormat("00");
                final String selectedDate = year + "-" + f.format((month+1)) + "-" + f.format(day);
                txtFecha.setText(selectedDate);
            }
        });

        newFragment.show(MainActivityNuevaCita.this.getSupportFragmentManager(), "datePicker");
    }

    public void ListarMascotas(){
        String url ="http://pets.jmanza.com/api/pets?user_id="+v.getId_usuario();
        JsonRequest jrq = new JsonObjectRequest(Request.Method.GET, url, null,this, this);
        rq.add(jrq);

    }

    public void ListarServicios(){
        String url ="http://pets.jmanza.com/api/services";
        JsonRequest jrq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = response.optJSONArray("data");
                JSONObject jsonObject = null;
                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        IdServicio.add(jsonObject.getInt("id"));
                        Servicio.add(jsonObject.getString("name"));
                    }
                    Servicios.setAdapter(new ArrayAdapter<String>(MainActivityNuevaCita.this, android.R.layout.simple_spinner_dropdown_item, Servicio));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivityNuevaCita.this, "Error al cargar los servicios", Toast.LENGTH_SHORT).show();
            }
        });
        rq.add(jrq);

    }

    public void ListarHoras(String fecha){
        Hora.clear();
        Hora.add("08:00");
        Hora.add("09:00");
        Hora.add("10:00");
        Hora.add("11:00");
        Hora.add("13:00");
        Hora.add("14:00");
        Hora.add("15:00");
        Hora.add("16:00");
        Hora.add("17:00");
        Hora.add("18:00");
        Hora.add("19:00");
        Horas.setAdapter(new ArrayAdapter<String>(MainActivityNuevaCita.this, android.R.layout.simple_spinner_dropdown_item, Hora));
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(MainActivityNuevaCita.this, "Error al cargar las mascotas", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        JSONArray jsonArray = response.optJSONArray("data");
        JSONObject jsonObject = null;
        try {
            for (int i=0; i<jsonArray.length(); i++){
                jsonObject = jsonArray.getJSONObject(i);
                Id.add(jsonObject.getInt("id"));
                Nombre.add(jsonObject.getString("name"));
            }
            Mascotas.setAdapter(new ArrayAdapter<String>(MainActivityNuevaCita.this, android.R.layout.simple_spinner_dropdown_item, Nombre));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivityNuevaCita.this.finish();
        Intent intent = new Intent(MainActivityNuevaCita.this, MainActivityCitasClientes.class);
        startActivity(intent);
    }
}