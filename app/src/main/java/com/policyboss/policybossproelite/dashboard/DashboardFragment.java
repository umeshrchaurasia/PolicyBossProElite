package com.policyboss.policybossproelite.dashboard;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.policyboss.policybossproelite.BaseFragment;
import com.policyboss.policybossproelite.BuildConfig;
import com.policyboss.policybossproelite.MyApplication;
import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.home.HomeActivity;
import com.policyboss.policybossproelite.knowledgeguru.KnowledgeGuruActivity;
import com.policyboss.policybossproelite.login.LoginActivity;
import com.policyboss.policybossproelite.pendingcases.PendingCasesActivity;
import com.policyboss.policybossproelite.salesmaterial.SalesMaterialActivity;
import com.policyboss.policybossproelite.utility.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.PrefManager;
import magicfinmart.datacomp.com.finmartserviceapi.Utility;
import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.database.UserBehaviourFacade;
import magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.DynamicController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.APIResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.IResponseSubcriber;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.controller.masters.MasterController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.ConstantEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.MenuMasterResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.ConstantsResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.UserConstatntResponse;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends BaseFragment implements View.OnClickListener, IResponseSubcriber, /*ILocationStateListener,*/ BaseFragment.PopUpListener {

    RecyclerView rvHome;
    DashboardRowAdapter mAdapter;
    BottomNavigationView navigation;
    PackageInfo pinfo;
    View view;
    ConstantEntity constantEntity;
    PrefManager prefManager;
    int forceUpdate;
    //LocationTracker locationTracker;
    //Location location;
    TextView tvKnowledge, tvPendingCAses, tvSalesMat;
    String LangType = "";


    WifiManager mainWifi;
    WifiReceiver receiverWifi;
    List<ScanResult> wifiList;
    ArrayList<String> wifiArrayList;
    DBPersistanceController db;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            getActivity().unregisterReceiver(receiverWifi);
        } catch (Exception e) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initialise(view);
        db = new DBPersistanceController(getActivity());


        setListener();
        receiverWifi = new WifiReceiver();
        wifiArrayList = new ArrayList<>();
        mainWifi = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        getActivity().registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mainWifi.startScan();


        registerPopUp(this);
        prefManager = new PrefManager(getActivity());
        LangType = prefManager.getLanguage();

     //   bindDashboardhAdapter();
        try {
            pinfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        if (!LangType.equals("")) {
            tvSalesMat.setText(db.getLangData(LangType, "SalesMaterial"));
            tvKnowledge.setText(db.getLangData(LangType, "KnowledgeGuru"));
            tvPendingCAses.setText(db.getLangData(LangType, "PendingCases"));

        }

        prefManager.setAppVersion("policyboss-elite-" + BuildConfig.VERSION_NAME);
        prefManager.setDeviceID( Utility.getDeviceId(this.getContext().getApplicationContext()));


        showDialog();
        new MasterController(getActivity()).geUserConstantSync(this);
        new MasterController(getActivity()).getMenuMaster(this);

        //send user behaviour



        return view;
    }

    class WifiReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
            try {
                wifiList = mainWifi.getScanResults();
                for (int i = 0; i < wifiList.size(); i++) {
                    wifiArrayList.add((wifiList.get(i)).toString());
                }
                new UserBehaviourFacade(getActivity()).saveWifi(wifiArrayList.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setListener() {
        tvKnowledge.setOnClickListener(this);
        tvPendingCAses.setOnClickListener(this);
        tvSalesMat.setOnClickListener(this);
    }

    private void initialise(View view) {

        tvKnowledge = (TextView) view.findViewById(R.id.tvKnowledge);
        tvPendingCAses = (TextView) view.findViewById(R.id.tvPendingCAses);
        tvSalesMat = (TextView) view.findViewById(R.id.tvSalesMat);

        rvHome = (RecyclerView) view.findViewById(R.id.rvHome);
        rvHome.setLayoutManager(new LinearLayoutManager(getActivity()));
        navigation = (BottomNavigationView) view.findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_sales:
                    //redirect to sales
                    startActivity(new Intent(getContext(), SalesMaterialActivity.class));
                  //  new TrackingController(getActivity()).sendData(new TrackingRequestEntity(new TrackingData("Sales Material : Sales Material From Dashboard "), Constants.SALES_MATERIAL), null);
                    return true;
                case R.id.nav_pending:
                    //redirect to pending status
                    startActivity(new Intent(getContext(), PendingCasesActivity.class));
                  //  new TrackingController(getActivity()).sendData(new TrackingRequestEntity(new TrackingData("Pending Cases : Pending Cases From Dashboard "), Constants.PENDING_CASES), null);
                    return true;
                case R.id.nav_knowledge:
                    //redirect to knowledge guru
                    startActivity(new Intent(getActivity(), KnowledgeGuruActivity.class));
                  //  new TrackingController(getActivity()).sendData(new TrackingRequestEntity(new TrackingData("Knowledge Guru : Knowledge Guru From Dashboard "), Constants.KNOWLEDGE_GURU), null);
                    return true;
            }
            return false;
        }
    };

    @Override
    public void OnSuccess(APIResponse response, String message) {
        cancelDialog();
        if (response instanceof ConstantsResponse) {
            constantEntity = ((ConstantsResponse) response).getMasterData();
            if (response.getStatusNo() == 0) {

                //region check for new vwesion
                int serverVersionCode = Integer.parseInt(((ConstantsResponse) response).getMasterData().getVersionCode());
                if (pinfo != null && pinfo.versionCode < serverVersionCode) {
                    forceUpdate = Integer.parseInt(((ConstantsResponse) response).getMasterData().getIsForceUpdate());
                    if (forceUpdate == 1) {
                        // forced update app
                        openPopUp(view, "UPDATE", "New version available on play store!!!! Please update.", "OK", false);
                    } else {
                        // aap with less version but not forced update
                        if (prefManager.getUpdateShown()) {
                            prefManager.setIsUpdateShown(false);
                            openPopUp(view, "UPDATE", "New version available on play store!!!! Please update.", "OK", true);
                        }
                    }
                } else if (((ConstantsResponse) response).getMasterData().
                        getMPSStatus().toLowerCase().equalsIgnoreCase("p")) {
                    if (getActivity() != null && prefManager.getMps() != null) {

                        //  ((HomeActivity) getActivity()).DialogMPS();
                    }
                }
                //endregion
                // if (getActivity() != null)
                //     ((HomeActivity) getActivity()).hideNavigationItem();
            }
        } else if (response instanceof UserConstatntResponse) {
            if (response.getStatusNo() == 0) {


//                if (LangType == "") {
//                    mAdapter = new DashboardRowAdapter(DashboardFragment.this);
//                    this.rvHome.setAdapter(mAdapter);
//                } else {
//                    mAdapter = new DashboardRowAdapter(DashboardFragment.this, LangType);
//                    this.rvHome.setAdapter(mAdapter);
//                }


            }
        } else if (response instanceof MenuMasterResponse) {
            if (response.getStatusNo() == 0) {

                ((HomeActivity)getActivity()).addDynamicMenu(((MenuMasterResponse) response));
                bindDashboardhAdapter();
            }
        }

    }

    @Override
    public void OnFailure(Throwable t) {
        cancelDialog();
    }

    @Override
    public void onPositiveButtonClick(Dialog dialog, View view) {
        dialog.cancel();
        openAppMarketPlace();
    }

    @Override
    public void onCancelButtonClick(Dialog dialog, View view) {

        if (forceUpdate == 1) {

        } else {
            dialog.cancel();
        }
    }

    private void openAppMarketPlace() {
        final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
       // new TrackingController(getActivity()).sendData(new TrackingRequestEntity(new TrackingData("Update : User open marketplace  "), "Update"), null);
    }

    /*@Override
    public void onLocationChanged(Location location) {
        location = locationTracker.mLocation;
    }

    @Override
    public void onConnected() {
        location = locationTracker.mLocation;
    }

    @Override
    public void onConnectionFailed() {
        location = null;
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvKnowledge:
                //redirect to knowledge guru
                startActivity(new Intent(getActivity(), KnowledgeGuruActivity.class));
               // new TrackingController(getActivity()).sendData(new TrackingRequestEntity(new TrackingData("Knowledge Guru : Knowledge Guru From Dashboard "), Constants.KNOWLEDGE_GURU), null);
                MyApplication.getInstance().trackEvent(Constants.KNOWLEDGE_GURU, "Clicked", "Knowledge Guru From Dashboard");
                break;
            case R.id.tvPendingCAses:
                //redirect to pending status
                startActivity(new Intent(getContext(), PendingCasesActivity.class));
               // new TrackingController(getActivity()).sendData(new TrackingRequestEntity(new TrackingData("Pending Cases : Pending Cases From Dashboard "), Constants.PENDING_CASES), null);
                MyApplication.getInstance().trackEvent(Constants.PENDING_CASES, "Clicked", "Pending Cases From Dashboard");
                break;
            case R.id.tvSalesMat:
                //redirect to sales
                startActivity(new Intent(getContext(), SalesMaterialActivity.class));
              //  new TrackingController(getActivity()).sendData(new TrackingRequestEntity(new TrackingData("Sales Material : Sales Material From Dashboard "), Constants.SALES_MATERIAL), null);
                MyApplication.getInstance().trackEvent(Constants.SALES_MATERIAL, "Clicked", "CUSTOMER COMM. From Dashboard");
                break;
        }
    }

    public void refreshAdapter() {

        if (!prefManager.getLanguage().isEmpty()) {

            tvSalesMat.setText(db.getLangData(prefManager.getLanguage(), "SalesMaterial"));
            tvKnowledge.setText(db.getLangData(prefManager.getLanguage(), "KnowledgeGuru"));
            tvPendingCAses.setText(db.getLangData(prefManager.getLanguage(), "PendingCases"));

        }
        if (mAdapter != null) {

            bindDashboardhAdapter();

        }

    }


    public void bindDashboardhAdapter() {


        mAdapter = new DashboardRowAdapter(this, 0, 1, 2);
        rvHome.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(mHandleMessageReceiver,
                        new IntentFilter(Utility.USER_DASHBOARD));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mHandleMessageReceiver);
    }

    //region broadcast receiver
    public BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction() != null) {
                if (intent.getAction().equalsIgnoreCase(Utility.USER_DASHBOARD)) {
                    //MenuMasterEntity menuMasterEntity = intent.getParcelableExtra("USER_DASHBOARD");
                    refreshAdapter();
                }
            }
        }
    };

    //endregion





}
