package zmachmobile.com.barclayseye.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zmachmobile.com.barclayseye.ApiBuilder;
import zmachmobile.com.barclayseye.R;
import zmachmobile.com.barclayseye.activities.MainActivity;
import zmachmobile.com.barclayseye.activities.UberActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class UberFragment extends Fragment {
    View view;
    Button btnRequest;
    TextView txtDestination,txtETA;

    public UberFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_uber, container, false);
        btnRequest=(Button)view.findViewById(R.id.btnRequest);
        txtDestination=(TextView)view.findViewById(R.id.txtDestination);
        txtETA=(TextView)view.findViewById(R.id.txtETA);
        txtDestination.setText("Barclays ATM, Market Street");
        loadData();
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

    private void loadData() {
        Call<Object> service = ApiBuilder.getService().requestUber(-6.190235,106.798434,-6.203888,106.801224);
        service.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Gson gson=new Gson();
                String json=gson.toJson(response.body());
                try {
                    JSONObject obj=new JSONObject(json);
                    JSONObject data=obj.getJSONObject("data");
                    txtETA.setText("Pickup in "+data.getString("estimate_arrival_time"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

}
