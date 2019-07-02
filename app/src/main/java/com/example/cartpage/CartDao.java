package com.example.cartpage;


import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CartDao {

    @Insert
    void insert(Cart cart);

    @Update
    void update(Cart cart);

    @Delete
    void delete(Cart cart);

    @Query("DELETE FROM cart_table")
    void deleteAllCarts();

    @Query("SELECT * FROM cart_table ORDER BY id ASC")
    LiveData<List<Cart>> getAllCarts();

    @Query("SELECT * FROM cart_table WHERE sku = :productSku")
    Cart findProductnAction(String productSku);

}