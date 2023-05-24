package com.example.mycoffee;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mycoffee.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth auth;
    User user;
    EditText name, email, password,confirmpass,phonenum;
    DatabaseReference reference= FirebaseDatabase.getInstance("https://ecomdb-273f5-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("u_should_buy_db");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();
        name = findViewById(R.id.eName);
        email = findViewById(R.id.eEmail);
        phonenum = findViewById(R.id.ePhone);
        password = findViewById(R.id.ePassword);
        confirmpass = findViewById(R.id.eConfirmPassword);
    }
    public void signup(View view){
        String userName = name.getText().toString();
        String mail = email.getText().toString();
        String pass = password.getText().toString();
        String phone = phonenum.getText().toString();
        String confirmpassword = confirmpass.getText().toString();

        if (TextUtils.isEmpty(userName)){
            Toast.makeText(this,"Please enter name:",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mail)){
            Toast.makeText(this,"Email is empty or not valid",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(phone)){
            Toast.makeText(this,"Phone is empty or not valid",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Please enter password:",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Pattern.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?\\d)(?=.*?[#?!@$%^&*-]).{8,}$",pass) ){
            Toast.makeText(this,"Password must have minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(confirmpassword)){
            Toast.makeText(this,"Please confirm password:",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!pass.equals(confirmpassword)){
            Toast.makeText(this,"Confirm Password does not match",Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(mail,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveUserToFirebase(userName,mail,phone);
                            Toast.makeText(RegisterActivity.this,"Registered Successfully.",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                        } else {
                            Toast.makeText(RegisterActivity.this, "Register failed. Please try again later.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
//        reference.child("users_info").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.hasChild(phone)){
//                    Toast.makeText(RegisterActivity.this,"Phonenumber is already registered",Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    reference.child("users_info").child(phone).child("fullname").setValue(userName);
//                    reference.child("users_info").child(phone).child("email").setValue(mail);
//                    reference.child("users_info").child(phone).child("password").setValue(pass);
//                    reference.child("users_info").child(phone).child("phone").setValue(phone);
//                    Toast.makeText(RegisterActivity.this,"Registered Successfully.",Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }
    public void signin(View view){
        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
    }
    private void saveUserToFirebase(String username, String email, String phonen) {
        DatabaseReference usersRef = reference.child("users_info");
        String userId = auth.getCurrentUser().getUid();

        user = new User(userId, email, phonen, username);
        usersRef.child(userId).setValue(user);

    }
}