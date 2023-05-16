package dajava.pro.ushouldbuy;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import dajava.pro.ushouldbuy.adapter.ItemListAdapter;
import dajava.pro.ushouldbuy.model.Item;
import dajava.pro.ushouldbuy.model.User;

public class DetailProduct extends AppCompatActivity  {


    DatabaseReference reference= FirebaseDatabase.
            getInstance("https://amplified-coder-384315-default-rtdb.firebaseio.com/").
            getReference("my_shop");
    private ImageView imageView;
    private TextView productNameTextView;
    private TextView productPriceTextView;
    private Button buttonAddToCart;
    private Picasso picasso = Picasso.get();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        imageView = findViewById(R.id.product_image);
        productNameTextView = findViewById(R.id.product_name);
        productPriceTextView = findViewById(R.id.product_price);
        User user = (User) getIntent().getExtras().get("object_user");
        Item item = (Item) getIntent().getExtras().get("item");
        picasso.load(item.getPicId()).into(imageView);
        productNameTextView.setText(item.getName().toString());
        productPriceTextView.setText(item.getPrice() + "");

    }
}
