package zmachmobile.com.barclayseye.activities;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zmachmobile.com.barclayseye.ApiBuilder;
import zmachmobile.com.barclayseye.ButtonChild;
import zmachmobile.com.barclayseye.Config;
import zmachmobile.com.barclayseye.MapChild;
import zmachmobile.com.barclayseye.R;
import zmachmobile.com.barclayseye.adapters.MapAdapter;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,SensorEventListener {

    private GoogleMap mMap;
    private SensorManager sensorManager;
    private Sensor sensorMagnetic, sensorAccelerometer;
    private LatLng london, destination;
    float[] gData = new float[3]; // accelerometer
    float[] mData = new float[3]; // magnetometer
    float[] rMat = new float[9];
    float[] iMat = new float[9];
    float[] orientation = new float[3];
    private float bearing;
    Toolbar myToolbar;
    ActionBar actionBar;
    private ImageView pullArrow, imgStreet;
    private TextView txtStreet;
    private boolean isVisible=false;
    private MarkerOptions markerStart;
    private MarkerOptions markerStop;
    private BitmapDescriptor iconStart;
    private BitmapDescriptor iconStop;
    PolylineOptions waypoints;
    Polyline polyline;
    private String json;
    private ButtonChild buttonChild;
    private double selectedDistance;
    private String selectedStreet;
    private TextView txtDuration, txtArrival;

    List<MapChild> mapChildList=new ArrayList<>();
    RecyclerView recyclerView;
    MapAdapter mapAdapter;
    String voiceInput,voiceTry;
    final int REQ_CODE_SPEECH_INPUT=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Config.isVoiceOnly==true){
            setContentView(R.layout.activity_maps);
        }else{
            if(Config.isModeYellow==true){
                setContentView(R.layout.activity_maps_yellow);
            }else{
                setContentView(R.layout.activity_maps_green);
            }
        }

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        pullArrow=(ImageView)findViewById(R.id.pullArrow);
        imgStreet=(ImageView)findViewById(R.id.imgStreet);
        txtStreet=(TextView)findViewById(R.id.txtStreet);
        txtDuration=(TextView)findViewById(R.id.txtDuration);
        txtArrival=(TextView)findViewById(R.id.txtArrival);

        overridePendingTransition(R.anim.anim_slide_in, R.anim.anim_slide_out);

        try{
            Config.textToSpeech.shutdown();
        }catch (Exception e){
            e.printStackTrace();
        }

        myToolbar = (Toolbar) findViewById(R.id.appBar);
        myToolbar.setTitle(R.string.app_name);
        setSupportActionBar(myToolbar);

        actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        london= new LatLng(53.463536, -2.291418);
        destination=new LatLng(52.1284000000,0.2876890000);
        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        sensorMagnetic=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorAccelerometer=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,sensorMagnetic,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,sensorAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);

        bearing=0;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapAdapter=new MapAdapter(mapChildList);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mapAdapter);
        recyclerView.setVisibility(View.GONE);

        pullArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVisible==false){
                    pullArrow.setImageResource(R.drawable.chevron_down);
                    recyclerView.setVisibility(View.VISIBLE);
                    isVisible=true;
                }else {
                    recyclerView.setVisibility(View.GONE);
                    pullArrow.setImageResource(R.drawable.chevron_up);
                    isVisible = false;
                }
            }
        });

        Bundle extras=getIntent().getExtras();
        String json=extras.getString("json");
        try {
            JSONObject jsonObject=new JSONObject(json);
            selectedStreet = jsonObject.getString("street");
            selectedDistance=jsonObject.getDouble("distance");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.map_style));
        prepareData();

        iconStart= BitmapDescriptorFactory.fromResource(R.drawable.marker_start);
        iconStop= BitmapDescriptorFactory.fromResource(R.drawable.icon_location);
        markerStop=new MarkerOptions()
                .position(destination)
                .title("Destination")
                .icon(iconStop);
        markerStart=new MarkerOptions()
                .position(london)
                .icon(iconStart);
        mMap.addMarker(markerStart);
        mMap.addMarker(markerStop);
    }

    private void prepareData() {
        Call<Object> service= ApiBuilder.getService().getDirection(53.463536, -2.291418,53.4750000000,-2.2865500000);
        service.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Gson gson=new Gson();
                String json=gson.toJson(response.body());
                try {
                    JSONObject obj=new JSONObject(json);
                    JSONObject data=obj.getJSONObject("data");
                    JSONArray steps=data.getJSONArray("steps");
                    waypoints=new PolylineOptions();
                    txtDuration.setText(data.getString("duration"));
                    txtArrival.setText(data.getString("arrival_estimation"));

                    for(int i=0;i<steps.length();i++){
                        JSONObject step=steps.getJSONObject(i);
                        waypoints.add(new LatLng(step.getDouble("start_lat"),step.getDouble("start_longi")));
                        waypoints.add(new LatLng(step.getDouble("end_lat"),step.getDouble("end_longi")));

                        mapChildList.add(new MapChild(step.getString("distance"),step.getString("instruction")));
                    }
                    mapAdapter.notifyDataSetChanged();

                    polyline=mMap.addPolyline(waypoints);
                    polyline.setColor(Color.parseColor("#373f51"));
                    polyline.setStartCap(new RoundCap());
                    polyline.setEndCap(new RoundCap());
                    polyline.setWidth(50f);
                    polyline.setJointType(JointType.ROUND);

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destination,10));

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(london)
                            .zoom(15)
                            .bearing(135)
                            .tilt(90)
                            .build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    voiceInput="We’re now heading to Barclays ATM, "+selectedStreet+" located "+Double.valueOf(Math.round(selectedDistance))+" from your location";
                    voiceTry="We didn't get that, please try again";
                    Config.textToSpeech = new TextToSpeech(getBaseContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if(status != TextToSpeech.ERROR) {
                                Config.textToSpeech.setLanguage(Locale.UK);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    Config.textToSpeech.speak(voiceInput,TextToSpeech.QUEUE_FLUSH,null,"CHOOSE");
                                }
                            }
                        }
                    });

                    Handler handler=new Handler();
                    animateMap(handler,53.4641052,-2.2919664,150,3000,"Head north toward Wharfside Way/A5081");
                    animateMap(handler,53.4653081,-2.2913433,150,6000,"Turn right onto Wharfside Way/A5081");
                    animateMap(handler,53.4651301,-2.289458,150,9000,"Turn left onto Sir Alex Ferguson Way");
                    animateMap(handler,53.4664417,-2.2890327,150,12000,"Turn left onto Trafford Wharf Rd");
                    animateMap(handler,53.4673137,-2.2924256,150,15000,"Turn right");
                    animateMap(handler,53.4678629,-2.2919817,150,18000,"Turn left");
                    animateMap(handler,53.4692677,-2.2960698,150,21000,"Turn right toward The Quays. Take the stairs");
                    animateMap(handler,53.4711682,-2.2949453,150,24000,"Slight left onto The Quays. Go through 1 roundabout");
                    animateMap(handler,53.4746917,-2.2876713,150,27000,"Slight right to stay on The Quays");
                    animateMap(handler,53.4750935,-2.2867761,150,30000,"Turn right onto Anchorage Quay. Destination will be on the left");

