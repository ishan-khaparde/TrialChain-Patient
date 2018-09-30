package trialchain.ibm.trialchain_patient;


public class Message {

      private String recordId;
      private String message;

    public Message()
    {
        message = recordId = null;
    }

    public void setRecordId(String recordId)
    {
        this.recordId = recordId;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

    public String getRecordId()
    {
        return recordId;
    }
}
