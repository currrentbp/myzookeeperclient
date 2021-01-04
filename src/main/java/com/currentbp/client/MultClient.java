package com.currentbp.client;

import com.currentbp.model.BaseMessage;

/**
 * @author baopan
 * @createTime 20201225
 */
public class MultClient {

    public BaseMessage sendMessage(BaseMessage baseMessage){
        return sendMessage("localhost:8080",baseMessage);
    }
    public BaseMessage sendMessage(String address,BaseMessage baseMessage){

        return new BaseMessage();
    }
}
