package jnu.action3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;

public class DataListActivity extends AppCompatActivity {
    private ArrayList<Acts> acts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);

        ListView listView = (ListView)findViewById(R.id.data_list);

        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");

        this.LoadRecords(uid);
        ArrayAdapter<Acts> actsArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_activated_1, acts);
        listView.setAdapter(actsArrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Acts act = acts.get(position);
                String gid = String.valueOf(act.getGroup_id());
                Intent intent = new Intent();
                intent.putExtra("gid", gid);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void LoadRecords(String uid){
        acts = (ArrayList<Acts>)DataSupport.where("uid=?", uid).find(Acts.class);
    }
}
