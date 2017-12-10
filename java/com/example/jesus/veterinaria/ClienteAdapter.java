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
 * Created by Fernando on 08/12/2017.
 */
public class ClienteAdapter extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<ClienteModelo> items;

    public ClienteAdapter (Activity activity, ArrayList<ClienteModelo> items) {
        this.activity = activity;
        this.items = items;
    }

    public void clear() {
        items.clear();
    }

    public void addAll(ArrayList<ClienteModelo> clientes) {
        for (int i = 0; i < clientes.size(); i++) {
            items.add(clientes.get(i));
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
            v = inf.inflate(R.layout.item_list, null);
        }

        ClienteModelo cliente = items.get(position);

        TextView title = (TextView) v.findViewById(R.id.name);
        title.setText(cliente.getName());
        TextView id = (TextView) v.findViewById(R.id.id);
        id.setText(cliente.getId());
        return v;
    }
}
