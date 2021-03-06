package org.zkoss.zss.api.impl;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zss.Setup;
import org.zkoss.zss.Util;
import org.zkoss.zss.api.CellOperationUtil;
import org.zkoss.zss.api.Range;
import org.zkoss.zss.api.Ranges;
import org.zkoss.zss.api.model.Book;
import org.zkoss.zss.api.model.Sheet;

/**
 * @author Hawk
 *
 */
public class Issue500Test {
	
	@BeforeClass
	public static void setUpLibrary() throws Exception {
		Setup.touch();
	}
	
	@Before
	public void startUp() throws Exception {
		Setup.pushZssContextLocale(Locale.TAIWAN);
	}
	
	@After
	public void tearDown() throws Exception {
		Setup.popZssContextLocale();
	}
	
	
	@Test
	public void testZSS502_2003(){
		Book book = Util.loadBook(this,"book/502-crossSheetReference.xls");
		Sheet sheet = book.getSheet("cell-reference");
		Range referencingCell = Ranges.range(sheet, "C4");
		assertEquals("=row!A1", referencingCell.getCellEditText());
		assertEquals("The first row is freezed.", referencingCell.getCellFormatText());

		Ranges.range(book.getSheet("row")).deleteSheet();
		
		assertEquals("='#REF'!A1", referencingCell.getCellEditText());
		//because ZSS-474 is unresolved, the assert below won't pass 
		//assertEquals("#REF", referencingCell.getCellFormatText());
	}
	
	@Test
	public void testZSS502_2007(){
		Book book = Util.loadBook(this,"book/502-crossSheetReference.xlsx");
		Sheet sheet = book.getSheet("cell-reference");
		Range referencingCell = Ranges.range(sheet, "C4");
		assertEquals("=row!A1", referencingCell.getCellEditText());
		assertEquals("The first row is freezed.", referencingCell.getCellFormatText());

		Ranges.range(book.getSheet("row")).deleteSheet();
		
		assertEquals("=row!A1", referencingCell.getCellEditText());
		//because ZSS-474 is unresolved, the assert below won't pass 
//		assertEquals("#REF", referencingCell.getCellFormatText());
	}
	
	
	@Test
	public void testZSS502_NonExistingSheet2003(){
		Book book = Util.loadBook(this,"book/blank.xls");
		Sheet sheet = book.getSheetAt(0);
		Range cell = Ranges.range(sheet, "A1");
		cell.setCellEditText("=nonExisted!B1");
		assertEquals("='#REF'!B1", cell.getCellEditText());
		assertEquals("#REF!", cell.getCellFormatText());
	}
	
	@Test
	public void testZSS502_NonExistingSheet2007(){
		Book book = Util.loadBook(this,"book/blank.xlsx");
		Sheet sheet = book.getSheetAt(0);
		Range cell = Ranges.range(sheet, "A1");
		cell.setCellEditText("=nonExisted!B1");
		assertEquals("=nonExisted!B1", cell.getCellEditText());
		assertEquals("#REF!", cell.getCellFormatText());
	}
	
	@Test
	public void testZSS510() {
		Book book = Util.loadBook(this, "book/blank.xlsx");
		Sheet sheet = book.getSheetAt(0);
		Range r = Ranges.range(sheet, "A1");
		r.setCellEditText("Hello");
		CellOperationUtil.applyDataFormat(r, "");
		r.getCellFormatText(); // get text shouldn't cause IndexOutBoundaryException
		assertEquals("General", r.getCellStyle().getDataFormat()); // should get General instead of empty string
	}
	
