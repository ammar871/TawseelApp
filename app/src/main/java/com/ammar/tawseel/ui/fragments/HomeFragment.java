package com.ammar.tawseel.ui.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ammar.tawseel.R;
import com.ammar.tawseel.adapters.AdapterFinshedOrders;
import com.ammar.tawseel.adapters.AdapterOrdersHome;
import com.ammar.tawseel.databinding.FragmentHomeBinding;
import com.ammar.tawseel.editor.ShardEditor;
import com.ammar.tawseel.netWorke.APIClient;
import com.ammar.tawseel.netWorke.APIInterFace;
import com.ammar.tawseel.pojo.data.DataFinshOrder;
import com.ammar.tawseel.pojo.data.DataOrder;
import com.ammar.tawseel.pojo.response.APIResponse;
import com.ammar.tawseel.uitllis.Cemmon;
import com.ammar.tawseel.uitllis.OnclicksInActivities;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    public OnclicksInActivities onclicksInActivities;
    DrawerLayout drawerLayout;

    public HomeFragment() {


    }

    AdapterOrdersHome adapterOrdersHome;
    AdapterFinshedOrders adapterFinshedOrders;
    FragmentHomeBinding binding;
    APIInterFace apiInterFace;
ShardEditor shardEditor;
    @SuppressLint({"UseCompatLoadingForDrawables", "RtlHardcoded"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        shardEditor=new ShardEditor(getActivity());
        if (shardEditor.loadData().get(ShardEditor.KEY_LANG)!=""){

            Cemmon.setLocale(getActivity(), shardEditor.loadData().get(ShardEditor.KEY_LANG));

        }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        apiInterFace = APIClient.getClient().create(APIInterFace.class);


        binding.rvOrders.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        binding.rvOrders.setHasFixedSize(true);

//        loadCurrentOrders();
        openDraw();

        binding.layoutFoundOrder.setOnClickListener(v -> {
            binding.layoutFoundOrder.setBackground(getResources().getDrawable(R.drawable.draw_selected_tab));
            binding.layoutNotFoundOrder.setBackground(getResources().getDrawable(R.drawable.draw_text_tab));
            binding.rvOrders.setVisibility(View.GONE);
            binding.layoutProgress.setVisibility(View.VISIBLE);
//            loadFinshidOrders();
        });
        binding.layoutNotFoundOrder.setOnClickListener(v -> {
            binding.layoutFoundOrder.setBackground(getResources().getDrawable(R.drawable.draw_text_tab));
            binding.layoutNotFoundOrder.setBackground(getResources().getDrawable(R.drawable.draw_selected_tab));
            binding.rvOrders.setVisibility(View.GONE);
            binding.layoutProgress.setVisibility(View.VISIBLE);
//            loadCurrentOrders();
        });

        return binding.getRoot();

    }

//    private void loadCurrentOrders() {
//        Call<APIResponse.ResponseCurrentOrder>call=apiInterFace.currentOrder( "application/json", "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
//                shardEditor.loadData().get(ShardEditor.KEY_LANG)
//                );
//        call.enqueue(new Callback<APIResponse.ResponseCurrentOrder>() {
//            @Override
//            public void onResponse(@NonNull Call<APIResponse.ResponseCurrentOrder> call,
//                                   @NonNull Response<APIResponse.ResponseCurrentOrder> response) {
//
//                if (response.code()==200){
//                    assert response.body() != null;
//                    if (response.body().getStauts()){
//                 adapterOrdersHome = new AdapterOrdersHome(getActivity(), (ArrayList<DataOrder>) response.body().getData());
//                 binding.rvOrders.setAdapter(adapterOrdersHome);
//                 binding.layoutProgress.setVisibility(View.GONE);
//                 binding.rvOrders.setVisibility(View.VISIBLE);
//             }
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<APIResponse.ResponseCurrentOrder> call, @NonNull Throwable t) {
//
//            }
//        });
//
//
//    }
//    private void loadFinshidOrders() {
//        Call<APIResponse.ResponseFinshedOrder>call=apiInterFace.finishedOrders(
//                "application/json", "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
//                shardEditor.loadData().get(ShardEditor.KEY_LANG)
//        );
//        call.enqueue(new Callback<APIResponse.ResponseFinshedOrder>() {
//            @Override
//            public void onResponse(@NonNull Call<APIResponse.ResponseFinshedOrder> call,
//                                   @NonNull Response<APIResponse.ResponseFinshedOrder> response) {
//
//                if (response.code()==200){
//                    assert response.body() != null;
//                    if (response.body().getStauts()){
//                        adapterFinshedOrders = new AdapterFinshedOrders(getActivity(), (ArrayList<DataFinshOrder>) response.body().getData());
//                        binding.rvOrders.setAdapter(adapterFinshedOrders);
//                        binding.layoutProgress.setVisibility(View.GONE);
//                        binding.rvOrders.setVisibility(View.VISIBLE);
//                    }
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<APIResponse.ResponseFinshedOrder> call, @NonNull Throwable t) {
//
//            }
//        });
//
//
//    }
    @SuppressLint("WrongConstant")
    private void openDraw() {
        binding.toogles.setOnClickListener((View.OnClickListener) v -> {

            DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.draw);
            drawer.openDrawer(Gravity.START);

        });
    }
}