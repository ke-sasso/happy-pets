package com.example.happypetsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener{

    EditText Usuario, Clave;
    Button btnIniciar, btnRegistrar;

    RequestQueue rq;

    Variables v = new Variables();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Usuario = (EditText) findViewById(R.id.Usuario);
        Clave = (EditText) findViewById(R.id.Clave);
        btnIniciar = (Button) findViewById(R.id.btnIniciar);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);

        rq = Volley.newRequestQueue(this);

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validar();
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivityRegistro.class);
                startActivity(intent);
            }
        });

    }

    public void Validar(){
        String url ="http://pets.jmanza.com/api/login";

        StringRequest request = new StringRequest(Request.Method.POST, url, this, this){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", Usuario.getText().toString());
                params.put("password", Clave.getText().toString());
                return params;
            }
        };
        rq.add(request);

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(MainActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(String response) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            String name = jsonObject.getString("name");
            v.setId_usuario(jsonObject.getInt("id"));
            Toast.makeText(MainActivity.this, "¡Hola, " + name + "!", Toast.LENGTH_SHORT).show();
            Entrar();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void Entrar(){
        Usuario.setText("");
        Clave.setText("");
        Intent intent = new Intent(MainActivity.this, MainActivityPrincipalClientes.class);
        startActivity(intent);
    }
}