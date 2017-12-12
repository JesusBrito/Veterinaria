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
 * Created by jesus on 9/12/17.
 */

public class ServicioAdapter extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<ServicioModelo> items;

    public ServicioAdapter (Activity activity, ArrayList<ServicioModelo> items) {
        this.activity = activity;
        this.items = items;
    }

    public void clear() {
        items.clear();
    }

    public void addAll(ArrayList<ServicioModelo> servicios) {
        for (int i = 0; i < servicios.size(); i++) {
            items.add(servicios.get(i));
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
            v = inf.inflate(R.layout.item_list_servicio, null);
        }
        ServicioModelo servicio = items.get(position);

        TextView title = (TextView) v.findViewById(R.id.name);
        title.setText(servicio.getName());
        TextView id = (TextView) v.findViewById(R.id.id);
        id.setText(servicio.getId());
        TextView precio = (TextView) v.findViewById(R.id.precio);
        precio.setText(servicio.getPrecio());
        return v;
    }
}
