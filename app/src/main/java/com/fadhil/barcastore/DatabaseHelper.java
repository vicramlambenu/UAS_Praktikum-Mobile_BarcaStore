package com.fadhil.barcastore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "barca_store_db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_PRODUCTS = "products";
    private static final String TABLE_CART = "cart";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Membuat tabel produk
        String createProductsTable = "CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCTS + " (" +
                "name TEXT PRIMARY KEY, " +
                "price REAL, " +
                "description TEXT, " +
                "imageResourceId INTEGER);";
        db.execSQL(createProductsTable);

        // Membuat tabel keranjang
        String createCartTable = "CREATE TABLE IF NOT EXISTS " + TABLE_CART + " (" +
                "name TEXT PRIMARY KEY, " +
                "price REAL, " +
                "quantity INTEGER);";
        db.execSQL(createCartTable);

        // Menambahkan produk default hanya jika tabel baru dibuat
        addDefaultProducts(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Hapus tabel lama dan buat tabel baru jika ada perubahan pada struktur database
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    // Menambahkan produk default
    private void addDefaultProducts(SQLiteDatabase db) {
        // Periksa apakah produk sudah ada di database
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);
        if (cursor.getCount() == 0) {
            addProduct(db, new ProductItem("Jersey Home", 80.0, "Jersey Barcelona Home", R.drawable.jersey_home));
            addProduct(db, new ProductItem("Bola", 25.0, "Bola resmi FC Barcelona", R.drawable.bola));
            addProduct(db, new ProductItem("Scarf", 15.0, "Scarf Barcelona", R.drawable.scarf));
        }
        cursor.close();
    }

    // Menambahkan produk ke tabel produk
    private void addProduct(SQLiteDatabase db, ProductItem product) {
        ContentValues values = new ContentValues();
        values.put("name", product.getName());
        values.put("price", product.getPrice());
        values.put("description", product.getDescription());
        values.put("imageResourceId", product.getImageResourceId());
        db.insert(TABLE_PRODUCTS, null, values);
    }

    // Mengambil semua produk
    public List<ProductItem> getAllProducts() {
        List<ProductItem> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                    int imageResourceId = cursor.getInt(cursor.getColumnIndexOrThrow("imageResourceId"));

                    productList.add(new ProductItem(name, price, description, imageResourceId));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error fetching products", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return productList;
    }

    // Menambahkan produk ke keranjang
    public void addToCart(ProductItem product) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT quantity FROM " + TABLE_CART + " WHERE name = ?", new String[]{product.getName()});

        if (cursor != null && cursor.moveToFirst()) {
            // Jika produk sudah ada di keranjang, tambahkan jumlahnya
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
            ContentValues values = new ContentValues();
            values.put("quantity", quantity + 1);
            db.update(TABLE_CART, values, "name = ?", new String[]{product.getName()});
        } else {
            // Jika produk belum ada, tambahkan sebagai entri baru
            ContentValues values = new ContentValues();
            values.put("name", product.getName());
            values.put("price", product.getPrice());
            values.put("quantity", 1);
            db.insert(TABLE_CART, null, values);
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    // Mengambil semua produk di keranjang
    public List<ProductItem> getCartItems() {
        List<ProductItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CART, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                    int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));

                    cartItems.add(new ProductItem(name, price, "", quantity));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error fetching cart items", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return cartItems;
    }

    // Menghapus item dari keranjang
    public void removeFromCart(ProductItem product) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT quantity FROM " + TABLE_CART + " WHERE name = ?", new String[]{product.getName()});

        if (cursor != null && cursor.moveToFirst()) {
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
            if (quantity > 1) {
                // Kurangi jumlah jika lebih dari 1
                ContentValues values = new ContentValues();
                values.put("quantity", quantity - 1);
                db.update(TABLE_CART, values, "name = ?", new String[]{product.getName()});
            } else {
                // Hapus produk jika hanya ada 1
                db.delete(TABLE_CART, "name = ?", new String[]{product.getName()});
            }
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    // Mengosongkan keranjang
    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, null, null);
    }
}
