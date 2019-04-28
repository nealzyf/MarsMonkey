package com.mars.monkey.component.common.response;

/**
 * Created on 2017/12/5.
 *
 * @author YouFeng.Zhu
 */
public class RespCode {
    public static final int SUCCESS = 0;
    @Deprecated
    public static final int FAILURE = -1;
    public static final int INVALID_RESOURCE_ID = 10001;
    public static final int NOT_AUTHORIZED = 10002;
    public static final int INVALID_PARAMETER = 10003;
    public static final int UNSUPPORTED_OPERATION = 10004;
    public static final int PROCESSING_FAILURE = 10005;
    public static final int SYSTEM_ERROR = 10006;
    public static final int EXTERNAL_ERROR = 10007;
    public static final int INTERNAL_ERROR = 10008;
    public static final int FORMAT_ERROR = 10009;
    public static final int ALREAD_EXIST = 10010;
}
