package magicfinmart.datacomp.com.finmartserviceapi.finmart.controller.login;

import magicfinmart.datacomp.com.finmartserviceapi.finmart.IResponseSubcriber;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.requestentity.LoginRequestEntity;

/**
 * Created by Rajeev Ranjan on 25/01/2018.
 */

public interface ILogin {
    void login(LoginRequestEntity loginRequestEntity, IResponseSubcriber iResponseSubcriber);
}