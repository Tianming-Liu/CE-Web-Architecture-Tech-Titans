package com.czandlh.utils;

import com.alibaba.fastjson.JSONObject;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

public class CsvUtils {

    public static void main(String[] args) throws Exception {
        csvReader();
    }

    /**
     * 简单的读取
     *
     * @throws Exception
     */
    public static List<String[]> csvReader() throws Exception {
        String classpath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String fileName = classpath + "templates/MPS LSOA Level Crime (most recent 24 months).csv";
        InputStreamReader reader = new InputStreamReader(new FileInputStream(fileName), Charset.forName("UTF-8"));
        CSVReader csvReader = new CSVReaderBuilder(reader).build();
        List<String[]> list = csvReader.readAll();
        for (String[] strings : list) {
            System.out.println(JSONObject.toJSONString(strings));
        }
        csvReader.close();
        return list;
    }

    public static List<String[]> csvReaderHotel() throws Exception {
        String classpath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String fileName = classpath + "templates/matched_hotel_lsoa.csv";
        InputStreamReader reader = new InputStreamReader(new FileInputStream(fileName), Charset.forName("UTF-8"));
        CSVReader csvReader = new CSVReaderBuilder(reader).build();
        List<String[]> list = csvReader.readAll();
        for (String[] strings : list) {
            System.out.println(JSONObject.toJSONString(strings));
        }
        csvReader.close();
        return list;
    }

}
