package com.activequant.transport;

import java.util.Map;

public interface IPublisher {

    public void send(Map<String, Object> message) throws Exception;

}
