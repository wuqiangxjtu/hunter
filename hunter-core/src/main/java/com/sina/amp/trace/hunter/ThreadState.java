package com.sina.amp.trace.hunter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Stack;

import com.twitter.zipkin.gen.Endpoint;
import com.twitter.zipkin.gen.Span;

public class ThreadState {
	
	private final Stack<Span> spanStack = new Stack<Span>();
   
    private final Endpoint endpoint;
    
    public ThreadState(Endpoint endpoint) {
    	this.endpoint = endpoint;
    }

    public ThreadState(final String ip, final int port, final String serviceName) {
    	this.endpoint = new Endpoint(ipAddressToInt(ip), (short)port, serviceName);
    }
    
    public Endpoint getEndpoint() {
    	return endpoint;
    }

    public void push(Span span) {
    	spanStack.push(span);
    }
    
    public Span pop(){
    	return spanStack.pop();
    }
    
    public Span peek() {
    	return spanStack.peek();
    }
    
    private static int ipAddressToInt(final String ip) {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(ip);
        } catch (final UnknownHostException e) {
            throw new IllegalArgumentException(e);
        }
        return ByteBuffer.wrap(inetAddress.getAddress()).getInt();
    }
    	
}
