package dajava.pro.pcoffee;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dajava.pro.pcoffee.adapter.PlaceOrderAdapter;
import dajava.pro.pcoffee.model.Item;
import dajava.pro.pcoffee.model.User;
import de.hdodenhof.circleimageview.CircleImageView;

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
    private EditText inputAddress;
    private TextView tvSubtotalAmount, totalAmount, tvDeliveryChargeAmount, buttonOrder;
    private  Bundle bundle = new Bundle();
    private List<Item> itemsInCartList;
    private User user;
    private Spinner spinnerPayment;
    private CircleImageView imageUser;
    Picasso picasso = Picasso.get();

    //paypal
    String selectedPayment;
    private static final String PAYPAL_CLIENT_ID
            = "AZaOXGS0EswqL29ChEG5wZQUSlAMv6VO3k-EIhoAZgdE779KhCmA4jySX4iTfsc9CN3yARGTFDhlTG8h";
    private static final String PAYPAL_ENVIRONMENT
            = PayPalConfiguration.ENVIRONMENT_SANDBOX; // or PayPalConfiguration.ENVIRONMENT_PRODUCTION for live environment
    private static final int PAYPAL_REQUEST_CODE = 123;
    private PayPalConfiguration paypalConfig = new PayPalConfiguration()
            .environment(PAYPAL_ENVIRONMENT)
            .clientId(PAYPAL_CLIENT_ID);
    private Intent paypalServiceIntent;
    private String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date now = new Date();
        return dateFormat.format(now);
    }
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

        phoneLabel = header.findViewById(R.id.nameLabel);
        usernameLabel = header.findViewById(R.id.nameLabel);
        imageUser = header.findViewById(R.id.imageUser);
        inputAddress = findViewById(R.id.inputAddress);
        tvSubtotalAmount = findViewById(R.id.tvSubtotalAmount);
        totalAmount = findViewById(R.id.tvTotalAmount);
        tvDeliveryChargeAmount = findViewById(R.id.tvDeliveryChargeAmount);
        spinnerPayment = findViewById(R.id.spinerPaymentmethod);
        spinnerPayment.setAdapter(new ArrayAdapter<String>(this, R.layout.item_paymentmethod,
                getResources().getStringArray(R.array.method)));

        selectedPayment = spinnerPayment.getSelectedItem().toString();

        //-------toolbar
        setSupportActionBar(toolbar);
        //---------Navigation drawer menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerlayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerlayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        user = (User) getIntent().getExtras().get("object_user");
        picasso.load(user.getImage()).into(imageUser);

        usernameLabel.setText(user.getFullname());
        phoneLabel.setText(user.getPhone());
        bundle=new Bundle();
        bundle.putSerializable("object_user",user);


        //---------main_menu(receiver_view)
        itemsInCartList = (List<Item>) getIntent().getExtras().get("itemInCart");
        if(itemsInCartList == null) {
            itemsInCartList = new ArrayList<>();
        }
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

        paypalServiceIntent = new Intent(this, PayPalService.class);
        paypalServiceIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
        startService(paypalServiceIntent);

    }
    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();

    }

    private void startPayPalPayment() {
        PayPalPayment payment = new PayPalPayment(new BigDecimal("10.00"), "USD", "Item Name",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    private void onPlaceOrderButtonClick(List<Item> itemList) {
        selectedPayment = spinnerPayment.getSelectedItem().toString();
        if(selectedPayment.equals("Paypal")) {
            Toast.makeText(CartActivity.this, "Starting payment paypal", Toast.LENGTH_LONG).show();
            startPayPalPayment();
        }
        if (TextUtils.isEmpty(inputAddress.getText().toString())) {
            inputAddress.setError("Please enter address ");
            return;
        }
        final String username = usernameLabel.getText().toString();
        final String phone = phoneLabel.getText().toString();
        final String address = inputAddress.getText().toString();
        final String total = totalAmount.getText().toString();
        final String paymentmethod = spinnerPayment.getSelectedItem().toString();
        final String date = getCurrentDateTime();
        reference.child("orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long count = snapshot.child(user.getPhone()).getChildrenCount();
                count++;
                reference.child("orders").child(phone).child(String.valueOf(count)).child("Phone").setValue(phone);
                reference.child("orders").child(phone).child(String.valueOf(count)).child("Name").setValue(username);
                reference.child("orders").child(phone).child(String.valueOf(count)).child("City").setValue(address);
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
                reference.child("orders").child(phone).child(String.valueOf(count)).child("Date").setValue(date);


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
                intent1.putExtra("object_user", (Serializable) user);
                startActivity(intent1);
                break;
            case R.id.cart:
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
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    String paymentDetails = confirmation.toJSONObject().toString();

                }
            } else if (resultCode == RESULT_CANCELED) {
                // Payment was canceled by the user
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                // Invalid payment configuration
            }
        }
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