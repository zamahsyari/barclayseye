package zmachmobile.com.barclayseye.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import zmachmobile.com.barclayseye.R;
import zmachmobile.com.barclayseye.fragments.AnswerFragment;
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

        myToolbar = (Toolbar) findViewById(R.id.appBar);
        myToolbar.setTitle(R.string.app_name);
        setSupportActionBar(myToolbar);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        WelcomeQuizFragment welcomeQuizFragment = new WelcomeQuizFragment();
        KeepDistanceFragment keepDistanceFragment = new KeepDistanceFragment();
        QuestionFragment questionFragment=new QuestionFragment();
        AnswerFragment answerFragment=new AnswerFragment();
        fragmentTransaction.add(R.id.fragmentContainer,welcomeQuizFragment);
        fragmentTransaction.commit();

        try{
            Intent intent=getIntent();
            String extra=intent.getStringExtra("extra");
            Log.i("extra",extra);
            if(extra.equals("step1")) {
                fragmentTransaction.replace(R.id.fragmentContainer, keepDistanceFragment);
                fragmentTransaction.commit();
            }else if(extra.equals("step2")){
                fragmentTransaction.replace(R.id.fragmentContainer, questionFragment);
                fragmentTransaction.commit();
            }else if(extra.equals("step3")){
                fragmentTransaction.replace(R.id.fragmentContainer, answerFragment);
                fragmentTransaction.commit();
            }else if(extra.equals("skip")){
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        }catch (Exception e){
            Log.e("ERROR",e.toString());
        }
    }
}
