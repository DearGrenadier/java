package parserxml;

public class ParserXML {

    public static void main(String[] args) {
        AuthorsSAXBuilder saxBuilder = new AuthorsSAXBuilder();
        saxBuilder.buildAuthorsList("data/xml.xml");
        System.out.println(saxBuilder.getStudents());
    }
    
}
