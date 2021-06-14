package com.ammar.tawseel.adapters;

import android.annotation.SuppressLint;
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
import com.ammar.tawseel.pojo.data.DataNotification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterNotiyLasted extends RecyclerView.Adapter<AdapterNotiyLasted.ViewHolderVidio> {

    ArrayList<DataNotification> list;
    private Context mcontext;
    private OnclickMessage onclickMessage;

    public interface OnclickMessage {

        public void itemOnclickNewBill(DataNotification dataNotification, int i);
    }

    int layout;

    public AdapterNotiyLasted(ArrayList<DataNotification> list, Context mcontext, OnclickMessage onclickMessage) {
        this.list = list;
        this.mcontext = mcontext;
        this.onclickMessage = onclickMessage;
    }

    public AdapterNotiyLasted(ArrayList<DataNotification> list, Context mcontext) {


        this.mcontext = mcontext;
        this.list = list;

    }

    @NonNull
    @Override
    public ViewHolderVidio onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notify, parent, false);
        return new ViewHolderVidio(view);

    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolderVidio holder, final int position) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");


        Date d = null;

        try {
            d = input.parse(list.get(position).getCreatedAt());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formatted = output.format(d);

        holder.date_notiy.setText(formatted + "");


        if (list.get(position).getType().equals("new-bill")) {
            holder.name_notiy.setText("فاتورةجدیدةرقم ");
            holder.desc_noty.setText(list.get(position).getTarget() + "");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onclickMessage.itemOnclickNewBill(list.get(position), 0);
                }
            });
        } else if (list.get(position).getType().equals("deliver-order")) {

            holder.name_notiy.setText("اشعارطلب توصیل للطلب رقم ");
            holder.desc_noty.setText(list.get(position).getTarget() + "");
            onclickMessage.itemOnclickNewBill(list.get(position), 1);

        } else if (list.get(position).getType().equals("deliver-confirm")) {
            holder.name_notiy.setText("لقد تم توصيل طلبك  ");
            holder.desc_noty.setText(list.get(position).getTarget() + "");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onclickMessage.itemOnclickNewBill(list.get(position), 2);
                }
            });

        } else if (list.get(position).getType().equals("accept-order")) {
            holder.name_notiy.setText("تم تأكید طلب المراسلة" + list.get(position).getTarget() + "  للطلب من قبل مندوب التوصیل  ");
            holder.desc_noty.setText(list.get(position).getTarget() + "");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onclickMessage.itemOnclickNewBill(list.get(position), 3);
                }
            });

        } else if (list.get(position).getType().equals("refuse-order")) {

            holder.name_notiy.setText("من قب لمندوب التوصيل" + list.get(position).getTarget() + "  تم رفض طلب المراسلة للطلب   ");
            holder.desc_noty.setText(list.get(position).getTarget() + "");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onclickMessage.itemOnclickNewBill(list.get(position), 4);
                }
            });

        } else if (list.get(position).getType().equals("user-add-rate")) {
            holder.name_notiy.setText("تم تقییم السائق: ");
            holder.desc_noty.setText(list.get(position).getTarget() + "");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onclickMessage.itemOnclickNewBill(list.get(position), 5);
                }
            });
        } else if (list.get(position).getType().equals("paid-bill")) {
            holder.name_notiy.setText("تم دفع فاتورةرقم");
            holder.desc_noty.setText(list.get(position).getTarget() + "");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onclickMessage.itemOnclickNewBill(list.get(position), 6);
                }
            });
        } else if (list.get(position).getType().equals("cancel-bill")) {
            holder.name_notiy.setText("تم الغاء فاتورة رقم :");
            holder.desc_noty.setText(list.get(position).getTarget() + "");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onclickMessage.itemOnclickNewBill(list.get(position), 7);
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
                    onclickMessage.itemOnclickNewBill(list.get(position), 8);
                }
            });
        }
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
