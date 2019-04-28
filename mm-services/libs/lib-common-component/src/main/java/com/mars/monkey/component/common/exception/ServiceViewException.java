package com.mars.monkey.component.common.exception;

import com.mars.monkey.component.common.response.RespCode;

/**
 * Created on 4/28/2019.
 *
 * @author YouFeng Zhu
 */
public class ServiceViewException extends RuntimeException {

    public static final ClassifiedError INVALID_RESOURCE_ID = new ClassifiedError(RespCode.INVALID_RESOURCE_ID, "Invalid resource ID.");

    public static final ClassifiedError NOT_AUTHORIZED = new ClassifiedError(RespCode.NOT_AUTHORIZED,
            "Not authorized.");

    public static final ClassifiedError INVALID_PARAMETER = new ClassifiedError(RespCode.INVALID_PARAMETER, "Invalid parameter.");

    public static final ClassifiedError UNSUPPORTED_OPERATION = new ClassifiedError(RespCode.UNSUPPORTED_OPERATION, "Unsupported operation.");

    public static final ClassifiedError PROCESSING_FAILURE = new ClassifiedError(RespCode.PROCESSING_FAILURE, "Failure during processing of the request.");

    public static final ClassifiedError SYSTEM_ERROR = new ClassifiedError(RespCode.SYSTEM_ERROR, "System error.");

    public static final ClassifiedError EXTERNAL_ERROR = new ClassifiedError(RespCode.EXTERNAL_ERROR, "External error.");

    public static final ClassifiedError INTERNAL_ERROR = new ClassifiedError(RespCode.INTERNAL_ERROR, "Internal error.");

    public static final ClassifiedError FORMAT_ERROR = new ClassifiedError(RespCode.FORMAT_ERROR, "FORMAT ERROR");

    private ClassifiedError classifiedError = null;
    private Object additionalError = null;

    public ServiceViewException(ClassifiedError classifiedError) {
        super();
        this.classifiedError = classifiedError;
    }

    public ServiceViewException(ClassifiedError classifiedError, String message) {
        super(message);
        this.classifiedError = classifiedError;
    }

    public ServiceViewException(ClassifiedError classifiedError, String message, Object additionalClassifiedError) {
        super(message);
        this.classifiedError = classifiedError;
        this.additionalError = additionalClassifiedError;
    }

    public ServiceViewException(ClassifiedError classifiedError, String message, Throwable cause) {
        super(message, cause);
        this.classifiedError = classifiedError;
    }

    public ClassifiedError getClassifiedError() {
        return classifiedError;
    }

    public void setClassifiedError(ClassifiedError classifiedError) {
        this.classifiedError = classifiedError;
    }

    public Object getAdditionalError() {
        return additionalError;
    }

    public void setAdditionalError(Object additionalError) {
        this.additionalError = additionalError;
    }

    public static ServiceViewExceptionBuilder withClassifiedError(ClassifiedError classifiedError) {
        return new ServiceViewExceptionBuilder().withClassifiedError(classifiedError) ;
    }

    public static final class ServiceViewExceptionBuilder {
        private ClassifiedError classifiedError = null;
        private Object additionalError = null;

        private ServiceViewExceptionBuilder() {
        }


        public ServiceViewExceptionBuilder withClassifiedError(ClassifiedError classifiedError) {
            this.classifiedError = classifiedError;
            return this;
        }

        public ServiceViewExceptionBuilder withAdditionalError(Object additionalError) {
            this.additionalError = additionalError;
            return this;
        }

        public ServiceViewException build() {
            ServiceViewException serviceViewException = new ServiceViewException(classifiedError);
            serviceViewException.setAdditionalError(additionalError);
            return serviceViewException;
        }
    }
}
