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

    public static String getWebSocketUrl(TestElement element) {
        return element.getPropertyAsString(FIELD_WEBSOCKET_URL);
    }

    public static void setWebSocketUrl(TestElement element, String websocketUrl) {
        element.setProperty(FIELD_WEBSOCKET_URL, websocketUrl);
    }

}
