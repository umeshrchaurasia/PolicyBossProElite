package magicfinmart.datacomp.com.finmartserviceapi.loan_fm.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public  class PersonalQuoteDisplayEntity implements Parcelable {
        /**
         * ID : 1694
         * ApplicantNme : pratik
         * LoanRequired : 19000000
         * ApplicantIncome : 100000
         * Turnover : 1000000
         * status : Failure
         * ProductId : 7
         * url : http://beta.erp.rupeeboss.com/homeloan/home_loan_application_form.aspx?qoutid=1694
         */

        private int ID;
        private String ApplicantNme;
        private String LoanRequired;
        private String ApplicantIncome;
        private String Turnover;
        @SerializedName("status")
        private String statusX;
        private int ProductId;
        private String url;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getApplicantNme() {
            return ApplicantNme;
        }

        public void setApplicantNme(String ApplicantNme) {
            this.ApplicantNme = ApplicantNme;
        }

        public String getLoanRequired() {
            return LoanRequired;
        }

        public void setLoanRequired(String LoanRequired) {
            this.LoanRequired = LoanRequired;
        }

        public String getApplicantIncome() {
            return ApplicantIncome;
        }

        public void setApplicantIncome(String ApplicantIncome) {
            this.ApplicantIncome = ApplicantIncome;
        }

        public String getTurnover() {
            return Turnover;
        }

        public void setTurnover(String Turnover) {
            this.Turnover = Turnover;
        }

        public String getStatusX() {
            return statusX;
        }

        public void setStatusX(String statusX) {
            this.statusX = statusX;
        }

        public int getProductId() {
            return ProductId;
        }

        public void setProductId(int ProductId) {
            this.ProductId = ProductId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(this.ID);
        dest.writeString(this.ApplicantNme);
        dest.writeString(this.LoanRequired);
        dest.writeString(this.ApplicantIncome);
        dest.writeString(this.Turnover);
        dest.writeString(this.statusX);
        dest.writeInt(this.ProductId);
        dest.writeString(this.url);
    }

    protected PersonalQuoteDisplayEntity(Parcel in){
        this.ID = in.readInt();
        this.ApplicantNme = in.readString();
        this.LoanRequired = in.readString();
        this.ApplicantIncome = in.readString();
        this.Turnover = in.readString();
        this.statusX = in.readString();
        this.ProductId = in.readInt();
        this.url = in.readString();
    }

  public  static final Creator<PersonalQuoteDisplayEntity> CREATOR = new Creator<PersonalQuoteDisplayEntity>() {
      @Override
      public PersonalQuoteDisplayEntity createFromParcel(Parcel source) {
          return new PersonalQuoteDisplayEntity(source);
      }

      @Override
      public PersonalQuoteDisplayEntity[] newArray(int size) {
          return new PersonalQuoteDisplayEntity[size];
      }
  };

}