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

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.runtime.RuntimeSchema;
import lombok.Data;

/**
 * @author ukuz90
 */
public class ProtostuffDemo {

    @Data
    static class User {
        private String userName;
        private int age;
    }

    public static void main(String[] args) {
        User user = new User();
        user.setUserName("zhuangjinjin");
        user.setAge(30);
        RuntimeSchema<User> schema = RuntimeSchema.createFrom(User.class);
        System.out.println(schema.getFieldByNumber(1).name);
        System.out.println(schema.getFieldByNumber(1).type);
        System.out.println(schema.getFieldByNumber(2).name);
        System.out.println(schema.getFieldByNumber(2).type);
        byte[] bytes = ProtostuffIOUtil.toByteArray(user, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
        for (byte b : bytes) {
            System.out.print(b + " ");
        }
        System.out.println(new String(bytes));
    }

}
