package step.learning.oop;

import com.google.gson.JsonObject;

@Serializable
public class MachineGun extends Weapon implements Automatic, Classified {
    private double fireRate ;

    public MachineGun( String name, double fireRate ) {
        super.setName( name ) ;
        this.setFireRate( fireRate ) ;
    }

    public double getFireRate() {
        return fireRate;
    }

    public void setFireRate(double fireRate) {
        this.fireRate = fireRate;
    }

    @Override
    public String getCard() {
        return String.format(
                "MachineGun: '%s' (fire rate %.1f bps)",
                super.getName(),
                this.getFireRate()
        ) ;
    }

    @Override
    public String getLevel() {
        return "For military";
    }

    @JsonParseCheck
    public static boolean isJsonParseable( JsonObject jsonObject ) {
        String[] requiredFields = { "name", "fireRate" } ;
        for( String field : requiredFields ) {
            if( ! jsonObject.has( field ) ) {
                return false ;
            }
        }
        return true ;
    }

    @JsonFactory
    public static MachineGun fromJson( JsonObject jsonObject ) throws IllegalArgumentException {
        String[] requiredFields = { "name", "fireRate" } ;
        for( String field : requiredFields ) {
            if( ! jsonObject.has( field ) ) {
                throw new IllegalArgumentException( "MachineGun construct error: Missing required field: " + field ) ;
            }
        }
        return new MachineGun(
                jsonObject.get( requiredFields[0] ).getAsString(),
                jsonObject.get( requiredFields[1] ).getAsDouble()
        ) ;
    }
}
