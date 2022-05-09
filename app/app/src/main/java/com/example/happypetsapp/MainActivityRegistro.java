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

public class MainActivityRegistro extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener{

    EditText Nombre, Email, Clave, Clave2;
    Button btnGuardar;

    RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_registro);

        Nombre = (EditText) findViewById(R.id.Nombre);
        Email = (EditText) findViewById(R.id.Email);
        Clave = (EditText) findViewById(R.id.Clave);
        Clave2 = (EditText) findViewById(R.id.Clave2);

        rq = Volley.newRequestQueue(this);

        btnGuardar = (Button) findViewById(R.id.btnGuardar);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Clave.getText().toString().contentEquals(Clave2.getText().toString()))
                    Registrar();
                else
                    Toast.makeText(MainActivityRegistro.this, "La contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void Registrar(){
        String url ="http://pets.jmanza.com/api/register";

        StringRequest request = new StringRequest(Request.Method.POST, url, this, this){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", Nombre.getText().toString());
                params.put("email", Email.getText().toString());
                params.put("password", Clave.getText().toString());
                return params;
            }
        };
        rq.add(request);

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(MainActivityRegistro.this, "Error al registrarse", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(String response) {
        Toast.makeText(MainActivityRegistro.this, "Se ha regristrado correctamente", Toast.LENGTH_SHORT).show();
        MainActivityRegistro.this.finish();
    }
}