package com.example.quizviz;

import static com.example.quizviz.SplashActivity.catList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;


public class CategoryActivity extends AppCompatActivity {

    private GridView catGrid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Categories");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        catGrid=findViewById(R.id.catGrid);

//        List<String> catList=new ArrayList<>();
//        catList.add("OOP");
//        catList.add("CN");
//        catList.add("OSSA");
//        catList.add("SE");
//        catList.add("DMS");
//        catList.add("SPM");
//        catList.add("OOC");
//        catList.add("ISDM");


        CatGridAdapter adapter=new CatGridAdapter(catList);
        catGrid.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home){

            CategoryActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}