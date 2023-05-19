package dajava.pro.ushouldbuy;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import dajava.pro.ushouldbuy.adapter.CommentAdapter;
import dajava.pro.ushouldbuy.model.Comment;
import dajava.pro.ushouldbuy.model.Item;
import dajava.pro.ushouldbuy.model.User;

public class DetailProductActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{
    DatabaseReference reference= FirebaseDatabase.
            getInstance("https://amplified-coder-384315-default-rtdb.firebaseio.com/").
            getReference("my_shop");
    private ImageView imageView;
    private TextView productNameTextView;
    private TextView productPriceTextView;
    private TextView buttonAddToCart;
    private final Picasso picasso = Picasso.get();
    private ImageView add;
    private ImageView minus;
    private final List<Item> itemList = new ArrayList<>();
    private final List<Comment> commentList = new ArrayList<>();
    private LinearLayout editQuantityItem;
    private TextView quantityTextView;
    private RecyclerView commentRecyclerView;
    private CommentAdapter commentAdapter;
    private Button addComment;
    private EditText contentCommentEditText;
    private RatingBar commentRating;
    private RatingBar ratingProduct;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerlayout;
    private String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date now = new Date();
        return dateFormat.format(now);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        imageView = findViewById(R.id.product_image);
        productNameTextView = findViewById(R.id.product_name);
        productPriceTextView = findViewById(R.id.product_price);
        add = findViewById(R.id.imageAddOneDetail);
        minus = findViewById(R.id.imageMinusDetail);
        ratingProduct = findViewById(R.id.product_rating);
        ratingProduct.setIsIndicator(true);
        buttonAddToCart = findViewById(R.id.addToCartButtonDetail);
        editQuantityItem = findViewById(R.id.addMoreLayoutDetail);
        quantityTextView = findViewById(R.id.tvCountDetail);
        addComment = findViewById(R.id.submit_comment_button);
        User user = (User) getIntent().getExtras().get("object_user");
        Item item = (Item) getIntent().getExtras().get("item");
        picasso.load(item.getPicId()).into(imageView);
        productNameTextView.setText(item.getName());
        productPriceTextView.setText(item.getPrice() + "");
        commentRecyclerView = findViewById(R.id.comments_recycler_view);
        contentCommentEditText = findViewById(R.id.comment_edit_text);
        commentRating = findViewById(R.id.user_rating);
        navigationView = findViewById(R.id.nav_view_shop_product_detail);
        toolbar = findViewById(R.id.toolbar_product_detail);
        toolbar.setTitle("P COFFEE ");
        toolbar.setSubtitle("Học viện công nghệ bưu chính viễn thông");
        drawerlayout = findViewById(R.id.drawer_layout_product_detail);
        setSupportActionBar(toolbar);

        //---------Navigation drawer menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerlayout ,
                toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerlayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(DetailProductActivity.this);

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(commentRating.getRating() == 0) {
                    Toast.makeText(DetailProductActivity.this, "Please vote rating for product", Toast.LENGTH_LONG).show();
                } else if (contentCommentEditText.getText() == null) {
                    Toast.makeText(DetailProductActivity.this, "Please enter your comment", Toast.LENGTH_LONG).show();
                } else {
                    String content = contentCommentEditText.getText().toString();
                    float rating = commentRating.getRating();
                    Comment comment = new Comment();
                    comment.setContent(content);
                    comment.setRating((int)rating);
                    comment.setUser(user);
                    comment.setDateTime(getCurrentDateTime());

                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Long newCommentId = snapshot.child("Drinks").
                                    child(String.valueOf(item.getId())).
                                    child("comments").
                                    getChildrenCount();
                            newCommentId += 1;
                            DatabaseReference sn = reference.child("Drinks").
                                    child(String.valueOf(item.getId())).
                                    child("comments").child(String.valueOf(newCommentId));
                            sn.child("id").setValue(newCommentId);
                            sn.child("content").setValue(comment.getContent());
                            sn.child("userComment").child("image").setValue(comment.getUser().getImage());
                            sn.child("userComment").child("fullname").setValue(comment.getUser().getFullname());
                            sn.child("rating").setValue(comment.getRating());
                            sn.child("dateTime").setValue(comment.getDateTime());
                            commentList.add(comment);
                            Collections.reverse(commentList);
                            initRecyclerView(commentList);
                            float sumStar = 0f;
                            for (Comment comment : commentList) {
                                sumStar += comment.getRating();
                            }
                            ratingProduct.setRating(sumStar / commentList.size());
                            contentCommentEditText.setText("");
                            commentRating.setRating(0);
                            Toast.makeText(DetailProductActivity.this, "Success leave comment", Toast.LENGTH_LONG).show();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });

        buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Item newItem = (Item) item.clone();
                    newItem.setTotalInCart(1);
                    itemList.add(item);
                    buttonAddToCart.setVisibility(View.GONE);
                    editQuantityItem.setVisibility(View.VISIBLE);
                    quantityTextView.setText("1");
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Item newItem = (Item) item.clone();
                    for (Item item1 : itemList) {
                        if (item1.getName().equals(newItem.getName())) {
                            item1.setTotalInCart(item1.getTotalInCart() + 1);
                            quantityTextView.setText(String.valueOf(item1.getTotalInCart()));
                        }
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    boolean delete = false;
                    Item newItem = (Item) item.clone();
                    for (Item item1 : itemList) {
                        if (item1.getName().equals(newItem.getName())) {
                            item1.setTotalInCart(item1.getTotalInCart() - 1);
                            if (item1.getTotalInCart() <= 0) {
                                item1.setTotalInCart(0);
                                delete = true;
                            }
                            newItem.setTotalInCart(item1.getTotalInCart());
                            quantityTextView.setText(String.valueOf(newItem.getTotalInCart()));
                        }
                    }
                    if(delete == true) {
                        itemList.removeIf(i -> i.getName().equals(newItem.getName()));
                        editQuantityItem.setVisibility(View.GONE);
                        buttonAddToCart.setVisibility(View.VISIBLE);
                        quantityTextView.setText("0");
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot commentSnapshot : snapshot.child("Drinks").
                                                    child(String.valueOf(item.getId())).
                                                    child("comments").
                                                    getChildren()) {
                    String content = commentSnapshot.child("content").getValue(String.class);
                    Integer rating = commentSnapshot.child("rating").getValue(Integer.class);
                    String dateTime = commentSnapshot.child("dateTime").getValue(String.class);
                    User userComment = new User();
                    userComment.setFullname(commentSnapshot.child("userComment").child("fullname").getValue(String.class));
                    userComment.setImage(commentSnapshot.child("userComment").child("image").getValue(String.class));
                    Comment comment = new Comment();
                    comment.setRating(rating);
                    comment.setContent(content);
                    comment.setUser(userComment);
                    comment.setDateTime(dateTime);
                    commentList.add(comment);
                }
                float sumStar = 0f;
                for (Comment comment : commentList) {
                    sumStar += comment.getRating();
                }
                Collections.reverse(commentList);
                ratingProduct.setRating(sumStar / commentList.size());
                initRecyclerView(commentList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initRecyclerView(List<Comment> commentList) {
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(commentList, DetailProductActivity.this);
        commentRecyclerView.setAdapter(commentAdapter);
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
