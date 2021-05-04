package com.ammar.tawseel.ui.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenusFragment extends Fragment {


    public MenusFragment() {

    }

    APIInterFace apiInterFace;
    ShardEditor shardEditor;
    FragmentMenusBinding binding;
    AdapterFatora adapterFatora;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        shardEditor=new ShardEditor(getActivity());
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menus, container, false);

        binding.rvOrders.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        binding.rvOrders.setHasFixedSize(true);
        apiInterFace = APIClient.getClient().create(APIInterFace.class);
        loadCurrentOrders("1");
        layoutMenus();

        openDraw();
        return binding.getRoot();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void layoutMenus() {
        binding.layoutFoundOrder.setOnClickListener(v -> {
            binding.layoutFoundOrder.setBackground(getResources().getDrawable(R.drawable.draw_selected_tab));
            binding.layoutNotFoundOrder.setBackground(getResources().getDrawable(R.drawable.draw_text_tab));
            binding.rvOrders.setVisibility(View.GONE);
            binding.layoutProgress.setVisibility(View.VISIBLE);
            loadCurrentOrders("1");

        });
        binding.layoutNotFoundOrder.setOnClickListener(v -> {
            binding.layoutFoundOrder.setBackground(getResources().getDrawable(R.drawable.draw_text_tab));
            binding.layoutNotFoundOrder.setBackground(getResources().getDrawable(R.drawable.draw_selected_tab));
            binding.rvOrders.setVisibility(View.GONE);
            binding.layoutProgress.setVisibility(View.VISIBLE);
            loadFinshidOrders("1");
        });
    }

    private void openDraw() {
        binding.toggles.setOnClickListener((View.OnClickListener) v -> {

            DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.draw);
            drawer.openDrawer(Gravity.LEFT);

        });
    }

    private void loadCurrentOrders(String page) {
        Call<APIResponse.ResponseFatora> call=apiInterFace.getPaid_bills(page, "application/json", "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                "ar"
        );
        call.enqueue(new Callback<APIResponse.ResponseFatora>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseFatora> call,
                                   @NonNull Response<APIResponse.ResponseFatora> response) {

                if (response.code()==200){
                    assert response.body() != null;
                    if (response.body().getStatus()){
                        adapterFatora = new AdapterFatora((ArrayList<DataFatora>) response.body().getData(),getActivity());
                        binding.rvOrders.setAdapter(adapterFatora);
                        binding.layoutProgress.setVisibility(View.GONE);
                        binding.rvOrders.setVisibility(View.VISIBLE);
                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseFatora> call, @NonNull Throwable t) {

            }
        });


    }
    private void loadFinshidOrders(String page) {
        Call<APIResponse.ResponseFatora> call=apiInterFace.getCanceled_bills(page, "application/json", "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                "ar"
        );
        call.enqueue(new Callback<APIResponse.ResponseFatora>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseFatora> call,
                                   @NonNull Response<APIResponse.ResponseFatora> response) {

                if (response.code()==200){
                    assert response.body() != null;
                    if (response.body().getStatus()){
                        adapterFatora = new AdapterFatora((ArrayList<DataFatora>) response.body().getData(),getActivity());
                        binding.rvOrders.setAdapter(adapterFatora);
                        binding.layoutProgress.setVisibility(View.GONE);
                        binding.rvOrders.setVisibility(View.VISIBLE);
                    }

                }else {
                    binding.layoutProgress.setVisibility(View.GONE);
                    binding.rvOrders.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseFatora> call, @NonNull Throwable t) {
                binding.layoutProgress.setVisibility(View.GONE);
                binding.rvOrders.setVisibility(View.VISIBLE);
            }
        });


    }
}