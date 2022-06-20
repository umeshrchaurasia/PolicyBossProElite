package com.policyboss.policybossproelite.offline_quotes.OfflineQuoteForm.Offline_Term;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.policyboss.policybossproelite.R;

/**
 * Created by IN-RB on 21-05-2018.
 */

public class HdfcIProtectAdapter_offline extends RecyclerView.Adapter<HdfcIProtectAdapter_offline.MyViewHolder> {

    Activity mContext;
    String[] IprotectLst;

    public HdfcIProtectAdapter_offline(Activity mContext, String[] iprotectLst) {
        this.mContext = mContext;
        IprotectLst = iprotectLst;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtMessage;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtMessage = (TextView) itemView.findViewById(R.id.txtMessage);
        }
    }


    @Override
    public HdfcIProtectAdapter_offline.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.iprotect_item_offline, parent, false);

        return new HdfcIProtectAdapter_offline.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        String strMsg = IprotectLst[position];

        holder.txtMessage.setText("" + strMsg);

    }


    @Override
    public int getItemCount() {
        return IprotectLst.length;
    }
}
