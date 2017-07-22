package zmachmobile.com.barclayseye.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import zmachmobile.com.barclayseye.R;
import zmachmobile.com.barclayseye.fragments.KeepDistanceFragment;
import zmachmobile.com.barclayseye.fragments.WelcomeQuizFragment;

public class QuizActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        WelcomeQuizFragment welcomeQuizFragment = new WelcomeQuizFragment();
        KeepDistanceFragment keepDistanceFragment = new KeepDistanceFragment();
        fragmentTransaction.add(R.id.fragmentContainer,welcomeQuizFragment);
        fragmentTransaction.commit();

        try{
            Intent intent=getIntent();
            String extra=intent.getStringExtra("extra");
            Log.i("extra",extra);
            if(extra.equals("step1")){
                fragmentTransaction.replace(R.id.fragmentContainer,keepDistanceFragment);
                fragmentTransaction.commit();
            }else if(extra.equals("skip")){
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        }catch (Exception e){
            Log.e("ERROR",e.toString());
        }
    }
}
