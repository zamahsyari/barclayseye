package zmachmobile.com.barclayseye.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zmachmobile.com.barclayseye.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class KeepDistanceFragment extends Fragment {


    public KeepDistanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_keep_distance, container, false);
    }

}
