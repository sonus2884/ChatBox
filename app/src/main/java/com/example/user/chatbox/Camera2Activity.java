package com.example.user.chatbox;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Camera2Activity extends AppCompatActivity {

    private Button uploadButton;
    private ImageView imageView;
    private Uri mUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;
    private String uploadId;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("_WriteExternalStorage", "tue");
            }
            if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                takePhoto();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);

        uploadButton = findViewById(R.id.uploadButton);
        imageView = findViewById(R.id.image);


        mAuth = FirebaseAuth.getInstance();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("User details").child(mAuth.getUid());
        mStorageRef = FirebaseStorage.getInstance().getReference("User details").child(mAuth.getUid());

        permission();

    }

    private void permission() {

        if (Build.VERSION.SDK_INT > 23) {

            if (((checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) &&
                    (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {

                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
            } else {

                takePhoto();
            }

        } else {
            takePhoto();
        }
    }

    public void takePhoto() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 7);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 7 && resultCode == RESULT_OK && data != null) {

            mUri = data.getData();
            imageView.setImageURI(mUri);
        }
    }

    private String getFileExtension(Uri uri) {

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return (mime.getExtensionFromMimeType(cr.getType(uri)));
    }


    public void upload(View view) {

        uploadImage();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }


    private void uploadImage() {

        if (mUri != null) {

            StorageReference fileStorageRef = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mUri));

            fileStorageRef.putFile(mUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(Camera2Activity.this, "Upload Successfully", Toast.LENGTH_SHORT).show();

                            UserDetail userDetail = new UserDetail();
                            String imageUri = taskSnapshot.getDownloadUrl().toString();
                            userDetail.setImageUri(imageUri);
                            userDetail.setName(MainActivity.name);
                            userDetail.setAbout("");
                            // String uploadId = mDatabaseRef.getKey();

                             mDatabaseRef.setValue(userDetail);
                             //Log.i("_upId",uploadId);
                            // Log.i("_uri",friendsName.toString());

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(Camera2Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

        } else {

            Toast.makeText(getApplicationContext(), "Please Choose a photo", Toast.LENGTH_SHORT).show();
        }
    }
}
