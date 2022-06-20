package com.policyboss.policybossproelite.offline_quotes.OfflineQuoteForm.Offline_Term;

import android.graphics.Paint;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.term.quoteapp.TermQuoteListFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.Utility;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.OfflineQuoteListEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.requestentity.TermFinmartRequest;

/**
 * Created by Nilesh birhade on 05/02/2018.
 */

public class TermQuoteAdapter_offline extends RecyclerView.Adapter<TermQuoteAdapter_offline.QuoteItem> implements Filterable {
    Fragment mFrament;
    List<TermFinmartRequest> mQuoteList;
    List<TermFinmartRequest> mQuoteListFiltered;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");



    public TermQuoteAdapter_offline(Fragment context, List<TermFinmartRequest> list) {
        this.mFrament = context;
        mQuoteList = list;
        mQuoteListFiltered = list;
    }

    @Override

    public QuoteItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_term_item_quote_offline, parent, false);
        return new TermQuoteAdapter_offline.QuoteItem(itemView);


    }

    @Override
    public void onBindViewHolder(QuoteItem holder, int position) {

        if (holder instanceof QuoteItem) {
            final TermFinmartRequest entity = mQuoteListFiltered.get(position);


            holder.txtPersonName.setText("ID# : " + entity.getTermRequestId()+"\nName : " + entity.getTermRequestEntity().getContactName());
            holder.txtCustRefNo.setText("Sum Insured : "+entity.getTermRequestEntity().getSumAssured());
            holder.txtQuoteDate.setText("Created Date : "+entity.getTermRequestEntity().getCreated_date());
            holder.llDetails.setTag(R.id.llDetails, entity);
            holder.txtOverflowMenu.setTag(R.id.txtOverflowMenu, entity);

            //holder.llDetails.setOnClickListener(this);
          //  holder.txtOverflowMenu.setOnClickListener(this);

            holder.llDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (entity.getQuote() == null || entity.getQuote().size() == 0)
                       // ((Good_OfflineMotorListActivity) mcontext).editOfflineMotor(entity);
                  //  TermFinmartRequest request = (TermFinmartRequest) view.getTag(view.getId());

                    ((TermQuoteListFragment_offline) mFrament).callInputTerm(entity.getTermRequestEntity().getInsurerId(), entity);

                }
            });


            if (entity.getQuote() != null && entity.getQuote().size() > 0) {
                holder.llQuoteList.setVisibility(View.VISIBLE);

                for (int i = 0; i < entity.getQuote().size(); i++) {
                    ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    TextView txtQuote = new TextView(mFrament.getActivity());
                    txtQuote.setPadding(0, 4, 0, 4);
                    txtQuote.setPaintFlags(txtQuote.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    txtQuote.setLayoutParams(lparams);
                    txtQuote.setTextColor(mFrament.getActivity().getResources().getColor(R.color.colorPrimary));
                    txtQuote.setText(entity.getQuote().get(i).getDocument_name());
                    txtQuote.setTag(R.id.llQuoteList, entity.getQuote().get(i));
                    txtQuote.setOnClickListener(onClickListener);
                    holder.llQuoteList.addView(txtQuote);
                }

            } else {
                holder.llQuoteList.setVisibility(View.GONE);
            }
        }

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            try {
                OfflineQuoteListEntity entity = (OfflineQuoteListEntity) v.getTag(R.id.llQuoteList);
                Utility.loadWebViewUrlInBrowser(mFrament.getActivity(), entity.getDocument_path());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private void openPopUp(View v, final TermFinmartRequest entity) {
        final PopupMenu popupMenu = new PopupMenu(mFrament.getActivity(), v);
        final Menu menu = popupMenu.getMenu();

        popupMenu.getMenuInflater().inflate(R.menu.recycler_menu_quote, menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menuCall:

                        ((TermQuoteListFragment) mFrament).dialNumber(entity.getTermRequestEntity().getContactMobile());
                        break;
                    case R.id.menuSms:
                        ((TermQuoteListFragment) mFrament).sendSms(entity.getTermRequestEntity().getContactMobile());
                        break;
                    case R.id.menuDelete:
                        ((TermQuoteListFragment) mFrament).removeQuote(entity);
                        break;
                }
                return false;
            }
        });

        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return mQuoteListFiltered.size();
    }



    public class QuoteItem extends RecyclerView.ViewHolder {

        //  public ImageView ivTripleDot;
        public TextView txtQuoteDate, txtCustRefNo, txtPersonName;
        LinearLayout llDetails;
        ImageView txtOverflowMenu;
        LinearLayout llQuoteList;

        public QuoteItem(View itemView) {
            super(itemView);
            txtQuoteDate = (TextView) itemView.findViewById(R.id.txtQuoteDate);
            txtCustRefNo = (TextView) itemView.findViewById(R.id.txtCustRefNo);
            txtPersonName = (TextView) itemView.findViewById(R.id.txtPersonName);
            txtOverflowMenu = (ImageView) itemView.findViewById(R.id.txtOverflowMenu);
            llDetails = (LinearLayout) itemView.findViewById(R.id.llDetails);
            llQuoteList = (LinearLayout) itemView.findViewById(R.id.llQuoteList);
        }
    }

    public void refreshAdapter(List<TermFinmartRequest> list) {
        mQuoteListFiltered = list;
        notifyDataSetChanged();
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
                    List<TermFinmartRequest> filteredList = new ArrayList<>();
                    for (TermFinmartRequest row : mQuoteList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTermRequestEntity().getContactName().toLowerCase().contains(charString.toLowerCase())
                                || row.getTermRequestEntity().getExisting_ProductInsuranceMapping_Id().toLowerCase().contains(charString.toLowerCase())) {
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
                mQuoteListFiltered = (ArrayList<TermFinmartRequest>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
