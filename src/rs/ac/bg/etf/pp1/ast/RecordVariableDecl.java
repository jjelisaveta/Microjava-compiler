// generated with ast extension for cup
// version 0.8
// 9/0/2022 19:48:27


package rs.ac.bg.etf.pp1.ast;

public class RecordVariableDecl extends RecordVarDecls {

    private RecordVarDecls RecordVarDecls;
    private RecordVarDecl RecordVarDecl;

    public RecordVariableDecl (RecordVarDecls RecordVarDecls, RecordVarDecl RecordVarDecl) {
        this.RecordVarDecls=RecordVarDecls;
        if(RecordVarDecls!=null) RecordVarDecls.setParent(this);
        this.RecordVarDecl=RecordVarDecl;
        if(RecordVarDecl!=null) RecordVarDecl.setParent(this);
    }

    public RecordVarDecls getRecordVarDecls() {
        return RecordVarDecls;
    }

    public void setRecordVarDecls(RecordVarDecls RecordVarDecls) {
        this.RecordVarDecls=RecordVarDecls;
    }

    public RecordVarDecl getRecordVarDecl() {
        return RecordVarDecl;
    }

    public void setRecordVarDecl(RecordVarDecl RecordVarDecl) {
        this.RecordVarDecl=RecordVarDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(RecordVarDecls!=null) RecordVarDecls.accept(visitor);
        if(RecordVarDecl!=null) RecordVarDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(RecordVarDecls!=null) RecordVarDecls.traverseTopDown(visitor);
        if(RecordVarDecl!=null) RecordVarDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(RecordVarDecls!=null) RecordVarDecls.traverseBottomUp(visitor);
        if(RecordVarDecl!=null) RecordVarDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("RecordVariableDecl(\n");

        if(RecordVarDecls!=null)
            buffer.append(RecordVarDecls.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(RecordVarDecl!=null)
            buffer.append(RecordVarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [RecordVariableDecl]");
        return buffer.toString();
    }
}
