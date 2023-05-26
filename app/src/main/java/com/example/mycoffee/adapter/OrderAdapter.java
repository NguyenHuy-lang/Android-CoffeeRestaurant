package com.example.mycoffee.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycoffee.R;
import com.example.mycoffee.model.Item;
import com.example.mycoffee.model.Order;
import com.squareup.picasso.Picasso;
import java.util.List;
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> mOrderList;
    static Picasso picasso = Picasso.get();

    public OrderAdapter(List<Order> orderList) {
        mOrderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item,
                parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = mOrderList.get(position);

        holder.cityTextView.setText(order.getCity());
        holder.nameTextView.setText(order.getName());
        holder.paymentMethodTextView.setText(order.getPaymentMethod());
        holder.phoneTextView.setText(order.getPhone());
        holder.totalCostTextView.setText(order.getTotalCost());
        holder.dateTextView.setText(order.getDateCreate());
        holder.statusTextView.setText(order.getStatus());
        holder.idTextView.setText(String.valueOf(order.getId()));
        // Notify
        holder.productRecyclerView.setAdapter(new ItemAdapter(order.getItems()));
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        public TextView cityTextView;
        public TextView nameTextView;
        public TextView paymentMethodTextView;
        public TextView phoneTextView;
        public TextView totalCostTextView;
        public TextView dateTextView;
        public TextView statusTextView;
        public TextView idTextView;
        public RecyclerView productRecyclerView;

        public OrderViewHolder(View itemView) {
            super(itemView);
            cityTextView = itemView.findViewById(R.id.city_text_view);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            paymentMethodTextView = itemView.findViewById(R.id.payment_method_text_view);
            phoneTextView = itemView.findViewById(R.id.phone_text_view);
            totalCostTextView = itemView.findViewById(R.id.total_cost_text_view);
            productRecyclerView = itemView.findViewById(R.id.item_recycler_view);
            dateTextView = itemView.findViewById(R.id.date_text_view);
            statusTextView = itemView.findViewById(R.id.status_text_view);
            idTextView = itemView.findViewById(R.id.id_text_view);
            productRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
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
}
