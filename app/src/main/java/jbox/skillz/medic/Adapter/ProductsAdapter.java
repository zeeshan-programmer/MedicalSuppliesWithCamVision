package jbox.skillz.medic.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jbox.skillz.medic.Model.Products;
import jbox.skillz.medic.R;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.BestSellerViewHolder> {

    private List<Products> productsList;
    public ProductsAdapter(List<Products> productsList){
        this.productsList = productsList;
    }
    @NonNull
    @Override
    public BestSellerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_layout, parent , false);
        return new BestSellerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BestSellerViewHolder holder, int position) {
//        holder.mText.setText(productsList.get(position).getOffer());
//        holder.mImageview.setImageResource(productsList.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class BestSellerViewHolder extends RecyclerView.ViewHolder{

        private ImageView mImageview;
        private TextView mText;
        public BestSellerViewHolder(@NonNull View itemView) {
            super(itemView);

//            mImageview = itemView.findViewById(R.id.bestSellerImage);
//            mText = itemView.findViewById(R.id.bestSellerText);
        }
    }
}
