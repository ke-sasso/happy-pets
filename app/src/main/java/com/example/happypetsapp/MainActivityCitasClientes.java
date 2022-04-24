package com.example.happypetsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
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

import java.util.ArrayList;

public class MainActivityCitasClientes extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    GridView grid;

    Button btnNuevaCita;

    ArrayList<Integer> Id = new ArrayList<>();
    ArrayList<String> Nombre = new ArrayList<>();
    ArrayList<String> Detalles = new ArrayList<>();

    Variables v = new Variables();
    RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_citas_clientes);

        grid = (GridView) findViewById(R.id.grid);
        btnNuevaCita = (Button) findViewById(R.id.btnAgendar);

        rq = Volley.newRequestQueue(this);

        btnNuevaCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(MainActivityCitasClientes.this, MainActivityNuevaMascota.class);
                //startActivity(intent);
            }
        });

        ListarCitas();
    }

    public void ListarCitas() {
        String url = "http://pets.jmanza.com/api/appointments?user_id=" + v.getId_usuario();
        JsonRequest jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(MainActivityCitasClientes.this, "Error al cargar citas", Toast.LENGTH_SHORT).show();
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
                        "\nServicio: " + jsonObject.getString("service"));
            }
            CargarGrid();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void CargarGrid() {
        ListAdapter adapter = new ListAdapter(MainActivityCitasClientes.this, Id, Detalles, Nombre);
        grid.setAdapter(adapter);
    }
}