
package parserxml;

import java.io.IOException;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class AuthorsSAXBuilder {
    
    private AuthorsList authors;
    private AuthorHandler ah;
    private XMLReader reader;
    
    public AuthorsSAXBuilder() {
        ah = new AuthorHandler();
        try {
            reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(ah);
        } catch (SAXException e) {
            System.err.print("ошибка SAX парсера: " + e);
        }
    }
    
    public AuthorsList getStudents() {
        return authors;
    }
    
    public void buildAuthorsList(String fileName) {
        try {
            reader.parse(fileName);
        } catch (SAXException e) {
            System.err.print("ошибка SAX парсера: " + e);
        } catch (IOException e) {
            System.err.print("ошибка I/О потока: " + e);
        }
        authors = ah.getAuthors();
    }
}
