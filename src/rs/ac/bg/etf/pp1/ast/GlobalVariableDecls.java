// generated with ast extension for cup
// version 0.8
// 27/0/2022 22:27:44


package rs.ac.bg.etf.pp1.ast;

public class GlobalVariableDecls extends GlobalVarDeclList {

    private GlobalVarDeclList GlobalVarDeclList;
    private GlobalOneVarDecl GlobalOneVarDecl;

    public GlobalVariableDecls (GlobalVarDeclList GlobalVarDeclList, GlobalOneVarDecl GlobalOneVarDecl) {
        this.GlobalVarDeclList=GlobalVarDeclList;
        if(GlobalVarDeclList!=null) GlobalVarDeclList.setParent(this);
        this.GlobalOneVarDecl=GlobalOneVarDecl;
        if(GlobalOneVarDecl!=null) GlobalOneVarDecl.setParent(this);
    }

    public GlobalVarDeclList getGlobalVarDeclList() {
        return GlobalVarDeclList;
    }

    public void setGlobalVarDeclList(GlobalVarDeclList GlobalVarDeclList) {
        this.GlobalVarDeclList=GlobalVarDeclList;
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
        if(GlobalVarDeclList!=null) GlobalVarDeclList.accept(visitor);
        if(GlobalOneVarDecl!=null) GlobalOneVarDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(GlobalVarDeclList!=null) GlobalVarDeclList.traverseTopDown(visitor);
        if(GlobalOneVarDecl!=null) GlobalOneVarDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(GlobalVarDeclList!=null) GlobalVarDeclList.traverseBottomUp(visitor);
        if(GlobalOneVarDecl!=null) GlobalOneVarDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("GlobalVariableDecls(\n");

        if(GlobalVarDeclList!=null)
            buffer.append(GlobalVarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(GlobalOneVarDecl!=null)
            buffer.append(GlobalOneVarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [GlobalVariableDecls]");
        return buffer.toString();
    }
}
