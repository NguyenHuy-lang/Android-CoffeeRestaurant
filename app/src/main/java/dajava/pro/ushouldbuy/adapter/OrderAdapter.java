package dajava.pro.ushouldbuy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;


import dajava.pro.ushouldbuy.R;
import dajava.pro.ushouldbuy.model.Order;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> mOrders;

    public OrderAdapter(List<Order> orders) {
        mOrders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item,
                parent, false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = mOrders.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return mOrders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {

        private TextView mCityTextView;
        private TextView mNameTextView;
        private TextView mPaymentMethodTextView;
        private TextView mPhoneTextView;
        private TextView mTotalCostTextView;
        private LinearLayout mItemsLayout;

        OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            mCityTextView = itemView.findViewById(R.id.city_text_view);
            mNameTextView = itemView.findViewById(R.id.name_text_view);
            mPaymentMethodTextView = itemView.findViewById(R.id.payment_method_text_view);
            mPhoneTextView = itemView.findViewById(R.id.phone_text_view);
            mTotalCostTextView = itemView.findViewById(R.id.total_cost_text_view);
            mItemsLayout = itemView.findViewById(R.id.items_layout);
        }

        void bind(Order order) {
            mCityTextView.setText("City: " + order.getCity());
            mNameTextView.setText("Name: " + order.getName());
            mPaymentMethodTextView.setText("Payment method: " + order.getPaymentMethod());
            mPhoneTextView.setText("Phone: " + order.getPhone());
            mTotalCostTextView.setText("Total Cost: " + order.getTotalCost());

            mItemsLayout.removeAllViews();
            for (Map.Entry<String, Integer> entry : order.getItems().entrySet()) {
                String itemName = entry.getKey();
                int quantity = entry.getValue();

                View itemView = LayoutInflater.from(mItemsLayout.getContext()).inflate(R.layout.items_layout,
                        mItemsLayout, false);
                TextView itemNameTextView = itemView.findViewById(R.id.item_name_text_view);
                TextView quantityTextView = itemView.findViewById(R.id.item_quantity_text_view);

                itemNameTextView.setText(itemName);
                quantityTextView.setText("Quantity: " + quantity);

                mItemsLayout.addView(itemView);
            }
        }
    }
}
