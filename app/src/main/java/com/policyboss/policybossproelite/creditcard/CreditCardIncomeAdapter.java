package com.policyboss.policybossproelite.creditcard;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.policyboss.policybossproelite.R;

import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.FilterEntity;

public class CreditCardIncomeAdapter extends RecyclerView.Adapter<CreditCardIncomeAdapter.ViewHolder> {


    private LayoutInflater mInflater;
    Context mContext;
    List<FilterEntity> listSumAssured;


    // data is passed into the constructor
    CreditCardIncomeAdapter(Context context, List<FilterEntity> listSumAssured) {
        mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.listSumAssured = listSumAssured;

    }

    public void refreshBinding(List<FilterEntity> list) {
        listSumAssured = list;
        notifyDataSetChanged();
    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_sumassured, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.txtSumAssured.setText(listSumAssured.get(position).getAmount());
        if (listSumAssured.get(position).isSelected()) {
            holder.txtSumAssured.setBackgroundResource(R.drawable.sumassuredborder_blue);
            holder.txtSumAssured.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        } else {
            holder.txtSumAssured.setBackgroundResource(R.drawable.sumassured_border);
            holder.txtSumAssured.setTextColor(ContextCompat.getColor(mContext, R.color.black));
        }
        holder.txtSumAssured.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.txtSumAssured.setBackgroundResource(R.drawable.sumassuredborder_blue);
                holder.txtSumAssured.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                for (int i = 0; i < listSumAssured.size(); i++) {
                    listSumAssured.get(i).setSelected(false);
                }
                listSumAssured.get(position).setSelected(true);
                ((CreditCardActivity) mContext).selectedIncome(listSumAssured.get(position));
                notifyDataSetChanged();
            }
        });
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return listSumAssured.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSumAssured;

        ViewHolder(View itemView) {
            super(itemView);
            txtSumAssured = (TextView) itemView.findViewById(R.id.txtSumAssured);
        }


    }

    public void clearBinding() {

        for (int i = 0; i < listSumAssured.size(); i++) {
            listSumAssured.get(i).setSelected(false);
        }

        notifyDataSetChanged();
    }


}