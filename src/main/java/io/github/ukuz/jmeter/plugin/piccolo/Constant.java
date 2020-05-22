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

import org.apache.jmeter.testelement.TestElement;

/**
 * @author ukuz90
 */
public class Constant {

    private Constant() {}

    public static final String FIELD_WEBSOCKET_URL = "FIELD_WEBSOCKET_URL";
    public static final String FIELD_DISPATCH_ROUTE_KEY = "FIELD_DISPATCH_ROUTE_KEY";
    public static final String FIELD_DISPATCH_PAYLOAD = "FIELD_DISPATCH_PAYLOAD";
    public static final String FIELD_DISPATCH_PAYLOAD_FILE = "FIELD_DISPATCH_PAYLOAD_FILE";
    public static final String FIELD_BIND_USER_ID_PREFIX = "FIELD_BIND_USER_ID_PREFIX";
    public static final String FIELD_BIND_USER_ID = "FIELD_BIND_USER_ID";

    public static String getWebSocketUrl(TestElement element) {
        return element.getPropertyAsString(FIELD_WEBSOCKET_URL);
    }

    public static void setWebSocketUrl(TestElement element, String websocketUrl) {
        element.setProperty(FIELD_WEBSOCKET_URL, websocketUrl);
    }

    public static String getDispatchRouteKey(TestElement element) {
        return element.getPropertyAsString(FIELD_DISPATCH_ROUTE_KEY);
    }

    public static void setDispatchRouteKey(TestElement element, String routeKey) {
        element.setProperty(FIELD_DISPATCH_ROUTE_KEY, routeKey);
    }

    public static String getDispatchPayload(TestElement element) {
        return element.getPropertyAsString(FIELD_DISPATCH_PAYLOAD);
    }

    public static void setDispatchPayload(TestElement element, String payload) {
        element.setProperty(FIELD_DISPATCH_PAYLOAD, payload);
    }

    public static String getDispatchPayloadFile(TestElement element) {
        return element.getPropertyAsString(FIELD_DISPATCH_PAYLOAD_FILE);
    }

    public static void setDispatchPayloadFile(TestElement element, String payloadFile) {
        element.setProperty(FIELD_DISPATCH_PAYLOAD_FILE, payloadFile);
    }

    public static String getBindUserIdPrefix(TestElement element) {
        return element.getPropertyAsString(FIELD_BIND_USER_ID_PREFIX);
    }

    public static void setBindUserIdPrefix(TestElement element, String bindUserIdPrefix) {
        element.setProperty(FIELD_BIND_USER_ID_PREFIX, bindUserIdPrefix);
    }

    public static String getBindUserId(TestElement element) {
        return element.getPropertyAsString(FIELD_BIND_USER_ID);
    }

    public static void setBindUserId(TestElement element, String bindUserId) {
        element.setProperty(FIELD_BIND_USER_ID, bindUserId);
    }

}
