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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zmachmobile.com.barclayseye.ApiBuilder;
import zmachmobile.com.barclayseye.ButtonChild;
import zmachmobile.com.barclayseye.Config;
import zmachmobile.com.barclayseye.R;
import zmachmobile.com.barclayseye.activities.MainActivity;
import zmachmobile.com.barclayseye.adapters.NearestAdapter;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearestFragment extends Fragment {
    View view;

    List<ButtonChild> buttonChildList=new ArrayList<>();
    RecyclerView recyclerView;
    ImageButton btnSpeak;
    NearestAdapter nearestAdapter;
    String voiceInput,voiceTry;
    final int REQ_CODE_SPEECH_INPUT=100;
    String atm;

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
        if(Config.isVoiceOnly==true){
            view=inflater.inflate(R.layout.fragment_nearest, container, false);
        }else{
            if(Config.isModeYellow==true){
                view=inflater.inflate(R.layout.fragment_nearest_green, container, false);
            }else{
                view=inflater.inflate(R.layout.fragment_nearest_yellow, container, false);
            }
        }
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);
        btnSpeak=(ImageButton)view.findViewById(R.id.btnSpeak);

        nearestAdapter=new NearestAdapter(getActivity().getBaseContext(),buttonChildList);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(nearestAdapter);

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSpeak();
            }
        });

        prepareData();
        return view;
    }

    private void prepareData() {
        Call<Object> service= ApiBuilder.getService().getNearestBranch(53.463536, -2.291418,1);
        service.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Gson gson=new Gson();
                String json=gson.toJson(response.body());
                try {
                    JSONObject obj=new JSONObject(json);
                    JSONArray data=obj.getJSONArray("data");
                    ButtonChild buttonChild;
                    String atm="";
                    for(int i=0;i<data.length();i++){
                        JSONObject res=data.getJSONObject(i);
                        buttonChild=new ButtonChild(i+1,res.getString("StreetName"),Double.valueOf(Math.round(res.getDouble("distance"))),"km");
                        buttonChildList.add(buttonChild);
                        atm+=(i+1)+". "+res.getString("StreetName")+", "+Double.valueOf(Math.round(res.getDouble("distance")))+" km away from your location. ";
                    }

                    doSpeak();

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

    private void doSpeak() {
        voiceInput="Hi, we found 3 nearest ATM around you. "+atm+".Please say the number after beep.";
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
                    JSONObject json=new JSONObject();
                    if(result.get(0).equals("one")){
                        try {
                            json.put("street",buttonChildList.get(0).title);
                            json.put("distance",buttonChildList.get(0).distance);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent intent=new Intent(getContext(),MainActivity.class);
                        intent.putExtra("extra","travel");
                        intent.putExtra("json",json.toString());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(intent);
                    }else if(result.get(0).equals("two")){
                        try {
                            json.put("street",buttonChildList.get(1).title);
                            json.put("distance",buttonChildList.get(1).distance);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent intent=new Intent(getContext(),MainActivity.class);
                        intent.putExtra("extra","travel");
                        intent.putExtra("json",json.toString());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(intent);
                    }else if(result.get(0).equals("three")){
                        try {
                            json.put("street",buttonChildList.get(2).title);
                            json.put("distance",buttonChildList.get(2).distance);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent intent=new Intent(getContext(),MainActivity.class);
                        intent.putExtra("extra","travel");
                        intent.putExtra("json",json.toString());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(intent);
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
