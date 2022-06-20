package com.policyboss.policybossproelite.offline_quotes;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.policyboss.policybossproelite.R;

import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.DocumentEntity;

/**
 * Created by Nilesh on 05/02/2018.
 */

public class UploadDocumentsAdapter extends RecyclerView.Adapter<UploadDocumentsAdapter.ApplicationItem> {

    Context mContex;
    List<DocumentEntity> mAppList;


    public UploadDocumentsAdapter(Context context, List<DocumentEntity> list) {
        this.mContex = context;
        this.mAppList = list;
    }


    @Override
    public ApplicationItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_document_upload, parent, false);
        return new ApplicationItem(itemView);
    }

    public class ApplicationItem extends RecyclerView.ViewHolder {

        ImageView ivPhoto, ivPhotoCam;
        TextView tvDocName,txtComment;
        LinearLayout lyParent;

        public ApplicationItem(View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.ivPhoto);
            ivPhotoCam = itemView.findViewById(R.id.ivPhotoCam);
            tvDocName = itemView.findViewById(R.id.tvDocName);
            txtComment = itemView.findViewById(R.id.txtComment);
            lyParent = itemView.findViewById(R.id.rlParent);
        }
    }

    @Override
    public void onBindViewHolder(ApplicationItem holder, int position) {

        if (holder instanceof ApplicationItem) {
            final ApplicationItem item = (ApplicationItem) holder;
            final DocumentEntity entity = mAppList.get(position);
            item.tvDocName.setText(entity.getDocname());

            if (entity.getDocpath().trim().equalsIgnoreCase("")) {
                holder.ivPhoto.setImageResource(R.drawable.doc_notuploaded);
                holder.txtComment.setVisibility(View.GONE);


            } else {
                holder.ivPhoto.setImageResource(R.drawable.doc_uploaded);
                holder.txtComment.setVisibility(View.VISIBLE);

            }
            item.ivPhotoCam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((AddOfflineQuotesActivity) mContex).galleryCamPopUp(entity);
                }
            });

            item.txtComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((AddOfflineQuotesActivity) mContex).viewUploadFile(entity.getDocpath());
                }
            });

        }

    }


    @Override
    public int getItemCount() {
        return mAppList.size();
    }

    public void updateList(DocumentEntity curEntity) {

        for (int pos = 0; pos < mAppList.size(); pos++) {
            if (mAppList.get(pos).getId() == (curEntity.getId())) {

                mAppList.set(pos, curEntity);

            }
        }

        notifyDataSetChanged();

        //  refreshAdapter(lstSpecial);
    }


}
