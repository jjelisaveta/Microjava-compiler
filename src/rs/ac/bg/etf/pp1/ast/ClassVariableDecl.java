// generated with ast extension for cup
// version 0.8
// 11/0/2022 21:45:12


package rs.ac.bg.etf.pp1.ast;

public class ClassVariableDecl extends ClassVarDeclList {

    private Type Type;
    private OneVarDecl OneVarDecl;

    public ClassVariableDecl (Type Type, OneVarDecl OneVarDecl) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.OneVarDecl=OneVarDecl;
        if(OneVarDecl!=null) OneVarDecl.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public OneVarDecl getOneVarDecl() {
        return OneVarDecl;
    }

    public void setOneVarDecl(OneVarDecl OneVarDecl) {
        this.OneVarDecl=OneVarDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(OneVarDecl!=null) OneVarDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(OneVarDecl!=null) OneVarDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(OneVarDecl!=null) OneVarDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassVariableDecl(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OneVarDecl!=null)
            buffer.append(OneVarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassVariableDecl]");
        return buffer.toString();
    }
}
