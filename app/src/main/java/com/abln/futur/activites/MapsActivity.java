//package com.abln.futur.activites;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.FragmentActivity;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.AutoCompleteTextView;
//import android.widget.Button;
//
//import com.abln.futur.R;
//import com.abln.futur.common.PrefManager;
//import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
//import com.google.android.gms.common.GooglePlayServicesRepairableException;
//import com.google.android.gms.common.api.Status;
//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.ui.PlaceAutocomplete;
//import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
//import com.google.android.gms.location.places.ui.PlaceSelectionListener;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
//
//    private GoogleMap mMap;
//
//  //  private GoogleMap mMap;
//   // GPSTracker gps;
//    private PrefManager prefManager = new PrefManager();
//
//    private Context mContext;
//    private Button search;
//
//    PlaceAutocompleteFragment placeAutoComplete;
////
////    AutoCompleteTextView autoCompleteTextView;
////    PlaceAutocomplete placeAutoComplete;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maps);
//        mContext = this;
//
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//    }
//
//    private void callSearch(){
//        Intent intent = null;
//        try {
//            intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
//                    .build((Activity)mContext);
//        } catch (GooglePlayServicesRepairableException e) {
//            e.printStackTrace();
//        } catch (GooglePlayServicesNotAvailableException e) {
//            e.printStackTrace();
//        }
//        startActivityForResult(intent, 2000);
//    }
//
//
//    /**
//     * Manipulates the map once available.
//     * This callback is triggered when the map is ready to be used.
//     * This is where we can add markers or lines, add listeners or move the camera. In this case,
//     * we just add a marker near Sydney, Australia.
//     * If Google Play services is not installed on the device, the user will be prompted to install
//     * it inside the SupportMapFragment. This method will only be triggered once the user has
//     * installed Google Play services and returned to the app.
//     */
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        setUpMap();
//
//        // Add a marker in Sydney and move the camera
//        double lat = Double.valueOf(prefManager.getLat());
//        double lan = Double.valueOf(prefManager.getLon());
//        LatLng sydney = new LatLng(lat, lan);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Your current location"));
////        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
////        mMap.setMaxZoomPreference(18f);
//
//        float zoomLevel = 18.0f; //This goes up to 21
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));
//
////        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
////            @Override
////            public void onCameraIdle() {
////                //get latlng at the center by calling
////                LatLng midLatLng = mMap.getCameraPosition().target;
////              /*  if(marker!=null){
////                    marker.remove();
////                }
////                marker= mMap.addMarker(markerOptions);
////              */
////                mMap.clear();
////                mMap.addMarker(new MarkerOptions()
////                        .position(midLatLng)
////                        .title("S"));
////
////            }
////        });
//    }
//
//
//    private void setUpMap() {
//        mMap.setOnMapClickListener(this);// add the listener for click for amap object
//
//    }
//
//    @Override
//    public void onMapClick(LatLng point) {
//        if(mMap!=null){
//            mMap.clear();
//            mMap.addMarker(new MarkerOptions().position(point).title("Selected Location"));
////            mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
////            mMap.setMaxZoomPreference(18f);
//        }
//
//    }
//
//    public void onLocationSelected(View view){
//        switch (view.getId()){
//            case R.id.btn_location_selected:
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapsActivity.this);
//                alertDialog.setTitle("Confirm the Location?");
//                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener()
//                {
//                    public void onClick(DialogInterface dialog,int which)
//                    {
//
//                        prefManager.setSelectedLat(String.valueOf(mMap.getCameraPosition().target.latitude));
//                        prefManager.setSelectedLon(String.valueOf(mMap.getCameraPosition().target.longitude));
////                        GlobalVariables.setGlobal_latitude((float) mMap.getCameraPosition().target.latitude);
////                        GlobalVariables.setGlobal_longitude((float) mMap.getCameraPosition().target.longitude);
//                        MapsActivity.this.finish();
//
//                        dialog.cancel();
//                    }
//                });
//                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener()
//                {
//                    public void onClick(DialogInterface dialog,int which)
//                    {
//                        MapsActivity.this.finish();
//                        dialog.cancel();
//                    }
//                });
//
//                // Showing Alert Message
//                alertDialog.show();
//                break;
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == 2000){
//            Log.d("MAP",""+resultCode+" dd"+data);
//        }
//    }
//}
