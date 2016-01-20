package ch.makery.address.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import ch.makery.address.model.Person;
import javafx.collections.ObservableList;

public class PdfMaker {

	public static void PersonListToPDF(ObservableList<Person> personList, File file) {
		
		Document document = new Document();
		
		try {
			String path = file.getAbsolutePath().substring(0, file.getAbsolutePath().length()-4);
			PdfWriter.getInstance(document, new FileOutputStream(path+".pdf"));
			
			document.open();
			Paragraph title = new Paragraph(file.getName().substring(0, file.getName().length()-4));
			title.setAlignment(Element.ALIGN_CENTER);
			title.setSpacingAfter(20);
			document.add(title);
			
			PdfPTable table = new PdfPTable(4);
			table.setWidthPercentage(100);
			
			float[] columnWidths = {2f, 4f, 1.5f, 2.5f};
			table.setWidths(columnWidths);
			
			for (Person person : personList) {
				
				PdfPCell cell1 = new PdfPCell(new Paragraph(person.getGenderAndLastName()));
				PdfPCell cell2 = new PdfPCell(new Paragraph(person.getFullAddress()));
				PdfPCell cell3 = new PdfPCell(new Paragraph(person.getTelNumber()));
				PdfPCell cell4 = new PdfPCell(new Paragraph(person.getDayList().toString()));
				
				table.addCell(cell1);
				table.addCell(cell2);
				table.addCell(cell3);
				table.addCell(cell4);

				
				if(person.getMoreInfo().length() > 0){
					
					PdfPCell moreInfo = new PdfPCell(new Paragraph(person.getMoreInfo()));
					moreInfo.setColspan(4);
					table.addCell(moreInfo);
					
				}
				
			}
			
			document.add(table);
			
			document.close();
			
		} catch (FileNotFoundException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
