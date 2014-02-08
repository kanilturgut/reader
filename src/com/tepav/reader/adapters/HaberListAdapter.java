package com.tepav.reader.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.tepav.reader.R;
import com.tepav.reader.activities.HaberDetailsActivity;
import com.tepav.reader.activities.MainScreenActivity;
import com.tepav.reader.cache.Cache;
import com.tepav.reader.delegates.HaberServiceDelegate;
import com.tepav.reader.models.Haber;
import com.tepav.reader.services.HaberService;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 07.02.2014
 * Time: 22:30
 */
public class HaberListAdapter extends ArrayAdapter<Haber> implements HaberServiceDelegate{

    private Context context;
    private ArrayList<Haber> haberList;
    private ProgressDialog progressDialog;
    private HaberService haberService = null;
    private String nextHaberURL, previousHaberURL;

    public HaberListAdapter(Context context) {
        super(context, R.layout.custom_row_haber);
        this.context = context;

        progressDialog = ProgressDialog.show(context, "Bekleyiniz", "Yükleniyor", false, false);

        haberService = new HaberService(this);
        haberService.getHaberList();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

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

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent = new Intent(context, HaberDetailsActivity.class);
                detailIntent.putExtra("class_type", MainScreenActivity.HABER_SERVICE);
                detailIntent.putExtra("class", (Haber) haberList.get(position));
                context.startActivity(detailIntent);
            }
        });


        if (position == (haberList.size() - 1)) {
            if (!nextHaberURL.equals(""))
                haberService.getNextHaberList(nextHaberURL);
        }

        return convertView;
    }

    @Override
    public void haberListRequestDidFinish(Map responseMap) {
        haberList = (ArrayList<Haber>) responseMap.get("haberList");
        previousHaberURL = (String) responseMap.get("prev");
        nextHaberURL = (String) responseMap.get("next");

        this.addAll(haberList);
        this.notifyDataSetChanged();
        progressDialog.dismiss();
    }

    @Override
    public void nextHaberListRequestDidFinish(Map responseMap) {

        haberList.addAll((ArrayList<Haber>) responseMap.get("haberList"));
        previousHaberURL = (String) responseMap.get("prev");
        nextHaberURL = (String) responseMap.get("next");

        this.addAll((ArrayList<Haber>) responseMap.get("haberList"));
        this.notifyDataSetChanged();
    }

    @Override
    public void prevHaberListRequestDidFinish(Map responseMap) {

    }

    private static class ViewHolder {
        ImageView imageOfHaber;
        TextView tvTitleOfHaber;
    }
}
