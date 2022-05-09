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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivityMascotas extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{

    GridView grid;

    Button btnNuevaMascota;

    ArrayList<Integer> Id =new ArrayList<>();
    ArrayList<String> Nombre =new ArrayList<>();
    ArrayList<String> Detalles =new ArrayList<>();

    Variables v = new Variables();
    RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mascotas);

        grid = (GridView) findViewById(R.id.grid);
        btnNuevaMascota = (Button) findViewById(R.id.btnNueva);

        rq = Volley.newRequestQueue(this);

        btnNuevaMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivityMascotas.this.finish();
                Intent intent = new Intent(MainActivityMascotas.this, MainActivityNuevaMascota.class);
                startActivity(intent);
            }
        });

        ListarMascotas();
    }

    public void ListarMascotas(){
        String url ="http://pets.jmanza.com/api/pets?user_id="+v.getId_usuario();
        JsonRequest jrq = new JsonObjectRequest(Request.Method.GET, url, null,this, this);
        rq.add(jrq);

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(MainActivityMascotas.this, "Error al cargar mascotas", Toast.LENGTH_SHORT).show();
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
                Detalles.add("Especie: " + jsonObject.getString("species") +
                        "\nNacimiento: " + jsonObject.getString("birthday"));
            }
            CargarGrid();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void CargarGrid(){
        v.setIsCitas(0);
        ListAdapter adapter = new ListAdapter(MainActivityMascotas.this, Id, Detalles, Nombre);
        grid.setAdapter(adapter);
    }
}