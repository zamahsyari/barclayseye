package zmachmobile.com.barclayseye;

/**
 * Created by zmachmobile on 7/23/17.
 */

public class Question {
    public String question,color;
    public float size, rotation;
    public int correct;
    public Answer optA, optB, optC, optD;
    public Question(String question, String color, float size, float rotation, int correct, Answer optA, Answer optB, Answer optC, Answer optD){
        this.question=question;
        this.color=color;
        this.size=size;
        this.rotation=rotation;
        this.correct=correct;
        this.optA=optA;
        this.optB=optB;
        this.optC=optC;
        this.optD=optD;
    }
    public boolean isCorrect(int selected){
        if(selected==correct){
            return true;
        }else{
            return false;
        }
    }
}
