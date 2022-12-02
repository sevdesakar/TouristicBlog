package com.example.tasarim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {

    ArrayAdapter<String> addressAdapter;
    Spinner sehirspinner;
    List<String> sehiridlist;

    EditText nedir, isim;
    TextView post, yazi, textView;
    ImageView image_added, close, ppresim;
    RatingBar ratingBar;
    Button btn_ekle;

    Uri imageUri;

    String myUrl = "";
    StorageTask uploadTask;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        nedir = findViewById(R.id.nedir);
        isim = findViewById(R.id.isim);
        textView = findViewById(R.id.textView);
        post = findViewById(R.id.post);
        image_added = findViewById(R.id.image_added);
        close = findViewById(R.id.close);
        ratingBar = findViewById(R.id.ratingBar);
        btn_ekle = findViewById(R.id.btn_ekle);
        ppresim = findViewById(R.id.ppresim);
        yazi = findViewById(R.id.yazi);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("kullanicilar").child(FirebaseAuth.getInstance().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                textView.setText(snapshot.child("adSoyad").getValue(String.class));
                Glide.with(ProfileActivity.this).load(snapshot.child("profilResmi").getValue(String.class)).into(ppresim);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Spinner için --------------------------------------
        sehirspinner = (Spinner) findViewById(R.id.sehirspinner);
        sehiridlist = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference fDatabaseRoot = database.getReference();

        final List<String> propertyAddressList = new ArrayList<String>();
        addressAdapter = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_dropdown_item_1line, propertyAddressList);
        sehirspinner.setAdapter(addressAdapter);
        fDatabaseRoot.child("sehir").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sehiridlist.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    propertyAddressList.add(snapshot.child("sehir").getValue(String.class));
                    sehiridlist.add(snapshot.child("sehirId").getValue(String.class));
                }

                addressAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        btn_ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isim.getText().toString().equals("")) {
                    if (!nedir.getText().toString().equals("")) {

                        if (!yazi.getText().toString().equals(""))
                        {
                            if (myUrl.length()>0)
                            {
                                ekle();
                            } else
                                Toast.makeText(ProfileActivity.this, "Resim Seçiniz", Toast.LENGTH_SHORT).show();


                        }
                    }
                } else
                    Toast.makeText(ProfileActivity.this, "Hiçbir alan boş geçilemez", Toast.LENGTH_SHORT).show();

            }
        });


        //foto için ----------
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });


    }

    private void ekle() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("post");
        String postId = reference.push().getKey();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("isim", isim.getText().toString());
        hashMap.put("resimUrl", myUrl);
        hashMap.put("postId", postId);
        hashMap.put("nedir", nedir.getText().toString());
        hashMap.put("puan", ratingBar.getNumStars());
        hashMap.put("yazi", yazi.getText().toString());
        hashMap.put("sehirId", sehiridlist.get((int) (sehirspinner.getSelectedItemPosition())));
        reference.child(postId).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Eklendi", Toast.LENGTH_LONG).show();
                    onBackPressed();
                } else
                    Toast.makeText(ProfileActivity.this, "Başarısız ", Toast.LENGTH_LONG).show();
            }
        });


    }

    // Select Image method
    private void SelectImage() {

        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }


    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri imageUri = data.getData();


            if (imageUri != null) {
                Toast.makeText(getApplicationContext(), "Yükleniyor", Toast.LENGTH_SHORT);
                final StorageReference filereferance = FirebaseStorage.getInstance().getReference("post").child(System.currentTimeMillis()
                        + "." + getFileExtension(imageUri));

                uploadTask = filereferance.putFile(imageUri);
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return filereferance.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            myUrl = downloadUri.toString();
                            Glide.with(ProfileActivity.this).load(myUrl).into(image_added);
                            //Toast.makeText(ProfileActivity.this,myUrl,Toast.LENGTH_LONG).show();

                            Toast.makeText(getApplicationContext(), "Yüklendi", Toast.LENGTH_SHORT);
                        } else {
                            Toast.makeText(getApplicationContext(), "Hata", Toast.LENGTH_SHORT);
                        }
                    }
                });

            } else {
                Toast.makeText(getApplicationContext(), "Fotoğraf Seçilmedi", Toast.LENGTH_SHORT);
            }


        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_category:
                    Intent intent1 = new Intent(ProfileActivity.this, CategoryActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.navigation_profile:
                    Intent intent2 = new Intent(ProfileActivity.this, ProfileActivity.class);
                    startActivity(intent2);
                    break;
            }
            return true;
        }
    };
}





