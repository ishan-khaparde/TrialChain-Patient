package trialchain.ibm.trialchain_patient;

import org.libsodium.jni.keys.PrivateKey;
import org.libsodium.jni.keys.PublicKey;

/**
 * Description: A plain java object to wrap alias, public key and private key together. This is just to handle all three easily.
 * */
public class Record {


    private String alias;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private String signkey;

    public Record(String alias, PrivateKey privateKey, PublicKey publicKey)
    {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.alias = alias;
    }
    public Record(String alias)
    {
        this.alias = alias;
        this.privateKey = null;
        this.publicKey = null;
    }
    public Record(String alias,String signkey)
    {
        this.alias = alias;
        this.signkey = signkey;
    }

    //Getters and setters for public key, private key and alias.
    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public String getAlias() {
        return alias;
    }

    public String getSignkey() {
        return signkey;
    }

    public void setSignkey(String signkey) {
        this.signkey = signkey;
    }
}
