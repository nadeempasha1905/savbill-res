CREATE OR REPLACE
PROCEDURE GET_BILLING_EFFICIENCY(CUR OUT SYS_REFCURSOR, P_GROUP_TYPE IN VARCHAR2, P_LOCATION_CODE IN VARCHAR2, P_MR_CODE IN VARCHAR2, P_TRFS IN VARCHAR2, P_FROM_DT IN VARCHAR2, P_TO_DT IN VARCHAR2, P_HEADER IN BOOLEAN)
IS   
 
 E 	  NUMBER;
 STR  VARCHAR2(4000);

BEGIN

	SELECT NVL(LENGTH(P_GROUP_TYPE||P_LOCATION_CODE||P_MR_CODE||P_TRFS||P_FROM_DT||P_TO_DT),0) INTO E FROM DUAL; 
		
	IF E>0 THEN
	
		IF P_GROUP_TYPE = 'TRF' THEN
		
			STR :=' SELECT SUBSTR(REPLACE(TRF,''DB'',''''),1,3) TRF, SUM(NVL(BILLED,0)+NVL(NOTBILLED,0)) TOBEBILLED, SUM(NVL(BILLED,0)) BILLED, SUM(NVL(NOTBILLED,0)) NOTBILLED,
					(CASE WHEN SUM(NVL(BILLED,0)+NVL(NOTBILLED,0))=0 THEN 0 ELSE ROUND((SUM(NVL(BILLED,0))/SUM(NVL(BILLED,0)+NVL(NOTBILLED,0)))*100,2) END) BILLEFF,
					SUM(NVL(BILLED,0)-NVL(DL,0)-NVL(MNR,0)-NVL(DC,0)-NVL(VACANT,0)) NOR, SUM(NVL(DL,0)) DL, SUM(NVL(MNR,0)) MNR, SUM(NVL(DC,0)) DC, SUM(NVL(VACANT,0)) VACANT
					FROM
					(SELECT TRIM(CBM_TRF_DESCR) TRF, COUNT(*) BILLED, 0 NOTBILLED,
					SUM(CASE WHEN MR_DL_MNR_IND=1 THEN 1 ELSE 0 END) DL,
					SUM(CASE WHEN MR_DL_MNR_IND=2 THEN 1 ELSE 0 END) MNR,
					SUM(CASE WHEN NVL(CBM_DC_FLG,''N'')=''Y'' THEN 1 ELSE 0 END) DC,
					SUM(CASE WHEN CM.CM_CONSMR_STS=10 THEN 1 ELSE 0 END) VACANT
					FROM  CUSTBILL_MASTER
					INNER JOIN CUST_MASTER CM ON CM.CM_RR_NO = CBM_RR_NO
					LEFT OUTER JOIN MTR_RDG M ON M.MR_BCN_RR_NO = CBM_RR_NO AND M.MR_RDG_DT = CBM_BILL_DT
					WHERE CM.CM_RR_NO LIKE  '''||P_LOCATION_CODE||'%''
					AND CBM_BILL_DT BETWEEN TO_DATE('''||P_FROM_DT||''',''DD/MM/YYYY'') AND TO_DATE('''||P_TO_DT||''',''DD/MM/YYYY'')
					AND CM.CM_SERVICE_DT <= TO_DATE('''||P_TO_DT||''',''DD/MM/YYYY'')'; 
					
			IF LENGTH(P_MR_CODE) > 0 THEN  
					STR := STR ||' AND CM.CM_MTR_RDR_CD = '''||P_MR_CODE||''' ';
			END IF;    
			
			IF LENGTH(P_TRFS) > 0 THEN
				STR := STR ||' AND CM.CM_TRF_CATG IN ('||P_TRFS||') ';
			END IF;			
			
			 STR:= STR||' 
			 		GROUP BY CBM_TRF_DESCR
					
					UNION
					
					SELECT TRIM(TM_SDESCR) TRF, 0 AS BILLED, COUNT(*) NOTBILLED, 0 DL, 0 MNR, 0 DC, 0 VACANT
					FROM  CUST_MASTER CM, TRF_MASTER
					WHERE CM.CM_RR_NO LIKE '''||P_LOCATION_CODE||'%''
					AND CM.CM_SERVICE_DT <= TO_DATE('''||P_TO_DT||''',''DD/MM/YYYY'')
					AND CM.CM_CONSMR_STS IN (1, 9, 10)
					AND CM.CM_MTR_RDG_DAY BETWEEN 1 AND 10
					AND CM.CM_TRF_CATG = TM_TRF_CODE';
					
			IF LENGTH(P_MR_CODE) > 0 THEN  
					STR := STR ||' AND CM.CM_MTR_RDR_CD = '''||P_MR_CODE||''' ';
			END IF;    
			
			IF LENGTH(P_TRFS) > 0 THEN
				STR := STR ||' AND CM.CM_TRF_CATG IN ('||P_TRFS||') ';
			END IF;			
			
			STR:= STR||' 						
					AND NOT EXISTS (SELECT ''X'' FROM CUSTBILL_MASTER WHERE CBM_RR_NO = CM.CM_RR_NO AND CBM_BILL_DT BETWEEN TO_DATE('''||P_FROM_DT||''',''DD/MM/YYYY'') AND TO_DATE('''||P_TO_DT||''',''DD/MM/YYYY''))
					GROUP BY TM_SDESCR)
					GROUP BY SUBSTR(REPLACE(TRF,''DB'',''''),1,3)
					ORDER BY REPLACE(SUBSTR(REPLACE(TRF,''DB'',''''),1,3),''HT'',''ZZ'')';
		                
  			STR:='  WITH T AS (
  			SELECT ROWNUM ROW_NO,A.* FROM ('||STR||') A
  			)
  			SELECT * FROM
			(';
  			
  			IF P_HEADER = TRUE THEN
				STR := STR || 'select TO_CHAR(''Sl. No'') ROW_NO,''Tariff'' TRF,''To be Billed'' TOBEBILLED,''Billed'' BILLED, ''Not Billed'' NOTBILLED,
				              ''% of Billing Efficency'' BILLEFF,''Normal'' NOR,''Door Lock'' DL,''MNR'' MNR,''Direct Connection'' DC,''Vacant'' VACANT
				              from T
				              UNION';
			END IF;
			
			IF P_HEADER = TRUE THEN
				STR := STR || ' SELECT TO_CHAR(ROW_NO) ROW_NO, TO_CHAR(TRF) TRF, TO_CHAR(TOBEBILLED) TOBEBILLED, TO_CHAR(BILLED) BILLED, TO_CHAR(NOTBILLED) NOTBILLED,
								TO_CHAR(BILLEFF) BILLEFF, TO_CHAR(NOR) NOR, TO_CHAR(DL) DL, TO_CHAR(MNR) MNR, TO_CHAR(DC) DC, TO_CHAR(VACANT) VACANT  FROM T';
			ELSE
				STR := STR || ' SELECT ROW_NO, TRF, TOBEBILLED, BILLED, NOTBILLED, BILLEFF, NOR, DL, MNR, DC, VACANT  FROM T';				
			END IF;
			
			IF P_HEADER = TRUE THEN
				STR := STR || ' UNION
								SELECT '''', ''Total:'', TO_CHAR(SUM(TOBEBILLED)) , TO_CHAR(SUM(BILLED)) BILLED, TO_CHAR(SUM(NOTBILLED)) NOTBILLED,
								TO_CHAR((CASE WHEN SUM(TOBEBILLED)=0 THEN 0 ELSE ROUND((SUM(BILLED)/SUM(TOBEBILLED))*100,2) END)) BILLEFF,
								TO_CHAR(SUM(BILLED-DL-MNR-DC-VACANT)) NOR, TO_CHAR(SUM(DL)) DL, TO_CHAR(SUM(MNR)) MNR, TO_CHAR(SUM(DC)) DC, TO_CHAR(SUM(VACANT)) VACANT
								FROM T';
			END IF;
			STR := STR || ')
			ORDER BY REPLACE(ROW_NO,''Sl. No'',0)
			';
		ELSIF P_GROUP_TYPE = 'SUBTRF' THEN
		
			STR :=' SELECT TRF, SUM(NVL(BILLED,0)+NVL(NOTBILLED,0)) TOBEBILLED, SUM(NVL(BILLED,0)) BILLED, SUM(NVL(NOTBILLED,0)) NOTBILLED,
					(CASE WHEN SUM(NVL(BILLED,0)+NVL(NOTBILLED,0))=0 THEN 0 ELSE ROUND((SUM(NVL(BILLED,0))/SUM(NVL(BILLED,0)+NVL(NOTBILLED,0)))*100,2) END) BILLEFF,
					SUM(NVL(BILLED,0)-NVL(DL,0)-NVL(MNR,0)-NVL(DC,0)-NVL(VACANT,0)) NOR, SUM(NVL(DL,0)) DL, SUM(NVL(MNR,0)) MNR, SUM(NVL(DC,0)) DC, SUM(NVL(VACANT,0)) VACANT
					FROM
					(SELECT TRIM(CBM_TRF_DESCR) TRF, COUNT(*) BILLED, 0 NOTBILLED,
					SUM(CASE WHEN MR_DL_MNR_IND=1 THEN 1 ELSE 0 END) DL,
					SUM(CASE WHEN MR_DL_MNR_IND=2 THEN 1 ELSE 0 END) MNR,
					SUM(CASE WHEN NVL(CBM_DC_FLG,''N'')=''Y'' THEN 1 ELSE 0 END) DC,
					SUM(CASE WHEN CM.CM_CONSMR_STS=10 THEN 1 ELSE 0 END) VACANT
					FROM  CUSTBILL_MASTER
					INNER JOIN CUST_MASTER CM ON CM.CM_RR_NO = CBM_RR_NO
					LEFT OUTER JOIN MTR_RDG M ON M.MR_BCN_RR_NO = CBM_RR_NO AND M.MR_RDG_DT = CBM_BILL_DT
					WHERE CM.CM_RR_NO LIKE  '''||P_LOCATION_CODE||'%''
					AND CBM_BILL_DT BETWEEN TO_DATE('''||P_FROM_DT||''',''DD/MM/YYYY'') AND TO_DATE('''||P_TO_DT||''',''DD/MM/YYYY'')
					AND CM.CM_SERVICE_DT <= TO_DATE('''||P_TO_DT||''',''DD/MM/YYYY'')'; 
					
			IF LENGTH(P_MR_CODE) > 0 THEN  
					STR := STR ||' AND CM.CM_MTR_RDR_CD = '''||P_MR_CODE||''' ';
			END IF;    
			
			IF LENGTH(P_TRFS) > 0 THEN
				STR := STR ||' AND CM.CM_TRF_CATG IN ('||P_TRFS||') ';
			END IF;			
			
			 STR:= STR||' 
			 		GROUP BY CBM_TRF_DESCR
					
					UNION
					
					SELECT TRIM(TM_SDESCR) TRF, 0 AS BILLED, COUNT(*) NOTBILLED, 0 DL, 0 MNR, 0 DC, 0 VACANT
					FROM  CUST_MASTER CM, TRF_MASTER
					WHERE CM.CM_RR_NO LIKE '''||P_LOCATION_CODE||'%''
					AND CM.CM_SERVICE_DT <= TO_DATE('''||P_TO_DT||''',''DD/MM/YYYY'')
					AND CM.CM_CONSMR_STS IN (1, 9, 10)
					AND CM.CM_MTR_RDG_DAY BETWEEN 1 AND 10
					AND CM.CM_TRF_CATG = TM_TRF_CODE';
					
			IF LENGTH(P_MR_CODE) > 0 THEN  
					STR := STR ||' AND CM.CM_MTR_RDR_CD = '''||P_MR_CODE||''' ';
			END IF;    
			
			IF LENGTH(P_TRFS) > 0 THEN
				STR := STR ||' AND CM.CM_TRF_CATG IN ('||P_TRFS||') ';
			END IF;			
			
			STR:= STR||' 						
					AND NOT EXISTS (SELECT ''X'' FROM CUSTBILL_MASTER WHERE CBM_RR_NO = CM.CM_RR_NO AND CBM_BILL_DT BETWEEN TO_DATE('''||P_FROM_DT||''',''DD/MM/YYYY'') AND TO_DATE('''||P_TO_DT||''',''DD/MM/YYYY''))
					GROUP BY TM_SDESCR)
					GROUP BY TRF
					ORDER BY REPLACE(TRF,''HT'',''ZZ'')';
		                
  			STR:='  WITH T AS (
  			SELECT ROWNUM ROW_NO,A.* FROM ('||STR||') A
  			)
  			SELECT * FROM
			(';
  			
  			IF P_HEADER = TRUE THEN
				STR := STR || 'select TO_CHAR(''Sl. No'') ROW_NO,''Tariff'' TRF,''To be Billed'' TOBEBILLED,''Billed'' BILLED, ''Not Billed'' NOTBILLED,
				              ''% of Billing Efficency'' BILLEFF,''Normal'' NOR,''Door Lock'' DL,''MNR'' MNR,''Direct Connection'' DC,''Vacant'' VACANT
				              from T
				              UNION';
			END IF;
  			
			IF P_HEADER = TRUE THEN
				STR := STR || ' SELECT TO_CHAR(ROW_NO) ROW_NO, TO_CHAR(TRF) TRF, TO_CHAR(TOBEBILLED) TOBEBILLED, TO_CHAR(BILLED) BILLED, TO_CHAR(NOTBILLED) NOTBILLED,
								TO_CHAR(BILLEFF) BILLEFF, TO_CHAR(NOR) NOR, TO_CHAR(DL) DL, TO_CHAR(MNR) MNR, TO_CHAR(DC) DC, TO_CHAR(VACANT) VACANT  FROM T';
			ELSE
				STR := STR || ' SELECT ROW_NO, TRF, TOBEBILLED, BILLED, NOTBILLED, BILLEFF, NOR, DL, MNR, DC, VACANT  FROM T';				
			END IF;
		
			IF P_HEADER = TRUE THEN
				STR := STR || ' UNION
								SELECT '''', ''Total:'', TO_CHAR(SUM(TOBEBILLED)) , TO_CHAR(SUM(BILLED)) BILLED, TO_CHAR(SUM(NOTBILLED)) NOTBILLED,
								TO_CHAR((CASE WHEN SUM(TOBEBILLED)=0 THEN 0 ELSE ROUND((SUM(BILLED)/SUM(TOBEBILLED))*100,2) END)) BILLEFF,
								TO_CHAR(SUM(BILLED-DL-MNR-DC-VACANT)) NOR, TO_CHAR(SUM(DL)) DL, TO_CHAR(SUM(MNR)) MNR, TO_CHAR(SUM(DC)) DC, TO_CHAR(SUM(VACANT)) VACANT
								FROM T ';
			END IF;
			STR := STR || ')
			ORDER BY TO_NUMBER(REPLACE(ROW_NO,''Sl. No'',0))
			';
	   
		END IF;
		
		   	OPEN cur FOR STR;
		    --OPEN cur FOR select str from dual;
	   	ELSE    
	   	
	   	    OPEN cur FOR select 'Invalid input' AS RESP from dual; 
	   	    
	   	END IF;


END GET_BILLING_EFFICIENCY;

/