package com.tepav.reader.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.tepav.reader.R;
import com.tepav.reader.cache.Cache;
import com.tepav.reader.models.Haber;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 07.02.2014
 * Time: 22:30
 */
public class HaberListAdapter extends ArrayAdapter<Haber>{

    private Context context;
    private ArrayList<Haber> haberList;

    public HaberListAdapter(Context context, ArrayList<Haber> list) {
        super(context, R.layout.custom_row_haber, list);

        this.context = context;
        this.haberList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_row_haber, parent, false);

            holder = new ViewHolder();
            holder.imageOfHaber = (ImageView) convertView.findViewById(R.id.ivImageOfHaber);
            holder.tvTitleOfHaber = (TextView) convertView.findViewById(R.id.tvTitleOfHaber);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvTitleOfHaber.setText(haberList.get(position).getHtitle());
        Cache.getInstance().getImageFromCache(context, haberList.get(position).getHimage(), holder.imageOfHaber);

        return convertView;
    }

    private static class ViewHolder {
        ImageView imageOfHaber;
        TextView tvTitleOfHaber;
    }
}
