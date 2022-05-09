package com.example.happypetsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;

public class MainActivityMenuDoctor extends AppCompatActivity {

    GridView grid;

    ArrayList<Integer> Id =new ArrayList<>();
    ArrayList<String> Nombre =new ArrayList<>();
    ArrayList<String> Imagenes =new ArrayList<>();

    Variables v = new Variables();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_doctor);

        grid = (GridView) findViewById(R.id.grid);

        CargarGrid();
    }

    public void CargarGrid(){
        v.setIsmenu(1);
        v.setIsservicios(0);

        Id.add(0);
        Id.add(1);

        Imagenes.add("https://pets.jmanza.com/img/servicios.jpeg");
        Imagenes.add("https://pets.jmanza.com/img/citas.jpeg");

        Nombre.add("Servicios");
        Nombre.add("Citas");

        GridAdapter adapter = new GridAdapter(MainActivityMenuDoctor.this, Id, Imagenes, Nombre);
        grid.setAdapter(adapter);
    }
}