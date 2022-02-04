// generated with ast extension for cup
// version 0.8
// 4/1/2022 11:43:14


package rs.ac.bg.etf.pp1.ast;

public class DesignatorBracket extends Designator {

    private Designator Designator;
    private DesignatorLBracket DesignatorLBracket;
    private Expr Expr;

    public DesignatorBracket (Designator Designator, DesignatorLBracket DesignatorLBracket, Expr Expr) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.DesignatorLBracket=DesignatorLBracket;
        if(DesignatorLBracket!=null) DesignatorLBracket.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public DesignatorLBracket getDesignatorLBracket() {
        return DesignatorLBracket;
    }

    public void setDesignatorLBracket(DesignatorLBracket DesignatorLBracket) {
        this.DesignatorLBracket=DesignatorLBracket;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(DesignatorLBracket!=null) DesignatorLBracket.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(DesignatorLBracket!=null) DesignatorLBracket.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(DesignatorLBracket!=null) DesignatorLBracket.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorBracket(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorLBracket!=null)
            buffer.append(DesignatorLBracket.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorBracket]");
        return buffer.toString();
    }
}
