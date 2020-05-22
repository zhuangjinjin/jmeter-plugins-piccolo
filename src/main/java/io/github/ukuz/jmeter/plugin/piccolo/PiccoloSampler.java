/*
 * Copyright 2020 ukuz90
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.ukuz.jmeter.plugin.piccolo;

import io.github.ukuz.piccolo.api.connection.Connection;
import io.github.ukuz.piccolo.client.websocket.WebSocketClient;
import io.github.ukuz.piccolo.client.websocket.WebSocketClientHandler;
import io.github.ukuz.piccolo.common.message.BindUserMessage;
import io.github.ukuz.piccolo.common.message.DispatcherMessage;
import io.github.ukuz.piccolo.common.message.HandshakeOkMessage;
import io.github.ukuz.piccolo.common.message.push.DispatcherMqMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.Interruptible;
import org.apache.jmeter.samplers.SampleResult;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ukuz90
 */
@Slf4j
public class PiccoloSampler extends AbstractSampler implements Interruptible, WebSocketClientHandler.BaseHandler {

    private boolean bind;
    private volatile Connection connection;
    private String userId;
    private static AtomicInteger userCount = new AtomicInteger();
    private int cnt;

    public PiccoloSampler() {
        cnt = userCount.incrementAndGet();
    }

    @Override
    public boolean interrupt() {
        Thread.currentThread().interrupt();
        return true;
    }

    @Override
    public SampleResult sample(Entry entry) {
        String userId = Constant.getBindUserId(this);
        if (userId == null || userId.isEmpty()) {
            String userIdPrefix = Constant.getBindUserIdPrefix(this);
            userId = userIdPrefix + cnt;
        }
        Connection connection = getConnection();
        if (!bind) {
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.info("bindUser success");
        DispatcherMessage message = new DispatcherMessage(connection);
        message.routeKey = Constant.getDispatchRouteKey(this);
//        message.payload =
        return null;
    }

    private Connection getConnection() {
        if (connection == null) {
            synchronized (this) {
                if (connection == null) {
                    String wsUrl = Constant.getWebSocketUrl(this);
                    log.info("wsUrl: {}", wsUrl);
                    URI uri = null;
                    try {
                        uri = new URI(wsUrl);
                        WebSocketClient webSocketClient = new WebSocketClient(uri);
                        webSocketClient.setClientHandler(this);
                        connection = webSocketClient.start(uri);

                    } catch (URISyntaxException | InterruptedException e) {
                        log.error("runtime error: ", e);
                    }


                }
            }
        }
        return connection;
    }

    @Override
    public void handle(Object msg) {
        if (msg instanceof HandshakeOkMessage) {
            bindUser();
        } else if (msg instanceof BindUserMessage) {
            bind = true;
        }
    }

    private void bindUser() {
        BindUserMessage msg = new BindUserMessage(connection);
        msg.userId = userId;
        msg.tags = "";
        connection.sendAsync(msg);
    }

}
