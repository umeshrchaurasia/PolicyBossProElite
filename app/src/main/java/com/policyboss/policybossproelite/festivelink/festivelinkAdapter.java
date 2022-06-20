package com.policyboss.policybossproelite.festivelink;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.design.CustomImageView;

import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.FestivalCompaignEntity;


public class festivelinkAdapter extends RecyclerView.Adapter<festivelinkAdapter.MyViewHolder> implements View.OnClickListener {

    Context mContext;
    List<FestivalCompaignEntity> lstShareMessageEntities;

    //  private static   Uri imageUri = null;
    public festivelinkAdapter(Context mContext, List<FestivalCompaignEntity> lstShareMessageEntities) {
        this.mContext = mContext;
        this.lstShareMessageEntities = lstShareMessageEntities;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.festive_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final FestivalCompaignEntity lstShareMessageEntity = lstShareMessageEntities.get(position);

        holder.lyParent.setTag(R.id.lyParent, lstShareMessageEntity);

        SimpleTarget target = new SimpleTarget<Bitmap>(300, 300) {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                if (bitmap != null) {
                    holder.lyParent.setOnClickListener(festivelinkAdapter.this);
                    holder.ivProduct.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                holder.ivProduct.setBackgroundDrawable(mContex.getResources()
//                        .getDrawable(R.drawable.finmart_placeholder));
                holder.lyParent.setOnClickListener(null);
            }
        };
        if (lstShareMessageEntity.getImagelink() != null) {

            Glide.with(mContext)
                    .load(lstShareMessageEntity.getImagelink())
                    .asBitmap()
                    //.placeholder(R.drawable.finmart_placeholder) // can also be a drawable
                    .into(target);


        }


    }

    @Override
    public int getItemCount() {
        return lstShareMessageEntities.size();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.lyParent) {
            ((festivelinkActivity) mContext).redirectToApplyMain((FestivalCompaignEntity) v.getTag(R.id.lyParent));
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lyParent;

        CustomImageView ivProduct;

        public MyViewHolder(@NonNull View view) {
            super(view);

            lyParent = (LinearLayout) itemView.findViewById(R.id.lyParent);
            ;
            ivProduct = (CustomImageView) itemView.findViewById(R.id.imglink);
        }
    }
}
