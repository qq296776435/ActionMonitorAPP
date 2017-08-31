package jnu.action;

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
    private RadioGroup groupGender;
    private RadioButton selectedGender;
    private Button okButton;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        ageInput = (EditText)findViewById(R.id.ageInput);
        heightInput = (EditText)findViewById(R.id.heightInput);
        weightInput = (EditText)findViewById(R.id.weightInput);
        groupGender = (RadioGroup)findViewById(R.id.radioGender);
        uidText = (TextView)findViewById(R.id.uidText);

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        uidText.setText(uid);

        okButton = (Button)findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedGender = (RadioButton)groupGender.findViewById(groupGender.getCheckedRadioButtonId());
                int age = 0;
                float height = 0.0f;
                float weight = 0.0f;
                boolean isMale = selectedGender.getText().toString().equals("Male");
                try{
                    age = Integer.parseInt(ageInput.getText().toString());
                    height = Float.parseFloat(heightInput.getText().toString());
                    weight = Float.parseFloat(weightInput.getText().toString());
                } catch (Exception e){
                    Toast.makeText(CreateUserActivity.this, "Make sure your message is correctly input.", Toast.LENGTH_SHORT).show();
                }
                if (age>0 && height>0.0f && weight>0.0f) {
                    User user = new User(uid, isMale, age, height, weight);
                    user.save();

                    Toast.makeText(CreateUserActivity.this, "New user created.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateUserActivity.this, ActionListActivity.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent);
                }
            }
        });
    }
}
