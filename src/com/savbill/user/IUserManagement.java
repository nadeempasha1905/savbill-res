package com.savbill.user;

import net.sf.json.JSONObject;

public interface IUserManagement {
	
	JSONObject validateUserID(JSONObject object);
	JSONObject authenticateUser(JSONObject object, String ipAdress);
	JSONObject authenticateUserRefresh(JSONObject object);
	JSONObject getUserMenus(String userRole,String ConnType);
	JSONObject logUserSessionData(JSONObject object);
	JSONObject logOutSessionData(JSONObject object);
	
	JSONObject getUserRoles(JSONObject object);
	JSONObject upsertUserRoles(JSONObject object);
	
	JSONObject getUserMasterDetails(JSONObject object);
	JSONObject upsertUserMaster(JSONObject object);
	JSONObject deleteUserMaster(JSONObject object);
	JSONObject getUserSessionDetails(JSONObject object);
	JSONObject updateUserSessions(JSONObject data);
	JSONObject getUserDeligationDetails(JSONObject object);
	JSONObject upsertUserDeligation(JSONObject object);
}
