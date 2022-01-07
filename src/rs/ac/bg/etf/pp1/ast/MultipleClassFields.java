// generated with ast extension for cup
// version 0.8
// 7/0/2022 19:17:20


package rs.ac.bg.etf.pp1.ast;

public class MultipleClassFields extends ClassFieldsDeclList {

    private ClassFieldsDeclList ClassFieldsDeclList;
    private ClassVarDecl ClassVarDecl;

    public MultipleClassFields (ClassFieldsDeclList ClassFieldsDeclList, ClassVarDecl ClassVarDecl) {
        this.ClassFieldsDeclList=ClassFieldsDeclList;
        if(ClassFieldsDeclList!=null) ClassFieldsDeclList.setParent(this);
        this.ClassVarDecl=ClassVarDecl;
        if(ClassVarDecl!=null) ClassVarDecl.setParent(this);
    }

    public ClassFieldsDeclList getClassFieldsDeclList() {
        return ClassFieldsDeclList;
    }

    public void setClassFieldsDeclList(ClassFieldsDeclList ClassFieldsDeclList) {
        this.ClassFieldsDeclList=ClassFieldsDeclList;
    }

    public ClassVarDecl getClassVarDecl() {
        return ClassVarDecl;
    }

    public void setClassVarDecl(ClassVarDecl ClassVarDecl) {
        this.ClassVarDecl=ClassVarDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ClassFieldsDeclList!=null) ClassFieldsDeclList.accept(visitor);
        if(ClassVarDecl!=null) ClassVarDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassFieldsDeclList!=null) ClassFieldsDeclList.traverseTopDown(visitor);
        if(ClassVarDecl!=null) ClassVarDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassFieldsDeclList!=null) ClassFieldsDeclList.traverseBottomUp(visitor);
        if(ClassVarDecl!=null) ClassVarDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MultipleClassFields(\n");

        if(ClassFieldsDeclList!=null)
            buffer.append(ClassFieldsDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ClassVarDecl!=null)
            buffer.append(ClassVarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MultipleClassFields]");
        return buffer.toString();
    }
}
