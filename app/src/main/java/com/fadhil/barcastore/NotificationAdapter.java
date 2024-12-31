package com.fadhil.barcastore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotifikasiViewHolder> {
    // Data yang akan ditampilkan di RecyclerView
    private List<ProductItem> checkedOutItems;

    // Konstruktor untuk menerima data yang akan ditampilkan
    public NotificationAdapter(List<ProductItem> checkedOutItems) {
        // Jika data yang diterima null, inisialisasi dengan list kosong
        this.checkedOutItems = checkedOutItems != null ? checkedOutItems : new ArrayList<>();
    }

    // Membuat dan mengembalikan ViewHolder
    @NonNull
    @Override
    public NotifikasiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout item_notification.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotifikasiViewHolder(view);
    }

    // Mengikat data ke tampilan ViewHolder
    @Override
    public void onBindViewHolder(@NonNull NotifikasiViewHolder holder, int position) {
        // Mendapatkan item yang akan ditampilkan pada posisi tertentu
        ProductItem item = checkedOutItems.get(position);

        // Mengisi data ke dalam TextView di ViewHolder
        holder.nameTextView.setText(item.getName());
        holder.priceTextView.setText(String.format("$%.2f", item.getPrice()));
    }

    // Mengembalikan jumlah item yang ada dalam data
    @Override
    public int getItemCount() {
        return checkedOutItems != null ? checkedOutItems.size() : 0;
    }

    // ViewHolder untuk item di RecyclerView
    public static class NotifikasiViewHolder extends RecyclerView.ViewHolder {
        // Referensi ke elemen-elemen UI di layout item_notification
        TextView nameTextView, priceTextView;

        // Konstruktor untuk inisialisasi elemen-elemen UI
        public NotifikasiViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.notifikasi_item_name);  // ID TextView untuk nama produk
            priceTextView = itemView.findViewById(R.id.notifikasi_item_price);  // ID TextView untuk harga produk
        }
    }
}
