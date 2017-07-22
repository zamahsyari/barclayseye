package zmachmobile.com.barclayseye.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zmachmobile.com.barclayseye.R;
import zmachmobile.com.barclayseye.activities.FinalActivity;
import zmachmobile.com.barclayseye.activities.UberActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class UberLoadingFragment extends Fragment {


    public UberLoadingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(getActivity().getBaseContext(),FinalActivity.class);
                getActivity().startActivity(intent);
            }
        },5000);
        return inflater.inflate(R.layout.fragment_uber_loading, container, false);
    }

}
