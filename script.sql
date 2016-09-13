/*

Download your country code from: http://download.geonames.org/export/dump/

Add the following header line at the beginning of the geonames file:
GEONAME_ID	NAME	NAME_ASCII	NAME_ALTERNATE	LATITUDE	LONGITUDE	FEATURE_CLASS	FEATURE_CODE	COUNTRY_CODE	CC2	ADMIN_CODE1	ADMIN_CODE2	ADMIN_CODE3	ADMIN_CODE4	POPULATION	ELEVATION	DEM	TIMEZONE	LAST_UPDATE

IMPORTANT NOTE: Remember to keep the tabs
IMPORTANT NOTE: Replace $COUNTRY_CODE with the ISO code of the country you want

import the text file, with a nice SQL IDE you will get the fields correctly mapped to the table
*/
CREATE TABLE GEONAMES_$COUNTRY_CODE  ( 
	GEONAME_ID   	decimal(10,0) NOT NULL,
	NAME         	varchar(500) NULL,
	NAME_ASCII   	varchar(500) NULL,
	LATITUDE     	varchar(100) NULL,
	LONGITUDE    	varchar(100) NULL,
	FEATURE_CLASS	varchar(25) NULL,
	FEATURE_CODE 	varchar(25) NULL,
	ADMIN_CODE1  	varchar(25) NULL,
	ADMIN_CODE2  	varchar(25) NULL,
	ADMIN_CODE3  	varchar(25) NULL,
	ADMIN_CODE4  	varchar(25) NULL,
	PRIMARY KEY(GEONAME_ID)
);

// Important, used to create the hierarchy
CREATE TABLE GEONAMES_HIERARCHY  ( 
	HIERARCHY_ID1 	decimal(10,0) NOT NULL,
	HIERARCHY_ID2 	decimal(10,0) NOT NULL,
	HIERARCHY_TYPE	varchar(25) NOT NULL 
	);

// Feature code explanation
CREATE TABLE GEONAMES_FEATURE_CODES  ( 
	CONCATENATED_CODE	varchar(25) NOT NULL,
	SHORT_DESCR      	varchar(500) NULL,
	LONG_DESCR       	varchar(1000) NULL,
	PRIMARY KEY(CONCATENATED_CODE)
);

// Not really needed but nice to have
CREATE TABLE GEONAMES_ADMIN_CODE_1  ( 
	GEONAME_ID       	decimal(10,0) NOT NULL,
	NAME             	varchar(500) NULL,
	NAME_ASCII       	varchar(500) NULL,
	CONCATENATED_CODE	varchar(200) NULL,
	PRIMARY KEY(GEONAME_ID)
);

// Not really needed but nice to have
CREATE TABLE GEONAMES_ADMIN_CODE_2  ( 
	GEONAME_ID       	decimal(10,0) NOT NULL,
	NAME             	varchar(500) NULL,
	NAME_ASCII       	varchar(500) NULL,
	CONCATENATED_CODE	varchar(500) NULL,
	PRIMARY KEY(GEONAME_ID)
);

// A query to check the hierarchy mapping, remember to use correct country table
select gh.HIERARCHY_ID1 as PARENT_ID, g.GEONAME_ID as NODE_ID, g.NAME, 
concat_ws('.',g.FEATURE_CLASS,g.FEATURE_CODE) as ADM_TYPE
from GEONAMES_HIERARCHY gh, GEONAMES_IT g
where gh.HIERARCHY_ID2 = g.GEONAME_ID
and g.FEATURE_CLASS = 'A';

// To map this query with a view, remeber to use correct country table
create view GEONAMES_CH_MAP as (
select gh.HIERARCHY_ID1 as PARENT_ID, g.GEONAME_ID as NODE_ID, g.NAME, 
concat_ws('.',g.FEATURE_CLASS,g.FEATURE_CODE) as ADM_TYPE
from GEONAMES_HIERARCHY gh, GEONAMES_CH g
where gh.HIERARCHY_ID2 = g.GEONAME_ID
and g.FEATURE_CLASS = 'A');