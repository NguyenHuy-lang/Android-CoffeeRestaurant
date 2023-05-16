package dajava.pro.ushouldbuy;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.List;

import dajava.pro.ushouldbuy.adapter.PlaceOrderAdapter;
import dajava.pro.ushouldbuy.model.Item;
import dajava.pro.ushouldbuy.model.User;

public class CartActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static String KEY_TEST = "KEY_TEST";
    DatabaseReference reference = FirebaseDatabase.
            getInstance("https://amplified-coder-384315-default-rtdb.firebaseio.com/").
            getReference("my_shop");
    private DrawerLayout drawerlayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView phoneLabel, usernameLabel;
    private View header;
    private float delivery_charge;
    private RecyclerView cartItemsRecyclerView;
    private PlaceOrderAdapter placeYourOrderAdapter;
    private EditText inputAddress, City;
    private TextView tvSubtotalAmount, totalAmount, tvDeliveryChargeAmount, buttonOrder;
    private  Bundle bundle = new Bundle();
    private List<Item> itemsInCartList;
    private User user;
    private Spinner spinnerPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        drawerlayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        header = navigationView.getHeaderView(0);

        toolbar = findViewById(R.id.toolbar_cart);
        toolbar.setTitle("P COFFEE ");
        toolbar.setSubtitle("Học viện công nghệ bưu chính viễn thông");

        phoneLabel = header.findViewById(R.id.phonenumberLabel);
        usernameLabel = header.findViewById(R.id.usernameLabel);
        inputAddress = findViewById(R.id.inputAddress);
        City = findViewById(R.id.inputCity);
        tvSubtotalAmount = findViewById(R.id.tvSubtotalAmount);
        totalAmount = findViewById(R.id.tvTotalAmount);
        tvDeliveryChargeAmount = findViewById(R.id.tvDeliveryChargeAmount);
        spinnerPayment = findViewById(R.id.spinerPaymentmethod);
        spinnerPayment.setAdapter(new ArrayAdapter<String>(this, R.layout.item_paymentmethod,
                getResources().getStringArray(R.array.method)));

        //-------toolbar
        setSupportActionBar(toolbar);
        //---------Navigation drawer menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerlayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerlayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        user = (User) getIntent().getExtras().get("object_user");
        usernameLabel.setText(user.getFullname());
        phoneLabel.setText(user.getPhone());
        bundle=new Bundle();
        bundle.putSerializable("object_user",user);


        //---------main_menu(receiver_view)
        itemsInCartList = (List<Item>) getIntent().getSerializableExtra("itemInCart");
        cartItemsRecyclerView = findViewById(R.id.cartItemsRecyclerView);
        initRecyclerView(itemsInCartList);
        calculateTotalAmount(itemsInCartList);
        //upload bill to firebase
        buttonOrder = findViewById(R.id.buttonPlaceYourOrder);
        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlaceOrderButtonClick(itemsInCartList);
            }
        });


    }

    private void onPlaceOrderButtonClick(List<Item> itemList) {
        if (TextUtils.isEmpty(inputAddress.getText().toString())) {
            inputAddress.setError("Please enter address ");
            return;
        } else if (TextUtils.isEmpty(City.getText().toString())) {
            City.setError("Please enter city");
            return;
        }
        final String username = usernameLabel.getText().toString();
        final String phone = phoneLabel.getText().toString();
        final String address = inputAddress.getText().toString();
        final String city = City.getText().toString();
        final String total = totalAmount.getText().toString();
        final String paymentmethod = spinnerPayment.getSelectedItem().toString();
        reference.child("orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long count = snapshot.child(user.getPhone()).getChildrenCount();
                count++;
                reference.child("orders").child(phone).child(String.valueOf(count)).child("Phone").setValue(phone);
                reference.child("orders").child(phone).child(String.valueOf(count)).child("Name").setValue(username);
                reference.child("orders").child(phone).child(String.valueOf(count)).child("City").setValue(city);
                for (int i = 0; i < itemList.size(); i++) {
                    reference.child("orders").child(phone).child(String.valueOf(count)).child("drinks").
                            child(String.valueOf(i + 1)).child("name").setValue(itemList.get(i).getName());
                    reference.child("orders").child(phone).child(String.valueOf(count)).child("drinks").
                            child(String.valueOf(i + 1)).child("price").setValue(itemList.get(i).getPrice());
                    reference.child("orders").child(phone).child(String.valueOf(count)).child("drinks").
                            child(String.valueOf(i + 1)).child("quantity").setValue(itemList.get(i).getTotalInCart());
                    reference.child("orders").child(phone).child(String.valueOf(count)).child("drinks").
                            child(String.valueOf(i + 1)).child("image").setValue(itemList.get(i).getPicId());
                }
                reference.child("orders").child(phone).child(String.valueOf(count)).child("Payment method").setValue(paymentmethod);
                reference.child("orders").child(phone).child(String.valueOf(count)).child("Total Cost").setValue(total);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //start success activity..
//        if (Build.VERSION.SDK_INT >= 24) {
//            if (ContextCompat.checkSelfPermission(CartActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(CartActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
//            } else {
//                String newMessenger = "Dat hang thanh cong";
//                // Create the notification payload
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "default");
//                builder.setSmallIcon(R.drawable.ic_notification);
//                builder.setContentTitle(StaticConfig.NAME);
//                builder.setContentText(newMessenger);
//                builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
//                builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
//
//                // Create the notification channel (required for Android Oreo and above)
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    NotificationChannel channel = new NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT);
//                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
//                    notificationManager.createNotificationChannel(channel);
//                }
//
//                // Send the notification
//                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
//                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
//                notificationManager.notify(0, builder.build());
//
//
//            }
//        }
        Intent i = new Intent(CartActivity.this, ShopActivity.class);
        i.putExtras(bundle);
        startActivity(i);
    }

    private void initRecyclerView(List<Item> itemList) {
        cartItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        placeYourOrderAdapter = new PlaceOrderAdapter(itemList);
        cartItemsRecyclerView.setAdapter(placeYourOrderAdapter);
    }

    @Override
    public void onBackPressed() {
        if (drawerlayout.isDrawerOpen(GravityCompat.START)) {
            drawerlayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

            // push data to previous activity
//            Intent returnIntent = new Intent();
//            returnIntent.putExtra(KEY_TEST, new ModelTest("NameTest", "12"));

            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent intent1 = new Intent(getApplicationContext(), ShopActivity.class);
                intent1.putExtra("itemInCart", (Serializable) itemsInCartList);
                startActivity(intent1);
                break;
            case R.id.cart:
                break;
            case R.id.my_order:
                Intent intent2 = new Intent(getApplicationContext(), WelcomeActivity.class);
                intent2.putExtra("object_user", user);
                startActivity(intent2);
                break;
            case R.id.log_out:
                Intent intent3 = new Intent(CartActivity.this, LoginActivity.class);
                startActivity(intent3);
                break;

        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 1000) {
            setResult(Activity.RESULT_OK, data);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void calculateTotalAmount(List<Item> itemList) {
        float subTotalAmount = 0f;
        delivery_charge = 0f;

        for (Item m : itemList) {
            subTotalAmount += m.getPrice() * m.getTotalInCart();
        }

        tvSubtotalAmount.setText(String.format("%.2f", subTotalAmount) + "đ");

        if (subTotalAmount < 300) {
            delivery_charge = 20.00f;
            tvDeliveryChargeAmount.setText(String.format("%.2f", delivery_charge) + "$");
            subTotalAmount += delivery_charge;
        } else if (subTotalAmount < 500) {
            delivery_charge = 10.00f;
            tvDeliveryChargeAmount.setText(String.format("%.2f", delivery_charge) + "$");
            subTotalAmount += delivery_charge;
        } else {
            delivery_charge = 0f;
            tvDeliveryChargeAmount.setText(String.format("%.2f", delivery_charge) + "$");
            subTotalAmount += delivery_charge;
        }
        totalAmount.setText(String.format("%.2f", subTotalAmount) + "$");
    }
}