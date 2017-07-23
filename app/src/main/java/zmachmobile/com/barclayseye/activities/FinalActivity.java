package zmachmobile.com.barclayseye.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import zmachmobile.com.barclayseye.R;

public class FinalActivity extends AppCompatActivity {
    Button btnBackHome, btnBarclays;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        overridePendingTransition(R.anim.anim_slide_in, R.anim.anim_slide_out);

        btnBackHome=(Button)findViewById(R.id.btnBackHome);
        btnBarclays=(Button)findViewById(R.id.btnBarclays);

        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
