package com.tepav.reader.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.tepav.reader.R;
import com.tepav.reader.activities.GunlukDetailsActivity;
import com.tepav.reader.delegates.GunlukServiceDelegate;
import com.tepav.reader.models.Gunluk;
import com.tepav.reader.services.GunlukService;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 08.02.2014
 * Time: 18:52
 */
public class GunlukListAdapter extends ArrayAdapter<Gunluk> implements GunlukServiceDelegate{

    private Context context;
    private ArrayList<Gunluk> gunlukList;
    private ProgressDialog progressDialog;
    private GunlukService gunlukService;
    private String nextGunlukURL, previousGunlukURL;

    public GunlukListAdapter(Context context) {
        super(context, R.layout.custom_row_gunluk);

        this.context = context;

        progressDialog = ProgressDialog.show(context, context.getResources().getString(R.string.pd_wait), context.getResources().getString(R.string.pd_loading), false, false);

        gunlukService = new GunlukService(this);
        gunlukService.getGunlukList();
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_row_gunluk, parent, false);

            holder = new ViewHolder();

            holder.tvGunlukTitle = (TextView) convertView.findViewById(R.id.tvGunlukTitle);
            holder.tvGunlukAuthorInfo = (TextView) convertView.findViewById(R.id.tvGunlukAuthorInfo);
            holder.tvGunlukContent = (TextView) convertView.findViewById(R.id.tvGunlukContent);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvGunlukTitle.setText(gunlukList.get(position).getBtitle());
        holder.tvGunlukAuthorInfo.setText(gunlukList.get(position).getPfullname());
        holder.tvGunlukContent.setText(Html.fromHtml(gunlukList.get(position).getBcontent()));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent = new Intent(context, GunlukDetailsActivity.class);
                detailIntent.putExtra("class", (Gunluk) gunlukList.get(position));
                context.startActivity(detailIntent);
            }
        });


        if (position == (gunlukList.size() - 1))
            if (!nextGunlukURL.equals(""))
                gunlukService.getNextGunlukList(nextGunlukURL);


        return convertView;
    }

    @Override
    public void gunlukListRequestDidFinish(Map responseMap) {
        gunlukList = (ArrayList<Gunluk>) responseMap.get("gunlukList");
        previousGunlukURL = (String) responseMap.get("prev");
        nextGunlukURL = (String) responseMap.get("next");

        this.addAll(gunlukList);
        this.notifyDataSetChanged();
        progressDialog.dismiss();
    }

    @Override
    public void nextGunlukListRequestDidFinish(Map responseMap) {

        gunlukList.addAll((ArrayList<Gunluk>) responseMap.get("gunlukList"));
        previousGunlukURL = (String) responseMap.get("prev");
        nextGunlukURL = (String) responseMap.get("next");

        this.addAll((ArrayList<Gunluk>) responseMap.get("gunlukList"));
        this.notifyDataSetChanged();
    }

    @Override
    public void prevGunlukListRequestDidFinish(Map responseMap) {

    }


    static class ViewHolder {
        TextView tvGunlukTitle, tvGunlukAuthorInfo, tvGunlukContent;
    }
}
