package dajava.pro.pcoffee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.List;

import dajava.pro.pcoffee.model.Item;
import dajava.pro.pcoffee.model.User;

public class HomeActivity extends AppCompatActivity {
    private Button goShoppingButton;
    private Button viewPersonInformationButton;
    private List<Item> itemInCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        User user = (User) getIntent().getExtras().get("object_user");
        itemInCart = (List<Item>) getIntent().getExtras().get("itemInCart");
        goShoppingButton = findViewById(R.id.goShoppingButton);
        viewPersonInformationButton = findViewById(R.id.viewPersonalInformationButton);
        goShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ShopActivity.class);
                intent.putExtra("object_user", user);
                intent.putExtra("itemInCart", (Serializable) itemInCart);
                startActivity(intent);
            }
        });
        viewPersonInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, WelcomeActivity.class);
                intent.putExtra("object_user", user);
                intent.putExtra("itemInCart", (Serializable) itemInCart);
                startActivity(intent);
            }
        });
    }
}
