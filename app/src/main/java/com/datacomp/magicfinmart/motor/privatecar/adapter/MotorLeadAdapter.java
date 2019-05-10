package com.datacomp.magicfinmart.motor.privatecar.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datacomp.magicfinmart.R;
import com.datacomp.magicfinmart.motor.privatecar.fragment.MotorLeadFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.CarMasterEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.MotorMyLeadEntity;

/**
 * Created by Rajeev Ranjan on 07/05/2019.
 */
public class MotorLeadAdapter extends RecyclerView.Adapter<MotorLeadAdapter.LeadItem> implements View.OnClickListener, Filterable {

    Fragment mFrament;
    List<MotorMyLeadEntity> mLeadList;
    List<MotorMyLeadEntity> mLeadListFiltered;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public MotorLeadAdapter(Fragment context, List<MotorMyLeadEntity> list) {
        this.mFrament = context;
        this.mLeadList = list;
        mLeadListFiltered = list;
    }


    public class LeadItem extends RecyclerView.ViewHolder {

        public TextView txtQuoteDate, txtVehicleName, txtPersonName, txtCrnNo, txtView, txtEdit;
        LinearLayout llDetails, llViewLead, llEditLead;
        ImageView imgView, imgEdit;


        public LeadItem(View itemView) {
            super(itemView);
            txtQuoteDate = (TextView) itemView.findViewById(R.id.txtQuoteDate);
            txtVehicleName = (TextView) itemView.findViewById(R.id.txtVehicleName);
            txtPersonName = (TextView) itemView.findViewById(R.id.txtPersonName);

            txtView = (TextView) itemView.findViewById(R.id.txtView);
            txtEdit  = (TextView) itemView.findViewById(R.id.txtEdit);

            txtCrnNo = (TextView) itemView.findViewById(R.id.txtCrnNo);
            llViewLead = (LinearLayout) itemView.findViewById(R.id.llViewLead);
            llEditLead = (LinearLayout) itemView.findViewById(R.id.llEditLead);


            llDetails = (LinearLayout) itemView.findViewById(R.id.llDetails);

            imgView = (ImageView) itemView.findViewById(R.id.imgView);
            imgEdit =  (ImageView) itemView.findViewById(R.id.imgEdit);

        }
    }

    @Override
    public MotorLeadAdapter.LeadItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_motor_lead, parent, false);
        return new MotorLeadAdapter.LeadItem(itemView);
    }

    @Override
    public void onBindViewHolder(MotorLeadAdapter.LeadItem holder, int position) {

        if (holder instanceof MotorLeadAdapter.LeadItem) {

            final MotorMyLeadEntity entity = mLeadListFiltered.get(position);

            if (entity.getCRN().trim().equals("")) {
                holder.txtView.setText("New Lead");
              //  holder.imgView.setBackgroundResource(R.drawable.file_add);
                holder.llViewLead.setVisibility(View.VISIBLE);
                holder.llEditLead.setVisibility(View.GONE);

            } else {
                holder.txtView.setText("View Lead");
           //   holder.imgView.setBackgroundResource(R.drawable.agreemnet);
                holder.llViewLead.setVisibility(View.VISIBLE);
                holder.llEditLead.setVisibility(View.VISIBLE);

            }

            holder.txtPersonName.setText(entity.getName());
            holder.txtVehicleName.setText(entity.getMake() + "," + entity.getModel());


            holder.txtQuoteDate.setText(entity.getExpiryDate());
            holder.txtCrnNo.setText("" + entity.getCRN());

            holder.llViewLead.setTag(R.id.llViewLead, entity);
            holder.llEditLead.setTag(R.id.llEditLead, entity);

            holder.llViewLead.setOnClickListener(this);
            holder.llEditLead.setOnClickListener(this);
        }


    }

    @Override
    public int getItemCount() {
        if (mLeadListFiltered != null)
            return mLeadListFiltered.size();
        else
            return 0;
    }

    public void refreshAdapter(List<MotorMyLeadEntity> list) {
        mLeadList = list;
        mLeadListFiltered = list;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

//            case R.id.txtNew:
//
//               if( ((Button)view).getText().equals("VIEW")) {
//                   ((MotorLeadFragment) mFrament).redirectToInputQuote((MotorMyLeadEntity) view.getTag(view.getId()));
//               }else{
//                   ((MotorLeadFragment) mFrament).redirectToInputQuote((MotorMyLeadEntity) view.getTag(view.getId()));
//               }
//                break;
//            case R.id.txtEdit:
//                ((MotorLeadFragment) mFrament).redirectToInputQuote((MotorMyLeadEntity) view.getTag(view.getId()));
//                break;

            case R.id.llViewLead:
            case R.id.llEditLead:
                ((MotorLeadFragment) mFrament).redirectToInputQuote((MotorMyLeadEntity) view.getTag(view.getId()));
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
                    mLeadListFiltered = mLeadList;
                } else {
                    List<MotorMyLeadEntity> filteredList = new ArrayList<>();
                    for (MotorMyLeadEntity row : mLeadList) {
                        CarMasterEntity carMasterEntity = new CarMasterEntity();

                        if (row.getName().toLowerCase().contains(charString.toLowerCase())

                                || String.valueOf(row.getCRN()).contains(charString.toLowerCase())) {

                            filteredList.add(row);
                        }

//                        if (carMasterEntity != null) {
//                            if (carMasterEntity.getMake_Name().toLowerCase().contains(charString.toLowerCase())
//                                    || carMasterEntity.getModel_Name().toLowerCase().contains(charString.toLowerCase())) {
//                                filteredList.add(row);
//                            }
//                        }

                    }

                    mLeadListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mLeadListFiltered;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mLeadListFiltered = (ArrayList<MotorMyLeadEntity>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
