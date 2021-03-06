package com.policyboss.policybossproelite.motor.privatecar.adapter;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.motor.privatecar.fragment.MotorApplicationFragment;

import java.util.ArrayList;
import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.Utility;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.ApplicationListEntity;

/**
 * Created by Rajeev Ranjan on 11/01/2018.
 */

public class MotorApplicationAdapter extends RecyclerView.Adapter<MotorApplicationAdapter.ApplicationItem> implements Filterable, View.OnClickListener {
    Fragment fragment;
    List<ApplicationListEntity> mAppList;
    List<ApplicationListEntity> mAppListFiltered;

    public MotorApplicationAdapter(Fragment context, List<ApplicationListEntity> mApplicationList) {
        this.fragment = context;
        mAppList = mApplicationList;
        mAppListFiltered = mApplicationList;
    }

    @Override

    public ApplicationItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_application, parent, false);
        return new ApplicationItem(itemView);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ll:
            case R.id.txtPersonName:
                ((MotorApplicationFragment) fragment).redirectApplication((ApplicationListEntity) view.getTag(view.getId()));
                break;
        }
    }

    @Override
    public void onBindViewHolder(ApplicationItem holder, int position) {

        if (holder instanceof ApplicationItem) {

            final ApplicationListEntity entity = mAppListFiltered.get(position);

            holder.txtPersonName.setText(entity.getMotorRequestEntity().getFirst_name()
                    + " " + entity.getMotorRequestEntity().getLast_name());
            holder.txtCRN.setText(String.valueOf(entity.getMotorRequestEntity().getCrn()));
            holder.txtCreatedDate.setText("" + entity.getMotorRequestEntity().getCreated_date());

            if (!(entity.getMotorRequestEntity().getRegistration_no().endsWith("-AA-1234")))
                holder.txtVehicleNo.setText("" + entity.getMotorRequestEntity().getRegistration_no());
            holder.txtPersonName.setTag(R.id.txtPersonName, entity);
            holder.txtPersonName.setOnClickListener(this);
            holder.ll.setTag(R.id.ll, entity);
            holder.ll.setOnClickListener(this);

            holder.txtOverflowMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openPopUp(view, entity);
                }
            });

            int statusPercent = entity.getMotorRequestEntity().getStatusPercent();
            if (statusPercent == 0) {
                holder.imgProgressStatus.setImageDrawable(fragment.getResources().getDrawable(R.mipmap.status_10));
            } else if (statusPercent == 50) {
                holder.imgProgressStatus.setImageDrawable(fragment.getResources().getDrawable(R.mipmap.status_50));
            } else if (statusPercent == 80) {
                holder.imgProgressStatus.setImageDrawable(fragment.getResources().getDrawable(R.mipmap.status_80));
            } else if (statusPercent == 90) {
                holder.imgProgressStatus.setImageDrawable(fragment.getResources().getDrawable(R.mipmap.status_90));
            } else if (statusPercent == 100) {
                holder.imgProgressStatus.setImageDrawable(fragment.getResources().getDrawable(R.mipmap.status_100));
            } else if (statusPercent == 25) {
                holder.imgProgressStatus.setImageDrawable(fragment.getResources().getDrawable(R.mipmap.status_25));
            }

           /* if (entity.getMotorRequestEntity().getStatusPercent() == 25
                    || entity.getMotorRequestEntity().getStatusPercent() == 0) {
                holder.imgProgressStatus.setImageDrawable(fragment.getResources().getDrawable(R.mipmap.status_25));
            } else if (entity.getMotorRequestEntity().getStatusPercent() == 50) {
                holder.imgProgressStatus.setImageDrawable(fragment.getResources().getDrawable(R.mipmap.status_50));
            } else {
                holder.imgProgressStatus.setImageDrawable(fragment.getResources().getDrawable(R.mipmap.status_100));
            }*/

            try {

                /*int logo = new DBPersistanceController(fragment.getActivity())
                        .getInsurerLogo(entity.getSelectedPrevInsID());

                Glide.with(fragment).load(logo)
                        .into(holder.imgInsurerLogo);*/
                Glide.with(fragment).load(Utility.getInsurerImage("" + entity.getSelectedPrevInsID()))
                        .into(holder.imgInsurerLogo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void openPopUp(View v, final ApplicationListEntity entity) {
        //creating a popup menu
        PopupMenu popup = new PopupMenu(fragment.getActivity(), v);
        //inflating menu from xml resource
        popup.inflate(R.menu.recycler_menu_application);
        //adding click listener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuCall:
                        ((MotorApplicationFragment) fragment).dialNumber(entity.getMotorRequestEntity().getMobile());
                        //Toast.makeText(fragment.getActivity(), "WIP " + entity.getMotorRequestEntity().getMobile(), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menuSms:
                        ((MotorApplicationFragment) fragment).sendSms(entity.getMotorRequestEntity().getMobile());
                        //Toast.makeText(fragment.getActivity(), "WIP SMS ", Toast.LENGTH_SHORT).show();
                        break;

                }
                return false;
            }
        });
        //displaying the popup
        popup.show();
    }


    @Override
    public int getItemCount() {
        if (mAppListFiltered == null) {
            return 0;
        } else {
            return mAppListFiltered.size();
        }
    }

    public void refreshAdapter(List<ApplicationListEntity> list) {
        mAppListFiltered = list;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mAppListFiltered = mAppList;
                } else {
                    List<ApplicationListEntity> filteredList = new ArrayList<>();
                    for (ApplicationListEntity row : mAppList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getMotorRequestEntity().getFirst_name().toLowerCase().contains(charString.toLowerCase())
                                || row.getMotorRequestEntity().getLast_name().toLowerCase().contains(charString.toLowerCase())
                                || row.getMotorRequestEntity().getRegistration_no().toLowerCase().contains(charString.toLowerCase())
                                || String.valueOf(row.getMotorRequestEntity().getCrn()).contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    mAppListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mAppListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mAppListFiltered = (ArrayList<ApplicationListEntity>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ApplicationItem extends RecyclerView.ViewHolder {

        TextView txtOverflowMenu, txtCreatedDate, txtCRN, txtVehicleNo, txtPersonName;
        ImageView imgInsurerLogo, imgProgressStatus;
        LinearLayout ll;

        public ApplicationItem(View itemView) {
            super(itemView);
            ll = (LinearLayout) itemView.findViewById(R.id.ll);
            txtOverflowMenu = (TextView) itemView.findViewById(R.id.txtOverflowMenu);
            txtCreatedDate = (TextView) itemView.findViewById(R.id.txtCreatedDate);
            txtCRN = (TextView) itemView.findViewById(R.id.txtCRN);
            txtVehicleNo = (TextView) itemView.findViewById(R.id.txtVehicleNo);
            txtPersonName = (TextView) itemView.findViewById(R.id.txtPersonName);
            imgInsurerLogo = (ImageView) itemView.findViewById(R.id.imgInsurerLogo);
            imgProgressStatus = (ImageView) itemView.findViewById(R.id.imgProgressStatus);
        }
    }


   /* public void refreshAdapter(List<ApplicationListEntity> list) {
        mAppList = list;
    }*/

}
