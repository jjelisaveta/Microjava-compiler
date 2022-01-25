// generated with ast extension for cup
// version 0.8
// 25/0/2022 20:18:17


package rs.ac.bg.etf.pp1.ast;

public class ErrorOneArrayFormParam extends OneFormPar {

    public ErrorOneArrayFormParam () {
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
        buffer.append("ErrorOneArrayFormParam(\n");

        buffer.append(tab);
        buffer.append(") [ErrorOneArrayFormParam]");
        return buffer.toString();
    }
}
