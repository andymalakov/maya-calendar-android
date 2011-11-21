package org.maya.calendar;

import java.util.Calendar;

import org.maya.calendar.model.MayaCalendar;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

public class CalendarImageHelper {
	static final int [] NUMBER_IMAGES = {
		R.drawable.d0,
		R.drawable.d1,
		R.drawable.d2,
		R.drawable.d3,
		R.drawable.d4,
		R.drawable.d5,
		R.drawable.d6,
		R.drawable.d7,
		R.drawable.d8,
		R.drawable.d9,
		R.drawable.d10,
		R.drawable.d11,
		R.drawable.d12,
		R.drawable.d13,
		R.drawable.d14,
		R.drawable.d15,
		R.drawable.d16,
		R.drawable.d17,
		R.drawable.d18,
		R.drawable.d19
	};
	
	static final int [] PERIOD_GLYPH_ICONS = {
		R.id.baktun_sign,
		R.id.katun_sign,
		R.id.tun_sign,
		R.id.winal_sign,
		R.id.kin_sign
	};
	
	static final int [] PERIOD_SYMBOLIC_IMAGES_SMALL = {
		R.drawable.baktun_s64,
		R.drawable.katun_s64,
		R.drawable.tun_s64,
		R.drawable.winal_s64,
		R.drawable.kin_s64,
	};
	
	static final int [] PERIOD_FACE_IMAGES_SMALL = {
		R.drawable.baktun64,
		R.drawable.katun64,
		R.drawable.tun64,
		R.drawable.winal64,
		R.drawable.kin64,
	};
	
	static final int [] PERIOD_FACE_IMAGES_LARGE = {
		R.drawable.baktun,
		R.drawable.katun,
		R.drawable.tun,
		R.drawable.winal,
		R.drawable.kin,
	};

	static final int [] PERIOD_NAMES = {
		R.string.baktun,
		R.string.katun,
		R.string.tun,
		R.string.winal,
		R.string.kin,
	};

	private final Activity activity;
    private MayaCalendar mc = MayaCalendar.create(MayaCalendar.GMT_CORRELATION);
	
	CalendarImageHelper (Activity activity) {
		this.activity = activity;
	}
	
	void displayDate (Calendar date) {
        mc.set(date);
        refresh();
	}   
	
	void displayLongCount (String longCountDate) {
        mc.parseLongCountDate(longCountDate);
        refresh();
	}   

	private void refresh() {
        TextView tv = (TextView) activity.findViewById(R.id.banner);
        tv.setText (mc.toGregorianString() + " in Maya Calendar is:"); //TODO: Localize
        
        int [] lc = mc.toLongCount();
        
        updateGlyph (lc[0], R.id.baktun_count, R.id.baktunText, R.string.baktunText);
        updateGlyph (lc[1], R.id.katun_count,  R.id.katunText, R.string.katunText);
        updateGlyph (lc[2], R.id.tun_count,    R.id.tunText, R.string.tunText);
        updateGlyph (lc[3], R.id.winal_count,  R.id.winalText, R.string.winalText);
        updateGlyph (lc[4], R.id.kin_count,    R.id.kinText, R.string.kinText);
	}
	
	private void updateGlyph (int count, int imageId, int textId, int textString) {
		assert count >= 0 && count < 20;
		ImageView iv = (ImageView) activity.findViewById(imageId);
		int numberImageId = CalendarImageHelper.NUMBER_IMAGES [count];
		iv.setImageResource(numberImageId);
		
		TextView tv = (TextView) activity.findViewById(textId);
		tv.setText ( count + " " + activity.getString(textString));
	}
	
	void setFaceGlyphs (boolean enable) {
		for (int i = 0; i < PERIOD_GLYPH_ICONS.length; i++) {
			int imageViewId = PERIOD_GLYPH_ICONS[i];
			ImageView iv = (ImageView) activity.findViewById(imageViewId);
			int drawableId;
			if (enable)
				drawableId = PERIOD_FACE_IMAGES_SMALL[i];
			else
				drawableId = PERIOD_SYMBOLIC_IMAGES_SMALL[i];

			iv.setImageResource(drawableId);
		}
	}
	
	int getCorrelationNumber () {
		return mc.getCorrelationNumber();
	}

	String getLongCountString() {
		return mc.toLongCountString();
	}
	
	public void setCorrelationNumber(int newCorrelationNumber) {
		Calendar currentDate = mc.toGregorianDate();
		mc = MayaCalendar.create(newCorrelationNumber);
		displayDate(currentDate);
	}

	static int findSmallFacePeriodImage(int id) {
		for (int i=0; i < PERIOD_GLYPH_ICONS.length; i++)
			if (PERIOD_GLYPH_ICONS [i] == id)
				return i;
		return -1;
	}

}
