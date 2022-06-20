package com.policyboss.policybossproelite.motor.privatecar.adapter;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.motor.privatecar.fragment.MotorLeadFragment;

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

        public TextView txtQuoteDate, txtVehicleName, txtPersonName, txtCrnNo, txtView, txtEdit, txtLost;
        LinearLayout llDetails, llViewLead, llEditLead, llNewLead;


        public LeadItem(View itemView) {
            super(itemView);
            txtQuoteDate = (TextView) itemView.findViewById(R.id.txtQuoteDate);
            txtVehicleName = (TextView) itemView.findViewById(R.id.txtVehicleName);
            txtPersonName = (TextView) itemView.findViewById(R.id.txtPersonName);

            txtView = (TextView) itemView.findViewById(R.id.txtView);
            txtEdit = (TextView) itemView.findViewById(R.id.txtEdit);
            txtLost = (TextView) itemView.findViewById(R.id.txtLost);
            txtCrnNo = (TextView) itemView.findViewById(R.id.txtCrnNo);

            llNewLead = (LinearLayout) itemView.findViewById(R.id.llNewLead);
            llViewLead = (LinearLayout) itemView.findViewById(R.id.llViewLead);
            llEditLead = (LinearLayout) itemView.findViewById(R.id.llEditLead);


            llDetails = (LinearLayout) itemView.findViewById(R.id.llDetails);


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

                holder.llNewLead.setVisibility(View.VISIBLE);
                holder.llViewLead.setVisibility(View.GONE);
                holder.llEditLead.setVisibility(View.GONE);

            } else {
                holder.llNewLead.setVisibility(View.GONE);
                holder.llViewLead.setVisibility(View.VISIBLE);
                holder.llEditLead.setVisibility(View.VISIBLE);

            }

//            textView1.setBackgroundResource(R.drawable.customeborder);
//            textView1.setTextColor(ContextCompat.getColor(PersonalLoanApplyActivity.this, R.color.description_text));

            if (entity.getHasDisposition() != null) {
                if (entity.getHasDisposition().equals("0")) {
                    holder.txtLost.setEnabled(true);
                    holder.llNewLead.setEnabled(true);
                    holder.llViewLead.setEnabled(true);
                    holder.llEditLead.setEnabled(true);

                    holder.txtLost.setText("Update Status");

                    holder.txtLost.setAlpha(1f);
                    holder.llNewLead.setAlpha(1f);
                    holder.llViewLead.setAlpha(1f);
                    holder.llEditLead.setAlpha(1f);

                } else {
                    holder.txtLost.setEnabled(false);
                    holder.llNewLead.setEnabled(false);
                    holder.llViewLead.setEnabled(false);
                    holder.llEditLead.setEnabled(false);

                    holder.txtLost.setText("Lost");

                    holder.txtLost.setAlpha(0.6f);
                    holder.llNewLead.setAlpha(0.6f);
                    holder.llViewLead.setAlpha(0.6f);
                    holder.llEditLead.setAlpha(0.6f);

                }
            } else {
                holder.txtLost.setEnabled(false);
                holder.llNewLead.setEnabled(false);
                holder.llViewLead.setEnabled(false);
                holder.llEditLead.setEnabled(false);

            }

            holder.txtPersonName.setText(entity.getName());
            holder.txtVehicleName.setText(entity.getMake() + "," + entity.getModel());


            holder.txtQuoteDate.setText(entity.getExpiryDate());
            holder.txtCrnNo.setText("" + entity.getCRN());

            holder.llNewLead.setTag(R.id.llNewLead, entity);
            holder.llViewLead.setTag(R.id.llViewLead, entity);
            holder.llEditLead.setTag(R.id.llEditLead, entity);
            holder.txtLost.setTag(R.id.txtLost, entity);

            holder.llNewLead.setOnClickListener(this);
            holder.llViewLead.setOnClickListener(this);
            holder.llEditLead.setOnClickListener(this);
            holder.txtLost.setOnClickListener(this);
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


            case R.id.llNewLead:
                MotorMyLeadEntity myLeadEntity1 = (MotorMyLeadEntity) view.getTag(view.getId());
                myLeadEntity1.setCRN("");
                ((MotorLeadFragment) mFrament).redirectToInputQuote(myLeadEntity1);
                break;

            case R.id.llEditLead:
                MotorMyLeadEntity myLeadEntity2 = (MotorMyLeadEntity) view.getTag(view.getId());
                ((MotorLeadFragment) mFrament).redirectEditToInputQuote(myLeadEntity2);
                break;
            case R.id.llViewLead:
                MotorMyLeadEntity myLeadEntity3 = (MotorMyLeadEntity) view.getTag(view.getId());

                ((MotorLeadFragment) mFrament).redirectViewToInputQuote(myLeadEntity3);
                break;

            case R.id.txtLost:
                MotorMyLeadEntity entity = (MotorMyLeadEntity) view.getTag(view.getId());
                if(entity.getVehicleRequestID().equalsIgnoreCase(""))
                {
                    entity.setVehicleRequestID("0");
                }
                ((MotorLeadFragment) mFrament).redirectToCommenttAlert(entity);
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


    public void updateDispostion(MotorMyLeadEntity entity) {
        for (MotorMyLeadEntity item : mLeadListFiltered) {

            if (item.equals(entity)) {

                int pos = mLeadListFiltered.indexOf(item);
                mLeadListFiltered.get(pos).setHasDisposition("1");
                break;
            }

        }
        notifyDataSetChanged();

    }
}
