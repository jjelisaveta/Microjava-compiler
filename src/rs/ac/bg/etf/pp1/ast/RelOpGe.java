// generated with ast extension for cup
// version 0.8
// 27/0/2022 22:27:44


package rs.ac.bg.etf.pp1.ast;

public class RelOpGe extends Relop {

    public RelOpGe () {
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
        buffer.append("RelOpGe(\n");

        buffer.append(tab);
        buffer.append(") [RelOpGe]");
        return buffer.toString();
    }
}
