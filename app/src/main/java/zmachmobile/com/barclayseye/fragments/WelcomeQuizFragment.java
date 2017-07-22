package zmachmobile.com.barclayseye.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import zmachmobile.com.barclayseye.activities.QuizActivity;
import zmachmobile.com.barclayseye.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomeQuizFragment extends Fragment {
    Button btnTakeQuiz, btnSkip;
    View view;
    public WelcomeQuizFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_welcome_quiz, container, false);
        btnTakeQuiz=(Button)view.findViewById(R.id.btnTakeQuiz);
        btnSkip=(Button)view.findViewById(R.id.btnSkip);

        btnTakeQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vibrator vb = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                vb.vibrate(100);
                Intent intent=new Intent(getActivity().getBaseContext(),QuizActivity.class);
                intent.putExtra("extra","step1");
                startActivity(intent);
            }
        });
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vibrator vb = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                vb.vibrate(100);
                Intent intent=new Intent(getActivity().getBaseContext(),QuizActivity.class);
                intent.putExtra("extra","skip");
                startActivity(intent);
            }
        });
        return view;
    }

}
