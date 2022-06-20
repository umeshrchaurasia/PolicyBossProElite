package com.policyboss.policybossproelite.switchuser;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.policyboss.policybossproelite.BaseActivity;
import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.utility.Constants;
import com.policyboss.policybossproelite.utility.ReadDeviceID;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import magicfinmart.datacomp.com.finmartserviceapi.PrefManager;
import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.APIResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.IResponseSubcriber;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.controller.login.LoginController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.LoginResponseEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.PospAgentEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.UserConstantEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.requestentity.LoginRequestEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.CheckLoginResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.LoginResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.PospAgentResponse;

public class SwitchUserActivity extends BaseActivity implements IResponseSubcriber {
    Button btnswitchuser;
    PrefManager prefManager;
    LoginRequestEntity loginRequestEntity;
    final String mapKey = "map_switchuser";
    LoginResponseEntity loginResponseEntity;
    DBPersistanceController db;
    RecyclerView rvSwitchUser;
    SwitchUserAdapter mAdapter;
    List<PospAgentEntity> switchUserLst;
    EditText etSearch;
    UserConstantEntity userConstantEntity;
    private PospAgentEntity pospAgentEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        loginRequestEntity = new LoginRequestEntity();
        prefManager = new PrefManager(this);
        db = new DBPersistanceController(SwitchUserActivity.this);
        loginResponseEntity = db.getUserData();
        userConstantEntity = db.getUserConstantsData();

        initialize();
        setTextWatcher();

        showDialog();
        new LoginController(SwitchUserActivity.this).getPospAgentData(userConstantEntity.getUserid(), "", SwitchUserActivity.this);

        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etSearch.setCursorVisible(true);
            }
        });
    }

    private void initialize() {

        etSearch = (EditText) findViewById(R.id.etSearch);
        btnswitchuser = (Button) findViewById(R.id.btnswitchuser);

        rvSwitchUser = (RecyclerView) findViewById(R.id.rvSwitchUser);
        rvSwitchUser.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SwitchUserActivity.this);
        rvSwitchUser.setLayoutManager(layoutManager);


    }

    private void saveMap(Map<String, String> inputMap) {
        SharedPreferences pSharedPref = getApplicationContext().getSharedPreferences(Constants.SWITCh_ParentDeatils_FINMART,
                Context.MODE_PRIVATE);
        if (pSharedPref != null) {
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.remove(mapKey).apply();
            editor.putString(mapKey, jsonString);
            editor.commit();
        }
    }

    public Map<String, String> loadMap() {
        Map<String, String> outputMap = new HashMap<>();
        SharedPreferences pSharedPref = getApplicationContext().getSharedPreferences("MyVariables",
                Context.MODE_PRIVATE);
        try {
            if (pSharedPref != null) {
                String jsonString = pSharedPref.getString(mapKey, (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while (keysItr.hasNext()) {
                    String key = keysItr.next();
                    outputMap.put(key, jsonObject.get(key).toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputMap;
    }


    @Override
    public void OnSuccess(APIResponse response, String message) {


        if (response instanceof CheckLoginResponse) {
            if (response.getStatusNo() == 0) {


                //region clear parent data and add new user.
                SharedPreferences preferences = getSharedPreferences(Constants.SWITCh_ParentDeatils_FINMART, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();

                Map<String, String> inputMap = new HashMap<String, String>();
                inputMap.put("Parent_email", loginResponseEntity.getEmailID());
                inputMap.put("Parent_name", loginResponseEntity.getFullName());
                inputMap.put("Parent_UID", userConstantEntity.getUserid());
                inputMap.put("Parent_PWD", prefManager.getUserPassword());
                inputMap.put("Parent_Fbaid", String.valueOf(loginResponseEntity.getFBAId()));
                inputMap.put("Parent_POSPNo", userConstantEntity.getPOSPNo());


                inputMap.put("Child_email", pospAgentEntity.getEmailId());
                inputMap.put("Child_name", pospAgentEntity.getName());
                inputMap.put("Child_UID", "");
                inputMap.put("Child_Fbaid", pospAgentEntity.getFBAID());
                saveMap(inputMap);

                loginRequestEntity.setUserName(pospAgentEntity.getEmailId());
                loginRequestEntity.setPassword("");
                loginRequestEntity.setDeviceId("" + new ReadDeviceID(SwitchUserActivity.this).getAndroidID());
                loginRequestEntity.setTokenId(prefManager.getToken());
                loginRequestEntity.setIsChildLogin("Y");

                showDialog();
                new DBPersistanceController(this).clearSwitchUser();
                new LoginController(SwitchUserActivity.this).login(loginRequestEntity, SwitchUserActivity.this);

                //endregion

            } else {
                Toast.makeText(this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if (response instanceof LoginResponse) {

            cancelDialog();
            if (response.getStatusNo() == 0) {


                Intent intent = new Intent();
                intent.putExtra("POSP_USER", true);
                setResult(Constants.SWITCH_USER_REQUEST_CODE, intent);

                Toast.makeText(SwitchUserActivity.this, "User Switched Successfully...", Toast.LENGTH_SHORT).show();

                finish();

                // prefManager.setIsUserLogin(true);
            } else {
                Toast.makeText(this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if (response instanceof PospAgentResponse) {
            cancelDialog();
            if (response.getStatusNo() == 0) {

                switchUserLst = ((PospAgentResponse) response).getMasterData();
                mAdapter = new SwitchUserAdapter(this, switchUserLst);
                rvSwitchUser.setAdapter(mAdapter);


            } else {
                rvSwitchUser.setAdapter(null);
                Toast.makeText(this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void OnFailure(Throwable t) {
        cancelDialog();
        Toast.makeText(this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
    }


    private void setTextWatcher() {

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 1) {
                    if (mAdapter != null) {
                        mAdapter.getFilter().filter(s);
                    }

                } else {
                    if (mAdapter != null) {
                        mAdapter.findAll(switchUserLst);
                        etSearch.setCursorVisible(false);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getUserSwitchData(PospAgentEntity pospAgentEntity) {

        loginRequestEntity.setUserName(pospAgentEntity.getEmailId());
        loginRequestEntity.setPassword("");
        loginRequestEntity.setDeviceId("" + new ReadDeviceID(SwitchUserActivity.this).getAndroidID());
        loginRequestEntity.setTokenId(prefManager.getToken());
        loginRequestEntity.setIsChildLogin("Y");

        showDialog();
        new LoginController(SwitchUserActivity.this).checkLoginSwitchUser(loginRequestEntity, SwitchUserActivity.this);


    }

    public void redirectToSwitchUser(PospAgentEntity pospAgentEntity) {
        this.pospAgentEntity = pospAgentEntity;
        ConfirmSwitchUser(pospAgentEntity);
    }


    public void ConfirmSwitchUser(PospAgentEntity pospAgentEntity) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(SwitchUserActivity.this);
            builder.setTitle("Switch User");

            builder.setMessage("Do you want to swith your account on: " + pospAgentEntity.getName());
            String positiveText = "Submit";
            String NegativeText = "Cancel";
            builder.setPositiveButton(positiveText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getUserSwitchData(pospAgentEntity);

                        }
                    });

            builder.setNegativeButton(NegativeText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            final androidx.appcompat.app.AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } catch (Exception ex) {
            Toast.makeText(this, "Please try again..", Toast.LENGTH_SHORT).show();
        }
    }


}
