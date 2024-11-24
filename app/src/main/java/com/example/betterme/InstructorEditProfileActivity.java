package com.example.betterme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.betterme.Models.Instructor;
import com.example.betterme.Shared.Shared;
import com.example.betterme.Utils.AppUtils;
import com.example.betterme.Utils.ExifUtils;
import com.example.betterme.Utils.FileUtils;
import com.example.betterme.Utils.MyDialogFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class InstructorEditProfileActivity extends AppCompatActivity {
    Button change_img_btn, edit_profile_btn,back_btn ;
    ImageView profile_img;
    EditText change_name_et,change_phone_number_et, change_Category_et, change_age_et;
    private static final int REQUEST_CODE_GALLERY = 11;
    private static final int REQUEST_CODE_CAMERA = 12;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 10;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 13;
    String mCurrentPhotoPath;
    String userChosenTask;
    File imageFile ;
    Uri selectedImageUri;
    FirebaseStorage storage;
    StorageReference storageReference;
    String image_path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_edit_profile);
        change_img_btn = findViewById(R.id.change_img_btn);
        edit_profile_btn = findViewById(R.id.edit_profile_btn);
        profile_img = findViewById(R.id.profile_img);
        change_name_et = findViewById(R.id.change_name_et);
        change_phone_number_et = findViewById(R.id.change_phone_number_et);
        change_age_et = findViewById(R.id.change_age_et);
        change_Category_et = findViewById(R.id.change_Category_et);
        back_btn = findViewById(R.id.btn_back);

        change_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        edit_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageFile != null){
                    if (!change_name_et.getText().toString().isEmpty() && !change_age_et.getText().toString().isEmpty() &&
                    !change_phone_number_et.getText().toString().isEmpty() && !change_Category_et.getText().toString().isEmpty()){
                        uploadImage();
                    }else{
                        Toast.makeText(getApplicationContext(), "Please fill all fields",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    if (!change_name_et.getText().toString().isEmpty() && !change_age_et.getText().toString().isEmpty() &&
                            !change_phone_number_et.getText().toString().isEmpty() && !change_Category_et.getText().toString().isEmpty()){
                        image_path = Shared.getInstance().myInstructor.getImage();
                        editProfile();
                    }else{
                        Toast.makeText(getApplicationContext(), "Please fill all fields",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        updateView();
    }

    private void updateView(){
        change_name_et.setText(Shared.getInstance().myInstructor.getName());
        change_age_et.setText(Shared.getInstance().myInstructor.getAge());
        change_phone_number_et.setText(Shared.getInstance().myInstructor.getMobile());
        change_Category_et.setText(Shared.getInstance().myInstructor.getExperties());

        if (Shared.getInstance().myInstructor.getImage() != null){
            Glide.with(this).load(Shared.getInstance().myInstructor.getImage()).into(profile_img);
        }
    }

    private void editProfile(){
        Instructor instructor = new Instructor(change_name_et.getText().toString(),change_phone_number_et.getText().toString()
                ,Shared.getInstance().myInstructor.getEmail(),Shared.getInstance().myInstructor.getPassword(),
                Shared.getInstance().myInstructor.getCertificate(),
                change_age_et.getText().toString(),change_Category_et.getText().toString(),
                FirebaseAuth.getInstance().getCurrentUser().getUid() ,1, image_path);

        FirebaseDatabase.getInstance().getReference("Instructors")
                .child(FirebaseAuth.getInstance().getUid())
                .setValue(instructor).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    Intent intent = new Intent(InstructorEditProfileActivity.this, SplashActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    ActivityCompat.finishAffinity(InstructorEditProfileActivity.this);

                    Toast.makeText(InstructorEditProfileActivity.this, "Edit Successfully", Toast.LENGTH_SHORT).show();

                }else {

                    Toast.makeText(InstructorEditProfileActivity.this, "Edit not success", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void selectImage() {
        final CharSequence[] items = {getString(R.string.text_take_photo), getString(R.string.choose_form_library),
                getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = true;
                if (items[item].equals(getString(R.string.text_take_photo))) {
                    userChosenTask = getString(R.string.text_take_photo);
                    if (result) {
                        openCamera();
                    }
                } else if (items[item].equals(getString(R.string.choose_form_library))) {
                    userChosenTask = getString(R.string.choose_form_library);
                    if (result) {
                        openGallery();
                    }

                } else if (items[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void openGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            openGalleryIntent();
        }
    }

    private void openGalleryIntent() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQUEST_CODE_GALLERY);
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA);
        } else {
            dispatchTakePictureIntent();
        }
    }

    private void dispatchTakePictureIntent() {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Log.d("FileException", ex.toString());

                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(InstructorEditProfileActivity.this, "com.example.betterme.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_CODE_CAMERA);
                }
            }
        } catch (Exception e) {
            Log.d("Camera", e.toString());
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGalleryIntent();
            } else {
                MyDialogFragment dialog = new MyDialogFragment();
                dialog.init("Error",this.getString(R.string.error_allow_permission_first));
                dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
            }
        } else if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                MyDialogFragment dialog = new MyDialogFragment();
                dialog.init("Error", this.getString(R.string.error_allow_permission_first) );
                dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            if (mCurrentPhotoPath != null && !mCurrentPhotoPath.isEmpty()) {
                Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
                bitmap = AppUtils.getResizedBitmap(bitmap, 1000);
                bitmap = ExifUtils.rotateBitmap(mCurrentPhotoPath, bitmap);
                File file = AppUtils.convertBitmaptoFile(bitmap, getApplicationContext());
                imageFile = file;

                profile_img.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));

                selectedImageUri = getImageUri(this,bitmap);

            }
        } else if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
                selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                bitmap = AppUtils.getResizedBitmap(bitmap, 800);
                bitmap = ExifUtils.rotateBitmap(FileUtils.getPath( InstructorEditProfileActivity.this, selectedImageUri), bitmap);
                File file = AppUtils.convertBitmaptoFile(bitmap, getApplicationContext());
                imageFile = file;

                profile_img.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void uploadImage(){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.show();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("images/"+randomKey);

        riversRef.putFile(selectedImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Image Upload Failed", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercentage = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        progressDialog.setMessage("Percentage: "+ (int)progressPercentage);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                image_path = uri.toString();
                                Log.d("IMAGE URI SNAPSHOT", "IMAGE URI SNAPSHOT: " + image_path);
                                editProfile();
                            }
                        });
                    }
                });
    }

}