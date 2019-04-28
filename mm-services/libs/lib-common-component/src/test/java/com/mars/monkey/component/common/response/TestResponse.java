package com.mars.monkey.component.common.response;

import com.mars.monkey.utils.transform.JsonTransformUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 5/15/2019.
 *
 * @author YouFeng Zhu
 */
public class TestResponse {

    private final static Logger LOGGER = LoggerFactory.getLogger(TestResponse.class);

    @Test
    public void responseMethod() {
        Response response = Response.fail("test");
        LOGGER.info("{}", JsonTransformUtil.toJson(response));
        Response<Long> test2 ;
        test2 = Response.<Long>withCode(RespCode.NOT_AUTHORIZED).withData(1L).build();
//        LOGGER.info("{}", builder.getData().getClass());
//        Response response2 = Response.withCode(RespCode.NOT_AUTHORIZED).withMessage("NOT_AUTHORIZED").withData();
    }

}
