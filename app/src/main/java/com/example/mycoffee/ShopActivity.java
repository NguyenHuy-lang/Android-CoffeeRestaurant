package com.example.mycoffee;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycoffee.adapter.ItemListAdapter;
import com.example.mycoffee.adapter.OrderAdapter;
import com.example.mycoffee.model.Item;
import com.example.mycoffee.model.Order;
import com.example.mycoffee.model.User;
import com.example.mycoffee.utils.NotificationUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class ShopActivity extends AppCompatActivity implements ItemListAdapter.ItemListClickListener , NavigationView.OnNavigationItemSelectedListener{
    private List<Item> itemList = null;
    private ItemListAdapter itemListAdapter;
    private Toolbar toolbar;
    private List<Item> itemsInCartList;
    private int totalItemInCart = 0;
    private TextView buttonCheckout;
    private Button back;
    private ImageView bg;
    Picasso picasso = Picasso.get();
    private NavigationView navigationView;
    DrawerLayout drawerlayout;
    View header;
    TextView phoneLabel,usernameLabel;
    private Bundle bundle;
    private User user;
    Button search;
    EditText nameProductSearch;
    CircleImageView imageUser;
    DatabaseReference reference= FirebaseDatabase.
            getInstance("https://amplified-coder-384315-default-rtdb.firebaseio.com/").
            getReference("my_shop");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        drawerlayout = findViewById(R.id.drawer_layout);
        navigationView =  findViewById(R.id.nav_view_shop);
        header=navigationView.getHeaderView(0);
        phoneLabel = header.findViewById(R.id.phoneLabel);
        usernameLabel =  header.findViewById(R.id.nameLabel);
        imageUser = header.findViewById(R.id.imageUser);
        search = findViewById(R.id.btn_search_product);
        nameProductSearch = findViewById(R.id.edit_name_product_search);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("P COFFEE ");
        toolbar.setSubtitle("Học viện công nghệ bưu chính viễn thông");
        user = (User) getIntent().getExtras().get("object_user");
        phoneLabel.setText(user.getPhone());
        usernameLabel.setText(user.getFullname());
        picasso.load(user.getImage()).into(imageUser);
        bundle=new Bundle();
        bundle.putSerializable("object_user",user);
        itemList = new ArrayList<>();
        itemsInCartList = (List<Item>) getIntent().getExtras().get("itemInCart");
        if(itemsInCartList == null) {
            itemsInCartList = new ArrayList<Item>();
        }

        reference.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot itemSnapshot : snapshot.child("Drinks").getChildren()) {
                    // Retrieve the name, picture ID, and price for this item
                    Integer id = itemSnapshot.child("id").getValue(Integer.class);
                    String itemName = itemSnapshot.child("name").getValue(String.class);
                    String itemPicId = itemSnapshot.child("image").getValue(String.class);
                    Integer itemPrice = itemSnapshot.child("price").getValue(Integer.class);
                    System.out.println(itemName + " " + itemPicId + " " + itemPrice);
                    // Create a new Item object and add it to the list for this shop
                    Item item = new Item(itemName, itemPicId, itemPrice);
                    item.setId(id);
                    itemList.add(item);
                }
                RecyclerView recyclerView =  findViewById(R.id.recycler_view_menu);
                recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                itemListAdapter = new ItemListAdapter(ShopActivity.this,itemList, ShopActivity.this);
                itemListAdapter.setData(itemList);
                recyclerView.setAdapter(itemListAdapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String name = nameProductSearch.getText().toString();
                if(name != null) {
                    System.out.println("name      " + name);
                    List<Item> listItemSearchByName = new ArrayList<>();
                    for (Item item : itemList) {
                        if (item.getName().contains(name)) {
                            listItemSearchByName.add(item);
                        }
                    }
                    RecyclerView recyclerView =  findViewById(R.id.recycler_view_menu);
                    recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                    itemListAdapter = new ItemListAdapter(ShopActivity.this,listItemSearchByName, ShopActivity.this);
                    itemListAdapter.setData(listItemSearchByName);
                    recyclerView.setAdapter(itemListAdapter);
                } else {
                    RecyclerView recyclerView =  findViewById(R.id.recycler_view_menu);
                    recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                    itemListAdapter = new ItemListAdapter(ShopActivity.this,itemList, ShopActivity.this);
                    itemListAdapter.setData(itemList);
                    recyclerView.setAdapter(itemListAdapter);
                }
            }
        });

        setSupportActionBar(toolbar);

        //---------Navigation drawer menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerlayout ,
                toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerlayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        back=findViewById(R.id.back_to_home);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent i=new Intent(getApplicationContext(),HomeActivity.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
        buttonCheckout = findViewById(R.id.view_ur_cart);
        buttonCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemsInCartList == null) {
                    Toast.makeText(ShopActivity.this, "Please add some items into cart.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(ShopActivity.this, CartActivity.class);
                i.putExtras(bundle);
                i.putExtra("itemInCart", (Serializable) itemsInCartList);
                startActivity(i);
            }
        });
        List<Order> orderList = new ArrayList<>();
        reference.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot itemSnapshot : snapshot.child("orders").child(user.getPhone()).getChildren()) {
                    // Retrieve the name, picture ID, and price for this item
                    String city = itemSnapshot.child("City").getValue(String.class);
                    String name = itemSnapshot.child("Name").getValue(String.class);
                    String paymentMethod = itemSnapshot.child("Payment method").getValue(String.class);
                    String phone = itemSnapshot.child("Phone").getValue(String.class);
                    String totalCost = itemSnapshot.child("Total Cost").getValue(String.class);
                    String date = itemSnapshot.child("Date").getValue(String.class);
                    String status = itemSnapshot.child("Status").getValue(String.class);
                    boolean notify = itemSnapshot.child("notify").getValue(boolean.class);
                    Long id = itemSnapshot.child("id").getValue(Long.class);
                    Order order = new Order();
                    order.setmName(name);
                    order.setmCity(city);
                    order.setmPhone(phone);
                    order.setmPaymentMethod(paymentMethod);
                    order.setmTotalCost(totalCost);
                    order.setDateCreate(date);
                    order.setStatus(status);
                    order.setmItems(new ArrayList<>());
                    order.setNotify(notify);
                    order.setId(id);
                    for (DataSnapshot dataSnapshot : itemSnapshot.child("drinks").getChildren()) {
                        String nameItem = dataSnapshot.child("name").getValue(String.class);
                        Integer price = dataSnapshot.child("price").getValue(Integer.class);
                        Long quantity = dataSnapshot.child("quantity").getValue(Long.class);
                        Item item = new Item();
                        item.setName(nameItem);
                        item.setPrice(price);
                        item.setTotalInCart(Math.toIntExact(quantity));
                        item.setPicId(dataSnapshot.child("image").getValue(String.class));
                        order.getItems().add(item);
                    }
                    orderList.add(order);

                }
                List<Long> listOrderUpdate = new ArrayList<>();
                String sequenceId = "";
                for (Order order1 : orderList) {
                    if (order1.isNotify()) {
                        listOrderUpdate.add(order1.getId());
                        sequenceId += order1.getId() + " ";
                    }
                }

                if(sequenceId.length() > 0) {
                    NotificationUtils.showNotification(getApplicationContext(),
                            "Order",
                            "Order " + sequenceId + " already admin update ",
                            WelcomeActivity.class, user, sequenceId);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onAddToCartClick(Item menu) {
        Intent intent1=new Intent(ShopActivity.this, DetailProductActivity.class);
        intent1.putExtra("object_user", user);
        intent1.putExtra("item", (Serializable) menu);
        intent1.putExtra("itemInCart", (Serializable) itemsInCartList);
        startActivity(intent1);
    }

    @Override
    public void onUpdateCartClick(Item menu) {
        if(itemsInCartList.contains(menu)) {
            int index = itemsInCartList.indexOf(menu);
            itemsInCartList.remove(index);
            itemsInCartList.add(index, menu);
            totalItemInCart = 0;
            for(Item m : itemsInCartList) {
                totalItemInCart = totalItemInCart + m.getTotalInCart();
            }
            buttonCheckout.setText("Checkout (" +totalItemInCart +") items");
        }
    }

    @Override
    public void onRemoveFromCartClick(Item menu) {
        if(itemsInCartList.contains(menu)) {
            itemsInCartList.remove(menu);
            totalItemInCart = 0;

            for(Item m : itemsInCartList) {
                totalItemInCart = totalItemInCart + m.getTotalInCart();
            }
            buttonCheckout.setText("Checkout (" +totalItemInCart +") items");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
            default:
                //do nothing
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            // get data from intent

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.home:
                break;
            case R.id.cart:
                Intent intent1=new Intent(ShopActivity.this,CartActivity.class);
                intent1.putExtra("object_user", (Serializable) user);
                intent1.putExtra("itemInCart", (Serializable) itemsInCartList);
                startActivity(intent1);
                break;
            case R.id.log_out:
                Intent intent2=new Intent(ShopActivity.this,LoginActivity.class);
                startActivity(intent2);
                break;

        }
        return true;
    }
}