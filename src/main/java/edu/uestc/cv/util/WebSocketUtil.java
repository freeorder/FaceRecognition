package edu.uestc.cv.util;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class WebSocketUtil extends MessageInbound {//hqc
    public static Set<WebSocketUtil> users = new CopyOnWriteArraySet<WebSocketUtil>();
    private String username;
    public WebSocketUtil(){};
    public WebSocketUtil(Set<WebSocketUtil> users){
        this.users = users;


    }
    //此方法接收浏览器二进制消息
    @Override
    protected void onBinaryMessage(ByteBuffer byteBuffer) throws IOException {

    }

    //接收浏览器文本消息
    @Override
    protected void onTextMessage(CharBuffer charBuffer) throws IOException {

        String message = String.valueOf(charBuffer);
        if(message.contains("userId")) {
           this.username = message.substring(7,message.length());
           users.add(this);
        }
    }

    @Override
    protected void onOpen(WsOutbound outbound) {//hqc 主要
        super.onOpen(outbound);
        users.add(this);

    }

    @Override
    protected void onClose(int status) {
        super.onClose(status);
        users.remove(this);
    }

    //用于发送信息
    public static void sendMessage(String message,String username){//name 一般为学生ID
        for(WebSocketUtil user:users){
            if (user.username.equals(username)){
                try {
                    user.getWsOutbound().writeTextMessage(CharBuffer.wrap(message));
                    user.getWsOutbound().flush();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    protected void onPong(ByteBuffer payload) {
        super.onPong(payload);

    }

    @Override
    public int getReadTimeout() {
        return super.getReadTimeout();
    }
}
