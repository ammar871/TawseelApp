package com.ammar.tawseel.ui.fragments;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import android.widget.EditText;
import android.widget.FrameLayout;

import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ammar.tawseel.R;
import com.ammar.tawseel.databinding.FragmentTawseelMapsBinding;
import com.ammar.tawseel.editor.ShardEditor;
import com.ammar.tawseel.example.audio_record_view.ChatTest.ChatAppActivity;
import com.ammar.tawseel.netWorke.APIClient;
import com.ammar.tawseel.netWorke.APIInterFace;
import com.ammar.tawseel.pojo.data.Driver;
import com.ammar.tawseel.pojo.response.APIResponse;

import com.ammar.tawseel.uitllis.Cemmon;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LOCATION_SERVICE;

public class TawseelMapsFragment extends Fragment implements GoogleMap.OnMarkerClickListener {
    FusedLocationProviderClient mFusedLocationClient;

    int PERMISSION_ID = 44;
    private static final int REQUEST_LOCATION = 11;
    APIInterFace apiInterFace;
    FragmentTawseelMapsBinding binding;
    ShardEditor shardEditor;
    ArrayList<Driver> drivers = new ArrayList<>();
    LocationManager locationManager;
    double latitude, longitude;
    GoogleMap googleMapBusey, googleMapOff, googleMapOn, map;
    private final static int LOCATION_PER_REQUEST = 9999;
    ArrayList<Driver> listDrivers = new ArrayList<>();
    AlertDialog alertDialogfilltere;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        shardEditor = new ShardEditor(getActivity());

