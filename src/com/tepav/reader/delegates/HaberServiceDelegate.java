package com.tepav.reader.delegates;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Umut Ozan Yıldırım
 * Date: 06/02/14
 * Time: 21:13
 */
public interface HaberServiceDelegate {

    public void haberListRequestDidFinish    (Map responseMap);
    public void nextHaberListRequestDidFinish(Map responseMap);
    public void prevHaberListRequestDidFinish(Map responseMap);

}
