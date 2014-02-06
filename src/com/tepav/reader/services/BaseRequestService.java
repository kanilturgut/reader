package com.tepav.reader.services;

/**
 * Created with IntelliJ IDEA.
 * User: Umut Ozan Yıldırım
 * Date: 06/02/14
 * Time: 17:27
 */
public class BaseRequestService {

    private static BaseRequestService instance;

    private static final String DOMAIN      = "http://test.vklab.net/tepav/server/";
    private static final String OP_HABER    = "/tepav/haber";
    private static final String OP_GUNLUK   = "/tepav/gunluk";
    private static final String OP_YAYIN    = "/tepav/yayin";

    private BaseRequestService() {

    }

    public static BaseRequestService getInstance() {

        if (instance == nil) {
            instance = new BaseRequestService();
        }

        return  instance;

    }



}
