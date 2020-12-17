package edu.uestc.cv.servlet;

import edu.uestc.cv.util.WebSocketUtil;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


public class WebSocketServer extends WebSocketServlet {
    public final Set<WebSocketUtil> users = new CopyOnWriteArraySet<>();
    @Override
    protected StreamInbound createWebSocketInbound(String s, HttpServletRequest httpServletRequest) {
        return new WebSocketUtil(users);
    }
}