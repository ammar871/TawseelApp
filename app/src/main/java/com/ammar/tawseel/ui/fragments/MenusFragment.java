package com.ammar.tawseel.ui.fragments;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ammar.tawseel.R;
import com.ammar.tawseel.adapters.AdapterFatora;
import com.ammar.tawseel.adapters.AdapterFinshedOrders;
import com.ammar.tawseel.adapters.AdapterOrdersHome;
import com.ammar.tawseel.databinding.FragmentMenusBinding;
import com.ammar.tawseel.editor.ShardEditor;
import com.ammar.tawseel.netWorke.APIClient;
import com.ammar.tawseel.netWorke.APIInterFace;
import com.ammar.tawseel.pojo.data.DataFatora;
import com.ammar.tawseel.pojo.data.DataFinshOrder;
import com.ammar.tawseel.pojo.data.DataOrder;
import com.ammar.tawseel.pojo.response.APIResponse;
import com.ammar.tawseel.uitllis.Cemmon;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenusFragment extends Fragment implements AdapterFatora.OnclickMessage{


    public MenusFragment() {

    }

    APIInterFace apiInterFace;
    ShardEditor shardEditor;
    FragmentMenusBinding binding;
    AdapterFatora adapterFatora;
int page = 1;
    int pageFinshe = 1;
    ArrayList<DataFatora>listcurrent=new ArrayList<>();
    ArrayList<DataFatora>listFinshed=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        shardEditor=new ShardEditor(getActivity());

        if (shardEditor.loadData().get(ShardEditor.KEY_LANG)!=""){

            Cemmon.setLocale(getActivity(), shardEditor.loadData().get(ShardEditor.KEY_LANG));

        }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menus, container, false);

        binding.rvOrdersCurrent.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        binding.rvOrdersCurrent.setHasFixedSize(true);
        binding.rvOrdersFinshed.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        binding.rvOrdersFinshed.setHasFixedSize(true);
        if (Cemmon.isNetworkOnline(getActivity())) {

            if (binding.layoutLocationInternet.getVisibility() == View.VISIBLE) {
                binding.layoutLocationInternet.setVisibility(View.GONE);
            }
            binding.layoutHome.setVisibility(View.VISIBLE);
            apiInterFace = APIClient.getClient().create(APIInterFace.class);
            loadCurrentOrders("1");
            layoutMenus();
            binding.nestscroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                        binding.proBarPag.setVisibility(View.VISIBLE);
                        page++;

                        loadCurrentBillPage(page);

                    }
                }
            });
            binding.nestscrollfinshed.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                        binding.proBarPagFinshid.setVisibility(View.VISIBLE);
                        pageFinshe++;
                        loadFinshedBillPage(pageFinshe);



                    }
                }
            });
            openDraw();
        }
        else {
            binding.layoutLocationInternet.setVisibility(View.VISIBLE);
            binding.layoutHome.setVisibility(View.GONE);
        }


        binding.btnDissmes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent(Intent.ACTION_MAIN, null);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
                    intent.setComponent(cn);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }catch (ActivityNotFoundException ignored){
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }
            }
        });
        return binding.getRoot();
    }

    private void loadFinshedBillPage(int pageFinshe) {
        Call<APIResponse.ResponseFatora> call=apiInterFace.getCanceled_bills(pageFinshe+"",
                "application/json", "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                shardEditor.loadData().get(ShardEditor.KEY_LANG)
        );
        call.enqueue(new Callback<APIResponse.ResponseFatora>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseFatora> call,
                                   @NonNull Response<APIResponse.ResponseFatora> response) {

                if (response.code()==200){
                    assert response.body() != null;
                    if (response.body().getStatus()){
                        binding.proBarPagFinshid.setVisibility(View.GONE);
                        listFinshed.addAll( response.body().getData());
                        adapterFatora = new AdapterFatora(listFinshed
                                , getActivity(), dataNotification -> {
                            Intent intent=new Intent(getActivity(),DetailesActivity.class);
                            intent.putExtra("idBill",dataNotification.getId()+"");
                            startActivityForResult(intent,1);
                        });

                    }

                }else {
                    binding.proBarPagFinshid.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseFatora> call, @NonNull Throwable t) {
                binding.proBarPagFinshid.setVisibility(View.GONE);
            }
        });



    }

    private void loadCurrentBillPage(int page) {
        Call<APIResponse.ResponseFatora> call=apiInterFace.getPaid_bills(page+"",
                "application/json", "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                shardEditor.loadData().get(ShardEditor.KEY_LANG)
        );
        call.enqueue(new Callback<APIResponse.ResponseFatora>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseFatora> call,
                                   @NonNull Response<APIResponse.ResponseFatora> response) {

                if (response.code()==200){
                    assert response.body() != null;
                    if (response.body().getStatus()){
                        binding.proBarPag.setVisibility(View.GONE);
                        listcurrent.addAll(response.body().getData());
                        adapterFatora = new AdapterFatora(listcurrent
                                , getActivity(), dataNotification -> {
                            Intent intent=new Intent(getActivity(),DetailesActivity.class);
                            intent.putExtra("idBill",dataNotification.getId()+"");
                            startActivityForResult(intent,1);
                        });

                    }else {
                        binding.proBarPag.setVisibility(View.GONE);
                    }

                }else if (response.code() == 401) {
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
            public void onFailure(@NonNull Call<APIResponse.ResponseFatora> call, @NonNull Throwable t) {
                binding.proBarPag.setVisibility(View.GONE);
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void layoutMenus() {
        binding.layoutFoundOrder.setOnClickListener(v -> {
            binding.layoutFoundOrder.setBackground(getResources().getDrawable(R.drawable.draw_selected_tab));
            binding.layoutNotFoundOrder.setBackground(getResources().getDrawable(R.drawable.draw_text_tab));
          binding.nestscroll.setVisibility(View.GONE);

            binding.layoutProgress.setVisibility(View.VISIBLE);

            loadFinshidOrders("1");

        });
        binding.layoutNotFoundOrder.setOnClickListener(v -> {
            binding.layoutFoundOrder.setBackground(getResources().getDrawable(R.drawable.draw_text_tab));
            binding.layoutNotFoundOrder.setBackground(getResources().getDrawable(R.drawable.draw_selected_tab));

            binding.nestscrollfinshed.setVisibility(View.GONE);
            binding.layoutProgress.setVisibility(View.VISIBLE);
            loadCurrentOrders("1");
        });
    }

    @SuppressLint("WrongConstant")
    private void openDraw() {
        binding.toggles.setOnClickListener((View.OnClickListener) v -> {

            DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.draw);
            drawer.openDrawer(Gravity.START);

        });
    }

    private void loadCurrentOrders(String page) {
        Call<APIResponse.ResponseFatora> call=apiInterFace.getPaid_bills(page, "application/json", "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                shardEditor.loadData().get(ShardEditor.KEY_LANG)
        );
        call.enqueue(new Callback<APIResponse.ResponseFatora>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseFatora> call,
                                   @NonNull Response<APIResponse.ResponseFatora> response) {

                if (response.code()==200){
                    assert response.body() != null;
                    if (response.body().getStatus()){
                        listcurrent=(ArrayList<DataFatora>) response.body().getData();
                        adapterFatora = new AdapterFatora(listcurrent
                                , getActivity(), dataNotification -> {
                                    Intent intent=new Intent(getActivity(),DetailesActivity.class);
                                    intent.putExtra("idBill",dataNotification.getId()+"");
                                    startActivityForResult(intent,1);
                                });
                        binding.rvOrdersCurrent.setAdapter(adapterFatora);
                        binding.layoutProgress.setVisibility(View.GONE);
                        binding.nestscroll.setVisibility(View.VISIBLE);
                    }else if (response.code() == 401) {
                        shardEditor.logOut();
                    }


                }

            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseFatora> call, @NonNull Throwable t) {
                binding.nestscroll.setVisibility(View.VISIBLE);
                binding.layoutProgress.setVisibility(View.GONE);
            }
        });


    }
    private void loadFinshidOrders(String page) {
        Call<APIResponse.ResponseFatora> call=apiInterFace.getCanceled_bills(page,
                "application/json", "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                shardEditor.loadData().get(ShardEditor.KEY_LANG)
        );
        call.enqueue(new Callback<APIResponse.ResponseFatora>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseFatora> call,
                                   @NonNull Response<APIResponse.ResponseFatora> response) {

                if (response.code()==200){
                    assert response.body() != null;
                    if (response.body().getStatus()){
                        listFinshed=(ArrayList<DataFatora>) response.body().getData();
                        adapterFatora = new AdapterFatora(listFinshed
                                , getActivity(), dataNotification -> {
                            Intent intent=new Intent(getActivity(),DetailesActivity.class);
                            intent.putExtra("idBill",dataNotification.getId()+"");
                            startActivityForResult(intent,1);
                        });
                        binding.rvOrdersFinshed.setAdapter(adapterFatora);
                        binding.layoutProgress.setVisibility(View.GONE);
                        binding.nestscrollfinshed.setVisibility(View.VISIBLE);
                    }

                }
                    else if (response.code() == 401) {
                        shardEditor.logOut();
                    }



            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseFatora> call, @NonNull Throwable t) {
                binding.layoutProgress.setVisibility(View.GONE);
                binding.nestscrollfinshed.setVisibility(View.VISIBLE);
            }
        });


    }

    @Override
    public void itemOnclickNewFatora(DataFatora dataNotification) {
        Intent intent = new Intent(getActivity(), DetailesActivity.class);
        intent.putExtra("idBill", dataNotification.getId() + "");
        startActivity(intent);
    }
}