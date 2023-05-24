package dajava.pro.pcoffee.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import dajava.pro.pcoffee.R;
import dajava.pro.pcoffee.model.User;

public class FragmentUpdateInforUser extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView selectedImage;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private String urlAvatar;
    Button uploadButton;
    Button saveChange;
    EditText editFullName;
    EditText editEmail;
    EditText editPassword;
    private DatabaseReference reference= FirebaseDatabase.
            getInstance("https://amplified-coder-384315-default-rtdb.firebaseio.com/").
            getReference("my_shop");


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_infor, container, false);

        User user = (User) getArguments().get("object_user");

        uploadButton = view.findViewById(R.id.upload_button);
        saveChange = view.findViewById(R.id.save_button);
        selectedImage = view.findViewById(R.id.avatar_image_view);
        editFullName = view.findViewById(R.id.fullname_edit_text);
        editEmail = view.findViewById(R.id.email_edit_text);
        editPassword = view.findViewById(R.id.password_edit_text);

        editFullName.setText(user.getFullname());
        editEmail.setText(user.getEmail());
        editPassword.setText(user.getPassword());



        // Initialize Firebase Storage

        storage = FirebaseStorage.getInstance("gs://amplified-coder-384315.appspot.com");
        storageReference = storage.getReference();

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        saveChange.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(!editFullName.getText().toString().equals(user.getFullname())) {
                    reference.addListenerForSingleValueEvent(new ValueEventListener(){
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            reference.child("users_info").
                                    child(user.getPhone()).
                                    child("fullname").
                                    setValue(editFullName.getText().toString());
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                if(!editEmail.getText().toString().equals(user.getEmail())) {
                    reference.addListenerForSingleValueEvent(new ValueEventListener(){
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            reference.child("users_info").
                                    child(user.getPhone()).
                                    child("email").
                                    setValue(editEmail.getText().toString());
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                if (urlAvatar != null) {
                    reference.addListenerForSingleValueEvent(new ValueEventListener(){
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            reference.child("users_info").
                                    child(user.getPhone()).
                                    child("image").
                                    setValue(urlAvatar);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                if(!editPassword.getText().toString().equals(user.getPassword())) {
                    reference.addListenerForSingleValueEvent(new ValueEventListener(){
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            reference.child("users_info").
                                    child(user.getPhone()).
                                    child("password").
                                    setValue(editPassword.getText().toString());
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                Toast.makeText(getContext(),"Update information personal success",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            selectedImage.setImageURI(imageUri);
            User user = (User) getArguments().get("object_user");

            // Get the filename and path of the selected image
            String filename = "avatar";
            String path = "users/" + "/" + filename + "_" + user.getPhone();
            // Upload the selected image to Firebase Storage
            StorageReference imageRef = storageReference.child(path);
            UploadTask uploadTask = imageRef.putFile(imageUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Image uploaded successfully
                    // Get the download URL of the uploaded image
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString();
                            urlAvatar = imageUrl;
                            Log.d("FragmentAds", "Image URL: " + imageUrl);

                            // You can now update the user's avatar URL in your Firebase Realtime Database or Firestore database
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Image upload failed
                    Log.e("FragmentAds", "Error uploading image: " + e.getMessage());
                }
            });
        }
    }
}
