package zmachmobile.com.barclayseye.fragments;


import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zmachmobile.com.barclayseye.ApiBuilder;
import zmachmobile.com.barclayseye.Config;
import zmachmobile.com.barclayseye.R;
import zmachmobile.com.barclayseye.activities.MainActivity;
import zmachmobile.com.barclayseye.activities.UberActivity;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class UberFragment extends Fragment {
    View view;
    Button btnRequest;
    TextView txtDestination,txtETA;
    String voiceInput,voiceTry;
    final int REQ_CODE_SPEECH_INPUT=100;
    String selectedStreet;

    public UberFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try{
            Config.textToSpeech.shutdown();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(Config.isVoiceOnly==true){
            view=inflater.inflate(R.layout.fragment_uber, container, false);
        }else{
            if(Config.isModeYellow==true){
                view=inflater.inflate(R.layout.fragment_uber_yellow, container, false);
            }else{
                view=inflater.inflate(R.layout.fragment_uber_green, container, false);
            }
        }
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

    public void addSelectedStreet(String selectedStreet){
        this.selectedStreet=selectedStreet;
    }

    private void loadData() {
        Call<Object> service = ApiBuilder.getService().requestUber(53.463536, -2.291418,53.4750000000,-2.2865500000);
        service.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Gson gson=new Gson();
                String json=gson.toJson(response.body());
                try {
                    JSONObject obj=new JSONObject(json);
                    JSONObject data=obj.getJSONObject("data");
                    txtETA.setText("Pickup in "+data.getString("estimate_arrival_time"));

                    voiceInput="It will cost about ";
                    voiceTry="We didn't get that, please try again";
                    Config.textToSpeech = new TextToSpeech(getActivity().getBaseContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if(status != TextToSpeech.ERROR) {
                                Config.textToSpeech.setLanguage(Locale.UK);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    Config.textToSpeech.speak(voiceInput,TextToSpeech.QUEUE_FLUSH,null,"CHOOSE");
                                }
                            }
                        }
                    });
                    afterSpeech();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    public void afterSpeech(){
        Config.textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {

            }

            @Override
            public void onDone(String s) {
                ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC,ToneGenerator.MAX_VOLUME);
                toneGen1.startTone(ToneGenerator.TONE_PROP_BEEP,150);
                startVoiceInput();
            }

            @Override
            public void onError(String s) {

            }
        });
    }

    private void startVoiceInput() {
        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault());
        try{
            startActivityForResult(intent,REQ_CODE_SPEECH_INPUT);
        }catch (Exception e){

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQ_CODE_SPEECH_INPUT:{
                if(resultCode==RESULT_OK && null != data){
                    ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Toast.makeText(getActivity().getBaseContext(),result.get(0),Toast.LENGTH_SHORT).show();
                    if(result.get(0).equals("yes")){
                        Intent intent=new Intent(getActivity().getBaseContext(),UberActivity.class);
                        intent.putExtra("extra","loading");
                        getActivity().startActivity(intent);
                    }else if(result.get(0).equals("no")) {
                        getActivity().finish();
                    }else{
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Config.textToSpeech.speak(voiceTry,TextToSpeech.QUEUE_FLUSH,null,"CHOOSE");
                        }
                        afterSpeech();
                    }
                }
            }
            break;
        }
    }

}
