package zmachmobile.com.barclayseye.activities;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import zmachmobile.com.barclayseye.R;

public class UberActivity extends AppCompatActivity {
    Toolbar myToolbar;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uber);

        myToolbar = (Toolbar) findViewById(R.id.appBar);
        myToolbar.setTitle(R.string.app_name);
        setSupportActionBar(myToolbar);

        actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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
