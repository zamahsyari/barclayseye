package zmachmobile.com.barclayseye.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import zmachmobile.com.barclayseye.ButtonChild;
import zmachmobile.com.barclayseye.R;
import zmachmobile.com.barclayseye.adapters.NearestAdapter;
import zmachmobile.com.barclayseye.adapters.TravelAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class TravelFragment extends Fragment {
    View view;
    List<ButtonChild> buttonChildList=new ArrayList<>();
    RecyclerView recyclerView;
    TravelAdapter travelAdapter;
    public TravelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_travel, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);

        travelAdapter=new TravelAdapter(getActivity().getBaseContext(),buttonChildList);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(travelAdapter);

        prepareData();
        return view;
    }

    private void prepareData() {
        ButtonChild buttonChild=new ButtonChild(1,"Guide me there",null,null);
        buttonChildList.add(buttonChild);

        buttonChild=new ButtonChild(2,"Ride an Uber",null,null);
        buttonChildList.add(buttonChild);

        buttonChild=new ButtonChild(3,"No thanks, I'm fine",null,null);
        buttonChildList.add(buttonChild);

        travelAdapter.notifyDataSetChanged();
    }
}
