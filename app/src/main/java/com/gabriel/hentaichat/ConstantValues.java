package com.gabriel.hentaichat;

/**
 * Created by gabriel on 2017/2/7.
 */

public class ConstantValues {


    /**
     * 接入qq登录接口的app_id
     */
    public static final String APP_ID = "1105887603";

    public static final String QCLOUD_BASE_API = "https://console.tim.qq.com/v4/";

    public static final String QCLOUD_MANAGER = "admin";
    public static final String FRIEND_IDENTIFIER = "identifier";
    public static final String FRIEND_NAME = "friend_name";

    /**
     * 第一次进入应用时登录态是否有效
     */
    public static boolean isSessionValid = false;

    /**
     * 接入腾讯云服务的帐号集成体系所用的accountType
     */
    public static final int ACCOUNT_TYPE = 10879;

    /**
     * 接入腾讯云服务的SdkAppId
     */
    public static final long SDK_APP_ID = 1400025340;


    /**
     * 接入腾讯云服务的public_key
     */
    public static final String PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----\n" +
            "MFYwEAYHKoZIzj0CAQYFK4EEAAoDQgAEz3IZ7neLwljaJ/zIue4ZK3VSevEUVQMH\n" +
            "V946UZ3cFO11oo7/GmJ1XXfiIx/IguBxtnyoW8+J4DOJozYEyrowqA==\n" +
            "-----END PUBLIC KEY-----";

    /**
     * 接入腾讯云服务的sig
     */
    public static final String SIG = "eJx10E9PwjAYx-H7XkXTszHVtVtn4mF-cEIQdDphp6as3XwilLIVojG*dy" +
            "OQuIvP9fdJvsnz5SGE8Mv0*VLW9XZvnHCfVmN0g-AV4WGEL-6AtaCEdMLv1AlQQsg18ykZKP1hodNCNk53J" +
            "8XCgJPfGyhQ2jho4Gyk2oAZzL16F8fc-50e2uP4MCrT8d0ki0xSRMv0yaQHv91t87U-74uEsYmN62RKZ1wl" +
            "arUqeQyjmGX5bJH0jwd4m5Oy2S9tcZ-tSJlX1diphaVttn6VjNu2uh0kHWzOn6Gcs4DQMMDet-cDeWVXRg__";
    /**
     * 腾讯云登录返回的sig
     */
    public static String LOGIN_SIG = "login_sig";

    /**
     * 腾讯云登录返回的identifier
     */
    public static String LOGIN_IDENTIFIER = "login_identifier";
}
