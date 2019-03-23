package com.example.shoppingcart;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingcart.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;

public class CartRVA extends RecyclerView.Adapter<CartRVA.CartViewHolder> {

    private static final String TAG = "CartRVA";

    private Button delete;

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Long> mPrice = new ArrayList<>();
    private ArrayList<Long> mQuantity = new ArrayList<>();
    private ArrayList<String> prodId = CartActivity.mProdId;
    private Context mContext;
    private DatabaseReference mDb;
    private String userId = Paper.book().read(Prevalent.UserPhoneKey);

    public CartRVA(ArrayList<String> mNames, ArrayList<Long> mPrice, ArrayList<Long> mQuantity, Context mContext) {
        Log.d(TAG, "CartRVA: In Constructor");
        this.mNames = mNames;
        this.mPrice = mPrice;
        this.mQuantity = mQuantity;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG,"In OnCreate");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_item, parent,false);
        CartViewHolder holder =  new CartViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, final int position) {
        Log.d(TAG,"onBind");
        holder.name.setText(mNames.get(position));
        holder.price.setText(mPrice.get(position).toString());
        holder.quantity.setText(mQuantity.get(position).toString());

        mDb = FirebaseDatabase.getInstance().getReference("Users");
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.child(userId).child("CartInformation").getChildren()) {

                            if (ds.getKey().equals(prodId.get(position))) {
                                System.out.println("Reached inside if loop");
                                    mDb.child(userId).child("CartInformation").child(ds.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(mContext, "Item removed from database", Toast.LENGTH_SHORT).show();
                                                Intent intent =new Intent(mContext,CartActivity.class);
                                                mContext.startActivity(intent);
                                            }
                                        }
                                    });
                            }
                        }

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
        return mNames.size();
    }


    public class CartViewHolder extends RecyclerView.ViewHolder {

        TextView name, quantity, price;
        RelativeLayout cartLayout;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_cart_name);
            quantity = itemView.findViewById(R.id.tv_cart_quantity);
            price = itemView.findViewById(R.id.tv_cart_price);
            delete = itemView.findViewById(R.id.delete_btn);
            cartLayout = itemView.findViewById(R.id.cart_layout);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
