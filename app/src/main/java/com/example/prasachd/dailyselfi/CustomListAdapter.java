package com.example.prasachd.dailyselfi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by prasachd on 11/18/15.
 */
public class CustomListAdapter extends ArrayAdapter<MySelfieBean> {
    private final Context mContext;

    public CustomListAdapter(Context context, int resource, int textViewResource, List<MySelfieBean> objects) {
        super(context, resource, textViewResource, objects);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MySelfieBean mySelfie = getItem(position);
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listitem, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.item_txt);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.item_img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (mySelfie != null) {
            viewHolder.textView.setText(getReadableSelfieName(mySelfie.getMName()));
            viewHolder.imageView.setImageBitmap(mySelfie.getMThumb());
        }

        return convertView;
    }

    private class ViewHolder {
        TextView textView;
        ImageView imageView;
    }

    private static String getReadableSelfieName(String name) {
        String[] split = name.split("_");
        String date = split[1];
        String time = split[2];
        String dateTime = date + time;
        String format = "yyyy_MM_dd_HH_mm_ss";

        try {
            return DateFormat.getDateTimeInstance().format(new SimpleDateFormat(format).parse(dateTime));
        } catch (ParseException pe) {
            return name;
        }
    }
}
