// generated with ast extension for cup
// version 0.8
// 16/1/2022 1:43:21


package rs.ac.bg.etf.pp1.ast;

public class DoStmt extends SingleStatement {

    private DoStart DoStart;
    private Statement Statement;
    private WhileStart WhileStart;
    private DoCondition DoCondition;

    public DoStmt (DoStart DoStart, Statement Statement, WhileStart WhileStart, DoCondition DoCondition) {
        this.DoStart=DoStart;
        if(DoStart!=null) DoStart.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.WhileStart=WhileStart;
        if(WhileStart!=null) WhileStart.setParent(this);
        this.DoCondition=DoCondition;
        if(DoCondition!=null) DoCondition.setParent(this);
    }

    public DoStart getDoStart() {
        return DoStart;
    }

    public void setDoStart(DoStart DoStart) {
        this.DoStart=DoStart;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public WhileStart getWhileStart() {
        return WhileStart;
    }

    public void setWhileStart(WhileStart WhileStart) {
        this.WhileStart=WhileStart;
    }

    public DoCondition getDoCondition() {
        return DoCondition;
    }

    public void setDoCondition(DoCondition DoCondition) {
        this.DoCondition=DoCondition;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DoStart!=null) DoStart.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(WhileStart!=null) WhileStart.accept(visitor);
        if(DoCondition!=null) DoCondition.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DoStart!=null) DoStart.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(WhileStart!=null) WhileStart.traverseTopDown(visitor);
        if(DoCondition!=null) DoCondition.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DoStart!=null) DoStart.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(WhileStart!=null) WhileStart.traverseBottomUp(visitor);
        if(DoCondition!=null) DoCondition.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DoStmt(\n");

        if(DoStart!=null)
            buffer.append(DoStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(WhileStart!=null)
            buffer.append(WhileStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DoCondition!=null)
            buffer.append(DoCondition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DoStmt]");
        return buffer.toString();
    }
}
