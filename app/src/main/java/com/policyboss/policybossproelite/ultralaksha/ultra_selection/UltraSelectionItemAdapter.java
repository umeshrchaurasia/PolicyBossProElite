package com.policyboss.policybossproelite.ultralaksha.ultra_selection;

import android.content.Context;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.policyboss.policybossproelite.R;

import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.model.TermSelectionEntity;

public class UltraSelectionItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    List<TermSelectionEntity> listInsur;

    public UltraSelectionItemAdapter(Context context) {
        mContext = context;
        listInsur = new DBPersistanceController(mContext).getUltraLakshaList();
    }

    public class DashboardItemHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView txtProductName, txtProductDesc;
        CardView card_view;

        public DashboardItemHolder(View view) {
            super(view);
            card_view = (CardView) view.findViewById(R.id.card_view);
            imgIcon = (ImageView) view.findViewById(R.id.imgIcon);
            txtProductName = (TextView) view.findViewById(R.id.txtProductName);
            txtProductDesc = (TextView) view.findViewById(R.id.txtProductDesc);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_term_selection_item, parent, false);
        return new DashboardItemHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof DashboardItemHolder) {
            final TermSelectionEntity termSelectionEntity = listInsur.get(position);
            if (termSelectionEntity.getCompantID() == 0)
                ((DashboardItemHolder) holder).imgIcon.setImageResource(R.drawable.ultra_lakshya);


            ((DashboardItemHolder) holder).txtProductName.setText(termSelectionEntity.getCompanyName());
            ((DashboardItemHolder) holder).card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ((UltraLakshaSelectionActivity)mContext).planClick(termSelectionEntity);

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listInsur.size();
    }
}