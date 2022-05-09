package com.example.happypetsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class MainActivityPerfilCliente extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{

    EditText Nombre, Email;
    Button btnGuardar;

    Variables v = new Variables();
    RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_perfil_cliente);

        Nombre = (EditText) findViewById(R.id.Nombre);
        Email = (EditText) findViewById(R.id.Email);

        rq = Volley.newRequestQueue(this);

        btnGuardar = (Button) findViewById(R.id.btnGuardar);

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

        StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivityPerfilCliente.this, "Información actualizada", Toast.LENGTH_SHORT).show();
                MainActivityPerfilCliente.this.finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivityPerfilCliente.this, "Error al actualizar información", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", Nombre.getText().toString());
                params.put("email", Email.getText().toString());
                return params;
            }
        };
        rq.add(request);
    }

    public void Mostrar(){
        String url ="http://pets.jmanza.com/api/users/"+v.getId_usuario();
        JsonRequest jrq = new JsonObjectRequest(Request.Method.GET, url, null,this, this);
        rq.add(jrq);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(MainActivityPerfilCliente.this, "Error al cargar información", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        JSONObject jsonObject = null;
        try {
            jsonObject = response.getJSONObject("data");
            Nombre.setText(jsonObject.getString("name"));
            Email.setText(jsonObject.getString("email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}