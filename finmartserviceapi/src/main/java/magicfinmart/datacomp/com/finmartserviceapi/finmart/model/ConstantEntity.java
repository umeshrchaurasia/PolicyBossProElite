package magicfinmart.datacomp.com.finmartserviceapi.finmart.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ConstantEntity extends RealmObject {

    /**
     * VersionCode : 0
     * IsForceUpdate : 0
     * PBNoOfHits : 5
     * PBHitTime : 6000
     * ROIHLBL : 8.30
     * <p>
     * ROIPLBL : 11.49
     * ROILABL : 8.75
     * POSPNo : 404
     * POSPStat : 1
     */
    @PrimaryKey
    private String VersionCode;
    private String IsForceUpdate;
    private String PBNoOfHits;
    private String PBHitTime;
    private String ROIHLBL;
    private String ROIPLBL;
    private String ROILABL;
    private String POSPNo;
    private String POSPStat;

    public String getVersionCode() {
        return VersionCode;
    }

    public void setVersionCode(String VersionCode) {
        this.VersionCode = VersionCode;
    }

    public String getIsForceUpdate() {
        return IsForceUpdate;
    }

    public void setIsForceUpdate(String IsForceUpdate) {
        this.IsForceUpdate = IsForceUpdate;
    }

    public String getPBNoOfHits() {
        return PBNoOfHits;
    }

    public void setPBNoOfHits(String PBNoOfHits) {
        this.PBNoOfHits = PBNoOfHits;
    }

    public String getPBHitTime() {
        return PBHitTime;
    }

    public void setPBHitTime(String PBHitTime) {
        this.PBHitTime = PBHitTime;
    }

    public String getROIHLBL() {
        return ROIHLBL;
    }

    public void setROIHLBL(String ROIHLBL) {
        this.ROIHLBL = ROIHLBL;
    }

    public String getROIPLBL() {
        return ROIPLBL;
    }

    public void setROIPLBL(String ROIPLBL) {
        this.ROIPLBL = ROIPLBL;
    }

    public String getROILABL() {
        return ROILABL;
    }

    public void setROILABL(String ROILABL) {
        this.ROILABL = ROILABL;
    }

    public String getPOSPNo() {
        return POSPNo;
    }

    public void setPOSPNo(String POSPNo) {
        this.POSPNo = POSPNo;
    }

    public String getPOSPStat() {
        return POSPStat;
    }

    public void setPOSPStat(String POSPStat) {
        this.POSPStat = POSPStat;
    }
}