package dajava.pro.ushouldbuy.adapter;


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

import com.squareup.picasso.Picasso;

import java.util.List;

import dajava.pro.ushouldbuy.R;
import dajava.pro.ushouldbuy.model.Item;

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
    public ItemListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods, parent, false);
        return  new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        picasso.load(itemList.get(position).getPicId()).into(holder.thumbImage);
        holder.itemName.setText(itemList.get(position).getName());
        holder.itemPrice.setText("Giá: "+itemList.get(position).getPrice()+"$");
        holder.addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item menu  = itemList.get(position);
                menu.setTotalInCart(1);
                clickListener.onAddToCartClick(menu);
                holder.addMoreLayout.setVisibility(View.VISIBLE);
                holder.addToCartButton.setVisibility(View.GONE);
                holder.tvCount.setText(menu.getTotalInCart()+"");
            }
        });
        holder.imageMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item menu  = itemList.get(position);
                int total = menu.getTotalInCart();
                total--;
                if(total > 0 ) {
                    menu.setTotalInCart(total);
                    clickListener.onUpdateCartClick(menu);
                    holder.tvCount.setText(total +"");
                } else {
                    holder.addMoreLayout.setVisibility(View.GONE);
                    holder.addToCartButton.setVisibility(View.VISIBLE);
                    menu.setTotalInCart(total);
                    clickListener.onRemoveFromCartClick(menu);
                }
            }
        });

        holder.imageAddOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item menu  = itemList.get(position);
                int total = menu.getTotalInCart();
                total++;
                if(total <= 10 ) {
                    menu.setTotalInCart(total);
                    clickListener.onUpdateCartClick(menu);
                    holder.tvCount.setText(total +"");
                }
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

