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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class AddProductActivity extends AppCompatActivity {

    private EditText product_nameET,product_quantityET,product_detailsET,product_amountET;
    private ImageView product_imageView,product_imageShow;
    private Button product_imageBTN,product_addBTN;
    private ProgressBar loadingBar;

    private Bitmap bitmapPicture = null;
    private String pName,pQuantity,pDetails,pAmount,downloadImageURL = "";

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        initialize();

        getPermission();

        addProductImage();

        deleteProductImage();

        addProduct();
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

    private void addProduct() {

        product_addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pName = product_nameET.getText().toString();
                pDetails = product_detailsET.getText().toString();
                pQuantity = product_quantityET.getText().toString();
                pAmount = product_amountET.getText().toString();

                if(pName.isEmpty()){
                    Toast.makeText(getApplicationContext(), "আপনার Product এর নাম দিন", Toast.LENGTH_SHORT).show();
                }
                else if(pDetails.isEmpty()){
                    Toast.makeText(getApplicationContext(), "আপনার Product এর বিস্তারিত লিখুন ", Toast.LENGTH_SHORT).show();
                }
                else if(pQuantity.isEmpty()){
                    Toast.makeText(getApplicationContext(), "আপনার Product এর পরিমাণ লিখুন ", Toast.LENGTH_SHORT).show();
                }
                else if(pAmount.isEmpty()){
                    Toast.makeText(getApplicationContext(), "আপনার Product এর দাম লিখুন ", Toast.LENGTH_SHORT).show();
                }
                else if(bitmapPicture == null){
                    Toast.makeText(getApplicationContext(), "আপনার Product এর ছবি যুক্ত করুন ", Toast.LENGTH_SHORT).show();
                }
                else{

                    uploadImageAndProduct();
                }
            }
        });

    }

    private void uploadImageAndProduct() {

        loadingBar.setVisibility(View.VISIBLE);
        product_addBTN.setVisibility(View.INVISIBLE);

        StorageReference imageRef = storageReference.child("images" + UUID.randomUUID() + ".jpg");

        Uri imageUri = getImageUri(getApplicationContext(), bitmapPicture);

        final UploadTask uploadTask = imageRef.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                loadingBar.setVisibility(View.INVISIBLE);
                product_addBTN.setVisibility(View.VISIBLE);

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
                    }
                });
            }

        });
    }

    private void updateDatabase(String phoneNumber) {

        String randomValue = UUID.randomUUID().toString();

        DatabaseReference productRef = databaseReference.child(randomValue);

        HashMap<String,Object> productMap = new HashMap<>();
        productMap.put("pID",randomValue);
        productMap.put("pName",pName);
        productMap.put("pDetails",pDetails);
        productMap.put("pQuantity",pQuantity);
        productMap.put("pAmount",pAmount);
        productMap.put("pPhone",phoneNumber);
        productMap.put("pImage",downloadImageURL);


        productRef.setValue(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "আপনার Product সফলভাবে যুক্ত করা হয়েছে ।", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                loadingBar.setVisibility(View.INVISIBLE);
                product_addBTN.setVisibility(View.VISIBLE);

                Toast.makeText(getApplicationContext(), "আপনার Product যুক্ত করা যায় নি আবার চেষ্টা করুন ।", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void deleteProductImage() {

        product_imageShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmapPicture = null;
                product_imageView.setImageResource(R.drawable.ic_baseline_image_24);
                product_imageShow.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void addProductImage() {

        product_imageBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddProductActivity.this);
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

        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                product_imageView.setImageBitmap(bitmapPicture);
                product_imageShow.setVisibility(View.VISIBLE);
            } else if (requestCode == 1) {
                Uri uri = data.getData();
                try {
                    bitmapPicture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                } catch (IOException e) {
                    Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                product_imageView.setImageBitmap(bitmapPicture);
                product_imageShow.setVisibility(View.VISIBLE);
            }
        }

    }

    private void initialize() {
        product_nameET = findViewById(R.id.product_nameET);
        product_quantityET = findViewById(R.id.product_quantityTV);
        product_detailsET = findViewById(R.id.product_detailsTV);
        product_amountET = findViewById(R.id.product_amountTV);
        product_imageView = findViewById(R.id.product_imageView);
        product_imageShow = findViewById(R.id.product_imageShow);
        product_imageBTN = findViewById(R.id.product_imageBTN);
        product_addBTN = findViewById(R.id.product_addBTN);
        loadingBar = findViewById(R.id.loadingBar);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("Product Images");
        databaseReference = FirebaseDatabase.getInstance(Constant.DATABASE_REFERENCE).getReference("products");
    }
}