package zmachmobile.com.barclayseye.activities;

import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import zmachmobile.com.barclayseye.Config;
import zmachmobile.com.barclayseye.R;

public class FinalActivity extends AppCompatActivity {
    Button btnBackHome, btnBarclays;
    Toolbar myToolbar;
    String voiceInput,voiceTry;
    final int REQ_CODE_SPEECH_INPUT=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Config.isVoiceOnly==true){
            setContentView(R.layout.activity_final);
        }else{
            if(Config.isModeYellow==true){
                setContentView(R.layout.activity_final_yellow);
            }else{
                setContentView(R.layout.activity_final_green);
            }
        }
        overridePendingTransition(R.anim.anim_slide_in, R.anim.anim_slide_out);

        try{
            Config.textToSpeech.shutdown();
        }catch (Exception e){
            e.printStackTrace();
        }

        myToolbar = (Toolbar) findViewById(R.id.appBar);
        myToolbar.setTitle(R.string.app_name);
        setSupportActionBar(myToolbar);

        btnBackHome=(Button)findViewById(R.id.btnBackHome);
        btnBarclays=(Button)findViewById(R.id.btnBarclays);

        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        btnBarclays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        voiceInput="Congratulations, you have reached your destination! Now let’s choose what to do next. 1. Back to Homepage. 2. Browse Barclays Product. Please say the number after beep";
        voiceTry="We didn't get that, please try again";
        Config.textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
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
                    Toast.makeText(getApplicationContext(),result.get(0),Toast.LENGTH_SHORT).show();
                    if(result.get(0).equals("one")){
                        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("extra","main");
                        startActivity(intent);
                    }else if(result.get(0).equals("two")){
                        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("extra","main");
                        startActivity(intent);
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
