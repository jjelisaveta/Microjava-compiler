// generated with ast extension for cup
// version 0.8
// 13/1/2022 22:37:7


package rs.ac.bg.etf.pp1.ast;

public class ErrorOneFormParam extends OneFormPar {

    public ErrorOneFormParam () {
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
        buffer.append("ErrorOneFormParam(\n");

        buffer.append(tab);
        buffer.append(") [ErrorOneFormParam]");
        return buffer.toString();
    }
}
