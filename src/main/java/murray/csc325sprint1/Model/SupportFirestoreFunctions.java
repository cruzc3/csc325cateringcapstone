package murray.csc325sprint1.Model;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import murray.csc325sprint1.FirestoreContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SupportFirestoreFunctions {
    private static volatile SupportFirestoreFunctions instance;
    private final Firestore db;
    private static final String COLLECTION_NAME = "Support";

    private SupportFirestoreFunctions() {
        db = FirestoreContext.getInstance().getFirestore();
    }

    public static SupportFirestoreFunctions getInstance() {
        if (instance == null) {
            synchronized (SupportFirestoreFunctions.class) {
                if (instance == null) {
                    instance = new SupportFirestoreFunctions();
                }
            }
        }
        return instance;
    }

    public void insertTicket(EmployeeSupport ticket) {
        try {
            Map<String, Object> ticketInfo = new HashMap<>();
            ticketInfo.put("ticketID", ticket.getTicketID());
            ticketInfo.put("user", ticket.getUser());
            ticketInfo.put("subject", ticket.getSubject());
            ticketInfo.put("cusmsg", ticket.getCusmsg());
            ticketInfo.put("isClosed", ticket.isClosed());
            ticketInfo.put("response", ticket.getResponse());

            db.collection(COLLECTION_NAME).document(String.valueOf(ticket.getTicketID())).set(ticketInfo).get();
        } catch (Exception e) {
            System.err.println("Error while inserting ticket: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateResponse(int ticketID, String response) {
        try {
            Map<String, Object> update = new HashMap<>();
            update.put("response", response);
            update.put("isClosed", true);
            ApiFuture<WriteResult> future = db.collection(COLLECTION_NAME).document(String.valueOf(ticketID)).update(update);
            future.get();
            System.out.println("Ticket updated: " + ticketID);
        } catch (Exception e) {
            System.err.println("Error while updating response: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public EmployeeSupport getTicket(int ticketID) {
        try {
            DocumentReference docRef = db.collection(COLLECTION_NAME).document(String.valueOf(ticketID));
            DocumentSnapshot doc = docRef.get().get();
            if (doc.exists()) {
                return new EmployeeSupport(
                        doc.getLong("ticketID").intValue(),
                        doc.getString("user"),
                        doc.getString("subject"),
                        doc.getString("cusmsg"),
                        doc.getBoolean("isClosed"),
                        doc.getString("response")
                );
            }
        } catch (Exception e) {
            System.err.println("Error while getting ticket: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<EmployeeSupport> getAllTickets() {
        List<EmployeeSupport> tickets = new ArrayList<>();
        try {
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                tickets.add(new EmployeeSupport(
                        document.getLong("ticketID").intValue(),
                        document.getString("user"),
                        document.getString("subject"),
                        document.getString("cusmsg"),
                        document.getBoolean("isClosed"),
                        document.getString("response")
                ));
            }
        } catch (Exception e) {
            System.err.println("Error while getting all tickets: " + e.getMessage());
            e.printStackTrace();
        }
        return tickets;
    }
}