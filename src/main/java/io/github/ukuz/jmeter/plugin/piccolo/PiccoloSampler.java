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
import io.github.ukuz.piccolo.common.message.HandshakeOkMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.Interruptible;
import org.apache.jmeter.samplers.SampleResult;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author ukuz90
 */
@Slf4j
public class PiccoloSampler extends AbstractSampler implements Interruptible, WebSocketClientHandler.BaseHandler {

    private boolean handshake;
    private boolean bind;
    private volatile Connection connection;

    public PiccoloSampler() {
    }

    @Override
    public boolean interrupt() {
        Thread.currentThread().interrupt();
        return true;
    }

    @Override
    public SampleResult sample(Entry entry) {
        handshake();
        return null;
    }

    private void handshake() {
        if (handshake || bind) {
            return;
        }
    }

    private void bindUser() {
        if (bind) {
            return;
        }
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

        }
    }

//    private WebSocketClient getClient() {
//        if (client == null) {
//            synchronized (this) {
//                if (client == null) {
//                    String wsUrl = Constant.getWebSocketUrl(this);
//                    log.info("wsUrl: {}", wsUrl);
//                    URI uri = null;
//                    try {
//                        uri = new URI(wsUrl);
//                    } catch (URISyntaxException e) {
//                        log.error("uri error", e);
//                    }
//                    client = new WebSocketClient(uri) {
//                        @Override
//                        public void onOpen(ServerHandshake handshakedata) {
//
//                        }
//
//                        @Override
//                        public void onMessage(String message) {
//
//                        }
//
//                        @Override
//                        public void onClose(int code, String reason, boolean remote) {
//
//                        }
//
//                        @Override
//                        public void onError(Exception ex) {
//
//                        }
//                    };
//                }
//            }
//        }
//        return client;
//    }
}
