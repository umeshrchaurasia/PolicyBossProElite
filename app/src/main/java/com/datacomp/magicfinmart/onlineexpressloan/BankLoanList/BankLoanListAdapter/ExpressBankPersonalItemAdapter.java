package com.datacomp.magicfinmart.onlineexpressloan.BankLoanList.BankLoanListAdapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.datacomp.magicfinmart.R;
import com.datacomp.magicfinmart.motor.privatecar.activity.PrivateCarDetailActivity;
import com.datacomp.magicfinmart.onlineexpressloan.KotakpersonalloanActivity;
import com.datacomp.magicfinmart.onlineexpressloan.RblpersonalloanActivity;
import com.datacomp.magicfinmart.webviews.CommonWebViewActivity;

import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.express_loan.model.PersonalLoanEntity;

/**
 * Created by IN-RB on 05-04-2018.
 */

public class ExpressBankPersonalItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity mContext;
    DBPersistanceController dbPersistanceController;

    List<PersonalLoanEntity> personalLoanEntityList;

    public ExpressBankPersonalItemAdapter(Activity mContext, List<PersonalLoanEntity> temppersonalLoanEntityList) {
        this.mContext = mContext;
        this.personalLoanEntityList = temppersonalLoanEntityList;
        dbPersistanceController = new DBPersistanceController(mContext);
    }

    public class PersonLoanItemHolder extends RecyclerView.ViewHolder {
        TextView txtbankName, txtCardType;
        ImageView imgCard;
        CardView card_view;
        Button btnApply, btnInfo;

        public PersonLoanItemHolder(View itemView) {
            super(itemView);
            card_view = (CardView) itemView.findViewById(R.id.card_view);
            txtbankName = (TextView) itemView.findViewById(R.id.txtbankName);
            txtCardType = (TextView) itemView.findViewById(R.id.txtCardType);

            imgCard = (ImageView) itemView.findViewById(R.id.imgCard);

            btnInfo = (Button) itemView.findViewById(R.id.btnInfo);
            btnApply = (Button) itemView.findViewById(R.id.btnApply);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_banlist_onlineexp_item, parent, false);
        return new ExpressBankPersonalItemAdapter.PersonLoanItemHolder(view);


        //layout_banlist_onlineexp_item
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof PersonLoanItemHolder) {
            final PersonalLoanEntity plEntity = personalLoanEntityList.get(position);
            ((PersonLoanItemHolder) holder).txtbankName.setText(plEntity.getBank_Name());

            Glide.with(mContext)
                    .load(plEntity.getDocument1())
                    .placeholder(R.drawable.finmart_placeholder) // can also be a drawable
                    .into(((PersonLoanItemHolder) holder).imgCard);

            ((PersonLoanItemHolder) holder).card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    switch (plEntity.getProductType().toUpperCase()) {

                        case "PL":

                            if (plEntity.getBank_Code().toUpperCase().contains("RBL")) {
                                mContext.startActivity(new Intent(mContext, RblpersonalloanActivity.class));
                            }
                            else if (plEntity.getBank_Code().toUpperCase().contains("KOTAK")) {
                                mContext.startActivity(new Intent(mContext, KotakpersonalloanActivity.class));
                            }
                            else if (plEntity.getBank_Code().toUpperCase().contains("IIFL")) {
                                if(plEntity.getWebView() == 1)
                                {
                                    mContext.startActivity(new Intent(mContext, CommonWebViewActivity.class)
                                            .putExtra("URL", "http://www.rupeeboss.com/apply-iifl-loan")
                                            .putExtra("NAME", "IIFL")
                                            .putExtra("TITLE", "IIFL"));
                                }else {
                                    Toast.makeText(mContext, "Work in progress", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else if (plEntity.getBank_Code().toUpperCase().contains("HDFC")) {
                                Toast.makeText(mContext, "Work in progress", Toast.LENGTH_SHORT).show();
                            }
                            break;

                        case "SPL":
                            if (plEntity.getBank_Code().toUpperCase().contains("STPL")) {
                                Toast.makeText(mContext, "Work in progress", Toast.LENGTH_SHORT).show();
                            }
                            break;

                    }


                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return personalLoanEntityList.size();
    }
}