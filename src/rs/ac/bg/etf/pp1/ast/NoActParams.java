// generated with ast extension for cup
// version 0.8
// 4/1/2022 11:43:14


package rs.ac.bg.etf.pp1.ast;

public class NoActParams extends ActParsList {

    public NoActParams () {
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
        buffer.append("NoActParams(\n");

        buffer.append(tab);
        buffer.append(") [NoActParams]");
        return buffer.toString();
    }
}
