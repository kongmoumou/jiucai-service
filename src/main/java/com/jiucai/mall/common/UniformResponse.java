package com.jiucai.mall.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.io.Serializable;
import java.util.Map;

@Getter
public class UniformResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int status;
    private String msg;
    private T data;

    /**
     * 统一的响应消息
     * @param status
     */
    private UniformResponse(int status){
        this.status = status;
    }

    private UniformResponse(int status, String msg){
        this.status = status;
        this.msg = msg;
    }

    private UniformResponse(int status, String msg, Map data) {
        this.status = status;
        this.msg = msg;
        this.data = (T) data;
    }

    private UniformResponse(int status, T data) {
        this.status = status;
        this.data =  data;
    }

    private UniformResponse(int status, String msg, T data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    /**
     * Success
     * @param <T>
     * @return
     */
    public static <T> UniformResponse<T> ResponseForSuccess(){
        return new UniformResponse<T>(ResponseStatusCode.SUCCESS.getCode(),ResponseStatusCode.SUCCESS.getDescription());
    }


    public static <T> UniformResponse<T> ResponseForSuccess(String msg){
        return new UniformResponse<T>(ResponseStatusCode.SUCCESS.getCode(),msg);
    }

    public static <T> UniformResponse<T> ResponseForSuccess(String msg,Map data){
        return new UniformResponse<T>(ResponseStatusCode.SUCCESS.getCode(),msg,data);
    }

    public static <T> UniformResponse<T> ResponseForSuccess(T data){
        return new UniformResponse<T>(ResponseStatusCode.SUCCESS.getCode(),data);
    }

    public static <T> UniformResponse<T> ResponseForSuccess(String msg,T data){
        return new UniformResponse<T>(ResponseStatusCode.SUCCESS.getCode(),msg,data);
    }

    /**
     * Fail
     * @param <T>
     * @return
     */
    public static <T> UniformResponse<T> ResponseForFail(){
        return new UniformResponse<T>(ResponseStatusCode.FAIL.getCode(),ResponseStatusCode.FAIL.getDescription());
    }

    public static <T> UniformResponse<T> ResponseForFail(String msg){
        return new UniformResponse<T>(ResponseStatusCode.FAIL.getCode(),msg);
    }

    /**
     * Error
     * @param <T>
     * @return
     */
    public static <T> UniformResponse<T> ResponseForError(){
        return new UniformResponse<T>(ResponseStatusCode.ERROR.getCode(),ResponseStatusCode.ERROR.getDescription());
    }

    public static <T> UniformResponse<T> ResponseForError(String msg){
        return new UniformResponse<T>(ResponseStatusCode.ERROR.getCode(),msg);
    }
    public static <T> UniformResponse<T> ResponseErrorCodeMessage(int code, String msg){
        return new UniformResponse<T>(ResponseStatusCode.NEED_LOGIN.getCode(),msg);
    }

    /**
     * isSuccess
     * @return
     */
    @JsonIgnore
    public boolean isSuccess(){
        return this.status == ResponseStatusCode.SUCCESS.getCode();
    }

}
