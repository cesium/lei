/**
 *
 * @author Bruno
 */
public class ExchangeRequest {
    int created_at;
    String from_shift_id;
    String to_shift_id;
    String id;

    public ExchangeRequest(int created_at, String from_shift_id, String to_shift_id,String id){
        this.created_at=created_at;
        this.from_shift_id=from_shift_id;
        this.to_shift_id=to_shift_id;
        this.id=id;
    }

    /* GETTERS */
    String getFrom_shift_id(){return this.from_shift_id;}
    String getTo_shift_id(){return this.to_shift_id;}
    int getCreated_at(){return this.created_at;}
    String getId(){return this.id;}

    @Override
    public String toString(){
        return "{from: " + this.from_shift_id +
                ", to: " + this.to_shift_id +
                ", created_at: " + this.created_at +
                ", id: " + this.id + "}";
    }

    public boolean equalsER(ExchangeRequest er){
        return ((er.created_at==this.created_at) &&
                (er.from_shift_id.equals(this.from_shift_id)) &&
                (er.to_shift_id.equals(this.to_shift_id)) &&
                (er.id.equals(this.id))
        );
    }
}
