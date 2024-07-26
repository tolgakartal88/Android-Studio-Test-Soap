package com.example.tptradingsoap.ui.metal.metalhelper;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tptradingsoap.R;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MetalRecyclerViewAdapter extends RecyclerView.Adapter<MetalRecyclerViewAdapter.MetalViewHolder> {

    private ArrayList<MetalDto> mData;
    LayoutInflater inflater;
    Context _context;

    public MetalRecyclerViewAdapter(Context context, ArrayList<MetalDto> data) {
        inflater = LayoutInflater.from(context);
        this.mData = data;
        _context=context;
    }


    @Override
    public MetalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_metal_recyclerview, parent, false);
        MetalViewHolder holder = new MetalViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MetalViewHolder holder, int position) {
        MetalDto selected = mData.get(position);
        holder.setData(selected, position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    class MetalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mKod;
        TextView mAciklama;
        TextView mAlis;
        TextView mSatis;
        TextView mGunellenmeZamani;


        public MetalViewHolder(View itemView) {
            super(itemView);
            mKod = (TextView) itemView.findViewById(R.id.item_kod);
            mAciklama = (TextView) itemView.findViewById(R.id.item_aciklama);
            mAlis = (TextView) itemView.findViewById(R.id.item_alis);
            mSatis = (TextView) itemView.findViewById(R.id.item_satis);
            mGunellenmeZamani = (TextView) itemView.findViewById(R.id.item_guncellenme_zamani);
        }

        public void setData(MetalDto selectedProduct, int position) {
            this.mKod.setText(mData.get(position).Kod);
            this.mAciklama.setText(mData.get(position).Aciklama);
            this.mAlis.setText(mData.get(position).Alis.toString());
            this.mSatis.setText(mData.get(position).Satis.toString());

            SimpleDateFormat simpledateformat = new SimpleDateFormat("d.M.yyyy HH:mm:ss");
            this.mGunellenmeZamani.setText(DateFormat.format("d.M.yyyy HH:mm:ss", mData.get(position).GuncellenmeZamani).toString());
        }
        @Override
        public void onClick(View v) {
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TextView tvUrl = v.findViewById(R.id.photo_url);
                    //Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse( tvUrl.getText().toString()) );
                    //v.getContext().startActivity(browse);
                }
            });

        }

    }

    @Override
    public void onBindViewHolder(@NonNull MetalViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        holder.onClick(holder.itemView);
    }
}