	@Test
	public void testZSS511_LEFTB() {
		Book book = Util.loadBook(this, "book/511-REPLACEB-LEFTB.xlsx");
		Sheet sheet = book.getSheet("LEFTB");
		
		assertEquals("#VALUE!", Ranges.range(sheet, "A1").getCellFormatText());
		assertEquals("", Ranges.range(sheet, "A2").getCellFormatText());
		assertEquals(" ", Ranges.range(sheet, "A3").getCellFormatText());
		assertEquals("\u5E8A", Ranges.range(sheet, "A4").getCellFormatText());
		assertEquals("\u5E8A ", Ranges.range(sheet, "A5").getCellFormatText());
		assertEquals("\u5E8A\u524D", Ranges.range(sheet, "A6").getCellFormatText());
		assertEquals("\u5E8A\u524D ", Ranges.range(sheet, "A7").getCellFormatText());
		assertEquals("\u5E8A\u524D\u660E", Ranges.range(sheet, "A8").getCellFormatText());
		assertEquals("\u5E8A\u524D\u660E ", Ranges.range(sheet, "A9").getCellFormatText());
		assertEquals("\u5E8A\u524D\u660E\u6708", Ranges.range(sheet, "A10").getCellFormatText());
		assertEquals("\u5E8A\u524D\u660E\u6708 ", Ranges.range(sheet, "A11").getCellFormatText());
		assertEquals("\u5E8A\u524D\u660E\u6708\u5149", Ranges.range(sheet, "A12").getCellFormatText());
		assertEquals("\u5E8A\u524D\u660E\u6708\u5149", Ranges.range(sheet, "A13").getCellFormatText());
		assertEquals("\u5E8A\u524D\u660E\u6708\u5149", Ranges.range(sheet, "A14").getCellFormatText());
		
		assertEquals("#VALUE!", Ranges.range(sheet, "C1").getCellFormatText());
		assertEquals("", Ranges.range(sheet, "C2").getCellFormatText());
		assertEquals("A", Ranges.range(sheet, "C3").getCellFormatText());
		assertEquals("A ", Ranges.range(sheet, "C4").getCellFormatText());
		assertEquals("A\u5E8A", Ranges.range(sheet, "C5").getCellFormatText());
		assertEquals("A\u5E8A ", Ranges.range(sheet, "C6").getCellFormatText());
		assertEquals("A\u5E8A\u524D", Ranges.range(sheet, "C7").getCellFormatText());
		assertEquals("A\u5E8A\u524D ", Ranges.range(sheet, "C8").getCellFormatText());
		assertEquals("A\u5E8A\u524D\u660E", Ranges.range(sheet, "C9").getCellFormatText());
		assertEquals("A\u5E8A\u524D\u660E ", Ranges.range(sheet, "C10").getCellFormatText());
		assertEquals("A\u5E8A\u524D\u660E\u6708", Ranges.range(sheet, "C11").getCellFormatText());
		assertEquals("A\u5E8A\u524D\u660E\u6708 ", Ranges.range(sheet, "C12").getCellFormatText());
		assertEquals("A\u5E8A\u524D\u660E\u6708\u5149", Ranges.range(sheet, "C13").getCellFormatText());
		assertEquals("A\u5E8A\u524D\u660E\u6708\u5149", Ranges.range(sheet, "C14").getCellFormatText());
		
		assertEquals("", Ranges.range(sheet, "E1").getCellFormatText());
	}
	
