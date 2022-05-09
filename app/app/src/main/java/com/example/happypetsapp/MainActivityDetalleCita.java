package com.example.happypetsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivityDetalleCita extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{

    TextView txtDatos;
    EditText txtComentarios;
    Spinner Estados;
    Button btnGuardar;
    ArrayList<String> Estado = new ArrayList<>();
    Variables v = new Variables();
    RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_detalle_cita);

        rq = Volley.newRequestQueue(this);
        txtDatos = (TextView) findViewById(R.id.txtDatos);
        txtComentarios = (EditText) findViewById(R.id.txtComentarios);
        Estados = (Spinner)findViewById(R.id.Estados);
        btnGuardar = (Button)findViewById(R.id.btnGuardar);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Actualizar();
            }
        });

        Mostrar();
    }

    public void Actualizar(){
        String url ="http://pets.jmanza.com/api/appointments/"+v.getIdCita();
        Toast.makeText(MainActivityDetalleCita.this, url, Toast.LENGTH_SHORT).show();

        StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivityDetalleCita.this, "Información actualizada", Toast.LENGTH_SHORT).show();
                MainActivityDetalleCita.this.finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivityDetalleCita.this, "Error al actualizar información", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("status", Estados.getSelectedItem().toString());
                params.put("description", txtComentarios.getText().toString());
                return params;
            }
        };
        rq.add(request);
    }

    public void Mostrar(){
        String url ="http://pets.jmanza.com/api/appointments/"+v.getIdCita();
        JsonRequest jrq = new JsonObjectRequest(Request.Method.GET, url, null,this, this);
        rq.add(jrq);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(MainActivityDetalleCita.this, "Error al cargar información", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        JSONObject jsonObject = null;
        try {
            jsonObject = response.getJSONObject("data");
            txtDatos.setText("Cliente: " + jsonObject.getString("user") +
                    "\n\nMascota: " + jsonObject.getString("pet") +
                    "\n\nEspecie: " + jsonObject.getString("species") +
                    "\n\nServicio: " + jsonObject.getString("service") +
                    "\n\nFecha y hora: " + jsonObject.getString("date") +
                    "\n\nPrecio: $" + jsonObject.getString("price"));

            String comentario = jsonObject.getString("description");
            if (!comentario.contentEquals("null"))
                txtComentarios.setText(comentario);

            Estado.add(jsonObject.getString("status"));
            Estado.add("cerrada");
            Estado.add("cancelada");
            Estados.setAdapter(new ArrayAdapter<String>(MainActivityDetalleCita.this, android.R.layout.simple_spinner_dropdown_item, Estado));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}