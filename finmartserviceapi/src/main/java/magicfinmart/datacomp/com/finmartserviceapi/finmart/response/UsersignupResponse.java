package magicfinmart.datacomp.com.finmartserviceapi.finmart.response;

import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.finmart.APIResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.UserHideEntity;


public class UsersignupResponse extends APIResponse {

	private UsersingupEntity MasterData;


	public UsersingupEntity getMasterData(){
		return MasterData;
	}

	public void setMasterData(UsersingupEntity MasterData) {
		this.MasterData = MasterData;
	}


	public static class UsersingupEntity {

		private String enableEliteSignupurl;
		private String enableProSignupurl;

		public String getEnableEliteSignupurl(){
			return enableEliteSignupurl;
		}

		public String getEnableProSignupurl(){
			return enableProSignupurl;
		}
	}
}