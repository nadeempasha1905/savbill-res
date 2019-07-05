package com.savbill.infrastructure;

import net.sf.json.JSONObject;

public interface IInfrastructureManagement {
	
	JSONObject getLocationDetails(JSONObject object);
	
	JSONObject getStationDetails(JSONObject object);
	JSONObject upsertStationDetails(JSONObject object);
	
	JSONObject getFeederDetails(JSONObject object);
	JSONObject upsertFeederDetails(JSONObject object);
	
	JSONObject getDTCDetails(JSONObject object);
	JSONObject upsertDTCDetails(JSONObject object);
	
	JSONObject getOMDetails(JSONObject object);
	JSONObject upsertOMDetails(JSONObject object);
	
	JSONObject getTownDetails(JSONObject object);
	JSONObject getDTCTownMapDetails(JSONObject object);
	JSONObject mapDTCTown(JSONObject data);
	
	JSONObject getDTCDetailsForTransfer(JSONObject object);
	JSONObject dtcTransfer(JSONObject data);

}
