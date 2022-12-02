package com.example.tasarim.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tasarim.Models.anasayfaModel;
import com.example.tasarim.R;
import com.example.tasarim.SecondActivity;

import java.util.List;

public class anasayfaAdapter extends RecyclerView.Adapter<anasayfaAdapter.ViewHolder> {

    private Context mContext;
    private List<anasayfaModel> mAnasayfa;

    public anasayfaAdapter(Context mContext, List<anasayfaModel> mAnasayfa) {
        this.mContext = mContext;
        this.mAnasayfa = mAnasayfa;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.anasayfa_item, parent, false);
        return new anasayfaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull anasayfaAdapter.ViewHolder holder, int position) {
        holder.isim.setText(mAnasayfa.get(position).getIsim().toString());
        holder.sehir.setText(mAnasayfa.get(position).getSehirId().toString());
        holder.ratingBar.setRating(mAnasayfa.get(position).getPuan());
        Glide.with(mContext).load(mAnasayfa.get(position).getResimUrl()).into(holder.image);


        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(mContext, SecondActivity.class);
                intent.putExtra("postId",mAnasayfa.get(position).getPostId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAnasayfa.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView isim,sehir;
        private RatingBar ratingBar;
        private ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            isim = itemView.findViewById(R.id.isim);
            sehir = itemView.findViewById(R.id.sehir);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            image = itemView.findViewById(R.id.view);
        }
    }
}
