package com.example.cartpage;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {
    private List<Cart> items = new ArrayList<>();
    private Context mcontext;

    private CartViewModel cartViewModel;



    public CartAdapter(Context context) {
        this.mcontext = context;
    }

    public void setCarts(List<Cart> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_single_element, parent, false);
        //cartViewModel = ViewModelProviders.of().get(CartViewModel.class);
        return new CartHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartHolder holder, int position) {
        final Cart currentitem = items.get(position);

        Glide.with(mcontext)
                .load(currentitem.getDisplayImg())
                .into(holder.cartImg);

        holder.productName.setText(currentitem.getProduct_name());
        holder.productPrice.setText("৳"+currentitem.getProuct_price()+"");
        holder.productSku.setText(currentitem.getSku());
        if(currentitem.getVaiants()!=null && currentitem.getVaiants().length()>0) {
            holder.productVariation.setVisibility(View.VISIBLE);
            holder.productVariation.setText(currentitem.getVaiants());

        }

        holder.qtyEt.setText(currentitem.getQunatity()+"");
        holder.stockCount.setText(currentitem.getStock() + " products available");

        holder.plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = holder.qtyEt.getText().toString();
                int total = Integer.parseInt(val);
                total = total + 1;
                if(total>currentitem.getStock()) {
                    total = currentitem.getStock();
                    Toast.makeText(mcontext, "Not more than "+ currentitem.getStock() + " Qty is not allowed", Toast.LENGTH_SHORT).show();
                }
                currentitem.setQunatity(total);
                ((CartPage)mcontext).updateCartItem(currentitem,mcontext);
                holder.qtyEt.setText(total+"");

            }
        });

        holder.minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = holder.qtyEt.getText().toString();
                int total = Integer.parseInt(val);
                total = total - 1;
                if(total<1) {
                    total = 1;
                    Toast.makeText(mcontext, "Less than 1 Qty is not allowed", Toast.LENGTH_SHORT).show();
                }
                holder.qtyEt.setText(total+"");
//                currentitem.setQunatity(Integer.parseInt(s.toString()));
                currentitem.setQunatity(total);
                ((CartPage)mcontext).updateCartItem(currentitem,mcontext);

            }
        });


        holder.qtyEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialogBuilder = new AlertDialog.Builder(mcontext).create();

                View dialogView =  LayoutInflater.from(mcontext)
                        .inflate(R.layout.custom_dialog, null);

                final EditText editText = (EditText) dialogView.findViewById(R.id.edt_comment);
                Button button1 = (Button) dialogView.findViewById(R.id.buttonSubmit);
                Button button2 = (Button) dialogView.findViewById(R.id.buttonCancel);

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogBuilder.dismiss();
                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String val = editText.getText().toString();
                        if(val.length()>0) {
                            int total = Integer.parseInt(val);
                            if(total<1) {
                                Toast.makeText(mcontext, "0 Qty is not allowed", Toast.LENGTH_SHORT).show();
                            }
                            else if(total>currentitem.getStock()) {
                                Toast.makeText(mcontext, "Not more than "+ currentitem.getStock() + " Qty is allowed", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                holder.qtyEt.setText(val);
                                currentitem.setQunatity(total);
                                ((CartPage)mcontext).updateCartItem(currentitem,mcontext);
                            }
                        }

                        else {

                        }
                        dialogBuilder.dismiss();
                    }
                });

                dialogBuilder.setView(dialogView);
                dialogBuilder.show();
            }
        });

        holder.dltFrmCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CartPage)mcontext).deleteItem(currentitem);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class CartHolder extends RecyclerView.ViewHolder {
        private ImageView cartImg,dltFrmCart;
        private ToggleButton wishButton,select;
        private TextView productName,productVariation,productSku,productPrice,stockCount;
        private Button plusBtn,minusBtn;
        private EditText qtyEt;


        public CartHolder(View itemView) {
            super(itemView);
            cartImg = itemView.findViewById(R.id.cart_singleImg);
            dltFrmCart = itemView.findViewById(R.id.deletFromCartImg);
            select = itemView.findViewById(R.id.select);
            wishButton = itemView.findViewById(R.id.wishButton);
            productName = itemView.findViewById(R.id.productName);
            productVariation = itemView.findViewById(R.id.variations);
            productSku = itemView.findViewById(R.id.productSku);
            productPrice = itemView.findViewById(R.id.productPrice);
            stockCount = itemView.findViewById(R.id.stockAvail);
            plusBtn = itemView.findViewById(R.id.plusBtn);
            minusBtn = itemView.findViewById(R.id.minusBtn);
            qtyEt = itemView.findViewById(R.id.qtyEditext);
        }
    }
}