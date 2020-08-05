/*
 * Copyright (c) 2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * <p>
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wso2telco.dep.validator.handler.utils;

public class SQLConstants {

    private SQLConstants() { }

    public static final String API_INFO_DEFAULT_SQL = "SELECT" +
            "    SUBS.USER_ID, " +
            "    APP.APPLICATION_ID, " +
            "    APP.NAME, " +
            "    APP.APPLICATION_TIER, " +
            "    AKM.KEY_TYPE, " +
            "    API.API_NAME, " +
            "    API.API_PROVIDER " +
            "FROM " +
            "    AM_SUBSCRIBER SUBS, " +
            "    AM_APPLICATION APP, " +
            "    AM_APPLICATION_KEY_MAPPING AKM, " +
            "    AM_API API " +
            "WHERE" +
            "  API.CONTEXT = ? " +
            "  AND AKM.CONSUMER_KEY = ? " +
            "  AND APP.SUBSCRIBER_ID = SUBS.SUBSCRIBER_ID " +
            "  AND AKM.APPLICATION_ID=APP.APPLICATION_ID ";

    public static final String API_INFO_VERSION_SQL = "SELECT" +
            "    SUBS.USER_ID, " +
            "    APP.APPLICATION_ID, " +
            "    APP.NAME, " +
            "    APP.APPLICATION_TIER, " +
            "    AKM.KEY_TYPE, " +
            "    API.API_NAME, " +
            "    API.API_PROVIDER " +
            "FROM" +
            "    AM_SUBSCRIBER SUBS, " +
            "    AM_APPLICATION APP, " +
            "    AM_APPLICATION_KEY_MAPPING AKM, " +
            "    AM_API API " +
            "WHERE" +
            "  API.CONTEXT = ? " +
            "  AND AKM.CONSUMER_KEY = ? " +
            "  AND API.API_VERSION = ? " +
            "  AND APP.SUBSCRIBER_ID = SUBS.SUBSCRIBER_ID " +
            "  AND AKM.APPLICATION_ID=APP.APPLICATION_ID ";
}
