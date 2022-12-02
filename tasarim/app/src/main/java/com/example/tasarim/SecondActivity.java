package com.example.tasarim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tasarim.Models.anasayfaModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SecondActivity extends AppCompatActivity {

    TextView third_title, about_text, venue_type_text,third_rating_number2;
    Button yorum_button;
    RatingBar third_ratingbar;
    ImageView header_background;
    ImageView down_arrow;
    Animation from_bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    yorum_button=findViewById(R.id.yorum_button);
        third_rating_number2=findViewById(R.id.third_rating_number2);
        third_ratingbar=findViewById(R.id.third_ratingbar);
        third_title=findViewById(R.id.third_title);
        about_text=findViewById(R.id.about_text);
        venue_type_text=findViewById(R.id.venue_type_text);
        header_background=findViewById(R.id.header_background);

       // FirebaseAuth.getInstance().signOut();

        String postId = getIntent().getStringExtra("postId");





        down_arrow = findViewById(R.id.down_arrow);
        from_bottom = AnimationUtils.loadAnimation(this, R.anim.anim_from_bottom);
        down_arrow.setAnimation(from_bottom);
        down_arrow.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(down_arrow, "background_image_transition");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SecondActivity.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });

        yorum_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser() == null){

                    Intent intent=new Intent(SecondActivity.this,LoginActivity.class);

                    startActivity(intent);

                }
                else {
                    Intent intent=new Intent(SecondActivity.this,CommentActivity.class);
                    intent.putExtra("postId",postId);
                    startActivity(intent);

                }

            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("post").child(postId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                anasayfaModel a = new anasayfaModel();
                a = snapshot.getValue(anasayfaModel.class);

                Glide.with(SecondActivity.this).load(a.getResimUrl()).into(header_background);

                about_text.setText(a.getYazi().toString());
                venue_type_text.setText(a.getNedir().toString());
                third_ratingbar.setRating(a.getPuan());
                third_rating_number2.setText(a.getSehirId().toString());
                third_title.setText(a.getIsim().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}