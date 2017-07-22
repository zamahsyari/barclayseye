package zmachmobile.com.barclayseye.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import zmachmobile.com.barclayseye.ButtonChild;
import zmachmobile.com.barclayseye.R;
import zmachmobile.com.barclayseye.activities.MapsActivity;

/**
 * Created by zmachmobile on 7/21/17.
 */

public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.MyViewHolder>{
    private List<ButtonChild> buttonChildList;
    private Context context;
    public TravelAdapter(Context context, List<ButtonChild> buttonChildList){
        this.buttonChildList=buttonChildList;
        this.context=context;
    }
    @Override
    public TravelAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_two,parent,false);
        return new TravelAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TravelAdapter.MyViewHolder holder, int position) {
        final ButtonChild buttonChild=buttonChildList.get(position);
        holder.orderNum.setText(String.valueOf(buttonChild.orderNum));
        holder.title.setText(buttonChild.title);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vibrator vb = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
                vb.vibrate(100);
                if(buttonChild.orderNum==1){
                    Intent intent=new Intent(context.getApplicationContext(), MapsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return buttonChildList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView orderNum, title;
        public LinearLayout layout;
        public MyViewHolder(View itemView) {
            super(itemView);
            orderNum=(TextView)itemView.findViewById(R.id.txtNum);
            title=(TextView)itemView.findViewById(R.id.txtTitle);
            layout=(LinearLayout)itemView.findViewById(R.id.linearLayout);
        }
    }
}
