package magicfinmart.datacomp.com.finmartserviceapi.loan_fm.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by IN-RB on 08-03-2018.
 */

public class BuyLoanQuerystring implements Parcelable {

    private int BankId;
    private int Quote_id;
    private String Prop_Loan_Eligible;
    private String Prop_Processing_Fee;
    private String Prop_type;

    private String MobileNo;
    private String City;

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }



    public int getBankId() {
        return BankId;
    }

    public void setBankId(int bankId) {
        BankId = bankId;
    }

    public int getQuote_id() {
        return Quote_id;
    }

    public void setQuote_id(int quote_id) {
        Quote_id = quote_id;
    }

    public String getProp_Loan_Eligible() {
        return Prop_Loan_Eligible;
    }

    public void setProp_Loan_Eligible(String prop_Loan_Eligible) {
        Prop_Loan_Eligible = prop_Loan_Eligible;
    }



    public String getProp_Processing_Fee() {
        return Prop_Processing_Fee;
    }

    public void setProp_Processing_Fee(String prop_Processing_Fee) {
        Prop_Processing_Fee = prop_Processing_Fee;
    }


    public String getProp_type() {
        return Prop_type;
    }

    public void setProp_type(String prop_type) {
        Prop_type = prop_type;
    }

    protected BuyLoanQuerystring(Parcel in) {
        BankId = in.readInt();
        Quote_id = in.readInt();
        Prop_Loan_Eligible = in.readString();

        Prop_Processing_Fee = in.readString();

        Prop_type = in.readString();
    }

    public static final Creator<BuyLoanQuerystring> CREATOR = new Creator<BuyLoanQuerystring>() {
        @Override
        public BuyLoanQuerystring createFromParcel(Parcel in) {
            return new BuyLoanQuerystring(in);
        }

        @Override
        public BuyLoanQuerystring[] newArray(int size) {
            return new BuyLoanQuerystring[size];
        }
    };

    public BuyLoanQuerystring() {
        BankId = 0;
        Quote_id = 0;
        Prop_Loan_Eligible = "";

        Prop_Processing_Fee = "";

        Prop_type = "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(BankId);
        dest.writeInt(Quote_id);
        dest.writeString(Prop_Loan_Eligible);

        dest.writeString(Prop_Processing_Fee);

        dest.writeString(Prop_type);
    }
}