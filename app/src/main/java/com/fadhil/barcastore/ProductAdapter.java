package com.fadhil.barcastore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<ProductItem> productList;
    private Context context;
    private DatabaseHelper databaseHelper;
    private OnItemClickListener listener;

    // Constructor with listener
    public ProductAdapter(List<ProductItem> productList, Context context, DatabaseHelper databaseHelper, OnItemClickListener listener) {
        this.productList = productList;
        this.context = context;
        this.databaseHelper = databaseHelper;
        this.listener = listener;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        ProductItem product = productList.get(position);
        holder.nameTextView.setText(product.getName());
        holder.priceTextView.setText(String.format("$%.2f", product.getPrice()));
        holder.descriptionTextView.setText(product.getDescription());
        holder.productImageView.setImageResource(product.getImageResourceId());

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // Method to update the product list
    public void updateProductList(List<ProductItem> newProductList) {
        this.productList = newProductList;
        notifyDataSetChanged();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView, priceTextView, descriptionTextView;
        public ImageView productImageView;

        public ProductViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.product_name);
            priceTextView = itemView.findViewById(R.id.product_price);
            descriptionTextView = itemView.findViewById(R.id.product_description);
            productImageView = itemView.findViewById(R.id.product_image); // Tambahkan inisialisasi ini
        }
    }


    // Interface for item click listener
    public interface OnItemClickListener {
        void onItemClick(ProductItem product);
    }
}

