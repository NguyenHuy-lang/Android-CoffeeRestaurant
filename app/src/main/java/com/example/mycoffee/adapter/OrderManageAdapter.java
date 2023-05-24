package com.example.mycoffee.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycoffee.R;
import com.example.mycoffee.model.Item;
import com.example.mycoffee.model.Order;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderManageAdapter
        extends RecyclerView.Adapter<OrderManageAdapter.OrderManageViewHolder>
        {

    private List<Order> mOrderList;
    private Context context;
    static Picasso picasso = Picasso.get();
    private OrderClickListenner orderClickListenner;

    public OrderManageAdapter(List<Order> orderList, Context context, OrderClickListenner orderClickListenner) {
        this.mOrderList = orderList;
        this.context = context;
        this.orderClickListenner = orderClickListenner;
    }

    @NonNull
    @Override
    public OrderManageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_manage,
                parent, false);
        return new OrderManageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderManageViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Order order = mOrderList.get(position);

        holder.cityTextView.setText(order.getCity());
        holder.nameTextView.setText(order.getName());
        holder.paymentMethodTextView.setText(order.getPaymentMethod());
        holder.phoneTextView.setText(order.getPhone());
        holder.totalCostTextView.setText(order.getTotalCost());
        holder.dateTextView.setText(order.getDateCreate());
        holder.orderStatusTextView.setText(order.getStatus());
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) holder.updateStatusOrderSpinner.getAdapter(); // Assuming the spinner uses an ArrayAdapter<String>
        int positionStatus = adapter.getPosition(order.getStatus());
        holder.updateStatusOrderSpinner.setSelection(positionStatus);

        // Notify
        holder.productRecyclerView.setAdapter(new ItemAdapter(order.getItems()));
        holder.updateStatusOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order getOder = mOrderList.get(position);
                orderClickListenner.updateOrderStatus(getOder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    public class OrderManageViewHolder extends RecyclerView.ViewHolder {
        public TextView cityTextView;
        public TextView nameTextView;
        public TextView paymentMethodTextView;
        public TextView phoneTextView;
        public TextView totalCostTextView;
        public TextView dateTextView;
        public Spinner updateStatusOrderSpinner;
        public RecyclerView productRecyclerView;
        public TextView orderStatusTextView;
        public Button updateStatusOrder;
        public OrderManageViewHolder(View itemView) {
            super(itemView);
            cityTextView = itemView.findViewById(R.id.city_text_view_manage);
            nameTextView = itemView.findViewById(R.id.name_text_view_manage);
            paymentMethodTextView = itemView.findViewById(R.id.payment_method_text_view_manage);
            phoneTextView = itemView.findViewById(R.id.phone_text_view_manage);
            totalCostTextView = itemView.findViewById(R.id.total_cost_text_view_manage);
            productRecyclerView = itemView.findViewById(R.id.item_recycler_view_manage);
            dateTextView = itemView.findViewById(R.id.date_text_view_manage);
            updateStatusOrderSpinner = itemView.findViewById(R.id.spinner_manage_update_order_status);
            updateStatusOrder = itemView.findViewById(R.id.update_order_manage);
            productRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            orderStatusTextView = itemView.findViewById(R.id.order_manage_status_text_view);

            updateStatusOrderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    orderStatusTextView.setText(
                            updateStatusOrderSpinner.getSelectedItem().toString());
                    System.out.println("Layout Position " + getLayoutPosition() + " === " + "getAdapterPosition " + getAdapterPosition());
                    mOrderList.get(getAdapterPosition()).setStatus(updateStatusOrderSpinner.getSelectedItem().toString());

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }
    }

    public static class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

        private List<Item> itemList;

        public ItemAdapter(List<Item> mItemList) {
            itemList = mItemList;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_layout, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            Item item = itemList.get(position);
            holder.itemPriceTextView.setText(String.valueOf(item.getPrice()));
            holder.itemNameTextView.setText(item.getName());
            holder.itemQuantityTextView.setText(String.valueOf(item.getTotalInCart()));

            picasso.load(itemList.get(position).getPicId()).into(holder.itemImageView);
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        public static class ItemViewHolder extends RecyclerView.ViewHolder {
            public TextView itemNameTextView;
            public TextView itemPriceTextView;
            public TextView itemQuantityTextView;
            public ImageView itemImageView;
            public ItemViewHolder(View itemView) {
                super(itemView);
                itemNameTextView = itemView.findViewById(R.id.item_name_text_view);
                itemPriceTextView = itemView.findViewById(R.id.item_price_text_view);
                itemQuantityTextView = itemView.findViewById(R.id.item_quantity_text_view);
                itemImageView = itemView.findViewById(R.id.item_image_view);

            }
        }
    }
    public interface OrderClickListenner {
        void updateOrderStatus(Order order);
    }
}
