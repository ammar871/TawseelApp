package com.ammar.tawseel.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.ammar.tawseel.R;
import com.ammar.tawseel.pojo.data.DataFatora;
import com.ammar.tawseel.pojo.data.PayDataMethod;
import com.ammar.tawseel.uitllis.Cemmon;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterPaymentMethods extends RecyclerView.Adapter<AdapterPaymentMethods.ViewHolderVidio> {

    ArrayList<PayDataMethod> list;
    private Context mcontext;

    private OnclickMessage onclickMessage;
    public interface OnclickMessage {

        public void itemOnclickNewFatora(DataFatora dataNotification);
    }

    int layout;

//    public AdapterPaymentMethods(ArrayList<DataFatora> list, Context mcontext, AdapterFatora.OnclickMessage onclickMessage) {
//        this.list = list;
//        this.mcontext = mcontext;
//        this.onclickMessage = onclickMessage;
//    }

    public AdapterPaymentMethods(ArrayList<PayDataMethod> list, Context mcontext) {


        this.mcontext = mcontext;
        this.list = list;

    }

    @NonNull
    @Override
    public ViewHolderVidio onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_method, parent, false);
        return new ViewHolderVidio(view);

    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolderVidio holder, final int position) {
        Picasso.with(mcontext).load(list.get(position).getImageUrl())
                .into(holder.imageViewMethod);

        if (Cemmon.LONG_USER.equalsIgnoreCase("ar"))
        holder.nameMethod.setText(list.get(position).getPaymentMethodAr());
        else
            holder.nameMethod.setText(list.get(position).getPaymentMethodEn());
    }




    @Override
    public int getItemCount() {
        return list.size();
    }

    public void removeItem(int position) {
        list.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public static class ViewHolderVidio extends RecyclerView.ViewHolder {

        ImageView imageViewMethod;
        TextView nameMethod;
        RadioButton radio_checked;

        public ViewHolderVidio(@NonNull View itemView) {
            super(itemView);
            imageViewMethod = itemView.findViewById(R.id.img_visa_method);

            nameMethod = itemView.findViewById(R.id.tv_name_method);

            radio_checked = itemView.findViewById(R.id.radio_visa);


        }



    }}

