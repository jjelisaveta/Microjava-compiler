// generated with ast extension for cup
// version 0.8
// 23/0/2022 22:37:5


package rs.ac.bg.etf.pp1.ast;

public class RelOpLt extends Relop {

    public RelOpLt () {
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
        buffer.append("RelOpLt(\n");

        buffer.append(tab);
        buffer.append(") [RelOpLt]");
        return buffer.toString();
    }
}
