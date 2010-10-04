package org.sf.maya.calendar;

import java.util.Calendar;

import org.sf.maya.calendar.model.MayaCalendar;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

public class CalendarImageHelper {
	static final int [] NUMBER_IMAGES = {
		R.drawable.n0,
		R.drawable.n1,
		R.drawable.n2,
		R.drawable.n3,
		R.drawable.n4,
		R.drawable.n5,
		R.drawable.n6,
		R.drawable.n7,
		R.drawable.n8,
		R.drawable.n9,
		R.drawable.n10,
		R.drawable.n11,
		R.drawable.n12,
		R.drawable.n13,
		R.drawable.n14,
		R.drawable.n15,
		R.drawable.n16,
		R.drawable.n17,
		R.drawable.n18,
		R.drawable.n19
	};
	
	private final Activity activity;
    private final MayaCalendar mc = MayaCalendar.getThompson();
	
	CalendarImageHelper (Activity activity) {
		this.activity = activity;
	}
	
	void displayDate (Calendar date) {
        mc.set(date);
        
        
        TextView tv = (TextView) activity.findViewById(R.id.banner);
        tv.setText (mc.toGregorianString() + " in Gregorian Calendar is:");
        
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
}
