package jnu.action;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

public class ActionCollectActivity extends AppCompatActivity implements ServiceConnection {
    private Acts acts;
    private boolean hasStarted;
    public static UIHandler handler;

    public static final int POP_SUCCESS = 4;
    public static final int POP_FAIL = 5;

    private TextView remainTime;
    private Button startButton;
    private Button stopButton;
    private ActionCollectService.Binder myBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_collect_layout);

        handler = new UIHandler();
        Intent intent = getIntent();
        acts = (Acts)intent.getSerializableExtra("acts");

        TextView title = (TextView) findViewById(R.id.action_title);
        title.setText(acts.getAction_name());
        hasStarted = false;

        remainTime = (TextView) findViewById(R.id.remainingTime);
        startButton = (Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.stopButton);

        remainTime.setText(String.format("%d", acts.getDuration()));

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CLICK_EVENT", "click start");
                handleStart();
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.d("CLICK_EVENT", "click stop");
                handleStop();
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
                case POP_FAIL:
                    int sizef = msg.getData().getInt("value");
                    Toast failToast = Toast.makeText(ActionCollectActivity.this, "Collect Failed!\n"+sizef, Toast.LENGTH_LONG);
                    failToast.setGravity(Gravity.CENTER, 0, 0);
                    LinearLayout toastView1 = (LinearLayout)failToast.getView();
                    ImageView imageView1 = new ImageView(getApplicationContext());
                    imageView1.setImageResource(R.drawable.cross);
                    toastView1.addView(imageView1, 0);
                    failToast.show();
                    unbind();
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
            startService(startIntent);
            bindService(startIntent, this, Context.BIND_AUTO_CREATE);
            Toast.makeText(ActionCollectActivity.this, "Start Collecting...", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleStop() {
        if (hasStarted && myBinder != null){
            hasStarted = false;
            myBinder.cancel();   // ???
            Intent stopIntent = new Intent(this, ActionCollectService.class);
            stopService(stopIntent);
            Toast.makeText(ActionCollectActivity.this, "Collect Cancel", Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }
}
