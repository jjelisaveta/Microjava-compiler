// generated with ast extension for cup
// version 0.8
// 28/11/2021 18:27:14


package rs.ac.bg.etf.pp1.ast;

public class ErrorClassVariableDecl extends ClassVarDecl {

    public ErrorClassVariableDecl () {
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
        buffer.append("ErrorClassVariableDecl(\n");

        buffer.append(tab);
        buffer.append(") [ErrorClassVariableDecl]");
        return buffer.toString();
    }
}