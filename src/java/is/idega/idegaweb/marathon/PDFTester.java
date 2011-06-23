package is.idega.idegaweb.marathon;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

public class PDFTester {

	public static void main(String args[]) {
		PDFTester tester = new PDFTester();

		try {
			//tester.doIt("Palli test strengur", "/Users/palli/Downloads/test.pdf");
			tester.doIt3();
		} catch (COSVisitorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public void doIt( String message, String  outfile ) throws IOException, COSVisitorException
    {
        // the document
        PDDocument doc = null;
        try
        {
            doc = new PDDocument();

            // Page 1
            PDFont font = PDType1Font.HELVETICA;
            PDPage page = new PDPage();
            page.setMediaBox(PDPage.PAGE_SIZE_A4);
            doc.addPage(page);
            float fontSize = 12.0f;

            PDRectangle pageSize = page.findMediaBox();
            float centeredXPosition = (pageSize.getWidth() - fontSize/1000f)/2f;
            float stringWidth = font.getStringWidth( message );
            float centeredYPosition = (pageSize.getHeight() - (stringWidth*fontSize)/1000f)/3f;

            PDPageContentStream contentStream = new PDPageContentStream(doc, page, false, false);
            contentStream.setFont( font, fontSize );
            contentStream.beginText();
            // counterclockwise rotation
            for (int i=0;i<8;i++) 
            {
                contentStream.setTextRotation(i*Math.PI*0.25, centeredXPosition, 
                        pageSize.getHeight()-centeredYPosition);
                contentStream.drawString( message + " " + i);
            }
            // clockwise rotation
            for (int i=0;i<8;i++) 
            {
                contentStream.setTextRotation(-i*Math.PI*0.25, centeredXPosition, centeredYPosition);
                contentStream.drawString( message + " " + i);
            }

            contentStream.endText();
            contentStream.close();

            // Page 2
            page = new PDPage();
            page.setMediaBox(PDPage.PAGE_SIZE_A4);
            doc.addPage(page);
            fontSize = 1.0f;

            contentStream = new PDPageContentStream(doc, page, false, false);
            contentStream.setFont( font, fontSize );
            contentStream.beginText();

            // text scaling
            for (int i=0;i<10;i++)
            {
                contentStream.setTextScaling(12+(i*6), 12+(i*6), 100, 100+i*50);
                contentStream.drawString( message + " " +i);
            }
            contentStream.endText();
            contentStream.close();

            // Page 3
            page = new PDPage();
            page.setMediaBox(PDPage.PAGE_SIZE_A4);
            doc.addPage(page);
            fontSize = 1.0f;

            contentStream = new PDPageContentStream(doc, page, false, false);
            contentStream.setFont( font, fontSize );
            contentStream.beginText();

            int i = 0;
            // text scaling combined with rotation 
            contentStream.setTextMatrix(12, 0, 0, 12, centeredXPosition, centeredYPosition*1.5);
            contentStream.drawString( message + " " +i++);

            contentStream.setTextMatrix(0, 18, -18, 0, centeredXPosition, centeredYPosition*1.5);
            contentStream.drawString( message + " " +i++);

            contentStream.setTextMatrix(-24, 0, 0, -24, centeredXPosition, centeredYPosition*1.5);
            contentStream.drawString( message + " " +i++);

            contentStream.setTextMatrix(0, -30, 30, 0, centeredXPosition, centeredYPosition*1.5);
            contentStream.drawString( message + " " +i++);

            contentStream.endText();
            contentStream.close();

            doc.save( outfile );
        }
        finally
        {
            if( doc != null )
            {
                doc.close();
            }
        }
    }

	
	public void doItUjac() {
		
	}
	
	
	public void doIt3() throws IOException, COSVisitorException {
		// the document
		PDDocument doc = null;
		try {
			doc = new PDDocument();

			PDPage page = new PDPage(PDPage.PAGE_SIZE_A4);
			doc.addPage(page);

			PDXObjectImage ximage = new PDJpeg(doc, new FileInputStream(
					"/Users/palli/Downloads/rm_vidurkenning_2010.jpg"));

			ximage.setHeight((int) PDPage.PAGE_SIZE_A4.getHeight());
			ximage.setWidth((int) PDPage.PAGE_SIZE_A4.getWidth());

			PDPageContentStream contentStream = new PDPageContentStream(doc,
					page);

			contentStream.drawImage(ximage, 0, 0);

			//PDFont font = PDType1Font.HELVETICA_BOLD;
			PDFont font = PDTrueTypeFont.loadTTF(doc, "/Users/palli/Downloads/AppleMyungjo.ttf");
			float fontSize = 20.0f;
			
			PDRectangle pageSize = page.findMediaBox();
			String message = "Björn Margeirsson";
			float stringWidth = font.getStringWidth(message);

			// get page center
			float centeredPosition = (pageSize.getWidth() - (stringWidth * fontSize) / 1000f) / 2f;
			
			contentStream.beginText();
			contentStream.setFont(font, fontSize);
			contentStream.setTextMatrix(1, 0, 0, 1, centeredPosition, 410);
			contentStream.drawString(message);
			
			fontSize = 14.0f;
			contentStream.setFont(font, fontSize);
			message = "lauk 42,2 km maraþon með eftirfarandi árangri:";
			stringWidth = font.getStringWidth(message);
			centeredPosition = (pageSize.getWidth() - (stringWidth * fontSize) / 1000f) / 2f;

			contentStream.setTextMatrix(1, 0, 0, 1, centeredPosition, 380);
			contentStream.drawString(message);

			message = "completed 42,2 km marathon with the folowing result:";
			stringWidth = font.getStringWidth(message);
			centeredPosition = (pageSize.getWidth() - (stringWidth * fontSize) / 1000f) / 2f;

			contentStream.setTextMatrix(1, 0, 0, 1, centeredPosition, 360);
			contentStream.drawString(message);

			message = "Byssutími / Official time: 2:33:58" ;
			stringWidth = font.getStringWidth(message);
			centeredPosition = (pageSize.getWidth() - (stringWidth * fontSize) / 1000f) / 2f;

			contentStream.setTextMatrix(1, 0, 0, 1, centeredPosition, 330);
			contentStream.drawString(message);

			message = "Flögutími / Chip time: 2:33:55" ;
			stringWidth = font.getStringWidth(message);
			centeredPosition = (pageSize.getWidth() - (stringWidth * fontSize) / 1000f) / 2f;

			contentStream.setTextMatrix(1, 0, 0, 1, centeredPosition, 310);
			contentStream.drawString(message);

			message = "1. sæti af 563 þátttakendum, 407 karlar og 156 konur" ;
			stringWidth = font.getStringWidth(message);
			centeredPosition = (pageSize.getWidth() - (stringWidth * fontSize) / 1000f) / 2f;

			contentStream.setTextMatrix(1, 0, 0, 1, centeredPosition, 290);
			contentStream.drawString(message);


			message = "Place 1 of 563 finishers, 407 male and 156 female" ;
			stringWidth = font.getStringWidth(message);
			centeredPosition = (pageSize.getWidth() - (stringWidth * fontSize) / 1000f) / 2f;

			contentStream.setTextMatrix(1, 0, 0, 1, centeredPosition, 270);
			contentStream.drawString(message);

			message = "1. sæti kynjaskipt, Placement: 1 by gender";
			stringWidth = font.getStringWidth(message);
			centeredPosition = (pageSize.getWidth() - (stringWidth * fontSize) / 1000f) / 2f;

			contentStream.setTextMatrix(1, 0, 0, 1, centeredPosition, 250);
			contentStream.drawString(message);

			message = "1. sæti í flokknum: Karlar 18 til 39 ára" ;
			stringWidth = font.getStringWidth(message);
			centeredPosition = (pageSize.getWidth() - (stringWidth * fontSize) / 1000f) / 2f;

			contentStream.setTextMatrix(1, 0, 0, 1, centeredPosition, 230);
			contentStream.drawString(message);


			message = "Place: 1 in age group: Men 18 to 39 years" ;
			stringWidth = font.getStringWidth(message);
			centeredPosition = (pageSize.getWidth() - (stringWidth * fontSize) / 1000f) / 2f;

			contentStream.setTextMatrix(1, 0, 0, 1, centeredPosition, 210);
			contentStream.drawString(message);

			contentStream.endText();
			
			contentStream.close();
			doc.save("/Users/palli/Downloads/rm_vidurkenning_2010.pdf");
		} finally {
			if (doc != null) {
				doc.close();
			}
		}

	}

}