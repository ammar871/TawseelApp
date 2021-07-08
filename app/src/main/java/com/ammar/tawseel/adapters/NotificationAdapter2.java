package com.ammar.tawseel.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ammar.tawseel.R;
import com.ammar.tawseel.helper.RecyclerItemTouchNotification;
import com.ammar.tawseel.netWorke.APIClient;
import com.ammar.tawseel.netWorke.APIInterFace;
import com.ammar.tawseel.pojo.data.DataNotification;
import com.ammar.tawseel.pojo.response.APIResponse;
import com.ammar.tawseel.uitllis.Cemmon;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationAdapter2 extends RecyclerView.Adapter<NotificationAdapter2.VH> {
    Context context;
    Map<Integer, List<DataNotification>> notificationList;
    private OnclickMessage onclickMessage;

    public NotificationAdapter2(Context context, Map<Integer, List<DataNotification>> notificationList, OnclickMessage onclickMessage) {
        this.context = context;
        this.notificationList = notificationList;
        this.onclickMessage = onclickMessage;
    }

    public NotificationAdapter2(Context context, Map<Integer, List<DataNotification>> notificationList) {
        this.context = context;
        this.notificationList = notificationList;

    }

    public interface OnclickMessage {

        public void deletingFromNoty(Integer dataNotification, int i, String target);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        view = layoutInflater.inflate(R.layout.item_home_noty, parent, false);
        return new VH(view);

    }

    AlertDialog alertDialog = null;

    AdapterNotification notificationAdapter;

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        List<DataNotification> notifications = notificationList.get(position);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
        long time;


        try {

            if (notifications.size() > 0) {
                time = sdf.parse(notifications.get(0).getCreatedAt()).getTime();
                String strDate = dateFormat.format(time);
                holder.title.setText(strDate);
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
        notificationAdapter = new AdapterNotification((ArrayList<DataNotification>) notifications, context);
        holder.notificationBody.setAdapter(notificationAdapter);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchNotification(0, ItemTouchHelper.RIGHT, (viewHolder, direction, position1) -> {
            if (viewHolder instanceof AdapterNotification.ViewHolderVidio) {
                View customLayout = LayoutInflater.from(context).inflate(R.layout.dialog_delete, null);


                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setView(customLayout);


                TextView tv_delete = customLayout.findViewById(R.id.tv_delete);
                TextView tv_cancel = customLayout.findViewById(R.id.tv_cancel);

                tv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        //    deletFromNotifcation();
//

//

                 /*   onclickMessage.deletingFromNoty(notifications.get(viewHolder.getAdapterPosition()).getId(),viewHolder.getAdapterPosition(),
                            (notifications.get(getAdapterPosition()).getTarget()+""));*/
                        if (notifications.size() == 1) {
                            Log.d("swippp", "onClick: " + notifications.get(viewHolder.getAdapterPosition()).getId()
                            + notifications.size());
//                deletFromNotifcation(notifications.get(position).getId() , viewHolder.getAdapterPosition());
//                            onclickMessage.deletingFromNoty(notifications.get(viewHolder.getAdapterPosition()).getId(), viewHolder.getAdapterPosition(),
//                                    (notifications.get(viewHolder.getAdapterPosition()).getTarget() + ""));

                            notificationAdapter.removeItem(viewHolder.getAdapterPosition());

                            notificationAdapter.notifyDataSetChanged();

                            removeItemParent(position);
                            notifyDataSetChanged();
//
                        } else {
                            Log.d("swippp", "onClick: " + notifications.get(viewHolder.getAdapterPosition()).getId());
                            onclickMessage.deletingFromNoty(notifications.get(viewHolder.getAdapterPosition()).getId(), viewHolder.getAdapterPosition(),
                                    (notifications.get(viewHolder.getAdapterPosition()).getTarget() + ""));
                            notificationAdapter.removeItem(viewHolder.getAdapterPosition());
                            notificationAdapter.notifyDataSetChanged();
                            notifyDataSetChanged();
                        }

//

//
//                            notifyDataSetChanged();


                        alertDialog.dismiss();
                    }
                });

                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        notificationAdapter.notifyDataSetChanged();
                        notifyDataSetChanged();

                        alertDialog.dismiss();
                    }
                });


                builder.setCancelable(true);
                alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(holder.notificationBody);

    }

    public void removeItem(int position) {


        notificationList.get(position).remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void removeItemParent(int position) {


        notificationList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public void addMore(Map<Integer, List<DataNotification>> longListMap) {
        notificationList.putAll(longListMap);
        notifyDataSetChanged();
    }

    public class VH extends RecyclerView.ViewHolder {
        private AppCompatTextView title;
        private RecyclerView notificationBody;

        public VH(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title_noty);
            notificationBody = (RecyclerView) itemView.findViewById(R.id.rv_list_notify_home);

            notificationBody.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));


        }


