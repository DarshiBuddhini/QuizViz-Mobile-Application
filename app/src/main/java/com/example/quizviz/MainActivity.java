package com.example.quizviz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mainTitle;
    private Button start_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainTitle=findViewById(R.id.app_name2);
        Typeface typeface= ResourcesCompat.getFont(this,R.font.gilroy_light);
        mainTitle.setTypeface(typeface);

        start_button=findViewById(R.id.Startbutton);

        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =new Intent(MainActivity.this,CategoryActivity.class);
                startActivity(intent);
            }
        });
    }
}