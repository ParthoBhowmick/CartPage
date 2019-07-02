package com.example.cartpage;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Cart.class}, version = 1)
public abstract class CartDatabase extends RoomDatabase {

    private static CartDatabase instance;

    public abstract CartDao cartDao();

    public static synchronized CartDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CartDatabase.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private CartDao cartDao;

        private PopulateDbAsyncTask(CartDatabase db) {
            cartDao = db.cartDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            cartDao.insert(new Cart("Floor Mat",  "JRPCMWDGGX-M", "https://www.jadroo.com/uploads/media/old/images/products/products-4014774912_1753305594.jpg", 1, 300, 2, "size : 50-80cm-2"));
            cartDao.insert(new Cart("Taco Holder Wave Shape Kitchen Food Pizza Shell Restaurant Baking Tool Stand Rack Display Pastry Stainless Steel Mexican Pancake(Silver)", "0123","https://www.jadroo.com/uploads/media/2019/06/TIoVh2ojUV/zhutu-medium.jpg",2 , 400, 5, null));
            cartDao.insert(new Cart("Casual Non-Slip Males Slippers", "XMA012-R-44", "https://www.jadroo.com/uploads/media/old/2018/12/%E4%B8%BB%E5%9B%BE-%E5%B0%8F%E8%B4%9D%E9%BB%91%E8%89%B2.jpg", 2, 580, 2, "color: red-2,size: 44" ));
            return null;
        }
    }
}