package jbox.skillz.medic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import jbox.skillz.medic.Model.Cart;
import jbox.skillz.medic.Model.Order;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFagment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFagment extends Fragment {

      View orderView;

      RecyclerView orderRecyclerView;
      CardView orderSection;

      DatabaseReference databaseReference;
      DatabaseReference databaseReferenceMyOrders;

      String uid = "";
      OrderAdapter adapter;
      ArrayList<Order> orderArrayList;

      // TODO: Rename parameter arguments, choose names that match
      // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
      private static final String ARG_PARAM1 = "param1";
      private static final String ARG_PARAM2 = "param2";

      // TODO: Rename and change types of parameters
      private String mParam1;
      private String mParam2;

      public OrderFagment() {
            // Required empty public constructor
      }

      /**
       * Use this factory method to create a new instance of
       * this fragment using the provided parameters.
       *
       * @param param1 Parameter 1.
       * @param param2 Parameter 2.
       * @return A new instance of fragment CartFagment.
       */
      // TODO: Rename and change types and number of parameters
      public static OrderFagment newInstance(String param1, String param2) {
            OrderFagment fragment = new OrderFagment();
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

            orderView = inflater.inflate(R.layout.fragment_order_fagment, container, false);

            orderSection = orderView.findViewById(R.id.orders_section);
            orderRecyclerView = orderView.findViewById(R.id.order_list);

            orderRecyclerView.setHasFixedSize(true);
            orderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext() , LinearLayoutManager.VERTICAL , false));

            return orderView;
      }

      @Override
      public void onStart() {
            super.onStart();

            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            uid = firebaseUser.getUid();

            //databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child(uid);
            databaseReferenceMyOrders = FirebaseDatabase.getInstance().getReference("MyOrders").child(uid);

            orderArrayList = new ArrayList<>();

            adapter = new OrderAdapter(getContext(), orderArrayList);
            orderRecyclerView.setAdapter(adapter);

            databaseReferenceMyOrders.addValueEventListener(new ValueEventListener(){

                  @Override
                  public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                              Order order = dataSnapshot.getValue(Order.class);
                              orderArrayList.add(order);
                        }
                        adapter.notifyDataSetChanged();
                        if(orderArrayList.size() == 0)
                        {
                              orderSection.setVisibility(View.GONE);
                              Toast.makeText(getContext(), "Order list is empity", Toast.LENGTH_LONG).show();
                        }
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError error) {

                  }
            });



      }

      public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{

            Context context;
            ArrayList<Order> orderArrayList;

            public OrderAdapter(Context context, ArrayList<Order> orderArrayList) {
                  this.context = context;
                  this.orderArrayList = orderArrayList;
            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                  View v = LayoutInflater.from(context).inflate(R.layout.order_list_layout, parent, false);
                  return new OrderViewHolder(v);

            }

            @Override
            public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {

                  Order order = orderArrayList.get(position);
                  int itemPrice = Integer.valueOf(order.getPrice());
                  int pos = position;

                  holder.orderItemsName.setText(order.getItems());
                  holder.orderItemPrice.setText(order.getPrice());
                  holder.orderItemTime.setText(order.getTime());
                  holder.orderItemStatus.setText("Track Order");
                  holder.orderItemAddress.setText(order.getAddress());

                  holder.orederItemImg.setImageResource(R.drawable.shoppp);

                  holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                              Intent i = new Intent(context, PopActivity.class);
                              startActivity(i);
                        }
                  });
                  holder.orderItemStatus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                              Intent i = new Intent(context, TrackingActivity.class);
                              i.putExtra("ordid", order.getOrderId());
                              i.putExtra("status", order.getStatus());
                              i.putExtra("payment", order.getPayment());
                              i.putExtra("address", order.getAddress());
                              context.startActivity(i);
                        }
                  });

            }

            @Override
            public int getItemCount() {
                  return orderArrayList.size();
            }

            public class OrderViewHolder extends RecyclerView.ViewHolder {

                  TextView orderItemsName, orderItemPrice, orderItemTime, orderItemStatus, orderItemAddress;

                  ImageView orederItemImg;

                  public OrderViewHolder(@NonNull View itemView) {
                        super(itemView);

                        orderItemsName = itemView.findViewById(R.id.order_list_layout_items);
                        orderItemPrice = itemView.findViewById(R.id.order_list_layout_price);
                        orderItemTime = itemView.findViewById(R.id.order_list_layout_time);
                        orderItemStatus = itemView.findViewById(R.id.order_list_layout_status);
                        orderItemAddress = itemView.findViewById(R.id.order_list_layout_address);
                        orederItemImg = itemView.findViewById(R.id.order_list_layout_img);

                  }
            }
      }

}