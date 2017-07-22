package zmachmobile.com.barclayseye.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import zmachmobile.com.barclayseye.R;
import zmachmobile.com.barclayseye.fragments.ChooseModeFragment;
import zmachmobile.com.barclayseye.fragments.WelcomeFragment;

public class WelcomeActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        WelcomeFragment welcomeFragment = new WelcomeFragment();
        ChooseModeFragment chooseModeFragment = new ChooseModeFragment();
        fragmentTransaction.add(R.id.fragmentContainer,welcomeFragment);
        fragmentTransaction.commit();

        try{
            Intent intent=getIntent();
            String extra=intent.getStringExtra("extra");
            Log.i("extra",extra);
            if(extra.equals("move")){
                fragmentTransaction.replace(R.id.fragmentContainer,chooseModeFragment);
                fragmentTransaction.commit();
            }
        }catch (Exception e){

        }
    }
}
