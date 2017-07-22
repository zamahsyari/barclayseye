package zmachmobile.com.barclayseye.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import zmachmobile.com.barclayseye.R;
import zmachmobile.com.barclayseye.activities.MainActivity;
import zmachmobile.com.barclayseye.activities.UberActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class UberFragment extends Fragment {
    View view;
    Button btnRequest;

    public UberFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_uber, container, false);
        btnRequest=(Button)view.findViewById(R.id.btnRequest);
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity().getBaseContext(),UberActivity.class);
                intent.putExtra("extra","loading");
                getActivity().startActivity(intent);
            }
        });
        return view;
    }

}
