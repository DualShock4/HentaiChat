package com.gabriel.hentaichat.model;

import java.util.ArrayList;

/**
 * Created by gabriel on 2017/3/3.
 */

public class FriendListGet {
    public String NeedUpdateAll;
    public int TimeStampNow;
    public int StartIndex;
    public ArrayList<FriendInfo> InfoItem;
    public int CurrentStandardSequence;
    public int FriendNum = 0;
    public String ActionStatus;
    public int ErrorCode;
    public String ErrorInfo;
    public String ErrorDisplay;

    public class FriendInfo {
        public String Info_Account;
        public ArrayList<InfoDetail> SnsProfileItem;

        public class InfoDetail {
            public String Tag;
            public String Value;
        }
    }


//    "SnsProfileItem":
//            [
//    {
//        "Tag":"Tag_Profile_IM_Nick",
//            "Value":"NickTest1"
//    },
//    {
//        "Tag":"Tag_SNS_IM_Group",
//            "Value":["Group1"]
//    },
//    {
//        "Tag":"Tag_SNS_IM_Remark",
//            "Value":"Remark1"
//    }
//    ]
}
