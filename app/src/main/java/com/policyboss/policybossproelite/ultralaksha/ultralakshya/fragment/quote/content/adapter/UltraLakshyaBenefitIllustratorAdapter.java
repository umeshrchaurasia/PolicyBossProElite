package com.policyboss.policybossproelite.ultralaksha.ultralakshya.fragment.quote.content.adapter;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.policyboss.policybossproelite.R;

import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.UltraLakshaIllustrationResponseNew;

/**
 * Created by IN-RB on 22-02-2018.
 */

public class UltraLakshyaBenefitIllustratorAdapter extends RecyclerView.Adapter<UltraLakshyaBenefitIllustratorAdapter.BenefitIllustratorItem> {

    Fragment mContext;

    private List<UltraLakshaIllustrationResponseNew.MasterDataBean.BenefitsBean> BenefitsIllustratorLst;


    public UltraLakshyaBenefitIllustratorAdapter(Fragment mContext, List<UltraLakshaIllustrationResponseNew.MasterDataBean.BenefitsBean> deathBenefitLst) {
        this.mContext = mContext;
        this.BenefitsIllustratorLst = deathBenefitLst;
    }


    public class BenefitIllustratorItem extends RecyclerView.ViewHolder
    {
        public TextView txtPolicyYear,txtAge ,txtLifeCover ,txtAnnualPrem,txtCashFlow,txtLoanAvailable;
        public LinearLayout lyParent;
        public BenefitIllustratorItem(View itemView) {
            super(itemView);
            txtPolicyYear = (TextView) itemView.findViewById(R.id.txtPolicyYear);
            txtAge = (TextView)itemView.findViewById(R.id.txtAge);
            txtLifeCover = (TextView)itemView.findViewById(R.id.txtLifeCover);
            txtAnnualPrem = (TextView)itemView.findViewById(R.id.txtAnnualPrem);
            txtCashFlow = (TextView)itemView.findViewById(R.id.txtCashFlow);
            txtLoanAvailable = (TextView)itemView.findViewById(R.id.txtLoanAvailable);
            lyParent = (LinearLayout) itemView.findViewById(R.id.lyParent);

        }
    }


    @Override
    public UltraLakshyaBenefitIllustratorAdapter.BenefitIllustratorItem onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.benefit_illustrator_item, parent, false);

        return new UltraLakshyaBenefitIllustratorAdapter.BenefitIllustratorItem(itemView);

    }

    @Override
    public void onBindViewHolder(BenefitIllustratorItem holder, int position) {

        final UltraLakshaIllustrationResponseNew.MasterDataBean.BenefitsBean benefitEntity = BenefitsIllustratorLst.get(position);
        holder.txtPolicyYear.setText( "" +benefitEntity.getYear());
        holder.txtAge.setText( "" +benefitEntity.getAge());
        holder.txtLifeCover.setText( "" +benefitEntity.getLicCover());

        holder.txtAnnualPrem.setText( "" +benefitEntity.getAnnualPremium());
        holder.txtCashFlow.setText( "" +benefitEntity.getCashFlow());
        holder.txtLoanAvailable.setText( "" +benefitEntity.getLoanAvailable());

    }

    @Override
    public int getItemCount() {
        return BenefitsIllustratorLst.size();
    }
}
