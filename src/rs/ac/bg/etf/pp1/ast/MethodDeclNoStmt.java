// generated with ast extension for cup
// version 0.8
// 6/0/2022 18:54:18


package rs.ac.bg.etf.pp1.ast;

public class MethodDeclNoStmt extends MethodDecl {

    private MethodReturnValue MethodReturnValue;
    private MethodName MethodName;
    private MethodFormPars MethodFormPars;
    private MultipleVarDecl MultipleVarDecl;

    public MethodDeclNoStmt (MethodReturnValue MethodReturnValue, MethodName MethodName, MethodFormPars MethodFormPars, MultipleVarDecl MultipleVarDecl) {
        this.MethodReturnValue=MethodReturnValue;
        if(MethodReturnValue!=null) MethodReturnValue.setParent(this);
        this.MethodName=MethodName;
        if(MethodName!=null) MethodName.setParent(this);
        this.MethodFormPars=MethodFormPars;
        if(MethodFormPars!=null) MethodFormPars.setParent(this);
        this.MultipleVarDecl=MultipleVarDecl;
        if(MultipleVarDecl!=null) MultipleVarDecl.setParent(this);
    }

    public MethodReturnValue getMethodReturnValue() {
        return MethodReturnValue;
    }

    public void setMethodReturnValue(MethodReturnValue MethodReturnValue) {
        this.MethodReturnValue=MethodReturnValue;
    }

    public MethodName getMethodName() {
        return MethodName;
    }

    public void setMethodName(MethodName MethodName) {
        this.MethodName=MethodName;
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

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MethodReturnValue!=null) MethodReturnValue.accept(visitor);
        if(MethodName!=null) MethodName.accept(visitor);
        if(MethodFormPars!=null) MethodFormPars.accept(visitor);
        if(MultipleVarDecl!=null) MultipleVarDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodReturnValue!=null) MethodReturnValue.traverseTopDown(visitor);
        if(MethodName!=null) MethodName.traverseTopDown(visitor);
        if(MethodFormPars!=null) MethodFormPars.traverseTopDown(visitor);
        if(MultipleVarDecl!=null) MultipleVarDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodReturnValue!=null) MethodReturnValue.traverseBottomUp(visitor);
        if(MethodName!=null) MethodName.traverseBottomUp(visitor);
        if(MethodFormPars!=null) MethodFormPars.traverseBottomUp(visitor);
        if(MultipleVarDecl!=null) MultipleVarDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodDeclNoStmt(\n");

        if(MethodReturnValue!=null)
            buffer.append(MethodReturnValue.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodName!=null)
            buffer.append(MethodName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
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

        buffer.append(tab);
        buffer.append(") [MethodDeclNoStmt]");
        return buffer.toString();
    }
}
