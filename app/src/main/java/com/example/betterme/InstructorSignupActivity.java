package com.example.betterme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.Transliterator;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.betterme.Models.Instructor;
import com.example.betterme.Models.User;
import com.example.betterme.Utils.AppUtils;
import com.example.betterme.Utils.ExifUtils;
import com.example.betterme.Utils.FileUtils;
import com.example.betterme.Utils.MyDialogFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
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

public class InstructorSignupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] items = {"Gym","Footbal","Badminton","Cricket","Padel","Swimming","Maths","Programming","English","Stats","Physics","Primary Tuition",};

    private EditText mName, mMobile, mEmail, mPassword, mCertificate, mAge;
    private Spinner spinner;
    Button create_account,back_btn, upload_img_btn;
    ImageView profile_img;
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
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private static final String INSRUCTOR = "instructor";
    private Instructor instructor;
    ProgressDialog progressDialog;

    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    String image_path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_signup);

        spinner = findViewById(R.id.dropdown_experties);
        profile_img = findViewById(R.id.profile_img);
        back_btn = findViewById(R.id.btn_back);
        create_account = findViewById(R.id.instructor_signup_create_btn);
        upload_img_btn = findViewById(R.id.upload_img_btn);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        spinner.setOnItemSelectedListener(this);



        mName = findViewById(R.id.signup_name_et);
        mMobile = findViewById(R.id.signup_mobile_et);
        mEmail = findViewById(R.id.signup_email_et);
        mPassword = findViewById(R.id.signup_password_et);
        mCertificate = findViewById(R.id.certificate_name);
        mAge = findViewById(R.id.instructor_age);


        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();

        onclicks();
        oncreateaccount();

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String choice  = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(getApplicationContext(),choice, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


    }

    private void createUser() {
        progressDialog = new ProgressDialog(InstructorSignupActivity.this);
        progressDialog.setMessage("loading");
        progressDialog.show();

        String name = mName.getText().toString();
        String mobile = mMobile.getText().toString();
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        String certificate = mCertificate.getText().toString();
        String age = mAge.getText().toString();
        String expertise = spinner.getSelectedItem().toString();


        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            if (!password.isEmpty() && !name.isEmpty() && !mobile.isEmpty() && !certificate.isEmpty()
            && !age.isEmpty() && !expertise.isEmpty() && !image_path.isEmpty()){
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    instructor = new Instructor(name,mobile,email,password,certificate,age,expertise,FirebaseAuth.getInstance().getCurrentUser().getUid() ,1, image_path);
                                    FirebaseDatabase.getInstance().getReference("Instructors")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(instructor).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                progressDialog.hide();

                                                Toast.makeText(InstructorSignupActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(InstructorSignupActivity.this, BookingListActivity.class));
                                                finish();
                                            }else {
                                                progressDialog.hide();

                                                Toast.makeText(InstructorSignupActivity.this, "Registered not success", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }else {
                                    progressDialog.hide();

                                    Toast.makeText(InstructorSignupActivity.this, "Task 1 failed Successfully", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(InstructorSignupActivity.this, "Registration Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                mPassword.setError("Empty Fields are not Allowed");
            }
        }else if (email.isEmpty()){
            mEmail.setError("Empty Fields are not Allowed");
        }else{
            mEmail.setError("Please Enter Correct Email");
        }
    }

    private void onclicks() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        upload_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

    }

    private void oncreateaccount() {
    create_account.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            Intent intent = new Intent(InstructorSignupActivity.this, HomeActivity.class);
//            startActivity(intent);
            if (imageFile != null){
                String expertise = spinner.getSelectedItem().toString();

                if (!mEmail.getText().toString().isEmpty() && Patterns.EMAIL_ADDRESS.matcher(mEmail.getText().toString()).matches()) {
                    if (!mPassword.getText().toString().isEmpty() && !mName.getText().toString().isEmpty() &&
                            !mMobile.getText().toString().isEmpty() && !mCertificate.getText().toString().isEmpty()
                            && !mAge.getText().toString().isEmpty() && !expertise.isEmpty()) {
                        uploadImage();
                    }else {
                        Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(getApplicationContext(), "Please select image", Toast.LENGTH_LONG).show();
            }
//            createUser();
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
                    Uri photoURI = FileProvider.getUriForFile(InstructorSignupActivity.this, "com.example.betterme.fileprovider",
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
                bitmap = ExifUtils.rotateBitmap(FileUtils.getPath( InstructorSignupActivity.this, selectedImageUri), bitmap);
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
                                createUser();
                            }
                        });
                    }
                });
    }
}
