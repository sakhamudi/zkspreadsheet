/* FontFamily.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Nov 7, 2010 10:31:11 AM , Created by Sam
}}IS_NOTE

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package org.zkoss.zss.app.zul;

import static org.zkoss.zss.app.base.Preconditions.checkNotNull;

import org.zkoss.poi.ss.usermodel.Cell;
import org.zkoss.poi.ss.usermodel.CellStyle;
import org.zkoss.poi.ss.usermodel.Font;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zss.app.MainWindowCtrl;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.CellEvent;
import org.zkoss.zss.ui.impl.Utils;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;

/**
 * @author Sam
 *
 */
public class FontFamily extends Div implements ZssappComponent, IdSpace{
	
	private final static String URI = "~./zssapp/html/fontFamily.zul";
	
	private Combobox fontfamilyCombobox;
	
	private Spreadsheet ss;
	
	
	public FontFamily() {
		Executions.createComponents(URI, this, null);
		
		Components.wireVariables(this, this, '$', true, true);
		Components.addForwards(this, this, '$');
	}
	
	public void onSelect$fontfamilyCombobox(Event event) {
		String font = fontfamilyCombobox.getSelectedItem().getLabel();
		Events.postEvent(Events.ON_SELECT, this, event.getData());
		MainWindowCtrl.getInstance().setFontFamily(font);
	}
	
	public String getText() {
		return fontfamilyCombobox.getText();
	}
	
	public void setText(String fontFamily) {
		fontfamilyCombobox.setText(fontFamily);
	}

	public void setWidth(String width) {
		fontfamilyCombobox.setWidth(width);
	}

	@Override
	public Spreadsheet getSpreadsheet() {
		return ss;
	}

	@Override
	public void setSpreadsheet(Spreadsheet spreadsheet) {
		ss = checkNotNull(spreadsheet, "Spreadsheet is null");
		initFontFamily();
	}

	private void initFontFamily() {
		ss.addEventListener(org.zkoss.zss.ui.event.Events.ON_CELL_FOUCSED, new EventListener() {
			public void onEvent(Event event) throws Exception {
				CellEvent evt = (CellEvent)event;
				int row = evt.getRow();
				int col = evt.getColumn();

				Cell cell = Utils.getCell(ss.getSelectedSheet(), row, col);
				
				//set to default font family
				fontfamilyCombobox.setText("Calibri");
				
				if (cell != null) {
					CellStyle cellStyle = cell.getCellStyle();
					Font font = ss.getBook().getFontAt(cellStyle.getFontIndex());
					fontfamilyCombobox.setText(font.getFontName());
				}
			}
		});
	}
}