package com.savbill.energyaudit;

import net.sf.json.JSONObject;

public interface IEnergyAuditManagement {
	
	JSONObject getDTCMeterMasterDetails(JSONObject object);
	JSONObject upsertDTCMeterMasterDetails(JSONObject object);
	JSONObject getDTCMeterReadingDetails(JSONObject object);
	
	JSONObject getFeederEnergyAuditDetails(JSONObject object);
	JSONObject upsertFeederEnergyAuditDetails(JSONObject data);
	JSONObject getentryrecordsforassessed(JSONObject object);

}
