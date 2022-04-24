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

public class GridAdapter extends BaseAdapter {
    Context contexto;
    List<Integer> ListaId;
    List<String> ListaImagenes;
    List<String> ListaNombre;

    Variables v = new Variables();

    public GridAdapter(Context contexto, List<Integer> listaId, List<String> listaImagenes, List<String> listaNombre) {
        this.contexto = contexto;
        ListaImagenes = listaImagenes;
        ListaNombre = listaNombre;
        ListaId = listaId;
    }

    @Override
    public int getCount() {
        return ListaImagenes.size();
    }

    @Override
    public Object getItem(int i) {
        return ListaImagenes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vista = view;
        LayoutInflater inflate = LayoutInflater.from(contexto);
        vista = inflate.inflate(R.layout.grid_items,null);

        ImageView Item = (ImageView) vista.findViewById(R.id.imgProducto);
        Picasso.get().load(ListaImagenes.get(i)).into(Item);

        TextView txtNombre = (TextView) vista.findViewById(R.id.txtNombre);
        txtNombre.setText(ListaNombre.get(i));

        Item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (v.getIsmenu()==1){
                    Menu(i);
                }else if (v.getIsservicios()==1)
                    Servicios();
            }
        });
        return vista;
    }

    public void Menu(int index){
        if (index==0){
            Intent intent = new Intent(contexto, MainActivityMascotas.class);
            contexto.startActivity(intent);
        } else if (index==1){
            Intent intent = new Intent(contexto, MainActivityServiciosClientes.class);
            contexto.startActivity(intent);
        } else if (index==2){
            Intent intent = new Intent(contexto, MainActivityCitasClientes.class);
            contexto.startActivity(intent);
        } else if (index==3){
            Intent intent = new Intent(contexto, MainActivityPerfilCliente.class);
            contexto.startActivity(intent);
        }
    }
    public void Servicios(){
        //Intent intent = new Intent(contexto, MainActivityDetalleServicios.class);
        //contexto.startActivity(intent);
    }
}
