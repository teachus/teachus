/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.teachus.utils;

public final class ClassUtils {
	
	public static ClassUtils INSTANCE = new ClassUtils();
	
	private ClassUtils() {
	}

	public static String getAsResourcePath(Class<?> clazz, String resourceName) {
		String resourcePath = clazz.getPackage().getName();
		resourcePath = resourcePath.replace(".", "/");
		resourcePath += "/";
		resourcePath += resourceName;
		
		return resourcePath;
	}
	
	public static String getAsResourceBundlePath(Class<?> clazz, String bundleName) {
		String resourceBundlePath = clazz.getPackage().getName();
		resourceBundlePath += ".";
		resourceBundlePath += bundleName;
		
		return resourceBundlePath;
	}
	
}
