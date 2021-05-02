package com.ammar.tawseel.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ammar.tawseel.R;
import com.ammar.tawseel.pojo.data.DataOrder;
import com.ammar.tawseel.ui.TrakingMapsActivity;

import java.util.ArrayList;

public class AdapterOrdersHome extends RecyclerView.Adapter<AdapterOrdersHome.ViewHolderVidio> {


    private Context mcontext;
    ArrayList<DataOrder> list;
    int layout;

    public AdapterOrdersHome(Context mcontext, ArrayList<DataOrder> list) {
        this.mcontext = mcontext;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolderVidio onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolderVidio(view);

    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolderVidio holder, final int position) {
        holder.name.setText(list.get(position).getIDName());
        holder.tv_fatora.setText(list.get(position).getBillId());
        holder.tv_price.setText(list.get(position).getPrice());
        holder.tv_place_from.setText(list.get(position).getFromAddress());
        holder.tv_place_to.setText(list.get(position).getToAddress());

        holder.img_track.setOnClickListener(v -> {

            Intent intent = new Intent(mcontext, TrakingMapsActivity.class);
            intent.putExtra("orderDetails", list.get(position));
            mcontext.startActivity(intent);
        });
    }


//    private void intentMothed(Class a, Catogray value) {
//
//        Intent intent = new Intent(mcontext, a);
//        intent.putExtra("catogery", value);
//
//        mcontext.startActivity(intent);
//    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolderVidio extends RecyclerView.ViewHolder {


        TextView name, tv_fatora, tv_price, tv_place_from, tv_place_to,img_track;;


        public ViewHolderVidio(@NonNull View itemView) {
            super(itemView);

            img_track = itemView.findViewById(R.id.ic_save_track);
            name = itemView.findViewById(R.id.tv_name);
            tv_fatora = itemView.findViewById(R.id.tv_fatora);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_place_from = itemView.findViewById(R.id.tv_place_from);
            tv_place_to = itemView.findViewById(R.id.tv_place_to);


        }


    }


}
