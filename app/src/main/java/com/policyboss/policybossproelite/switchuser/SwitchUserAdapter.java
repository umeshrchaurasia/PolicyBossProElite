package com.policyboss.policybossproelite.switchuser;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.policyboss.policybossproelite.R;

import java.util.ArrayList;
import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.PospAgentEntity;

/**
 * Created by Rajeev Ranjan on 06/12/2019.
 */
public class SwitchUserAdapter extends RecyclerView.Adapter<SwitchUserAdapter.SwitchUserItem> implements View.OnClickListener, Filterable {

    Activity mContext;
    List<PospAgentEntity> switchUserLst;
    List<PospAgentEntity> switchUserLstFiltered;

    public SwitchUserAdapter(Activity mContext, List<PospAgentEntity> switchUserLst) {
        this.mContext = mContext;
        this.switchUserLst = switchUserLst;
        switchUserLstFiltered = switchUserLst;
    }


    public class SwitchUserItem extends RecyclerView.ViewHolder {

        public TextView txtTitle, txtEmail, txtSSID, txtFBAID;
        public LinearLayout lyParent;

        public SwitchUserItem(@NonNull View itemView) {
            super(itemView);

            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtEmail = (TextView) itemView.findViewById(R.id.txtEmail);
            txtSSID = (TextView) itemView.findViewById(R.id.txtSSID);
            txtFBAID = (TextView) itemView.findViewById(R.id.txtFBAID);
            lyParent = (LinearLayout) itemView.findViewById(R.id.lyParent);
        }
    }

    @NonNull
    @Override
    public SwitchUserAdapter.SwitchUserItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_agent_search_item, parent, false);

        return new SwitchUserAdapter.SwitchUserItem(itemView);

    }

    @Override
    public void onBindViewHolder(SwitchUserItem holder, int position) {

        PospAgentEntity pospAgentEntity = switchUserLstFiltered.get(position);

        holder.txtTitle.setText("" + pospAgentEntity.getName());
        holder.txtEmail.setText("" + pospAgentEntity.getEmailId());
        holder.txtSSID.setText("SSID: " + pospAgentEntity.getSSID());
        holder.txtFBAID.setText("FBAID:" + pospAgentEntity.getFBAID());
        holder.lyParent.setTag(R.id.lyParent, pospAgentEntity);
        holder.lyParent.setOnClickListener(this);


    }

    @Override
    public int getItemCount() {

        if (switchUserLstFiltered == null) {
            return 0;
        } else {
            return switchUserLstFiltered.size();
        }

    }


    public void findAll(List<PospAgentEntity> tempswitchUserLst) {
        switchUserLst = tempswitchUserLst;
        switchUserLstFiltered = tempswitchUserLst;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {

        switch ((view.getId())) {

            case R.id.lyParent:

                ((SwitchUserActivity) mContext).redirectToSwitchUser((PospAgentEntity) view.getTag(R.id.lyParent));

                break;
        }

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    switchUserLstFiltered = switchUserLst;
                } else {
                    List<PospAgentEntity> filteredList = new ArrayList<>();
                    for (PospAgentEntity row : switchUserLst) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())
                          || row.getFBAID().toLowerCase().contains(charString.toLowerCase())
                          || row.getSSID().toLowerCase().contains(charString.toLowerCase()) )
                        {
                            filteredList.add(row);
                        }
                    }

                    switchUserLstFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = switchUserLstFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults filterResults) {
                switchUserLstFiltered = (ArrayList<PospAgentEntity>) filterResults.values;
                notifyDataSetChanged();

            }
        };


    }


}
