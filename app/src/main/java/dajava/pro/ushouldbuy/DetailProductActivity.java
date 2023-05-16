package dajava.pro.ushouldbuy;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

import dajava.pro.ushouldbuy.adapter.CommentAdapter;
import dajava.pro.ushouldbuy.model.Comment;
import dajava.pro.ushouldbuy.model.Item;
import dajava.pro.ushouldbuy.model.User;

public class DetailProductActivity extends AppCompatActivity  {
    DatabaseReference reference= FirebaseDatabase.
            getInstance("https://amplified-coder-384315-default-rtdb.firebaseio.com/").
            getReference("my_shop");
    private ImageView imageView;
    private TextView productNameTextView;
    private TextView productPriceTextView;
    private TextView buttonAddToCart;
    private Picasso picasso = Picasso.get();
    private ImageView add;
    private ImageView minus;
    private List<Item> itemList = new ArrayList<>();
    private List<Comment> commentList = new ArrayList<>();
    private LinearLayout editQuantityItem;
    private TextView quantityTextView;
    private RecyclerView commentRecyclerView;
    private CommentAdapter commentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        imageView = findViewById(R.id.product_image);
        productNameTextView = findViewById(R.id.product_name);
        productPriceTextView = findViewById(R.id.product_price);
        add = findViewById(R.id.imageAddOneDetail);
        minus = findViewById(R.id.imageMinusDetail);
        buttonAddToCart = findViewById(R.id.addToCartButtonDetail);
        editQuantityItem = findViewById(R.id.addMoreLayoutDetail);
        quantityTextView = findViewById(R.id.tvCountDetail);
        User user = (User) getIntent().getExtras().get("object_user");
        Item item = (Item) getIntent().getExtras().get("item");
        picasso.load(item.getPicId()).into(imageView);
        productNameTextView.setText(item.getName().toString());
        productPriceTextView.setText(item.getPrice() + "");
        commentRecyclerView = findViewById(R.id.comments_recycler_view);

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
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setContent("good and nice");
        comment.setRating(3);
        commentList.add(comment);
        initRecyclerView(commentList);
    }

    private void initRecyclerView(List<Comment> commentList) {
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(commentList, DetailProductActivity.this);
        commentRecyclerView.setAdapter(commentAdapter);
    }
}
