// generated with ast extension for cup
// version 0.8
// 14/0/2022 19:23:14


package rs.ac.bg.etf.pp1.ast;

public class ReturnVoid extends MethodReturnValue {

    public ReturnVoid () {
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
        buffer.append("ReturnVoid(\n");

        buffer.append(tab);
        buffer.append(") [ReturnVoid]");
        return buffer.toString();
    }
}
