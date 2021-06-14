package com.ammar.tawseel.ui.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ammar.tawseel.R;
import com.ammar.tawseel.databinding.FragmentFatoraBinding;
import com.ammar.tawseel.editor.ShardEditor;
import com.ammar.tawseel.netWorke.APIClient;
import com.ammar.tawseel.netWorke.APIInterFace;
import com.ammar.tawseel.pojo.response.APIResponse;
import com.ammar.tawseel.uitllis.Cemmon;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FatoraFragment extends Fragment {


    public FatoraFragment() {
        // Required empty public constructor
    }

    String idBill = "";
    FragmentFatoraBinding binding;
    APIInterFace apiInterFace;
    ShardEditor shardEditor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        shardEditor = new ShardEditor(getActivity());

        if (shardEditor.loadData().get(ShardEditor.KEY_LANG)!=""){

            Cemmon.setLocale(getActivity(), shardEditor.loadData().get(ShardEditor.KEY_LANG));

        }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fatora, container, false);
        apiInterFace = APIClient.getClient().create(APIInterFace.class);


        openDraw();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            idBill = bundle.getString("id");
            Log.d("idBill", "onCreateView: " + idBill);
            if (!idBill.equals("")) {

                loadShowBill(idBill);
            }

        }




        return binding.getRoot();
    }
    @SuppressLint("WrongConstant")
    private void openDraw() {
        binding.toggls.setOnClickListener((View.OnClickListener) v -> {
            DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.draw);
            drawer.openDrawer(Gravity.START);

        });
    }
    private void loadShowBill(String idBill) {

        Call<APIResponse.ResponseShowBill> call = apiInterFace.showBill(idBill
                , "application/json",
                "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                shardEditor.loadData().get(ShardEditor.KEY_LANG));
        call.enqueue(new Callback<APIResponse.ResponseShowBill>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseShowBill> call,
                                   @NonNull Response<APIResponse.ResponseShowBill> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus()) {
                        binding.tvNumberFatora.setText(response.body().getData().getId() + "");
                        binding.tvNameFatora.setText(response.body().getData().getName() + "");
                        binding.tvDevelaryFatora.setText(response.body().getData().getIDName() + "");
                        binding.tvTvNumFatoraFatora.setText(response.body().getData().getId() + "");
                        binding.tvFromAddress.setText(response.body().getData().getFromAddress() + "");
                        binding.tvToAddress.setText(response.body().getData().getToAddress() + "");
                        binding.tvTvPricFatora.setText(response.body().getData().getPrice() + "");

                        binding.homeContent.setVisibility(View.VISIBLE);
                        binding.layoutProgress.setVisibility(View.GONE);


                    }

                }


            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseShowBill> call, @NonNull Throwable t) {

            }
        });
    }
}