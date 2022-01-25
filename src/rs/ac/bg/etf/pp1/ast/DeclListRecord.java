// generated with ast extension for cup
// version 0.8
// 25/0/2022 20:18:17


package rs.ac.bg.etf.pp1.ast;

public class DeclListRecord extends DeclList {

    private DeclList DeclList;
    private RecordDecl RecordDecl;

    public DeclListRecord (DeclList DeclList, RecordDecl RecordDecl) {
        this.DeclList=DeclList;
        if(DeclList!=null) DeclList.setParent(this);
        this.RecordDecl=RecordDecl;
        if(RecordDecl!=null) RecordDecl.setParent(this);
    }

    public DeclList getDeclList() {
        return DeclList;
    }

    public void setDeclList(DeclList DeclList) {
        this.DeclList=DeclList;
    }

    public RecordDecl getRecordDecl() {
        return RecordDecl;
    }

    public void setRecordDecl(RecordDecl RecordDecl) {
        this.RecordDecl=RecordDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DeclList!=null) DeclList.accept(visitor);
        if(RecordDecl!=null) RecordDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DeclList!=null) DeclList.traverseTopDown(visitor);
        if(RecordDecl!=null) RecordDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DeclList!=null) DeclList.traverseBottomUp(visitor);
        if(RecordDecl!=null) RecordDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DeclListRecord(\n");

        if(DeclList!=null)
            buffer.append(DeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(RecordDecl!=null)
            buffer.append(RecordDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DeclListRecord]");
        return buffer.toString();
    }
}
