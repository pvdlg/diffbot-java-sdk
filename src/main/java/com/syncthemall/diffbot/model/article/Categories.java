/**
 * The MIT License
 * Copyright (c) 2013 Pierre-Denis Vanduynslager
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.syncthemall.diffbot.model.article;

import java.io.Serializable;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 * Categories element extracted from an {@link Article} by Diffbot (Article API).
 * 
 * @author Pierre-Denis Vanduynslager <pierre.denis.vanduynslager@gmail.com>
 */
public final class Categories extends GenericJson implements Serializable {

	/** Serial code version <code>serialVersionUID</code>. **/
	private static final long serialVersionUID = 2507981859510362861L;

	@Key(value = "entertainment_culture")
	private float entertainmentCulture;
	@Key(value = "hospitality_recreation")
	private float hospitalityRecreation;
	@Key
	private float other;
	@Key(value = "business_finance")
	private float businessFinance;
	@Key(value = "technology_internet")
	private float technologyInternet;
	@Key(value = "socialissues")
	private float socialIssues;
	@Key
	private float sports;
	@Key(value = "humaninterest")
	private float humanInterest;
	@Key(value = "religion_belief")
	private float religionBelief;
	@Key(value = "war_conflict")
	private float warConflict;
	@Key
	private float education;
	@Key(value = "health_medical_pharma")
	private float healthMedicalPharma;
	@Key
	private float labor;
	@Key(value = "law_crime")
	private float lawCrime;
	@Key
	private float politics;
	@Key
	private float environment;
	@Key
	private float weather;
	@Key(value = "disaster_accident")
	private float disasterAccident;

	/**
	 * @return score for entertainment_culture
	 */
	public float getEntertainmentCulture() {
		return entertainmentCulture;
	}

	/**
	 * @return score for hospitality_recreation
	 */
	public float getHospitalityRecreation() {
		return hospitalityRecreation;
	}

	/**
	 * @return score for other
	 */
	public float getOther() {
		return other;
	}

	/**
	 * @return score for business_finance
	 */
	public float getBusinessFinance() {
		return businessFinance;
	}

	/**
	 * @return score for technology_internet
	 */
	public float getTechnologyInternet() {
		return technologyInternet;
	}

	/**
	 * @return score for socialissues
	 */
	public float getSocialIssues() {
		return socialIssues;
	}
	/**
	 * @return score for sports
	 */
	public float getSports() {
		return sports;
	}
	/**
	 * @return score for humaninterest
	 */
	public float getHumanInterest() {
		return humanInterest;
	}
	/**
	 * @return score for religion_belief
	 */
	public float getReligionBelief() {
		return religionBelief;
	}
	/**
	 * @return score for war_conflict
	 */
	public float getWarConflict() {
		return warConflict;
	}
	/**
	 * @return score for education
	 */
	public float getEducation() {
		return education;
	}
	/**
	 * @return score for health_medical_pharma
	 */
	public float getHealthMedicalPharma() {
		return healthMedicalPharma;
	}
	/**
	 * @return score for labor
	 */
	public float getLabor() {
		return labor;
	}
	/**
	 * @return score for law_crime
	 */
	public float getLawCrime() {
		return lawCrime;
	}
	/**
	 * @return score for politics
	 */
	public float getPolitics() {
		return politics;
	}
	/**
	 * @return score for environment
	 */
	public float getEnvironment() {
		return environment;
	}
	/**
	 * @return score for weather
	 */
	public float getWeather() {
		return weather;
	}
	/**
	 * @return score for disaster_accident
	 */
	public float getDisasterAccident() {
		return disasterAccident;
	}

	@Override
	public String toString() {
		return "Categories - " + super.toString();
	}
}