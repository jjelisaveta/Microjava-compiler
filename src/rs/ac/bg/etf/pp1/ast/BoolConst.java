// generated with ast extension for cup
// version 0.8
// 11/0/2022 21:45:12


package rs.ac.bg.etf.pp1.ast;

public class BoolConst extends TypeConst {

    private String boolConst;

    public BoolConst (String boolConst) {
        this.boolConst=boolConst;
    }

    public String getBoolConst() {
        return boolConst;
    }

    public void setBoolConst(String boolConst) {
        this.boolConst=boolConst;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("BoolConst(\n");

        buffer.append(" "+tab+boolConst);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [BoolConst]");
        return buffer.toString();
    }
}
