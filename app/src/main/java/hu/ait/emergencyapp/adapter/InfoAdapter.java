package hu.ait.emergencyapp.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hu.ait.emergencyapp.R;
import hu.ait.emergencyapp.data.Article;
import hu.ait.emergencyapp.data.City;
import hu.ait.emergencyapp.data.Info;

/**
 * Created by jessicahong on 7/3/17.
 */

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {

    private List<Info> infoList;
    private boolean colorOne = true;

    public InfoAdapter() {
        infoList = new ArrayList<Info>();

    }

    @Override
    public InfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View infoRow = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.info_row, parent, false);

        if (colorOne){
            infoRow.setBackgroundColor(Color.parseColor("#ffada1"));
            colorOne = !colorOne;

        } else {
            infoRow.setBackgroundColor(Color.parseColor("#fff2f0"));
            colorOne = !colorOne;

        }

        return new ViewHolder(infoRow);
    }

    @Override
    public void onBindViewHolder(InfoAdapter.ViewHolder holder, int position) {
        holder.infoName.setText(infoList.get(position).getInfoName());
        holder.infoContent.setText(infoList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    public void addCityInfo(City city){
        if(city.getGeneralEmergency() != null){
            infoList.add(new Info("General Emergency", city.getGeneralEmergency()));
        }
        if(city.getPoliceNumber() != null){
            infoList.add(new Info("Police", city.getPoliceNumber()));
        }
        if(city.getAmbulanceNumber() != null){
            infoList.add(new Info("Ambulance", city.getAmbulanceNumber()));
        }
        if(city.getFireNumber() != null){
            infoList.add(new Info("Fire", city.getFireNumber()));
        }

    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView infoName;
        private TextView infoContent;

        public ViewHolder(View itemView) {
            super(itemView);

            infoName = (TextView) itemView.findViewById(R.id.infoName);
            infoContent = (TextView) itemView.findViewById(R.id.infoContent);

        }
    }
}