        if (shardEditor.loadData().get(ShardEditor.KEY_LANG) != "") {

            Cemmon.setLocale(getActivity(), shardEditor.loadData().get(ShardEditor.KEY_LANG));
            Locale locale = new Locale(shardEditor.loadData().get(ShardEditor.KEY_LANG));
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getActivity().getResources().updateConfiguration(config,
                    getActivity().getResources().getDisplayMetrics());

        }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tawseel_maps, container, false);

        apiInterFace = APIClient.getClient().create(APIInterFace.class);


        if (Cemmon.isNetworkOnline(getActivity())) {
            if (binding.layoutLocationInternet.getVisibility() == View.VISIBLE) {
                binding.layoutLocationInternet.setVisibility(View.GONE);
            }
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
            binding.tvSearsh.setSelected(true);
            getLastLocation();
            checkGPS();
            openDraw();
            getDrivers();
            // getLocation();
            // method to get the location
//        binding.searshImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                binding.progressMapsearsh.setVisibility(View.VISIBLE);
//                binding.layoyutMaping.setVisibility(View.GONE);
//                searshCallApi(binding.edSearsh.getText().toString());
//            }
//        });
            binding.imgFiltter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAlrtDilogFillter();
                }
            });
        } else {
            binding.layoutLocationInternet.setVisibility(View.VISIBLE);
        }


        return binding.getRoot();
    }

    SupportMapFragment mapFragment;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (Cemmon.isNetworkOnline(getActivity())) {

            if (binding.layoutLocationInternet.getVisibility() == View.VISIBLE) {
                binding.layoutLocationInternet.setVisibility(View.GONE);
            }
            mapFragment =
                    (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            if (mapFragment != null) {

                mapFragment.getMapAsync(callback);
            }
        } else {
            binding.layoutLocationInternet.setVisibility(View.VISIBLE);
        }
        binding.btnDissmes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_MAIN, null);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
                    intent.setComponent(cn);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (ActivityNotFoundException ignored) {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }
            }
        });

    }


    private void getAlrtDilogFillter() {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fillter_layout, null);
        RadioButton box_inside = view.findViewById(R.id.box_inside);
        RadioButton box_outside = view.findViewById(R.id.box_outSide);

        RatingBar appCompatRatingBar = view.findViewById(R.id.tv_datefillter);


        alertDialogfilltere = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
        alertDialogfilltere.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialogfilltere.show();
        alertDialogfilltere.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                fillterDtivers(box_inside, box_outside, appCompatRatingBar, "");
                // Your code ...
            }
        });

    }

    private void fillterDtivers(RadioButton box_inside, RadioButton box_outside, RatingBar appCompatRatingBar, String editText) {
        int star = Math.round(appCompatRatingBar.getRating());
        if (box_inside.isChecked()) {
            setApplicationDrivers(latitude, longitude, star, editText, true);

        } else if (box_outside.isChecked()) {

            setApplicationDrivers(latitude, longitude, star, "", false);

        }
    }

    public int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 100);
        long totalSeconds = (int) (totalDuration / 100);

        // calculating percentage
        percentage = (((double) currentSeconds) / totalSeconds) * 100;

        // return percentage
        return percentage.intValue();
    }

    private void setApplicationDrivers(double latitude,
                                       double longitude,
                                       int star,
                                       String kiloometrs,
                                       boolean b) {
        binding.progressMapsearsh.setVisibility(View.VISIBLE);
        binding.layoyutMaping.setVisibility(View.GONE);
        Call<APIResponse.ResponseFillter> call = apiInterFace.filtersDrivires( star
                , kiloometrs, b, "application/json",
                "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                shardEditor.loadData().get(ShardEditor.KEY_LANG));
        call.enqueue(new Callback<APIResponse.ResponseFillter>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseFillter> call, @NonNull Response<APIResponse.ResponseFillter> response) {
                if (response.code() == 200) {
                    if (map != null) {
                        map.clear();
                    }
                    binding.progressMapsearsh.setVisibility(View.GONE);
                    binding.layoyutMaping.setVisibility(View.VISIBLE);
                    listDrivers.clear();

                    assert response.body() != null;
                    if (response.body().getStatus()) {

                        binding.progressMapsearsh.setVisibility(View.GONE);
                        binding.layoyutMaping.setVisibility(View.VISIBLE);
                        if (response.body().getData().size() > 0) {


                            for (int i = 0; i < listDrivers.size(); i++) {
                                id = response.body().getData().get(i).getId().toString();

                                if (listDrivers.get(i).getGpsLat() != null &&
                                        listDrivers.get(i).getGpsLng() != null) {


                                    double lat = Double.parseDouble(listDrivers.get(i).getGpsLat() + "");
                                    Log.d("latlang", "onResponse: " + lat);
                                    double lang = Double.parseDouble(listDrivers.get(i).getGpsLng().toString().trim());
                                    LatLng latLng = new LatLng(lat, lang);
                                    MarkerOptions markerOptions;

                                    if (map != null && getActivity() != null) {
                                        if (listDrivers.get(i).getStatus().equals("off")) {
                                            int posstion = i;
                                            googleMapOff = map;


                                            Marker marker = googleMapOff.addMarker(new MarkerOptions().position(latLng).title("off").icon(
                                                    bitmapDescriptorFromVector(R.drawable.ic_off)));
                                            marker.setTag(listDrivers.get(i).getId() + "");

                                        } else if (listDrivers.get(i).getStatus().equals("on")) {
                                            int posstion = i;
                                            googleMapOn = map;

                                            Marker marker = googleMapOn.addMarker(new MarkerOptions().position(latLng).title("on").icon(
                                                    bitmapDescriptorFromVector(R.drawable.ic_icon_on)));
                                            marker.setTag(listDrivers.get(i).getId() + "");

                                        } else if (listDrivers.get(i).getStatus().equals("busey")) {
                                            int posstion = i;
                                            googleMapBusey = map;
                                            Marker marker = googleMapBusey.addMarker(new MarkerOptions().position(latLng).title("busey").icon(
                                                    bitmapDescriptorFromVector(R.drawable.ic_icon_bussy)));
                                            marker.setTag(listDrivers.get(i).getId() + "");


                                        }

                                    }


                                }
                                int index = i;
                                map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                    @Override
                                    public boolean onMarkerClick(Marker marker) {

                                        Log.d("aggdgsgsg", "onMarkerClick: " + marker.getTag());
                                        if (marker.getTitle().equals("off"))
                                            openDialog(getString(R.string.off));
                                        else if (marker.getTitle().equals("busey"))
                                            openDialog(getString(R.string.busey));
                                        else if (marker.getTitle().equals("on"))
//                                                    if (shardEditor.loadData().get(ShardEditor.ORDER_ID).equals("")) {
                                            showDialogSelecteService(marker.getTag() + "");
                                        //    callSendMessage(id);

//                                                    } else {
//                                                        showDialogSelecteService(id);
//                                                        //  callSendTwoMessage(id);
//                                                    }


                                        return false;
                                    }
                                });
                            }


                        }
                        Log.d("loca", "onResponse: " + latitude + longitude);
                        if (map != null && getActivity() != null) {

                            map.animateCamera(CameraUpdateFactory.zoomTo(17), 500, null);
                            LatLng latLngMe = new LatLng(latitude, longitude);
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngMe, 14f));
                            map.addMarker(new MarkerOptions().position(latLngMe).title("My Location").icon(
                                    BitmapDescriptorFactory.fromBitmap(resizeBitmap(110, 110))));
                        }


                    } else {
                        binding.progressMapsearsh.setVisibility(View.GONE);
                        binding.layoyutMaping.setVisibility(View.VISIBLE);

                    }


                }
            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseFillter> call, @NonNull Throwable t) {

            }
        });
        map.animateCamera(CameraUpdateFactory.zoomTo(17), 200, null);
        LatLng latLngMe = new LatLng(latitude, longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngMe, 14f));
        map.addMarker(new MarkerOptions().position(latLngMe).title("My Location").icon(
                BitmapDescriptorFactory.fromBitmap(resizeBitmap(110, 110))));

    }

  /*  private void searshCallApi(String searchName) {
        if (searchName != null && !searchName.isEmpty()) {
            if (map != null) {
                map.clear();
            }
            Call<APIResponse.ResponseSearchDrivers> call = apiInterFace.searshDrivires(searchName,
                    "application/json",
                    "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                    shardEditor.loadData().get(ShardEditor.KEY_LANG)
            );

            call.enqueue(new Callback<APIResponse.ResponseSearchDrivers>() {
                @Override
                public void onResponse(@NonNull Call<APIResponse.ResponseSearchDrivers> call,
                                       @NonNull Response<APIResponse.ResponseSearchDrivers> response) {


                    if (response.code() == 200) {
                        listDrivers.clear();

                        assert response.body() != null;
                        if (response.body().getStatus()) {

                            binding.progressMapsearsh.setVisibility(View.GONE);
                            binding.layoyutMaping.setVisibility(View.VISIBLE);
                            if (response.body().getData().size() > 0) {


                             *//*   if (map != null && getActivity() != null) {
                                    if (listDrivers.get(i).getStatus().equals("off")) {
                                        int posstion = i;
                                        googleMapOff = map;


                                        Marker marker = googleMapOff.addMarker(new MarkerOptions().position(latLng).title("off").icon(
                                                bitmapDescriptorFromVector(R.drawable.ic_off)));
                                        marker.setTag(listDrivers.get(i).getId() + "");

                                    } else if (listDrivers.get(i).getStatus().equals("on")) {
                                        int posstion = i;
                                        googleMapOn = map;

                                        Marker marker = googleMapOn.addMarker(new MarkerOptions().position(latLng).title("on").icon(
                                                bitmapDescriptorFromVector(R.drawable.ic_icon_on)));
                                        marker.setTag(listDrivers.get(i).getId() + "");

                                    } else if (listDrivers.get(i).getStatus().equals("busey")) {
                                        int posstion = i;
                                        googleMapBusey = map;
                                        Marker marker = googleMapBusey.addMarker(new MarkerOptions().position(latLng).title("busey").icon(
                                                bitmapDescriptorFromVector(R.drawable.ic_icon_bussy)));
                                        marker.setTag(listDrivers.get(i).getId() + "");

//                                                googleMapBusey.addMarker(new MarkerOptions().position(latLng).title("busey").snippet(
//                                                        listDrivers.get(i).getId()+""
//                                                ).icon(
//                                                        bitmapDescriptorFromVector(R.drawable.ic_map_bussy)));


                                    }

                                }*//*


                                for (int i = 0; i < response.body().getData().size(); i++) {
                                    id = response.body().getData().get(i).getId().toString();

                                    if (response.body().getData().get(i).getGpsLat() != null &&
                                            response.body().getData().get(i).getGpsLng() != null) {


                                        double lat = Double.parseDouble(response.body().getData().get(i).getGpsLat() + "");
                                        Log.d("latlang", "onResponse: " + lat);
                                        double lang = Double.parseDouble(response.body().getData().get(i).getGpsLng().toString().trim());
                                        LatLng latLng = new LatLng(lat, lang);
                                        MarkerOptions markerOptions;
                                        if (response.body().getData().get(i).getStatus().equals("off")) {
                                            googleMapOff = map;


                                            googleMapOff.addMarker(new MarkerOptions().position(latLng).title("off").icon(
                                                    bitmapDescriptorFromVector(R.drawable.ic_map_off)
                                            ));


                                        } else if (response.body().getData().get(i).getStatus().equals("on")) {
                                            googleMapOn = map;
                                            googleMapOn.addMarker(new MarkerOptions().position(latLng).title("on").icon(
                                                    bitmapDescriptorFromVector(R.drawable.ic_icon_on)));

                                        } else if (response.body().getData().get(i).getStatus().equals("busey")) {

                                            googleMapBusey = map;
                                            googleMapBusey.addMarker(new MarkerOptions().position(latLng).title("busey").icon(
                                                    bitmapDescriptorFromVector(R.drawable.ic_map_bussy)));


                                        }

                                        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                            @Override
                                            public boolean onMarkerClick(Marker marker) {

                                                if (marker.getTitle().equals("off"))
                                                    openDialog("مندوب التوصيل غير متوفر الآن");
                                                else if (marker.getTitle().equals("busey"))
                                                    openDialog("مندوب التوصيل لديه طلب حالي، لا يمكنك التواصل معه الآن");
                                                else if (marker.getTitle().equals("on"))
//                                                    if (shardEditor.loadData().get(ShardEditor.ORDER_ID).equals("")) {
                                                    showDialogSelecteService(id);
                                                //    callSendMessage(id);

//                                                    } else {
//                                                        showDialogSelecteService(id);
//                                                        //  callSendTwoMessage(id);
//                                                    }


                                                return false;
                                            }
                                        });


                                    }


                                }
                                map.animateCamera(CameraUpdateFactory.zoomTo(17), 200, null);
                                LatLng latLngMe = new LatLng(latitude, longitude);
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngMe, 14f));
                                map.addMarker(new MarkerOptions().position(latLngMe).title("My Location").icon(
                                        BitmapDescriptorFactory.fromBitmap(resizeBitmap(110, 110))));


                            }


                        } else {
                            binding.progressMapsearsh.setVisibility(View.GONE);
                            binding.layoyutMaping.setVisibility(View.VISIBLE);

                        }

                    }

                }


                @Override
                public void onFailure(@NonNull Call<APIResponse.ResponseSearchDrivers> call, @NonNull Throwable t) {
                    binding.progressMapsearsh.setVisibility(View.GONE);
                    binding.layoyutMaping.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            Toast.makeText(getActivity(), "اكتب كلمة البحث", Toast.LENGTH_SHORT).show();
        }

    }*/

    @SuppressLint("WrongConstant")
    private void openDraw() {
        binding.toggls.setOnClickListener((View.OnClickListener) v -> {
            DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.draw);
            drawer.openDrawer(Gravity.START);

        });
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
//
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(getActivity(),
//                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//
//            ActivityCompat.requestPermissions(getActivity(),
//                    new String[]{
//                            Manifest.permission.ACCESS_COARSE_LOCATION,
//                            Manifest.permission.ACCESS_FINE_LOCATION
//
//                    }, LOCATION_PER_REQUEST);
//        } else {

        // check if location is enabled
        if (isLocationEnabled()) {

            mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location == null) {
                        requestNewLocationData();
                    } else {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Cemmon.latude = latitude;
                        Cemmon.langtude = longitude;
                        Log.d("jdeeeeeee", "onComplete: " + latitude + longitude);
                    }
                }
            });
        }

    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
