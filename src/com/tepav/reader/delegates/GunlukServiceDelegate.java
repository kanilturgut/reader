package com.tepav.reader.delegates;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Umut Ozan Yıldırım
 * Date: 07/02/14
 * Time: 16:33
 */
public interface GunlukServiceDelegate {

    public void gunlukListRequestDidFinish    (Map responseMap);
    public void nextGunlukListRequestDidFinish(Map responseMap);
    public void prevGunlukListRequestDidFinish(Map responseMap);

}
