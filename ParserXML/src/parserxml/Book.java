package parserxml;

public class Book {
    
    private int bookId;
    private String title;
    private String pages;
    private String releaseDate;

    public Book(int bookId, String title, String pages, String releaseDate) {
        this.bookId = bookId;
        this.title = title;
        this.pages = pages;
        this.releaseDate = releaseDate;
    }

    public Book() {
    }

    public int getId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getPages() {
        return pages;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setId(int bookId) {
        this.bookId = bookId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "Book{" + "bookId=" + bookId + ", title=" + title + ", pages=" + pages +
                ", releaseDate=" + releaseDate + '}';
    }
    
}
