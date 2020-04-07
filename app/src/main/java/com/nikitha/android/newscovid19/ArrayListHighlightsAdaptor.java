package com.nikitha.android.newscovid19;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ArrayListHighlightsAdaptor extends ArrayAdapter<ListItems> {
    ListItems currentword;
    ArrayList<ListItems> listItemsArray=new ArrayList<>();
    Context context;
    Activity contextActivity;

    public ArrayListHighlightsAdaptor(@NonNull Context context, @NonNull ArrayList<ListItems> objects) {
        super(context, 0, objects);
        listItemsArray=objects;
        this.context= context;
        this.contextActivity= (Activity) context;
    }

    @NonNull
    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_items_layout, parent, false);
        }
        final String[] parts = (parent.toString()).split("app:id/", 2);
        currentword = getItem(position);

       /* GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
        listItemView.setLayoutManager(layoutManager);
        if(position==1){
            layoutManager.setSpanCount(2);
        }*/
        ImageView imageView1= listItemView.findViewById(R.id.image1);
        imageView1.setImageResource(R.drawable.corona);

        final TextView textViewtitle= listItemView.findViewById(R.id.title1);
        textViewtitle.setText(currentword.getTitle());

        TextView textViewsource= listItemView.findViewById(R.id.source1);
        textViewsource.setText(currentword.getSource());

        TextView textViewpublish= listItemView.findViewById(R.id.publish1);
        textViewpublish.setText(currentword.getPublishDate());

        TextView textViewauthor= listItemView.findViewById(R.id.author1);
        textViewauthor.setText(currentword.getAuthor());

        listItemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String webReaderLink= currentword.getUrl();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(webReaderLink));
                contextActivity.startActivity(i);
            }
        });

        return listItemView;
    }

    public void setData(ArrayList<ListItems> data) {
        listItemsArray.addAll(data);
        notifyDataSetChanged();
    }
}
