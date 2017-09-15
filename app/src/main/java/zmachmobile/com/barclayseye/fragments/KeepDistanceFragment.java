package zmachmobile.com.barclayseye.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import zmachmobile.com.barclayseye.Answer;
import zmachmobile.com.barclayseye.Config;
import zmachmobile.com.barclayseye.Question;
import zmachmobile.com.barclayseye.R;
import zmachmobile.com.barclayseye.activities.QuizActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class KeepDistanceFragment extends Fragment {
    Button btnStart;
    View view;

    public KeepDistanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try{
            Config.textToSpeech.shutdown();
        }catch (Exception e){
            e.printStackTrace();
        }
        view=inflater.inflate(R.layout.fragment_keep_distance, container, false);
        btnStart=(Button)view.findViewById(R.id.btnStart);
        Config.questions=new ArrayList<Question>();

        addQuestions();

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity().getBaseContext(),QuizActivity.class);
                intent.putExtra("extra","question");
                getActivity().startActivity(intent);
            }
        });
        return view;
    }

    private void addQuestions() {
        Answer optA=new Answer("W",0);
        Answer optB=new Answer("B",0);
        Answer optC=new Answer("D",0);
        Answer optD=new Answer("M",0);
        Question question=new Question("M","#696969",50,0,4,optA,optB,optC,optD);
        Config.questions.add(question);

        optA=new Answer("G",0);
        optB=new Answer("C",0);
        optC=new Answer("P",0);
        optD=new Answer("O",0);
        question=new Question("P","#696969",36,0,3,optA,optB,optC,optD);
        Config.questions.add(question);

        optA=new Answer("V",0);
        optB=new Answer("U",0);
        optC=new Answer("Y",0);
        optD=new Answer("S",0);
        question=new Question("U","#696969",25,0,2,optA,optB,optC,optD);
        Config.questions.add(question);

        optA=new Answer("C",0);
        optB=new Answer("C",90);
        optC=new Answer("C",180);
        optD=new Answer("C",270);
        question=new Question("C","#979797",59,270,4,optA,optB,optC,optD);
        Config.questions.add(question);

        optA=new Answer("C",0);
        optB=new Answer("C",90);
        optC=new Answer("C",180);
        optD=new Answer("C",270);
        question=new Question("C","#979797",59,180,3,optA,optB,optC,optD);
        Config.questions.add(question);

        optA=new Answer("C",0);
        optB=new Answer("C",90);
        optC=new Answer("C",180);
        optD=new Answer("C",270);
        question=new Question("C","#979797",59,90,2,optA,optB,optC,optD);
        Config.questions.add(question);
    }

}
