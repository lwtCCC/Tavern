package com.lwt.tavern.common;

import com.lwt.tavern.dto.shop.ShopWithUpdateDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result{
    private Boolean success;
    private String errorMsg;
    private Object data;

    /**
     * 成功返回消息
     * @return
     */
    public static Result success(){
        return new Result(true,null,null);
    }

    /**
     * 成功返回附带数据的消息
     * @param data 返回的数据
     * @return
     */
    public static Result success(Object data){
        return new Result(true,null,data);
    }

    /**
     * 失败返回消息
     * @param errorMsg 失败的消息
     * @return
     */
    public static Result fail(String errorMsg){
        return new Result(false,errorMsg,null);
    }

}
