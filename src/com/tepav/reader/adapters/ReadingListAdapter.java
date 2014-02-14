package com.tepav.reader.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.tepav.reader.R;
import com.tepav.reader.activities.GunlukDetailsActivity;
import com.tepav.reader.activities.HaberDetailsActivity;
import com.tepav.reader.activities.YayinDetailsActivity;
import com.tepav.reader.cache.Cache;
import com.tepav.reader.models.Gunluk;
import com.tepav.reader.models.Haber;
import com.tepav.reader.models.Yayin;
import com.tepav.reader.services.ReadingListService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 13.02.2014
 * Time: 10:45
 */
public class ReadingListAdapter extends ArrayAdapter<Object>{

    private Context context = null;
    private List readingList = null;
    private ReadingListService readingListService = null;
    private int persistanceType;

    private final int INSTANCE_OF_HABER = 0;
    private final int INSTANCE_OF_GUNLUK = 1;
    private final int INSTANCE_OF_YAYIN = 2;

    public ReadingListAdapter(Context context, int persistanceType) {
        super(context, R.layout.custom_row_haber);

        this.context = context;
        this.persistanceType = persistanceType;

        readingListService = ReadingListService.getInstance(this.context);

        if (persistanceType == ReadingListService.PERSISTANCE_TYPE_FAVORITES)
            readingList = readingListService.getFavoritesList();
        if (persistanceType == ReadingListService.PERSISTANCE_TYPE_READ_LIST)
            readingList = readingListService.getReadingList();


        this.addAll(readingList);
        this.notifyDataSetChanged();
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            holder = new ViewHolder();

            if (instanceOfWhat(readingList.get(position)) == INSTANCE_OF_HABER) {
                convertView = inflater.inflate(R.layout.custom_row_haber, parent, false);
                holder.imageOfHaber = (ImageView) convertView.findViewById(R.id.ivImageOfHaber);
                holder.tvTitleOfHaber = (TextView) convertView.findViewById(R.id.tvTitleOfHaber);
            } else if (instanceOfWhat(readingList.get(position)) == INSTANCE_OF_GUNLUK) {
                convertView = inflater.inflate(R.layout.custom_row_gunluk, parent, false);
                holder.tvGunlukTitle = (TextView) convertView.findViewById(R.id.tvGunlukTitle);
                holder.tvGunlukAuthorInfo = (TextView) convertView.findViewById(R.id.tvGunlukAuthorInfo);
                holder.tvGunlukContent = (TextView) convertView.findViewById(R.id.tvGunlukContent);
            } else if (instanceOfWhat(readingList.get(position)) == INSTANCE_OF_YAYIN) {
                convertView = inflater.inflate(R.layout.custom_row_yayin, parent, false);
                holder.tvYayinTitle = (TextView) convertView.findViewById(R.id.tvYayinTitle);
                holder.tvYayinAuthorInfo = (TextView) convertView.findViewById(R.id.tvYayinAuthorInfo);
            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        //observe for class
        if (instanceOfWhat(readingList.get(position)) == INSTANCE_OF_HABER) {
            holder.tvTitleOfHaber.setText(((Haber) readingList.get(position)).getHtitle());
            Cache.getInstance().getImageFromCache(context, ((Haber) readingList.get(position)).getHimage(), holder.imageOfHaber);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent detailIntent = new Intent(context, HaberDetailsActivity.class);
                    detailIntent.putExtra("class", (Haber) readingList.get(position));
                    context.startActivity(detailIntent);
                }
            });
        } else if (instanceOfWhat(readingList.get(position)) == INSTANCE_OF_GUNLUK) {
            holder.tvGunlukTitle.setText(((Gunluk) readingList.get(position)).getBtitle());
            holder.tvGunlukAuthorInfo.setText(((Gunluk) readingList.get(position)).getPfullname());
            holder.tvGunlukContent.setText(Html.fromHtml(((Gunluk) readingList.get(position)).getBcontent()));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent detailIntent = new Intent(context, GunlukDetailsActivity.class);
                    detailIntent.putExtra("class", (Gunluk) readingList.get(position));
                    context.startActivity(detailIntent);
                }
            });
        } else if (instanceOfWhat(readingList.get(position)) == INSTANCE_OF_YAYIN) {
            holder.tvYayinTitle.setText(((Yayin) readingList.get(position)).getYtitle());
            holder.tvYayinAuthorInfo.setText(((Yayin) readingList.get(position)).getYauthors() + " - " + ((Yayin) readingList.get(position)).getYtype());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent detailIntent = new Intent(context, YayinDetailsActivity.class);
                    detailIntent.putExtra("class", (Yayin) readingList.get(position));
                    context.startActivity(detailIntent);
                }
            });
        }


        return convertView;
    }

    private static class ViewHolder {

        //for haber
        ImageView imageOfHaber;
        TextView tvTitleOfHaber;

        //for gunluk
        TextView tvGunlukTitle, tvGunlukAuthorInfo, tvGunlukContent;

        //for basili yayinlar
        TextView tvYayinTitle, tvYayinAuthorInfo;

    }


    private int instanceOfWhat(Object obj) {
        if (obj instanceof Haber) {
            return INSTANCE_OF_HABER;
        } else if (obj instanceof Gunluk) {
            return INSTANCE_OF_GUNLUK;
        } else if (obj instanceof Yayin) {
            return INSTANCE_OF_YAYIN;
        } else {
            //error
            Log.e("ReadingListAdapter", "Unknown class type");
            return -1;
        }
    }
}
