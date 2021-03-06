CREATE OR REPLACE
PACKAGE BODY "PKG_DCB_REPORTS" AS
--=============================================================================================
--	WRITTEN BY				:	RAVI KUMAR
---------------	MODIFICATION	HISTORY -------------------------------------------------------
--	VERSION NO				:	1.00
--	MODIFIED DATE			:   11-JUL-2019
--	MODIFICATION PURPOSE 	:	FOR DCB AND DCB TRELATED REPORTS
----------------------------------------------------------------------------------------------
--===================================================================================

	--===================================================================================
	--  PROCEDURES TO GET DCB
	--=================================================================================== 
	PROCEDURE GET_DCB(CUR OUT SYS_REFCURSOR, P_REPORT_TYPE IN VARCHAR2, P_LOCATION_CODE IN VARCHAR2, P_MONTH IN VARCHAR2, P_TARIFFS IN VARCHAR2, 
	                  P_STATION IN VARCHAR2, P_FEEDER IN VARCHAR2, P_TRANSFORMER IN VARCHAR2, P_MR IN VARCHAR2, P_GPS IN VARCHAR2, P_HEADER IN  BOOLEAN DEFAULT TRUE) IS
	
	HT     NUMBER;
	STR    VARCHAR2(4000); 
	P_TRFS VARCHAR2(2000);
	
	BEGIN    
	
		IF LENGTH(NVL(P_LOCATION_CODE,0)) <=0 THEN
		
			OPEN CUR FOR SELECT 'Invalid Location Code' as RESP from dual;
			
		ELSIF LENGTH(NVL(P_MONTH,0)) <=0 THEN
		
			OPEN CUR FOR SELECT 'Invalid Month' as RESP from dual;
			
		ELSE 
		
			P_TRFS := REPLACE(P_TARIFFS,'''','');	
		
			IF P_REPORT_TYPE = 'MAIN_DCB' THEN 
		
				SELECT COUNT(*) INTO HT FROM  MONTHLY_LEDGER, DCB_TRF_DESR WHERE  ML_OM_CD LIKE P_LOCATION_CODE||'%' AND ML_MONTH_YEAR  = TO_DATE(P_MONTH,'Mon-yyyy') AND ML_TRF_DESCR(+)=TM_SDESCR AND TRFGRP=2;
						
				    STR:='	WITH T AS 
				    		(SELECT TRFGRP, TRFORDER, ACCCD_DMD, ACCCD_BAL, VC,TM_LDESCR TRF_DESC,TM_SDESCR TRF,SUM(ML_LOAD_KW) KW,SUM(ML_LOAD_HP) HP,SUM(ML_LOAD_KVA) KVA,
							SUM(ML_INST_CNT) TOT_INSTS,SUM(ML_ACTIVE_INST) ACTIVE_INSTS,SUM(ML_ACTIVE_INST) TOBE_BILLED,SUM(ML_BILLED_INST) BILLED_INSTS,
							SUM(ML_MTRD_UNITS) MTR_UNITS,SUM(ML_ASSD_CONSMP) ASSD_UNITS,SUM(ML_WHEELED_ENRGY) WHEELED_UNITS,SUM(ML_MTRD_UNITS)+SUM(ML_ASSD_CONSMP)+SUM(ML_WHEELED_ENRGY) CONSMPTOT,
							SUM(ML_REVENUE_OB) REV_OB,SUM(ML_INTEREST_OB) INT_OB,SUM(ML_TAX_OB) TAX_OB,SUM(ML_REVENUE_OB+ML_INTEREST_OB+ML_TAX_OB) OBTOT,
							SUM(ML_REVENUE_DMD+ML_DIFF_REV) REV_DMD,SUM(ML_INTEREST_DMD+ML_DIFF_INT) INT_DMD,SUM(ML_TAX_DMD+ML_DIFF_TAX) TAX_DMD,
							SUM(ML_REVENUE_DMD+ML_DIFF_REV+ML_INTEREST_DMD+ML_DIFF_INT+ML_TAX_DMD+ML_DIFF_TAX) DMDTOT,
							SUM(ML_REVENUE_OB+ML_REVENUE_DMD) OBDMDREV,SUM(ML_INTEREST_OB+ML_INTEREST_DMD) OBDMDINT,SUM(ML_TAX_OB+ML_TAX_DMD) OBDMDTAX,
							SUM(ML_REVENUE_OB+ML_INTEREST_OB+ML_TAX_OB+ML_REVENUE_DMD+ML_INTEREST_DMD+ML_TAX_DMD) OBDMDTOT,
							SUM(ML_CCCOLL_REVENUE) CCREV_COLL,SUM(ML_ADJCOLL_REVENUE) ADJREV_COLL, SUM(ML_CCCOLL_REVENUE+ML_ADJCOLL_REVENUE) COLLREVTOT,SUM(ML_CCCOLL_INTEREST) CCINT_COLL,
							SUM(ML_ADJCOLL_INTEREST) ADJINT_COLL, SUM(ML_CCCOLL_INTEREST+ML_ADJCOLL_INTEREST) COLLINTTOT,
							SUM(ML_CCCOLL_TAX) CCTAX_COLL,SUM(ML_ADJCOLL_TAX) ADJTAX_COLL, SUM(ML_CCCOLL_TAX+ML_ADJCOLL_TAX) COLLTAXTOT,
							SUM(ML_CCCOLL_REVENUE+ML_CCCOLL_INTEREST+ML_CCCOLL_TAX+ML_ADJCOLL_REVENUE+ML_ADJCOLL_INTEREST+ML_ADJCOLL_TAX) COLLTOT,
							SUM(ML_REVENUE_OB+ML_REVENUE_DMD-ML_CCCOLL_REVENUE-ML_ADJCOLL_REVENUE+ML_DIFF_REV) REV_CB,SUM(ML_INTEREST_OB+ML_INTEREST_DMD-ML_CCCOLL_INTEREST-ML_ADJCOLL_REVENUE+ML_DIFF_REV) INT_CB,
							SUM(ML_TAX_OB+ML_TAX_DMD-ML_CCCOLL_TAX-ML_ADJCOLL_TAX+ML_DIFF_TAX) TAX_CB,SUM(ML_REVENUE_OB+ML_REVENUE_DMD-ML_CCCOLL_REVENUE-ML_ADJCOLL_REVENUE+ML_DIFF_REV) +
							SUM(ML_INTEREST_OB+ML_INTEREST_DMD-ML_CCCOLL_INTEREST-ML_ADJCOLL_REVENUE+ML_DIFF_REV)+SUM(ML_TAX_OB+ML_TAX_DMD-ML_CCCOLL_TAX-ML_ADJCOLL_TAX+ML_DIFF_TAX)  CB_TOTAL
							FROM MONTHLY_LEDGER,DCB_TRF_DESR 
							WHERE ML_OM_CD LIKE '''||P_LOCATION_CODE||'%'' 
							AND ML_MONTH_YEAR  = TO_DATE('''||P_MONTH||''',''Mon-yyyy'') 
							AND ML_TRF_DESCR(+) = TM_SDESCR';
							
							IF  LENGTH(P_TRFS)>0 THEN 
								STR:= STR ||' AND TM_TRF_CODE IN ('||P_TRFS||')';
							END IF; 
							IF  LENGTH(P_STATION)>0 THEN 
								STR:= STR ||' AND ML_STATION_CD = ('''||P_STATION||''')';
							END IF;
							IF  LENGTH(P_FEEDER)>0 THEN 
								STR:= STR ||' AND ML_FDR_CD = ('''||P_FEEDER||''')';
							END IF;
							IF  LENGTH(P_TRANSFORMER)>0 THEN 
								STR:= STR ||' AND ML_TC_CD = ('''||P_TRANSFORMER||''')';
							END IF;
							IF  LENGTH(P_MR)>0 THEN 
								STR:= STR ||' AND ML_MR_CD = ('''||P_MR||''')';
							END IF;
							IF  LENGTH(P_GPS)>0 THEN 
								STR:= STR ||' AND ML_GRAMA_PANCHAYAT IN ('''||P_GPS||''')';
							END IF;
							
							STR:= STR ||' GROUP BY TRFGRP, TRFORDER, ACCCD_DMD, ACCCD_BAL, VC,TM_SDESCR, TM_LDESCR 
										  ORDER BY TRFGRP, TRFORDER)' ;								
								          
							/*IF P_HEADER=TRUE AND NVL(HT,0) = 0 THEN
								STR:= STR ||'SELECT * FROM T WHERE TRFGRP = 2 UNION SELECT 1,99,NULL,NULL,NULL,NULL,''HT Total:'', 
												SUM(KW),SUM(HP),SUM(KVA),SUM(TOT_INSTS),SUM(ACTIVE_INSTS),SUM(TOBE_BILLED),SUM(BILLED_INSTS),SUM(MTR_UNITS),SUM(ASSD_UNITS),SUM(WHEELED_UNITS),SUM(CONSMPTOT), 
												SUM(REV_OB),SUM(INT_OB),SUM(TAX_OB),SUM(OBTOT),SUM(REV_DMD),SUM(INT_DMD),SUM(TAX_DMD),SUM(DMDTOT),SUM(OBDMDREV),SUM(OBDMDINT),SUM(OBDMDTAX),SUM(OBDMDTOT),												 
												SUM(CCREV_COLL),SUM(ADJREV_COLL),SUM(COLLREVTOT),SUM(CCINT_COLL),SUM(ADJINT_COLL),SUM(COLLINTTOT),SUM(CCTAX_COLL),SUM(ADJTAX_COLL),SUM(COLLTAXTOT),SUM(COLLTOT),												
												SUM(REV_CB), SUM(INT_CB), SUM(INT_CB), SUM(CB_TOTAL) FROM T WHERE TRFGRP= 2 UNION  SELECT * FROM T WHERE TRFGRP = 1 UNION SELECT 2,99,NULL,NULL,NULL,NULL,''HT Total:'', 
												SUM(KW),SUM(HP),SUM(KVA),SUM(TOT_INSTS),SUM(ACTIVE_INSTS),SUM(TOBE_BILLED),SUM(BILLED_INSTS),SUM(MTR_UNITS),SUM(ASSD_UNITS),SUM(WHEELED_UNITS),SUM(CONSMPTOT), 
												SUM(REV_OB),SUM(INT_OB),SUM(TAX_OB),SUM(OBTOT),SUM(REV_DMD),SUM(INT_DMD),SUM(TAX_DMD),SUM(DMDTOT),SUM(OBDMDREV),SUM(OBDMDINT),SUM(OBDMDTAX),SUM(OBDMDTOT),												 
												SUM(CCREV_COLL),SUM(ADJREV_COLL),SUM(COLLREVTOT),SUM(CCINT_COLL),SUM(ADJINT_COLL),SUM(COLLINTTOT),SUM(CCTAX_COLL),SUM(ADJTAX_COLL),SUM(COLLTAXTOT),SUM(COLLTOT),												
												SUM(REV_CB), SUM(INT_CB), SUM(INT_CB), SUM(CB_TOTAL) FROM T WHERE TRFGRP= 1 UNION SELECT 3,99,NULL,NULL,NULL,NULL,''LT+HT Total:'', 
												SUM(KW),SUM(HP),SUM(KVA),SUM(TOT_INSTS),SUM(ACTIVE_INSTS),SUM(TOBE_BILLED),SUM(BILLED_INSTS),SUM(MTR_UNITS),SUM(ASSD_UNITS),SUM(WHEELED_UNITS),SUM(CONSMPTOT), 
												SUM(REV_OB),SUM(INT_OB),SUM(TAX_OB),SUM(OBTOT),SUM(REV_DMD),SUM(INT_DMD),SUM(TAX_DMD),SUM(DMDTOT),SUM(OBDMDREV),SUM(OBDMDINT),SUM(OBDMDTAX),SUM(OBDMDTOT),												 
												SUM(CCREV_COLL),SUM(ADJREV_COLL),SUM(COLLREVTOT),SUM(CCINT_COLL),SUM(ADJINT_COLL),SUM(COLLINTTOT),SUM(CCTAX_COLL),SUM(ADJTAX_COLL),SUM(COLLTAXTOT),SUM(COLLTOT),												
												SUM(REV_CB),SUM(INT_CB),SUM(INT_CB),SUM(CB_TOTAL) FROM T';  
									
								ELSIF P_HEADER=TRUE AND NVL(HT,0) != 0 THEN
								       STR:= STR ||'    SELECT * FROM T UNION SELECT 3,99,NULL,NULL,NULL,NULL,''LT+HT Total:'', 
														SUM(KW),SUM(HP),SUM(KVA),SUM(TOT_INSTS),SUM(ACTIVE_INSTS),SUM(TOBE_BILLED),SUM(BILLED_INSTS),SUM(MTR_UNITS),SUM(ASSD_UNITS),SUM(WHEELED_UNITS),SUM(CONSMPTOT), 
														SUM(REV_OB),SUM(INT_OB),SUM(TAX_OB),SUM(OBTOT),SUM(REV_DMD),SUM(INT_DMD),SUM(TAX_DMD),SUM(DMDTOT),SUM(OBDMDREV),SUM(OBDMDINT),SUM(OBDMDTAX),SUM(OBDMDTOT),												 
														SUM(CCREV_COLL),SUM(ADJREV_COLL),SUM(COLLREVTOT),SUM(CCINT_COLL),SUM(ADJINT_COLL),SUM(COLLINTTOT),SUM(CCTAX_COLL),SUM(ADJTAX_COLL),SUM(COLLTAXTOT),SUM(COLLTOT),												
														SUM(REV_CB),SUM(INT_CB),SUM(INT_CB),SUM(CB_TOTAL) FROM T';
								ELSE
									   STR:= STR ||'    SELECT * FROM T';
										
								END IF; */
								
							IF P_HEADER=TRUE THEN 
							
							        STR:= STR ||'   SELECT * FROM T 
							        				UNION 
							        				SELECT 3,99,NULL,NULL,NULL,NULL,''Grand Total:'',
							        				SUM(KW),SUM(HP),SUM(KVA),
							        				SUM(TOT_INSTS),SUM(ACTIVE_INSTS),SUM(TOBE_BILLED),SUM(BILLED_INSTS),
							        				SUM(MTR_UNITS),SUM(ASSD_UNITS),SUM(WHEELED_UNITS),SUM(CONSMPTOT), 
												 	SUM(REV_OB),SUM(INT_OB),SUM(TAX_OB),SUM(OBTOT),
												 	SUM(REV_DMD),SUM(INT_DMD),SUM(TAX_DMD),SUM(DMDTOT),
												 	SUM(OBDMDREV),SUM(OBDMDINT),SUM(OBDMDTAX),SUM(OBDMDTOT),												 
													SUM(CCREV_COLL),SUM(ADJREV_COLL),SUM(COLLREVTOT),
													SUM(CCINT_COLL),SUM(ADJINT_COLL),SUM(COLLINTTOT),
													SUM(CCTAX_COLL),SUM(ADJTAX_COLL),SUM(COLLTAXTOT),
													SUM(COLLTOT),												
						 							SUM(REV_CB),SUM(INT_CB),SUM(INT_CB),SUM(CB_TOTAL) 
						 							FROM T';
						    ELSE 
						    		STR:= STR ||'   SELECT * FROM T';
							END IF;
							
							STR:=' SELECT ROWNUM ROW_NO,A.* FROM ('||STR||') A';	
					 
		        END IF; --REPORTTYPE
		        
		    END IF;
		
		OPEN cur FOR STR;
	    --OPEN cur FOR select str from dual;
		
	END;  
	
	
	
	--VAR CUR REFCURSOR;
	--EXEC PKG_DCB_REPORTS.GET_DCB(:CUR,'MAIN_DCB', '2110105', 'Apr-2013','','','','','','',false);

END PKG_DCB_REPORTS;

/