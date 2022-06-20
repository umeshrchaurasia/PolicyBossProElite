package com.policyboss.policybossproelite.helpfeedback.raiseticket.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.helpfeedback.raiseticket.RaiseTicketActivity;
import com.policyboss.policybossproelite.utility.CircleTransform;

import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.RaiseTickeViewEntity;

/**
 * Created by Rajeev Ranjan on 09/05/2019.
 */
public class RaiseTicketViewAdapter extends RecyclerView.Adapter<RaiseTicketViewAdapter.RaiseTicketViewItem> {

    Context context;
    List<RaiseTickeViewEntity> viewEntityList;

    public RaiseTicketViewAdapter(Context context, List<RaiseTickeViewEntity> viewEntityList) {
        this.context = context;
        this.viewEntityList = viewEntityList;
    }

    public class RaiseTicketViewItem extends RecyclerView.ViewHolder {

        public ImageView ivUser;
        public ReadMoreTextView txtComment;
        public LinearLayout lyParent;

        public RaiseTicketViewItem(View itemView) {
            super(itemView);
            txtComment = itemView.findViewById(R.id.txtComment);
            lyParent = itemView.findViewById(R.id.lyParent);
            ivUser = itemView.findViewById(R.id.ivUser);
        }
    }

    @Override
    public RaiseTicketViewAdapter.RaiseTicketViewItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_view_raise_ticket, parent, false);
        return new RaiseTicketViewAdapter.RaiseTicketViewItem(itemView);

    }

    @Override
    public void onBindViewHolder(RaiseTicketViewAdapter.RaiseTicketViewItem holder, int position) {

        if (holder instanceof RaiseTicketViewAdapter.RaiseTicketViewItem) {
            final RaiseTickeViewEntity entity = viewEntityList.get(position);

            holder.txtComment.setText("" + entity.getComment());

            Glide.with(context)
                    .load(entity.getDocpath())
                   // .override(60, 60)
                    .transform(new CircleTransform(context))
                    .into(holder.ivUser);

            holder.ivUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ((RaiseTicketActivity) context).redirectToRaiseTicket(view, entity.getDocpath());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return viewEntityList.size();
    }
}
