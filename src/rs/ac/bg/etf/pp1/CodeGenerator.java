package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.mj.runtime.Run;

public class CodeGenerator extends VisitorAdaptor {

	private int mainPc;
	private List<Obj> constants = new ArrayList<>();

	Logger log = Logger.getLogger(getClass());

	public int getMainPc() {
		return mainPc;
	}
	
	public void visit(Program program) {
	
	}

	/* constants */
	public void visit(ConstantDecl constantDecl) {
		Obj obj = Tab.find(constantDecl.getConstName());
		Code.load(obj);
	}

	public void visit(ConstantDecls constantDecls) {
		Obj obj = Tab.find(constantDecls.getConstName());
		Code.load(obj);
	}

	/* method declarations */
	public void visit(MethodName methodName) {
		if ("main".equalsIgnoreCase(methodName.getMethodName())) {
			mainPc = Code.pc;
		}
		methodName.obj.setAdr(Code.pc);
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

	/* desginators */
	public void visit(DesignatorLBracket designatorLBracket) {
		Obj arrayObj = ((DesignatorBracket) designatorLBracket.getParent()).getDesignator().obj;
		Code.load(arrayObj);
		//prvo se ucitava ovo pa onda Expr u zagradi - ne mora dup
	}
	
	public void visit(DesignatorBracket designatorBracket) {
		//designator je ucitan kod zagrade, a expr se ucitava posle toga, a pre desginatorBracket
	}

	public void visit(DesignatorPoint designatorPoint) {
		Code.load(designatorPoint.getDesignator().obj);		
	}
	
	public void visit(DesignatorIdent designatorIdent) {
		//ne mora jer se poziva FactorNoParen
	}
	
	public void visit(FactorNoParen designator) {
		Code.load(designator.obj);
		if (designator.obj.getType().getKind() == Struct.Array) {
			Code.put(Code.dup_x1); 
			Code.put(Code.pop);
		}	
	}

	public void visit(FactorParen factorParen) {
		Obj obj = factorParen.obj;
		int addrOffset = obj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(addrOffset);
	}

	/* designator statement */
	public void visit(AssignStmt assignStmt) {
		
		Obj designatorObj = assignStmt.getDesignator().obj;
		Code.store(designatorObj);
	}
	
	public void visit(DesignatorStmtFuncCall designatorStmtFuncCall) {
		Obj obj = designatorStmtFuncCall.obj;
		int addrOffset = obj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(addrOffset);
	}

	public void visit(DesignatorStmtInc designatorStmtInc) {
		//stek: adresa, index
		if (designatorStmtInc.getDesignator().obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}
		Code.load(designatorStmtInc.getDesignator().obj);
		
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(designatorStmtInc.getDesignator().obj);
	}
	
	public void visit(DesignatorStmtDec designatorStmtDec) {
		if (designatorStmtDec.getDesignator().obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}
		Code.load(designatorStmtDec.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(designatorStmtDec.getDesignator().obj);
	}
	
	public void visit(FactorType factorType) {
		//Code.put(Code.pop);
		Code.put(Code.new_);
		int size = 4 * factorType.obj.getType().getMembers().size();
		Code.put2(size);
	}
	
	public void visit(FactorArrayType factorArrayType) {
		Code.put(Code.newarray);
		if (factorArrayType.getType().struct.getKind() == Struct.Char) {
			Code.put(1);
		} else {
			Code.put(0);
		}
	}
	
	/* relational operators */ 
	public void visit(CondFactExprRelop condFactExprRelop) {
		/*Relop relop = condFactExprRelop.getRelop();
		if (relop instanceof RelOpEq) {
			Code.put(Code.eq);
		} else if (relop instanceof RelOpNe) {
			Code.put(Code.ne);
		} else if (relop instanceof RelOpGt) {
			Code.put(Code.gt);
		} else if (relop instanceof RelOpGe) {
			Code.put(Code.ge);
		} else if (relop instanceof RelOpLt) {
			Code.put(Code.lt);
		} else if (relop instanceof RelOpLe) {
			Code.put(Code.le);
		}*/
	}

	
	/* arithmetic operators */
	public void visit(Terms terms) {
		Mulop mulop = terms.getMulop();

		if (mulop instanceof MulOpMul) {
			Code.put(Code.mul);
		} else if (mulop instanceof MulOpDiv) {
			Code.put(Code.div);
		} else if (mulop instanceof MulOpMod) {
			Code.put(Code.rem);
		}
	}


	public void visit(AddOpsList addOpsList) {
		Addop addop = addOpsList.getAddop();

		if (addop instanceof AddOpPlus) {
			Code.put(Code.add);
		} else if (addop instanceof AddOpMinus) {
			Code.put(Code.sub);
		}
	}

	public void visit(OneAddop oneAddop) {
		Addop addop = oneAddop.getAddop();

		if (addop instanceof AddOpPlus) {
			Code.put(Code.add);
		} else if (addop instanceof AddOpMinus) {
			Code.put(Code.sub);
		}
	}
	
	public void visit(MinusTerm minusTerm) {
		Code.loadConst(-1);
		Code.put(Code.mul);
	}

	/* type constants */
	public void visit(FactorTypeConst typeConst) {
		Code.load(typeConst.obj);
	}

	/* statements */
	public void visit(ReturnStmtExpr returnStmtExpr) {
		Code.put(Code.exit);
		Code.put(Code.return_);
		
	}

	public void visit(ReturnStmt returnStmt) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	public void visit(PrintStmt printStmt) {
		if (printStmt.getExpr().obj.getType() == Tab.intType
				|| printStmt.getExpr().obj.getType().getKind() == Struct.Bool) {
			Code.loadConst(5);
			Code.put(Code.print);
		} else {
			Code.loadConst(1);
			Code.put(Code.bprint);
		}
	}

	public void visit(PrintStmtNum printStmtNum) {
		if (printStmtNum.getExpr().obj.getType() == Tab.intType
				|| printStmtNum.getExpr().obj.getType().getKind() == Struct.Bool) {
			Code.loadConst(printStmtNum.getNumConst());
			Code.put(Code.print);
		} else {
			Code.loadConst(1);
			Code.put(Code.bprint);
		}
	}
	
	public void visit(ReadStmt readStmt) {
		if (readStmt.getDesignator().obj.getType() == Tab.intType
				|| readStmt.getDesignator().obj.getType().getKind() == Struct.Bool) {
			Code.put(Code.read);
		} else {
			Code.put(Code.bread);
		}
	}

}
