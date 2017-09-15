package zmachmobile.com.barclayseye.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import zmachmobile.com.barclayseye.Config;
import zmachmobile.com.barclayseye.Question;
import zmachmobile.com.barclayseye.R;
import zmachmobile.com.barclayseye.activities.QuizActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnswerFragment extends Fragment {
    LinearLayout optA, optB, optC, optD;
    TextView txtOptA, txtOptB, txtOptC, txtOptD;
    Button btnNotsure;
    View view;
    int correct;

    public AnswerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try{
            Config.textToSpeech.shutdown();
        }catch (Exception e){
            e.printStackTrace();
        }

        view=inflater.inflate(R.layout.fragment_answer, container, false);
        optA=(LinearLayout)view.findViewById(R.id.optA);
        optB=(LinearLayout)view.findViewById(R.id.optB);
        optC=(LinearLayout)view.findViewById(R.id.optC);
        optD=(LinearLayout)view.findViewById(R.id.optD);
        txtOptA=(TextView)view.findViewById(R.id.txtOptA);
        txtOptB=(TextView)view.findViewById(R.id.txtOptB);
        txtOptC=(TextView)view.findViewById(R.id.txtOptC);
        txtOptD=(TextView)view.findViewById(R.id.txtOptD);
        btnNotsure=(Button)view.findViewById(R.id.btnNotsure);

        loadData();

        optA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoNext(1);
            }
        });
        optB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoNext(2);
            }
        });
        optC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoNext(3);
            }
        });
        optD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoNext(4);
            }
        });
        btnNotsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoNext(0);
            }
        });
        return view;
    }

    private void gotoNext(int answer) {
        if(answer == correct){
            Config.score+=1;
        }
        Log.i("SCORE","skor : "+Config.score);
        Intent intent=new Intent(getActivity().getBaseContext(),QuizActivity.class);
        intent.putExtra("extra","question");
        getActivity().startActivity(intent);
    }

    private void loadData() {
        Question question= Config.questions.get(Config.currentQuestion-1);
        txtOptA.setText(question.optA.answer);
        txtOptA.setRotation(question.optA.rotation);

        txtOptB.setText(question.optB.answer);
        txtOptB.setRotation(question.optB.rotation);

        txtOptC.setText(question.optC.answer);
        txtOptC.setRotation(question.optC.rotation);

        txtOptD.setText(question.optD.answer);
        txtOptD.setRotation(question.optD.rotation);

        correct=question.correct;
    }

}
