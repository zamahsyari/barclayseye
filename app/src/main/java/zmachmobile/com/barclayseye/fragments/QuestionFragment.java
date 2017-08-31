package zmachmobile.com.barclayseye.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import zmachmobile.com.barclayseye.Config;
import zmachmobile.com.barclayseye.Question;
import zmachmobile.com.barclayseye.R;
import zmachmobile.com.barclayseye.activities.QuizActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragment extends Fragment {
    View view;
    TextView txtQuestion;

    public QuestionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try{
            Config.textToSpeech.shutdown();
        }catch (Exception e){
            e.printStackTrace();
        }
        view=inflater.inflate(R.layout.fragment_question, container, false);
        txtQuestion=view.findViewById(R.id.txtQuestion);

        if(Config.currentQuestion+1 >= Config.questions.size()){
            Intent intent=new Intent(getActivity().getBaseContext(),QuizActivity.class);
            intent.putExtra("extra","final");
            getActivity().startActivity(intent);
        }else{
            Config.currentQuestion= Config.currentQuestion+1;
            Question question= Config.questions.get(Config.currentQuestion-1);

            txtQuestion.setText(question.question);
            txtQuestion.setTextColor(Color.parseColor(question.color));
            txtQuestion.setTextSize(question.size);
            txtQuestion.setRotation(question.rotation);

            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent(getActivity().getBaseContext(),QuizActivity.class);
                    intent.putExtra("extra","answer");
                    getActivity().startActivity(intent);
                }
            },3000);
        }
        return view;
    }

}