//    @Override
//    public void
//    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == PERMISSION_ID) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getLastLocation();
//            }
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();
//        if (checkPermissions()) {
//            getLastLocation();
//        }
    }


    private void checkGPS() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            binding.layoutLocation.setVisibility(View.VISIBLE);
            binding.contentMap.setVisibility(View.GONE);


        } else {
            binding.layoutLocation.setVisibility(View.GONE);
            binding.contentMap.setVisibility(View.VISIBLE);

        }
        binding.tvTurrn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, 0);
                binding.layoutLocation.setVisibility(View.GONE);
                binding.contentMap.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            switch (requestCode) {
                case 0:
                    TawseelMapsFragment newFrag = new TawseelMapsFragment();
                    // passing value from activity


                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.layout_view,
                                    newFrag)
                            .commitNow();
                    break;
            }
        }
    }

    private void showDialogIntrnet() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_check_intrnet, null);
//        radioGroup = view.findViewById(R.id.radioGroup);
//
//
//
//        btn_next.setOnClickListener(v -> {
//
//            alertDialog.dismiss();
//
//        });

        alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();

        alertDialog.show();

    }

    @Override
    public void onStart() {
        super.onStart();
           }

    String id;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {


        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            Double lat = new Double(latitude);
            Double lng = new Double(longitude);
            if (lat != null && lng != null) {
                Log.d("latLang", "onMapReady: " + lat + lng);

            }

            showDriversOnTheMap(googleMap);


        }
    };

    private void showDriversOnTheMap(GoogleMap googleMap) {
        Call<APIResponse.ResponseDrivers> call = apiInterFace.getDrivers(
                "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                shardEditor.loadData().get(ShardEditor.KEY_LANG),
                "application/json");

        call.enqueue(new Callback<APIResponse.ResponseDrivers>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseDrivers> call,
                                   @NonNull Response<APIResponse.ResponseDrivers> response) {


                if (response.code() == 200) {
                    if (map != null) {
                        map.clear();
                    }
                    assert response.body() != null;
                    if (response.body().getStauts()) {

                        binding.layoutHome.setVisibility(View.VISIBLE);
                        binding.progressMap.setVisibility(View.GONE);
                        if (response.body().getData().size() > 0) {
                            listDrivers = (ArrayList<Driver>) response.body().getData();

                            for (int i = 0; i < listDrivers.size(); i++) {
                                id = response.body().getData().get(i).getId().toString();

                                if (listDrivers.get(i).getGpsLat() != null &&
                                        listDrivers.get(i).getGpsLng() != null) {


                                    double lat = Double.parseDouble(listDrivers.get(i).getGpsLat() + "");
                                    Log.d("latlang", "onResponse: " + lat);
                                    double lang = Double.parseDouble(listDrivers.get(i).getGpsLng().toString().trim());
                                    LatLng latLng = new LatLng(lat, lang);
                                    MarkerOptions markerOptions;

                                    if (map != null && getActivity() != null) {
                                        if (listDrivers.get(i).getStatus().equals("off")) {
                                            int posstion = i;
                                            googleMapOff = map;


                                            Marker marker = googleMapOff.addMarker(new MarkerOptions().position(latLng).title("off").icon(
                                                    bitmapDescriptorFromVector(R.drawable.ic_off)));
                                            marker.setTag(listDrivers.get(i).getId() + "");

                                        } else if (listDrivers.get(i).getStatus().equals("on")) {
                                            int posstion = i;
                                            googleMapOn = map;

                                            Marker marker = googleMapOn.addMarker(new MarkerOptions().position(latLng).title("on").icon(
                                                    bitmapDescriptorFromVector(R.drawable.ic_icon_on)));
                                            marker.setTag(listDrivers.get(i).getId() + "");

                                        } else if (listDrivers.get(i).getStatus().equals("busey")) {
                                            int posstion = i;
                                            googleMapBusey = map;
                                            Marker marker = googleMapBusey.addMarker(new MarkerOptions().position(latLng).title("busey").icon(
                                                    bitmapDescriptorFromVector(R.drawable.ic_icon_bussy)));
                                            marker.setTag(listDrivers.get(i).getId() + "");

//                                                googleMapBusey.addMarker(new MarkerOptions().position(latLng).title("busey").snippet(
//                                                        listDrivers.get(i).getId()+""
//                                                ).icon(
//                                                        bitmapDescriptorFromVector(R.drawable.ic_map_bussy)));


                                        }

                                    }


                                }
                                int index = i;
                                map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                    @Override
                                    public boolean onMarkerClick(Marker marker) {

                                        Log.d("aggdgsgsg", "onMarkerClick: " + marker.getTag());
                                        if (marker.getTitle().equals("off"))
                                            openDialog(getString(R.string.off));
                                        else if (marker.getTitle().equals("busey"))
                                            openDialog(getString(R.string.busey));
                                        else if (marker.getTitle().equals("on"))
//                                                    if (shardEditor.loadData().get(ShardEditor.ORDER_ID).equals("")) {
                                            showDialogSelecteService(marker.getTag() + "");
                                        //    callSendMessage(id);

//                                                    } else {
//                                                        showDialogSelecteService(id);
//                                                        //  callSendTwoMessage(id);
//                                                    }


                                        return false;
                                    }
                                });
                            }


                        }
                        Log.d("loca", "onResponse: " + latitude + longitude);
                        if (getActivity() != null) {
                            googleMap.animateCamera(CameraUpdateFactory.zoomTo(17), 500, null);
                            LatLng latLngMe = new LatLng(latitude, longitude);
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngMe, 14f));
                            googleMap.addMarker(new MarkerOptions().position(latLngMe).title(getString(R.string.my_location)).icon(
                                    BitmapDescriptorFactory.fromBitmap(resizeBitmap(110, 110))));
                        }


                    } else {
                        binding.layoutHome.setVisibility(View.VISIBLE);
                        binding.progressMap.setVisibility(View.GONE);

                    }

                }
                else if (response.code() == 401) {
                    shardEditor.logOut();

                }

            }


            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseDrivers> call, @NonNull Throwable t) {
                binding.layoutHome.setVisibility(View.VISIBLE);
                binding.progressMap.setVisibility(View.GONE);
                //Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static Bitmap createCustomMarker(Context context, @DrawableRes int resource, String uri) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.image_map, null);

        CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);

        Glide.with(context).load("https://i.stack.imgur.com/7pUVC.jpg")
                .into(markerImage);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);

        return bitmap;
    }

    RadioGroup radioGroup;
    RadioButton radioButton;
    FrameLayout btn_next;
    TextView text;
    ProgressBar progressBar;
    AlertDialog alertDialog = null;

    private void showDialogSelecteService(String id) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.layout_send_messgae, null);
        radioGroup = view.findViewById(R.id.radioGroup);
        text = view.findViewById(R.id.tv_text_dialog);
        progressBar = view.findViewById(R.id.progrss_dilog);
        btn_next = view.findViewById(R.id.tv_next);


        btn_next.setOnClickListener(v -> {
            text.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            int radioId = radioGroup.getCheckedRadioButtonId();
            radioButton = view.findViewById(radioId);

//            Intent intent=new Intent(getActivity(), ChatAppActivity.class);
//            intent.putExtra("to", id);
//            startActivity(intent);

            Bundle bundle = new Bundle();
            bundle.putString("to", id);
            ChatFragment fragobj = new ChatFragment();
            fragobj.setArguments(bundle);

            Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.layout_view, fragobj)
                    .addToBackStack(null)
                    .commit();
            alertDialog.dismiss();

        });

        alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();


    }


    void getDrivers() {


        Call<APIResponse.ResponseDrivers> call = apiInterFace.getDrivers(
                "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                "ar",
                "application/json");

        call.enqueue(new Callback<APIResponse.ResponseDrivers>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseDrivers> call,
                                   @NonNull Response<APIResponse.ResponseDrivers> response) {


                if (response.code() == 200) {

                    assert response.body() != null;
                    if (response.body().getStauts()) {
                        drivers = (ArrayList<Driver>) response.body().getData();


                    }

                }

            }


            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseDrivers> call, @NonNull Throwable t) {

                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private BitmapDescriptor bitmapDescriptorFromVector(int vectorResId) {

        @SuppressLint("UseCompatLoadingForDrawables") Drawable vectorDrawable = getActivity().getResources().getDrawable(vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = lat;
                longitude = longi;
                Log.d("location", "Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
            } else {
                Toast.makeText(getActivity(), "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public Bitmap resizeBitmap(int width, int height) {

        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        return smallMarker;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }


    void openDialog(String title) {


        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.layout_dialog, null);

        TextView tv_title = view.findViewById(R.id.tv_label_dialog);


        tv_title.setText(title);
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();

        alertDialog.show();

        new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub

                alertDialog.dismiss();
            }
        }.start();

    }


}