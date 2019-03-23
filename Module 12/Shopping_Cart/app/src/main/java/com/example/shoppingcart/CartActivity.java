package com.example.shoppingcart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingcart.Model.Users;
import com.example.shoppingcart.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity{

    private DatabaseReference mDb, mDbP;
    private TextView totalPriceView;
    private Button placeOrder;
    Long totalPrice = Long.parseLong("0");
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Long> mPrices = new ArrayList<>();
    public static ArrayList<String> mProdId = new ArrayList<>();
    private ArrayList<Long> mQuantity = new ArrayList<>();
    String userId = Paper.book().read(Prevalent.UserPhoneKey);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mDb = FirebaseDatabase.getInstance().getReference("Users");
        mDbP = FirebaseDatabase.getInstance().getReference("ProductCollection");
        placeOrder = (Button) findViewById(R.id.place_order);
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(userId).child("CartInformaation").exists()) {
                            Toast.makeText(CartActivity.this, "No items in the cart to place order", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(CartActivity.this,"Your order has been placed", Toast.LENGTH_LONG).show();
                            changeQuantityInDatabase();
                            Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                            startActivity(intent);
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
    protected void onStart() {
        super.onStart();
        mDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot ds: dataSnapshot.child(userId).child("CartInformation").getChildren()) {
                    mDbP.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds1:dataSnapshot.getChildren()) {
                                if (ds.getKey().equals(ds1.child("ProductId").getValue())) {
                                    mNames.add(ds1.child("Name").getValue(String.class));
                                    Long price = ds1.child("Price").getValue(Long.class);
                                    Long quantity = ds.getValue(Long.class);
                                    totalPrice += (price * quantity);
                                    mQuantity.add(quantity);
                                    mPrices.add(price);
                                    mProdId.add(ds.getKey());
//                                    System.out.println(totalPrice);
//                                    System.out.println(mPrices);
//                                    System.out.println(mNames);
                                }
                            }
                            initRecyclerView();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void changeQuantityInDatabase() {

        mDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot ds: dataSnapshot.child(userId).child("CartInformation").getChildren()) {
                    mDbP.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds1:dataSnapshot.getChildren()) {

                                if (ds.getKey().equals(ds1.child("ProductId").getValue())) {
                                    Long decreaseQuantity = Long.parseLong(ds1.child("Quantity").getValue().toString()) - Long.parseLong("1");
                                    mDbP.child(ds1.getKey()).child("Quantity").setValue(decreaseQuantity).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) { Toast.makeText(CartActivity.this, "Quantity decreased", Toast.LENGTH_SHORT).show();
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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void initRecyclerView() {
//        System.out.println("Line.no: 76");
        RecyclerView recyclerView = findViewById(R.id.rv_cart);
        CartRVA adapter = new CartRVA(mNames, mPrices, mQuantity, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        totalPriceView = (TextView) findViewById(R.id.total_price);
        totalPriceView.setText("Total Price: " + totalPrice.toString());

    }
}
