package zmachmobile.com.barclayseye.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import zmachmobile.com.barclayseye.R;
import zmachmobile.com.barclayseye.fragments.ChooseModeFragment;
import zmachmobile.com.barclayseye.fragments.WelcomeFragment;

public class WelcomeActivity extends AppCompatActivity{
    Toolbar myToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        overridePendingTransition(R.anim.anim_slide_in, R.anim.anim_slide_out);

        myToolbar = (Toolbar) findViewById(R.id.appBar);
        myToolbar.setTitle(R.string.app_name);
        setSupportActionBar(myToolbar);
        getSupportActionBar().hide();

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
                getSupportActionBar().show();
                fragmentTransaction.replace(R.id.fragmentContainer,chooseModeFragment);
                fragmentTransaction.commit();
            }else{

            }
        }catch (Exception e){

        }
    }
}
