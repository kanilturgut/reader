package com.tepav.reader.delegates;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Umut Ozan Yıldırım
 * Date: 07/02/14
 * Time: 19:12
 */
public interface YayinServiceDelegate {

    public void yayinListRequestDidFinish    (Map responseMap);
    public void nextYayinListRequestDidFinish(Map responseMap);
    public void prevYayinListRequestDidFinish(Map responseMap);

}
