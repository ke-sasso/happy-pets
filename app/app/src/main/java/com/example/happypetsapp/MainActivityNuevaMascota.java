package com.example.happypetsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivityNuevaMascota extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener{

    EditText Nombre, Especie, Nacimiento;
    Button btnGuardar;

    Variables v = new Variables();

    RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nueva_mascota);

        Nombre = (EditText) findViewById(R.id.Nombre);
        Especie = (EditText) findViewById(R.id.Especie);
        Nacimiento = (EditText) findViewById(R.id.Nacimiento);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);

        rq = Volley.newRequestQueue(this);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Registrar();
            }
        });
    }

    public void Registrar(){
        String url ="http://pets.jmanza.com/api/pets";

        StringRequest request = new StringRequest(Request.Method.POST, url, this, this){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", Nombre.getText().toString());
                params.put("species", Especie.getText().toString());
                params.put("birthday", Nacimiento.getText().toString());
                params.put("user_id", Integer.toString(v.getId_usuario()));
                return params;
            }
        };
        rq.add(request);

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(MainActivityNuevaMascota.this, "Error al registrar mascota ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(String response) {
        Toast.makeText(MainActivityNuevaMascota.this, "Se ha regristrado correctamente", Toast.LENGTH_SHORT).show();
        MainActivityNuevaMascota.this.finish();
        Intent intent = new Intent(MainActivityNuevaMascota.this, MainActivityMascotas.class);
        MainActivityNuevaMascota.this.startActivity(intent);
    }
    public void onBackPressed() {
        super.onBackPressed();
        MainActivityNuevaMascota.this.finish();
        Intent intent = new Intent(MainActivityNuevaMascota.this, MainActivityMascotas.class);
        startActivity(intent);
    }
}