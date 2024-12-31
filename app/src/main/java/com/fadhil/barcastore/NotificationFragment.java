package com.fadhil.barcastore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// NotifikasiFragment.java
public class NotificationFragment extends Fragment {
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<ProductItem> checkedOutItems;

    // onCreateView dipanggil saat fragment pertama kali diciptakan
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout fragment_notification.xml ke dalam view
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        // Inisialisasi RecyclerView dan set LayoutManager
        recyclerView = view.findViewById(R.id.recycler_view_checked_out_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Mengambil data yang dikirim dari fragment lain (misalnya CartFragment)
        Bundle arguments = getArguments();
        if (arguments != null) {
            // Mendapatkan data yang diserahkan melalui Bundle
            checkedOutItems = (List<ProductItem>) arguments.getSerializable("checkedOutItems");
        }

        // Inisialisasi adapter dan set ke RecyclerView
        adapter = new NotificationAdapter(checkedOutItems);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
