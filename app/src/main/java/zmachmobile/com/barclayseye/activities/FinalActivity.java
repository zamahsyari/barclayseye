package zmachmobile.com.barclayseye.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import zmachmobile.com.barclayseye.ButtonChild;
import zmachmobile.com.barclayseye.R;
import zmachmobile.com.barclayseye.adapters.FinalAdapter;
import zmachmobile.com.barclayseye.adapters.UsageModeAdapter;

public class FinalActivity extends AppCompatActivity {
    List<ButtonChild> buttonChildList=new ArrayList<>();
    RecyclerView recyclerView;
    FinalAdapter finalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        overridePendingTransition(R.anim.anim_slide_in, R.anim.anim_slide_out);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);

        finalAdapter=new FinalAdapter(getApplicationContext(),buttonChildList);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(finalAdapter);

        prepareData();
    }

    private void prepareData() {
        ButtonChild buttonChild=new ButtonChild(1,"back to Homepage",null,null);
        buttonChildList.add(buttonChild);

        buttonChild=new ButtonChild(2,"Browse Barclays Products",null,null);
        buttonChildList.add(buttonChild);

        finalAdapter.notifyDataSetChanged();
    }
}
