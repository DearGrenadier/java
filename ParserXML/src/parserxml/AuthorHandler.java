
package parserxml;

import java.util.EnumSet;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class AuthorHandler extends DefaultHandler {
    
    private final AuthorsList authors;
    private Author current = null;
    private Book book = null;
    private LibraryEnum currentEnum = null;
    private final EnumSet<LibraryEnum> withText;
    
    public AuthorHandler() {
        authors = new AuthorsList();
        withText = EnumSet.range(LibraryEnum.NAME, LibraryEnum.RELEASEDATE);
    }
    
    public AuthorsList getAuthors() {
        return authors;
    }
    
    @Override
    public void startElement(String uri, String localName,
                              String qName, Attributes attributes) {
        if ("author".equalsIgnoreCase(localName)) {
            current = new Author();
            current.setAuthorId(Integer.parseInt(attributes.getValue("authorId")));
            if (attributes.getLength()== 2) {
                current.setLetter(attributes.getValue("letter"));
            }
        } else if ("book".equalsIgnoreCase(localName)) {
            book = new Book();
            book.setId(Integer.parseInt(attributes.getValue("bookId")));
        } else {
            LibraryEnum temp = LibraryEnum.valueOf(localName.toUpperCase());
            if (withText.contains(temp)) {
                currentEnum = temp;
            }
        }
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) {
        if ("author".equals(localName)) {
            authors.add(current);
        }
    }
    
    @Override
    public void characters(char[] ch, int start, int length) {
        String s = new String(ch, start, length).trim();
        if (currentEnum != null) {
            switch (currentEnum) {
                case NAME:
                    current.setName(s);
                    break;
                case SURNAME:
                    current.setSurname(s);
                    break;
                case COUNTRY:
                    current.setCountry(s);
                    break;
                case BIRTHDATE:
                    current.setBirthDate(s);
                    break;
                    
                case TITLE:
                    current.getBook().setTitle(s);
                    break;
                case PAGES:
                    current.getBook().setPages(s);
                    break;
                case RELEASEDATE:
                    current.getBook().setReleaseDate(s);
                    break;
                default:
                    throw new EnumConstantNotPresentException(
                        currentEnum.getDeclaringClass(), currentEnum.name());
            }
        }
        currentEnum = null;
    }
    
}
