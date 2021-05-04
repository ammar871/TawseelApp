package com.ammar.tawseel.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ammar.tawseel.R;
import com.ammar.tawseel.adapters.AdapterNotification;
import com.ammar.tawseel.adapters.AdapterNotiyLasted;
import com.ammar.tawseel.adapters.AdapterOrdersHome;
import com.ammar.tawseel.databinding.FragmentNotifecationBinding;
import com.ammar.tawseel.editor.ShardEditor;
import com.ammar.tawseel.netWorke.APIClient;
import com.ammar.tawseel.netWorke.APIInterFace;
import com.ammar.tawseel.pojo.data.DataNotification;
import com.ammar.tawseel.pojo.data.DataOrder;
import com.ammar.tawseel.pojo.response.APIResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotifecationFragment extends Fragment {


    public NotifecationFragment() {
        // Required empty public constructor
    }

    FragmentNotifecationBinding binding;
    APIInterFace apiInterFace;
    ShardEditor shardEditor;
    AdapterNotiyLasted adapterNotiyLasted;
    ArrayList<DataNotification> listNotiyToday=new ArrayList<>();
    ArrayList<DataNotification> listlast=new ArrayList<>();
    AdapterNotification adapterNotification;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        shardEditor = new ShardEditor(getActivity());
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notifecation, container, false);
        apiInterFace = APIClient.getClient().create(APIInterFace.class);

        binding.rvListNotify.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        binding.rvListNotify.setHasFixedSize(true);

        binding.rvListNotifyToday.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        binding.rvListNotifyToday.setHasFixedSize(true);
        openDraw();
        loadDataNotification("1");
        return binding.getRoot();
    }

    private void loadDataNotification(String page) {
        Call<APIResponse.ResponseNotification> call=apiInterFace.getNotification( page,"application/json", "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                "ar"
        );
        call.enqueue(new Callback<APIResponse.ResponseNotification>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseNotification> call,
                                   @NonNull Response<APIResponse.ResponseNotification> response) {

                if (response.code()==200){
                    assert response.body() != null;
                    if (response.body().getStatus()){


                            for (int i=0;i<response.body().getData().size();i++ ){
                            if (response.body().getData().get(i).getReaded().equalsIgnoreCase("0")){

                                listNotiyToday.add(response.body().getData().get(i));
                            }else {
                                listlast.add(response.body().getData().get(i));
                            }

                            }




                        Log.d("responses", "onResponse: "+listNotiyToday.size()+listlast.size());
                        adapterNotification = new AdapterNotification(listNotiyToday,getActivity());
                        binding.rvListNotifyToday.setAdapter(adapterNotification);



                        adapterNotiyLasted = new AdapterNotiyLasted(listlast,getActivity());
                        binding.rvListNotify.setAdapter(adapterNotiyLasted);
                        binding.layoutProgress.setVisibility(View.GONE);
                        binding.homeContant.setVisibility(View.VISIBLE);
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

    private void openDraw() {
        binding.toggles.setOnClickListener((View.OnClickListener) v -> {

            DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.draw);
            drawer.openDrawer(Gravity.LEFT);

        });
    }
}