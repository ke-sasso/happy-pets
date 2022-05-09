package com.example.happypetsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivityCitasDoctor extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{

    GridView grid;
    Spinner Estados;

    TextView txtFecha;
    ImageView Calendar;
    Button Filtrar;

    ArrayList<Integer> Id = new ArrayList<>();
    ArrayList<String> Nombre = new ArrayList<>();
    ArrayList<String> Detalles = new ArrayList<>();
    ArrayList<String> Estado = new ArrayList<>();

    Variables v = new Variables();
    RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_citas_doctor);

        grid = (GridView) findViewById(R.id.grid);
        txtFecha = (TextView)findViewById(R.id.txtFecha);
        Calendar = (ImageView)findViewById(R.id.Calendar);
        Estados = (Spinner)findViewById(R.id.Estados);
        Filtrar = (Button)findViewById(R.id.btnFiltrar);

        txtFecha.setText(ObtenerFecha());

        rq = Volley.newRequestQueue(this);

        Calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        Filtrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Id.clear();
                Nombre.clear();
                Detalles.clear();
                String filtro = txtFecha.getText().toString();
                if (!(Estados.getSelectedItemPosition()==0)){
                    filtro += "&status="+Estados.getSelectedItem().toString();
                }
                ListarCitas(filtro);
            }
        });

        ListarCitas(ObtenerFecha());
        ListarEstados();
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

        newFragment.show(MainActivityCitasDoctor.this.getSupportFragmentManager(), "datePicker");
    }

    public String ObtenerFecha(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void ListarEstados(){
        Estado.add("Todos");
        Estado.add("Pendiente");
        Estado.add("Cerrada");
        Estado.add("Cancelada");
        Estados.setAdapter(new ArrayAdapter<String>(MainActivityCitasDoctor.this, android.R.layout.simple_spinner_dropdown_item, Estado));
    }

    public void ListarCitas(String filtro) {
        String url = "http://pets.jmanza.com/api/appointments?date=" + filtro;
        JsonRequest jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(MainActivityCitasDoctor.this, "Error al cargar citas", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        JSONArray jsonArray = response.optJSONArray("data");
        JSONObject jsonObject = null;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                Id.add(jsonObject.getInt("id"));
                Nombre.add("Dueño: " + jsonObject.getString("user") +  "\nMascota: " + jsonObject.getString("pet"));
                Detalles.add("Horario: " + jsonObject.getString("date") +
                        "\nServicio: " + jsonObject.getString("service")+
                        "\nEstado: " + jsonObject.getString("status"));
            }
            CargarGrid();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void CargarGrid() {
        v.setIsCitas(1);
        ListAdapter adapter = new ListAdapter(MainActivityCitasDoctor.this, Id, Detalles, Nombre);
        grid.setAdapter(adapter);
    }

}