package com.policyboss.policybossproelite.health.quoappfragment;

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

import java.util.ArrayList;
import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.HealthApplication;

/**
 * Created by Nilesh on 05/02/2018.
 */

public class HealthApplicationAdapter extends RecyclerView.Adapter<HealthApplicationAdapter.ApplicationItem> implements View.OnClickListener, Filterable {
    Fragment fragment;
    List<HealthApplication> mAppList;
    List<HealthApplication> mAppListFiltered;

    public HealthApplicationAdapter(Fragment context, List<HealthApplication> mApplicationList) {
        this.fragment = context;
        mAppList = mApplicationList;
        mAppListFiltered = mApplicationList;
    }

    @Override

    public ApplicationItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_health_item_application, parent, false);
        return new ApplicationItem(itemView);
    }

    @Override
    public void onBindViewHolder(ApplicationItem holder, int position) {
        if (holder instanceof ApplicationItem) {
            HealthApplication healthApplication = mAppListFiltered.get(position);
            holder.txtCRN.setText(healthApplication.getCrn());
            holder.txtCreatedDate.setText(healthApplication.getHealthRequest().getCreated_date());
            holder.txtPersonName.setText(healthApplication.getHealthRequest().getContactName());
            holder.txtSumAssured.setText(healthApplication.getHealthRequest().getSumInsured());

            try {

                Glide.with(fragment).load(healthApplication.getInsImage()).into(holder.imgInsurerLogo);

                if (healthApplication.getHealthRequest().getStatusPercent() == 0) {
                    holder.imgProgressStatus.setImageDrawable(fragment.getResources().getDrawable(R.mipmap.status_10));
                } else if (healthApplication.getHealthRequest().getStatusPercent() == 50) {
                    holder.imgProgressStatus.setImageDrawable(fragment.getResources().getDrawable(R.mipmap.status_50));
                } else if (healthApplication.getHealthRequest().getStatusPercent() == 80) {
                    holder.imgProgressStatus.setImageDrawable(fragment.getResources().getDrawable(R.mipmap.status_80));
                } else if (healthApplication.getHealthRequest().getStatusPercent() == 90) {
                    holder.imgProgressStatus.setImageDrawable(fragment.getResources().getDrawable(R.mipmap.status_90));
                } else if (healthApplication.getHealthRequest().getStatusPercent() == 100) {
                    holder.imgProgressStatus.setImageDrawable(fragment.getResources().getDrawable(R.mipmap.status_100));
                } else if (healthApplication.getHealthRequest().getStatusPercent() == 25) {
                    holder.imgProgressStatus.setImageDrawable(fragment.getResources().getDrawable(R.mipmap.status_25));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.txtCRN.setTag(R.id.txtCRN, healthApplication);
            holder.txtCreatedDate.setTag(R.id.txtCreatedDate, healthApplication);
            holder.txtPersonName.setTag(R.id.txtPersonName, healthApplication);
            holder.txtSumAssured.setTag(R.id.txtSumAssured, healthApplication);
            holder.txtOverflowMenu.setTag(R.id.txtOverflowMenu, healthApplication);
            holder.imgInsurerLogo.setTag(R.id.imgInsurerLogo, healthApplication);
            holder.llApp.setTag(R.id.llApp, healthApplication);
            holder.llbottom.setTag(R.id.llbottom, healthApplication);


            holder.txtCRN.setOnClickListener(this);
            holder.txtCreatedDate.setOnClickListener(this);
            holder.txtPersonName.setOnClickListener(this);
            holder.txtSumAssured.setOnClickListener(this);
            holder.txtOverflowMenu.setOnClickListener(this);
            holder.imgInsurerLogo.setOnClickListener(this);
            holder.llApp.setOnClickListener(this);
            holder.llbottom.setOnClickListener(this);


        }
    }


    private void openPopUp(View v, final HealthApplication entity) {
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
                        ((HealthApplicationFragment) fragment).dialNumber(entity.getHealthRequest().getContactMobile());
                        break;
                    case R.id.menuSms:
                        ((HealthApplicationFragment) fragment).sendSms(entity.getHealthRequest().getContactMobile());
                        break;

                }
                return false;
            }
        });
        //displaying the popup
        popup.show();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.txtCRN:
            case R.id.txtCreatedDate:
            case R.id.txtPersonName:
            case R.id.txtSumAssured:
            case R.id.imgInsurerLogo:
            case R.id.llbottom:
            case R.id.llApp:
                ((HealthApplicationFragment) fragment).redirectToQuote((HealthApplication) view.getTag(view.getId()));
                break;
            case R.id.txtOverflowMenu:
                openPopUp(view, (HealthApplication) view.getTag(view.getId()));
                break;

        }
    }

    @Override
    public int getItemCount() {
        if (mAppListFiltered == null) {
            return 0;
        } else {
            return mAppListFiltered.size();
        }
    }

    public class ApplicationItem extends RecyclerView.ViewHolder {

        TextView txtOverflowMenu, txtCreatedDate, txtCRN, txtSumAssured, txtPersonName;
        ImageView imgInsurerLogo, imgProgressStatus;
        LinearLayout llbottom, llApp;

        public ApplicationItem(View itemView) {
            super(itemView);
            txtOverflowMenu = (TextView) itemView.findViewById(R.id.txtOverflowMenu);
            txtCreatedDate = (TextView) itemView.findViewById(R.id.txtCreatedDate);
            txtCRN = (TextView) itemView.findViewById(R.id.txtCRN);
            txtSumAssured = (TextView) itemView.findViewById(R.id.txtSumAssured);
            txtPersonName = (TextView) itemView.findViewById(R.id.txtPersonName);
            imgInsurerLogo = (ImageView) itemView.findViewById(R.id.imgInsurerLogo);
            imgProgressStatus = (ImageView) itemView.findViewById(R.id.imgProgressStatus);
            llbottom = (LinearLayout) itemView.findViewById(R.id.llbottom);
            llApp = (LinearLayout) itemView.findViewById(R.id.llApp);
        }
    }


    public void refreshAdapter(List<HealthApplication> list) {
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
                    List<HealthApplication> filteredList = new ArrayList<>();
                    for (HealthApplication row : mAppList) {
                        if (row.getHealthRequest().getContactName().toLowerCase().contains(charString.toLowerCase())
                                || row.getHealthRequest().getCrn().toLowerCase().contains(charString.toLowerCase())) {
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
                mAppListFiltered = (ArrayList<HealthApplication>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