	@Test
	public void testZSS511_REPLACEB() throws UnsupportedEncodingException {
		Book book = Util.loadBook(this, "book/511-REPLACEB-LEFTB.xlsx");
		Sheet sheet = book.getSheet("REPLACEB");
		assertEquals("A\u6E2C\u660E\u6708\u5149\u662F\u5B57\u4E32", Ranges.range(sheet, "A1").getCellFormatText());
		assertEquals("A\u6E2C\u660E\u6708\u5149 \u5B57\u4E32", Ranges.range(sheet, "A2").getCellFormatText());
		assertEquals("A\u6E2C\u660E\u6708\u5149\u5B57\u4E32", Ranges.range(sheet, "A3").getCellFormatText());
		assertEquals("A\u6E2C\u660E\u6708\u5149 \u4E32", Ranges.range(sheet, "A4").getCellFormatText());
		assertEquals("A\u6E2C\u660E\u6708\u5149\u4E32", Ranges.range(sheet, "A5").getCellFormatText());
		assertEquals("A\u6E2C\u660E\u6708\u5149 ", Ranges.range(sheet, "A6").getCellFormatText());
		assertEquals("A\u6E2C\u660E\u6708\u5149", Ranges.range(sheet, "A7").getCellFormatText());
		assertEquals("A\u6E2C\u660E\u6708\u5149", Ranges.range(sheet, "A8").getCellFormatText());
		
		assertEquals("\u5E8A \u4F60\u597D\u55CE", Ranges.range(sheet, "C1").getCellFormatText());
		assertEquals("\u5E8A \u4F60\u597D\u55CE\u660E\u6708\u5149", Ranges.range(sheet, "C2").getCellFormatText());
		assertEquals("\u5E8A \u4F60\u597D\u55CE \u6708\u5149", Ranges.range(sheet, "C3").getCellFormatText());
		assertEquals("\u5E8A \u4F60\u597D\u55CE\u6708\u5149", Ranges.range(sheet, "C4").getCellFormatText());
		assertEquals("\u5E8A \u4F60\u597D\u55CE \u5149", Ranges.range(sheet, "C5").getCellFormatText());
		assertEquals("\u5E8A \u4F60\u597D\u55CE\u5149", Ranges.range(sheet, "C6").getCellFormatText());
		assertEquals("\u5E8A \u4F60\u597D\u55CE ", Ranges.range(sheet, "C7").getCellFormatText());
		assertEquals("\u5E8A \u4F60\u597D\u55CE", Ranges.range(sheet, "C8").getCellFormatText());
		
		assertEquals("#VALUE!", Ranges.range(sheet, "E1").getCellFormatText());
		assertEquals("#VALUE!", Ranges.range(sheet, "E2").getCellFormatText());
		assertEquals("\u4F60\u597D\u55CE", Ranges.range(sheet, "E3").getCellFormatText());
		assertEquals(" ", Ranges.range(sheet, "E4").getCellFormatText());
		assertEquals("\u6E2C", Ranges.range(sheet, "E5").getCellFormatText());
		assertEquals("\u6E2C\u8A66", Ranges.range(sheet, "E6").getCellFormatText());
		
		assertEquals("\u4F60\u597D\u55CE\u5E8A\u524D\u660E\u6708\u5149", Ranges.range(sheet, "G1").getCellFormatText());
		assertEquals(" \u4F60\u597D\u55CE", Ranges.range(sheet, "G2").getCellFormatText());
		assertEquals("\u5E8A\u4F60\u597D\u55CE\u524D\u660E\u6708\u5149", Ranges.range(sheet, "G3").getCellFormatText());
		assertEquals("\u5E8A \u4F60\u597D\u55CE", Ranges.range(sheet, "G4").getCellFormatText());
		assertEquals("\u5E8A\u524D\u4F60\u597D\u55CE\u660E\u6708\u5149", Ranges.range(sheet, "G5").getCellFormatText());
		assertEquals("\u5E8A\u524D \u4F60\u597D\u55CE", Ranges.range(sheet, "G6").getCellFormatText());
		assertEquals("\u5E8A\u524D\u660E\u4F60\u597D\u55CE\u6708\u5149", Ranges.range(sheet, "G7").getCellFormatText());
		assertEquals("\u5E8A\u524D\u660E \u4F60\u597D\u55CE", Ranges.range(sheet, "G8").getCellFormatText());
		assertEquals("\u5E8A\u524D\u660E\u6708\u4F60\u597D\u55CE\u5149", Ranges.range(sheet, "G9").getCellFormatText());
		
		assertEquals("A \u660E\u6708\u5149", Ranges.range(sheet, "I1").getCellFormatText());
		assertEquals("A \u660E\u6708\u5149\u662F\u5B57\u4E32", Ranges.range(sheet, "I2").getCellFormatText());
		assertEquals("A \u660E\u6708\u5149 \u5B57\u4E32", Ranges.range(sheet, "I3").getCellFormatText());
		assertEquals("A \u660E\u6708\u5149\u5B57\u4E32", Ranges.range(sheet, "I4").getCellFormatText());
		assertEquals("A \u660E\u6708\u5149 \u4E32", Ranges.range(sheet, "I5").getCellFormatText());
		assertEquals("A \u660E\u6708\u5149\u4E32", Ranges.range(sheet, "I6").getCellFormatText());
		
		assertEquals("HelWhatlo World", Ranges.range(sheet, "K1").getCellFormatText());
		assertEquals("HelWhato World", Ranges.range(sheet, "K2").getCellFormatText());
		assertEquals("HelWhat World", Ranges.range(sheet, "K3").getCellFormatText());
		assertEquals("HelWhatWorld", Ranges.range(sheet, "K4").getCellFormatText());
		assertEquals("HelWhatorld", Ranges.range(sheet, "K5").getCellFormatText());
		assertEquals("HelWhatrld", Ranges.range(sheet, "K6").getCellFormatText());
		assertEquals("HelWhatld", Ranges.range(sheet, "K7").getCellFormatText());
		assertEquals("HelWhatd", Ranges.range(sheet, "K8").getCellFormatText());
	}
}
