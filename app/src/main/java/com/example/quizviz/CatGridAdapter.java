package com.example.quizviz;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

public class CatGridAdapter extends BaseAdapter {

    private List<String> catList;

    //constructor
    public CatGridAdapter(List<String> catList) {
        this.catList = catList;
    }

    //methods
    @Override
    public int getCount() {
        return catList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {

        View view;

        if(convertview==null){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_item_layout,parent,false);


        }
        else
        {
            view=convertview;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(parent.getContext(),SetsActivity.class);
                intent.putExtra("CATEGORY",catList.get(position));
                intent.putExtra("CATEGORY_ID",position+1);

                parent.getContext().startActivity(intent);

            }
        });
        ((TextView) view.findViewById(R.id.catName)).setText(catList.get(position));
        Random rnd = new Random();
        int color= Color.argb(255,rnd.nextInt(255),rnd.nextInt(255),rnd.nextInt(255));
        view.setBackgroundColor(color);


        return view;
    }
}
