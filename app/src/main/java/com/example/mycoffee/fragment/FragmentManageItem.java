package com.example.mycoffee.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycoffee.R;
import com.example.mycoffee.ShopActivity;
import com.example.mycoffee.adapter.ItemListAdapter;
import com.example.mycoffee.adapter.ItemManageAdapter;
import com.example.mycoffee.model.Item;
import com.example.mycoffee.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FragmentManageItem extends Fragment implements ItemManageAdapter.ItemListener {
    private static final int PICK_IMAGE_REQUEST = 3;

    private ItemManageAdapter itemManageAdapter;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Bundle bundle;
    private EditText editNameItem;
    private EditText editPriceItem;
    private ImageView editImageItem;
    private Button btnSave;
    private Button btnDelete;
    private Button btnCancel;
    private Button btnUpdate;
    private Button btnUpLoadImage;
    private List<Item> itemList;
    private Picasso picasso = Picasso.get();
    private boolean isUpdate = false;
    private RecyclerView recyclerView;
    private Integer idItemUpdate;
    private String imageUrlUpload = null;
    private String nameItem = null;
    private View myView;
    private Button btnSearchItem;
    private EditText findItemByNameEditText;

    DatabaseReference reference= FirebaseDatabase.
            getInstance("https://amplified-coder-384315-default-rtdb.firebaseio.com/").
            getReference("my_shop");
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.manage_item_fragment, container, false);
        myView = view;
        btnSearchItem = view.findViewById(R.id.btn_search_product_admin);
        findItemByNameEditText = view.findViewById(R.id.edit_name_product_search_admin);
        editNameItem = view.findViewById(R.id.editNameItem);
        editImageItem = view.findViewById(R.id.editImageItem);
        editPriceItem = view.findViewById(R.id.editPriceItem);
        recyclerView = view.findViewById(R.id.item_manage);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        btnCancel = view.findViewById(R.id.btnCancel);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnSave = view.findViewById(R.id.btnSave);
        btnUpLoadImage = view.findViewById(R.id.btnUploadImage);
        btnUpdate.setEnabled(false);
        User user = (User) getArguments().get("object_user");
        itemList = new ArrayList<>();

        storage = FirebaseStorage.getInstance("gs://amplified-coder-384315.appspot.com");
        storageReference = storage.getReference();
        editNameItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isUpdate) {
                    btnUpdate.setEnabled(false);
                    btnCancel.setEnabled(true);
                    btnSave.setEnabled(true);
                }
            }
        });
        editPriceItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isUpdate) {
                    btnUpdate.setEnabled(false);
                    btnCancel.setEnabled(true);
                    btnSave.setEnabled(true);
                }

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Thông báo xóa");
                builder.setMessage("Bạn có chắc muốn hủy không");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editNameItem.setText("");
                        editPriceItem.setText("");
                        editImageItem.setImageResource(R.drawable.ic_blank_user);
                        btnCancel.setEnabled(false);
                        btnSave.setEnabled(false);
                        btnUpdate.setEnabled(false);
                        idItemUpdate = null;
                        isUpdate = false;
                        Toast.makeText(getActivity().getApplicationContext(), "Xoa Thanh Cong", Toast.LENGTH_SHORT).show();
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
        reference.child("Drinks").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    String image = itemSnapshot.child("image").getValue(String.class);
                    String name = itemSnapshot.child("name").getValue(String.class);
                    Integer price = itemSnapshot.child("price").getValue(Integer.class);
                    Integer id = itemSnapshot.child("id").getValue(Integer.class);
                    Item item = new Item();
                    item.setPrice(price);
                    item.setName(name);
                    item.setPicId(image);
                    item.setId(id);
                    itemList.add(item);
                }
                Collections.reverse(itemList);
                itemManageAdapter = new ItemManageAdapter(itemList, getContext());
                itemManageAdapter.setItemListener(FragmentManageItem.this);
                // Set the RecyclerView adapter to be the OrderAdapter
                recyclerView.setAdapter(itemManageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editNameItem.getText().toString();
                Integer price = (editPriceItem.getText().toString() == null || editPriceItem.getText().toString().length() == 0) ? null : Integer.parseInt(editPriceItem.getText().toString());
                if(name == null || price == null || imageUrlUpload == null) {
                    if (name == null || name.length() == 0) {
                        editNameItem.setError("please enter name drink");
                    } else if (price == null) {
                        editPriceItem.setError("please enter price drink");
                    }
                    Toast.makeText(getActivity().getApplicationContext(), "Please fill fully information", Toast.LENGTH_LONG).show();
                }else{
                    reference.child("Drinks").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Long count = snapshot.getChildrenCount();
                            count += 1;
                            reference.child("Drinks").child(count + "").child("id").setValue(count);
                            reference.child("Drinks").child(count + "").child("name").setValue(name);
                            reference.child("Drinks").child(count + "").child("image").setValue(imageUrlUpload);
                            reference.child("Drinks").child(count + "").child("price").setValue(price);
                            nameItem = name;
                            Item item = new Item();
                            item.setId(Math.toIntExact(count));
                            item.setName(name);
                            item.setPicId(imageUrlUpload);
                            item.setPrice(price);
                            itemList.add(item);
                            itemManageAdapter = new ItemManageAdapter(itemList, getContext());
                            itemManageAdapter.setItemListener(FragmentManageItem.this);
                            // Set the RecyclerView adapter to be the OrderAdapter
                            recyclerView.setAdapter(itemManageAdapter);
                            editImageItem.setImageResource(R.drawable.ic_blank_user);
                            editNameItem.setText("");
                            editPriceItem.setText("");
                            Toast.makeText(getContext(), "Success update", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idItemUpdate != null) {
                    String name = editNameItem.getText().toString();
                    Integer price = Integer.parseInt(editPriceItem.getText().toString());
                    if(name == null || price == null || imageUrlUpload == null) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please fill fully information", Toast.LENGTH_LONG).show();
                    } else {
                        reference.child("drink").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                reference.child("Drinks").child(idItemUpdate + "").child("name").setValue(name);
                                reference.child("Drinks").child(idItemUpdate + "").child("image").setValue(imageUrlUpload);
                                reference.child("Drinks").child(idItemUpdate + "").child("price").setValue(price);
                                editImageItem.setImageResource(R.drawable.ic_blank_user);
                                editNameItem.setText("");
                                editPriceItem.setText("");
                                Toast.makeText(getContext(), "Success update", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }
            }
        });

        btnUpLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editNameItem.getText() == null || editNameItem.getText().toString().length() < 4) {
                    Toast.makeText(getContext(), "Name of item not empty and must be length > 4", Toast.LENGTH_LONG).show();
                } else {
                    nameItem = editNameItem.getText().toString();
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, PICK_IMAGE_REQUEST);
                }
            }
        });

        btnSearchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = findItemByNameEditText.getText().toString();
                if(name != null) {
                    System.out.println("name      " + name);
                    List<Item> listItemSearchByName = new ArrayList<>();
                    for (Item item : itemList) {
                        if (item.getName().contains(name)) {
                            listItemSearchByName.add(item);
                        }
                    }
                    itemManageAdapter = new ItemManageAdapter(listItemSearchByName, getContext());
                    itemManageAdapter.setItemListener(FragmentManageItem.this);
                    // Set the RecyclerView adapter to be the OrderAdapter
                    recyclerView.setAdapter(itemManageAdapter);
                } else {
                    itemManageAdapter = new ItemManageAdapter(itemList, getContext());
                    itemManageAdapter.setItemListener(FragmentManageItem.this);
                    // Set the RecyclerView adapter to be the OrderAdapter
                    recyclerView.setAdapter(itemManageAdapter);
                }
            }
        });


        return view;
    }
    public void updateData() {
        try{
            if(myView != null) {
                itemList = new ArrayList<>();
                reference.child("Drinks").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                            String image = itemSnapshot.child("image").getValue(String.class);
                            String name = itemSnapshot.child("name").getValue(String.class);
                            Integer price = itemSnapshot.child("price").getValue(Integer.class);
                            Integer id = itemSnapshot.child("id").getValue(Integer.class);
                            Item item = new Item();
                            item.setPrice(price);
                            item.setName(name);
                            item.setPicId(image);
                            item.setId(id);
                            itemList.add(item);
                        }
                        Collections.reverse(itemList);
                        itemManageAdapter = new ItemManageAdapter(itemList, getContext());
                        itemManageAdapter.setItemListener(FragmentManageItem.this);
                        recyclerView = myView.findViewById(R.id.item_manage);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        // Set the RecyclerView adapter to be the OrderAdapter
                        recyclerView.setAdapter(itemManageAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }


        } catch (Exception e) {

        }

    }

    @Override
    public void onItemEditListener(View view, int id) {
        for(Item item : itemList) {
            if(item.getId() == id) {
                editNameItem.setText(item.getName());
                editPriceItem.setText(item.getPrice() + "");
                picasso.load(item.getPicId()).into(editImageItem);
                btnSave.setEnabled(false);
                btnUpdate.setEnabled(true);
                btnCancel.setEnabled(true);
                idItemUpdate = id;
                imageUrlUpload = item.getPicId();
                isUpdate = true;
                break;
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            editImageItem.setImageURI(imageUri);
            User user = (User) getArguments().get("object_user");

            // Get the filename and path of the selected image
            String filename = "avatar.jpg";
            nameItem = nameItem.toLowerCase().replaceAll("\\s+", "-");
            String path = "Drink/" + nameItem;
            // Upload the selected image to Firebase Storage
            StorageReference imageRef = storageReference.child(path);
            UploadTask uploadTask = imageRef.putFile(imageUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Image uploaded successfully
                    // Get the download URL of the uploaded image
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString();
                            imageUrlUpload = imageUrl;
                            Log.d("FragmentAds", "Image URL: " + imageUrl);

                            // You can now update the user's avatar URL in your Firebase Realtime Database or Firestore database
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Image upload failed
                    Log.e("FragmentAds", "Error uploading image: " + e.getMessage());
                }
            });
        }
    }
}
