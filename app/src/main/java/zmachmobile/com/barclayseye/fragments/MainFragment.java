package zmachmobile.com.barclayseye.fragments;


import android.content.Intent;
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
import zmachmobile.com.barclayseye.R;
import zmachmobile.com.barclayseye.activities.MainActivity;
import zmachmobile.com.barclayseye.adapters.ChooseServiceAdapter;
import zmachmobile.com.barclayseye.adapters.UsageModeAdapter;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    View view;

    List<ButtonChild> buttonChildList=new ArrayList<>();
    RecyclerView recyclerView;
    ChooseServiceAdapter chooseServiceAdapter;
    TextToSpeech tts;
    final int REQ_CODE_SPEECH_INPUT=100;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);

        chooseServiceAdapter=new ChooseServiceAdapter(getActivity().getBaseContext(),buttonChildList);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(chooseServiceAdapter);

        prepareData();

        tts = new TextToSpeech(getActivity().getBaseContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        tts.speak("Which Barclays service do you want to go to? 1. ATM. 2. Branch. Please say the number after beep. BEEP",TextToSpeech.QUEUE_FLUSH,null,"CHOOSE");
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
                startVoiceInput();
            }

            @Override
            public void onError(String s) {
            }
        });

        return view;
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
