package com.ammar.tawseel.adapters;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ammar.tawseel.R;
import com.ammar.tawseel.pojo.data.DataMessags;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterMessages extends RecyclerView.Adapter<AdapterMessages.ViewHolderVidio> {

    ArrayList<DataMessags> list;
    private Context mcontext;
    private OnclickMessage onclickMessage;

    public interface OnclickMessage {

        public void itemOnclick();
    }

    int layout;

    public AdapterMessages(ArrayList<DataMessags> list, Context mcontext, OnclickMessage onclickMessage) {


        this.mcontext = mcontext;
        this.list = list;
        this.onclickMessage = onclickMessage;
    }

    @NonNull
    @Override
    public ViewHolderVidio onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new ViewHolderVidio(view);

    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolderVidio holder, final int position) {
        holder.name.setText(list.get(position).getIDName() + "");
        holder.message.setText(list.get(position).getMessage() + "");
        Picasso.with(mcontext).load(list.get(position).getAvatar())
                .into(holder.roundedImageView);
        holder.itemView.setOnClickListener(v -> {

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
        TextView name, message, date;
        RoundedImageView roundedImageView;
        public RelativeLayout viewBackground;
        public FrameLayout viewForeground;


        public ViewHolderVidio(@NonNull View itemView) {
            super(itemView);

            viewBackground = itemView.findViewById(R.id.view_background);
            roundedImageView = itemView.findViewById(R.id.imageView1);
            name = itemView.findViewById(R.id.tv_name_message);
            message = itemView.findViewById(R.id.tv_messgee);
            date = itemView.findViewById(R.id.tv_date_messgee);
            viewForeground = itemView.findViewById(R.id.view_for_background);


        }


//        public void restoreItem(ClipData.Item item, int position) {
//            cartList.add(position, item);
//            // notify item added by position
//            notifyItemInserted(position);
//        }
    }


}
