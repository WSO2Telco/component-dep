/*******************************************************************************
 * Copyright  (c) 2017, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package org.wso2telco.dep.notifyeventmediator;

import org.apache.synapse.SynapseException;

/**
 * Convert Stream Properties to appropriate types.
 */
public class PropertyTypeConverter {

	private static String errorMsg = "Cannot convert '%s' to a %s";

	private PropertyTypeConverter() {
		throw new IllegalStateException("Utility class");
	}

	public static int convertToInt(String string) {
		try {
			return Integer.parseInt(string);
		} catch (NumberFormatException e) {
			throw new SynapseException(String.format(errorMsg, string, "integer"), e);
		}
	}

	public static float convertToFloat(String string) {
		try {
			return Float.parseFloat(string);
		} catch (NumberFormatException e) {
			throw new SynapseException(String.format(errorMsg, string, "float"), e);
		}
	}

	public static double convertToDouble(String string) {
		try {
			return Double.parseDouble(string);
		} catch (NumberFormatException e) {
			throw new SynapseException(String.format(errorMsg, string, "double"), e);
		}
	}

	public static long convertToLong(String string) {
		try {
			return Long.parseLong(string);
		} catch (NumberFormatException e) {
			throw new SynapseException(String.format(errorMsg, string, "long"), e);
		}
	}

	public static boolean convertToBoolean(String string) {
		try {
			return Boolean.parseBoolean(string);
		} catch (Exception e) {
			throw new SynapseException(String.format(errorMsg, string, "boolean"), e);
		}
	}
}