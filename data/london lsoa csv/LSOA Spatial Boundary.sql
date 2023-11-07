-- LOAD DATA LOCAL INFILE '/Users/tianming_liu/00Self/CE/0017 Web Architecture/Final/CE-Web-Architecture-Tech-Titans/data/london lsoa csv/LSOA London.csv' 
-- INTO TABLE LSOA_GEO 
-- FIELDS TERMINATED BY ',' 
-- ENCLOSED BY '"' 
-- LINES TERMINATED BY '\n' IGNORE 1 LINES (LSOA_Code, LSOA_Name, POPDEN, @var4) SET polygon_data = ST_GeomFromText(@var4);


-- LOAD DATA LOCAL INFILE 'path/to/folder/FILE' 
-- INTO TABLE photo_locations 
-- FIELDS TERMINATED BY ',' 
-- ENCLOSED BY '"' 
-- LINES TERMINATED BY '\n' IGNORE 1 LINES (pid, lat, lon, @var4) SET coords = ST_GeomFromText(@var4);

-- SELECT * FROM LSOA_GEO

-- Create a new column for geometry in WGS84 Coodinate System
-- ALTER TABLE LSOA_GEO ADD COLUMN wgs84_geom GEOMETRY;

-- Transfrom OSGB36 coords to WGS84
UPDATE LSOA_GEO
SET wgs84_geom = ST_Transform(polygon_data, 4326)
WHERE ST_SRID(polygon_data) = 27700;