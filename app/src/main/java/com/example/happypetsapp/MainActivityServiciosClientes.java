package com.example.happypetsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class MainActivityServiciosClientes extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{

    GridView grid;

    ArrayList<Integer> Id =new ArrayList<>();
    ArrayList<String> Nombre =new ArrayList<>();
    ArrayList<String> Imagenes =new ArrayList<>();

    Variables v = new Variables();
    RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_servicios_clientes);

        grid = (GridView) findViewById(R.id.grid);

        rq = Volley.newRequestQueue(this);

        ListarServicios();
    }

    public void ListarServicios(){
        String url ="http://pets.jmanza.com/api/services";
        JsonRequest jrq = new JsonObjectRequest(Request.Method.GET, url, null,this, this);
        rq.add(jrq);

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(MainActivityServiciosClientes.this, "Error al cargar los servicios", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        JSONArray jsonArray = response.optJSONArray("data");
        JSONObject jsonObject = null;
        try {
            for (int i=0; i<jsonArray.length(); i++){
                jsonObject = jsonArray.getJSONObject(i);
                Id.add(jsonObject.getInt("id"));
                Nombre.add(jsonObject.getString("name") + "\n" + jsonObject.getString("price"));
                Imagenes.add(jsonObject.getString("photo"));
            }
            CargarGrid();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        v.setIsmenu(1);
        v.setIsservicios(0);
        super.onBackPressed();
    }

    public void CargarGrid(){
        v.setIsmenu(0);
        v.setIsservicios(1);

        GridAdapter adapter = new GridAdapter(MainActivityServiciosClientes.this, Id, Imagenes, Nombre);
        grid.setAdapter(adapter);
    }
}