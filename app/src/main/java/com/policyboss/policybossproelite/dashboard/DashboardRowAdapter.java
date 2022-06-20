package com.policyboss.policybossproelite.dashboard;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.policyboss.policybossproelite.BaseFragment;
import com.policyboss.policybossproelite.BuildConfig;
import com.policyboss.policybossproelite.MyApplication;
import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.creditcard.AppliedCreditListActivity;
import com.policyboss.policybossproelite.health.HealthQuoteAppActivity;
import com.policyboss.policybossproelite.healthcheckupplans.HealthCheckUpListActivity;
import com.policyboss.policybossproelite.home.HomeActivity;

import com.policyboss.policybossproelite.motor.privatecar.activity.PrivateCarDetailActivity;
import com.policyboss.policybossproelite.motor.twowheeler.activity.TwoWheelerQuoteAppActivity;
import com.policyboss.policybossproelite.ncd.NCDActivity;
import com.policyboss.policybossproelite.offline_quotes.AddNewOfflineQuotesActivity;
import com.policyboss.policybossproelite.quicklead.QuickLeadActivity;
import com.policyboss.policybossproelite.term.termselection.TermSelectionActivity;
import com.policyboss.policybossproelite.ultralaksha.ultra_selection.UltraLakshaSelectionActivity;
import com.policyboss.policybossproelite.utility.Constants;
import com.policyboss.policybossproelite.webviews.CommonWebViewActivity;

import java.util.List;
import java.util.Map;

import magicfinmart.datacomp.com.finmartserviceapi.PrefManager;
import magicfinmart.datacomp.com.finmartserviceapi.Utility;
import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.DashboardarrayEntity;
import magicfinmart.datacomp.com.finmartserviceapi.model.DashboardMultiLangEntity;


