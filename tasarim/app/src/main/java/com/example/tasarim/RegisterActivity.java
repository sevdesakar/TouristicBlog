package com.example.tasarim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;

import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity {
    EditText et_adsoyad, et_mail, et_sifre;
    ImageView image_added, close;
    TextView post;
    Button btn_kayitol;
    private FirebaseAuth mAuth;

    Uri imageUri;

    String myUrl = "";
    StorageTask uploadTask;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        close=findViewById(R.id.close);
        post=findViewById(R.id.post);
        et_adsoyad=findViewById(R.id.et_adsoyad);
        et_mail=findViewById(R.id.et_mail);
        et_sifre=findViewById(R.id.et_sifre);
        btn_kayitol=findViewById(R.id.btn_kayitol);
        image_added = findViewById(R.id.image_added);


        btn_kayitol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_sifre.getText().toString().length()!=6)
                {
                    // Toast.makeText(RegisterActivity.this, "Şifreniz maksimum 6 karakter olmalıdır", Toast.LENGTH_SHORT).show();

                    if (!et_adsoyad.getText().toString().equals(""))
                    {
                        // Toast.makeText(RegisterActivity.this, "Bütün Alanları doldurun.", Toast.LENGTH_SHORT).show();
                        if (!et_mail.getText().toString().equals("")) {
                            if (myUrl.length()>0){
                            register(et_mail.getText().toString(), et_sifre.getText().toString());
                            }
                            else Toast.makeText(RegisterActivity.this, "Resim seçiniz", Toast.LENGTH_SHORT).show();
                        }
                        else Toast.makeText(RegisterActivity.this, "Mail alanı boş geçilemez", Toast.LENGTH_SHORT).show();


                    }                else Toast.makeText(RegisterActivity.this, "Ad soyad alanı boş geçilemez", Toast.LENGTH_SHORT).show();

                } else Toast.makeText(RegisterActivity.this, "Şifreniz minimum 6 karakter olmalıdır", Toast.LENGTH_SHORT).show();


            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });




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
                final StorageReference filereferance = FirebaseStorage.getInstance().getReference("kullanicilar").child(System.currentTimeMillis()
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
                            Glide.with(RegisterActivity.this).load(myUrl).into(image_added);
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

     void register(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(RegisterActivity.this, "Kayıt Başarılı", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            DatabaseReference referance = FirebaseDatabase.getInstance().getReference("kullanicilar").child(user.getUid());


                            HashMap<String,Object> yeniKullanici = new HashMap<>();
                            yeniKullanici.put("adSoyad",et_adsoyad.getText().toString());
                            yeniKullanici.put("email",et_mail.getText().toString());
                            yeniKullanici.put("sifre",et_sifre.getText().toString());
                            yeniKullanici.put("kullaniciid",user.getUid());
                            yeniKullanici.put("profilResmi", myUrl);


                            referance.setValue(yeniKullanici).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterActivity.this, "Kayıt Başarılı", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                                        startActivity(i);

                                    }
                                    Toast.makeText(RegisterActivity.this, "Kayıt Başarısız", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {

                            Toast.makeText(RegisterActivity.this, "Kayıt Başarısız.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void onLoginClick(View view){
            startActivity(new Intent(this,LoginActivity.class));
    }
}