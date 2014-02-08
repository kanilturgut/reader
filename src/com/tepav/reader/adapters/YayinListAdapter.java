package com.tepav.reader.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.tepav.reader.R;
import com.tepav.reader.activities.YayinDetailsActivity;
import com.tepav.reader.delegates.YayinServiceDelegate;
import com.tepav.reader.models.Yayin;
import com.tepav.reader.services.YayinService;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 08.02.2014
 * Time: 21:45
 */
public class YayinListAdapter extends ArrayAdapter<Yayin> implements YayinServiceDelegate {

    private Context context;
    private ArrayList<Yayin> yayinList;
    private ProgressDialog progressDialog;
    private YayinService yayinService;
    private String nextYayinURL, previousYayinURL;

    public YayinListAdapter(Context context) {
        super(context, R.layout.custom_row_yayin);

        this.context = context;

        progressDialog = ProgressDialog.show(context, "Bekleyiniz", "Yükleniyor", false, false);

        yayinService = new YayinService(this);
        yayinService.getYayinList();
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_row_yayin, parent, false);

            holder = new ViewHolder();

            holder.tvYayinTitle = (TextView) convertView.findViewById(R.id.tvYayinTitle);
            holder.tvYayinAuthorInfo = (TextView) convertView.findViewById(R.id.tvYayinAuthorInfo);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvYayinTitle.setText(yayinList.get(position).getYtitle());
        holder.tvYayinAuthorInfo.setText(yayinList.get(position).getYauthors() + " - " + yayinList.get(position).getYtype());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent = new Intent(context, YayinDetailsActivity.class);
                detailIntent.putExtra("class", (Yayin) yayinList.get(position));
                context.startActivity(detailIntent);
            }
        });


        if (position == (yayinList.size() - 1))
            if (!nextYayinURL.equals(""))
                yayinService.getNextYayinList(nextYayinURL);


        return convertView;
    }

    @Override
    public void yayinListRequestDidFinish(Map responseMap) {
        yayinList = (ArrayList<Yayin>) responseMap.get("raporList");
        yayinList.addAll((ArrayList<Yayin>) responseMap.get("basılıYayinList"));
        yayinList.addAll((ArrayList<Yayin>) responseMap.get("notList"));
        previousYayinURL = (String) responseMap.get("prev");
        nextYayinURL = (String) responseMap.get("next");

        this.addAll(yayinList);
        this.notifyDataSetChanged();
        progressDialog.dismiss();
    }

    @Override
    public void nextYayinListRequestDidFinish(Map responseMap) {

        yayinList.addAll((ArrayList<Yayin>) responseMap.get("raporList"));
        yayinList.addAll((ArrayList<Yayin>) responseMap.get("basılıYayinList"));
        yayinList.addAll((ArrayList<Yayin>) responseMap.get("notList"));
        previousYayinURL = (String) responseMap.get("prev");
        nextYayinURL = (String) responseMap.get("next");

        this.addAll((ArrayList<Yayin>) responseMap.get("raporList"));
        this.addAll((ArrayList<Yayin>) responseMap.get("basılıYayinList"));
        this.addAll((ArrayList<Yayin>) responseMap.get("notList"));
        this.notifyDataSetChanged();
    }

    @Override
    public void prevYayinListRequestDidFinish(Map responseMap) {

    }


    static class ViewHolder {
        TextView tvYayinTitle, tvYayinAuthorInfo;
    }
}
