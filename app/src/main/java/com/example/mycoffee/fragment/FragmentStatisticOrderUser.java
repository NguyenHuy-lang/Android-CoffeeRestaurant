package com.example.mycoffee.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycoffee.R;
import com.example.mycoffee.adapter.OrderAdapter;
import com.example.mycoffee.model.Item;
import com.example.mycoffee.model.Order;
import com.example.mycoffee.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FragmentStatisticOrderUser extends Fragment {
    private RecyclerView mRecyclerView;
    private OrderAdapter mOrderAdapter;
    List<Long> idOrder = new ArrayList<>();
    List<Order> orderList = new ArrayList<>();
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
        String sequenceId = (String) getArguments().get("order_ids");
        String[] numbersArray = sequenceId.split(" ");
        for (String number : numbersArray) {
            try {
                long value = Long.parseLong(number);
                idOrder.add(value);
            } catch (NumberFormatException e) {
                // Handle parsing errors if necessary
            }
        }
        orderList = new ArrayList<>();
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
                    Long id = itemSnapshot.child("id").getValue(Long.class);
                    Order order = new Order();
                    order.setmName(name);
                    order.setmCity(city);
                    order.setmPhone(phone);
                    order.setmPaymentMethod(paymentMethod);
                    order.setmTotalCost(totalCost);
                    order.setDateCreate(date);
                    order.setStatus(status);
                    order.setId(id);
                    order.setmItems(new ArrayList<>());
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
                Collections.reverse(orderList);
                List<Order> tmp = new ArrayList<>();
                if (idOrder.size() > 0) {
                    for (Long id : idOrder) {
                        for (Order order: orderList) {
                            if (order.getId().longValue() == id.longValue()) {
                                tmp.add(order);
                                break;
                            }
                        }
                    }
                    mOrderAdapter = new OrderAdapter(tmp);
                    // Set the RecyclerView adapter to be the OrderAdapter
                    mRecyclerView.setAdapter(mOrderAdapter);
                } else {
                    mOrderAdapter = new OrderAdapter(orderList);
                    // Set the RecyclerView adapter to be the OrderAdapter
                    mRecyclerView.setAdapter(mOrderAdapter);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Create an instance of the OrderAdapter and pass in the list of orders
        return view;
    }
}

