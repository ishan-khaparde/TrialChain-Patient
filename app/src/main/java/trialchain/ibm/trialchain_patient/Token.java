package trialchain.ibm.trialchain_patient;

public class Token {

    private String host;
    private String id;
    private String sk;

    private String sessionId;

    public Token(String id)
    {
        this.id = id;

    }
    public Token()
    {
        host = id = null;

        sessionId = null;
    }


    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }
    public String getSessionId()
    {return sessionId;}

    public String getPublicKey() {
        return id;
    }

    public void setPublicKey(String id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSk() {
        return sk;
    }

    public void setSk(String sk) {
        this.sk = sk;
    }
}
