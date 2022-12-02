package com.example.tasarim.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tasarim.KategoriActivity;
import com.example.tasarim.Models.categoryModel;
import com.example.tasarim.R;
import java.util.List;

public class categoryAdapter extends RecyclerView.Adapter<categoryAdapter.ViewHolder> {

    private Context mContext;
    private List<categoryModel> mCategory;


    public categoryAdapter(Context mContext, List<categoryModel> mCategory) {
        this.mContext = mContext;
        this.mCategory = mCategory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.category_item, parent, false);
        return new categoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {



        holder.sehir.setText(mCategory.get(position).getSehir().toString());
        Glide.with(mContext).load(mCategory.get(position).getResimUrl()).into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(mContext, KategoriActivity.class);
                intent.putExtra("sehirId",mCategory.get(position).getSehirId());
                mContext.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return mCategory.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView sehir;
        private ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sehir  = itemView.findViewById(R.id.sehir);
            image = itemView.findViewById(R.id.view);
        }
    }
}
