package com.example.adunik_krishi.screens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.adunik_krishi.R;
import com.example.adunik_krishi.constant.Constant;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class AddQuestionActivity extends AppCompatActivity {

    private EditText question_titleET,question_detailsET;
    private Button question_imageBTN,question_addBTN;
    private ImageView question_imageView,question_imageShow;
    private ProgressBar loadingBar;

    private Bitmap bitmapPicture = null;
    private String qTitle,qDetails,downloadImageURL = "";

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        initialize();

        getPermission();

        addQuestionImage();

        deleteQuestionImage();

        addQuestion();
    }

    private void getPermission() {
        String[] permissions = {Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(permissions,100);
        }
    }

    private void addQuestion() {

        question_addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                qTitle = question_titleET.getText().toString();
                qDetails = question_detailsET.getText().toString();

                if(qTitle.isEmpty()){
                    Toast.makeText(getApplicationContext(), "আপনার প্রশ্ন লিখুন", Toast.LENGTH_SHORT).show();
                }
                else if(qDetails.isEmpty()){
                    Toast.makeText(getApplicationContext(), "আপনার প্রস্ন এর বিস্তারিত লিখুন ", Toast.LENGTH_SHORT).show();
                }
                else if(bitmapPicture == null){
                    Toast.makeText(getApplicationContext(), "আপনার প্রশ্ন এর ছবি যুক্ত করুন ", Toast.LENGTH_SHORT).show();
                }
                else{

                    uploadImageAndQuestion();
                }
            }
        });

    }

    private void uploadImageAndQuestion() {

        loadingBar.setVisibility(View.VISIBLE);
        question_addBTN.setVisibility(View.INVISIBLE);

        StorageReference imageRef = storageReference.child("images" + UUID.randomUUID() + ".jpg");

        Uri imageUri = getImageUri(getApplicationContext(), bitmapPicture);

        final UploadTask uploadTask = imageRef.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                loadingBar.setVisibility(View.INVISIBLE);
                question_addBTN.setVisibility(View.VISIBLE);

                Toast.makeText(getApplicationContext(), "ছবি আপলোড করা যাচ্ছে না, আবার চেষ্টা করুন ।", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        downloadImageURL = imageRef.getDownloadUrl().toString();
                        return imageRef.getDownloadUrl();
                    }

                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){

                            downloadImageURL = task.getResult().toString();

                            String phoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            String phoneNumber2 = preferences.getString("phone", null);

                            if (!phoneNumber.isEmpty()) {
                                updateDatabase(phoneNumber.substring(3));
                            }
                            else if (!phoneNumber2.isEmpty()) {
                                updateDatabase(phoneNumber2);
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "ছবি সফলভাবে যুক্ত হয় নি ।", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });
    }

    private void updateDatabase(String phoneNumber) {

        String randomValue = UUID.randomUUID().toString();

        String currentDate = new SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(new Date());

        DatabaseReference questionRef = databaseReference.child(randomValue);

        HashMap<String,Object> productMap = new HashMap<>();

        productMap.put("qID",randomValue);
        productMap.put("qTitle",qTitle);
        productMap.put("qDetails",qDetails);
        productMap.put("qDate",currentDate);
        productMap.put("qPhone",phoneNumber);
        productMap.put("qImage",downloadImageURL);


        questionRef.setValue(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "আপনার প্রশ্ন সফলভাবে যুক্ত করা হয়েছে ।", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                else{
                    Toast.makeText(getApplicationContext(), "আপনার প্রশ্ন সফলভাবে যুক্ত করা যাচ্ছে না ।", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                loadingBar.setVisibility(View.INVISIBLE);
                question_addBTN.setVisibility(View.VISIBLE);

                Toast.makeText(getApplicationContext(), "আপনার প্রশ্ন যুক্ত করা যায় নি, আবার চেষ্টা করুন ।", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void addQuestionImage() {

        question_imageBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmapPicture = null;
                question_imageView.setImageResource(R.drawable.ic_baseline_image_24);
                question_imageShow.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void deleteQuestionImage() {

        question_imageBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddQuestionActivity.this);
                String[] items = {"ক্যামেরা","গ্যালারী"};
                builder.setTitle("যে কোন একটি সিলেক্ট করুন");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                openCamera();
                                break;
                            case 1:
                                openGallery();
                                break;
                        }
                    }
                });
                Dialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void openGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent,1);
    }

    private void openCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                Bundle bundle = data.getExtras();
                bitmapPicture = (Bitmap) bundle.get("data");
                question_imageView.setImageBitmap(bitmapPicture);
                question_imageShow.setVisibility(View.VISIBLE);
            } else if (requestCode == 1) {
                Uri uri = data.getData();
                try {
                    bitmapPicture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                } catch (IOException e) {
                    Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                question_imageView.setImageBitmap(bitmapPicture);
                question_imageShow.setVisibility(View.VISIBLE);
            }
        }

    }

    private void initialize() {

        question_titleET = findViewById(R.id.question_titleET);
        question_detailsET = findViewById(R.id.question_detailsTV);
        question_imageBTN = findViewById(R.id.question_imageBTN);
        question_addBTN = findViewById(R.id.question_addBTN);
        question_imageView = findViewById(R.id.question_imageView);
        question_imageShow = findViewById(R.id.question_imageShow);
        loadingBar = findViewById(R.id.loadingBar);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("Question Images");
        databaseReference = FirebaseDatabase.getInstance(Constant.DATABASE_REFERENCE).getReference("questions");
    }
}