// generated with ast extension for cup
// version 0.8
// 6/0/2022 18:54:18


package rs.ac.bg.etf.pp1.ast;

public class RecordVarDecl extends RecordVarDecls {

    private RecordVarDecls RecordVarDecls;
    private VarDecl VarDecl;

    public RecordVarDecl (RecordVarDecls RecordVarDecls, VarDecl VarDecl) {
        this.RecordVarDecls=RecordVarDecls;
        if(RecordVarDecls!=null) RecordVarDecls.setParent(this);
        this.VarDecl=VarDecl;
        if(VarDecl!=null) VarDecl.setParent(this);
    }

    public RecordVarDecls getRecordVarDecls() {
        return RecordVarDecls;
    }

    public void setRecordVarDecls(RecordVarDecls RecordVarDecls) {
        this.RecordVarDecls=RecordVarDecls;
    }

    public VarDecl getVarDecl() {
        return VarDecl;
    }

    public void setVarDecl(VarDecl VarDecl) {
        this.VarDecl=VarDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(RecordVarDecls!=null) RecordVarDecls.accept(visitor);
        if(VarDecl!=null) VarDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(RecordVarDecls!=null) RecordVarDecls.traverseTopDown(visitor);
        if(VarDecl!=null) VarDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(RecordVarDecls!=null) RecordVarDecls.traverseBottomUp(visitor);
        if(VarDecl!=null) VarDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("RecordVarDecl(\n");

        if(RecordVarDecls!=null)
            buffer.append(RecordVarDecls.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDecl!=null)
            buffer.append(VarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [RecordVarDecl]");
        return buffer.toString();
    }
}
