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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ammar.tawseel.R;
import com.ammar.tawseel.adapters.AdapterMessages;

import com.ammar.tawseel.helper.RecyclerItemTouchHelper;
import com.ammar.tawseel.databinding.FragmentMessagesBinding;
import com.ammar.tawseel.editor.ShardEditor;
import com.ammar.tawseel.netWorke.APIClient;
import com.ammar.tawseel.netWorke.APIInterFace;
import com.ammar.tawseel.pojo.data.DataMessags;
import com.ammar.tawseel.pojo.response.APIResponse;
import com.ammar.tawseel.uitllis.Cemmon;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener,
        AdapterMessages.OnclickMessage {


    public MessagesFragment() {

    }

    APIInterFace apiInterFace;
    ShardEditor shardEditor;

    ArrayList<DataMessags> list = new ArrayList<>();
    AdapterMessages adapterMessages;
    FragmentMessagesBinding binding;
int page=1;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        shardEditor = new ShardEditor(getActivity());

        if (shardEditor.loadData().get(ShardEditor.KEY_LANG)!=""){

            Cemmon.setLocale(getActivity(), shardEditor.loadData().get(ShardEditor.KEY_LANG));

        }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_messages, container, false);

        if (Cemmon.isNetworkOnline(getActivity())) {

            if (binding.layoutLocationInternet.getVisibility() == View.VISIBLE) {
                binding.layoutLocationInternet.setVisibility(View.GONE);
            }
            binding.layoutHome.setVisibility(View.VISIBLE);

            apiInterFace = APIClient.getClient().create(APIInterFace.class);
            loadDataMessages("1");


            openDraw();


            ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rvMessage);

            binding.scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                        binding.proBarPagFinshid.setVisibility(View.VISIBLE);
                        page++;
                        if (list.size()>0){
                            loadMessagesPage(page);
                        }else {
                            binding.proBarPagFinshid.setVisibility(View.GONE);
                        }



                    }
                }
            });

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

    private void loadMessagesPage(int page) {

        Call<APIResponse.ResponseMessages> call = apiInterFace.getMessages(
                page+"", "application/json",
                "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                shardEditor.loadData().get(ShardEditor.KEY_LANG)
        );
        call.enqueue(new Callback<APIResponse.ResponseMessages>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseMessages> call,
                                   @NonNull Response<APIResponse.ResponseMessages> response) {

                if (response.code() == 200) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {

                        Log.d("responses", "onResponse: " + response.body().getData().size());
                        list.addAll( response.body().getData());
                        adapterMessages = new AdapterMessages(list, getActivity(), dataMessags -> {
                            Bundle bundle = new Bundle();
                            bundle.putString("orderId", dataMessags.getOrderId().toString());
                            bundle.putString("to", dataMessags.getDriverId() + "");


                            ChatFragment fragobj = new ChatFragment();
                            fragobj.setArguments(bundle);
                            Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.layout_view, fragobj)
                                    .addToBackStack(null)
                                    .commit();
                        });
                        binding.rvMessage.setAdapter(adapterMessages);
                      binding.proBarPagFinshid.setVisibility(View.GONE);
                    } else {
                        binding.proBarPagFinshid.setVisibility(View.GONE);
                    }

                }

            }


            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseMessages> call, @NonNull Throwable t) {
                binding.proBarPagFinshid.setVisibility(View.GONE);
            }
        });

    }

    private void loadDataMessages(String page) {
        binding.layoutProgress.setVisibility(View.VISIBLE);
        binding.rvMessage.setVisibility(View.GONE);

        Call<APIResponse.ResponseMessages> call = apiInterFace.getMessages(
                page, "application/json", "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                shardEditor.loadData().get(ShardEditor.KEY_LANG)
        );
        call.enqueue(new Callback<APIResponse.ResponseMessages>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseMessages> call,
                                   @NonNull Response<APIResponse.ResponseMessages> response) {

                if (response.code() == 200) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        list.clear();
                        Log.d("responses", "onResponse: " + response.body().getData().size());
                        list = (ArrayList<DataMessags>) response.body().getData();
                        setupAdapter(list);
                        binding.layoutProgress.setVisibility(View.GONE);
                        binding.rvMessage.setVisibility(View.VISIBLE);
                    } else {
                        binding.layoutProgress.setVisibility(View.GONE);
                        binding.rvMessage.setVisibility(View.VISIBLE);
                    }

                }

            }


            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseMessages> call, @NonNull Throwable t) {
                binding.layoutProgress.setVisibility(View.GONE);
                binding.rvMessage.setVisibility(View.VISIBLE);
            }
        });


    }

    private void setupAdapter(List<DataMessags> data) {
        adapterMessages = new AdapterMessages((ArrayList<DataMessags>) data, getActivity(), this);
        binding.rvMessage.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        binding.rvMessage.setHasFixedSize(true);
        binding.rvMessage.setAdapter(adapterMessages);
//        binding.rvMessage.setItemAnimator(new DefaultItemAnimator());
//        binding.rvMessage.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }

    @SuppressLint("WrongConstant")
    private void openDraw() {
        binding.toggles.setOnClickListener((View.OnClickListener) v -> {

            DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.draw);
            drawer.openDrawer(Gravity.START);

        });
    }
