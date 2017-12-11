package com.example.jesus.veterinaria;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jesus on 11/12/17.
 */

public class ListaMascotasAdapter extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<ListaMascotasModelo> items;

    public ListaMascotasAdapter (Activity activity, ArrayList<ListaMascotasModelo> items) {
        this.activity = activity;
        this.items = items;
    }

    public void clear() {
        items.clear();
    }

    public void addAll(ArrayList<ListaMascotasModelo> mascotas) {
        for (int i = 0; i < mascotas.size(); i++) {
            items.add(mascotas.get(i));
        }
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_list_mascotavscliente, null);
        }
        ListaMascotasModelo mascota = items.get(position);
        TextView nombre = (TextView) v.findViewById(R.id.lblNombre);
        nombre.setText(mascota.getNombre());
        TextView raza = (TextView) v.findViewById(R.id.lblRaza);
        raza.setText(mascota.getRaza());
        TextView color = (TextView) v.findViewById(R.id.lblColor);
        color.setText(mascota.getColor());
        TextView fecha = (TextView) v.findViewById(R.id.lblFecha);
        fecha.setText(mascota.getFecha());
        return v;
    }
}
