package jnu.action3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class DataPlotActivity extends AppCompatActivity {
    private LineChart chart;
    private EditText uidText;
    private EditText gidText;

    private final int ACC_DATA = 1;
    private final int GYR_DATA = 2;

    private final int[] RED = {R.color.Red};
    private final int[] GREEN = {R.color.Green};
    private final int[] BLUE = {R.color.Blue};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_plot);

        chart = (LineChart)findViewById(R.id.chart);
        uidText = (EditText)findViewById(R.id.data_plot_uid_txt);
        gidText = (EditText)findViewById(R.id.data_plot_gid_txt);
        Button accBtn = (Button)findViewById(R.id.data_plot_acc_btn);
        Button gyrBtn = (Button)findViewById(R.id.data_plot_gyr_btn);
        Button actsBtn = (Button)findViewById(R.id.data_plot_acts_query_btn);

        accBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = uidText.getText().toString();
                String gid = gidText.getText().toString();
                List<DataRow> drs = queryDatarows(uid, gid);
                if(drs != null)
                    plot(drs, ACC_DATA);
                else {
                    Toast t = Toast.makeText(getApplicationContext(), "No such records found.", Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER, 0, 0);
                    t.show();
                }
            }
        });
        gyrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = uidText.getText().toString();
                String gid = gidText.getText().toString();
                List<DataRow> drs = queryDatarows(uid, gid);
                if(drs != null)
                    plot(drs, GYR_DATA);
                else {
                    Toast t = Toast.makeText(getApplicationContext(), "No such records found.", Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER, 0, 0);
                    t.show();
                }
            }
        });

        actsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = uidText.getText().toString();
                if(uid.equals("")) {
                    Toast t = Toast.makeText(getApplicationContext(), "User ID cannot be empty.", Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER, 0, 0);
                    t.show();
                }
                else {
                    Intent intent = new Intent(DataPlotActivity.this, DataListActivity.class);
                    intent.putExtra("uid", uid);
                    startActivityForResult(intent, 1);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                String gid = data.getStringExtra("gid");
                gidText.setText(gid);
            }
        }
    }

    private List<DataRow> queryDatarows(String uid, String gid){
        Acts act = DataSupport.where("uid=? AND group_id=?", uid, gid).findFirst(Acts.class);
        if(act != null)
            return DataSupport.where("group_id=?", gid).find(DataRow.class);
        else
            return null;
    }

    private void plot(List<DataRow> datarows, int type){
        List<Entry> x_entry = new ArrayList<>();
        List<Entry> y_entry = new ArrayList<>();
        List<Entry> z_entry = new ArrayList<>();
        LineDataSet x_dataSet;
        LineDataSet y_dataSet;
        LineDataSet z_dataSet;

        int t = 0;
        if(type == this.ACC_DATA){
            for(DataRow data: datarows){
                x_entry.add(new Entry(t, data.getAccX()));
                y_entry.add(new Entry(t, data.getAccY()));
                z_entry.add(new Entry(t++, data.getAccZ()));
            }
            x_dataSet = new LineDataSet(x_entry, "Acc_X");
            y_dataSet = new LineDataSet(y_entry, "Acc_Y");
            z_dataSet = new LineDataSet(z_entry, "Acc_Z");
        }
        else{
            for(DataRow data: datarows){
                x_entry.add(new Entry(t, data.getGyrX()));
                y_entry.add(new Entry(t, data.getGyrY()));
                z_entry.add(new Entry(t++, data.getGyrZ()));
            }
            x_dataSet = new LineDataSet(x_entry, "Gyr_X");
            y_dataSet = new LineDataSet(y_entry, "Gyr_Y");
            z_dataSet = new LineDataSet(z_entry, "Gyr_Z");
        }

        x_dataSet.setColors(RED, getApplicationContext());
        x_dataSet.setCircleColors(RED, getApplicationContext());
        x_dataSet.setCircleRadius(1f);
        y_dataSet.setColors(GREEN, getApplicationContext());
        y_dataSet.setCircleColors(GREEN, getApplicationContext());
        y_dataSet.setCircleRadius(1f);
        z_dataSet.setColors(BLUE, getApplicationContext());
        z_dataSet.setCircleColors(BLUE, getApplicationContext());
        z_dataSet.setCircleRadius(1f);

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(x_dataSet);
        dataSets.add(y_dataSet);
        dataSets.add(z_dataSet);

        LineData lineData = new LineData(dataSets);
        chart.setData(lineData);
        chart.invalidate();
    }
}
