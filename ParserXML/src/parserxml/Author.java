package parserxml;

public class Author {
    private int authorId;
    private String letter;
    private String name;
    private String surname;
    private String country;
    private String birthDate;
    private Book book = null;

    public void setBook(Book book) {
        this.book = book;
    }

    public Book getBook() {
        return book;
    }

    public Author(int authorId, String letter, String name, String surname,
            String country, String birthDate) {
        this.authorId = authorId;
        this.letter = letter;
        this.name = name;
        this.surname = surname;
        this.country = country;
        this.birthDate = birthDate;
        this.book = new Book();
    }

    public Author() {
        this.book = new Book();
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public int getAuthorId() {
        return authorId;
    }

    public String getLetter() {
        return letter;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getCountry() {
        return country;
    }

    public String getBirthDate() {
        return birthDate;
    }

    @Override
    public String toString() {
        return "Author{" + "authorId=" + authorId + ", letter=" + letter + ", name=" +
                name + ", surname=" + surname + ", country=" + country +
                ", birthDate=" + birthDate + ", book=" + book.toString() + '}';
    }
    
}
