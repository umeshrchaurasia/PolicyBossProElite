package magicfinmart.datacomp.com.finmartserviceapi.finmart.response;

import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.finmart.APIResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.UserHideEntity;


public class UsersignupResponse extends APIResponse {

	private List<UsersingupEntity> MasterData;
	public List<UsersingupEntity> getMasterData() {
		return MasterData;
	}

	public void setMasterData(List<UsersingupEntity> masterData) {
		MasterData = masterData;
	}




	public static class UsersingupEntity {

		private String enable_pro_signupurl;
		private String enable_elite_signupurl;

		public String getEnableEliteSignupurl(){
			return enable_elite_signupurl;
		}

		public String getEnableProSignupurl(){
			return enable_elite_signupurl;
		}
	}
}