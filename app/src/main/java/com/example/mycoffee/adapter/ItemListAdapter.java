package com.example.mycoffee.adapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mycoffee.R;
import com.example.mycoffee.model.Item;
import com.squareup.picasso.Picasso;
import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.MyViewHolder> {
    private Context mContext;
    private List<Item> itemList;
    private ItemListClickListener clickListener;
    Picasso picasso = Picasso.get();

    public ItemListAdapter(Context mContext,List<Item> itemList,ItemListClickListener clickListener) {
        this.mContext = mContext;
        this.itemList = itemList;
        this.clickListener = clickListener;
    }

    public void setData(List<Item> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods, parent, false);
        return  new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        picasso.load(itemList.get(position).getPicId()).into(holder.thumbImage);
        holder.itemName.setText(itemList.get(position).getName());
        holder.itemPrice.setText("Giá: "+itemList.get(position).getPrice()+"$");
        holder.addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item menu  = itemList.get(position);
                clickListener.onAddToCartClick(menu);
                holder.tvCount.setText(menu.getTotalInCart()+"");
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView  itemPrice;
        TextView  addToCartButton;
        ImageView thumbImage;
        ImageView imageMinus;
        ImageView imageAddOne;
        TextView  tvCount;
        LinearLayout addMoreLayout;


        public MyViewHolder(@NonNull View view) {
            super(view);
            itemName = view.findViewById(R.id.itemName);
            itemPrice = view.findViewById(R.id.itemPrice);
            addToCartButton = view.findViewById(R.id.addToCartButton);
            thumbImage = view.findViewById(R.id.thumbImageFood);
            imageMinus = view.findViewById(R.id.imageMinus);
            imageAddOne = view.findViewById(R.id.imageAddOne);
            tvCount = view.findViewById(R.id.tvCount);

            addMoreLayout  = view.findViewById(R.id.addMoreLayout);
        }
    }

    public interface ItemListClickListener {
        public void onAddToCartClick(Item menu);
        public void onUpdateCartClick(Item menu);
        public void onRemoveFromCartClick(Item menu);
    }
}

