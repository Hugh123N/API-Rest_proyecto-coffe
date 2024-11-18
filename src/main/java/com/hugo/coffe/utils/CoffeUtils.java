package com.hugo.coffe.utils;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.xml.crypto.Data;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Slf4j
public class CoffeUtils {

    private CoffeUtils(){

    }

    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus){
        return new ResponseEntity<String>("{\"message\":\""+responseMessage+"\"}", httpStatus);
    }
    //para generar ID de la facturaa
    public static String getUUID(){
        Date date=new Date();
        long time=date.getTime();
        return "FACTURA-"+time;
    }
    //obtener un json de textos que ingresara
    public static JSONArray getJsonArrayFromString(String data) throws JSONException {
        JSONArray jsonArray=new JSONArray(data);
        return jsonArray;
    }

    public static Map<String,Object> getMapFromJson(String data){
        if(!Strings.isNullOrEmpty(data))
            return new Gson().fromJson(data, new TypeToken<Map<String,Object>>(){
            }.getType());
        return new HashMap<>();
    }

    public static boolean isFileExist(String patch){
        log.info("interno isFileExist");
        try {
            File file=new File(patch);
            return file.exists() ? Boolean.TRUE : Boolean.FALSE;
        }catch (Exception e){
            log.error("error de validacion de archivo ",e.getMessage());
        }
        return false;
    }

}
