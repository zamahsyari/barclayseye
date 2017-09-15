package zmachmobile.com.barclayseye.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import zmachmobile.com.barclayseye.Config;
import zmachmobile.com.barclayseye.R;
import zmachmobile.com.barclayseye.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class FinalQuizFragment extends Fragment {
    View view;
    Button btnStart;

    public FinalQuizFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try{
            Config.textToSpeech.shutdown();
        }catch (Exception e){
            e.printStackTrace();
        }
        view=inflater.inflate(R.layout.fragment_final_quiz, container, false);
        btnStart=(Button)view.findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Config.score <=3 ){
                    Config.isModeYellow=false;
                }else{
                    Config.isModeYellow=true;
                }
                Intent intent=new Intent(getActivity().getBaseContext(),MainActivity.class);
                intent.putExtra("extra","main");
                getActivity().startActivity(intent);
            }
        });
        return view;
    }

}
