package zmachmobile.com.barclayseye.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zmachmobile.com.barclayseye.Config;
import zmachmobile.com.barclayseye.R;
import zmachmobile.com.barclayseye.activities.FinalActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class UberDriverFragment extends Fragment {


    public UberDriverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        if(Config.isVoiceOnly==true){
            view=inflater.inflate(R.layout.fragment_uber_driver, container, false);
        }else{
            if(Config.isModeYellow==true){
                view=inflater.inflate(R.layout.fragment_uber_driver_yellow, container, false);
            }else{
                view=inflater.inflate(R.layout.fragment_uber_driver_green, container, false);
            }
        }
        try{
            Config.textToSpeech.shutdown();
        }catch (Exception e){
            e.printStackTrace();
        }
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(getActivity().getBaseContext(),FinalActivity.class);
                getActivity().startActivity(intent);
            }
        },5000);
        return view;
    }

}