AlertDialog alertDialog=null;
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof AdapterMessages.ViewHolderVidio) {

            View customLayout = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_delete, null);


            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setView(customLayout);


            TextView tv_delete = customLayout.findViewById(R.id.tv_delete);
            TextView tv_cancel = customLayout.findViewById(R.id.tv_cancel);

            tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("idOrder", "onSwiped: "+list.get(viewHolder.getAdapterPosition()).getIDName());
                    deletFromChat(list.get(viewHolder.getAdapterPosition()).getOrderId(), list
                            .get(viewHolder.getAdapterPosition()).getIDName());
                    adapterMessages.removeItem(viewHolder.getAdapterPosition());
                    alertDialog.dismiss();
                }
            });

            tv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterMessages.notifyDataSetChanged();
                    alertDialog.dismiss();
                }
            });


            builder.setCancelable(true);
            alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();

            // get the removed item name to display it in snack bar
         /*   String name = cartList.get(viewHolder.getAdapterPosition()).getName();

            // backup of removed item for undo purpose
            final Item deletedItem = cartList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());

//            // showing snack bar with Undo option*/
//            Snackbar snackbar = Snackbar
//                    .make(binding.coordinatorLayout,  list.get(0).toString()+" removed from cart!", Snackbar.LENGTH_LONG);
//            snackbar.setAction("UNDO", new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    // undo is selected, restore the deleted item
//
//                }
//            });
//            snackbar.setActionTextColor(Color.YELLOW);
//            snackbar.show();
        }
    }

    private void deletFromChat(String id, String orderId) {
        binding.layoutProgress.setVisibility(View.VISIBLE);
        binding.rvMessage.setVisibility(View.GONE);
        Call<APIResponse.ResponseDeleteChat> chatCall = apiInterFace.deleteChat(
                id, "application/json", "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                shardEditor.loadData().get(ShardEditor.KEY_LANG)
        );
        chatCall.enqueue(new Callback<APIResponse.ResponseDeleteChat>() {
            @Override
            public void onResponse(Call<APIResponse.ResponseDeleteChat> call, Response<APIResponse.ResponseDeleteChat> response) {
                if (response.code() == 200) {
                    binding.layoutProgress.setVisibility(View.GONE);
                    binding.rvMessage.setVisibility(View.VISIBLE);
                    if (response.body().getStatus()) {


                        Snackbar snackbar = Snackbar
                                .make(binding.coordinatorLayout, id + " تم حذفه من قائمة الرسائل " , Snackbar.LENGTH_LONG);

                        snackbar.setActionTextColor(Color.YELLOW);
                        snackbar.show();
                    }
                }
                else {
                    binding.layoutProgress.setVisibility(View.GONE);
                    binding.rvMessage.setVisibility(View.VISIBLE);
                    Log.d("eiled", "onResponse: "+response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<APIResponse.ResponseDeleteChat> call, Throwable t) {
                Log.d("eiled", "onResponse: "+t.getMessage());
            }
        });
    }


    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.layout_view, fragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void itemOnclick(DataMessags dataMessags) {

//        Intent intent=new Intent(getActivity(), ChatAppActivity.class);
//        intent.putExtra("orderId", dataMessags.getOrderId() + "");
//        intent.putExtra("to", dataMessags.getDriverId() + "");
//        startActivity(intent);
        Bundle bundle = new Bundle();
        bundle.putString("orderId", dataMessags.getOrderId().toString());
        bundle.putString("to", dataMessags.getDriverId() + "");


        ChatFragment fragobj = new ChatFragment();
        fragobj.setArguments(bundle);
        Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_view, fragobj)
                .addToBackStack(null)
                .commit();
    }
}