package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.mj.runtime.Run;

public class CodeGenerator extends VisitorAdaptor {
	

	private int mainPc;
	private List<Obj> constants = new ArrayList<>();
	
	Logger log = Logger.getLogger(getClass());
	
	public int getMainPc(){
		return mainPc;
	}
	
	public void visit(ProgName progName) {
		Code.dataSize = progName.obj.getLocalSymbols().size();
	}
	
	public void visit(ConstantDecl constantDecl) {
		Obj obj = Tab.find(constantDecl.getConstName());
		constants.add(obj);
	}
	
	public void visit(ConstantDecls constantDecls) {
		Obj obj = Tab.find(constantDecls.getConstName());
		constants.add(obj);
	}
	
	public void visit(ConstDecl constDecl) {
		while (!constants.isEmpty()) {
			Obj constObj = constants.remove(0);
			Code.load(constObj);
		}
	}
	
	public void visit(MethodName methodName){
		
		if("main".equalsIgnoreCase(methodName.getMethodName())){
			mainPc = Code.pc;
		}
		methodName.obj.setAdr(Code.pc);

		// Generate the entry
		Code.put(Code.enter);
		Code.put(methodName.obj.getLevel());
		Code.put(methodName.obj.getLocalSymbols().size());
	}
	
	public void visit(MethodDeclStmt methodDecl) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(MethodDeclNoStmt methodDecl) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(FactorNoParen designator) {
		Code.load(designator.obj);
	}
	
	/*public void visit(DesignatorIdent designator) {
		SyntaxNode parent = designator.getParent();
		log.info(parent.getClass().getName() +" " + designator.obj.getName());
		Code.load(designator.obj);
		
	}*/
	
	
	public void visit(AssignStmt assignStmt) {
		Code.store(assignStmt.getDesignator().obj);
	}
	
	public void visit(Terms terms) {
		Mulop mulop = terms.getMulop();

		if (mulop instanceof MulOpMul) {
			Code.put(Code.mul);
			return;
		}

		if (mulop instanceof MulOpDiv) {
			Code.put(Code.div);
			return;
		}

		if (mulop instanceof MulOpMod) {
			Code.put(Code.rem);
			return;
		}
	}
	
	public void visit(AddOpsList addOpsList) {
		Addop addop = addOpsList.getAddop();

		if (addop instanceof AddOpPlus) {
			Code.put(Code.add);
			return;
		}

		if (addop instanceof AddOpMinus) {
			Code.put(Code.sub);
			return;
		}
	}
	
	public void visit(OneAddop oneAddop) {
		Addop addop = oneAddop.getAddop();

		if (addop instanceof AddOpPlus) {
			Code.put(Code.add);
			return;
		}

		if (addop instanceof AddOpMinus) {
			Code.put(Code.sub);
			return;
		}
	}
	
	public void visit(PrintStmt printStmt) {
		//Code.load(printStmt.getExpr().obj);
	    //Code.load(new Obj(Obj.Con, "width", Tab.intType, 1, 0));
		if(printStmt.getExpr().obj.getType() == Tab.intType){
			Code.loadConst(5);
			Code.put(Code.print);
			log.info("ovde");
		}else{
			Code.loadConst(1);
			Code.put(Code.bprint);
		}
	}
	
	public void visit(NumConst numConst){
		Code.load(numConst.obj);
	}
	
	public void visit(CharConst charConst){
		Code.load(charConst.obj);
	}
	
	public void visit(BoolConst boolConst){
		Code.load(boolConst.obj);
	}
	
}
