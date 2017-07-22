package zmachmobile.com.barclayseye.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zmachmobile.com.barclayseye.R;
import zmachmobile.com.barclayseye.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragment extends Fragment {

    public QuestionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(getActivity().getBaseContext(),MainActivity.class);
                intent.putExtra("extra","step3");
                getActivity().startActivity(intent);
            }
        },3000);
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

}
