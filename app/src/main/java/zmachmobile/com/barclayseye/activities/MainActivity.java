package zmachmobile.com.barclayseye.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import zmachmobile.com.barclayseye.Config;
import zmachmobile.com.barclayseye.R;
import zmachmobile.com.barclayseye.adapters.UsageModeAdapter;
import zmachmobile.com.barclayseye.fragments.ChooseModeFragment;
import zmachmobile.com.barclayseye.fragments.MainFragment;
import zmachmobile.com.barclayseye.fragments.NearestFragment;
import zmachmobile.com.barclayseye.fragments.TravelFragment;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    String extra, json;
    ActionBar actionBar;
    FragmentManager fragmentManager;
    MainFragment mainFragment;
    NearestFragment nearestFragment;
    TravelFragment travelFragment;
    ChooseModeFragment chooseModeFragment;
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Config.isVoiceOnly==true){
            setContentView(R.layout.activity_main);
        }else{
            if(Config.isModeYellow==true){
                setContentView(R.layout.activity_main_yellow);
            }else{
                setContentView(R.layout.activity_main_green);
            }
        }
        overridePendingTransition(R.anim.anim_slide_in, R.anim.anim_slide_out);

        try{
            Config.textToSpeech.shutdown();
        }catch (Exception e){
            e.printStackTrace();
        }

        myToolbar = (Toolbar) findViewById(R.id.appBar);
        myToolbar.setTitle(R.string.app_name);
        setSupportActionBar(myToolbar);

        actionBar=getSupportActionBar();

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        mainFragment= new MainFragment();
        nearestFragment=new NearestFragment();
        travelFragment=new TravelFragment();
        chooseModeFragment=new ChooseModeFragment();
        fragmentTransaction.add(R.id.fragmentContainer,chooseModeFragment);
        fragmentTransaction.commit();

        try{
            intent=getIntent();
            extra=intent.getStringExtra("extra");
            json=intent.getStringExtra("json");
            Log.i("extra",extra);
            if(extra.equals("choose")){
                actionBar.setDisplayHomeAsUpEnabled(false);
                fragmentTransaction.replace(R.id.fragmentContainer,chooseModeFragment);
                fragmentTransaction.commit();
            }else if(extra.equals("nearest")){
                actionBar.setDisplayHomeAsUpEnabled(true);
                fragmentTransaction.replace(R.id.fragmentContainer,nearestFragment);
                fragmentTransaction.commit();
            }else if(extra.equals("travel")){
                actionBar.setDisplayHomeAsUpEnabled(true);
                travelFragment.addJson(json);
                fragmentTransaction.replace(R.id.fragmentContainer,travelFragment);
                fragmentTransaction.commit();
            }else if(extra.equals("main")){
                actionBar.setDisplayHomeAsUpEnabled(true);
                fragmentTransaction.replace(R.id.fragmentContainer,mainFragment);
                fragmentTransaction.commit();
            }
        }catch (Exception e){
            Log.e("ERROR",e.toString());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if(extra.equals("nearest")){
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }else if(extra.equals("travel")){
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("extra","nearest");
                startActivity(intent);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
