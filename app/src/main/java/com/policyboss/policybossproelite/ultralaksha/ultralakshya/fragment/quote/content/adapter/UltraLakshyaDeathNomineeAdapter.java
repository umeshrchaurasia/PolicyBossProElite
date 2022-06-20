package com.policyboss.policybossproelite.ultralaksha.ultralakshya.fragment.quote.content.adapter;

import android.graphics.Paint;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.ultralaksha.ultralakshya.fragment.quote.content.UltraLakshayDeathBenefitToNominee;

import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.UltraLakshaIllustrationResponseNew;

/**
 * Created by IN-RB on 22-02-2018.
 */

public class UltraLakshyaDeathNomineeAdapter extends RecyclerView.Adapter<UltraLakshyaDeathNomineeAdapter.DeathBenefitItem> {

    Fragment mContext;
    List<UltraLakshaIllustrationResponseNew.MasterDataBean.DeathBenefitBean>  DeathBenefitLst;


    public UltraLakshyaDeathNomineeAdapter(Fragment mContext,  List<UltraLakshaIllustrationResponseNew.MasterDataBean.DeathBenefitBean> deathBenefitLst) {
        this.mContext = mContext;
        this.DeathBenefitLst = deathBenefitLst;
    }


    public class DeathBenefitItem extends RecyclerView.ViewHolder
    {
        public TextView txtYear ,txtJeevanPremPaid ,txtJeevanBenefit , txtLakhshyaPremPaid ,txtLakhshyaBenefit;
        public LinearLayout lyParent;
        public Spanned htmlLink;
        public DeathBenefitItem(View itemView) {
            super(itemView);
            txtYear = (TextView) itemView.findViewById(R.id.txtYear);
            txtJeevanPremPaid = (TextView)itemView.findViewById(R.id.txtJeevanPremPaid);
            txtJeevanBenefit = (TextView)itemView.findViewById(R.id.txtJeevanBenefit);
            txtLakhshyaPremPaid = (TextView)itemView.findViewById(R.id.txtLakhshyaPremPaid);
            txtLakhshyaBenefit = (TextView)itemView.findViewById(R.id.txtLakhshyaBenefit);
            lyParent = (LinearLayout) itemView.findViewById(R.id.lyParent);

        }
    }


    @Override
    public UltraLakshyaDeathNomineeAdapter.DeathBenefitItem onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.death_benefit_item, parent, false);

        return new UltraLakshyaDeathNomineeAdapter.DeathBenefitItem(itemView);

    }

    @Override
    public void onBindViewHolder(DeathBenefitItem holder, int position) {

        final UltraLakshaIllustrationResponseNew.MasterDataBean.DeathBenefitBean deathBenefitEntity = DeathBenefitLst.get(position);
        holder.txtYear.setPaintFlags(holder.txtYear.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        holder.txtYear.setText( "" +deathBenefitEntity.getYear());

        holder.txtJeevanPremPaid.setText( "" +deathBenefitEntity.getJeevanPremiumPaid());
        holder.txtJeevanBenefit.setText( "" +deathBenefitEntity.getJeevanBenefitsPayable());

        holder.txtLakhshyaPremPaid.setText( "" +deathBenefitEntity.getLakshyaPremiumPaid());
        holder.txtLakhshyaBenefit.setText( "" +deathBenefitEntity.getLakshyaBenefitsPayable());


        holder.lyParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((UltraLakshayDeathBenefitToNominee)mContext).showDeathPayableAlert(deathBenefitEntity);
            }
        });


    }


    @Override
    public int getItemCount() {
        return DeathBenefitLst.size();
    }
}
