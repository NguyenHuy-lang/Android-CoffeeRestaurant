package dajava.pro.ushouldbuy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dajava.pro.ushouldbuy.adapter.ItemListAdapter;
import dajava.pro.ushouldbuy.model.Item;
import dajava.pro.ushouldbuy.model.User;

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
        phoneLabel = header.findViewById(R.id.phonenumberLabel);
        usernameLabel =  header.findViewById(R.id.usernameLabel);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("P COFFEE ");
        toolbar.setSubtitle("Học viện công nghệ bưu chính viễn thông");
        bg = findViewById(R.id.restaurant_bg);
//        bg.setImageResource(shop.getShopBg());
//        picasso.load(shop.getShopBg()).into(bg);
        user = (User) getIntent().getExtras().get("object_user");
        phoneLabel.setText(user.getPhone());
        usernameLabel.setText(user.getFullname());
        bundle=new Bundle();
        bundle.putSerializable("object_user",user);
        itemList = new ArrayList<>();


        reference.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot itemSnapshot : snapshot.child("Drinks").getChildren()) {
                    // Retrieve the name, picture ID, and price for this item
                    Integer id = itemSnapshot.child("id").getValue(Integer.class);
                    String itemName = itemSnapshot.child("name").getValue(String.class);
                    String itemPicId = itemSnapshot.child("image").getValue(String.class);
                    float itemPrice = itemSnapshot.child("price").getValue(Float.class);
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
                Intent i=new Intent(getApplicationContext(),WelcomeActivity.class);
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

    }

    @Override
    public void onAddToCartClick(Item menu) {
        Intent intent1=new Intent(ShopActivity.this, DetailProductActivity.class);
        intent1.putExtra("object_user", user);
        intent1.putExtra("item", (Serializable) menu);
        startActivity(intent1);

//        if(itemsInCartList == null) {
//            itemsInCartList = new ArrayList<>();
//        }
//        itemsInCartList.add(menu);
//        totalItemInCart = 0;
//
//        for(Item m : itemsInCartList) {
//            totalItemInCart = totalItemInCart + m.getTotalInCart();
//        }
//        buttonCheckout.setText("Checkout (" +totalItemInCart +") items");

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
//                Intent intent1=new Intent(ShopActivity.this,CartActivity.class);
//                intent1.putExtras(bundle);
//                startActivity(intent1);
                break;
            case R.id.log_out:
                Intent intent2=new Intent(ShopActivity.this,LoginActivity.class);
                startActivity(intent2);
                break;

        }
        return true;
    }
}