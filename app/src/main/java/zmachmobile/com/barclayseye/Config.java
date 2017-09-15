package zmachmobile.com.barclayseye;

import android.speech.tts.TextToSpeech;

import java.util.ArrayList;

/**
 * Created by zmachmobile on 7/22/17.
 */

public class Config {
    public static final String baseUrl="https://katakamu.id/barclayseye-api/public/api/";
    public static ArrayList<Question> questions=new ArrayList<Question>();
    public static int currentQuestion=0;
    public static TextToSpeech textToSpeech;
    public static boolean showWelcome=true;
    public static int score  = 0;
    public static boolean isVoiceOnly=true;
    public static boolean isModeYellow=false;
}
