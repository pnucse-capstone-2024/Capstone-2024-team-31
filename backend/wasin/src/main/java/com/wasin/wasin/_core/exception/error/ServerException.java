package com.wasin.wasin._core.exception.error;

import com.wasin.wasin._core.exception.BaseException;
import com.wasin.wasin._core.exception.CustomException;
import com.wasin.wasin._core.util.ApiUtils;
import org.springframework.http.HttpStatus;


// 서버 에러 500
public class ServerException extends CustomException {

    public ServerException(BaseException exception){
        super(exception.getMessage());
    }

    @Override
    public ApiUtils.ApiResult<?> body() {
        return ApiUtils.error(getMessage(), status());
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
