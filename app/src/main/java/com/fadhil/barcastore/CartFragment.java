package com.fadhil.barcastore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private DatabaseHelper databaseHelper;
    private TextView totalPriceTextView;
    private List<ProductItem> cartItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_cart);
        totalPriceTextView = view.findViewById(R.id.total_price_text_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        databaseHelper = new DatabaseHelper(getContext());
        cartItems = new ArrayList<>(databaseHelper.getCartItems());

        // Ambil data produk dari Bundle jika tersedia
        Bundle arguments = getArguments();
        if (arguments != null) {
            String productName = arguments.getString("productName");
            double productPrice = arguments.getDouble("productPrice");
            String productDescription = arguments.getString("productDescription");
            int productImage = arguments.getInt("productImage");

            // Tambahkan item baru ke daftar cart
            ProductItem newProduct = new ProductItem(productName, productPrice, productDescription, productImage);
            cartItems.add(newProduct);

            // Simpan ke database
            databaseHelper.addToCart(newProduct);
        }

        // Inisialisasi adapter
        adapter = new CartAdapter(cartItems);
        recyclerView.setAdapter(adapter);

        // Tambahkan listener untuk tombol Checkout
        adapter.setOnCheckoutClickListener(product -> {
            // Logika untuk checkout
            Toast.makeText(getContext(), "Checkout: " + product.getName(), Toast.LENGTH_SHORT).show();
        });

        // Tambahkan listener untuk tombol Hapus
        adapter.setOnDeleteClickListener(product -> {
            // Hapus item dari database
            databaseHelper.removeFromCart(product);
            // Update daftar cart dan total harga
            updateCartItems();
        });

        adapter.setOnCheckoutClickListener(product -> {
            // Kirim data ke NotificationFragment
            List<ProductItem> checkedOutItems = new ArrayList<>();
            checkedOutItems.add(product);  // Menambahkan item yang di-checkout

            // Membuat Bundle untuk mengirim data ke NotificationFragment
            Bundle bundle = new Bundle();
            bundle.putSerializable("checkedOutItems", (ArrayList<ProductItem>) checkedOutItems);

            // Pindah ke NotificationFragment
            NotificationFragment notificationFragment = new NotificationFragment();
            notificationFragment.setArguments(bundle);

            // Transaksi Fragment
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, notificationFragment)
                    .addToBackStack(null)
                    .commit();

            // Tampilkan Toast
            Toast.makeText(getContext(), "Checkout: " + product.getName(), Toast.LENGTH_SHORT).show();
        });

        // Hitung total harga awal
        updateTotalPrice();

        return view;
    }

    private void updateCartItems() {
        // Ambil data terbaru dari database
        cartItems = new ArrayList<>(databaseHelper.getCartItems());
        // Perbarui data di adapter
        adapter.updateCartItems(cartItems);
        // Perbarui total harga
        updateTotalPrice();
    }

    // Metode untuk menghapus item dari cart dan memperbarui RecyclerView
    public void removeItem(int position) {
        if (position >= 0 && position < cartItems.size()) {
            // Hapus item dari daftar cart
            cartItems.remove(position);
            // Perbarui RecyclerView
            adapter.notifyItemRemoved(position);
            adapter.notifyItemRangeChanged(position, cartItems.size());
            // Perbarui total harga setelah item dihapus
            updateTotalPrice();
        }
    }

    private void updateTotalPrice() {
        double totalPrice = 0.0;
        for (ProductItem item : cartItems) {
            totalPrice += item.getPrice();
        }
        totalPriceTextView.setText(String.format("Total: $%.2f", totalPrice));
    }
}
