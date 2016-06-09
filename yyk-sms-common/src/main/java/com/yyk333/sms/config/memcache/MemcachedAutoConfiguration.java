/*
 * Copyright 2016 Brian Matthews
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yyk333.sms.config.memcache;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import com.yyk333.sms.utils.PropertiesUtil;

import net.spy.memcached.MemcachedClient;

@Configuration
public class MemcachedAutoConfiguration {

    private static final String LOCALHOST = "1f9dd6492b60415a.m.cnbjalicm12pub001.ocs.aliyuncs.com";

    private static final int DEFAULT_PORT = 11211;

    @Bean
    public MemcachedClient memcachedClient() throws IOException {
        final List<InetSocketAddress> addresses = new ArrayList<InetSocketAddress>();
        final String servers = PropertiesUtil.getProperty("memcached.servers");
        if (StringUtils.isEmpty(servers)) {
            addresses.add(new InetSocketAddress(LOCALHOST, DEFAULT_PORT));
        } else {
            for (final String server : servers.split(",")) {
                final int colon = server.indexOf(":");
                if (colon == -1) {
                    addresses.add(new InetSocketAddress(server, DEFAULT_PORT));
                } else {
                    final int port = Integer.parseInt(server.substring(colon + 1));
                    addresses.add(new InetSocketAddress(server.substring(0, colon), port));
                }
            }
        }
        return new MemcachedClient(addresses);
    }
}
