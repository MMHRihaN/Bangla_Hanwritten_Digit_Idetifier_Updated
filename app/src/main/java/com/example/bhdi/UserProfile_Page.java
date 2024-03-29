package com.example.bhdi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;

import com.example.bhdi.databinding.ActivityUserProfilePageBinding;
import com.example.bhdi.databinding.ActivityUserProfilePageBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile_Page extends AppCompatActivity {

    ActivityUserProfilePageBinding binding;
    TextView name,profession,email,about,birthday,phone, country , gender , UserName , Division , District ;
    SwipeRefreshLayout swipeRefreshLayout ;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userId;
    TextView age , Upload ;
    CircleImageView img;

    ActivityResultLauncher<String> launcher;
    FirebaseDatabase database;
    FirebaseStorage storage;
    CardView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_page);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(UserProfile_Page.this, Login_Page.class));
        }
        binding = ActivityUserProfilePageBinding.inflate(getLayoutInflater());
        name=findViewById(R.id.name);
        email= findViewById(R.id.email);
        img = findViewById(R.id.image);
        birthday = findViewById(R.id.birthday);
        phone = findViewById(R.id.phone);
      //  logout = findViewById(R.id.logout);
        gender = findViewById(R.id.gender) ;
        Upload = findViewById(R.id.upload) ;
        UserName = findViewById(R.id.userName) ;
        swipeRefreshLayout = findViewById(R.id.reload) ;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        Division = findViewById(R.id.division) ;
        District = findViewById(R.id.district) ;

        StorageReference dc = storage.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid());
        // Refresh the Whole page .-------------------------------------------------------------------------------------------------------------
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Load Image from storage in ImageView
                dc.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Glide               //
                                .with(UserProfile_Page.this)
                                .load(uri) // the uri you got from Firebase
                                .circleCrop()
                                .override(600,600)
                                .into(img); //Your imageView variable
                      //  Toast.makeText(UserProfile_Page.this, "Success", Toast.LENGTH_SHORT).show();
                        String userid = firebaseAuth.getCurrentUser().getUid() ;
                        DocumentReference documentReference = firebaseFirestore.collection("users").document(userid) ;

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserProfile_Page.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                swipeRefreshLayout.setRefreshing(false);
            }
        });



        // --------------------------------------------------------------------------------------------------------------------------------------

        // set user wallpaper to the CircleImageView ----------------------------------------------------------------------------------------------
        dc.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Glide.with(UserProfile_Page.this)
                        .load(uri) // the uri you got from Firebase
                        .circleCrop()
                        .override(600,600)
                        .into(img); //Your imageView variable
               // Toast.makeText(UserProfile_Page.this, "Success", Toast.LENGTH_SHORT).show();
                String userid = firebaseAuth.getCurrentUser().getUid() ;
                DocumentReference documentReference = firebaseFirestore.collection("users").document(userid) ;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserProfile_Page.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // --------------------------------------------------------------------------------------------------------------------------------------


        launcher = registerForActivityResult(new ActivityResultContracts.GetContent()
                , new ActivityResultCallback<Uri>() {

                    @Override
                    public void onActivityResult(Uri result) {
                        binding.image.setImageURI(result);

                        StorageReference reference = storage.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid());

                        reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        //  Toast.makeText(ProfileActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri result) {

                                                        database.getReference().child("users").child("Image").child(firebaseAuth.getCurrentUser().getUid())
                                                                .setValue(result.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        Toast.makeText(UserProfile_Page.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(UserProfile_Page.this, "not uploaded", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(UserProfile_Page.this, "failed to upload", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(UserProfile_Page.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                });

        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.launch("image/*");
            }
        });

        // Loading Other information from firebase firestore .


        DocumentReference documentReference = firebaseFirestore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                name.setText(value.getString("fname"));
                email.setText("EMAIL  :  " + value.getString("email"));
                phone.setText("PHONE  :  " + value.getString("phone"));
                birthday.setText("Date of Birth  :  " + value.getString("dateOfBirth"));
                gender.setText("Gender  :  " + value.getString("gender"));
                UserName.setText("Username  :  " + value.getString("username"));
                Division.setText("Division  :  " + value.getString("division"));
                District.setText("District  :  " + value.getString("district"));
            }
        });

        // for User Logout
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.remove("isLoggedin");
//                editor.commit();
//                //Toast.makeText(UserProfile_Page.this, "Logged out", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(UserProfile_Page.this, Login_Page.class));
//                finish();
//            }
//        });

    }
}


////Egula login er xml page theke remove kore nisi... line 181 to 214
//<androidx.cardview.widget.CardView
//        android:layout_height="110dp"
//        android:id="@+id/logout"
//        android:layout_width="150dp"
//        app:cardCornerRadius="15dp"
//        app:cardElevation="12dp"
//        android:layout_gravity="center"
//        android:layout_row="0"
//        android:layout_column="0"
//        >
//
//<LinearLayout
//                    android:layout_width="match_parent"
//                            android:layout_height="match_parent"
//                            android:id="@+id/gotoMap"
//
//                            android:layout_marginTop="2dp"
//
//                            android:orientation="vertical"  >
//
//
//<TextView
//                        android:layout_width="wrap_content"
//                                android:layout_height="wrap_content"
//                                android:text="Logout"
//                                android:layout_gravity="center_horizontal"
//                                android:textSize="30sp"
//                                android:textStyle="bold"
//                                android:textColor="@color/black"
//                                />
//</LinearLayout>
//
//</androidx.cardview.widget.CardView>