public class DashboardRowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //private static final int ROW_HEADER = 5;
    private int ROW_INSURANCE = 0;
    private int ROW_DISCLOSURE = 1;
    private int ROW_LOAN = 2;
    private int ROW_MORE_SERVICES = 3;
    private int TOTAL_ROW = 3;
    Fragment mFragment;
    DBPersistanceController mReal;
    Context mContext;
    String LangType;
    PrefManager prefManager;


    public DashboardRowAdapter(Fragment fragment, int InsurancePosition, int DisclosurePosition, int loanPosition) {
        mFragment = fragment;
        mContext = mFragment.getActivity();
        ROW_INSURANCE = InsurancePosition;
        ROW_DISCLOSURE = DisclosurePosition;
        ROW_LOAN = loanPosition;
        mReal = new DBPersistanceController(mFragment.getActivity());
        prefManager = new PrefManager(mFragment.getActivity());

    }


    public class HeaderRow extends RecyclerView.ViewHolder {
        public HeaderRow(View view) {
            super(view);
        }
    }

    public class InsuranceHolder extends RecyclerView.ViewHolder {
        RecyclerView rvDashboard;
        TextView txtTypeName, tvPoweredBy;
        ImageView ivLogo;


        public InsuranceHolder(View view) {
            super(view);
            rvDashboard = (RecyclerView) view.findViewById(R.id.rvDashboard);
            txtTypeName = (TextView) view.findViewById(R.id.txtTypeName);
            ivLogo = view.findViewById(R.id.ivLogo);
            tvPoweredBy = view.findViewById(R.id.tvPoweredBy);

        }
    }


    public class DisclosureHolder extends RecyclerView.ViewHolder {

        TextView txtDisclosure;
        LinearLayout lyDisclosure;


        public DisclosureHolder(View view) {
            super(view);
            txtDisclosure = (TextView) view.findViewById(R.id.txtDisclosure);
            lyDisclosure = (LinearLayout) view.findViewById(R.id.lyDisclosure);


        }
    }

    public class LoanHolder extends RecyclerView.ViewHolder {
        RecyclerView rvDashboard;
        TextView txtTypeName, tvPoweredBy;
        ImageView ivLogo;

        public LoanHolder(View view) {
            super(view);
            rvDashboard = (RecyclerView) view.findViewById(R.id.rvDashboard);
            txtTypeName = (TextView) view.findViewById(R.id.txtTypeName);
            ivLogo = view.findViewById(R.id.ivLogo);
            tvPoweredBy = view.findViewById(R.id.tvPoweredBy);
        }
    }

    public class MoreServiceHolder extends RecyclerView.ViewHolder {
        RecyclerView rvDashboard;
        TextView txtTypeName, tvPoweredBy;
        ImageView ivLogo;
        CardView card_view;

        public MoreServiceHolder(View view) {
            super(view);
            rvDashboard = (RecyclerView) view.findViewById(R.id.rvDashboard);
            txtTypeName = (TextView) view.findViewById(R.id.txtTypeName);
            ivLogo = view.findViewById(R.id.ivLogo);
            tvPoweredBy = view.findViewById(R.id.tvPoweredBy);
            card_view = view.findViewById(R.id.card_view);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
//            case ROW_HEADER:
//                view = LayoutInflater.from(parent.getContext()).inflate(
//                        R.layout.layout_dashboard_header, parent, false);
//                return new HeaderRow(view);

            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.layout_dashboard_recycler, parent, false);
                return new InsuranceHolder(view);

            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.layout_dashboard_disclosure, parent, false);
                return new DisclosureHolder(view);

            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.layout_dashboard_recycler, parent, false);
                return new LoanHolder(view);

           // case 3:
           //     view = LayoutInflater.from(parent.getContext()).inflate(
           //             R.layout.layout_dashboard_recycler, parent, false);
           //     return new MoreServiceHolder(view);

            default:
                break;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


        LangType = prefManager.getLanguage();

        if (holder instanceof HeaderRow) {

        } else if (holder instanceof InsuranceHolder) {

            // final List<DashboardEntity> listIns = mReal.getInsurProductList();
            final List<DashboardMultiLangEntity> listIns = mReal.getInsurProductLangList();

            if (LangType.equals("")) {
             //   ((InsuranceHolder) holder).txtTypeName.setText("INSURANCE");
                ((InsuranceHolder) holder).txtTypeName.setText("LANDMARK INSURANCE BROKERS PVT.LTD.\n(IRDAI CoR #216)");
            } else {
                ((InsuranceHolder) holder).txtTypeName.setText(mReal.getLangData(LangType, "Insurance"));
            }


            ((BaseFragment) mFragment).setLanguage(LangType, ((InsuranceHolder) holder).txtTypeName);

            ((InsuranceHolder) holder).ivLogo.setImageDrawable(mContext.getResources().getDrawable(R.drawable.logo_policyboss1));
            ((InsuranceHolder) holder).rvDashboard.setLayoutManager(new LinearLayoutManager(mFragment.getActivity()));
            ((InsuranceHolder) holder).rvDashboard.setAdapter(new DashboardItemAdapter(mFragment, listIns, LangType));


//            ((InsuranceHolder) holder).rvDashboard.addOnItemTouchListener(
//                    new RecyclerItemClickListener(((InsuranceHolder) holder).rvDashboard,
//                            new RecyclerItemClickListener.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(View view, int position) {
//                                    if (mReal.getUserConstantsData().getEnableInsuranceBusiness().equals("1")) {
//                                        switchMenus(listIns.get(position));
//                                    } else {
//                                        Toast.makeText(mContext, "Your not authorize to sell Insurance..", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            }));


        } else if (holder instanceof DisclosureHolder) {

            ((DisclosureHolder) holder).lyDisclosure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mContext.startActivity(new Intent(mContext, CommonWebViewActivity.class)
                            .putExtra("URL", "file:///android_asset/Disclosure.html")
                            .putExtra("NAME", "DISCLOSURE")
                            .putExtra("TITLE", "DISCLOSURE"));

                }
            });

        } else if (holder instanceof LoanHolder) {
            //   final List<DashboardEntity> listLoan = mReal.getLoanProductList();
//            if (mReal.isHideLoan()) {
//                ((LoanHolder) holder).txtTypeName.setVisibility(View.GONE);
//                ((LoanHolder) holder).ivLogo.setVisibility(View.GONE);
//                ((LoanHolder) holder).rvDashboard.setVisibility(View.GONE);
//            }
            final List<DashboardMultiLangEntity> listLoan = mReal.getLoanProductLangList();

            //if loan is not available show kotak label
            //else as it is

            if (mReal.isHideLoan()) {
                ((LoanHolder) holder).txtTypeName.setVisibility(View.GONE);
                ((LoanHolder) holder).ivLogo.setVisibility(View.GONE);
                ((LoanHolder) holder).tvPoweredBy.setVisibility(View.GONE);
            } else {
                if (LangType.equals("")) {
                    ((LoanHolder) holder).txtTypeName.setText("LOANS");
                } else {
                    ((LoanHolder) holder).txtTypeName.setText(mReal.getLangData(LangType, "Loans"));
                }
            }


            ((BaseFragment) mFragment).setLanguage(LangType, ((LoanHolder) holder).txtTypeName);

            ((LoanHolder) holder).ivLogo.setImageDrawable(mContext.getResources().getDrawable(R.drawable.logo_rupeeboss1));
            ((LoanHolder) holder).rvDashboard.setLayoutManager(new LinearLayoutManager(mFragment.getActivity()));
            ((LoanHolder) holder).rvDashboard.setAdapter(new DashboardItemAdapter(mFragment, listLoan, LangType));

//            ((LoanHolder) holder).rvDashboard.addOnItemTouchListener(
//                    new RecyclerItemClickListener(((LoanHolder) holder).rvDashboard,
//                            new RecyclerItemClickListener.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(View view, int position) {
//                                    switchMenus(listLoan.get(position));
//                                }
//                            }));

            ((LoanHolder) holder).txtTypeName.setVisibility(View.GONE);
            ((LoanHolder) holder).ivLogo.setVisibility(View.GONE);
            ((LoanHolder) holder).tvPoweredBy.setVisibility(View.GONE);
            ((LoanHolder) holder).rvDashboard.setVisibility(View.GONE);


        }

        /*
        else if (holder instanceof MoreServiceHolder) {

            //   final List<DashboardEntity> listMore = mReal.getMoreProductList();
            final List<DashboardMultiLangEntity> listMore = mReal.getMoreProductLangList();

            if (LangType.equals("")) {
                ((MoreServiceHolder) holder).txtTypeName.setText("MORE SERVICES");
            } else {
                ((MoreServiceHolder) holder).txtTypeName.setText(mReal.getLangData(LangType, "MoreServices"));
            }
            ((BaseFragment) mFragment).setLanguage(LangType, ((MoreServiceHolder) holder).txtTypeName);

            ((MoreServiceHolder) holder).tvPoweredBy.setVisibility(View.GONE);
            ((MoreServiceHolder) holder).ivLogo.setVisibility(View.GONE);
            ((MoreServiceHolder) holder).rvDashboard.setLayoutManager(new LinearLayoutManager(mFragment.getActivity()));
            ((MoreServiceHolder) holder).rvDashboard.setAdapter(new DashboardItemAdapter(mFragment, listMore, LangType));


        } */


    }

    // not in used...
    private void switchMenus(DashboardMultiLangEntity dashboardEntity) {
        int productID = dashboardEntity.getProductId();


        //fetching parent ss_id in case of switch user
        Map<String, String> map = ((HomeActivity) mContext).loadMap();
        String parent_ssid = "";
        if (map.size() > 0) {
            parent_ssid = map.get("Parent_POSPNo");
        }


        switch (productID) {

            case 1:

                //car
                if (mReal.getUserConstantsData().getFourWheelerEnabled().equalsIgnoreCase("1")) {
                    mContext.startActivity(new Intent(mContext, PrivateCarDetailActivity.class));
                } else {

                    String motorUrl = mReal.getUserConstantsData().getFourWheelerUrl();

                    String ipaddress = "0.0.0.0";
                    try {
                        ipaddress = Utility.getMacAddress(mContext);
                    } catch (Exception io) {
                        ipaddress = "0.0.0.0";
                    }


                    //&ip_address=10.0.3.64&mac_address=10.0.3.64&app_version=2.2.0&product_id=1
                    String append = "&ip_address=" + ipaddress + "&mac_address=" + ipaddress
                            + "&app_version=policyboss-" + BuildConfig.VERSION_NAME
                            + "&device_id=" + Utility.getDeviceId(mContext)
                            + "&product_id=1&login_ssid=" + parent_ssid;
                    motorUrl = motorUrl + append;

                    mContext.startActivity(new Intent(mContext, CommonWebViewActivity.class)
                            .putExtra("URL", motorUrl)
                            .putExtra("NAME", "Motor Insurance")
                            .putExtra("TITLE", "Motor Insurance"));

                }

                //mContext.startActivity(new Intent(mContext, PrivateCarDetailActivity.class));
               // new TrackingController(mContext).sendData(new TrackingRequestEntity(new TrackingData("Motor insurance tab on home page"), Constants.PRIVATE_CAR), null);
                MyApplication.getInstance().trackEvent(Constants.PRIVATE_CAR, "Clicked", "Motor insurance tab on home page");
                break;


            case 24:
                //fin peace
                mContext.startActivity(new Intent(mContext, CommonWebViewActivity.class)
                        .putExtra("URL", "https://10oqcnw.finpeace.ind.in/app#/"
                                + mReal.getUserData().getFBAId())
                        .putExtra("NAME", "FIN-PEACE")
                        .putExtra("TITLE", "FIN-PEACE"));
               // new TrackingController(mContext).sendData(new TrackingRequestEntity(new TrackingData("Fin Peace tab on home page"), Constants.FIN_PEACE), null);
                MyApplication.getInstance().trackEvent(Constants.FIN_PEACE, "Clicked", "Fin Peace tab on home page");
                break;
            case 2:
                //health

                // mContext.startActivity(new Intent(mContext, HealthQuoteAppActivity.class));


                if (mReal.getConstantsData().getHealthappenable().equalsIgnoreCase("1")) {
                    mContext.startActivity(new Intent(mContext, HealthQuoteAppActivity.class));
                } else {

                    String healthUrl = mReal.getUserConstantsData().getHealthurl();
                    //String healthUrl = new DBPersistanceController(mContext).getUserConstantsData().getHealthurltemp();

                    String ipaddress = "0.0.0.0";
                    try {
                        ipaddress = Utility.getMacAddress(mContext);
                    } catch (Exception io) {
                        ipaddress = "0.0.0.0";
                    }

                    String append = "&ip_address=" + ipaddress
                            + "&app_version=policyboss-" + Utility.getVersionName(mContext)
                            + "&device_id=" + Utility.getDeviceId(mContext) + "&login_ssid=" + parent_ssid;

                    healthUrl = healthUrl + append;

                    if (mReal.getConstantsData().getHealthThrowBrowser() != null &&
                            mReal.getConstantsData().getHealthThrowBrowser().equalsIgnoreCase("1")) {
                        Utility.loadWebViewUrlInBrowser(mContext, healthUrl);
                    } else {
                        mContext.startActivity(new Intent(mContext, CommonWebViewActivity.class)
                                .putExtra("URL", healthUrl)
                                .putExtra("NAME", "Health Insurance")
                                .putExtra("TITLE", "Health Insurance"));
                    }
                }


              //  new TrackingController(mContext).sendData(new TrackingRequestEntity(new TrackingData("Health insurance tab on home page"), Constants.HEALTH_INS), null);
                MyApplication.getInstance().trackEvent(Constants.HEALTH_INS, "Clicked", "Health insurance tab on home page");
                break;
            case 7:
                //home loan
            //    mContext.startActivity(new Intent(mContext, NewHomeApplicaionActivity.class));
             //   new TrackingController(mContext).sendData(new TrackingRequestEntity(new TrackingData("Home Loan tab on home page"), Constants.HOME_LOAN), null);
            //    MyApplication.getInstance().trackEvent(Constants.HOME_LOAN, "Clicked", "Home Loan tab on home page");
                break;
            case 19:
                //personal loan
           //
               // new TrackingController(mContext).sendData(new TrackingRequestEntity(new TrackingData("Personal loan tab on home page"), Constants.PERSONA_LOAN), null);
          //      MyApplication.getInstance().trackEvent(Constants.PERSONA_LOAN, "Clicked", "Personal loan tab on home page");
                break;
            case 8:
                //lap
            //    mContext.startActivity(new Intent(mContext, NewLAPApplicaionActivity.class));
              //  new TrackingController(mContext).sendData(new TrackingRequestEntity(new TrackingData("LAP tab on home page"), Constants.LAP), null);
          //      MyApplication.getInstance().trackEvent(Constants.LAP, "Clicked", "LAP tab on home page");
                break;
            case 4:
                //cc
                // mContext.startActivity(new Intent(mContext, CreditCardMainActivity.class));
                mContext.startActivity(new Intent(mContext, AppliedCreditListActivity.class));
               // new TrackingController(mContext).sendData(new TrackingRequestEntity(new TrackingData("Credit Card tab on home page"), Constants.CREDIT_CARD), null);
                MyApplication.getInstance().trackEvent(Constants.CREDIT_CARD, "Clicked", "Credit Card tab on home page");
                break;
            case 6:
                //BT
                //  mContext.startActivity(new Intent(mContext, BalanceTransferDetailActivity.class));
           //     mContext.startActivity(new Intent(mContext, NewbusinessApplicaionActivity.class));
              //  new TrackingController(mContext).sendData(new TrackingRequestEntity(new TrackingData("Business tab on home page"), Constants.BUSINESS_LOAN), null);
            //    MyApplication.getInstance().trackEvent(Constants.BUSINESS_LOAN, "Clicked", "Business tab on home page");
                break;
            case 9:

                mContext.startActivity(new Intent(mContext, QuickLeadActivity.class));
               // new TrackingController(mContext).sendData(new TrackingRequestEntity(new TrackingData("Quick Lead tab on home page"), Constants.QUICK_LEAD), null);
                MyApplication.getInstance().trackEvent(Constants.QUICK_LEAD, "Clicked", "Quick Lead tab on home page");
                break;

            case 10:
                //bike

                if (mReal.getUserConstantsData().getTwoWheelerEnabled().equalsIgnoreCase("1")) {
                    mContext.startActivity(new Intent(mContext, TwoWheelerQuoteAppActivity.class));
                } else {

                    String motorUrl = mReal.getUserConstantsData().getTwoWheelerUrl();

                    String ipaddress = "0.0.0.0";
                    try {
                        ipaddress = Utility.getMacAddress(mContext);
                    } catch (Exception io) {
                        ipaddress = "0.0.0.0";
                    }

                    String append = "&ip_address=" + ipaddress + "&mac_address=" + ipaddress
                            + "&app_version=policyboss-" + BuildConfig.VERSION_NAME
                            + "&device_id=" + Utility.getDeviceId(mContext)
                            + "&product_id=10&login_ssid=" + parent_ssid;
                    motorUrl = motorUrl + append;
                    mContext.startActivity(new Intent(mContext, CommonWebViewActivity.class)
                            .putExtra("URL", motorUrl)
                            .putExtra("NAME", "Two Wheeler Insurance")
                            .putExtra("TITLE", "Two Wheeler Insurance"));

                }


                //Toast.makeText(mContext.getContext(), "WIP.", Toast.LENGTH_SHORT).show();
                //mContext.startActivity(new Intent(mContext, TwoWheelerQuoteAppActivity.class));
              //  new TrackingController(mContext).sendData(new TrackingRequestEntity(new TrackingData("Two Wheeler tab on home page"), Constants.TWO_WHEELER), null);
                MyApplication.getInstance().trackEvent(Constants.TWO_WHEELER, "Clicked", "Two Wheeler tab on home page");
                break;


            case 11:
                //health check up
                mContext.startActivity(new Intent(mContext, HealthCheckUpListActivity.class));
               // new TrackingController(mContext).sendData(new TrackingRequestEntity(new TrackingData("Health CheckUp"), Constants.HEALTH_CHECKUP), null);
                MyApplication.getInstance().trackEvent(Constants.HEALTH_CHECKUP, "Clicked", "Health CheckUp tab on home page");
                break;

            case 12:
                //Commercial vehicle

                String cvUrl = mReal.getUserConstantsData().getCVUrl();

                String ipaddress = "0.0.0.0";
                try {
                    ipaddress = Utility.getMacAddress(mContext);
                } catch (Exception io) {
                    ipaddress = "0.0.0.0";
                }

                String append = "&ip_address=" + ipaddress + "&mac_address="
                        + "&app_version=policyboss-" + BuildConfig.VERSION_NAME
                        + "&device_id=" + Utility.getDeviceId(mContext)
                        + "&product_id=12&login_ssid=" + parent_ssid;
                cvUrl = cvUrl + append;
                mContext.startActivity(new Intent(mContext, CommonWebViewActivity.class)
                        .putExtra("URL", cvUrl)
                        .putExtra("NAME", "Commercial Vehicle Insurance")
                        .putExtra("TITLE", "Commercial Vehicle Insurance"));


                MyApplication.getInstance().trackEvent(Constants.CV, "Clicked", "Health CheckUp tab on home page");
                break;

            case 13:
                Utility.loadWebViewUrlInBrowser(mContext,
                        "http://www.rupeeboss.com/equifax-finmart?fbaid="
                                + String.valueOf(mReal.getUserData().getFBAId()));
                break;


            case 14:
                Utility.loadWebViewUrlInBrowser(mContext,
                        "https://yesbankbot.buildquickbots.com/chat/rupeeboss/staff/?userid=" + String.valueOf(mReal.getUserData().getFBAId()) + "&usertype=FBA&vkey=b34f02e9-8f1c");
               /* mContext.startActivity(new Intent(mContext, CommonWebViewActivity.class)
                        .putExtra(                break;
"URL", "https://yesbankbot.buildquickbots.com/chat/rupeeboss/staff/?userid=" + String.valueOf(mReal.getUserData().getFBAId()) + "&usertype=FBA&vkey=b34f02e9-8f1c")
                        .putExtra("NAME", "" + "Loan On Messenger")
                        .putExtra("TITLE", "" + "Loan On Messenger"));*/

            case 15: //ncd
                //car
                mContext.startActivity(new Intent(mContext, NCDActivity.class));
              //  new TrackingController(mContext).sendData(new TrackingRequestEntity(new TrackingData("Campaign "), Constants.CAMPAIGN), null);
                MyApplication.getInstance().trackEvent(Constants.CAMPAIGN, "Clicked", "CAMPAIGN");

                break;
            case 16:
                mContext.startActivity(new Intent(mContext, AddNewOfflineQuotesActivity.class));
              //  new TrackingController(mContext).sendData(new TrackingRequestEntity(new TrackingData("Offline quote "), Constants.CAMPAIGN), null);
                MyApplication.getInstance().trackEvent(Constants.OFFLINE, "Clicked", "OFFLINE");
                break;

            case 49: // added by Nilesh 13.02.2019 -- Ultra laksh
                mContext.startActivity(new Intent(mContext, UltraLakshaSelectionActivity.class));
               // new TrackingController(mContext).sendData(new TrackingRequestEntity(new TrackingData("Ultra lakshya"), Constants.CAMPAIGN), null);
                MyApplication.getInstance().trackEvent(Constants.ULTRA_LAKSHA, "Clicked", "ULTRA_LAKSHYA");
                break;

            case 18:
                //Life Insurance
                mContext.startActivity(new Intent(mContext, TermSelectionActivity.class));
              //  new TrackingController(mContext).sendData(new TrackingRequestEntity(new TrackingData("Life insurance tab on home page"), Constants.LIFE_INS), null);
                MyApplication.getInstance().trackEvent(Constants.LIFE_INS, "Clicked", "Life insurance tab on home page");
                break;

            case 5: //investment

                if (mReal.getUserConstantsData().getInvestmentEnabled().equals("1")) {
                    String invUrl = mReal.getUserConstantsData().getInvestmentUrl();

                    try {
                        ipaddress = Utility.getMacAddress(mContext);
                    } catch (Exception io) {
                        ipaddress = "0.0.0.0";
                    }

                    append = "&ip_address=" + ipaddress
                            + "&app_version=policyboss-" + Utility.getVersionName(mContext)
                            + "&device_id=" + Utility.getDeviceId(mContext) + "&login_ssid=" + parent_ssid;

                    invUrl = invUrl + append;

                    if (mReal.getConstantsData().getHealthThrowBrowser() != null &&
                            mReal.getConstantsData().getHealthThrowBrowser().equalsIgnoreCase("1")) {
                        Utility.loadWebViewUrlInBrowser(mContext, invUrl);
                    } else {
                        mContext.startActivity(new Intent(mContext, CommonWebViewActivity.class)
                                .putExtra("URL", invUrl)
                                .putExtra("NAME", "INVESTMENT PLANS")
                                .putExtra("TITLE", "INVESTMENT PLANS"));
                    }
                } else {
                    Toast.makeText(mContext, "You'r not authorize to sell Investment.", Toast.LENGTH_SHORT).show();
                }
                break;

        }

        if (productID < 100) {
            if (dashboardEntity.getIsNewprdClickable() != null) {
                if (dashboardEntity.getIsNewprdClickable().equals("Y")) {
                    //   region Getting Dynamic Product and Clickable action Using UserConstatnt Data

                    String dynamicUrl = "";
                    for (DashboardarrayEntity entity : mReal.getUserConstantsData().getDashboardarray()) {

                        if (Integer.valueOf(entity.getProdId()) == productID) {

                            dynamicUrl = entity.getUrl();
                            break;

                        }

                    }


                    if (!dynamicUrl.isEmpty()) {
                        String ipaddress = "0.0.0.0";
                        try {
                            ipaddress = Utility.getMacAddress(mContext);
                        } catch (Exception io) {
                            ipaddress = "0.0.0.0";
                        }


                        //&ip_address=10.0.3.64&mac_address=10.0.3.64&app_version=2.2.0&product_id=1
                        String append = "&ip_address=" + ipaddress + "&mac_address=" + ipaddress
                                + "&app_version=policyboss-" + BuildConfig.VERSION_NAME
                                + "&device_id=" + Utility.getDeviceId(mContext)
                                + "&product_id=" + productID + "&login_ssid=" + parent_ssid;
                        dynamicUrl = dynamicUrl + append;

                        mContext.startActivity(new Intent(mContext, CommonWebViewActivity.class)
                                .putExtra("URL", dynamicUrl)
                                .putExtra("NAME", dashboardEntity.getProductName())
                                .putExtra("TITLE", dashboardEntity.getProductName()));

                    }

                    //endregion
                }
            }
        } else if (productID >= 100) {
            mContext.startActivity(new Intent(mContext, CommonWebViewActivity.class)
                    .putExtra("URL", "" + dashboardEntity.getLink())
                    .putExtra("NAME", "" + dashboardEntity.getProductName())
                    .putExtra("TITLE", "" + dashboardEntity.getProductName()));
        }
    }

    @Override
    public int getItemCount() {
        return TOTAL_ROW;
    }

    @Override
    public int getItemViewType(int position) {

        switch (position) {
            //  case ROW_HEADER:
            //      return ROW_HEADER;
            case 0:
                return ROW_INSURANCE;
            case 1:
                return ROW_DISCLOSURE;
            case 2:
                return ROW_LOAN;
          //  case 3:
              //  return c;
            default:
                break;
        }
        return position;
    }


}