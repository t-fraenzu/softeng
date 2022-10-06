package mse.fhnw.ch;

public class Policy {

    private String policyId;
    private String name = "";
    private Object rate;

    public void setLastName(String name) {

        this.name += name;
    }

    public String getState() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getId() {
        return policyId;
    }

    public void setRate(Object rate) {

        this.rate = rate;
    }

    public void setId(String policyId) {

        this.policyId = policyId;
    }

    public String getLastName() {
        return name;
    }

    public Object getRate() {
        return rate;
    }
}
