package dajava.pro.ushouldbuy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import dajava.pro.ushouldbuy.R;
import dajava.pro.ushouldbuy.ShopActivity;
import dajava.pro.ushouldbuy.adapter.ItemListAdapter;
import dajava.pro.ushouldbuy.adapter.OrderAdapter;
import dajava.pro.ushouldbuy.model.Item;
import dajava.pro.ushouldbuy.model.Order;
import dajava.pro.ushouldbuy.model.User;

public class FragmentAds1 extends Fragment {

    private RecyclerView mRecyclerView;
    private OrderAdapter mOrderAdapter;
    private  Bundle bundle = new Bundle();
    DatabaseReference reference= FirebaseDatabase.
            getInstance("https://amplified-coder-384315-default-rtdb.firebaseio.com/").
            getReference("my_shop");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.order_list_fragment, container, false);

        mRecyclerView = view.findViewById(R.id.order_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        User user = (User) getArguments().get("object_user");
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
                    Order order = new Order();
                    order.setmName(name);
                    order.setmCity(city);
                    order.setmPhone(phone);
                    order.setmPaymentMethod(paymentMethod);
                    order.setmTotalCost(totalCost);
                    order.setmItems(new ArrayList<>());
                    for (DataSnapshot dataSnapshot : itemSnapshot.child("drinks").getChildren()) {
                        String nameItem = dataSnapshot.child("name").getValue(String.class);
                        Long price = dataSnapshot.child("price").getValue(Long.class);
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
                Collections.reverse(orderList);
                mOrderAdapter = new OrderAdapter(orderList);

                // Set the RecyclerView adapter to be the OrderAdapter
                mRecyclerView.setAdapter(mOrderAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // Create an instance of the OrderAdapter and pass in the list of orders


        return view;
    }
}

