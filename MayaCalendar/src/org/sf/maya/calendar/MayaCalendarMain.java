package org.sf.maya.calendar;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

public class MayaCalendarMain extends Activity {
	
	
	////////////////////////////////////////////////////////
	// Model
	private final Calendar calendar = Calendar.getInstance();
	private final CalendarImageHelper calendarHelper;
	
	
	public MayaCalendarMain () {
		calendarHelper = new CalendarImageHelper (this);
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    	
        calendarHelper.displayDate (calendar);
        
        Button changeDateBtn = (Button) this.findViewById(R.id.changeDateBtn);
        changeDateBtn.setOnClickListener(new View.OnClickListener () {
            public void onClick(View v) {
            	showDialog(CHANGE_DATE_DIALOG);
            }
        });

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
		builder.setTitle ("About");
		builder.setMessage("<b>Maya Calendar</b> freeware app is written by Andy Malakov"); //TODO: HTML
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		
		builder.create().show();
		
	}
    
	
    
}