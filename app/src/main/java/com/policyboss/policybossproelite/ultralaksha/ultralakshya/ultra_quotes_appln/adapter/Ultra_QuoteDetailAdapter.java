package com.policyboss.policybossproelite.ultralaksha.ultralakshya.ultra_quotes_appln.adapter;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.ultralaksha.ultralakshya.ultra_quotes_appln.fragment.UltraQuoteDetailFragment;

import java.util.ArrayList;
import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.finmart.requestentity.UltralakshaRequestEntity;

/**
 * Created by Rajeev Ranjan on 19/02/2019.
 */

public class Ultra_QuoteDetailAdapter extends RecyclerView.Adapter<Ultra_QuoteDetailAdapter.QuoteItem> implements  Filterable {

    Fragment mFrament;
    List<UltralakshaRequestEntity> mQuoteList;
    List<UltralakshaRequestEntity> mQuoteListFiltered;

    public Ultra_QuoteDetailAdapter(Fragment mFrament, List<UltralakshaRequestEntity> list) {
        this.mFrament = mFrament;
        this.mQuoteList = list;
        mQuoteListFiltered = list;
    }

    public class QuoteItem extends RecyclerView.ViewHolder {

        LinearLayout lyParent;
        public TextView txtPersonName, txtCrn, txtQuoteDate, tvQuoteDate, tvloanamount;
        public ImageView txtOverflowMenu;

        public QuoteItem(View itemView) {
            super(itemView);
            txtQuoteDate = (TextView) itemView.findViewById(R.id.txtQuoteDate);
            txtPersonName = (TextView) itemView.findViewById(R.id.txtPersonName);

            txtCrn = (TextView) itemView.findViewById(R.id.txtCrn);
            tvQuoteDate = (TextView) itemView.findViewById(R.id.tvQuoteDate);
            lyParent = (LinearLayout) itemView.findViewById(R.id.lyParent);

            txtOverflowMenu = (ImageView) itemView.findViewById(R.id.txtOverflowMenu);

        }
    }

    @Override
    public QuoteItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_quote_ultra, parent, false);
        return new Ultra_QuoteDetailAdapter.QuoteItem(itemView);
    }

    @Override
    public void onBindViewHolder(QuoteItem holder, int position) {

        if (holder instanceof Ultra_QuoteDetailAdapter.QuoteItem) {
            final UltralakshaRequestEntity entity = mQuoteListFiltered.get(position);
            try {

                holder.txtPersonName.setText("" + entity.getContactName());
                holder.txtQuoteDate.setText("" + entity.getCreatedDate());

                holder.txtCrn.setText("" + entity.getCRN());

                 holder.lyParent.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         ((UltraQuoteDetailFragment)mFrament).redirectQuote(entity);
                     }
                 });



                holder.txtOverflowMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // openPopUp(view, entity);
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return mQuoteListFiltered.size();
    }



    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mQuoteListFiltered = mQuoteList;
                } else {
                    List<UltralakshaRequestEntity> filteredList = new ArrayList<>();
                    for (UltralakshaRequestEntity row : mQuoteList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getContactName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    mQuoteListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mQuoteListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mQuoteListFiltered = (ArrayList<UltralakshaRequestEntity>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


}
