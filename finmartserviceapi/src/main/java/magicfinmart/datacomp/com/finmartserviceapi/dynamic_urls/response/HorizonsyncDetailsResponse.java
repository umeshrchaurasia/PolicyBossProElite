package magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.response;

import magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.requestentity.POSPHorizonEnity;
import magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.requestentity.SyncContactEntity;


import magicfinmart.datacomp.com.finmartserviceapi.finmart.APIResponse;
public class  HorizonsyncDetailsResponse  extends APIResponse {

    private String user_type;
    private String product;
    private String status;
    private String channel;

    private SyncContactEntity SYNC_CONTACT;
    private POSPHorizonEnity POSP;

    public String getUserType() { return user_type; }
    public void setUserType(String value) { this.user_type = value; }

    public String getProduct() { return product; }
    public void setProduct(String value) { this.product = value; }

    public String getStatus() { return status; }
    public void setStatus(String value) { this.status = value; }

    public String getChannel() { return channel; }
    public void setChannel(String value) { this.channel = value; }





    public SyncContactEntity getResult() {
        return SYNC_CONTACT;
    }

    public void setResult(SyncContactEntity syncContact_result) {
        this.SYNC_CONTACT = syncContact_result;
    }

    public POSPHorizonEnity getPOSP() {
        return POSP;
    }

    public void setPOSP(POSPHorizonEnity POSP) {
        this.POSP = POSP;
    }

}


