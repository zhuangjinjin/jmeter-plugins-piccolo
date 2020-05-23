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
import io.github.ukuz.piccolo.common.message.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.Interruptible;
import org.apache.jmeter.samplers.SampleResult;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * @author ukuz90
 */
@Slf4j
public class PiccoloSampler extends AbstractSampler implements Interruptible, WebSocketClientHandler.BaseHandler {

    private boolean bind;
    private volatile Thread currentThread;
    private volatile Connection connection;
    private String userId;
    private static AtomicInteger userCount = new AtomicInteger();
    private int cnt;
    private byte[] ret;

    public PiccoloSampler() {
        cnt = userCount.incrementAndGet();
        log.info("------------------cnt:{}", cnt);
    }

    @Override
    public boolean interrupt() {
        Thread.currentThread().interrupt();
        return true;
    }

    @Override
    public SampleResult sample(Entry entry) {
        ret = null;
        prepare();
        SampleResult result = new SampleResult();
        result.setSampleLabel(getName());
        invokeDispatchMessage(result);
        result.setDataType(SampleResult.TEXT);
        result.setResponseData(ret);
        if (ret != null) {
            result.setSuccessful(true);
        }
        return result;
    }

    private void prepare() {
        userId = Constant.getBindUserId(this);
        if (StringUtils.isBlank(userId)) {
            String userIdPrefix = Constant.getBindUserIdPrefix(this);
            userId = userIdPrefix + cnt;
        }
        log.debug("userId: {}", userId);
        getConnection();
        if (!bind) {
           waitForResult();
        }
        log.info("bind user success");
    }

    private void invokeDispatchMessage(SampleResult result) {
        DispatcherMessage message = new DispatcherMessage(connection);
        message.routeKey = Constant.getDispatchRouteKey(this);
        String payloadFile = Constant.getDispatchPayloadFile(this);
        try {
            if (StringUtils.isNotBlank(payloadFile)) {
                message.payload = FileUtils.readFileToByteArray(new File(payloadFile));
            } else {
                log.warn("payload file can not empty");
            }
        } catch (IOException e) {
            log.error("read payload file error", e);
        }
        result.sampleStart();
        connection.sendSync(message);
        waitForResult();
        result.sampleEnd();
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
            log.debug("handshake ok, msg: {}", msg);
            bindUser();
        } else if (msg instanceof OkMessage) {
            log.debug("bind user ok, msg: {}", msg);
            obtainResult();
            bind = true;
        } else if (msg instanceof DispatcherResponseMessage) {
            log.debug("receive dispatch msg: {}", msg);
            ret = ((DispatcherResponseMessage)msg).payload;
            obtainResult();
        }
    }

    private void bindUser() {
        BindUserMessage msg = new BindUserMessage(connection);
        msg.userId = userId;
        msg.tags = "";
        connection.sendAsync(msg);
        log.debug("bind user start");
    }

    @Override
    public void clear() {
        super.clear();
        log.info("clear");
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    private void waitForResult() {
        currentThread = Thread.currentThread();
        LockSupport.park();
    }

    private void obtainResult() {
        if (currentThread != null) {
            LockSupport.unpark(currentThread);
            currentThread = null;
        }
    }
}
