package murray.csc325sprint1.Model;

public class EmployeeSupport {
    private int ticketID;
    private String user;
    private String subject;
    private String cusmsg;
    private boolean isClosed;
    private String response;

    private long timestamp; // Add this

    public EmployeeSupport(int ticketID, String user, String subject, String cusmsg, boolean isClosed, String response, long timestamp) {
        this.ticketID = ticketID;
        this.user = user;
        this.subject = subject;
        this.cusmsg = cusmsg;
        this.isClosed = isClosed;
        this.response = response;
        this.timestamp = timestamp; // Add this
    }

    // Add getter & setter
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getTicketID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
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

    public String getCusmsg() {
        return cusmsg;
    }

    public void setCusmsg(String cusmsg) {
        this.cusmsg = cusmsg;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}