package vn.edu.ntu.ngocnhan.appsnews;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import java.util.List;

public class Customadapter extends ArrayAdapter<Docbao> {

    public Customadapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public Customadapter(Context context, int resource, List<Docbao> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.dong_layout_listview, null);
        }

        Docbao p = getItem(position);

        if (p != null) {
            //Anh xa + Gan gia tri
            TextView txttitle = (TextView) view.findViewById(R.id.textViewtitle);
            txttitle.setText(p.title);

            Log.d("picaso", "getView: " + p.image);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            Glide.with(getContext()).load("p.image").into(imageView);
        }


        return view;
    }

}