package jnu.action;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.InputStream;
import java.util.ArrayList;

public class ActionListActivity extends AppCompatActivity {
    private ArrayList<Action> actionsArrayList;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ListView listView = (ListView) findViewById(R.id.list_list);

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");

        loadJson();
        ArrayAdapter<Action> actionArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, actionsArrayList);
        listView.setAdapter(actionArrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Action action = actionsArrayList.get(position);
                int group_id = DataSupport.count(Acts.class);
                Acts acts = new Acts(action.getName(), action.getDuration(), uid, group_id, action.getId());
                Toast.makeText(ActionListActivity.this, action.getName(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ActionListActivity.this, ActionCollectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("acts", acts);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void loadJson() {
        try {
            actionsArrayList = new ArrayList<>();
            InputStream is = getAssets().open("actions.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String j = new String(buffer, "UTF-8");
            JSONObject json = new JSONObject(j);
            JSONArray actionsJsonList = json.getJSONArray("actions");

            for (int i = 0; i < actionsJsonList.length(); i++){
                JSONObject actionJson = actionsJsonList.getJSONObject(i);
                actionsArrayList.add(new Action(actionJson.getInt("id"), actionJson.getString("name"), actionJson.getInt("duration")));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
