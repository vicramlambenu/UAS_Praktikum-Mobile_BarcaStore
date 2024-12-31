package com.fadhil.barcastore;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private DatabaseHelper databaseHelper;
    private EditText searchInput;
    private List<ProductItem> productList; // Daftar produk yang sudah diambil dari database

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_products);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchInput = view.findViewById(R.id.search_input);

        databaseHelper = new DatabaseHelper(getContext());
        productList = databaseHelper.getAllProducts(); // Ambil semua produk sekali saja

        // Setup adapter dan listener
        adapter = new ProductAdapter(productList, getContext(), databaseHelper, product -> {
            // Simpan data produk ke keranjang (misalnya, database atau in-memory list)
            databaseHelper.addToCart(product); // Pastikan metode ini ada di DatabaseHelper

            // Tampilkan pesan Toast
            Toast.makeText(getContext(), product.getName() + " telah ditambahkan ke keranjang", Toast.LENGTH_SHORT).show();
        });


        recyclerView.setAdapter(adapter);

        // Menambahkan TextWatcher untuk pencarian
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterProducts(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        return view;
    }

    private void filterProducts(String query) {
        List<ProductItem> filteredList = new ArrayList<>();
        String queryLowerCase = query.toLowerCase(); // Panggil sekali untuk efisiensi
        for (ProductItem product : productList) { // Gunakan produk yang sudah ada
            if (product.getName().toLowerCase().contains(queryLowerCase)) {
                filteredList.add(product);
            }
        }
        adapter.updateProductList(filteredList); // Update daftar yang difilter
    }
}
