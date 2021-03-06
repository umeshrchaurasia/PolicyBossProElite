package com.policyboss.policybossproelite.offline_quotes.OfflineQuoteListAdapter;

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

import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.DocumentsOfflineEntity;

/**
 * Created by IN-RB on 09-11-2018.
 */

public class OfflineListItemAdapter extends RecyclerView.Adapter<OfflineListItemAdapter.OfflineDocItem> {

    Activity mcontext;
    List<DocumentsOfflineEntity> documentsOfflineList;

    public OfflineListItemAdapter(Activity mcontext, List<DocumentsOfflineEntity> documentsOfflineList) {
        this.mcontext = mcontext;
        this.documentsOfflineList = documentsOfflineList;
    }




    public class OfflineDocItem extends RecyclerView.ViewHolder {

        TextView txtDocName;
        LinearLayout lyParent;


        public OfflineDocItem(View view) {
            super(view);
            txtDocName = (TextView) view.findViewById(R.id.txtDocName);
            lyParent = (LinearLayout) view.findViewById(R.id.lyParent);
        }
    }

    @Override
    public OfflineListItemAdapter.OfflineDocItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_offiline_doc_item, parent, false);
        return new OfflineListItemAdapter.OfflineDocItem(view);
    }

    @Override
    public void onBindViewHolder(OfflineDocItem holder, int position) {
        final DocumentsOfflineEntity docuEntity = documentsOfflineList.get(position);
        holder.txtDocName.setText(docuEntity.getDocument_name());
        holder.txtDocName.setPaintFlags(holder.txtDocName.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);


        holder.lyParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // ((OfflineQuotesListActivity)mcontext).redirectToQuoteList(docuEntity);

            }
        });



    }




    @Override
    public int getItemCount() {
        return documentsOfflineList.size();
    }
}
