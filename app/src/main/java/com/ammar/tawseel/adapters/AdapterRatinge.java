package com.ammar.tawseel.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ammar.tawseel.R;
import com.ammar.tawseel.pojo.data.DataNotification;
import com.ammar.tawseel.pojo.data.DriverRate;
import com.ammar.tawseel.pojo.data.Rating;
import com.ammar.tawseel.pojo.response.APIResponse;
import com.ammar.tawseel.ui.TrakingMapsActivity;
import com.ammar.tawseel.uitllis.Cemmon;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Callback;

public class AdapterRatinge extends RecyclerView.Adapter<AdapterRatinge.ViewHolderVidio> {

    ArrayList<Rating> list;
    private Context mcontext;
    private OnclickMessage onclickMessage;

    public interface OnclickMessage {

        public void itemOnclickNewRating(Rating dataNotification);
    }

    int layout;

    public AdapterRatinge(ArrayList<Rating> list, Context mcontext, OnclickMessage onclickMessage) {
        this.list = list;
        this.mcontext = mcontext;
        this.onclickMessage = onclickMessage;
    }

    public AdapterRatinge(List<Rating> list, Context mcontext) {


        this.mcontext = mcontext;
        this.list = (ArrayList<Rating>) list;

    }

    @NonNull
    @Override
    public ViewHolderVidio onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_raitng_menus, parent, false);
        return new ViewHolderVidio(view);

    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolderVidio holder, final int position) {
        holder.name_rate.setText(list.get(position).getDriver().getIDName());
        holder.comment.setText(list.get(position).getText());
        holder.ratingBar.setRating(Float.parseFloat(list.get(position).getStars()));
        if (list.get(position).getDriver().getAvatar() != null) {
            Glide.with(mcontext).load(Cemmon.BASE_URL+list.get(position).getDriver().getAvatar()).placeholder(R.drawable.imagerat).into(holder.imageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickMessage.itemOnclickNewRating(list.get(position));
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


        public RelativeLayout viewBackground;
        public FrameLayout viewForeground;
        TextView name_rate, comment;
        CircleImageView imageView;
        AppCompatRatingBar ratingBar;
        CardView cardView;

        public ViewHolderVidio(@NonNull View itemView) {
            super(itemView);


            name_rate = itemView.findViewById(R.id.tv_nameerating);
            comment = itemView.findViewById(R.id.tv_commentr);
            ratingBar = itemView.findViewById(R.id.tv_ratingm);
            imageView = itemView.findViewById(R.id.imageViewraitng);

        }


//        public void restoreItem(ClipData.Item item, int position) {
//            cartList.add(position, item);
//            // notify item added by position
//            notifyItemInserted(position);
//        }
    }
}

