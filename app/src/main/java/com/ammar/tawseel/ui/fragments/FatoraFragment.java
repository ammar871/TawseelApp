package com.ammar.tawseel.ui.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
    String orderId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        shardEditor = new ShardEditor(getActivity());

        if (shardEditor.loadData().get(ShardEditor.KEY_LANG) != "") {

            Cemmon.setLocale(getActivity(), shardEditor.loadData().get(ShardEditor.KEY_LANG));

        }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fatora, container, false);
        apiInterFace = APIClient.getClient().create(APIInterFace.class);


        binding.layoutSucess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.prodSucces.setVisibility(View.VISIBLE);
                binding.tvFinshOprator.setVisibility(View.GONE);
                callPaidOrder();
            }
        });
        binding.layoutDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.prodone.setVisibility(View.VISIBLE);
                binding.tvCloseOprator.setVisibility(View.GONE);

                callCancelOrder();
            }
        });
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert getFragmentManager() != null;
                getFragmentManager().popBackStackImmediate();
            }
        });
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

    private void callPaidOrder() {
        Call<APIResponse.PaidOrderResponse> call=apiInterFace.finshOpreatoor(orderId,"cash"
                , "application/json",
                "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                shardEditor.loadData().get(ShardEditor.KEY_LANG));

        call.enqueue(new Callback<APIResponse.PaidOrderResponse>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.PaidOrderResponse> call,
                                   @NonNull Response<APIResponse.PaidOrderResponse> response) {
                if (response.code()==200){
                    if (response.body().getStatus()){

                        Toast.makeText(getActivity(), R.string.succesOprator, Toast.LENGTH_SHORT).show();
                        binding.prodSucces.setVisibility(View.GONE);
                        binding.tvFinshOprator.setVisibility(View.VISIBLE);
                        assert getFragmentManager() != null;
                        getFragmentManager().popBackStackImmediate();

                    }else {
                        Toast.makeText(getActivity(), ""+response.body().getMessage().get(0), Toast.LENGTH_SHORT).show();
                        binding.prodSucces.setVisibility(View.GONE);
                        binding.tvFinshOprator.setVisibility(View.VISIBLE);
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
            public void onFailure(Call<APIResponse.PaidOrderResponse> call, Throwable t) {
                binding.prodSucces.setVisibility(View.GONE);
                binding.tvFinshOprator.setVisibility(View.VISIBLE);
            }
        });

    }

    private void callCancelOrder() {

        Call<APIResponse.PaidOrderResponse> call=apiInterFace.cancelOpreatoor(orderId
                , "application/json",
                "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                shardEditor.loadData().get(ShardEditor.KEY_LANG));

        call.enqueue(new Callback<APIResponse.PaidOrderResponse>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.PaidOrderResponse> call,
                                   @NonNull Response<APIResponse.PaidOrderResponse> response) {
                if (response.code()==200){
                    if (response.body().getStatus()){

                        Toast.makeText(getActivity(), R.string.succesOprator, Toast.LENGTH_SHORT).show();
                        binding.prodone.setVisibility(View.GONE);
                        binding.tvCloseOprator.setVisibility(View.VISIBLE);
                        assert getFragmentManager() != null;
                        getFragmentManager().popBackStackImmediate();
                    }else {
                        Toast.makeText(getActivity(), ""+response.body().getMessage().get(0), Toast.LENGTH_SHORT).show();
                        binding.prodone.setVisibility(View.GONE);
                        binding.tvCloseOprator.setVisibility(View.VISIBLE);
                    }



                }
                binding.prodone.setVisibility(View.GONE);
                binding.tvCloseOprator.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<APIResponse.PaidOrderResponse> call, Throwable t) {
                binding.prodone.setVisibility(View.GONE);
                binding.tvCloseOprator.setVisibility(View.VISIBLE);
            }
        });
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

                        orderId=response.body().getData().getOrderId()+"";
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