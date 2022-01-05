// generated with ast extension for cup
// version 0.8
// 5/0/2022 20:57:9


package rs.ac.bg.etf.pp1.ast;

public class ConstantDecls extends ConstDeclList {

    private ConstDeclList ConstDeclList;
    private String constName;
    private TypeConst TypeConst;

    public ConstantDecls (ConstDeclList ConstDeclList, String constName, TypeConst TypeConst) {
        this.ConstDeclList=ConstDeclList;
        if(ConstDeclList!=null) ConstDeclList.setParent(this);
        this.constName=constName;
        this.TypeConst=TypeConst;
        if(TypeConst!=null) TypeConst.setParent(this);
    }

    public ConstDeclList getConstDeclList() {
        return ConstDeclList;
    }

    public void setConstDeclList(ConstDeclList ConstDeclList) {
        this.ConstDeclList=ConstDeclList;
    }

    public String getConstName() {
        return constName;
    }

    public void setConstName(String constName) {
        this.constName=constName;
    }

    public TypeConst getTypeConst() {
        return TypeConst;
    }

    public void setTypeConst(TypeConst TypeConst) {
        this.TypeConst=TypeConst;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstDeclList!=null) ConstDeclList.accept(visitor);
        if(TypeConst!=null) TypeConst.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstDeclList!=null) ConstDeclList.traverseTopDown(visitor);
        if(TypeConst!=null) TypeConst.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstDeclList!=null) ConstDeclList.traverseBottomUp(visitor);
        if(TypeConst!=null) TypeConst.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstantDecls(\n");

        if(ConstDeclList!=null)
            buffer.append(ConstDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+constName);
        buffer.append("\n");

        if(TypeConst!=null)
            buffer.append(TypeConst.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstantDecls]");
        return buffer.toString();
    }
}
