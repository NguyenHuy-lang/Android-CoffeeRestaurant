package com.example.mycoffee.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mycoffee.R;
import com.example.mycoffee.model.Item;
import com.squareup.picasso.Picasso;
import java.util.List;


public class PlaceOrderAdapter extends RecyclerView.Adapter<PlaceOrderAdapter.MyViewHolder> {

    private List<Item> itemList;
    Picasso picasso = Picasso.get();

    public PlaceOrderAdapter(List<Item> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    public void updateData(List<Item> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_order_recycler_row, parent, false);
        return  new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        holder.thumbImage.setImageResource(itemList.get(position).getPicId());
        picasso.load(itemList.get(position).getPicId()).into(holder.thumbImage);
        holder.itemName.setText(itemList.get(position).getName());
        holder.itemPrice.setText("Giá: " + itemList.get(position).getPrice()*itemList.get(position).getTotalInCart()+"$");
        holder.itemQty.setText("Số lượng: " + itemList.get(position).getTotalInCart());


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView  itemPrice;
        TextView  itemQty;
        ImageView thumbImage;

        public MyViewHolder(View view) {
            super(view);
            itemName = view.findViewById(R.id.itemName);
            itemPrice = view.findViewById(R.id.itemPrice);
            itemQty = view.findViewById(R.id.itemQty);
            thumbImage = view.findViewById(R.id.thumbImage);
        }
    }
}
