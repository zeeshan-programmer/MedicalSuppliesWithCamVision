package jbox.skillz.medic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.droidnet.DroidListener;
import com.droidnet.DroidNet;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jbox.skillz.medic.Model.Cart;

public class CartActivity extends AppCompatActivity implements DroidListener {

      DroidNet mDroidNet;
      String chkConn;

      String uid;

      public int totalPrice = 0;
      public int grandPrice = 0;
      public int discountPrice = 0;
      public int shippingPrice = 200;

      LinearLayout grandLayout;

      RecyclerView cartRecyclerView;
      CartAdapter adapter;
      ArrayList<Cart> cartArrayList;

      DatabaseReference databaseReference;

      Button checkoutBtn;

      CardView listCard;

      ProgressDialog mProgress;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_cart);

            getSupportActionBar().setTitle("Cart");

            DroidNet.init(this);
            mDroidNet = DroidNet.getInstance();
            mDroidNet.addInternetConnectivityListener(this);

            cartArrayList = new ArrayList<>();

            grandLayout = findViewById(R.id.grand_layout);

            checkoutBtn = findViewById(R.id.cart_next_btn);

            listCard = findViewById(R.id.list_section);

            cartRecyclerView = findViewById(R.id.cart_list);
            cartRecyclerView.setHasFixedSize(true);
            cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            uid = firebaseUser.getUid();

            databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child(uid);

            adapter = new CartAdapter(this, cartArrayList);
            cartRecyclerView.setAdapter(adapter);

            databaseReference.addValueEventListener(new ValueEventListener(){

                  @Override
                  public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                              Cart cart = dataSnapshot.getValue(Cart.class);
                              cartArrayList.add(cart);
                        }
                        adapter.notifyDataSetChanged();
                        if(cartArrayList.size() == 0)
                        {
                              grandLayout.setVisibility(View.GONE);
                              Toast.makeText(CartActivity.this, "Cart Is Empity", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                              grandLayout.setVisibility(View.VISIBLE);
                        }
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError error) {

                  }
            });

      }

      @Override
      protected void onStart() {
            super.onStart();

            totalPrice = 0;
            grandPrice = 0;
            discountPrice = 0;
            shippingPrice = 200;

            checkoutBtn.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {

                        for (int i = 0; i<cartArrayList.size(); i++)
                        {
                              Cart cart = cartArrayList.get(i);
                              totalPrice = totalPrice + Integer.valueOf(cart.getTotalPrice().toString());
                        }

                        if (totalPrice >= 1000)
                        {
                              discountPrice = 200;
                              shippingPrice = 0;
                              grandPrice = totalPrice-discountPrice;
                        }
                        else
                        {
                              shippingPrice = 200;
                              grandPrice = totalPrice+shippingPrice;
                        }

                        String items = "";
                        for (int i = 0; i<cartArrayList.size(); i++)
                        {
                              Cart cart = cartArrayList.get(i);
                              items = items + "("+ (i+1) + ") " + cart.getName() +
                                      "\n\t\t\t Quantity: " + cart.getQuantity() +
                                      "\n\t\t\t Price: " + cart.getTotalPrice() + "\n";
                        }

                        Intent i = new Intent(getApplicationContext(), CheckoutActivity.class);
                        i.putExtra("price", ""+totalPrice);
                        i.putExtra("items", ""+items);
                        i.putExtra("grandprice", ""+grandPrice);
                        i.putExtra("discountprice", ""+discountPrice);
                        i.putExtra("shippingprice", ""+shippingPrice);
                        startActivity(i);
                        finish();

                  }
            });

      }

      @Override
      public void onBackPressed() {
            super.onBackPressed();
            finish();
      }

      @Override
      public void onInternetConnectivityChanged(boolean isConnected) {
            if (isConnected) {
                  chkConn = "yes";
            } else {
                  chkConn = "no";
            }
      }

      public static class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {


            Context context;
            ArrayList<Cart> cartArrayList;
            ArrayList<Cart> cartArrayListtt;

            public CartAdapter(Context context, ArrayList<Cart> cartArrayList) {
                  this.context = context;
                  this.cartArrayList = cartArrayList;
                  this.cartArrayListtt = cartArrayList;
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                  View v = LayoutInflater.from(context).inflate(R.layout.cart_list_layout, parent, false);
                  return new CartViewHolder(v);

            }

            @Override
            public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

                  Cart cart = cartArrayListtt.get(position);
                  int itemPrice = Integer.valueOf(cart.getPrice());
                  int pos = position;

                  holder.cartItemName.setText(cart.getName());
                  holder.cartItemTotalPrice.setText(cart.getPrice());
                  holder.cartItemPrice.setText(cart.getPrice());
                  holder.cartItemQuantity.setText(cart.getQuantity());

                  Picasso.get().load(cart.getImage()).placeholder(R.drawable.tblts).into(holder.cartItemImg);

                  holder.cartItemBtn.setOnClickListener(new ElegantNumberButton.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                              holder.cartItemQuantity.setText(holder.cartItemBtn.getNumber());
                              holder.cartItemTotalPrice.setText(""+itemPrice*(Integer.valueOf(holder.cartItemBtn.getNumber())));

                              cartArrayList.remove(pos);
                              cartArrayList.add(pos, new Cart(""+holder.cartItemName.getText(), ""+holder.cartItemPrice.getText(), ""+holder.cartItemTotalPrice.getText(),
                                      ""+cart.getImage(),
                                      ""+holder.cartItemQuantity.getText()));
                        }
                  });

                  holder.cartItemImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                              Intent i = new Intent(context, ProductDetails.class);
                              i.putExtra("name", holder.cartItemName.getText());
                              i.putExtra("price", holder.cartItemPrice.getText());
                              i.putExtra("image", cart.getImage());
                              i.putExtra("description", cart.getDescription());
                              i.putExtra("quant", holder.cartItemQuantity.getText());
                              context.startActivity(i);
                        }
                  });

            }

            @Override
            public int getItemCount() {
                  return cartArrayList.size();
            }

            public static class CartViewHolder extends RecyclerView.ViewHolder{

                  TextView cartItemName, cartItemPrice, cartItemTotalPrice, cartItemQuantity;

                  ElegantNumberButton cartItemBtn;

                  ImageView cartItemImg;

                  public CartViewHolder(@NonNull View itemView) {
                        super(itemView);

                        cartItemName = itemView.findViewById(R.id.cart_list_layout_name);
                        cartItemPrice = itemView.findViewById(R.id.cart_list_layout_price);
                        cartItemTotalPrice = itemView.findViewById(R.id.cart_list_layout_total_price);
                        cartItemImg = itemView.findViewById(R.id.cart_list_layout_img);
                        cartItemQuantity = itemView.findViewById(R.id.cart_list_layout_quantity);

                        cartItemBtn = itemView.findViewById(R.id.num_btn);

                  }
            }

      }

}
