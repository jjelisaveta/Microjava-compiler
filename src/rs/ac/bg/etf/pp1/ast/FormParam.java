// generated with ast extension for cup
// version 0.8
// 7/0/2022 19:17:20


package rs.ac.bg.etf.pp1.ast;

public class FormParam extends FormPars {

    private OneFormPar OneFormPar;

    public FormParam (OneFormPar OneFormPar) {
        this.OneFormPar=OneFormPar;
        if(OneFormPar!=null) OneFormPar.setParent(this);
    }

    public OneFormPar getOneFormPar() {
        return OneFormPar;
    }

    public void setOneFormPar(OneFormPar OneFormPar) {
        this.OneFormPar=OneFormPar;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(OneFormPar!=null) OneFormPar.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OneFormPar!=null) OneFormPar.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OneFormPar!=null) OneFormPar.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParam(\n");

        if(OneFormPar!=null)
            buffer.append(OneFormPar.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParam]");
        return buffer.toString();
    }
}
