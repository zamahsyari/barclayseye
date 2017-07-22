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
import android.support.v7.view.menu.ListMenuItemView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import zmachmobile.com.barclayseye.ButtonChild;
import zmachmobile.com.barclayseye.activities.MainActivity;
import zmachmobile.com.barclayseye.activities.QuizActivity;
import zmachmobile.com.barclayseye.R;
import zmachmobile.com.barclayseye.adapters.UsageModeAdapter;

import static android.app.Activity.RESULT_OK;

public class ChooseModeFragment extends Fragment {
    View view;
    ImageButton btnSpeak;
    TextToSpeech tts;
    final int REQ_CODE_SPEECH_INPUT=100;

    List<ButtonChild> buttonChildList=new ArrayList<>();
    RecyclerView recyclerView;
    UsageModeAdapter usageModeAdapter;

    public ChooseModeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_choose_mode, container, false);
        btnSpeak=view.findViewById(R.id.btnSpeak);

        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);

        usageModeAdapter=new UsageModeAdapter(getActivity().getBaseContext(),buttonChildList);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(usageModeAdapter);

        prepareData();

        tts = new TextToSpeech(getActivity().getBaseContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        tts.speak("Welcome, please select application usage mode. 1. Full voice assistance. 2. Full visual guidance. Please say the number after beep.",TextToSpeech.QUEUE_FLUSH,null,"CHOOSE");
                    }
                }
            }
        });
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
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

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vibrator vb = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                vb.vibrate(100);
                startVoiceInput();
            }
        });
        return view;
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

    private void prepareData() {
        ButtonChild buttonChild=new ButtonChild(1,"Full voice assistance",null,null);
        buttonChildList.add(buttonChild);

        buttonChild=new ButtonChild(2,"Full visual guidance",null,null);
        buttonChildList.add(buttonChild);

        usageModeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQ_CODE_SPEECH_INPUT:{
                if(resultCode==RESULT_OK && null != data){
                    ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Toast.makeText(getActivity().getBaseContext(),result.get(0),Toast.LENGTH_SHORT).show();
                    if(result.get(0).equals("two")){
                        Intent intent=new Intent(getActivity().getBaseContext(),MainActivity.class);
                        getActivity().startActivity(intent);
                    }else{
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            tts.speak("We didn't get that, please try again",TextToSpeech.QUEUE_FLUSH,null,"CHOOSE");
                        }
                        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                            @Override
                            public void onStart(String s) {

                            }

                            @Override
                            public void onDone(String s) {
                                startVoiceInput();
                            }

                            @Override
                            public void onError(String s) {

                            }
                        });
                    }
                }
            }
            break;
        }
    }
}