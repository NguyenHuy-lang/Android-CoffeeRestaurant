package dajava.pro.pcoffee.fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dajava.pro.pcoffee.R;
import dajava.pro.pcoffee.adapter.OrderManageAdapter;
import dajava.pro.pcoffee.model.Item;
import dajava.pro.pcoffee.model.Order;
import dajava.pro.pcoffee.model.User;
public class FragmentManageOrder extends Fragment implements OrderManageAdapter.OrderClickListenner {

    private RecyclerView mRecyclerView;
    private OrderManageAdapter mOrderAdapter;
    private Bundle bundle = new Bundle();
    private Spinner spinner;
    private Spinner updateStatusOrder;
    public User user;
    private Button updateStatusOrderButton;
    DatabaseReference reference= FirebaseDatabase.
            getInstance("https://amplified-coder-384315-default-rtdb.firebaseio.com/").
            getReference("my_shop");
    private List<Order> getAllOrder = new ArrayList<>();
    private List<Order> orderByStatus = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.manage_order_fragment, container, false);

        mRecyclerView = view.findViewById(R.id.order_manage);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        user = (User) getArguments().get("object_user");
        List<Order> orderList = new ArrayList<>();
        spinner = view.findViewById(R.id.spinner_manage_order);


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
                    getAllOrder.add(order);
                }
                Collections.reverse(orderList);
                List<Order> tmp = new ArrayList<>();
                for (Order order : getAllOrder) {
                    if (order.getStatus().equals("Pending")) {
                        tmp.add(order);
                    }
                }
                mOrderAdapter = new OrderManageAdapter(tmp,
                        getContext(),
                        FragmentManageOrder.this);

                // Set the RecyclerView adapter to be the OrderAdapter
                mRecyclerView.setAdapter(mOrderAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                String selectedItem = parent.getSelectedItem().toString();
                orderByStatus.clear();
                for (Order order : getAllOrder) {
                    if (order.getStatus().equals(selectedItem)) {
                        orderByStatus.add(order);
                    }
                }
                mOrderAdapter = new OrderManageAdapter(orderByStatus,
                        getContext(),
                        FragmentManageOrder.this);
                // Set the RecyclerView adapter to be the OrderAdapter
                mRecyclerView.setAdapter(mOrderAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // Create an instance of the OrderAdapter and pass in the list of orders


        return view;
    }

    @Override
    public void updateOrderStatus(Order order) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reference.child("orders")
                            .child(order.getPhone())
                            .child(String.valueOf(order.getId()))
                            .child("Status").setValue(order.getStatus());
                Toast.makeText(getContext(), "Update Order to " + order.getStatus() + " Success!", Toast.LENGTH_LONG).show();
                for (Order order1 : getAllOrder) {
                    if (order1.getDateCreate().equals(order.getDateCreate())) {
                        order1.setStatus(order.getStatus());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void updateData() {
        getAllOrder = new ArrayList<>();
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
                    getAllOrder.add(order);
                }
                Collections.reverse(orderList);
                List<Order> tmp = new ArrayList<>();
                for (Order order : getAllOrder) {
                    if (order.getStatus().equals("Pending")) {
                        tmp.add(order);
                    }
                }
                mOrderAdapter = new OrderManageAdapter(tmp,
                        getContext(),
                        FragmentManageOrder.this);

                // Set the RecyclerView adapter to be the OrderAdapter
                mRecyclerView.setAdapter(mOrderAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

