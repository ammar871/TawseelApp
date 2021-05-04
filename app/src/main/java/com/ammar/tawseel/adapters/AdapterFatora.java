package com.ammar.tawseel.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ammar.tawseel.R;
import com.ammar.tawseel.pojo.data.DataFatora;
import com.ammar.tawseel.pojo.data.DataMessags;
import com.ammar.tawseel.pojo.data.DataOrder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterFatora extends RecyclerView.Adapter<AdapterFatora.ViewHolderVidio> {

    ArrayList<DataFatora> list;
    private Context mcontext;



    int layout;

    public AdapterFatora(ArrayList<DataFatora> list, Context mcontext) {


        this.mcontext = mcontext;
        this.list = list;

    }

    @NonNull
    @Override
    public ViewHolderVidio onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fatoraes, parent, false);
        return new ViewHolderVidio(view);

    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolderVidio holder, final int position) {
        holder.number.setText(list.get(position).getOrderId() + "");
        holder.to_fatora.setText(list.get(position).getToAddress() + "");
        holder.from_fatora.setText(list.get(position).getFromAddress() + "");
        holder.price.setText(list.get(position).getPrice() + "");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  onclickMessage.itemOnclick();
            }
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

    public void removeItem(int position) {
        list.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public static class ViewHolderVidio extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView number, price, to_fatora, from_fatora;


        public ViewHolderVidio(@NonNull View itemView) {
            super(itemView);


            number = itemView.findViewById(R.id.number_fatoram);
            price = itemView.findViewById(R.id.price_fatoram);
            to_fatora = itemView.findViewById(R.id.toFatoraM);
            from_fatora = itemView.findViewById(R.id.from_fatoram);


        }


//        public void restoreItem(ClipData.Item item, int position) {
//            cartList.add(position, item);
//            // notify item added by position
//            notifyItemInserted(position);
//        }
    }}

