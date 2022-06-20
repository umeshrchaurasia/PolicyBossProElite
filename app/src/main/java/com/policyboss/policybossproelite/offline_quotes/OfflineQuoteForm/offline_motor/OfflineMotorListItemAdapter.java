package com.policyboss.policybossproelite.offline_quotes.OfflineQuoteForm.offline_motor;

import android.app.Activity;
import android.graphics.Paint;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.policyboss.policybossproelite.R;

import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.Utility;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.OfflineMotorListEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.OfflineQuoteListEntity;


public class OfflineMotorListItemAdapter extends RecyclerView.Adapter<OfflineMotorListItemAdapter.OfflineMotorItem> {

    Activity mcontext;
    List<OfflineMotorListEntity> offlineMotorList;

    public OfflineMotorListItemAdapter(Activity mcontext, List<OfflineMotorListEntity> documentsOfflineList) {
        this.mcontext = mcontext;
        this.offlineMotorList = documentsOfflineList;
    }


    public class OfflineMotorItem extends RecyclerView.ViewHolder {

        TextView txtName, txtVehicleNo, txtRegDate;
        LinearLayout llQuoteList, llOfflineMotor;


        public OfflineMotorItem(View view) {
            super(view);
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtVehicleNo = (TextView) view.findViewById(R.id.txtVehicleNo);
            txtRegDate = (TextView) view.findViewById(R.id.txtRegDate);
            llQuoteList = (LinearLayout) view.findViewById(R.id.llQuoteList);
            llOfflineMotor = (LinearLayout) view.findViewById(R.id.llOfflineMotor);
        }
    }

    @Override
    public OfflineMotorItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_offline_motor_list_item, parent, false);
        return new OfflineMotorItem(view);
    }

    @Override
    public void onBindViewHolder(OfflineMotorItem holder, int position) {

        final OfflineMotorListEntity entity = offlineMotorList.get(position);
        holder.txtName.setText("ID# : "+ entity.getMotorRequestEntity().getVehicleRequestID() +"\nName: " + entity.getMotorRequestEntity().getFirst_name() + " " + entity.getMotorRequestEntity().getLast_name());


      //  holder.txtName.setText("Name : " + entity.getMotorRequestEntity().getFirst_name() + " " + entity.getMotorRequestEntity().getLast_name());
        holder.txtVehicleNo.setText("Vehicle No : " + entity.getMotorRequestEntity().getRegistration_no());
        holder.txtRegDate.setText("Created Date : " + entity.getMotorRequestEntity().getCreated_date());
        holder.llOfflineMotor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (entity.getQuote() == null || entity.getQuote().size() == 0)
                    ((OfflineMotorListActivity) mcontext).editOfflineMotor(entity);
            }
        });


        if (entity.getQuote() != null && entity.getQuote().size() > 0) {
            holder.llQuoteList.setVisibility(View.VISIBLE);

            for (int i = 0; i < entity.getQuote().size(); i++) {
                ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView txtQuote = new TextView(mcontext);
                txtQuote.setPadding(0, 4, 0, 4);
                txtQuote.setPaintFlags(txtQuote.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                txtQuote.setLayoutParams(lparams);
                txtQuote.setTextColor(mcontext.getResources().getColor(R.color.colorPrimary));
                txtQuote.setText(entity.getQuote().get(i).getDocument_name());
                txtQuote.setTag(R.id.llQuoteList, entity.getQuote().get(i));
                txtQuote.setOnClickListener(onClickListener);
                holder.llQuoteList.addView(txtQuote);
            }

        } else {
            holder.llQuoteList.setVisibility(View.GONE);
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            try {
                OfflineQuoteListEntity entity = (OfflineQuoteListEntity) v.getTag(R.id.llQuoteList);
                Utility.loadWebViewUrlInBrowser(mcontext, entity.getDocument_path());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    public int getItemCount() {
        return offlineMotorList.size();
    }
}
