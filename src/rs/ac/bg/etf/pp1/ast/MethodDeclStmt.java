// generated with ast extension for cup
// version 0.8
// 5/0/2022 20:57:9


package rs.ac.bg.etf.pp1.ast;

public class MethodDeclStmt extends MethodDecl {

    private MethodReturnValue MethodReturnValue;
    private String I2;
    private MethodFormPars MethodFormPars;
    private MultipleVarDecl MultipleVarDecl;
    private StatementList StatementList;

    public MethodDeclStmt (MethodReturnValue MethodReturnValue, String I2, MethodFormPars MethodFormPars, MultipleVarDecl MultipleVarDecl, StatementList StatementList) {
        this.MethodReturnValue=MethodReturnValue;
        if(MethodReturnValue!=null) MethodReturnValue.setParent(this);
        this.I2=I2;
        this.MethodFormPars=MethodFormPars;
        if(MethodFormPars!=null) MethodFormPars.setParent(this);
        this.MultipleVarDecl=MultipleVarDecl;
        if(MultipleVarDecl!=null) MultipleVarDecl.setParent(this);
        this.StatementList=StatementList;
        if(StatementList!=null) StatementList.setParent(this);
    }

    public MethodReturnValue getMethodReturnValue() {
        return MethodReturnValue;
    }

    public void setMethodReturnValue(MethodReturnValue MethodReturnValue) {
        this.MethodReturnValue=MethodReturnValue;
    }

    public String getI2() {
        return I2;
    }

    public void setI2(String I2) {
        this.I2=I2;
    }

    public MethodFormPars getMethodFormPars() {
        return MethodFormPars;
    }

    public void setMethodFormPars(MethodFormPars MethodFormPars) {
        this.MethodFormPars=MethodFormPars;
    }

    public MultipleVarDecl getMultipleVarDecl() {
        return MultipleVarDecl;
    }

    public void setMultipleVarDecl(MultipleVarDecl MultipleVarDecl) {
        this.MultipleVarDecl=MultipleVarDecl;
    }

    public StatementList getStatementList() {
        return StatementList;
    }

    public void setStatementList(StatementList StatementList) {
        this.StatementList=StatementList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MethodReturnValue!=null) MethodReturnValue.accept(visitor);
        if(MethodFormPars!=null) MethodFormPars.accept(visitor);
        if(MultipleVarDecl!=null) MultipleVarDecl.accept(visitor);
        if(StatementList!=null) StatementList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodReturnValue!=null) MethodReturnValue.traverseTopDown(visitor);
        if(MethodFormPars!=null) MethodFormPars.traverseTopDown(visitor);
        if(MultipleVarDecl!=null) MultipleVarDecl.traverseTopDown(visitor);
        if(StatementList!=null) StatementList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodReturnValue!=null) MethodReturnValue.traverseBottomUp(visitor);
        if(MethodFormPars!=null) MethodFormPars.traverseBottomUp(visitor);
        if(MultipleVarDecl!=null) MultipleVarDecl.traverseBottomUp(visitor);
        if(StatementList!=null) StatementList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodDeclStmt(\n");

        if(MethodReturnValue!=null)
            buffer.append(MethodReturnValue.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+I2);
        buffer.append("\n");

        if(MethodFormPars!=null)
            buffer.append(MethodFormPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MultipleVarDecl!=null)
            buffer.append(MultipleVarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(StatementList!=null)
            buffer.append(StatementList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodDeclStmt]");
        return buffer.toString();
    }
}
