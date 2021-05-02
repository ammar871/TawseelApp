package com.ammar.tawseel.ui;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.ammar.tawseel.R;
import com.ammar.tawseel.databinding.ActivityTrakingMapsBinding;
import com.ammar.tawseel.editor.ShardEditor;
import com.ammar.tawseel.pojo.data.DataFinshOrder;
import com.ammar.tawseel.pojo.data.DataOrder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TrakingMapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private DataOrder orderData;
    private DataFinshOrder finshOrder;
    private GoogleMap mMap;
    ShardEditor sharedEditor;
    ActivityTrakingMapsBinding binding;
    private ArrayList<LatLng> locationArrayList;
    private ArrayList<LatLng> locationArrayListfinshed;
    LatLng trking;
    ArrayList<String> namesLocations = new ArrayList<>();
    ArrayList<String> namesLocationsFinsh = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedEditor = new ShardEditor(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_traking_maps);
        locationArrayList = new ArrayList<>();
        locationArrayListfinshed = new ArrayList<>();
        if (getIntent() != null) {
            orderData = getIntent().getParcelableExtra("orderDetails");
            finshOrder = getIntent().getParcelableExtra("orderDetailsFinshed");
            if (orderData != null) {

                LatLng starting = new LatLng(Double.parseDouble(orderData.getFromLat() + ""), Double.parseDouble(orderData.getFromLng() + ""));
                trking = new LatLng(Double.parseDouble(orderData.getCurrentLat() + ""), Double.parseDouble(orderData.getCurrentLng() + ""));
                LatLng endting = new LatLng(Double.parseDouble(orderData.getToLat() + ""), Double.parseDouble(orderData.getToLng() + ""));
                locationArrayList.add(starting);

                locationArrayList.add(endting);
                namesLocations.add("from" + orderData.getFromAddress());
                namesLocations.add("to " + orderData.getToAddress());

                SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat outputtime = new SimpleDateFormat("hh:mm a");

                Date d = null;
                Date dtime = null;
                try {
                    d = input.parse(orderData.getCreatedAt());
                    dtime = input.parse(orderData.getCreatedAt());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formatted = output.format(d);
                String formattedtime = outputtime.format(dtime);
                Log.i("DATE", "" + formatted + "\n" + formattedtime);
                Log.d("dating", "onCreate: " + orderData.getCreatedAt());

                binding.tvDate.setText(formatted);
                binding.tvTime.setText(formattedtime);
                binding.tvName.setText(orderData.getIDName());
                binding.tvFrom.setText(orderData.getFromAddress() + "");
                binding.tvTo.setText(orderData.getToAddress() + "");
                binding.numberFatora.setText(orderData.getBillId());
                binding.priceFatora.setText(orderData.getPrice());
            }
            if (finshOrder != null) {
                LatLng starting = new LatLng(Double.parseDouble(finshOrder.getFromLat() + ""), Double.parseDouble(finshOrder.getFromLng() + ""));
                LatLng endting = new LatLng(Double.parseDouble(finshOrder.getToLat() + ""), Double.parseDouble(finshOrder.getToLng() + ""));
                locationArrayListfinshed.add(starting);

                locationArrayListfinshed.add(endting);
                namesLocationsFinsh.add("from" + finshOrder.getFromAddress());
                namesLocationsFinsh.add("to " + finshOrder.getToAddress());
                SimpleDateFormat format = new SimpleDateFormat(
                        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
                format.setTimeZone(TimeZone.getTimeZone(finshOrder.getCreatedAt()));
                // binding.tvDate.setText(format);
                binding.tvName.setText(finshOrder.getIDName());
                binding.tvFrom.setText(finshOrder.getFromAddress() + "");
                binding.tvTo.setText(finshOrder.getToAddress() + "");
                binding.numberFatora.setText(finshOrder.getBillId());
                binding.priceFatora.setText(finshOrder.getPrice());


            }


            //  Log.d("order", "onCreate: " + orderData.getFromLat());
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maptrakink);
        mapFragment.getMapAsync(this);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (orderData != null) {
            for (int i = 0; i < locationArrayList.size(); i++) {

                googleMap.animateCamera(CameraUpdateFactory.zoomTo(17), 200, null);

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationArrayList.get(i), 6f));
                googleMap.addMarker(new MarkerOptions().position(locationArrayList.get(i)).title(namesLocations.get(i)).icon(
                        bitmapDescriptorFromVector(this, R.drawable.ic_icontraking)));

            }

        }

        if (finshOrder != null) {
            for (int i = 0; i < locationArrayListfinshed.size(); i++) {

                googleMap.animateCamera(CameraUpdateFactory.zoomTo(17), 200, null);

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationArrayListfinshed.get(i), 6f));
                googleMap.addMarker(new MarkerOptions().position(locationArrayListfinshed.get(i)).title(namesLocationsFinsh.get(i)).icon(
                        bitmapDescriptorFromVector(this, R.drawable.ic_icontraking)));

            }

        }

        if (orderData != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(trking, 6f));
            googleMap.addMarker(new MarkerOptions().position(trking).title("My Location").icon(
                    BitmapDescriptorFactory.fromBitmap(resizeBitmap(110, 110))));

        }

    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public Bitmap resizeBitmap(int width, int height) {

        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        return smallMarker;
    }


}