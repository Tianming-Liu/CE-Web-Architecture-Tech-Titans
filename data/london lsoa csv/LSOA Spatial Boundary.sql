-- CREATE TABLE `ucfntl0`.`lsoa` (
--   `LSOA_Code` VARCHAR(255) NOT NULL,
--   `LSOA_Name` VARCHAR(255) NULL,
--   `POPDEN` FLOAT NULL,
--   `geometry` GEOMETRY NULL,
--   PRIMARY KEY (`LSOA_Code`));

LOAD DATA LOCAL INFILE '/Users/tianming_liu/00Self/CE/0017 Web Architecture/Final/CE-Web-Architecture-Tech-Titans/data/london lsoa csv/LSOA_London_4326.csv' 
INTO TABLE lsoa 
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"' 
LINES TERMINATED BY '\n' IGNORE 1 LINES (LSOA_Code, LSOA_Name, POPDEN, @var4) SET geo = ST_GeomFromText(@var4);