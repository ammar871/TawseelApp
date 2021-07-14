package com.ammar.tawseel.ui.fragments;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ammar.tawseel.R;
import com.ammar.tawseel.adapters.AdapterNotification;
import com.ammar.tawseel.adapters.AdapterNotiyLasted;
import com.ammar.tawseel.adapters.NotificationAdapter2;
import com.ammar.tawseel.databinding.FragmentNotifecationBinding;
import com.ammar.tawseel.editor.ShardEditor;
import com.ammar.tawseel.helper.RecyclerHelperToutchNotyLast;
import com.ammar.tawseel.helper.RecyclerItemTouchNotification;
import com.ammar.tawseel.netWorke.APIClient;
import com.ammar.tawseel.netWorke.APIInterFace;
import com.ammar.tawseel.pojo.data.DataNotification;
import com.ammar.tawseel.pojo.data.ModelNotifyDate;
import com.ammar.tawseel.pojo.response.APIResponse;
import com.ammar.tawseel.uitllis.Cemmon;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotifecationFragment extends Fragment implements
        RecyclerItemTouchNotification.RecyclerItemTouchHelperListener, RecyclerHelperToutchNotyLast.RecyclerItemTouchHelperListener {

    int page = 1;
    Map<Integer, List<DataNotification>> lists;

    public NotifecationFragment() {
        // Required empty public constructor
    }

    ArrayList<ModelNotifyDate> modelNotifyDates = new ArrayList<>();
    HashMap<Integer, List<DataNotification>> hashMap = new HashMap<Integer, List<DataNotification>>();
    FragmentNotifecationBinding binding;
    APIInterFace apiInterFace;
    ShardEditor shardEditor;
    AdapterNotification adapterNotificationToday;
    AdapterNotiyLasted adapterNotificationAuther;

    String currentDate;
    ArrayList<DataNotification> listToday = new ArrayList<>();
    ArrayList<DataNotification> listauther = new ArrayList<>();
    ArrayList<DataNotification> listlast = new ArrayList<>();
    NotificationAdapter2 adapter2;
    ArrayList<DataNotification> listlastnew = new ArrayList<>();
    AdapterNotification adapterNotification;
    ArrayList<ModelNotifyDate> listHome = new ArrayList<>();
    public static final String TAG = "Notifecation";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        shardEditor = new ShardEditor(getActivity());

        if (shardEditor.loadData().get(ShardEditor.KEY_LANG) != "") {

            Cemmon.setLocale(getActivity(), shardEditor.loadData().get(ShardEditor.KEY_LANG));

        }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notifecation, container, false);
        binding.rvListNotifyToday.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        if (Cemmon.isNetworkOnline(getActivity())) {

            if (binding.layoutLocationInternet.getVisibility() == View.VISIBLE) {
                binding.layoutLocationInternet.setVisibility(View.GONE);
            }
            inItView();
            binding.layoutHome.setVisibility(View.VISIBLE);

            apiInterFace = APIClient.getClient().create(APIInterFace.class);
            ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchNotification(0, ItemTouchHelper.RIGHT, this);
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rvListNotifyToday);
            Date c = Calendar.getInstance().getTime();

            @SuppressLint("SimpleDateFormat") SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy", new Locale("en"));
            currentDate = output.format(c);
            loadDataNotificationTwoAdapter(page + "");
            //    loadDataNotification(page + "");

            ItemTouchHelper.SimpleCallback itemTouchHelperCallbacklast = new RecyclerHelperToutchNotyLast(0, ItemTouchHelper.RIGHT, this);
            new ItemTouchHelper(itemTouchHelperCallbacklast).attachToRecyclerView(binding.rvListNotifyLast);
            openDraw();
            binding.homeContant.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                        binding.proBarPag.setVisibility(View.VISIBLE);


                        if (listlastnew.size() == 0) {
                            binding.proBarPag.setVisibility(View.GONE);

                        } else {
                            page++;
                            Log.d("pppppppppppppp", "onScrollChange: " + page);
                            loadDataNotificationPagenation(page + "");
                        }


                    }
                }
            });
        } else {
            binding.layoutLocationInternet.setVisibility(View.VISIBLE);
            binding.layoutHome.setVisibility(View.GONE);
        }

        binding.btnDissmes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_MAIN, null);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
                    intent.setComponent(cn);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (ActivityNotFoundException ignored) {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }
            }
        });

        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    private void inItView() {
        binding.rvListNotifyLast.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        binding.rvListNotifyLast.setHasFixedSize(true);

        binding.rvListNotifyToday.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        binding.rvListNotifyToday.setHasFixedSize(true);
    }

    String formatDate(String date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");

        Date d = null;

        try {
            d = input.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formatted = output.format(d);

        return formatted;
    }

    int i = 0;
    int id = 1;
    List<DataNotification> dayDates;

    private void loadDataNotification(String page) {

        binding.layoutProgress.setVisibility(View.VISIBLE);
        binding.homeContant.setVisibility(View.GONE);
        Call<APIResponse.ResponseNotification> call = apiInterFace.getNotification(page, "application/json", "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                shardEditor.loadData().get(ShardEditor.KEY_LANG)
        );
        call.enqueue(new Callback<APIResponse.ResponseNotification>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseNotification> call,
                                   @NonNull Response<APIResponse.ResponseNotification> response) {

                if (response.code() == 200) {
                    assert response.body() != null;
                    listlastnew.clear();
                    if (response.body().getStatus()) {
                        listlastnew = (ArrayList<DataNotification>) response.body().getData();
                        long millisPerDay = TimeUnit.DAYS.toMillis(1);
                        Map<Long, List<DataNotification>> datesByDay = new HashMap<>();
                        Map<Integer, List<DataNotification>> datesByDay1 = new HashMap<>();
                        for (DataNotification date : listlastnew) {
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                                long time = 0;

                                time = sdf.parse(date.getCreatedAt()).getTime();

                                long day = time / millisPerDay;
                                dayDates = datesByDay.get(day);
                                if (dayDates == null) {
                                    dayDates = new ArrayList<>();
                                    datesByDay.put(day, dayDates);
                                    datesByDay1.put(i, dayDates);
                                    i++;
                                }
                                dayDates.add(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.e("fffffffffffffff", datesByDay1.toString());

                        adapter2.addMore(datesByDay1);

                        binding.rvListNotifyToday.setAdapter(adapter2);
                        binding.layoutProgress.setVisibility(View.GONE);
                        binding.homeContant.setVisibility(View.VISIBLE);
                    }


                } else if (response.code() == 401) {
                    LayoutInflater inflater = LayoutInflater.from(getActivity());
                    View view = inflater.inflate(R.layout.dialog_logout, null);


                    AlertDialog alertDialog1 = new AlertDialog.Builder(getActivity())
                            .setView(view)
                            .create();
                    alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alertDialog1.show();

                    new CountDownTimer(3000, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onFinish() {
                            shardEditor.logOut();

                            alertDialog1.dismiss();
                        }
                    }.start();


                }

            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseNotification> call, @NonNull Throwable t) {
                binding.layoutProgress.setVisibility(View.GONE);
                binding.homeContant.setVisibility(View.VISIBLE);
            }
        });


    }


    private void loadDataNotificationTwoAdapter(String page) {

        binding.layoutProgress.setVisibility(View.VISIBLE);
        binding.homeContant.setVisibility(View.GONE);
        Call<APIResponse.ResponseNotification> call = apiInterFace.getNotification(page, "application/json", "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                shardEditor.loadData().get(ShardEditor.KEY_LANG)
        );
        call.enqueue(new Callback<APIResponse.ResponseNotification>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseNotification> call,
                                   @NonNull Response<APIResponse.ResponseNotification> response) {

                if (response.code() == 200) {
                    assert response.body() != null;
                    listlastnew.clear();
                    if (response.body().getStatus()) {
                        listlastnew = (ArrayList<DataNotification>) response.body().getData();

                        @SuppressLint("SimpleDateFormat") SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy", new Locale("en"));

                        Date d = null;
                        Date d2 = null;


                        for (int i = 0; i < listlastnew.size(); i++) {

                            try {
                                d = input.parse(listlastnew.get(i).getCreatedAt());

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            String formatted = output.format(d);

                            Log.d(TAG, "onResponse: " + currentDate + "\n" + formatted);
                            if (currentDate.equalsIgnoreCase(formatted)) {
                                listToday.add(listlastnew.get(i));


                            } else {
                                listauther.add(listlastnew.get(i));
                            }


                        }

                        if (listToday.size() == 0) {
                            binding.tvToday.setVisibility(View.GONE);
                        } else {
                            binding.tvToday.setVisibility(View.VISIBLE);
                        }
                        if (listauther.size() > 0) {

                            binding.otherDate.setVisibility(View.VISIBLE);
                            try {
                                d2 = input.parse(listauther.get(i).getCreatedAt());
                                String formatted2 = output.format(d2);
                                binding.otherDate.setText(formatted2 + "");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        } else {
                            binding.otherDate.setVisibility(View.GONE);
                        }

                        adapterNotification = new AdapterNotification(listToday, getActivity());
                        binding.rvListNotifyToday.setAdapter(adapterNotification);

                        adapterNotificationAuther = new AdapterNotiyLasted(listauther, getActivity());
                        binding.rvListNotifyLast.setAdapter(adapterNotificationAuther);

                        binding.layoutProgress.setVisibility(View.GONE);
                        binding.homeContant.setVisibility(View.VISIBLE);
                    }


                } else if (response.code() == 401) {
                    LayoutInflater inflater = LayoutInflater.from(getActivity());
                    View view = inflater.inflate(R.layout.dialog_logout, null);


                    AlertDialog alertDialog1 = new AlertDialog.Builder(getActivity())
                            .setView(view)
                            .create();
                    alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alertDialog1.show();

                    new CountDownTimer(3000, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onFinish() {
                            shardEditor.logOut();

                            alertDialog1.dismiss();
                        }
                    }.start();


                }

            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseNotification> call, @NonNull Throwable t) {
                binding.layoutProgress.setVisibility(View.GONE);
                binding.homeContant.setVisibility(View.VISIBLE);
            }
        });


    }


    @SuppressLint("WrongConstant")
    private void openDraw() {
        binding.toggles.setOnClickListener((View.OnClickListener) v -> {

            DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.draw);
            drawer.openDrawer(Gravity.START);

        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void loadDataNotificationPagenation(String page) {
        Call<APIResponse.ResponseNotification> call = apiInterFace.getNotification(page, "application/json", "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                shardEditor.loadData().get(ShardEditor.KEY_LANG)
        );
        call.enqueue(new Callback<APIResponse.ResponseNotification>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseNotification> call,
                                   @NonNull Response<APIResponse.ResponseNotification> response) {

                if (response.code() == 200) {
                    assert response.body() != null;

                    if (response.body().getStatus()) {
                      ArrayList<DataNotification>list=new ArrayList<>();
                        list= (ArrayList<DataNotification>) response.body().getData();

                        @SuppressLint("SimpleDateFormat") SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy", new Locale("en"));

                        Date d = null;
                        Date d2 = null;


                        for (int i = 0; i < list.size(); i++) {

                            try {
                                d = input.parse(listlastnew.get(i).getCreatedAt());

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            String formatted = output.format(d);

                            Log.d(TAG, "onResponse: " + currentDate + "\n" + formatted);
                            if (currentDate.equalsIgnoreCase(formatted)) {
                                listToday.add(list.get(i));


                            } else {
                                listauther.add(list.get(i));
                            }


                        }

                        if (listToday.size() == 0) {
                            binding.tvToday.setVisibility(View.GONE);
                        } else {
                            binding.tvToday.setVisibility(View.VISIBLE);
                        }
                        if (listauther.size() > 0) {

                            binding.otherDate.setVisibility(View.VISIBLE);
                            try {
                                d2 = input.parse(listauther.get(i).getCreatedAt());
                                String formatted2 = output.format(d2);
                                binding.otherDate.setText(formatted2 + "");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        } else {
                            binding.otherDate.setVisibility(View.GONE);
                        }


                        binding.rvListNotifyToday.setAdapter(adapterNotification);


                        binding.rvListNotifyLast.setAdapter(adapterNotificationAuther);

                        binding.proBarPag.setVisibility(View.GONE);

                    }


                } else if (response.code() == 401) {
                    LayoutInflater inflater = LayoutInflater.from(getActivity());
                    View view = inflater.inflate(R.layout.dialog_logout, null);


                    AlertDialog alertDialog1 = new AlertDialog.Builder(getActivity())
                            .setView(view)
                            .create();
                    alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alertDialog1.show();

                    new CountDownTimer(3000, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onFinish() {
                            shardEditor.logOut();

                            alertDialog1.dismiss();
                        }
                    }.start();


                }

            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseNotification> call, @NonNull Throwable t) {
            binding.proBarPag.setVisibility(View.GONE);
            }
        });



    }

    @Override
    public void onStart() {
        super.onStart();

    }

    AlertDialog alertDialog = null;


    private void dialog_Deleting(RecyclerView.ViewHolder viewHolder) {
        View customLayout = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_delete, null);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(customLayout);


        TextView tv_delete = customLayout.findViewById(R.id.tv_delete);
        TextView tv_cancel = customLayout.findViewById(R.id.tv_cancel);

        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                deletFromNotifcation(listNotiyToday.get(viewHolder.getAdapterPosition()).getId(), listNotiyToday
//                        .get(viewHolder.getAdapterPosition()).getTarget() + "");
//                adapterNotification.removeItem(viewHolder.getAdapterPosition());
                alertDialog.dismiss();
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterNotification.notifyDataSetChanged();
                alertDialog.dismiss();
            }
        });


        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    private void deletFromNotifcation(Integer id, String target,int posstion) {

        Call<APIResponse.ResponseDeleteChat> chatCall = apiInterFace.deleteNotifecatio(
                id + "", "application/json", "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                shardEditor.loadData().get(ShardEditor.KEY_LANG)
        );
        chatCall.enqueue(new Callback<APIResponse.ResponseDeleteChat>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseDeleteChat> call, @NonNull Response<APIResponse.ResponseDeleteChat> response) {
                if (response.code() == 200) {

                    binding.layoutProgress.setVisibility(View.GONE);
                    binding.homeContant.setVisibility(View.VISIBLE);
                    if (response.body().getStatus()) {

                        loadDataNotificationTwoAdapter(page + "");
//                      adapter2.notifyDataSetChanged();
                    adapterNotification.removeItem(posstion);

                        Snackbar snackbar = Snackbar
                                .make(binding.coordinatorLayout, target + getString(R.string.delete_from_noty), Snackbar.LENGTH_LONG);

                        snackbar.setActionTextColor(Color.YELLOW);
                        snackbar.show();

                    }
                } else {
                    binding.layoutProgress.setVisibility(View.GONE);
                    binding.homeContant.setVisibility(View.VISIBLE);
                    Log.d("eiled", "onResponse: " + response.body().getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseDeleteChat> call, @NonNull Throwable t) {
                Log.d("eiled", "onResponse: " + t.getMessage());
            }
        });
    }

    private void deletFromNotifcationlaste(Integer id, String target,int posstion) {

        Call<APIResponse.ResponseDeleteChat> chatCall = apiInterFace.deleteNotifecatio(
                id + "", "application/json", "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                shardEditor.loadData().get(ShardEditor.KEY_LANG)
        );
        chatCall.enqueue(new Callback<APIResponse.ResponseDeleteChat>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseDeleteChat> call, @NonNull Response<APIResponse.ResponseDeleteChat> response) {
                if (response.code() == 200) {

                    binding.layoutProgress.setVisibility(View.GONE);
                    binding.homeContant.setVisibility(View.VISIBLE);
                    if (response.body().getStatus()) {

                        loadDataNotificationTwoAdapter(page + "");
//                      adapter2.notifyDataSetChanged();
                    adapterNotificationAuther.removeItem(posstion);

                        Snackbar snackbar = Snackbar
                                .make(binding.coordinatorLayout, target + getString(R.string.delete_from_noty), Snackbar.LENGTH_LONG);

                        snackbar.setActionTextColor(Color.YELLOW);
                        snackbar.show();

                    }
                } else {
                    binding.layoutProgress.setVisibility(View.GONE);
                    binding.homeContant.setVisibility(View.VISIBLE);
                    Log.d("eiled", "onResponse: " + response.body().getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseDeleteChat> call, @NonNull Throwable t) {
                Log.d("eiled", "onResponse: " + t.getMessage());
            }
        });
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof AdapterNotification.ViewHolderVidio) {
            View customLayout = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_delete, null);


            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setView(customLayout);


            TextView tv_delete = customLayout.findViewById(R.id.tv_delete);
            TextView tv_cancel = customLayout.findViewById(R.id.tv_cancel);

            tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    deletFromNotifcation(listToday.get(viewHolder.getAdapterPosition()).getId(), listToday.get(viewHolder.getAdapterPosition()).getTarget(),viewHolder.getAdapterPosition());

                    alertDialog.dismiss();
                }
            });

            tv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    adapterNotification.notifyDataSetChanged();


                    alertDialog.dismiss();
                }
            });


            builder.setCancelable(true);
            alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();
        }
    }

    AlertDialog alertDialog2 = null;

    @Override
    public void onSwipedLast(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof AdapterNotiyLasted.ViewHolderVidio) {
            View customLayout = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_delete, null);


            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setView(customLayout);


            TextView tv_delete = customLayout.findViewById(R.id.tv_delete);
            TextView tv_cancel = customLayout.findViewById(R.id.tv_cancel);

            tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    deletFromNotifcationlaste(listauther.get(viewHolder.getAdapterPosition()).getId(), listauther.get(viewHolder.getAdapterPosition()).getTarget(),viewHolder.getAdapterPosition());

                    alertDialog2.dismiss();
                }
            });

            tv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    adapterNotificationAuther.notifyDataSetChanged();


                    alertDialog2.dismiss();
                }
            });


            builder.setCancelable(true);
            alertDialog2 = builder.create();
            alertDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog2.show();
        }
    }
}

