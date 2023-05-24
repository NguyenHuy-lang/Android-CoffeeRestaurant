package dajava.pro.pcoffee.adapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.List;
import dajava.pro.pcoffee.model.Item;

public class ItemManageAdapter extends RecyclerView.Adapter<ItemManageAdapter.MyViewHolder> {
    private Context context;
    private List<Item> itemList;
    private Picasso picasso = Picasso.get();
    private ItemListener itemListener;
    DatabaseReference reference= FirebaseDatabase.
            getInstance("https://amplified-coder-384315-default-rtdb.firebaseio.com/").
            getReference("my_shop");
    public ItemManageAdapter(List<Item> itemList,Context context) {
        this.context = context;
        this.itemList = itemList;
    }
    public void setData(List<Item> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manage,
                parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        picasso.load(itemList.get(position).getPicId()).into(holder.itemImage);
        holder.itemNameTextView.setText(itemList.get(position).getName().toString());
        holder.itemPriceTextView.setText(itemList.get(position).getPrice() + "");
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

     class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView itemImage;
        private TextView itemNameTextView;
        private TextView itemPriceTextView;
        private Button delete;
        private Button edit;
        public MyViewHolder(@NonNull View view) {
            super(view);
            itemImage = view.findViewById(R.id.image_item_manage);
            itemNameTextView = view.findViewById(R.id.name_item_manage_text_view);
            itemPriceTextView = view.findViewById(R.id.price_item_manage_text_view);
            delete = view.findViewById(R.id.delete_manage_button);
            edit = view.findViewById(R.id.edit_manage_button);
            edit.setOnClickListener(this);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Thông báo xóa");
                    builder.setMessage("Bạn có chắc muốn xóa " + itemList.get(getAdapterPosition()).getName() + " này không");
                    builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int pos = getAdapterPosition();
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    DatabaseReference itemRef = reference.child("Drinks").child(String.valueOf(itemList.get(pos).getId()));
                                    itemRef.removeValue();
                                    itemList.removeIf(x -> x.getId() == itemList.get(pos).getId());
                                    notifyDataSetChanged();
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            Toast.makeText(context.getApplicationContext(), "Xoa Thanh Cong", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        }

        @Override
        public void onClick(View view) {
            if (itemListener != null) {
                Item item = itemList.get(getAdapterPosition());
                itemListener.onItemEditListener(view, item.getId());
            }
        }
    }
    public interface ItemListener{
        void onItemEditListener(View view, int id);
    }
}
