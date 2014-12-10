package courseclient;

public class ProjectView {
    private int id;
    private String  name;
    private String startDate;
    private String endDate;
    private String managers;
    private String performers;
    private int percent;

    public ProjectView(int id, String name, String managers, String performers,
            String startDate, String endDate, int percent) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.managers = managers;
        this.performers = performers;
        this.percent = percent;
    }

    public ProjectView() {
    }
    
    
    public void setId(int id) {
        this.id = id;
    }

    public void setManagers(String managers) {
        this.managers = managers;
    }
    
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setManager(String managers) {
        this.managers = managers;
    }

    public void setPerformers(String performers) {
        this.performers = performers;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public int getId() {
        return id;
    }

    public String getManagers() {
        return managers;
    }
    
    
    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getManager() {
        return managers;
    }

    public String getPerformers() {
        return performers;
    }

    public int getPercent() {
        return percent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}

