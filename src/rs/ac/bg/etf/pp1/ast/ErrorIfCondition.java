// generated with ast extension for cup
// version 0.8
// 19/1/2022 0:13:7


package rs.ac.bg.etf.pp1.ast;

public class ErrorIfCondition extends IfCondition {

    public ErrorIfCondition () {
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
        buffer.append("ErrorIfCondition(\n");

        buffer.append(tab);
        buffer.append(") [ErrorIfCondition]");
        return buffer.toString();
    }
}
