package jnu.action3;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.LinkedList;

public class ActionCollectActivity extends AppCompatActivity implements ServiceConnection {
    private Acts acts;
    private boolean hasStarted;
    int currentAngle;
    public static UIHandler handler;

    static final int POP_SUCCESS = 7;
    static final int ACC_DATA = 8;
    static final int GYR_DATA = 9;

    private Button startButton;
    private Button stopButton;
    private Button accButton;
    private Button gyrButton;
    private Button angle0Button;
    private Button angle45Button;
    private Button angle90Button;
    private Button angle135Button;
    private Button angle180Button;
    private Button angle225Button;
    private Button angle270Button;
    private Button angle315Button;
    private Button[] angleButtons;
    private LineChart chart;

    private ActionCollectService.Binder myBinder;

    private LineDataSet acc_x, acc_y, acc_z, gyr_x, gyr_y, gyr_z;
    private LineData linedata;

    private final int[] RED = {R.color.Red};
    private final int[] GREEN = {R.color.Green};
    private final int[] BLUE = {R.color.Blue};

    private int acc_count, gyr_count;
    private static final int DISPLAY_SIZE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_collect_layout);

        TextView title = (TextView)findViewById(R.id.action_collect_title_txt);
        TextView remainTime = (TextView)findViewById(R.id.action_colect_remain_txt);
        startButton = (Button)findViewById(R.id.action_collect_start_btn);
        stopButton = (Button)findViewById(R.id.action_collect_stop_btn);
        accButton = (Button)findViewById(R.id.action_colect_acc_btn);
        gyrButton = (Button)findViewById(R.id.action_colect_gyr_btn);
        angle0Button = (Button)findViewById(R.id.Angle_0);
        angle45Button = (Button)findViewById(R.id.Angle_45);
        angle90Button = (Button)findViewById(R.id.Angle_90);
        angle135Button = (Button)findViewById(R.id.Angle_135);
        angle180Button = (Button)findViewById(R.id.Angle_180);
        angle225Button = (Button)findViewById(R.id.Angle_225);
        angle270Button = (Button)findViewById(R.id.Angle_270);
        angle315Button = (Button)findViewById(R.id.Angle_315);
        final Button[] angleButtons = {angle0Button,angle45Button,angle90Button,angle135Button,
                angle180Button,angle225Button,angle270Button,angle315Button};
        this.angleButtons = angleButtons;

        chart = (LineChart)findViewById(R.id.realtime_chart);

        handler = new UIHandler();
        Intent intent = getIntent();
        acts = (Acts)intent.getSerializableExtra("acts");

        title.setText(acts.getAction_name());
        remainTime.setText(String.valueOf(acts.getDuration()));
        hasStarted = false;

        acc_x = new LineDataSet(new LinkedList<Entry>(), "acc_x");
        acc_x.setColors(RED, getApplicationContext());
        acc_x.setCircleColors(RED, getApplicationContext());
        acc_x.setCircleRadius(1f);
        acc_y = new LineDataSet(new LinkedList<Entry>(), "acc_y");
        acc_y.setColors(GREEN, getApplicationContext());
        acc_y.setCircleColors(GREEN, getApplicationContext());
        acc_y.setCircleRadius(1f);
        acc_z = new LineDataSet(new LinkedList<Entry>(), "acc_z");
        acc_z.setColors(BLUE, getApplicationContext());
        acc_z.setCircleColors(BLUE, getApplicationContext());
        acc_z.setCircleRadius(1f);
        gyr_x = new LineDataSet(new LinkedList<Entry>(), "gyr_x");
        gyr_x.setColors(RED, getApplicationContext());
        gyr_x.setCircleColors(RED, getApplicationContext());
        gyr_x.setCircleRadius(1f);
        gyr_y = new LineDataSet(new LinkedList<Entry>(), "gyr_y");
        gyr_y.setColors(GREEN, getApplicationContext());
        gyr_y.setCircleColors(GREEN, getApplicationContext());
        gyr_y.setCircleRadius(1f);
        gyr_z = new LineDataSet(new LinkedList<Entry>(), "gyr_z");
        gyr_z.setColors(BLUE, getApplicationContext());
        gyr_z.setCircleColors(BLUE, getApplicationContext());
        gyr_z.setCircleRadius(1f);
        acc_count = 0;
        gyr_count = 0;

        linedata = new LineData();

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setAxisMaximum(10f);
        yAxis.setAxisMinimum(-10f);
        chart.setData(linedata);

        currentAngle = -1;

        startButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handleStart();
            }
        });
        stopButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v){
                handleStop();
            }
        });
        accButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linedata.getDataSetCount() == 3) {
                    linedata.removeDataSet(0);
                    linedata.removeDataSet(0);
                    linedata.removeDataSet(0);
                }
                linedata.addDataSet(acc_x);
                linedata.addDataSet(acc_y);
                linedata.addDataSet(acc_z);
                chart.notifyDataSetChanged();
            }
        });
        gyrButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linedata.getDataSetCount() == 3){
                    linedata.removeDataSet(0);
                    linedata.removeDataSet(0);
                    linedata.removeDataSet(0);
                }
                linedata.addDataSet(gyr_x);
                linedata.addDataSet(gyr_y);
                linedata.addDataSet(gyr_z);
                chart.notifyDataSetChanged();
            }
        });

        angle0Button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                
                for (Button button:angleButtons){
                    button.setEnabled(false);
                }

                currentAngle = 0;

                startButton.setEnabled(true);
                startButton.setText(startButton.getText()+" 0°");
            }
        });
        angle45Button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                
                for (Button button:angleButtons){
                    button.setEnabled(false);
                }

                currentAngle = 45;

                startButton.setEnabled(true);
                startButton.setText(startButton.getText()+" 45°");
            }
        });
        angle90Button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                
                for (Button button:angleButtons){
                    button.setEnabled(false);
                }

                currentAngle = 90;

                startButton.setEnabled(true);
                startButton.setText(startButton.getText()+" 90°");
            }
        });
        angle135Button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                
                for (Button button:angleButtons){
                    button.setEnabled(false);
                }

                currentAngle = 135;

                startButton.setEnabled(true);
                startButton.setText(startButton.getText()+" 135°");
            }
        });
        angle180Button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                
                for (Button button:angleButtons){
                    button.setEnabled(false);
                }

                currentAngle = 180;

                startButton.setEnabled(true);
                startButton.setText(startButton.getText()+" 180°");
            }
        });
        angle225Button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                
                for (Button button:angleButtons){
                    button.setEnabled(false);
                }

                currentAngle = 225;

                startButton.setEnabled(true);
                startButton.setText(startButton.getText()+" 225°");
            }
        });
        angle270Button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                for (Button button:angleButtons){
                    button.setEnabled(false);
                }

                currentAngle = 270;

                startButton.setEnabled(true);
                startButton.setText(startButton.getText()+" 270°");
            }
        });
        angle315Button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                for (Button button:angleButtons){
                    button.setEnabled(false);
                }

                currentAngle = 315;

                startButton.setEnabled(true);
                startButton.setText(startButton.getText()+" 315°");
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    class UIHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
            switch (msg.what) {
                case POP_SUCCESS:
                    int sizes = msg.getData().getInt("value");
                    Toast successToast = Toast.makeText(ActionCollectActivity.this, "Collect Success!\n"+sizes, Toast.LENGTH_LONG);
                    successToast.setGravity(Gravity.CENTER, 0, 0);
                    LinearLayout toastView = (LinearLayout)successToast.getView();
                    ImageView imageView = new ImageView(getApplicationContext());
                    imageView.setImageResource(R.drawable.tick);
                    toastView.addView(imageView, 0);
                    successToast.show();
                    acts.save();
                    unbind();
                    break;
                case ACC_DATA:
                    float[] acc = msg.getData().getFloatArray("value");
                    if(acc_x.getEntryCount() == DISPLAY_SIZE) {
                        acc_x.removeFirst();
                        acc_y.removeFirst();
                        acc_z.removeFirst();
                    }
                    acc_x.addEntry(new Entry(acc_count, acc[0]));
                    acc_y.addEntry(new Entry(acc_count, acc[1]));
                    acc_z.addEntry(new Entry(acc_count++, acc[2]));
                    linedata.notifyDataChanged();
                    chart.notifyDataSetChanged();
                    chart.invalidate();
                    break;
                case GYR_DATA:
                    float[] gyr = msg.getData().getFloatArray("value");
                    if(gyr_x.getEntryCount() == DISPLAY_SIZE) {
                        gyr_x.removeFirst();
                        gyr_y.removeFirst();
                        gyr_z.removeFirst();
                    }
                    gyr_x.addEntry(new Entry(gyr_count, gyr[0]));
                    gyr_y.addEntry(new Entry(gyr_count, gyr[1]));
                    gyr_z.addEntry(new Entry(gyr_count++, gyr[2]));
                    linedata.notifyDataChanged();
                    chart.notifyDataSetChanged();
                    chart.invalidate();
                    break;
            }
        }
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        myBinder = (ActionCollectService.Binder)iBinder;
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName){
        myBinder = null;
    }

    private void unbind(){
        unbindService(this);
        this.finish();
    }

    private void handleStart() {
        if (!hasStarted){
            hasStarted = true;
            startButton.setClickable(false);

            Intent startIntent = new Intent(this, ActionCollectService.class);
            startIntent.putExtra("action_duration", acts.getDuration());
            startIntent.putExtra("group_id", acts.getGroup_id());
            startIntent.putExtra("angle", currentAngle);
            startService(startIntent);
            bindService(startIntent, this, Context.BIND_AUTO_CREATE);
            Toast.makeText(ActionCollectActivity.this, "Start Collecting...", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleStop() {
        if (hasStarted && myBinder != null){
            hasStarted = false;
            myBinder.cancel();
            Intent stopIntent = new Intent(this, ActionCollectService.class);
            stopService(stopIntent);
            Toast.makeText(ActionCollectActivity.this, "Collect Cancel", Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }
}
