package dajava.pro.ushouldbuy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dajava.pro.ushouldbuy.model.User;

public class LoginActivity extends AppCompatActivity {

    EditText phoneNumber,password1;
    DatabaseReference reference= FirebaseDatabase.
            getInstance("https://amplified-coder-384315-default-rtdb.firebaseio.com/").getReference("my_shop");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phoneNumber = findViewById(R.id.ePhonelogin);
        password1 = findViewById(R.id.ePasswordlogin);
    }
    public void signIn(View view){
        String phone = phoneNumber.getText().toString();
        String pass1 = password1.getText().toString();
        if (phone.isEmpty() || pass1.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please fill all these field", Toast.LENGTH_SHORT).show();

        } else {
            reference.child("users_info").addListenerForSingleValueEvent(new ValueEventListener(){
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(phone)){
                        final String getPhone=snapshot.child(phone).child("phone").getValue(String.class);
                        final String getPassword=snapshot.child(phone).child("password").getValue(String.class);
                        final String getName = snapshot.child(phone).child("fullname").getValue(String.class);
                        final String getEmail = snapshot.child(phone).child("email").getValue(String.class);
                        final String getImage = snapshot.child(phone).child("password").getValue(String.class);
                        if(getPassword.equals(pass1)){
                            User us=new User(getEmail,getPhone,getName);
                            us.setPassword(pass1);
                            us.setImage(getImage);
                            Toast.makeText(LoginActivity.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("object_user", us);
                            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(LoginActivity.this,"Sai mật khẩu",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(LoginActivity.this,"Không tồn tại số điện thoại này",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    public void signUp(View view){
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
    }

}