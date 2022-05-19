package android.example.fakeify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    EditText r_name, r_surname, r_mail, r_password, r_password_again;
    Button bt_register;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        r_name = (EditText) findViewById(R.id.r_name);
        r_surname = (EditText) findViewById(R.id.r_surname);
        r_mail = (EditText) findViewById(R.id.r_mail);
        r_password = (EditText) findViewById(R.id.r_password);
        r_password_again = (EditText) findViewById(R.id.r_password_again);
        bt_register = (Button) findViewById(R.id.bt_register);

        DB = new DBHelper(this);




        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = r_name.getText().toString();
                String surname = r_surname.getText().toString();
                String mail = r_mail.getText().toString();
                String password = r_password.getText().toString();
                String password_again = r_password_again.getText().toString();

                if(name.isEmpty() || surname.isEmpty() || mail.isEmpty() || password.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Please fill all the fields.", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(password.equals(password_again)){
                        Boolean checkMail = DB.checkMail(mail);
                        if(checkMail == false){
                            Boolean insert = DB.insertData(name, surname, mail, password);
                            if (insert==true){
                                Toast.makeText(RegisterActivity.this, "You are successfully registered. An info mail has sent.", Toast.LENGTH_SHORT).show();
                                sendMail(name, surname, mail, password);
                                finish();
                            }
                        }
                        else{
                            Toast.makeText(RegisterActivity.this, "Mail already exists. Please sign in.", Toast.LENGTH_SHORT).show();

                        }
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "Passwords does not match", Toast.LENGTH_SHORT).show();

                    }
                }
        };
            private void sendMail(String name, String surname, String mail, String password) {
                final String subject = "Fakeify Uygulama Bilgileriniz";
                String message = "Welcome to Fakeify!!!\n" + "User Information\n" + "Name: " + name +
                        "\nSurname: " + surname + "\nPassword:" + password;
                JavaMailAPI javaMailAPI = new JavaMailAPI(RegisterActivity.this, mail, subject, message);
                javaMailAPI.execute();
            };



        });

    }
}