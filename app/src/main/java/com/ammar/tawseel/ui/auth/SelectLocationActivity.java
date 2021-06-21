package com.ammar.tawseel.ui.auth;

import androidx.fragment.app.FragmentActivity;

import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Bundle;

import com.ammar.tawseel.BuildConfig;
import com.ammar.tawseel.R;
import com.ammar.tawseel.databinding.ActivitySelectLocationBinding;
import com.ammar.tawseel.editor.ShardEditor;
import com.ammar.tawseel.uitllis.Cemmon;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.skyfishjy.library.RippleBackground;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SelectLocationActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, LocationListener, Animation.AnimationListener {
    public static int map_to_addsignup = 0;
    SupportMapFragment mapFragment;
    CameraPosition cameraPosition;
    String longitude, latitude;
    double lat;
    double lan;
    String locality = null;
    String country = null;
    String state;
    String sub_admin;
    String city;
    String pincode;
    String locality_city;
    String sub_localoty;
    String country_code;
    FusedLocationProviderClient mFusedLocationProviderClient;
    RippleBackground content;
    // Animation
    Animation animFadein;
    private GoogleMap mMap;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    private TextView resutText;
    private LatLng latLng = null;
    private boolean mRequestingLocationUpdates;
    private Button ic_save_proceed;
    private Context context;
    private TextView id_tv_change;
    private ImageView img_back, img_pin;
    //private RadarView radar_view;
    private ProgressBar pro_bar;
ShardEditor shardEditor;
    ActivitySelectLocationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        shardEditor=new ShardEditor(this);
        if (shardEditor.loadData().get(ShardEditor.KEY_LANG)!=""){

            Cemmon.setLocale(this, shardEditor.loadData().get(ShardEditor.KEY_LANG));
            Locale locale = new Locale(shardEditor.loadData().get(ShardEditor.KEY_LANG));
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_location);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        try {
            findViewByID();
            startLocationButtonClick();
            pro_bar.setVisibility(View.VISIBLE);
            configureCameraIdle();
            binding.icSaveProceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        //code
                        Intent intent = new Intent();
                        intent.putExtra("location", binding.tvGetLocation.getText().toString());
                        intent.putExtra("lantuid", lat+"");
                        intent.putExtra("lang", lan+"");
                        setResult(RESULT_OK, intent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
          binding.layoutLocation.setVisibility(View.VISIBLE);
          binding.layoutContent.setVisibility(View.GONE);


        }else {
            binding.layoutLocation.setVisibility(View.GONE);
            binding.layoutContent.setVisibility(View.VISIBLE);

        }
        binding.tvTurrn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                binding.layoutLocation.setVisibility(View.GONE);
                binding.layoutContent.setVisibility(View.VISIBLE);
            }
        });


    }

    private void configureCameraIdle() {
        pro_bar.setVisibility(View.VISIBLE);
        onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCameraIdle() {
                try {
                    binding.proBar.setVisibility(View.VISIBLE);
                    LatLng latLng = mMap.getCameraPosition().target;
                    Geocoder geocoder = new Geocoder(SelectLocationActivity.this);
                    binding.tvGetLocation.setText("جارى التحميل...");
                    lat=latLng.latitude;
                    lan=latLng.longitude;
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        if (addressList != null && addressList.size() > 0) {
                            locality = addressList.get(0).getAddressLine(0);
                            country = addressList.get(0).getCountryName();
                            state = addressList.get(0).getAdminArea();
                            sub_admin = addressList.get(0).getSubAdminArea();
                            city = addressList.get(0).getFeatureName();
                            pincode = addressList.get(0).getPostalCode();
                            locality_city = addressList.get(0).getLocality();
                            sub_localoty = addressList.get(0).getSubLocality();
                            country_code = addressList.get(0).getCountryCode();
                            if (city != null && state != null && sub_admin != null && locality_city != null) {
                                binding.tvGetLocation.setText(state + " , "+ city +" , " + locality_city );
                                binding.proBar.setVisibility(View.GONE);
                            } else {
                                binding.tvGetLocation.setText("غير قادر على تحديد الموقع ...");
                                binding.proBar.setVisibility(View.GONE);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void startLocationButtonClick() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        try {
            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            mRequestingLocationUpdates = true;
                            configureCameraIdle();
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                            if (response.isPermanentlyDenied()) {
                                // open device settings when the permission is
                                // denied permanently
                                openSettings();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findViewByID() {
        try {
            context = SelectLocationActivity.this;

            img_pin = findViewById(R.id.img_pin);
            content = findViewById(R.id.content);
            pro_bar = findViewById(R.id.pro_bar);
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
//              content.startRippleAnimation();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openSettings() {
        try {
            Intent intent = new Intent();
            intent.setAction(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package",
                    BuildConfig.APPLICATION_ID, null);
            intent.setData(uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //get device location
    private void getDeviceLocation() {
        Log.d("GoogleMapsActivity", "get location your device");
        try {
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();
                            if (currentLocation != null) {
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 15f);
                            }
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        try {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }


    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            getDeviceLocation();
            mMap.setOnCameraIdleListener(onCameraIdleListener);
            mMap.setTrafficEnabled(true);
            mMap.setIndoorEnabled(true);
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
            mMap.setBuildingsEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setCompassEnabled(false);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.resetMinMaxZoomPreference();
            mMap.getUiSettings().setMapToolbarEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMyLocationClickListener(this);
            mMap.isIndoorEnabled();
            mMap.isBuildingsEnabled();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }
}
