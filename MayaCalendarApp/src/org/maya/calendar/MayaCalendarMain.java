package org.maya.calendar;

import java.util.Calendar;

import org.maya.calendar.R.id;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

public class MayaCalendarMain extends Activity implements View.OnClickListener {
	
	
	////////////////////////////////////////////////////////
	// Model
	private final Calendar calendar = Calendar.getInstance();
	private final CalendarImageHelper calendarHelper;
	private boolean showFace = true;
	
	public MayaCalendarMain () {
		calendarHelper = new CalendarImageHelper (this);
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    	
        calendarHelper.displayDate (calendar);
        
        Button changeDateGregorianBtn = (Button) this.findViewById(R.id.changeDateGregorianBtn);
        changeDateGregorianBtn.setOnClickListener(new View.OnClickListener () {
            public void onClick(View v) {
            	showDialog(CHANGE_DATE_DIALOG);
            }
        });
        
        Button changeDateMayaBtn = (Button) this.findViewById(R.id.changeDateMayaBtn);
        changeDateMayaBtn.setOnClickListener(new View.OnClickListener () {
            public void onClick(View v) {
            	setLongCountDateDialog();
            }
        });        
        
		for (int i = 0; i < CalendarImageHelper.PERIOD_GLYPH_ICONS.length; i++) {
			int imageViewId = CalendarImageHelper.PERIOD_GLYPH_ICONS[i];
			ImageView iv = (ImageView) findViewById(imageViewId);
			iv.setOnClickListener(this);
		}
    	findViewById(id.banner).setOnClickListener(this);		
        
    }

    public void onClick(View v) {
    	if (v instanceof ImageView)
    		zoomGlyph ((ImageView)v);
    	else
    		toggleFace ();
    }

    
	private void toggleFace() {
    	showFace = ! showFace;
    	calendarHelper.setFaceGlyphs(showFace);
	}

	private void zoomGlyph(View v) {
		int period = CalendarImageHelper.findSmallFacePeriodImage(v.getId());
		if (period != -1) {
			LayoutInflater inflater = getLayoutInflater();
			View layout = inflater.inflate(R.layout.zoom_glyph,
			                               (ViewGroup) findViewById(R.id.zoom_glyph_layout));
	
			ImageView image = (ImageView) layout.findViewById(R.id.glyphZoom);
			image.setImageResource(CalendarImageHelper.PERIOD_FACE_IMAGES_LARGE[period]);
			TextView text = (TextView) layout.findViewById(R.id.glyphDesc);
			text.setText( getString(CalendarImageHelper.PERIOD_NAMES[period]) );
	
			Toast toast = new Toast(getApplicationContext());
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.setView(layout);
			toast.show();
		}
	}


	////////////////////////////////////////////////////////
    /// Menu

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
//			case R.id.changeDate: 
//				showDialog(CHANGE_DATE_DIALOG);
//				return true;
			case R.id.about: 
				showAbout();
				return true;
			case R.id.setCorrelationDate:
				changeCorrelationDate();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}


	
	
	
	////////////////////////////////////////////////////////
	/// Dialogs
	
//	private static final int ABOUT_DIALOG = 1;
	private static final int CHANGE_DATE_DIALOG = 2;
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case CHANGE_DATE_DIALOG:
				return setDate( new DatePickerDialog(this,  mDateSetListener,  2010, 10, 27)); 
				
		}
		return super.onCreateDialog(id);
	}

	private Dialog setDate(DatePickerDialog datePickerDialog) {
		datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		return datePickerDialog;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
			case CHANGE_DATE_DIALOG:
				 setDate((DatePickerDialog)dialog); break;
		}
		
		super.onPrepareDialog(id, dialog);
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
	
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			//TODO: Localize date format
			String date_selected = String.valueOf(monthOfYear+1)+" /"+String.valueOf(dayOfMonth)+" /"+String.valueOf(year);
			Toast.makeText(MayaCalendarMain.this, "Selected "+date_selected, Toast.LENGTH_SHORT).show();
			calendar.set (Calendar.YEAR, year);
			calendar.set (Calendar.MONTH, monthOfYear);
			calendar.set (Calendar.DAY_OF_MONTH, dayOfMonth);
			calendarHelper.displayDate(calendar);
		}
	};
	
	
	private void showAbout() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle (R.string.aboutTitle);
		builder.setMessage("Maya Calendar freeware app is written by Andy Malakov"); //TODO: HTML
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		
		builder.create().show();
	}

	private void setLongCountDateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle (R.string.pickLongCountDateTitle);
		builder.setMessage("Format: N.N.N.N.N"); 
		
		final EditText input = new EditText(this);
		input.setText(calendarHelper.getLongCountString(), TextView.BufferType.NORMAL);
		builder.setView(input);

		
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String longCountString = input.getText().toString();
				
				Toast.makeText(MayaCalendarMain.this, "Selected "+longCountString, Toast.LENGTH_SHORT).show();
				calendarHelper.displayLongCount(longCountString);
			}
		});
		
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			  public void onClick(DialogInterface dialog, int whichButton) {
			    // Canceled.
			  }
		});

		builder.create().show();
		
	}
	
	
	// Activities
	
	private void changeCorrelationDate () {
		final String SEPARATOR = " - ";
		// Expecting that items have format "number - description"
		int correlationNumber = calendarHelper.getCorrelationNumber();
		String itemPrefix = String.valueOf(correlationNumber) + SEPARATOR;
		
		int selectedItem = -1;
		final CharSequence [] items = getResources().getTextArray(R.array.correlationConstantsList); 
		for (int i = 0; i < items.length; i++) {
			CharSequence item = items [i];
			if (item.toString().startsWith(itemPrefix)) { //TODO: doesn't work?
				selectedItem = i;
				break;
			}
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.pickCorrelationDateTitle);
		builder.setSingleChoiceItems(R.array.correlationConstantsList, selectedItem, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int newSelectedItem) {
		        if (newSelectedItem != -1) {
		        	String s = String.valueOf(items[newSelectedItem]);
		        	int newCorrelationNumber = Integer.parseInt(s.substring (0, s.indexOf (SEPARATOR)));
			    	
		        	Toast.makeText(getApplicationContext(), "Selected " + newCorrelationNumber, Toast.LENGTH_SHORT).show();
		        	calendarHelper.setCorrelationNumber (newCorrelationNumber);
		        }
		    	
		        dialog.dismiss();
		    }
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
    
}