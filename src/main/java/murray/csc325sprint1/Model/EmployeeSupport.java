package murray.csc325sprint1.Model;

public class EmployeeSupport {
    private int ticketID;
    private String user;
    private String subject;
    private String cusmsg;
    private String status;
    private String response;

    public EmployeeSupport(){}

    public EmployeeSupport(int ticketID, String user, String subject, String cusmsg, String status, String response) {
        this.ticketID = ticketID;
        this.user = user;
        this.subject = subject;
        this.cusmsg = cusmsg;
        this.status = status;
        this.response = response;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getcusmsg() {
        return cusmsg;
    }
    public void setcusmsg(String cusmsg) {
        this.cusmsg = cusmsg;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getResponse() {
        return response;
    }
    public void setResponse(String response) {
        this.response = response;
    }
    public int getTicketID() {
        return ticketID;
    }
    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }
}
