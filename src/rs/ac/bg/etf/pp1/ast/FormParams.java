// generated with ast extension for cup
// version 0.8
// 16/0/2022 23:10:24


package rs.ac.bg.etf.pp1.ast;

public class FormParams extends FormPars {

    private FormPars FormPars;
    private OneFormPar OneFormPar;

    public FormParams (FormPars FormPars, OneFormPar OneFormPar) {
        this.FormPars=FormPars;
        if(FormPars!=null) FormPars.setParent(this);
        this.OneFormPar=OneFormPar;
        if(OneFormPar!=null) OneFormPar.setParent(this);
    }

    public FormPars getFormPars() {
        return FormPars;
    }

    public void setFormPars(FormPars FormPars) {
        this.FormPars=FormPars;
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
        if(FormPars!=null) FormPars.accept(visitor);
        if(OneFormPar!=null) OneFormPar.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormPars!=null) FormPars.traverseTopDown(visitor);
        if(OneFormPar!=null) OneFormPar.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormPars!=null) FormPars.traverseBottomUp(visitor);
        if(OneFormPar!=null) OneFormPar.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParams(\n");

        if(FormPars!=null)
            buffer.append(FormPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OneFormPar!=null)
            buffer.append(OneFormPar.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParams]");
        return buffer.toString();
    }
}
