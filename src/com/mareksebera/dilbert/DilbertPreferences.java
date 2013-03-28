package com.mareksebera.dilbert;

import org.joda.time.DateMidnight;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DilbertPreferences {

	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;

	private static final String PREF_CURRENT_DATE = "dilbert_current_date";
	private static final String PREF_CURRENT_URL = "dilbert_current_url";
	private static final String PREF_HIGH_QUALITY_ENABLED = "dilbert_use_high_quality";

	public static final DateTimeFormatter dateFormatter = DateTimeFormat
			.forPattern("yyyy-MM-dd");

	public DilbertPreferences(Context _context) {
		preferences = PreferenceManager.getDefaultSharedPreferences(_context);
		editor = preferences.edit();
	}

	public DateMidnight getCurrentDate() {
		String savedDate = preferences.getString(PREF_CURRENT_DATE, null);
		if (savedDate == null)
			return DateMidnight.now();
		else
			return DateMidnight.parse(savedDate, dateFormatter);
	}

	public boolean saveCurrentDate(DateMidnight currentDate) {
		editor.putString(PREF_CURRENT_DATE,
				currentDate.toString(DilbertPreferences.dateFormatter));
		return editor.commit();
	}

	public String getLastUrl() {
		return preferences.getString(PREF_CURRENT_URL, null);
	}

	public boolean isHighQualityOn() {
		return preferences.getBoolean(PREF_HIGH_QUALITY_ENABLED, true);
	}

	public boolean saveCurrentUrl(String date, String s) {
		editor.putString(PREF_CURRENT_URL, s);
		editor.putString(date, s);
		return editor.commit();
	}

	public boolean toggleHighQuality() {
		return editor.putBoolean(PREF_HIGH_QUALITY_ENABLED, !isHighQualityOn())
				.commit();
	}

	public String loadCachedUrl(String dateKey) {
		return preferences.getString(dateKey, null);
	}

	public boolean removeCache(DateMidnight currentDate) {
		return editor.remove(
				currentDate.toString(DilbertPreferences.dateFormatter))
				.commit();
	}

}
