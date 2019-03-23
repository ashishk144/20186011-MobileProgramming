package com.example.shoppingcart;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shoppingcart.Model.Products;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileReader;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ProductViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";
    private DatabaseReference mDb;
    private ArrayList<String> mProductNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(ArrayList<String> mProductNames, ArrayList<String> mImages, Context mContext) {
        this.mProductNames = mProductNames;
        this.mImages = mImages;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item, parent, false);
        ProductViewHolder holder = new ProductViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, final int position) {
        Log.d(TAG, "OnBindView Holder: Called");

        Glide.with(mContext).asBitmap().load(mImages.get(position)).into(holder.image);
        holder.name.setText(mProductNames.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            Products product;
            @Override
            public void onClick(View v) {
                mDb = FirebaseDatabase.getInstance().getReference("ProductCollection");

                mDb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        product = dataSnapshot.child(position+"").getValue(Products.class);
                        Log.d(TAG, product.toString());
                        Intent intent = new Intent(mContext, ProductDetailsActivity.class);
                        intent.putExtra("productDetails", (Parcelable) product);
                        mContext.startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProductNames.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TextView name;
        RelativeLayout parentLayout;

        public ProductViewHolder(View itemView) {
             super(itemView);
             image = (CircleImageView) itemView.findViewById(R.id.image);
             name = (TextView) itemView.findViewById(R.id.name);
             parentLayout = (RelativeLayout) itemView.findViewById(R.id.parent_layout);
        }
    }
}
