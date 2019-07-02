package com.example.cartpage;


import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CartRepository implements FindResponse {
    private CartDao cartDao;
    private LiveData<List<Cart>> allCarts;
    private Cart cart;

    public CartRepository(Application application) {
        CartDatabase database = CartDatabase.getInstance(application);
        cartDao = database.cartDao();
        allCarts = cartDao.getAllCarts();
    }

    public void insert(Cart note) {
        new InsertCartAsyncTask(cartDao).execute(note);
    }

    public void update(Cart note,Context ctx) {
        new UpdateCartAsyncTask(cartDao,ctx).execute(note);
    }

    public void delete(Cart note) {
        new DeleteCartAsyncTask(cartDao).execute(note);
    }

    public void deleteAllCarts() {
        new DeleteAllCartsAsyncTask(cartDao).execute();
    }

    public LiveData<List<Cart>> getAllCarts() {
        return allCarts;
    }

    public void findProductnAction(String productSku, Cart cart, Context context) {
        new findProductAsyncTask(cartDao, cart, context).execute(productSku);
    }

    @Override
    public Cart processFinish(Cart output) {
        return output;
    }


    private static class InsertCartAsyncTask extends AsyncTask<Cart, Void, Void> {
        private CartDao cartDao;

        private InsertCartAsyncTask(CartDao cartDao) {
            this.cartDao = cartDao;
        }

        @Override
        protected Void doInBackground(Cart... notes) {
            cartDao.insert(notes[0]);
            return null;
        }
    }


    private static class findProductAsyncTask extends AsyncTask<String, Void, Cart> {
        private CartDao cartDao;
        private Cart mcart;
        private Context mcontext;

        private findProductAsyncTask(CartDao cartDao, Cart cart, Context context) {
            this.cartDao = cartDao;
            this.mcart = cart;
            this.mcontext = context;
        }

        @Override
        protected Cart doInBackground(String... items) {
            return cartDao.findProductnAction(items[0]);
        }

        @Override
        protected void onPostExecute(Cart cart) {
            if (cart != null) {
                int qty = mcart.getQunatity() + cart.getQunatity();
                if (cart.getStock() < qty) {
                    Toast.makeText(mcontext, "You can't add more than " + cart.getStock() + " products to Cart", Toast.LENGTH_SHORT).show();
                } else {
                    cart.setQunatity(qty);
                    ((MainActivity)mcontext).updateCartItem(cart,mcontext);
                }
            } else {
                //Toast.makeText(mcontext,  , Toast.LENGTH_SHORT).show();
                ((MainActivity)mcontext).insertItem(mcart);
            }
        }
    }

    private static class UpdateCartAsyncTask extends AsyncTask<Cart, Void, Void> {
        private CartDao cartDao;
        private ProgressDialog dialog;
        private Context ctx;

        private UpdateCartAsyncTask(CartDao cartDao,Context ctx) {
            this.cartDao = cartDao;
            this.ctx = ctx;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ctx);
            dialog.setMessage("Updating...");
            dialog.setIndeterminate(true);
            dialog.show();
        }


        @Override
        protected Void doInBackground(Cart... notes) {
            cartDao.update(notes[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    private static class DeleteCartAsyncTask extends AsyncTask<Cart, Void, Void> {
        private CartDao cartDao;

        private DeleteCartAsyncTask(CartDao cartDao) {
            this.cartDao = cartDao;
        }

        @Override
        protected Void doInBackground(Cart... notes) {
            cartDao.delete(notes[0]);
            return null;
        }
    }

    private static class DeleteAllCartsAsyncTask extends AsyncTask<Void, Void, Void> {
        private CartDao cartDao;

        private DeleteAllCartsAsyncTask(CartDao cartDao) {
            this.cartDao = cartDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            cartDao.deleteAllCarts();
            return null;
        }
    }
}