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
package dk.teachus.ws.service;


/**
 * Financial service for querying the balance for a given teacher.
 */
public interface FinanceService {
	
	/**
	 * Get the total amount of paid lessons in a given month. This is calculated as lessons which has been held in that
	 * month and which has been paid. This also means that the amount returned might change if a lesson is paid at a
	 * later time.
	 * 
	 * @param teacherUserId
	 *            The teachers username
	 * @param password
	 *            The teachers password
	 * @param year
	 *            The year for which to get the total amount of paid lessons
	 * @param month
	 *            The month for which to get the total amount of paid lessons
	 * @return The total amount of paid lessons in the given month
	 */
	double getIncomeForMonth(String teacherUserId, String password, int year, int month);
	
}
