package com.mc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class Invoice {

	/**
	 * @param args first argument after the program are the actual hours to be billed
	 * 
	 * @since
	 */
	public static void main(String[] args) {
		try {
			final Double GERMAN_SALES_TAX = new Double(1.19);

			final Double hours = new Double(args[0]);
			
			final Document document = new Document(PageSize.A4, 80, 50, 50, 50);
			// move file destination into properties file
			PdfWriter.getInstance(document, new FileOutputStream(
					Messages.getString("Invoice.0"))); //$NON-NLS-1$
			document.open();

			// header
			final Paragraph head = new Paragraph(Messages.getString("Invoice.1")); //$NON-NLS-1$
			final Font hfont = head.getFont();
			hfont.setSize(24);
			hfont.setStyle(Font.BOLD);
			document.add(head);

			// header info on the left
			final Paragraph taxinfo = new Paragraph(
					Messages.getString("Invoice.2")); //$NON-NLS-1$
			final Font tfont = taxinfo.getFont();
			tfont.setSize(8);
			taxinfo.setAlignment(Element.ALIGN_RIGHT);
			document.add(taxinfo);

			final Paragraph saddresspara = new Paragraph(
					Messages.getString("Invoice.3")); //$NON-NLS-1$
			final Font font = saddresspara.getFont();
			font.setSize(8);
			document.add(saddresspara);

			final CustomerAddress myCustomerAddress = readProperties(Messages.getString("Invoice.4")); //$NON-NLS-1$
			// read customer information into property file
			document.add(new Paragraph(myCustomerAddress.getCustName() + Messages.getString("Invoice.5") //$NON-NLS-1$
					+ myCustomerAddress.getCustAdress() + Messages.getString("Invoice.6") //$NON-NLS-1$
					+ myCustomerAddress.getCustZip() + Messages.getString("Invoice.7") //$NON-NLS-1$
					+ myCustomerAddress.CustTown + Messages.getString("Invoice.8"))); //$NON-NLS-1$

			final Calendar cal = Calendar.getInstance();

			String[] monthName = { Messages.getString("Invoice.9"), Messages.getString("Invoice.10"), Messages.getString("Invoice.11"), Messages.getString("Invoice.12"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					Messages.getString("Invoice.13"), Messages.getString("Invoice.14"), Messages.getString("Invoice.15"), Messages.getString("Invoice.16"), Messages.getString("Invoice.17"), Messages.getString("Invoice.18"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
					Messages.getString("Invoice.19"), Messages.getString("Invoice.20") }; //$NON-NLS-1$ //$NON-NLS-2$

			final Paragraph subject1 = new Paragraph(
					Messages.getString("Invoice.21")); //$NON-NLS-1$
			final Font s1font = subject1.getFont();
			s1font.setSize(8);
			document.add(subject1);

			Paragraph subject2 = null;
			subject2 = new Paragraph(
					Messages.getString("Invoice.22") //$NON-NLS-1$
							+ (cal.get(Calendar.MONTH) + 1)
							+ Messages.getString("Invoice.23") //$NON-NLS-1$
							+ cal.get(Calendar.YEAR)
							+ Messages.getString("Invoice.24") //$NON-NLS-1$
							+ DateFormat.getDateInstance().format(new Date())
							+ Messages.getString("Invoice.25")); //$NON-NLS-1$
			final Font s2font = subject2.getFont();
			s2font.setSize(8);
			document.add(subject2);

			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(Messages.getString("Invoice.26")); //$NON-NLS-1$
			String month = monthName[cal.get(Calendar.MONTH)];
			stringBuilder.append(month);
			stringBuilder.append(Messages.getString("Invoice.27")); //$NON-NLS-1$
			stringBuilder.append(cal.get(Calendar.YEAR));
			stringBuilder.append(Messages.getString("Invoice.28")); //$NON-NLS-1$
			stringBuilder.append(cal.get(Calendar.MONTH) + 1);
			stringBuilder.append(Messages.getString("Invoice.29")); //$NON-NLS-1$
			stringBuilder.append(cal.get(Calendar.YEAR) + Messages.getString("Invoice.30")); //$NON-NLS-1$

			final Paragraph title = new Paragraph(stringBuilder.toString());
			final Font tifont = title.getFont();
			tifont.setSize(12);
			tifont.setStyle(Font.BOLD);
			document.add(title);

			final Paragraph salut = new Paragraph(
					Messages.getString("Invoice.31") //$NON-NLS-1$
							+ Messages.getString("Invoice.32")); //$NON-NLS-1$
			final Font s3font = salut.getFont();
			s3font.setSize(12);
			document.add(salut);

			// block with the computing data
			final DecimalFormat df = new DecimalFormat();
			df.applyPattern(Messages.getString("Invoice.33")); //$NON-NLS-1$

			final Items invoiceItems = new Items();
			invoiceItems.setHours(hours);
			final Double rate = getActualRateFromProps(Messages.getString("Invoice.4"));
			invoiceItems.setRate(rate);
			invoiceItems.setTax_rate(GERMAN_SALES_TAX);
			invoiceItems.compute();

			PdfPTable table = new PdfPTable(4);

			table.addCell(Messages.getString("Invoice.34")); //$NON-NLS-1$
			table.addCell(Messages.getString("Invoice.35")); //$NON-NLS-1$
			table.addCell(Messages.getString("Invoice.36")); //$NON-NLS-1$
			table.addCell(Messages.getString("Invoice.37")); //$NON-NLS-1$

			float[] widths = new float[] { 0.50f, 0.15f, 0.20f, 0.15f };
			table.setWidths(widths);

			NumberFormat nf = NumberFormat.getCurrencyInstance();

			table.addCell(Messages.getString("Invoice.38")); //$NON-NLS-1$
			table.addCell((df.format(hours)).replace(Messages.getString("Invoice.39"), Messages.getString("Invoice.40"))); //$NON-NLS-1$ //$NON-NLS-2$
			// 42, you know ...
			table.addCell(nf.format(invoiceItems.getRate()));
			PdfPCell myCell = new PdfPCell(new Phrase(nf.format(invoiceItems
					.getNetto())));
			myCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(myCell);

			table.addCell(Messages.getString("Invoice.41")); //$NON-NLS-1$
			table.addCell(Messages.getString("Invoice.42")); //$NON-NLS-1$
			table.addCell(Messages.getString("Invoice.43")); //$NON-NLS-1$
			myCell = new PdfPCell(new Phrase(nf.format(invoiceItems.getTax())));
			myCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(myCell);

			table.addCell(Messages.getString("Invoice.44")); //$NON-NLS-1$
			table.addCell(Messages.getString("Invoice.45")); //$NON-NLS-1$
			table.addCell(Messages.getString("Invoice.46")); //$NON-NLS-1$
			myCell = new PdfPCell(new Phrase(
					nf.format(invoiceItems.getBrutto())));
			myCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(myCell);

			table.setWidthPercentage(100);
			document.add(table);

			final Paragraph target = new Paragraph(
					Messages.getString("Invoice.47") //$NON-NLS-1$
							+ computeTagetMonth(cal.get(Calendar.MONTH) + 2)
							+ Messages.getString("Invoice.48") //$NON-NLS-1$
							+ computeTargetYear(cal.get(Calendar.MONTH) + 2)
							+ Messages.getString("Invoice.49")); //$NON-NLS-1$
			final Font targetfont = target.getFont();
			targetfont.setSize(12);
			document.add(target);

			final Paragraph footer = new Paragraph(
					Messages.getString("Invoice.50")); //$NON-NLS-1$
			final Font footerfont = footer.getFont();
			footerfont.setSize(12);
			footerfont.setStyle(Font.BOLD);
			document.add(footer);

			setProperties(document);
			document.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	private static String computeTargetYear(int nextMon) {

		int year = Calendar.getInstance().get(Calendar.YEAR);
		if (nextMon == 13) {
			return Messages.getString("Invoice.51") + (year + 1); //$NON-NLS-1$
		} else {
			return Messages.getString("Invoice.52") + year; //$NON-NLS-1$
		}
	}

	private static String computeTagetMonth(int nextMon) {
		if (nextMon == 13) {
			return Messages.getString("Invoice.53"); //$NON-NLS-1$
		} else {
			final DecimalFormat numFormat = new DecimalFormat("00");

			return Messages.getString("Invoice.54") + numFormat.format(nextMon); //$NON-NLS-1$
		}
	}

	private static void setProperties(final Document document) {
		document.addAuthor(Messages.getString("Invoice.55")); //$NON-NLS-1$
		document.addCreationDate();
		document.addSubject(Messages.getString("Invoice.56")); //$NON-NLS-1$
	}

	private static CustomerAddress readProperties(final String propName) { 
		CustomerAddress myCustAddress = null;
		try {
			String pwd = System.getenv(Messages.getString("Invoice.57")); //$NON-NLS-1$
			pwd = fixMacOSBug(pwd);
			
            System.out.println(Messages.getString("Invoice.58") + propName + Messages.getString("Invoice.59") + pwd); //$NON-NLS-1$ //$NON-NLS-2$
            // Class.getResourceAsStream ("resource.properties");
            FileInputStream returnPropStream = new FileInputStream(pwd+Messages.getString("Invoice.60")+propName); //$NON-NLS-1$
            System.out.println(Messages.getString("Invoice.61")); //$NON-NLS-1$
            Properties prop = new Properties();
            prop.load(returnPropStream);
			
			// read the entries in the file
			final String CustName = prop.getProperty(Messages.getString("Invoice.62")); //$NON-NLS-1$
			final String CustAddress = prop.getProperty(Messages.getString("Invoice.63")); //$NON-NLS-1$
			final String CustZip = prop.getProperty(Messages.getString("Invoice.64")); //$NON-NLS-1$
			final String CustTown = prop.getProperty(Messages.getString("Invoice.65")); //$NON-NLS-1$

			myCustAddress = new CustomerAddress(CustName, CustAddress, CustZip,
					CustTown);
			System.out
					.println(Messages.getString("Invoice.66") + myCustAddress.CustName); //$NON-NLS-1$
		    // prop.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return myCustAddress;
	}

	/**
	 * On Mac OS getenv("PWD"); returns null, why?
	 * @todo as on stackoverflow ?
	 * @param pwd
	 * @return
	 */
	private static String fixMacOSBug(String pwd) {
		// Mac OS anormality?
		if (pwd == null || pwd.length() == 0){
			pwd = "/Users/stefan";
		}
		return pwd;
	}

	private static Double getActualRateFromProps(final String propName) {
		String myRate = "";
		try {
			// read the property file
			String pwd = System.getenv(Messages.getString("Invoice.57"));
			pwd = fixMacOSBug(pwd);
			FileInputStream returnPropStream = new FileInputStream(pwd+Messages.getString("Invoice.60")+propName);
			System.out.println("Read property file: " + propName);
			Properties prop = new Properties();
			prop.load(returnPropStream);

			// read the entries in the file
			myRate = prop.getProperty("Rate");
			System.out.println("Creating invoice with " + myRate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Double double1 = new Double(myRate);
		return double1;
	}
}
