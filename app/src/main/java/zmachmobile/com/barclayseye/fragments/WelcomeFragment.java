package zmachmobile.com.barclayseye.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
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
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(getActivity().getBaseContext(),WelcomeActivity.class);
                intent.putExtra("extra","move");
                getActivity().startActivity(intent);
            }
        },3000);
        view=inflater.inflate(R.layout.fragment_welcome,container,false);
        return view;
    }
}
