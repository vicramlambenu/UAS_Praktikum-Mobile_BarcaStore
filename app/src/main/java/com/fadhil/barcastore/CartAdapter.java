package com.fadhil.barcastore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<ProductItem> cartItems;
    private OnCheckoutClickListener checkoutClickListener;
    private OnDeleteClickListener deleteClickListener;

    // Constructor
    public CartAdapter(List<ProductItem> cartItems) {
        this.cartItems = cartItems;
    }

    // Interface untuk listener tombol Checkout
    public interface OnCheckoutClickListener {
        void onCheckoutClick(ProductItem product);
    }

    // Interface untuk listener tombol Hapus
    public interface OnDeleteClickListener {
        void onDeleteClick(ProductItem product);
    }

    // Setter untuk listener Checkout
    public void setOnCheckoutClickListener(OnCheckoutClickListener listener) {
        this.checkoutClickListener = listener;
    }

    // Setter untuk listener Hapus
    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        ProductItem item = cartItems.get(position);
        holder.nameTextView.setText(item.getName());
        holder.priceTextView.setText(String.format("$%.2f", item.getPrice()));

        // Tangani klik pada tombol Checkout
        holder.checkoutButton.setOnClickListener(v -> {
            if (checkoutClickListener != null) {
                checkoutClickListener.onCheckoutClick(item);
            }
        });

        // Tangani klik pada tombol Hapus
        holder.deleteButton.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(item);
            }
        });
    }


    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, priceTextView;
        Button checkoutButton, deleteButton;

        public CartViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.cart_item_name);
            priceTextView = itemView.findViewById(R.id.cart_item_price);
            checkoutButton = itemView.findViewById(R.id.checkout_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }

    public void updateCartItems(List<ProductItem> newCartItems) {
        this.cartItems = newCartItems;
        notifyDataSetChanged();
    }
}
