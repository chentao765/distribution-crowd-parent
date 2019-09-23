package cn.ct.cloud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResultDTO<T>{
    private String result;
    private String message;
    private T data;

    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";
    public static final String NO_MSG = "NO_MSG";
    public static final String NO_DATA = "NO_DATA";

    public static ResultDTO<String> successNoData() {
        return new ResultDTO<>(SUCCESS, NO_MSG, NO_DATA);
    }

    public static <T> ResultDTO<T> successWithData(T data) {
        return new ResultDTO<>(SUCCESS, NO_MSG, data);
    }

    public static <T> ResultDTO<T> failed(String message) {
        return new ResultDTO<>(FAILED, message, null);
    }



}