//        @Override
//        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
////            deletFromNotifcation(notifications.get(position).getId() , viewHolder.getAdapterPosition());
//
//            if (viewHolder instanceof AdapterNotification.ViewHolderVidio) {
//                View customLayout = LayoutInflater.from(context).inflate(R.layout.dialog_delete, null);
//
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(context)
//                        .setView(customLayout);
//
//
//                TextView tv_delete = customLayout.findViewById(R.id.tv_delete);
//                TextView tv_cancel = customLayout.findViewById(R.id.tv_cancel);
//
//                tv_delete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//
//
//                  //    deletFromNotifcation();
////
//
////
//
//                     /*   onclickMessage.deletingFromNoty(notifications.get(viewHolder.getAdapterPosition()).getId(),viewHolder.getAdapterPosition(),
//                                (notifications.get(getAdapterPosition()).getTarget()+""));*/
////                        if (notifications.size() == 1){
//                          Log.d("swippp", "onClick: " + notifications.get(viewHolder.getAdapterPosition()).getId());
////                deletFromNotifcation(notifications.get(position).getId() , viewHolder.getAdapterPosition());
////                            onclickMessage.deletingFromNoty(notifications.get(position).getId(),viewHolder.getAdapterPosition(),
////                                    (notifications.get(getAdapterPosition()).getTarget()+""));
//////                            removeItemParent(getAdapterPosition());
//////                            notificationAdapter.removeItem(viewHolder.getAdapterPosition());
//////                            notificationAdapter.notifyDataSetChanged();
//////                            notifyDataSetChanged();
////
////                        }else {
//////                       deletFromNotifcation(notifications.get(position).getId(),viewHolder.getAdapterPosition());
////                            onclickMessage.deletingFromNoty(notifications.get(position).getId(),viewHolder.getAdapterPosition(),
////                                    (notifications.get(getAdapterPosition()).getTarget()+""));
//////                            notificationAdapter.removeItem(viewHolder.getAdapterPosition());
//////                            notificationAdapter.notifyDataSetChanged();
//////                            notifyDataSetChanged();
////                        }
//
////
//
////
////                            notifyDataSetChanged();
//
//
//
//                        alertDialog.dismiss();
//                    }
//                });
//
//                tv_cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        notificationAdapter.notifyDataSetChanged();
//                        notifyDataSetChanged();
//
//                        alertDialog.dismiss();
//                    }
//                });
//
//
//                builder.setCancelable(true);
//                alertDialog = builder.create();
//                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                alertDialog.show();
//            }
//
//        }


        private void deletFromNotifcation(Integer id, int index) {
            APIInterFace apiInterFace = APIClient.getClient().create(APIInterFace.class);
            Call<APIResponse.ResponseDeleteChat> chatCall = apiInterFace.deleteNotifecatio(
                    id + "", "application/json", "Bearer" + " " + Cemmon.USER_TOKEN,
                    Cemmon.LONG_USER);
            chatCall.enqueue(new Callback<APIResponse.ResponseDeleteChat>() {
                @Override
                public void onResponse(Call<APIResponse.ResponseDeleteChat> call, Response<APIResponse.ResponseDeleteChat> response) {
                    if (response.code() == 200) {
//                            binding.layoutProgress.setVisibility(View.GONE);
//                            binding.homeContant.setVisibility(View.VISIBLE);
                        if (response.body().getStatus()) {
                            //   loadDataNotification(page + "");
                            Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();

//                                Snackbar snackbar = Snackbar
//                                        .make(, target + " تم حذفه من قائمة الاشعارات ", Snackbar.LENGTH_LONG);
//
//                                snackbar.setActionTextColor(Color.YELLOW);
//                                snackbar.show();
                        }
                    } else {

                        Log.d("eiled", "onResponse: " + response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<APIResponse.ResponseDeleteChat> call, Throwable t) {
                    Log.d("eiled", "onResponse: " + t.getMessage());
                }
            });
        }


    }
}
