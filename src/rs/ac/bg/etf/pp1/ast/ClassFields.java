// generated with ast extension for cup
// version 0.8
// 16/1/2022 1:43:21


package rs.ac.bg.etf.pp1.ast;

public class ClassFields implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private ClassFieldsDeclList ClassFieldsDeclList;

    public ClassFields (ClassFieldsDeclList ClassFieldsDeclList) {
        this.ClassFieldsDeclList=ClassFieldsDeclList;
        if(ClassFieldsDeclList!=null) ClassFieldsDeclList.setParent(this);
    }

    public ClassFieldsDeclList getClassFieldsDeclList() {
        return ClassFieldsDeclList;
    }

    public void setClassFieldsDeclList(ClassFieldsDeclList ClassFieldsDeclList) {
        this.ClassFieldsDeclList=ClassFieldsDeclList;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ClassFieldsDeclList!=null) ClassFieldsDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassFieldsDeclList!=null) ClassFieldsDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassFieldsDeclList!=null) ClassFieldsDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassFields(\n");

        if(ClassFieldsDeclList!=null)
            buffer.append(ClassFieldsDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassFields]");
        return buffer.toString();
    }
}
