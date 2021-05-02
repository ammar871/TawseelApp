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

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ammar.tawseel.R;
import com.ammar.tawseel.adapters.AdapterMessages;

import com.ammar.tawseel.adapters.RecyclerItemTouchHelper;
import com.ammar.tawseel.databinding.FragmentMessagesBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Objects;

public class MessagesFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, AdapterMessages.OnclickMessage {


    public MessagesFragment() {

    }

    ArrayList<String> list = new ArrayList<>();
    AdapterMessages adapterMessages;
    FragmentMessagesBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_messages, container, false);
        list.add("ssss");
        list.add("ssss");
        list.add("ssss");
        list.add("ssss");
        list.add("ssss");
        list.add("ssss");
        list.add("ssss");
        list.add("ssss");
        list.add("ssss");
        list.add("ssss");
        list.add("ssss");

        adapterMessages = new AdapterMessages(list, getActivity(), this);
        binding.rvMessage.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        binding.rvMessage.setHasFixedSize(true);
        binding.rvMessage.setAdapter(adapterMessages);
        binding.rvMessage.setItemAnimator(new DefaultItemAnimator());
        binding.rvMessage.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        openDraw();


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rvMessage);

        return binding.getRoot();
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