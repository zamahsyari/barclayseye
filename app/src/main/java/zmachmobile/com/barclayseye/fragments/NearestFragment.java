package zmachmobile.com.barclayseye.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zmachmobile.com.barclayseye.ApiBuilder;
import zmachmobile.com.barclayseye.ButtonChild;
import zmachmobile.com.barclayseye.Config;
import zmachmobile.com.barclayseye.R;
import zmachmobile.com.barclayseye.adapters.NearestAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearestFragment extends Fragment {
    View view;

    List<ButtonChild> buttonChildList=new ArrayList<>();
    RecyclerView recyclerView;
    NearestAdapter nearestAdapter;

    public NearestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try{
            Config.textToSpeech.shutdown();
        }catch (Exception e){
            e.printStackTrace();
        }
        view=inflater.inflate(R.layout.fragment_nearest, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);

        nearestAdapter=new NearestAdapter(getActivity().getBaseContext(),buttonChildList);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(nearestAdapter);

        prepareData();
        return view;
    }

    private void prepareData() {
        Call<Object> service= ApiBuilder.getService().getNearestBranch(53.53323100,2.28486800,1);
        service.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Gson gson=new Gson();
                String json=gson.toJson(response.body());
                try {
                    JSONObject obj=new JSONObject(json);
                    JSONArray data=obj.getJSONArray("data");
                    ButtonChild buttonChild;
                    for(int i=0;i<data.length();i++){
                        JSONObject res=data.getJSONObject(i);
                        buttonChild=new ButtonChild(i+1,res.getString("StreetName"),Double.valueOf(Math.round(res.getDouble("distance"))),"km");
                        buttonChildList.add(buttonChild);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                nearestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

}
