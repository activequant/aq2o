package com.activequant.interfaces.transport;

import java.util.Map;

import com.activequant.domainmodel.PersistentEntity;

public interface IPublisher {

    public void send(Map<String, Object> message) throws Exception;
    public void send(PersistentEntity entity) throws Exception;

}
