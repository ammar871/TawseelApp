package com.ammar.tawseel.ui.fragments;

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
import com.ammar.tawseel.databinding.FragmentChatBinding;
import com.ammar.tawseel.editor.ShardEditor;
import com.ammar.tawseel.netWorke.APIClient;
import com.ammar.tawseel.netWorke.APIInterFace;
import com.ammar.tawseel.pojo.response.APIResponse;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatFragment extends Fragment {


    public ChatFragment() {
        // Required empty public constructor
    }

    APIInterFace apiInterFace;
    ShardEditor shardEditor;
    FragmentChatBinding binding;
    String to, orderId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        shardEditor = new ShardEditor(getActivity());
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false);

        apiInterFace = APIClient.getClient().create(APIInterFace.class);

        Bundle bundle = this.getArguments();

        if (bundle != null) {

            to = getArguments().getString("to");
            orderId = getArguments().getString("orderId");
            Log.d("bundelsdata", "onCreateView: " + to + "\n " + orderId);
            getChatsBetween(to, orderId);

        }

        binding.toggles.setOnClickListener((View.OnClickListener) v -> {

            DrawerLayout drawer = (DrawerLayout)getActivity().findViewById(R.id.draw);
            drawer.openDrawer(Gravity.LEFT);

        });
        return binding.getRoot();
    }

    private void getChatsBetween(String to, String orderId) {

        Call<APIResponse.ResponseChatBetween> call = apiInterFace
                .chatBetweenUser(to, orderId, "1", "application/json",
                        "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                        "ar"
                );

        call.enqueue(new Callback<APIResponse.ResponseChatBetween>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseChatBetween> call, @NonNull Response<APIResponse.ResponseChatBetween> response) {
                if (response.code() == 200) {

                    assert response.body() != null;
                    if (response.body().getStatus()) {
                     //   binding.tvNameLable.setText(response.body().getData().getDriver().);
                        Picasso.with(getActivity()).
                                load(response.body().getData().getDriver().getAvatar())
                                .into(binding.imageView1);
                        Log.d("messageRespons", "onResponse: " + response.body().getData().getMessages().get(0).getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseChatBetween> call, @NonNull Throwable t) {

            }
        });

    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.layout_view, fragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }
}