package jbox.skillz.medic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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

public class ProductDetails extends AppCompatActivity {

      ImageView detImage;
      TextView detName, detPrice, detDescription;
      Button detAddBtn, detAddedBtn;

      ArrayList<Cart> cartArrayList;

      String uid;
      DatabaseReference databaseReference;

      String strImg;
      String strQuant;


      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_product_details);

            getSupportActionBar().setTitle("Product Details");

            detImage = findViewById(R.id.det_img);
            detPrice = findViewById(R.id.det_price);
            detName = findViewById(R.id.det_name);
            detDescription = findViewById(R.id.det_desc);
            detAddBtn = findViewById(R.id.det_add_btn);

            detAddedBtn = findViewById(R.id.det_added_btn);

            Intent intent = getIntent();

            detName.setText(intent.getStringExtra("name"));
            detPrice.setText(intent.getStringExtra("price"));
            detDescription.setText(intent.getStringExtra("description"));
            strImg = intent.getStringExtra("image");
            strQuant = intent.getStringExtra("quant");

            Picasso.get().load(strImg).placeholder(R.drawable.tblts).into(detImage);

            cartArrayList = new ArrayList<>();
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            uid = firebaseUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child(uid);

      }

      @Override
      protected void onStart() {
            super.onStart();

            databaseReference.addValueEventListener(new ValueEventListener(){
                  @Override
                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChildren())
                        {
                              for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Cart cart = dataSnapshot.getValue(Cart.class);
                                    cartArrayList.add(cart);
                              }
                        }
                  }
                  @Override
                  public void onCancelled(@NonNull DatabaseError error) {
                  }
            });

            detAddBtn.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        cartArrayList.add(new Cart(""+detName.getText().toString(), ""+detPrice.getText().toString(),
                                ""+detPrice.getText().toString(),""+strImg, ""+strQuant,
                                ""+detDescription.getText().toString()));
                        databaseReference.setValue(cartArrayList).addOnCompleteListener(new OnCompleteListener<Void>() {
                              @Override
                              public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(ProductDetails.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), CartActivity.class);
                                    startActivity(i);
                                    finish();
                              }
                        }).addOnFailureListener(new OnFailureListener() {
                              @Override
                              public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ProductDetails.this, "Network Error", Toast.LENGTH_SHORT).show();
                              }
                        });
                  }
            });
      }
}