package jbox.skillz.medic.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jbox.skillz.medic.Model.Products;
import jbox.skillz.medic.ProductDetails;
import jbox.skillz.medic.R;

public class HomeSearchAdapter extends RecyclerView.Adapter<HomeSearchAdapter.MyAdapterViewHolder>
{

    public Context context;
    public ArrayList<Products> productsArrayList;

    public HomeSearchAdapter(Context c, ArrayList<Products> productsArrayList) {
        this.context = c;
        this.productsArrayList = productsArrayList;
    }

    @Override
    public int getItemCount() {
        return productsArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public MyAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.products_layout, parent, false);

        return new MyAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyAdapterViewHolder holder, int position)
    {
          Products products = productsArrayList.get(position);

          holder.homeItemName.setText(products.getName());
          holder.homeItemPrice.setText(products.getPrice());

          String strImg = products.getImage();

//                  holder.homeItemImg.setImageResource(products.getImage());
          Picasso.get().load(strImg).placeholder(R.drawable.tblts).into(holder.homeItemImg);
          holder.homeItemImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                      Intent i = new Intent(context, ProductDetails.class);
                      i.putExtra("name", products.getName());
                      i.putExtra("price", products.getPrice());
                      i.putExtra("image", products.getImage());
                      i.putExtra("description", products.getDescription());
                      i.putExtra("quant", products.getQuantity());
                      context.startActivity(i);
                }
          });


    }

    public class MyAdapterViewHolder extends RecyclerView.ViewHolder
    {

          TextView homeItemName, homeItemPrice;
          ImageView homeItemImg;


        public MyAdapterViewHolder(View itemView)
        {
            super(itemView);
              homeItemName = itemView.findViewById(R.id.home_item_name);
              homeItemPrice = itemView.findViewById(R.id.home_item_price);
              homeItemImg = itemView.findViewById(R.id.home_item_img);
        }
    }

}
