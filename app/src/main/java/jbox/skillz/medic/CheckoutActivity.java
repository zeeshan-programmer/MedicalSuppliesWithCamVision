package jbox.skillz.medic;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import jbox.skillz.medic.Model.Order;

public class CheckoutActivity extends AppCompatActivity {
      Button place_order, codBtn, pickupBtn;

      Button trackOrder, homeBtn;

      TextView orderID;
      TextView totalPrice, discountPrice, shippingPrice, grandPrice;
      EditText checkoutAddress, checkoutMobile;

      String strItems = "";
      String paymentType = "Cash on Delivery";

      String chkStatus = "yes";

      String uid;
      DatabaseReference databaseReference;
      DatabaseReference databaseReferenceProfile;
      DatabaseReference databaseReferenceMyOrders;
      DatabaseReference databaseReferenceAllOrders;

      Dialog dialog;


      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_checkout);
            place_order=findViewById(R.id.place_order);
            codBtn =findViewById(R.id.cod_btn);
            pickupBtn =findViewById(R.id.pickup_btn);

            totalPrice = findViewById(R.id.total_price);
            discountPrice = findViewById(R.id.discount_price);
            shippingPrice = findViewById(R.id.shipping_price);
            grandPrice = findViewById(R.id.grand_price);

            checkoutAddress = findViewById(R.id.checkout_address);
            checkoutMobile = findViewById(R.id.checkout_mobile);

            Intent intent = getIntent();
            dialog = new Dialog(CheckoutActivity.this);

            totalPrice.setText(intent.getStringExtra("price"));
            strItems = intent.getStringExtra("items");
            grandPrice.setText(intent.getStringExtra("grandprice"));
            discountPrice.setText(intent.getStringExtra("discountprice"));
            shippingPrice.setText(intent.getStringExtra("shippingprice"));

            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            uid = firebaseUser.getUid();

            databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child(uid);
            databaseReferenceMyOrders = FirebaseDatabase.getInstance().getReference("MyOrders").child(uid);
            databaseReferenceAllOrders = FirebaseDatabase.getInstance().getReference("AllOrders");

            databaseReferenceProfile = FirebaseDatabase.getInstance().getReference().child("Accounts").child(uid);
            databaseReferenceProfile.addValueEventListener(new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        String loc = dataSnapshot.child("location").getValue().toString();
                        String mobile = dataSnapshot.child("mobile").getValue().toString();

                        checkoutAddress.setText(loc);
                        checkoutMobile.setText(mobile);

                  }
                  @Override
                  public void onCancelled(@NonNull DatabaseError databaseError) {

                  }
            });

            pickupBtn.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        pickupBtn.setBackgroundResource(R.drawable.button_blue);
                        codBtn.setBackgroundResource(R.drawable.button_white);
                        paymentType = "Self Pick-Up";
                  }
            });

            codBtn.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        codBtn.setBackgroundResource(R.drawable.button_blue);
                        pickupBtn.setBackgroundResource(R.drawable.button_white);
                        paymentType = "Cash on Delivery";
                  }
            });

            place_order.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {

                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa");
                        String datetime = dateformat.format(c.getTime());

                        String key1 = databaseReferenceMyOrders.push().getKey();
                        // String key2 = databaseReferenceAllOrders.push().getKey();

                        HashMap<String,String> orderMap = new HashMap<>();
                        orderMap.put("items",strItems);
                        orderMap.put("time",datetime);
                        orderMap.put("total", grandPrice.getText().toString());
                        orderMap.put("status","processing");
                        orderMap.put("user id", uid);
                        orderMap.put("order id", key1);
                        orderMap.put("payment", paymentType);
                        orderMap.put("address", checkoutAddress.getText().toString());
                        orderMap.put("mobile", checkoutMobile.getText().toString());

                        Order order = new Order(""+strItems, ""+grandPrice.getText().toString(), ""+datetime, "processing",
                                ""+uid, ""+key1, ""+paymentType, ""+checkoutAddress.getText().toString(),
                                ""+checkoutMobile.getText().toString());

                        databaseReferenceMyOrders.child(key1).setValue(order);
                        databaseReferenceAllOrders.child(key1).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                              @Override
                              public void onComplete(@NonNull Task<Void> task) {
                                    databaseReference.removeValue();
                                    if (chkStatus.equals("yes")){
                                          OrderInvoic(""+key1, "processing", ""+paymentType, ""+checkoutAddress.getText().toString());
                                          chkStatus = "no";
                                    }
                                    else{
                                          Toast.makeText(CheckoutActivity.this, "Ordrer Is Placed", Toast.LENGTH_LONG).show();
                                    }
                              }
                        });

                }
            });
            getSupportActionBar().setTitle("Checkout");
      }

      void OrderInvoic(String orderId, String status, String payment, String address)
      {
            Dialog dialog = new Dialog(CheckoutActivity.this);
            dialog.setContentView(R.layout.invoice_page);
            dialog.setCanceledOnTouchOutside(false);
            dialog.onBackPressed();

            trackOrder = dialog.findViewById(R.id.track_order_btn);
            homeBtn = dialog.findViewById(R.id.back_to_home_btn);
            orderID = dialog.findViewById(R.id.order_id_tv);
            orderID.setText(orderId);

            homeBtn.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        Intent i=new Intent(CheckoutActivity.this,HomeActivity.class);
                        startActivity(i);
                        finish();
                  }
            });
            trackOrder.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        Intent i=new Intent(CheckoutActivity.this,TrackingActivity.class);
                        i.putExtra("ordid", orderId);
                        i.putExtra("status", status);
                        i.putExtra("payment", payment);
                        i.putExtra("address", address);
                        startActivity(i);
                        finish();
                  }
            });

            dialog.show();
      }

      @Override
      public void onBackPressed() {
            super.onBackPressed();
            dialog.dismiss();
            this.finish();
      }
}
