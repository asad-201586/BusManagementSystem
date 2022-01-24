package com.example.newapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private Button profileEmailVerifyButton;
    private BottomSheetDialog sheetDialog;
    private FirebaseAuth fAuth;
    DocumentReference documentReference;
    FirebaseUser user;

    private ImageView profileImage;
    private StorageReference storageReference, profileStorageRef;
    private FirebaseFirestore firebaseFirestore;
    private TextView profileName, proEditableName, proEditablePhnNum, proEditableEmail, proEditableDepartment, checkIsEmailVerified, editProfileTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        this.setTitle("User Profile");

        // Change Action Bar Color
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5DAFF1")));

        // For Back Button
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowHomeEnabled(true);

        // Change Status Bar Color
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusBarColor));
        }

        profileEmailVerifyButton = (Button) findViewById(R.id.verifiedEmailButtonId);
        editProfileTextView = (TextView) findViewById(R.id.editProfileTextViewId);
        profileImage = (ImageView) findViewById(R.id.editableProfileImageViewId);
        profileName = (TextView) findViewById(R.id.profileNameId);
        proEditableName = (TextView) findViewById(R.id.editableProfileNameId);
        proEditablePhnNum = (TextView) findViewById(R.id.editableProfilePhnNumId);
        proEditableDepartment = (TextView) findViewById(R.id.editableDepartmentNameId);
        proEditableEmail = (TextView) findViewById(R.id.editableProfileEmailId);
        checkIsEmailVerified = (TextView) findViewById(R.id.checkIsEmailVerifiedId);

        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        documentReference = firebaseFirestore.collection("users").document(user.getUid());

        // For Email Verification
        if (user != null)
            user.reload();

        if (!user.isEmailVerified()){
            Log.d("profile_", "onCreate: email is not verified");
            profileEmailVerifyButton.setVisibility(View.VISIBLE);
            checkIsEmailVerified.setText("Email Unverified");

        }else{
            checkIsEmailVerified.setText("Email Verified");
            Log.d("profile_", "onCreate: email is verified");

            // Store Email Verification Status as Verified
            Map<String, Object> edited = new HashMap<>();
            edited.put("emailVerification", "Verified");
            documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        }

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                profileName.setText(value.getString("name"));
                proEditableName.setText(value.getString("name"));
                proEditableEmail.setText(value.getString("email"));
                proEditablePhnNum.setText(value.getString("phone"));
                proEditableDepartment.setText(value.getString("department"));

            }
        });

        storageReference = FirebaseStorage.getInstance().getReference();
        profileStorageRef = storageReference.child("Users/"+user.getUid()+"/profile.jpg");

        profileStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

        profileEmailVerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(ProfileActivity.this, "Email verification link has been sent.\nPlease check your email", Toast.LENGTH_LONG).show();

                        // buttonEmailVerify.setVisibility(View.GONE);
                        // textViewEmailVerify.setVisibility(View.GONE);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d("tag", "onFailure : Email not sent " + e.getMessage());

                    }
                });

            }
        });

        // For Edit Profile
        editProfileTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sheetDialog = new BottomSheetDialog(ProfileActivity.this,R.style.BottomSheetStyle);
                View view = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.bottomsheet_dialog,
                        (LinearLayout)findViewById(R.id.sheetLayoutId));

                EditText profileNameBottomSheet, profilePhnNumberBottomSheet, profileEmailBottomSheet, profileDepartmentBottomSheet;
                profileNameBottomSheet = (EditText) view.findViewById(R.id.editNameId);
                profileEmailBottomSheet = (EditText) view.findViewById(R.id.editEmailId);
                profilePhnNumberBottomSheet =(EditText) view.findViewById(R.id.editPhnNumId);
                profileDepartmentBottomSheet = (EditText) view.findViewById(R.id.editDepartmentNameId);

                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                        profileNameBottomSheet.setText(value.getString("name"));
                        profileEmailBottomSheet.setText(value.getString("email"));
                        profilePhnNumberBottomSheet.setText(value.getString("phone"));
                        profileDepartmentBottomSheet.setText(value.getString("department"));

                    }
                });

                Button updateProfileData = (Button) view.findViewById(R.id.updateButtonId);
                updateProfileData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (profileNameBottomSheet.getText().toString().isEmpty() || profilePhnNumberBottomSheet.getText().toString().isEmpty() || profileEmailBottomSheet.getText().toString().isEmpty() || profileDepartmentBottomSheet.getText().toString().isEmpty()){
                            Toast.makeText(ProfileActivity.this, "One or Many Fields are Empty", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String email = profileEmailBottomSheet.getText().toString();
                        user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Map<String, Object> edited = new HashMap<>();
                                edited.put("email", email);
                                edited.put("name", profileNameBottomSheet.getText().toString());
                                edited.put("phone", profilePhnNumberBottomSheet.getText().toString());
                                edited.put("department", profileDepartmentBottomSheet.getText().toString());
                                documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(ProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                                        finish();
                                    }
                                });
                                //Toast.makeText(Profile.this, "Email is Changed", Toast.LENGTH_LONG).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                });
                sheetDialog.setContentView(view);
                sheetDialog.show();
            }
        });

    }

    // Update Profile Picture
    public void fabChangeProPic(View view) {

        ImagePicker.with(ProfileActivity.this)
                //.cameraOnly()	          User can only capture image using Camera
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start(1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000){
            if (resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                //profileImage.setImageURI(imageUri);
                uploadProfileImageToFirebase(imageUri);
            }
        }
    }

    private void uploadProfileImageToFirebase(Uri imageUri) {
        // upload image to firebase storage
        final StorageReference fileRef = storageReference.child("Users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImage);

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        String profileImageUri = imageUri.toString();
        Map<String, Object> edited = new HashMap<>();
        edited.put("profileImg", profileImageUri);

        documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
}