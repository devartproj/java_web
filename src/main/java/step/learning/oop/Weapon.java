package step.learning.oop;

public abstract class Weapon {
    @Required
    private String name ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract String getCard() ;
}
