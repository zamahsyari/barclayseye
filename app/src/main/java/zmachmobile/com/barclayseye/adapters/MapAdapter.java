package zmachmobile.com.barclayseye.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import zmachmobile.com.barclayseye.ButtonChild;
import zmachmobile.com.barclayseye.MapChild;
import zmachmobile.com.barclayseye.R;

/**
 * Created by zmachmobile on 9/14/17.
 */

public class MapAdapter extends RecyclerView.Adapter<MapAdapter.MyViewHolder> {
    private List<MapChild> mapChildList;

    public MapAdapter(List<MapChild> mapChildList){
        this.mapChildList=mapChildList;
    }

    @Override
    public MapAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_map,parent,false);
        return new MapAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MapAdapter.MyViewHolder holder, int position) {
        MapChild mapChild=mapChildList.get(position);
        holder.txtTitle.setText(mapChild.title);
        holder.txtDistance.setText(mapChild.distance);
    }

    @Override
    public int getItemCount() {
        return mapChildList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDistance, txtTitle;
        public MyViewHolder(View itemView) {
            super(itemView);
            txtDistance=(TextView)itemView.findViewById(R.id.txtDistance);
            txtTitle=(TextView)itemView.findViewById(R.id.txtTitle);
        }
    }
}
