// generated with ast extension for cup
// version 0.8
// 16/1/2022 1:43:21


package rs.ac.bg.etf.pp1.ast;

public class RecordDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private RecordName RecordName;
    private RecordVarDecls RecordVarDecls;

    public RecordDecl (RecordName RecordName, RecordVarDecls RecordVarDecls) {
        this.RecordName=RecordName;
        if(RecordName!=null) RecordName.setParent(this);
        this.RecordVarDecls=RecordVarDecls;
        if(RecordVarDecls!=null) RecordVarDecls.setParent(this);
    }

    public RecordName getRecordName() {
        return RecordName;
    }

    public void setRecordName(RecordName RecordName) {
        this.RecordName=RecordName;
    }

    public RecordVarDecls getRecordVarDecls() {
        return RecordVarDecls;
    }

    public void setRecordVarDecls(RecordVarDecls RecordVarDecls) {
        this.RecordVarDecls=RecordVarDecls;
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
        if(RecordName!=null) RecordName.accept(visitor);
        if(RecordVarDecls!=null) RecordVarDecls.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(RecordName!=null) RecordName.traverseTopDown(visitor);
        if(RecordVarDecls!=null) RecordVarDecls.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(RecordName!=null) RecordName.traverseBottomUp(visitor);
        if(RecordVarDecls!=null) RecordVarDecls.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("RecordDecl(\n");

        if(RecordName!=null)
            buffer.append(RecordName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(RecordVarDecls!=null)
            buffer.append(RecordVarDecls.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [RecordDecl]");
        return buffer.toString();
    }
}
