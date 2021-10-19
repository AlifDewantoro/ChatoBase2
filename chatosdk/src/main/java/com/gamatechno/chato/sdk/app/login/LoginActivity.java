package com.gamatechno.chato.sdk.app.login;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gamatechno.chato.sdk.R;
import com.gamatechno.chato.sdk.data.DAO.Customer.Customer;
import com.gamatechno.chato.sdk.data.constant.Preferences;
import com.gamatechno.chato.sdk.module.activity.ChatoCoreActivity;
import com.gamatechno.chato.sdk.utils.ChatoAlertDialog.ChatoAlertDialog;
import com.gamatechno.ggfw.utils.GGFWUtil;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

public class LoginActivity extends ChatoCoreActivity implements LoginView.View {


    protected Button btnLogin;
    protected EditText edNip;
    protected EditText edPassword;
    protected TextView tv_lupapassword;
    protected TextView tv_login;
    protected TextView tv_perusahaan;
    protected TextView tv_daftar;
    protected LinearLayout lay_daftar;
    private String nip;
    private String password;
    private LoginPresenter presenter;

    Gson gson = new Gson();
    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

        if(GGFWUtil.getStringFromSP(getContext(), Preferences.CUSTOMER_INFO).equals("")){
            // startActivity(new Intent(getContext(), UserTokenActivity.class));
            finish();
        } else {
            customer = gson.fromJson(GGFWUtil.getStringFromSP(getContext(), Preferences.CUSTOMER_INFO), Customer.class);
            if(customer.getAuth_type().equals(Customer.aut_type_ldap)){
                lay_daftar.setVisibility(View.GONE);
                tv_lupapassword.setVisibility(View.GONE);
            }
            tv_login.setText("Login ");
            tv_perusahaan.setText(customer.getCustomer_company());
        }
        presenter = new LoginPresenter(getContext(), this);

        btnLogin.setOnClickListener(v -> {
            if (checkKeyChanges()) {
                nip = edNip.getText().toString();
                password = edPassword.getText().toString();
                validsai();
            }
        });

        tv_daftar.setOnClickListener(v -> {
//            startActivity(new Intent(getContext(), RegisterActivity.class));
        });

        edNip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (checkKeyChanges()) {
                    btnLogin.setBackground(getResources().getDrawable(R.drawable.btn_login));
                    btnLogin.setClickable(true);
                } else {
                    btnLogin.setBackgroundColor(getContext().getResources().getColor(R.color.grey_300));
                    btnLogin.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (checkKeyChanges()) {
                    btnLogin.setBackground(getResources().getDrawable(R.drawable.button_main));
                    btnLogin.setClickable(true);
                } else {
                    btnLogin.setBackground(getContext().getResources().getDrawable(R.drawable.button_disable));
                    btnLogin.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private boolean checkKeyChanges() {
        if (edNip.getText().toString().equals("") || edPassword.getText().toString().equals("")) {
            return false;
        } else {
            return true;
        }
    }

    private void validsai() {

        if (TextUtils.isEmpty(nip) || TextUtils.isEmpty(password)) {
            edNip.setError("Data tidak boleh kososng");
            edPassword.setError("Data tidak boleh kososng");
        } else {
            presenter.doLogin(nip, password);
        }

    }

    @Override
    public void onSucces() {
        Log.d("LoginActivity", "onSucces: "+FirebaseInstanceId.getInstance().getToken());
        String token = FirebaseInstanceId.getInstance().getToken();
        if(token == null){
            token = FirebaseInstanceId.getInstance().getToken();
        }
        GGFWUtil.setStringToSP(getContext(), Preferences.FIREBASE_TOKEN, token);
        presenter.updateTokenFirebase(token);
    }

    @Override
    public void onSuccessUpdateTokenFirebase() {
//        startActivity(new Intent(LoginActivity.this, ChatoActivity.class));
        LoginActivity.this.finish();
    }

    @Override
    public void onFailedUpdateTokenFirebase() {
        GGFWUtil.ToastShort(getContext(), getResources().getString(R.string.helper_error_server));
    }

    @Override
    public void onLoading() {
        btnLogin.setBackground(getContext().getResources().getDrawable(R.drawable.button_disable));
        btnLogin.setText("Mohon tunggu...");
        btnLogin.setClickable(false);
    }

    @Override
    public void onHideLoading() {
        btnLogin.setBackground(getResources().getDrawable(R.drawable.button_main));
        btnLogin.setText("L O G I N");
        btnLogin.setClickable(true);
    }

    @Override
    public void onErrorConnection(String message) {

    }

    @Override
    public void onAuthFailed(String error) {
        new ChatoAlertDialog(getContext(),
                               "Kombinasi username dan kata sandi salah",
                               "Pastikan kombinasi username dan kata sandi anda benar dan terdaftar. Silahkan mencoba kembali.",
                                new ChatoAlertDialog.OnAlertDialog(){
                                    @Override
                                    public void onOk(Dialog dialog) {
                                        dialog.dismiss();
                                    }
                                });
        /*new AlertDialog.Builder(LoginActivity.this)
                .setMessage(error)
                .setPositiveButton("Ok", null)
                .show();*/
    }

    private void initView(){
        btnLogin = findViewById(R.id.btn_login);
        tv_lupapassword = findViewById(R.id.tv_lupapassword);
        lay_daftar = findViewById(R.id.lay_daftar);
        tv_login = findViewById(R.id.tv_login);
        tv_perusahaan = findViewById(R.id.tv_perusahaan);
        tv_daftar = findViewById(R.id.tv_daftar);
        edNip = findViewById(R.id.et_nip);
        edPassword = findViewById(R.id.et_password);
    }
}
