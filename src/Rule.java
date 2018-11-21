/**
 * Dto used for
 */
public class Rule {
    private Integer number;
    private String productionRule;
    private Symbol terminal;

    public Rule(Integer number, String productionRule, Symbol terminal) {
        this.number = number;
        this.productionRule = productionRule;
        this.terminal = terminal;
    }

    public Integer getNumber() {
        return number;
    }

    @Override
    public String toString(){
        return this.number + "\tby " + productionRule;
    }
}
