package com.example.quizviz;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SetsAdapter extends BaseAdapter {

    private int noOfSets;

    public SetsAdapter(int noOfSets) {
        this.noOfSets = noOfSets;
    }

    @Override
    public int getCount() {

        return noOfSets;
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
        if(convertview==null)
        {
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.set_item_layout,parent,false);
        }
        else
        {
            view=convertview;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(parent.getContext(),QuestionActivity.class);
                intent.putExtra("SETNO",position+1);

                parent.getContext().startActivity(intent);
            }
        });

        ((TextView) view.findViewById(R.id.setNo)).setText(String.valueOf(position+1));
        return view;
    }
}
