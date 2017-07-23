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

import zmachmobile.com.barclayseye.Global;
import zmachmobile.com.barclayseye.R;
import zmachmobile.com.barclayseye.fragments.MainFragment;
import zmachmobile.com.barclayseye.fragments.NearestFragment;
import zmachmobile.com.barclayseye.fragments.TravelFragment;
import zmachmobile.com.barclayseye.fragments.UberFragment;
import zmachmobile.com.barclayseye.fragments.UberLoadingFragment;

public class UberActivity extends AppCompatActivity {
    Intent intent;
    String extra;
    Toolbar myToolbar;
    ActionBar actionBar;
    FragmentManager fragmentManager;
    UberFragment uberFragment;
    UberLoadingFragment uberLoadingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uber);
        overridePendingTransition(R.anim.anim_slide_in, R.anim.anim_slide_out);

        try{
            Global.textToSpeech.shutdown();
        }catch (Exception e){
            e.printStackTrace();
        }

        myToolbar = (Toolbar) findViewById(R.id.appBar);
        myToolbar.setTitle(R.string.app_name);
        setSupportActionBar(myToolbar);

        actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        uberFragment= new UberFragment();
        uberLoadingFragment=new UberLoadingFragment();
        fragmentTransaction.add(R.id.fragmentContainer,uberFragment);
        fragmentTransaction.commit();

        try{
            intent=getIntent();
            extra=intent.getStringExtra("extra");
            Log.i("extra",extra);
            if(extra.equals("loading")){
                actionBar.setDisplayHomeAsUpEnabled(true);
                fragmentTransaction.replace(R.id.fragmentContainer,uberLoadingFragment);
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
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            intent.putExtra("extra","nearest");
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
