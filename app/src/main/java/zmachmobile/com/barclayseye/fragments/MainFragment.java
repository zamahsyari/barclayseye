package zmachmobile.com.barclayseye.fragments;


import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import zmachmobile.com.barclayseye.ButtonChild;
import zmachmobile.com.barclayseye.Config;
import zmachmobile.com.barclayseye.R;
import zmachmobile.com.barclayseye.activities.MainActivity;
import zmachmobile.com.barclayseye.adapters.ChooseServiceAdapter;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    View view;

    List<ButtonChild> buttonChildList=new ArrayList<>();
    RecyclerView recyclerView;
    ChooseServiceAdapter chooseServiceAdapter;
    final int REQ_CODE_SPEECH_INPUT=100;

    SwipeRefreshLayout onRefresh;
    String voiceInput,voiceTry;
    ImageButton btnSpeak;

    public MainFragment() {
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
            view=inflater.inflate(R.layout.fragment_main, container, false);
        }else{
            if(Config.isModeYellow==true){
                view=inflater.inflate(R.layout.fragment_main_yellow, container, false);
            }else{
                view=inflater.inflate(R.layout.fragment_main_green, container, false);
            }
        }
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);

        onRefresh=(SwipeRefreshLayout)view.findViewById(R.id.onRefresh);
        btnSpeak=(ImageButton) view.findViewById(R.id.btnSpeak);

        chooseServiceAdapter=new ChooseServiceAdapter(getActivity().getBaseContext(),buttonChildList);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(chooseServiceAdapter);

        prepareData();

        voiceInput="Hello, which Barclays service do you want to go to? 1. ATM. 2. Branch. Please say the number after beep.";
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

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vibrator vb = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                vb.vibrate(100);
                startVoiceInput();
            }
        });

        onRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Config.textToSpeech.speak(voiceInput,TextToSpeech.QUEUE_FLUSH,null,"CHOOSE");
                }
                afterSpeech();

            }
        });

        return view;
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
        onRefresh.setRefreshing(false);
    }

    private void prepareData() {
        ButtonChild buttonChild=new ButtonChild(1,"ATM",null,null);
        buttonChildList.add(buttonChild);

        buttonChild=new ButtonChild(2,"Branch",null,null);
        buttonChildList.add(buttonChild);

        chooseServiceAdapter.notifyDataSetChanged();
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
                        Intent intent=new Intent(getActivity().getBaseContext(),MainActivity.class);
                        intent.putExtra("extra","nearest");
                        getActivity().startActivity(intent);
                    }else if(result.get(0).equals("two")){
                        Intent intent=new Intent(getActivity().getBaseContext(),MainActivity.class);
                        intent.putExtra("extra","nearest");
                        getActivity().startActivity(intent);
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
