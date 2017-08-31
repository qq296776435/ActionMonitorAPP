package jnu.action;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.litepal.crud.DataSupport;

import java.util.List;

public class UserActivity extends AppCompatActivity {
    private EditText uidText;
    private Button okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        uidText = (EditText)findViewById(R.id.uidText);
        okButton = (Button)findViewById(R.id.id_ok_button);

        uidText.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                if (actionId == EditorInfo.IME_ACTION_GO || (event!=null && event.getKeyCode()==KeyEvent.KEYCODE_ENTER)){
                    onComplete();
                    return true;
                }
                return false;
            }
        });
        okButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onComplete();
            }
        });
    }

    private boolean isUserExisted(String uid){
        List<User> users = DataSupport.where("uid=?", uid).find(User.class);
        return !users.isEmpty();
    }

    private void onComplete(){
        String uid = uidText.getText().toString();
        Intent intent;
        if(isUserExisted(uid)){
            Toast.makeText(UserActivity.this, "User Existed, start collecting", Toast.LENGTH_SHORT).show();
            intent = new Intent(UserActivity.this, ActionListActivity.class);
        }
        else{
            Toast.makeText(UserActivity.this, "User not found, Please input your information", Toast.LENGTH_SHORT).show();
            intent = new Intent(UserActivity.this, CreateUserActivity.class);
        }
        intent.putExtra("uid", uid);
        startActivity(intent);
    }
}
