package zmachmobile.com.barclayseye;

/**
 * Created by zmachmobile on 7/20/17.
 */

public class ButtonChild {
    public int orderNum;
    public String title, unit;
    public Double distance;
    public ButtonChild(int orderNum, String title, Double distance, String unit){
        this.orderNum=orderNum;
        this.title=title;
        this.distance=distance;
        this.unit=unit;
    }
}
