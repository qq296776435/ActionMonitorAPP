package jnu.action3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

public class CreateUserActivity extends AppCompatActivity {
    private TextView uidText;
    private EditText ageInput;
    private EditText heightInput;
    private EditText weightInput;
    private RadioGroup genderGroup;
    private RadioButton selectedGender;
    private Button okButton;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        uidText = (TextView)findViewById(R.id.create_user_uid_txt);
        ageInput = (EditText)findViewById(R.id.create_user_age_edit);
        heightInput = (EditText)findViewById(R.id.create_user_height_edit);
        weightInput = (EditText)findViewById(R.id.create_user_weight_edit);
        genderGroup = (RadioGroup)findViewById(R.id.create_user_gender_radio);
        selectedGender = (RadioButton)genderGroup.findViewById(genderGroup.getCheckedRadioButtonId());
        okButton = (Button)findViewById(R.id.create_user_ok_btn);

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        uidText.setText(uid);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int age = Integer.parseInt(ageInput.getText().toString());
                float height = Float.parseFloat(heightInput.getText().toString());
                float weight = Float.parseFloat(weightInput.getText().toString());
                boolean isMale = selectedGender.getText().toString().equals("Male");

                User user = new User(uid, isMale, age, height, weight);
                user.save();

                Toast.makeText(CreateUserActivity.this, "New user created.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CreateUserActivity.this, ActionListActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });
    }
}
