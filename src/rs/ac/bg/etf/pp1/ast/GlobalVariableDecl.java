// generated with ast extension for cup
// version 0.8
// 4/1/2022 11:43:14


package rs.ac.bg.etf.pp1.ast;

public class GlobalVariableDecl extends GlobalVarDeclList {

    private GlobalOneVarDecl GlobalOneVarDecl;

    public GlobalVariableDecl (GlobalOneVarDecl GlobalOneVarDecl) {
        this.GlobalOneVarDecl=GlobalOneVarDecl;
        if(GlobalOneVarDecl!=null) GlobalOneVarDecl.setParent(this);
    }

    public GlobalOneVarDecl getGlobalOneVarDecl() {
        return GlobalOneVarDecl;
    }

    public void setGlobalOneVarDecl(GlobalOneVarDecl GlobalOneVarDecl) {
        this.GlobalOneVarDecl=GlobalOneVarDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(GlobalOneVarDecl!=null) GlobalOneVarDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(GlobalOneVarDecl!=null) GlobalOneVarDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(GlobalOneVarDecl!=null) GlobalOneVarDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("GlobalVariableDecl(\n");

        if(GlobalOneVarDecl!=null)
            buffer.append(GlobalOneVarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [GlobalVariableDecl]");
        return buffer.toString();
    }
}
