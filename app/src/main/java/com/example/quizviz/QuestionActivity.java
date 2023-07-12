package com.example.quizviz;

import static com.example.quizviz.SetsActivity.category_id;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {

    //creating variables
    private TextView question;
    private TextView qCount;
    private TextView timer;
    private Button option1,option2,option3,option4;
    private int questNum=0;
    private CountDownTimer countdown;
    private int score;
    private int setNo;
    private List<Question> questionList;

    private FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        question=findViewById(R.id.Question);
        qCount=findViewById(R.id.qCount);
        timer=findViewById(R.id.countdown);

        option1=findViewById(R.id.opt1);
        option2=findViewById(R.id.opt2);
        option3=findViewById(R.id.opt3);
        option4=findViewById(R.id.opt4);

        option1.setOnClickListener(this);
        option2.setOnClickListener(this);
        option3.setOnClickListener(this);
        option4.setOnClickListener(this);

        setNo=getIntent().getIntExtra("SETNO",1);
        firestore=FirebaseFirestore.getInstance();

        getQuestionList();
        score=0;
    }

    private void getQuestionList(){

        questionList=new ArrayList<>();
        firestore.collection("QUIZ").document("CAT"+String.valueOf(category_id ))
                .collection("SET"+String.valueOf(setNo))
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful())
                        {
                            QuerySnapshot questions=task.getResult();
                            for(QueryDocumentSnapshot doc:questions){
                                questionList.add(new Question(doc.getString("QUESTION"),
                                        doc.getString("A"),
                                        doc.getString("B"),
                                        doc.getString("C"),
                                        doc.getString("D"),
                                        Integer.valueOf(doc.getString("ANSWER"))

                                ));
                            }
                            setQuestion();
                        }
                        else
                        {
                            Toast.makeText(QuestionActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                        }
                        //loadingDialog.cancel();


                    }
                });



    }

    public void setQuestion(){
        timer.setText((String.valueOf(10)));
        question.setText(questionList.get(0).getQuestion());
        option1.setText(questionList.get(0).getOptionA());
        option2.setText(questionList.get(0).getOptionB());
        option3.setText(questionList.get(0).getOptionC());
        option4.setText(questionList.get(0).getOptionD());

        qCount.setText(String.valueOf(1)+ "/" + String.valueOf(questionList.size()));
        startTimer();//call function

        questNum=0;

    }


    //timer function
    private void startTimer(){
        countdown=new CountDownTimer(12000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(millisUntilFinished<10000) {
                    timer.setText(String.valueOf(millisUntilFinished / 1000));
                }
            }

            @Override
            public void onFinish() {
                changeQuestion();
            }
        };

        countdown.start();
    }

    @Override
    public void onClick(View view) {

        int selectedOption=0;
        switch (view.getId())
        {
            case R.id.opt1:
                selectedOption=1;
                break;
            case R.id.opt2:
                selectedOption=2;
                break;
            case R.id.opt3:
                selectedOption=3;
                break;
            case R.id.opt4:
                selectedOption=4;
                break;
            default:

        }
        countdown.cancel();
        checkAnswer(selectedOption,view);
    }


    //function for check correct answer
    private void checkAnswer(int selectedOption,View view){

        if(selectedOption==questionList.get(questNum).getCorrectAns())
        {
            //right answer
            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                score++;

        }
        else
        {
            //wrong answer
            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.RED));

            switch (questionList.get(questNum).getCorrectAns())
            {
                case 1:
                    option1.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 2:
                    option2.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;

                case 3:
                    option3.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;

                case 4:
                    option4.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
            }
        }

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeQuestion();
            }
        },3000);



    }

    private void changeQuestion()
    {
        if(questNum<questionList.size()-1)
        {
            questNum++;
            playAnim(question,0,0);
            playAnim(option1,0,1);
            playAnim(option2,0,2);
            playAnim(option3,0,3);
            playAnim(option4,0,4);

            qCount.setText(String.valueOf(questNum+1)+"/"+String.valueOf(questionList.size()));
            timer.setText((String.valueOf(10)));
            startTimer();


        }
        else
        {
            //go to score activity
            Intent intent=new Intent(QuestionActivity.this,ScoreActivity.class);
            intent.putExtra("SCORE",String.valueOf(score)+ "/"+String.valueOf(questionList.size()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            //QuestionActivity.this.finish();
        }
    }

    private void playAnim(View view,final int value,int ViewNo){

        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500)
                .setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                            if(value==0)
                            {
                                switch(ViewNo)
                                {
                                    case 0:
                                        ((TextView)view).setText(questionList.get(questNum).getQuestion());
                                        break;
                                    case 1:
                                        ((Button)view).setText(questionList.get(questNum).getOptionA());
                                        break;
                                    case 2:
                                        ((Button)view).setText(questionList.get(questNum).getOptionB());
                                        break;
                                    case 3:
                                        ((Button)view).setText(questionList.get(questNum).getOptionC());
                                        break;
                                    case 4:
                                        ((Button)view).setText(questionList.get(questNum).getOptionD());
                                        break;
                                }

                                if(ViewNo !=0)
                                {
                                    ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1653FF")));
                                }

                                playAnim(view,1,ViewNo);


                            }
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        countdown.cancel();
    }
}