package com.ammar.tawseel.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ammar.tawseel.R;
import com.ammar.tawseel.adapters.AdapterMessages;

import com.ammar.tawseel.adapters.AdapterNotification;
import com.ammar.tawseel.adapters.RecyclerItemTouchHelper;
import com.ammar.tawseel.databinding.FragmentMessagesBinding;
import com.ammar.tawseel.editor.ShardEditor;
import com.ammar.tawseel.netWorke.APIClient;
import com.ammar.tawseel.netWorke.APIInterFace;
import com.ammar.tawseel.pojo.data.DataMessags;
import com.ammar.tawseel.pojo.data.DataNotification;
import com.ammar.tawseel.pojo.response.APIResponse;
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        shardEditor = new ShardEditor(getActivity());
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_messages, container, false);
        apiInterFace = APIClient.getClient().create(APIInterFace.class);
loadDataMessages("1");


        openDraw();


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rvMessage);

        return binding.getRoot();
    }

    private void loadDataMessages(String page) {
    binding.layoutProgress.setVisibility(View.VISIBLE);
    binding.rvMessage.setVisibility(View.GONE);

            Call<APIResponse.ResponseMessages> call=apiInterFace.getMessages( page,"application/json", "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                    "ar"
            );
            call.enqueue(new Callback<APIResponse.ResponseMessages>() {
                @Override
                public void onResponse(@NonNull Call<APIResponse.ResponseMessages> call,
                                       @NonNull Response<APIResponse.ResponseMessages> response) {

                    if (response.code()==200){
                        assert response.body() != null;
                        if (response.body().getStatus()){
                            Log.d("responses", "onResponse: "+response.body().getData().size());
                            setupAdapter(response.body().getData());
                            binding.layoutProgress.setVisibility(View.GONE);
                            binding.rvMessage.setVisibility(View.VISIBLE);
                        }else {
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
        binding.rvMessage.setItemAnimator(new DefaultItemAnimator());
        binding.rvMessage.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }
    private void openDraw() {
        binding.toggles.setOnClickListener((View.OnClickListener) v -> {

            DrawerLayout drawer = (DrawerLayout)getActivity().findViewById(R.id.draw);
            drawer.openDrawer(Gravity.LEFT);

        });
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof AdapterMessages.ViewHolderVidio) {

            Toast.makeText(getActivity(), "deleted", Toast.LENGTH_SHORT).show();

            adapterMessages.removeItem(viewHolder.getAdapterPosition());

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

    @Override
    public void itemOnclick() {
        loadFragment(new ChatFragment());
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
}