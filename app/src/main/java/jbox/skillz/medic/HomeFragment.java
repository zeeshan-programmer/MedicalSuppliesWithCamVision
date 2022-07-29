package jbox.skillz.medic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jbox.skillz.medic.Adapter.HomeSearchAdapter;
import jbox.skillz.medic.Model.Cart;
import jbox.skillz.medic.Model.Order;
import jbox.skillz.medic.Model.Products;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class HomeFragment extends Fragment {


      View homeView;

      TextView nameTv;
      ImageButton uploadBtn;
      Button cartBtn;
      RecyclerView homeRecyclerView;

      EditText searchView;

      DatabaseReference databaseReference;
      DatabaseReference databaseReferenceInventory;

      String uid = "";

      HomeAdapter adapter;
      ArrayList<Products> productsArrayList;



      // TODO: Rename parameter arguments, choose names that match
      // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
      private static final String ARG_PARAM1 = "param1";
      private static final String ARG_PARAM2 = "param2";

      // TODO: Rename and change types of parameters
      private String mParam1;
      private String mParam2;

      public HomeFragment() {
            // Required empty public constructor
      }

      /**
       * Use this factory method to create a new instance of
       * this fragment using the provided parameters.
       *
       * @param param1 Parameter 1.
       * @param param2 Parameter 2.
       * @return A new instance of fragment HomeFragment.
       */
      // TODO: Rename and change types and number of parameters
      public static HomeFragment newInstance(String param1, String param2) {
            HomeFragment fragment = new HomeFragment();
            Bundle args = new Bundle();
            args.putString(ARG_PARAM1, param1);
            args.putString(ARG_PARAM2, param2);
            fragment.setArguments(args);
            return fragment;
      }

      @Override
      public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            if (getArguments() != null) {
                  mParam1 = getArguments().getString(ARG_PARAM1);
                  mParam2 = getArguments().getString(ARG_PARAM2);
            }
      }

      @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState) {
            // Inflate the layout for this fragment

            homeView = inflater.inflate(R.layout.fragment_home, container, false);

            uploadBtn = homeView.findViewById(R.id.upload_btn);
            cartBtn = homeView.findViewById(R.id.cart_btn);
            nameTv = homeView.findViewById(R.id.name_tv);

            searchView = homeView.findViewById(R.id.search_view);

            homeRecyclerView = homeView.findViewById(R.id.bestSellerRecyclerview);

            homeRecyclerView.setHasFixedSize(true);
            homeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext() , LinearLayoutManager.HORIZONTAL , false));


            return homeView;
      }

      @Override
      public void onStart() {
            super.onStart();

            uploadBtn.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        Intent i = new Intent(getContext(), UploadPrescription.class);
                        startActivity(i);
                  }
            });

            cartBtn.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        Intent i = new Intent(getContext(), CartActivity.class);
                        startActivity(i);
                  }
            });

            FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();

            if (current_user != null)
            {
                  uid = current_user.getUid();
                  databaseReference = FirebaseDatabase.getInstance().getReference().child("Accounts").child(uid);
                  databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                              String username = dataSnapshot.child("name").getValue().toString();
                              nameTv.setText(username);

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                  });
            }

            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            uid = firebaseUser.getUid();

            databaseReferenceInventory = FirebaseDatabase.getInstance().getReference("Inventory");

            productsArrayList = new ArrayList<>();


            adapter = new HomeAdapter(getContext(), productsArrayList);
            homeRecyclerView.setAdapter(adapter);

            databaseReferenceInventory.addValueEventListener(new ValueEventListener(){

                  @Override
                  public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                              Products products = dataSnapshot.getValue(Products.class);
                              productsArrayList.add(products);
                        }
                        adapter.notifyDataSetChanged();
                        if(productsArrayList.size() == 0)
                        {
                              Toast.makeText(getContext(), "Product list is empity", Toast.LENGTH_LONG).show();
                        }
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError error) {

                  }
            });

            searchView.addTextChangedListener(new TextWatcher() {
                  @Override
                  public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                  }

                  @Override
                  public void onTextChanged(CharSequence s, int start, int before, int count) {

                  }

                  @Override
                  public void afterTextChanged(Editable s) {
                        if (!s.toString().isEmpty())
                        {
                              search(s.toString());
                        }
                        else
                        {
                              search("");
                        }
                  }
            });

      }

      public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder>{

            Context context;
            ArrayList<Products> homeArraylist;

            public HomeAdapter(Context context, ArrayList<Products> homeArraylist) {
                  this.context = context;
                  this.homeArraylist= homeArraylist;
            }

            @NonNull
            @Override
            public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                  View v = LayoutInflater.from(context).inflate(R.layout.products_layout, parent, false);
                  return new HomeViewHolder(v);
            }

            @Override
            public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {

                  Products products = productsArrayList.get(position);

                  holder.homeItemName.setText(products.getName());
                  holder.homeItemPrice.setText(products.getPrice());

                  String strImg = products.getImage();

//                  holder.homeItemImg.setImageResource(products.getImage());
                  Picasso.get().load(strImg).placeholder(R.drawable.tblts).into(holder.homeItemImg);
                  holder.homeItemImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                              Intent i = new Intent(getContext(), ProductDetails.class);
                              i.putExtra("name", products.getName());
                              i.putExtra("price", products.getPrice());
                              i.putExtra("image", products.getImage());
                              i.putExtra("description", products.getDescription());
                              i.putExtra("quant", products.getQuantity());
                              startActivity(i);
                        }
                  });


            }

            @Override
            public int getItemCount() {
                  return productsArrayList.size();
            }

            public class HomeViewHolder extends RecyclerView.ViewHolder{

                  TextView homeItemName, homeItemPrice;
                  ImageView homeItemImg;


                  public HomeViewHolder(@NonNull View itemView) {
                        super(itemView);

                        homeItemName = itemView.findViewById(R.id.home_item_name);
                        homeItemPrice = itemView.findViewById(R.id.home_item_price);

                        homeItemImg = itemView.findViewById(R.id.home_item_img);


                  }

            }

      }

      public void search(String s)
      {
            Query query = databaseReferenceInventory.orderByChild("name")
                    .startAt(s)
                    .endAt(s + "\uf8ff");

            query.addValueEventListener(new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChildren())
                        {
                              productsArrayList.clear();
                              for (DataSnapshot dss: snapshot.getChildren())
                              {
                                    final Products products = dss.getValue(Products.class);
                                    productsArrayList.add(products);
                              }

                              HomeSearchAdapter myAdapter = new HomeSearchAdapter(getContext(), productsArrayList);
                              homeRecyclerView.setAdapter(myAdapter);
                              myAdapter.notifyDataSetChanged();

                        }
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError error) {

                  }
            });

      }
}