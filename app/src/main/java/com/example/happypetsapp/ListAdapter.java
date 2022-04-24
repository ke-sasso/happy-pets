package com.example.happypetsapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ListAdapter extends BaseAdapter {
    Context contexto;
    List<Integer> ListaId;
    List<String> ListaDetalle;
    List<String> ListaNombre;

    Variables v = new Variables();

    public ListAdapter(Context contexto, List<Integer> listaId, List<String> listaDetalle, List<String> listaNombre) {
        this.contexto = contexto;
        ListaDetalle = listaDetalle;
        ListaNombre = listaNombre;
        ListaId = listaId;
    }

    @Override
    public int getCount() {
        return ListaNombre.size();
    }

    @Override
    public Object getItem(int i) {
        return ListaNombre.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vista = view;
        LayoutInflater inflate = LayoutInflater.from(contexto);
        vista = inflate.inflate(R.layout.list_items,null);

        TextView txtNombre = (TextView) vista.findViewById(R.id.txtNombre);
        txtNombre.setText(ListaNombre.get(i));
        TextView txtDetalle = (TextView) vista.findViewById(R.id.txtDetalle);
        txtDetalle.setText(ListaDetalle.get(i));

        txtNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return vista;
    }
}
