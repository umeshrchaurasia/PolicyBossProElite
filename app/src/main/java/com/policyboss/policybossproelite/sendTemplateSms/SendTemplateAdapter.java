package com.policyboss.policybossproelite.sendTemplateSms;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.policyboss.policybossproelite.R;

import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.SmsTemplateEntity;

/**
 * Created by IN-RB on 22-02-2018.
 */

public class SendTemplateAdapter extends RecyclerView.Adapter<SendTemplateAdapter.NotificationItem> {

    Activity mContext;

    List<SmsTemplateEntity> smsTemplateEntityList;
    public SendTemplateAdapter(Activity mContext, List<SmsTemplateEntity> templateEntityList) {
        this.mContext = mContext;
        smsTemplateEntityList = templateEntityList;
    }

    public class NotificationItem extends RecyclerView.ViewHolder
    {
        public TextView txtTitle ,txtStatus;
        public ImageView ivSMS;
        public LinearLayout lyParent;
        public NotificationItem(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);

            ivSMS = (ImageView) itemView.findViewById(R.id.ivSMS);
            lyParent = (LinearLayout) itemView.findViewById(R.id.lyParent);

        }
    }


    @Override
    public SendTemplateAdapter.NotificationItem onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.send_sms_temp_item, parent, false);

        return new SendTemplateAdapter.NotificationItem(itemView);

    }

    @Override
    public void onBindViewHolder(NotificationItem holder, int position) {

        final SmsTemplateEntity smsTemplateEntity = smsTemplateEntityList.get(position);

        if(smsTemplateEntity.getFba_sms_id() == 4){
            holder.lyParent.setVisibility(View.GONE);

        }else{
            holder.lyParent.setVisibility(View.VISIBLE);
        }
        holder.txtTitle.setText( "" +smsTemplateEntity.getTemplete());

        holder.lyParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((SendTemplateSmsActivity)mContext).redirectToTempate(smsTemplateEntity);
            }

        });
    }


    @Override
    public int getItemCount() {
        return smsTemplateEntityList.size();
    }
}
