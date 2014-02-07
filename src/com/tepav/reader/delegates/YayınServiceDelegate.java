package com.tepav.reader.delegates;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Umut Ozan Yıldırım
 * Date: 07/02/14
 * Time: 19:12
 */
public interface YayınServiceDelegate {

    public void yayınListRequestDidFinish    (Map responseMap);
    public void nextYayınListRequestDidFinish(Map responseMap);
    public void prevYayınListRequestDidFinish(Map responseMap);

}
