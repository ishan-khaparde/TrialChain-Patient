package trialchain.ibm.trialchain_patient;


public class AddRecordToken {

    String token;
    String alias;
    String clinicName;
    String clinic_id;
    String api;

    public AddRecordToken()
    {
        token = clinicName = clinic_id = api = null;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String name) {
        this.clinicName = name;
    }

    public String getClinic_id() {
        return clinic_id;
    }

    public void setClinic_id(String clinic_id) {
        this.clinic_id = clinic_id;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public void setAlias(String alias)
    {
        this.alias = alias;
    }

    public String getAlias()

    {return alias;}



}
