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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import zmachmobile.com.barclayseye.Config;
import zmachmobile.com.barclayseye.activities.MainActivity;
import zmachmobile.com.barclayseye.activities.QuizActivity;
import zmachmobile.com.barclayseye.R;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomeQuizFragment extends Fragment {
    Button btnTakeQuiz, btnSkip;
    View view;
    final int REQ_CODE_SPEECH_INPUT=100;
    SwipeRefreshLayout onRefresh;
    String voiceInput,voiceTry;

    public WelcomeQuizFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try{
            Config.textToSpeech.shutdown();
        }catch (Exception e){
            e.printStackTrace();
        }
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_welcome_quiz, container, false);
        btnTakeQuiz=(Button)view.findViewById(R.id.btnTakeQuiz);
        btnSkip=(Button)view.findViewById(R.id.btnSkip);
        onRefresh=(SwipeRefreshLayout)view.findViewById(R.id.onRefresh);

        btnTakeQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vibrator vb = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                vb.vibrate(100);
                Intent intent=new Intent(getActivity().getBaseContext(),QuizActivity.class);
                intent.putExtra("extra","welcome");
                startActivity(intent);
            }
        });
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vibrator vb = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                vb.vibrate(100);
                Intent intent=new Intent(getActivity().getBaseContext(),MainActivity.class);
                intent.putExtra("extra","main");
                startActivity(intent);
            }
        });

        voiceInput="Let's take a quiz to determine your visual guidance. Don't worry, we will only test on your first time using this app. Please choose the number after beep. 1. Take the quiz. 2. Skip the quiz.";
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

    private void startVoiceInput() {
        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault());
        try{
            startActivityForResult(intent,REQ_CODE_SPEECH_INPUT);
        }catch (Exception e){

        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQ_CODE_SPEECH_INPUT:{
                if(resultCode==RESULT_OK && null != data){
                    ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Toast.makeText(getActivity().getBaseContext(),result.get(0),Toast.LENGTH_SHORT).show();
                    if(result.get(0).equals("one")){
                        Intent intent=new Intent(getActivity().getBaseContext(),QuizActivity.class);
                        intent.putExtra("extra","question");
                        startActivity(intent);
                    }else if(result.get(0).equals("two")){
                        Intent intent=new Intent(getActivity().getBaseContext(),MainActivity.class);
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
