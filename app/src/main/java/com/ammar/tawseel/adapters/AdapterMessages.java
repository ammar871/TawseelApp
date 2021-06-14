package com.ammar.tawseel.adapters;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.util.Log;
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
import com.ammar.tawseel.netWorke.APIClient;
import com.ammar.tawseel.pojo.data.DataMessags;
import com.ammar.tawseel.uitllis.Cemmon;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterMessages extends RecyclerView.Adapter<AdapterMessages.ViewHolderVidio> {

    ArrayList<DataMessags> list;
    private Context mcontext;
    private OnclickMessage onclickMessage;
    String image;

    public interface OnclickMessage {

        void itemOnclick(DataMessags dataMessags);
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
        holder.date.setText(list.get(position).getCreatedAt() + "");
        Log.d("hyhyyyyyyyy", "onBindViewHolder: " + list.get(position).getType());
        if (list.get(position).getIDName() != null) {
            holder.name.setText(list.get(position).getIDName() + "");
        } else {
            holder.name.setText("بدون اسم ");
        }

        if (list.get(position).getType().equals("text")) {
            holder.message.setText(list.get(position).getMessage() + "");

        } else if (list.get(position).getType().equals("audio")) {
            holder.message.setText("ملف صوتــى ");

        } else if (list.get(position).getType().equals("image")) {
            holder.message.setText("تم ارسال صورة");

        }

        Picasso.with(mcontext).load(Cemmon.BASE_URL + list.get(position).getAvatar()).placeholder(R.drawable.imagerat)
                .resize(50, 50)
                .into(holder.roundedImageView);
        holder.itemView.setOnClickListener(v -> {
            try {

                onclickMessage.itemOnclick(list.get(position));

            } catch (Exception e) {
                e.printStackTrace();
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
        TextView name, message, date;
        CircleImageView roundedImageView;
        public RelativeLayout viewBackground;
        public FrameLayout viewForeground;


        public ViewHolderVidio(@NonNull View itemView) {
            super(itemView);

            viewBackground = itemView.findViewById(R.id.view_background);
            roundedImageView = itemView.findViewById(R.id.imageView1message);
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
