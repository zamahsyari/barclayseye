package zmachmobile.com.barclayseye.activities;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zmachmobile.com.barclayseye.ApiBuilder;
import zmachmobile.com.barclayseye.R;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,SensorEventListener {

    private GoogleMap mMap;
    private SensorManager sensorManager;
    private Sensor sensorMagnetic, sensorAccelerometer;
    private LatLng london;
    float[] gData = new float[3]; // accelerometer
    float[] mData = new float[3]; // magnetometer
    float[] rMat = new float[9];
    float[] iMat = new float[9];
    float[] orientation = new float[3];
    private float bearing;
    Toolbar myToolbar;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        overridePendingTransition(R.anim.anim_slide_in, R.anim.anim_slide_out);

        myToolbar = (Toolbar) findViewById(R.id.appBar);
        myToolbar.setTitle(R.string.app_name);
        setSupportActionBar(myToolbar);

        actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        london= new LatLng(53.2835727000,-0.3338594000);
        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        sensorMagnetic=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorAccelerometer=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,sensorMagnetic,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,sensorAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);

        bearing=0;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.map_style));
        prepareData();

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        LatLng MOUNTAIN_VIEW = new LatLng(37.4, -122.1);

//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
    }

    private void prepareData() {
        Call<Object> service= ApiBuilder.getService().getDirection(53.2835727000,-0.3338594000,52.1284000000,0.2876890000);
        service.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Gson gson=new Gson();
                String json=gson.toJson(response.body());
                try {
                    JSONObject obj=new JSONObject(json);
                    JSONObject data=obj.getJSONObject("data");
                    JSONArray steps=data.getJSONArray("steps");
                    Polyline polyline;
                    PolylineOptions waypoints=new PolylineOptions();
//                    LatLng london= new LatLng(53.2835727000,-0.3338594000);

                    for(int i=0;i<steps.length();i++){
                        JSONObject step=steps.getJSONObject(i);
                        waypoints.add(new LatLng(step.getDouble("start_lat"),step.getDouble("start_longi")));
                        waypoints.add(new LatLng(step.getDouble("end_lat"),step.getDouble("end_longi")));
                    }
                    polyline=mMap.addPolyline(waypoints);
                    polyline.setColor(Color.parseColor("#373f51"));
                    polyline.setStartCap(new RoundCap());
                    polyline.setEndCap(new RoundCap());
                    polyline.setWidth(50f);
                    polyline.setJointType(JointType.ROUND);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(london,15));

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(london)
                            .zoom(15)
                            .bearing(bearing)
                            .tilt(90)
                            .build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),2000,null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float[] data;
        switch ( sensorEvent.sensor.getType() ) {
            case Sensor.TYPE_ACCELEROMETER:
                gData = sensorEvent.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mData = sensorEvent.values.clone();
                break;
            default: return;
        }

        if ( SensorManager.getRotationMatrix( rMat, iMat, gData, mData ) ) {
            bearing= (int) ( Math.toDegrees( SensorManager.getOrientation( rMat, orientation )[0] ) + 360 ) % 360;
        }

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(london)
                .zoom(15)
                .bearing(bearing)
                .tilt(90)
                .build();
        if(mMap!=null){
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,sensorMagnetic,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,sensorAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            intent.putExtra("extra","travel");
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
