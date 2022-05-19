package android.example.fakeify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText mail, password;
    Button bt_signin, bt_signup;
    int numOfTries = 0;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mail = (EditText) findViewById(R.id.mail);
        password = (EditText) findViewById(R.id.password);
        bt_signin = (Button) findViewById(R.id.bt_signin);
        bt_signup = (Button) findViewById(R.id.bt_signup);
        DB = new DBHelper(this);

        bt_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail_in = mail.getText().toString();
                String password_in = password.getText().toString();

                if(mail_in.isEmpty() || password_in.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();

                }
                else{
                    Boolean validationCheck = DB.validationCheck(mail_in, password_in);
                    if(validationCheck == true){
                        Toast.makeText(MainActivity.this, "Sign in successfull", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), SongListActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Invalid Credentials !!!", Toast.LENGTH_SHORT).show();
                        numOfTries +=1;
                        if (numOfTries>3){
                            numOfTries = 0;
                            Toast.makeText(MainActivity.this, "Password entered incorrectly 3 times. Please register if you don't have an account.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                            startActivity(intent);
                        }
                    }
                }
            }
        });

        bt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });


    }
}