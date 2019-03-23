package com.example.shoppingcart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shoppingcart.Model.Products;
import com.example.shoppingcart.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    private ImageView productImage;
    private TextView productName, productDescription, productQuantity, productCurrency;
    private EditText userQuantity;
    private int userInput;
    private Button addToC, goToC;
    private String userId = Paper.book().read(Prevalent.UserPhoneKey);

    private static final String TAG = "ProductDetailsActivity";
    private DatabaseReference mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Intent intent = getIntent();
        Bundle productData = intent.getExtras();
        final Products product = (Products) productData.getParcelable("productDetails");
        Log.d(TAG, product.toString());

        addToC = (Button) findViewById(R.id.add_to_cart_btn);
        goToC = (Button) findViewById(R.id.go_to_cart_btn);
        userQuantity = (EditText) findViewById(R.id.user_quantity_input);
        productImage = (ImageView) findViewById(R.id.detailsimage);
        Glide.with(this).asBitmap().load(product.getProductPicUrl()).into(productImage);

        productName = (TextView) findViewById(R.id.product_name);
        productDescription = (TextView) findViewById(R.id.product_description);
        productQuantity = (TextView) findViewById(R.id.product_quantity);
        productCurrency = (TextView) findViewById(R.id.product_currency);

//        System.out.println(product.getName());
        System.out.println(userId);
        productName.setText(product.getName());
        productDescription.setText(product.getDescription());
        productQuantity.setText("Quantity Available: " + String.valueOf(product.getQuantity()));
        String priceQuote = product.getCurrencyCode() + product.getPrice();
        productCurrency.setText(priceQuote);

        addToC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDb = FirebaseDatabase.getInstance().getReference();
                mDb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (product.getQuantity().compareTo(Long.parseLong("0")) <= 0) {
                            Toast.makeText(ProductDetailsActivity.this, "Out of stock currently", Toast.LENGTH_LONG).show();
                            return;
                        }
                        else if (!dataSnapshot.child("Users").child(userId).child("CartInformation").exists()) {
                            Log.d("ADDTOCART", "Reached IF Loop");
                            if (userQuantity.getText().toString() == "") {
                                Toast.makeText(ProductDetailsActivity.this, "Please enter a value in quantity field.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            userInput = Integer.parseInt(userQuantity.getText().toString());
                            if (userInput > product.getQuantity()) {
                                Toast.makeText(ProductDetailsActivity.this, "Are you blind??? See the available quantity", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            HashMap<String, Integer> productInformation = new HashMap<>();
                            productInformation.put(product.getProductId(), userInput);
                            HashMap<String, Object> userCartData = new HashMap<>();
                            userCartData.put("CartInformation",productInformation);
                            mDb.child("Users").child(userId).updateChildren(userCartData)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ProductDetailsActivity.this, "Your product has been added to cart", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(ProductDetailsActivity.this, "Your product has not been added to cart", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            if (!dataSnapshot.child("Users").child(userId).child("CartInformation").child(product.getProductId()).exists()) {
                                if (userQuantity.getText().toString() == "") {
                                    Toast.makeText(ProductDetailsActivity.this, "Please enter a value in quantity field.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                userInput = Integer.parseInt(userQuantity.getText().toString());
                                if (userInput > product.getQuantity()) {
                                    System.out.println("Reched Line 87");
                                    Toast.makeText(ProductDetailsActivity.this, "Are you blind??? See the available quantity", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                HashMap<String, Object> cartData = new HashMap<>();
                                cartData.put(product.getProductId(), userInput);
                                mDb.child("Users").child(userId).child("CartInformation").updateChildren(cartData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ProductDetailsActivity.this, "Your product has been added to cart", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(ProductDetailsActivity.this, "Your product has not been added to cart", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                for (DataSnapshot ds : dataSnapshot.child("Users").child(userId).child("CartInformation").getChildren()) {
                                    if (ds.getKey().equals(product.getProductId())) {
                                        Long presentQuantity = ds.getValue(Long.class);
                                        userInput = Integer.parseInt(userQuantity.getText().toString()) + Integer.parseInt(presentQuantity.toString());
                                        if (userQuantity.getText().toString() == "") {
                                            Toast.makeText(ProductDetailsActivity.this, "Please enter a value in quantity field.", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        else if (userInput > product.getQuantity()) {
                                            Toast.makeText(ProductDetailsActivity.this, "You may not add those many quantities, as the quantity you add may exceed the  available limit", Toast.LENGTH_SHORT).show();
                                            mDb.child("Users").child(userId).child("CartInformation").child(ds.getKey()).removeValue();
                                        } else {
                                            HashMap<String, Object> cartData = new HashMap<>();
                                            cartData.put(product.getProductId(), userInput);
                                            mDb.child("Users").child(userId).child("CartInformation").updateChildren(cartData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(ProductDetailsActivity.this, "Your product has been added to cart", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(ProductDetailsActivity.this, "Your product has not been added to cart", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        goToC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailsActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

    }
}