//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            Intent intent=new Intent(getApplicationContext(), FinalActivity.class);
//                            startActivity(intent);
//                        }
//                    },33000);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    private void animateMap(Handler handler, final double latitude, final double longitude, final int bearing, int duration, final String voice) {
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                Config.textToSpeech = new TextToSpeech(getBaseContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if(status != TextToSpeech.ERROR) {
                            Config.textToSpeech.setLanguage(Locale.UK);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Config.textToSpeech.speak(voice,TextToSpeech.QUEUE_FLUSH,null,"CHOOSE");
                            }
                        }
                    }
                });

                mMap.clear();
                LatLng startPoint=new LatLng(latitude,longitude);
                mMap.addMarker(new MarkerOptions()
                        .position(startPoint)
                        .icon(iconStart)
                );
                mMap.addMarker(markerStop);
                polyline=mMap.addPolyline(waypoints);
                polyline.setColor(Color.parseColor("#373f51"));
                polyline.setStartCap(new RoundCap());
                polyline.setEndCap(new RoundCap());
                polyline.setWidth(50f);
                polyline.setJointType(JointType.ROUND);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(latitude,longitude))
                        .zoom(15)
                        .bearing(bearing)
                        .tilt(90)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        };
        handler.postDelayed(runnable,duration);
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
                .zoom(10)
                .bearing(bearing)
                .tilt(0)
                .build();
        if(mMap!=null){
//            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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
