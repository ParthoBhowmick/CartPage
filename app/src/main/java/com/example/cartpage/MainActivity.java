package com.example.cartpage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.widget.Toast.*;


public class MainActivity extends AppCompatActivity implements TryCartFragment.OnFragmentInteractionListener {

    private Fragment fragment;
    private FragmentManager manager;

    private CartViewModel cartViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addFragment();
        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
    }

    public void addFragment() {
        manager = getSupportFragmentManager();
        fragment = manager.findFragmentById(R.id.frameContainer);
        if(fragment==null) {
            fragment = new TryCartFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.frameContainer,fragment);
            transaction.commit();
        }
    }




    public void viewCart() {
        Intent intent = new Intent(this,CartPage.class);
        startActivity(intent);
    }

    public void addCart(Cart cart) {

        if (cart.getProduct_name()!=null) {
            cartViewModel.findProductnAction(cart.getSku(),cart,this);
        }
    }

    public void insertItem(Cart cart) {
        cartViewModel.insert(cart);
    }

    public void updateCartItem(Cart cart,Context ctx) {
        cartViewModel.update(cart,ctx);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
