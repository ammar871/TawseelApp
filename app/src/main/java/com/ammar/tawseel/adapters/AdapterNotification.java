package com.ammar.tawseel.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.ammar.tawseel.R;
import com.ammar.tawseel.helper.RecyclerItemTouchNotification;
import com.ammar.tawseel.pojo.data.DataNotification;
import com.ammar.tawseel.ui.ConfirmActivity;
import com.ammar.tawseel.ui.OrdersActivity;
import com.ammar.tawseel.ui.RatingUsersActivity;
import com.ammar.tawseel.ui.fragments.DetailesActivity;
import com.ammar.tawseel.ui.fragments.FatoraFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.ViewHolderVidio> {

    ArrayList<DataNotification> list;
    private Context mcontext;
    private OnclickMessage onclickMessage;
    AlertDialog alertDialog = null;



    public interface OnclickMessage {

        public void itemOnclickNewBill(Integer dataNotification, String i);
    }

    int layout;

//    @Override
//    public int getItemViewType(int position) {
//        if (list.get(position).getReaded().equals("0")) {
//            return 0;
//        }
//
//        return 1;
//
//
//    }

    public AdapterNotification(ArrayList<DataNotification> list, Context mcontext, OnclickMessage onclickMessage) {
        this.list = list;
        this.mcontext = mcontext;
        this.onclickMessage = onclickMessage;
    }

    public AdapterNotification(ArrayList<DataNotification> list, Context mcontext) {


        this.mcontext = mcontext;
        this.list = list;

    }

    @NonNull
    @Override
    public ViewHolderVidio onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view;
//        if (viewType == 0 ) {
            view = layoutInflater.inflate(R.layout.item_notify, parent, false);
            return new ViewHolderVidio(view);
//        }
//            view = layoutInflater.inflate(R.layout.item_notify, parent, false);
//            return new ViewHolderVidio(view);

    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolderVidio holder, final int position) {


        @SuppressLint("SimpleDateFormat") SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy",new Locale("en"));

        Date d = null;

        try {
            d = input.parse(list.get(position).getCreatedAt());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formatted = output.format(d);

        holder.date_notiy.setText(formatted + "");


        if (list.get(position).getType().equals("new-bill")) {
            holder.name_notiy.setText(R.string.new_bill);
            holder.desc_noty.setText(list.get(position).getTarget() + "");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putString("id", list.get(position).getTarget() + "");
                    FatoraFragment category = new FatoraFragment();
                    category.setArguments(bundle);

                    ((AppCompatActivity) mcontext).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.layout_view, category)
                            .addToBackStack(null)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .commit();

                }
            });
        } else if (list.get(position).getType().equals("deliver-order")) {

            holder.name_notiy.setText(R.string.noty_delviry_order);
            holder.desc_noty.setText(list.get(position).getTarget() + "");
       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(mcontext, ConfirmActivity.class);
               intent.putExtra("idOrder",list.get(position).getTarget()+"");
               mcontext.startActivity(intent);
           }
       });

        } else if (list.get(position).getType().equals("deliver-confirm")) {
            holder.name_notiy.setText(R.string.deliverd_order);
            holder.desc_noty.setText(list.get(position).getTarget() + "");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   mcontext.startActivity(new Intent(mcontext, OrdersActivity.class));
                }
            });

        } else if (list.get(position).getType().equals("accept-order")) {
            holder.name_notiy.setText(R.string.accept_order);
            holder.desc_noty.setText(list.get(position).getTarget() + "");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mcontext.startActivity(new Intent(mcontext, OrdersActivity.class));
                }
            });

        } else if (list.get(position).getType().equals("refuse-order")) {

            holder.name_notiy.setText( R.string.refuse_order);
            holder.desc_noty.setText(list.get(position).getTarget() + "");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mcontext.startActivity(new Intent(mcontext, OrdersActivity.class));
                }
            });

        } else if (list.get(position).getType().equals("user-add-rate")) {
            holder.name_notiy.setText(R.string.rate_dliviry);
            holder.desc_noty.setText(list.get(position).getTarget() + "");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mcontext.startActivity(new Intent(mcontext, RatingUsersActivity.class));
                }
            });
        } else if (list.get(position).getType().equals("paid-bill")) {
            holder.name_notiy.setText(R.string.paid_bill_);
            holder.desc_noty.setText(list.get(position).getTarget() + "");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mcontext, DetailesActivity.class);
                    intent.putExtra("idBill", list.get(position).getParams());
                    mcontext.startActivity(intent);
                }
            });
        } else if (list.get(position).getType().equals("cancel-bill")) {
            holder.name_notiy.setText(R.string.cancel_bill);
            holder.desc_noty.setText(list.get(position).getTarget() + "");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mcontext, DetailesActivity.class);
                    intent.putExtra("idBill", list.get(position).getParams());
                    mcontext.startActivity(intent);
                }
            });
        }
        else if (list.get(position).getType().equals("admin-accept-order")||
                list.get(position).getType().equals("admin-refues-order")||
                list.get(position).getType().equals("admin-id-papers")||
                list.get(position).getType().equals("admin-activate-account")||
                list.get(position).getType().equals("admin-financial-boost")||
                list.get(position).getType().equals("admin-new-rate")||
                list.get(position).getType().equals("admin-cash")) {

            holder.name_notiy.setText(list.get(position).getTarget() + "");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }else {
            holder.name_notiy.setText(list.get(position).getTarget() + "");
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        notifyItemRemoved(position);
    }

    public static class ViewHolderVidio extends RecyclerView.ViewHolder {

        ImageView imageView;

        public RelativeLayout viewBackground;
        public FrameLayout viewForeground;
        TextView name_notiy, desc_noty, date_notiy;
        CardView cardView;

        public ViewHolderVidio(@NonNull View itemView) {
            super(itemView);

            viewBackground = itemView.findViewById(R.id.view_background_noty);
            viewForeground = itemView.findViewById(R.id.view_for_background_noty);
            name_notiy = itemView.findViewById(R.id.tv_name_lable_noty);
            desc_noty = itemView.findViewById(R.id.tv_messgee_noty);
            date_notiy = itemView.findViewById(R.id.tv_date_messgee_noty);
            cardView = itemView.findViewById(R.id.card);

        }


//        public void restoreItem(ClipData.Item item, int position) {
//            cartList.add(position, item);
//            // notify item added by position
//            notifyItemInserted(position);
//        }
    }
}

