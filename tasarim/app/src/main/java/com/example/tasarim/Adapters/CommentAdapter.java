package com.example.tasarim.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tasarim.CommentActivity;
import com.example.tasarim.Models.anasayfaModel;
import com.example.tasarim.Models.yorum_model;
import com.example.tasarim.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context mContext;
    private List<yorum_model> mList;

    private String kAdi;


   //BURAYAB İ BAKK

    public CommentAdapter(CommentActivity mContext, List<yorum_model> mList) {
        this.mContext = mContext;
        this.mList = mList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.yorum_item, parent, false);
        return new CommentAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tw_yorum;
        TextView tw_kullaniciAdi;

        public ViewHolder(View itemView) {
            super(itemView);
            tw_kullaniciAdi = itemView.findViewById(R.id.tw_kullaniciAdi);
            tw_yorum = itemView.findViewById(R.id.tw_yorum);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
      // firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
       yorum_model tw_yorum =mList.get(position);
        holder.tw_yorum.setText(tw_yorum.getYorum());
        kullaniciAdiAl(holder.tw_kullaniciAdi,tw_yorum.getGonderen());
        holder.tw_yorum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("gonderen",tw_yorum.getGonderen());
                mContext.startActivity(intent);
            }
        });

    }

    public void kullaniciAdiAl(TextView tw, String gonderen)
    {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("kullanicilar").child(gonderen);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tw.setText(snapshot.child("adSoyad").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    @Override
    public int getItemCount() {
         return mList.size();
    }




}