// notifecation
   /*    for (int i = 0; i < response.body().getData().size(); i++) {

                            if (response.body().getData().get(i).getReaded().equalsIgnoreCase("0")) {

                                listNotiyToday.add(response.body().getData().get(i));
                            } else {
                                listlast.add(response.body().getData().get(i));
                                Cemmon.DATE_AND_TIME = response.body().getData().get(i).getCreatedAt();

                                @SuppressLint("SimpleDateFormat") SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat output = new SimpleDateFormat("MMM  dd / yyyy ", new Locale(shardEditor.loadData().get(ShardEditor.KEY_LANG)));

                                Date d = null;

                                try {
                                    d = input.parse(response.body().getData().get(i).getCreatedAt());

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                String formatted = output.format(d);

                                binding.tvDate.setText(formatted + "");
                            }

                        }


                        Log.d("responses", "onResponse: " + listNotiyToday.size() + listlast.size());
                        adapterNotification = new AdapterNotification(listNotiyToday, getActivity(),
                                (dataNotification, i) -> {

                                    if (i == 0) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("id", dataNotification.getTarget() + "");
                                        FatoraFragment category = new FatoraFragment();
                                        category.setArguments(bundle);

                                        Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                                                .beginTransaction()
                                                .replace(R.id.layout_view, category)
                                                .addToBackStack(null)
                                                .commit();

                                    } else if (i == 1) {


                                    } else if (i == 2 || i == 3 || i == 4) {

                                        startActivity(new Intent(getActivity(), OrdersActivity.class));

                                    } else if (i == 5) {

                                        startActivity(new Intent(getActivity(), RatingUsersActivity.class));

                                    } else if (i == 6 || i == 7) {
                                        Intent intent = new Intent(getActivity(), DetailesActivity.class);
                                        intent.putExtra("idBill", dataNotification.getParams());
                                        startActivity(intent);


                                    }


                                });
                        binding.rvListNotifyToday.setAdapter(adapterNotification);


                        adapterNotiyLasted = new AdapterNotiyLasted(listlast, getActivity()
                                , (dataNotification, i) -> {
                            if (i == 0) {
                                Bundle bundle = new Bundle();
                                bundle.putString("id", dataNotification.getTarget() + "");
                                FatoraFragment category = new FatoraFragment();
                                category.setArguments(bundle);

                                Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.layout_view, category)
                                        .addToBackStack(null)
                                        .commit();

                            } else if (i == 1) {


                            } else if (i == 2 || i == 3 || i == 4) {

                                startActivity(new Intent(getActivity(), OrdersActivity.class));

                            } else if (i == 5) {

                                startActivity(new Intent(getActivity(), RatingUsersActivity.class));

                            } else if (i == 6 || i == 7) {
                                Intent intent = new Intent(getActivity(), DetailesActivity.class);
                                intent.putExtra("idBill", dataNotification.getTarget());
                                startActivity(intent);


                            }
                        });
                        binding.rvListNotify.setAdapter(adapterNotiyLasted);
                        binding.layoutProgress.setVisibility(View.GONE);
                        binding.homeContant.setVisibility(View.VISIBLE);


                    }*/