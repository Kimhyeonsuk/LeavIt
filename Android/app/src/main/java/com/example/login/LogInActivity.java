package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.login.network.NetworkTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import retrofit2.Retrofit;

public class LogInActivity extends AppCompatActivity {
    private EditText et_id, et_password;
    private Button btn_login, btn_findId, btn_findPassword, btn_register;
    private CheckBox chk_login;
    private DBOpenHelper DB_Helper;
    private Toolbar tb_logIn;
    private SharedPreferences.Editor sp_editor_login;
    private SharedPreferences sp_login;
    private HttpURLConnection con;
    private String loginResult;
    private HTTPConnetction connection;

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        // ======================= ????????? ????????? =====================//
        et_id = (EditText) findViewById(R.id.et_id);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        chk_login = (CheckBox) findViewById(R.id.chk_login);
        btn_findId = (Button) findViewById(R.id.btn_findId);
        btn_findPassword = (Button) findViewById(R.id.btn_findPassword);
        btn_register = (Button) findViewById(R.id.btn_register);
        connection=new HTTPConnetction();

        // ======================= ?????? ??????????????? ???????????? ?????? intent??? =================//
        Intent intent_mainPage = new Intent(this, MainPageActivity.class);
        Intent intent_findId = new Intent(this, FindIdActivity.class);
        Intent intent_findPassword = new Intent(this, FindPasswordActivity.class);
        Intent intent_register = new Intent(this, SignUpActivity.class);

        // ================== ?????????????????? ?????? SharedPreference??? Editor ==============//
        sp_login = getSharedPreferences("setting", 0);
        sp_editor_login = sp_login.edit();

        // ====================== ????????? ????????? ????????? ??? ========================//
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ID = et_id.getText().toString(); // ????????? id??? ?????????
                String PW = et_password.getText().toString(); // ????????? ??????????????? ?????????

                String json="";
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("id",ID);
                    jsonObject.put("password",PW);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //NetworkTask networkTask=new NetworkTask(getApplicationContext(),"http://192.168.56.1:3000/signin/submit",jsonObject,"POST");
                NetworkTask networkTask=new NetworkTask(getApplicationContext(),"http://3.35.48.170:3000/signin/submit",jsonObject,"POST");
                try {
                    JSONObject resultObject=new JSONObject(networkTask.execute().get());
                    if(resultObject==null){
                        Log.d("fail","?????? ??????");
                        return;
                    }
                    String resultString=resultObject.getString("msg");

                    if(resultString.equals("success")){
                        startActivity(intent_mainPage);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "????????? ??????", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // ===================== ???????????????, ??????????????????, ???????????? ?????? ===========================//
        btn_findId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "????????? ??????", Toast.LENGTH_SHORT).show();
                startActivity(intent_findId);
            }
        });

        btn_findPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "???????????? ??????", Toast.LENGTH_SHORT).show();
                startActivity(intent_findPassword);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "????????????", Toast.LENGTH_SHORT).show();
                startActivity(intent_register);
            }
        });

        // ========================= ??????????????? ???????????? ===========================//
        // reference : https://es1015.tistory.com/18
        chk_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String ID = et_id.getText().toString(); // ????????? id??? ?????????
                String PW = et_password.getText().toString(); // ????????? ??????????????? ?????????

                if (!ID.equals("") && !PW.equals("")) {
                    if (isChecked == true) {
                        Toast.makeText(LogInActivity.this, "??????????????? ?????????", Toast.LENGTH_SHORT).show();
                        sp_editor_login.putString("ID", ID);
                        sp_editor_login.putString("PW", PW);
                        sp_editor_login.putBoolean("chk_login", true);
                        sp_editor_login.commit();
                    } else {
                        Toast.makeText(LogInActivity.this, "??????????????? ??????", Toast.LENGTH_SHORT).show();
                        sp_editor_login.clear();
                        sp_editor_login.commit();
                    }
                } else {
                    Toast.makeText(LogInActivity.this, "ID??? ??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                    chk_login.setChecked(false);
                }
            }
        });

        if (sp_login.getBoolean("chk_login", false) == true) {
            et_id.setText(sp_login.getString("ID", ""));
            et_password.setText((sp_login.getString("PW", "")));
            chk_login.setChecked(true);
        }

        this.func_Biometric();
    }

    public void func_Biometric(){
        // ???????????? ??? ?????? ??????
        executor = ContextCompat.getMainExecutor(this);

        // ???????????? ????????? ?????? ??????
        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            // ???????????? ?????? ???????????? ???????????? ?????? ???
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), "?????? ????????? ???????????? ????????????", Toast.LENGTH_SHORT).show();

                // ???????????? ??? ?????????
                biometricPrompt.cancelAuthentication();
            }

            // ???????????? ?????????
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "?????? ????????? ??????????????????", Toast.LENGTH_SHORT).show();

                Intent intent_bio_success = new Intent(LogInActivity.this, MainPageActivity.class);
                startActivity(intent_bio_success);
            }

            // ???????????? ?????? ?????? ?????? ???
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "?????? ????????? ??????????????????. ?????? ??????????????????", Toast.LENGTH_SHORT).show();
            }
        });

        // ???????????? ?????? ????????? ?????????
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("?????? ??????")
                //.setSubtitle("????????? ????????? ????????? ???????????? ????????? ??????????????????.")
                .setNegativeButtonText("??????")
                //.setDeviceCredentialAllowed(false)
                .build();

        // ???????????? ??? ?????????
        biometricPrompt.authenticate(promptInfo);
    }
    public boolean CheckPermission(){
        String[] PERMISSIONS= {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if(hasPermissions(getApplicationContext(),PERMISSIONS)){
            return true;
        }
        return false;
    }
    public boolean hasPermissions(Context context, String... permissions){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M&&context!=null&&permissions!=null){
            for(String permission: permissions){
                if(ActivityCompat.checkSelfPermission(context,permission)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(LogInActivity.this,permissions,100);
                }
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions,@NonNull int[] grantResults){
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(getApplicationContext(), "permission was granted", Toast.LENGTH_SHORT).show();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

}