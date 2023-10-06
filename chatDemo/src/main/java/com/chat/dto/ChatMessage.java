package com.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class ChatMessage {
    //메세지 타입 : 입장, 채팅
    public enum MessageType{
        ENTER, TALK
    }

}
