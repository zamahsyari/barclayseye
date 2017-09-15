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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import zmachmobile.com.barclayseye.ButtonChild;
import zmachmobile.com.barclayseye.Config;
import zmachmobile.com.barclayseye.R;
import zmachmobile.com.barclayseye.activities.MainActivity;
import zmachmobile.com.barclayseye.activities.MapsActivity;
import zmachmobile.com.barclayseye.activities.UberActivity;
import zmachmobile.com.barclayseye.adapters.TravelAdapter;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class TravelFragment extends Fragment {
    View view;
    List<ButtonChild> buttonChildList=new ArrayList<>();
    RecyclerView recyclerView;
    TravelAdapter travelAdapter;
    String voiceInput,voiceTry, json;
    final int REQ_CODE_SPEECH_INPUT=100;

    public TravelFragment() {
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
            view=inflater.inflate(R.layout.fragment_travel, container, false);
        }else{
            if(Config.isModeYellow==true){
                view=inflater.inflate(R.layout.fragment_travel_yellow, container, false);
            }else{
                view=inflater.inflate(R.layout.fragment_travel_green, container, false);
            }
        }
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);

        travelAdapter=new TravelAdapter(getActivity().getBaseContext(),buttonChildList);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(travelAdapter);

        prepareData();

        voiceInput="How do you want to go from your location to there? 1. Guide me there. 2. Ride an Uber. 3. No thanks, I'm fine. Please say the number after beep";
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
        return view;
    }

    public void addJson(String json){
        this.json=json;
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
                    if(result.get(0).equals("one")){
                        Intent intent=new Intent(getContext(), MapsActivity.class);
                        intent.putExtra("json",json);
                        getContext().startActivity(intent);
                    }else if(result.get(0).equals("two")){
                        Intent intent=new Intent(getContext(), UberActivity.class);
                        intent.putExtra("json",json);
                        getContext().startActivity(intent);
                    }else if(result.get(0).equals("three")){
                        Intent intent=new Intent(getContext(), MainActivity.class);
                        intent.putExtra("extra","main");
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
