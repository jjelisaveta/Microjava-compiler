// generated with ast extension for cup
// version 0.8
// 28/11/2021 18:27:14


package rs.ac.bg.etf.pp1.ast;

public class GlobalVariableDecl extends GlobalVarDeclList {

    private Type Type;
    private GlobalOneVarDecl GlobalOneVarDecl;

    public GlobalVariableDecl (Type Type, GlobalOneVarDecl GlobalOneVarDecl) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.GlobalOneVarDecl=GlobalOneVarDecl;
        if(GlobalOneVarDecl!=null) GlobalOneVarDecl.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
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
        if(Type!=null) Type.accept(visitor);
        if(GlobalOneVarDecl!=null) GlobalOneVarDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(GlobalOneVarDecl!=null) GlobalOneVarDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(GlobalOneVarDecl!=null) GlobalOneVarDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("GlobalVariableDecl(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

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
