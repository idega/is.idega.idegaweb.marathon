package is.idega.idegaweb.marathon;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.ujac.print.DocumentPrinter;
import org.ujac.util.io.FileResourceLoader;

public class PDFTester2 {

	public static void main(String[] args) {
		PDFTester2 test = new PDFTester2();
		test.doIt();
	}

	public void doIt() {
		try {
			Map documentProperties = new HashMap();
			documentProperties.put("name", "Palli pulsa");
			documentProperties.put("name2", "Laddi lúði");
			documentProperties.put("name3", "Gunni grís");

			// instantiating the document printer
			FileInputStream templateStream = new FileInputStream(
					"/Users/palli/Downloads/image-test.xml");
			DocumentPrinter documentPrinter = new DocumentPrinter(
					templateStream, documentProperties);

			// defining the ResourceLoader: This is necessary if you like to
			// dynamically load resources like images during template
			// processing.
			documentPrinter.setResourceLoader(new FileResourceLoader("/Users/palli/Downloads/"));
			// generating the document output
			FileOutputStream pdfStream = new FileOutputStream(
					"/Users/palli/Downloads/image-test.pdf");
			documentPrinter.printDocument(pdfStream);
			
			pdfStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
