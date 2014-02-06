package com.tepav.reader.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.tepav.reader.R;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 06.02.2014
 * Time: 20:13
 */
public class NavigationDrawerAdapter extends ArrayAdapter<String>{

    private Context context = null;
    private String[] list = null;

    public NavigationDrawerAdapter(Context context, String[] array) {
            super(context, R.layout.custom_drawer_row_without_padding, array);

        this.context = context;
        this.list = array;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = null;

        if (position == 3 || position == 4 || position == 5) {
            rowView = inflater.inflate(R.layout.custom_drawer_with_left_padding, parent, false);
        } else
            rowView = inflater.inflate(R.layout.custom_drawer_row_without_padding, parent, false);


        TextView tv = (TextView) rowView.findViewById(R.id.tvNavigator);
        tv.setText(list[position]);

        ImageView imageOfItem = (ImageView) rowView.findViewById(R.id.imageOfItem);
        switch (position) {
            case 0:
                imageOfItem.setImageResource(R.drawable.calendar);
                break;
            case 1:
                imageOfItem.setImageResource(R.drawable.calendar);
                break;
            case 2:
                imageOfItem.setImageResource(R.drawable.search_and_publish);
                break;
            case 3:
                imageOfItem.setImageResource(R.drawable.reports);
                break;
            case 4:
                imageOfItem.setImageResource(R.drawable.notes);
                break;
            case 5:
                imageOfItem.setImageResource(R.drawable.printed);
                break;
            case 6:
                imageOfItem.setImageResource(R.drawable.read_list);
                break;
            case 7:
                imageOfItem.setImageResource(R.drawable.favorites);
                break;
            case 8:
                imageOfItem.setImageResource(R.drawable.user);
                break;

        }

        return rowView;
    }
}
