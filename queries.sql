select TABLE_SCHEMA, TABLE_NAME, round(((data_length + index_length) / 1024 / 1024), 2) 'SIZE'
from information_schema.TABLES
where TABLE_SCHEMA = 'test'
order by 3 desc
go

select count(*)
from GEONAMES_IT
go

select *
from GEONAMES_IT
where lower(NAME) like '%luino%'
union all
select *
from GEONAMES_IT
where lower(NAME) like '%maccagno%'
union all
select *
from GEONAMES_IT
where lower(NAME) like '%germignaga%'
go

select *
from GEONAMES_FEATURE_CODES
go

select FEATURE_CODE,count(*)
from GEONAMES_IT
where FEATURE_CLASS in ('A','P')
group by FEATURE_CODE
go

select *
from GEONAMES_ADMIN_CODE_1
order by 1 asc
go

select *
from GEONAMES_ADMIN_CODE_2
order by 1 asc
go

select *
from GEONAMES_HIERARCHY
where HIERARCHY_ID2 = 3174618
go

select g.GEONAME_ID, g.NAME, g.FEATURE_CLASS, g.FEATURE_CODE, gh.HIERARCHY_TYPE
from GEONAMES_IT g, GEONAMES_HIERARCHY gh
where g.GEONAME_ID = gh.HIERARCHY_ID2
go

select *
from GEONAMES_FEATURE_CODES
go

select *
from GEONAMES_IT
where FEATURE_CODE = 'ADM1'
go

select *
from GEONAMES_IT
where FEATURE_CODE = 'ADM2'
go

select *
from GEONAMES_IT
where FEATURE_CODE = 'ADM3'
go

// search luino
select *
from GEONAMES_IT
where lower(NAME) like '%luino%'
and FEATURE_CLASS = 'A'
go

// search id of luino parent
select *
from GEONAMES_HIERARCHY
where HIERARCHY_ID2 = 6540157
go

// search name of luino parent
select *
from GEONAMES_IT
where GEONAME_ID = 3164697
go

// search id of varese parent
select *
from GEONAMES_HIERARCHY
where HIERARCHY_ID2 = 3164697
go

// search name of varese parent
select *
from GEONAMES_IT
where GEONAME_ID = 3174618
go

// search id of lombardia parent
select *
from GEONAMES_HIERARCHY
where HIERARCHY_ID2 = 3174618
go

// search name of lombardia parent
select *
from GEONAMES_IT
where GEONAME_ID = 3175395
go

// now search repubblica italiana parent
select *
from GEONAMES_HIERARCHY
where HIERARCHY_ID2 = 3175395
go

// search if luino is parent of anything
select *
from GEONAMES_HIERARCHY
where HIERARCHY_ID1 = 6540157
go

// serch all comunes in varese provincia
select g.GEONAME_ID,g.NAME
from GEONAMES_HIERARCHY gh, GEONAMES_IT g
where gh.HIERARCHY_ID1 = 3164697
and gh.HIERARCHY_ID2 = g.GEONAME_ID
go

select *
from GEONAMES_HIERARCHY
where HIERARCHY_ID2 = 7285438
go

select *
from GEONAMES_HIERARCHY
where HIERARCHY_ID1 = 7285438
go

select *
from GEONAMES_HIERARCHY
where HIERARCHY_ID2 = 6458771
go

select *
from GEONAMES_HIERARCHY
where HIERARCHY_ID2 = 2658370
go

// CH
select *
from GEONAMES_CH
go

select *
from GEONAMES_CH
where lower(NAME) like '%caslano%'
go

select *
from GEONAMES_HIERARCHY where HIERARCHY_ID2 = 7285438
go

// serch all comunes in lugano district
select g.GEONAME_ID,g.NAME
from GEONAMES_HIERARCHY gh, GEONAMES_CH g
where gh.HIERARCHY_ID1 = 6458771
and gh.HIERARCHY_ID2 = g.GEONAME_ID
go

// ES
select *
from GEONAMES_ES
where lower(NAME) like '%aran%'
and FEATURE_CLASS = 'P'
go

select *
from GEONAMES_FEATURE_CODES
where CONCATENATED_CODE like '%ADMD%'
go

select *
from GEONAMES_HIERARCHY
where HIERARCHY_ID1 = 6457336
go

// serch all comunes in lugano district
select g.GEONAME_ID,g.NAME
from GEONAMES_HIERARCHY gh, GEONAMES_ES g
where gh.HIERARCHY_ID1 = 6457336
and gh.HIERARCHY_ID2 = g.GEONAME_ID
go

select *
from GEONAMES_HIERARCHY
where HIERARCHY_ID2 = 6457336
go