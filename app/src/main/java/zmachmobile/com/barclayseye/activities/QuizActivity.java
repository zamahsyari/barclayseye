package zmachmobile.com.barclayseye.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import zmachmobile.com.barclayseye.Global;
import zmachmobile.com.barclayseye.R;
import zmachmobile.com.barclayseye.fragments.AnswerFragment;
import zmachmobile.com.barclayseye.fragments.FinalQuizFragment;
import zmachmobile.com.barclayseye.fragments.KeepDistanceFragment;
import zmachmobile.com.barclayseye.fragments.QuestionFragment;
import zmachmobile.com.barclayseye.fragments.WelcomeQuizFragment;

public class QuizActivity extends AppCompatActivity {
    Toolbar myToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        overridePendingTransition(R.anim.anim_slide_in, R.anim.anim_slide_out);

        try{
            Global.textToSpeech.shutdown();
        }catch (Exception e){
            e.printStackTrace();
        }

        myToolbar = (Toolbar) findViewById(R.id.appBar);
        myToolbar.setTitle(R.string.app_name);
        setSupportActionBar(myToolbar);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        WelcomeQuizFragment welcomeQuizFragment = new WelcomeQuizFragment();
        KeepDistanceFragment keepDistanceFragment = new KeepDistanceFragment();
        QuestionFragment questionFragment=new QuestionFragment();
        AnswerFragment answerFragment=new AnswerFragment();
        FinalQuizFragment finalQuizFragment=new FinalQuizFragment();
        fragmentTransaction.add(R.id.fragmentContainer,welcomeQuizFragment);
        fragmentTransaction.commit();

        try{
            Intent intent=getIntent();
            String extra=intent.getStringExtra("extra");
            Log.i("extra",extra);
            if(extra.equals("welcome")) {
                fragmentTransaction.replace(R.id.fragmentContainer, keepDistanceFragment);
                fragmentTransaction.commit();
            }else if(extra.equals("question")){
                fragmentTransaction.replace(R.id.fragmentContainer, questionFragment);
                fragmentTransaction.commit();
            }else if(extra.equals("answer")){
                fragmentTransaction.replace(R.id.fragmentContainer, answerFragment);
                fragmentTransaction.commit();
            }else if(extra.equals("final")){
                fragmentTransaction.replace(R.id.fragmentContainer,finalQuizFragment);
                fragmentTransaction.commit();
            }
        }catch (Exception e){
            Log.e("ERROR",e.toString());
        }
    }
}
