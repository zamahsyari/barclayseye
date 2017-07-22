package zmachmobile.com.barclayseye.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zmachmobile.com.barclayseye.ApiBuilder;
import zmachmobile.com.barclayseye.R;
import zmachmobile.com.barclayseye.activities.WelcomeActivity;

public class WelcomeFragment extends Fragment {
    View view;
    Button btnChoose;

    public WelcomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_welcome,container,false);
        btnChoose=(Button)view.findViewById(R.id.btnChooseMode);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vibrator vb = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                vb.vibrate(100);
                Intent intent=new Intent(getActivity().getBaseContext(),WelcomeActivity.class);
                intent.putExtra("extra","move");
                getActivity().startActivity(intent);
            }
        });
        getApiVersion();
        return view;
    }

    private void getApiVersion() {
        Call<Object> version=ApiBuilder.getService().getVersion();
        version.enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Gson gson=new Gson();
                String resp=gson.toJson(response.body());
                try {
                    JSONObject obj=new JSONObject(resp);
                    Log.i("RESULT","VERSION : "+obj.getString("version")+", STATE : "+obj.getString("state")+", YEAR : "+obj.getString("year"));
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
