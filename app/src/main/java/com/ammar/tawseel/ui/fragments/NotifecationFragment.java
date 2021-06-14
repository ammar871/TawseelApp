package com.ammar.tawseel.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ammar.tawseel.R;
import com.ammar.tawseel.adapters.AdapterChatePage;
import com.ammar.tawseel.adapters.AdapterMessages;
import com.ammar.tawseel.adapters.AdapterNotification;
import com.ammar.tawseel.adapters.AdapterNotiyLasted;
import com.ammar.tawseel.databinding.FragmentNotifecationBinding;
import com.ammar.tawseel.editor.ShardEditor;
import com.ammar.tawseel.helper.RecyclerItemTouchHelper;
import com.ammar.tawseel.helper.RecyclerItemTouchNotification;
import com.ammar.tawseel.netWorke.APIClient;
import com.ammar.tawseel.netWorke.APIInterFace;
import com.ammar.tawseel.pojo.data.DataNotification;
import com.ammar.tawseel.pojo.response.APIResponse;
import com.ammar.tawseel.ui.ConfirmActivity;
import com.ammar.tawseel.ui.OrdersActivity;
import com.ammar.tawseel.ui.RatingUsersActivity;
import com.ammar.tawseel.uitllis.Cemmon;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotifecationFragment extends Fragment  implements RecyclerItemTouchNotification.RecyclerItemTouchHelperListener{

int page=1;
    public NotifecationFragment() {
        // Required empty public constructor
    }

    FragmentNotifecationBinding binding;
    APIInterFace apiInterFace;
    ShardEditor shardEditor;
    AdapterNotiyLasted adapterNotiyLasted;
    ArrayList<DataNotification> listNotiyToday = new ArrayList<>();
    ArrayList<DataNotification> listlast = new ArrayList<>();
    AdapterNotification adapterNotification;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        shardEditor = new ShardEditor(getActivity());

        if (shardEditor.loadData().get(ShardEditor.KEY_LANG) != "") {

            Cemmon.setLocale(getActivity(), shardEditor.loadData().get(ShardEditor.KEY_LANG));

        }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notifecation, container, false);

        apiInterFace = APIClient.getClient().create(APIInterFace.class);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchNotification(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rvListNotifyToday);

        inItView();
        openDraw();
        loadDataNotification(page+"");

binding.homeContant.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
          binding.proBarPag.setVisibility(View.VISIBLE);

          page++;
          loadDataNotificationPagenation(page+"");


        }
    }
});


        return binding.getRoot();

    }

    private void inItView() {
//        binding.rvListNotify.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
//        binding.rvListNotify.setHasFixedSize(true);

        binding.rvListNotifyToday.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        binding.rvListNotifyToday.setHasFixedSize(true);
    }

    private void loadDataNotification(String page) {
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
                        listlast= (ArrayList<DataNotification>) response.body().getData();
//                        Log.d("responses", "onResponse: " + listNotiyToday.size() + listlast.size());
                        adapterNotification = new AdapterNotification(listlast, getActivity(),
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

                       /* for (int i = 0; i < response.body().getData().size(); i++) {

                            if (response.body().getData().get(i).getReaded().equalsIgnoreCase("0")) {

                                listNotiyToday.add(response.body().getData().get(i));
                            } else {
                                listlast.add(response.body().getData().get(i));
                                Cemmon.DATE_AND_TIME = response.body().getData().get(i).getCreatedAt();

                                @SuppressLint("SimpleDateFormat") SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");

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
                        });*/
//                        binding.rvListNotify.setAdapter(adapterNotiyLasted);
                        binding.layoutProgress.setVisibility(View.GONE);
                        binding.homeContant.setVisibility(View.VISIBLE);


                    }

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
                        binding.proBarPag.setVisibility(View.GONE);
                        listlast.addAll((ArrayList<DataNotification>) response.body().getData());

                        adapterNotification = new AdapterNotification(listlast, getActivity(),
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

                       /* for (int i = 0; i < response.body().getData().size(); i++) {

                            if (response.body().getData().get(i).getReaded().equalsIgnoreCase("0")) {

                                listNotiyToday.add(response.body().getData().get(i));
                            } else {
                                listlast.add(response.body().getData().get(i));
                                Cemmon.DATE_AND_TIME = response.body().getData().get(i).getCreatedAt();

                                @SuppressLint("SimpleDateFormat") SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");

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
                        });*/
//                        binding.rvListNotify.setAdapter(adapterNotiyLasted);



                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseNotification> call, @NonNull Throwable t) {
                binding.proBarPag.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof AdapterNotification.ViewHolderVidio) {


            Log.d("idOrder", "onSwiped: "+listlast.get(viewHolder.getAdapterPosition()).getTarget());
            deletFromNotifcation(listlast.get(viewHolder.getAdapterPosition()).getId(), listlast
                    .get(viewHolder.getAdapterPosition()).getTarget());
            adapterNotification.removeItem(viewHolder.getAdapterPosition());
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

    private void deletFromNotifcation(Integer id, String target) {

        Call<APIResponse.ResponseDeleteChat> chatCall = apiInterFace.deleteNotifecatio(
                id+"", "application/json", "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                shardEditor.loadData().get(ShardEditor.KEY_LANG)
        );
        chatCall.enqueue(new Callback<APIResponse.ResponseDeleteChat>() {
            @Override
            public void onResponse(Call<APIResponse.ResponseDeleteChat> call, Response<APIResponse.ResponseDeleteChat> response) {
                if (response.code() == 200) {
                    binding.layoutProgress.setVisibility(View.GONE);
                    binding.homeContant.setVisibility(View.VISIBLE);
                    if (response.body().getStatus()) {


                        Snackbar snackbar = Snackbar
                                .make(binding.coordinatorLayout, target + " تم حذفه من قائمة الاشعارات " , Snackbar.LENGTH_LONG);

                        snackbar.setActionTextColor(Color.YELLOW);
                        snackbar.show();
                    }
                }
                else {
                    binding.layoutProgress.setVisibility(View.GONE);
                    binding.homeContant.setVisibility(View.VISIBLE);
                    Log.d("eiled", "onResponse: "+response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<APIResponse.ResponseDeleteChat> call, Throwable t) {
                Log.d("eiled", "onResponse: "+t.getMessage());
            }
        });
    }
}