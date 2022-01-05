// generated with ast extension for cup
// version 0.8
// 5/0/2022 20:57:9


package rs.ac.bg.etf.pp1.ast;

public class ConstantDecl extends ConstDeclList {

    private Type Type;
    private String constName;
    private TypeConst TypeConst;

    public ConstantDecl (Type Type, String constName, TypeConst TypeConst) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.constName=constName;
        this.TypeConst=TypeConst;
        if(TypeConst!=null) TypeConst.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
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
        if(Type!=null) Type.accept(visitor);
        if(TypeConst!=null) TypeConst.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(TypeConst!=null) TypeConst.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(TypeConst!=null) TypeConst.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstantDecl(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
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
        buffer.append(") [ConstantDecl]");
        return buffer.toString();
    }
}
