// generated with ast extension for cup
// version 0.8
// 28/11/2021 18:27:14


package rs.ac.bg.etf.pp1.ast;

public class MulOpMul extends Mulop {

    public MulOpMul () {
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
        buffer.append("MulOpMul(\n");

        buffer.append(tab);
        buffer.append(") [MulOpMul]");
        return buffer.toString();
    }
}
