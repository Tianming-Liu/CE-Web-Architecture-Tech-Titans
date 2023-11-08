-- --Create a sql query for lsoa searcn in certain lat and lon
-- SELECT LSOA_Code
-- FROM lsoa
-- WHERE ST_Contains(geo, POINT(-0.12687241011485248, 51.51919313380855));

SELECT LSOA_Code
FROM lsoa
WHERE ST_Distance_Sphere(ST_Centroid(geo), POINT(-0.12687241011485248, 51.51919313380855)) <= 500;

SELECT LSOA_Num FROM lsoa WHERE ST_Distance_Sphere(ST_Centroid(geo), POINT(-0.12687241011485248,51.51919313380855)) <= 2000;