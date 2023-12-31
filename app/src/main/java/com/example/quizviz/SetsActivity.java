package com.example.quizviz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Sets;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SetsActivity extends AppCompatActivity {

    private GridView set_Grid;
    public static int category_id;
    private FirebaseFirestore firestore_DB;
    private Dialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sets);


        Toolbar toolbar=findViewById(R.id.toolbar_sets);
        setSupportActionBar(toolbar);

        String title=getIntent().getStringExtra("CATEGORY");
        category_id=getIntent().getIntExtra("CATEGORY_ID",1);


        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//back button

        set_Grid=findViewById(R.id.sets_gridview);


        loadingDialog=new Dialog(SetsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_bg);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();



        //database
        firestore_DB=FirebaseFirestore.getInstance();
        //calling function
        loadSets();




    }

    private void loadSets() {
        firestore_DB.collection("QUIZ").document("CAT"+String.valueOf(category_id))
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful())
                        {
                            DocumentSnapshot doc=task.getResult();
                            if(doc.exists())
                            {
                                long sets=(long)doc.get("SETS");

                                //FETCH DATA
                                SetsAdapter adapter=new SetsAdapter((int) sets);
                                set_Grid.setAdapter(adapter);


                            }
                            else{
                                Toast.makeText(SetsActivity.this,"No Cat Document Exists",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                        else
                        {
                            Toast.makeText(SetsActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                        }
                        loadingDialog.cancel();
                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home){

            SetsActivity.this.finish();//back button method
        }
        return super.onOptionsItemSelected(item);
    }
}