package com.example.ramserver.chat;

import com.example.ramserver.model.ChatMessage;
import com.example.ramserver.service.ChatRoomService;
import com.example.ramserver.vo.ChatInsertVo;
import com.example.ramserver.vo.FindChatRoomVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class MessageHandler  {
    @Autowired
    ChatRoomService chatRoomService;
    //hello라는 api로 매핑 ex) client에서 ../hello라는 api로
    //메시지를 보내면 "broadcasting()메소드가 호출됨
    //클라이언트로부터 오는 메시지는 이 메소드의 파라미터와 바인딩
    //리턴값은 Sendto 어노테이션에 mapping되어있는 api를 구독하고 있는 클라이언트들에게 브로드캐스팅
    @MessageMapping("/hello")
    @SendTo("/topic/roomid")
    public ChatMessage broadcasting(ChatMessage message) throws ParseException {
        String Senddate= new SimpleDateFormat((message.getDate())).format(new Date());
        SimpleDateFormat transFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date Regdate=transFormat.parse(Senddate);

        //메시지를 보낸사람과 받는사람을 확인해 채팅의 내용을 DB에 저장한다.
        //메시지의 타입은 일반 텍스트 메시지와 구매신청 메시지로 구분된다.
        FindChatRoomVo findChatRoomVo=new FindChatRoomVo(message.getAuthor(), message.getReceiver());
        ChatInsertVo chatInsertVo=new ChatInsertVo(chatRoomService.GetMessageCount(),chatRoomService.GetChatRoomId(findChatRoomVo).get(0),
                message.getAuthor(), message.getReceiver(), message.getMsg(), Regdate, message.getType());
        chatRoomService.InsertChatMessage(chatInsertVo);
        return message;
    }
}
