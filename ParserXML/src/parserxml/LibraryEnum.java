package parserxml;

public enum LibraryEnum {
    
    AUTHORS("authors"),
    AUTHOR("authors"),
    NAME("name"),
    SURNAME("surname"),
    COUNTRY("country"),
    BIRTHDATE("birthDate"),
    BOOK("book"),
    TITLE("title"),
    PAGES("pages"),
    RELEASEDATE("releaseDate"),
    AUTHORID("authorId"),
    LETTER("letter"),
    BOOKID("bookId");
    
    private final String value;
    
    private LibraryEnum(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
}