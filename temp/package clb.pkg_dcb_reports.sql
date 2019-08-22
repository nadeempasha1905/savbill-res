CREATE OR REPLACE
PACKAGE "PKG_DCB_REPORTS" IS
	--=============================================================================================
	--	WRITTEN BY				:	RAVI KUMAR
	---------------	MODIFICATION	HISTORY -------------------------------------------------------
	--	VERSION NO				:	1.00
	--	MODIFIED DATE			:   11-JUL-2019
	--	MODIFICATION PURPOSE 	:	FOR DCB AND DCB RELADETD REPORTS
	----------------------------------------------------------------------------------------------
	--===================================================================================
	--  PROCEDURES TO VALIDATE USER_ID
	--=================================================================================== 
	PROCEDURE GET_DCB(CUR OUT SYS_REFCURSOR, P_REPORT_TYPE IN VARCHAR2, P_LOCATION_CODE IN VARCHAR2, P_MONTH IN VARCHAR2, P_TARIFFS IN VARCHAR2, 
	                  P_STATION IN VARCHAR2, P_FEEDER IN VARCHAR2, P_TRANSFORMER IN VARCHAR2, P_MR IN VARCHAR2, P_GPS IN VARCHAR2, P_HEADER IN  BOOLEAN DEFAULT TRUE);
	
END PKG_DCB_REPORTS;

/