import java.util.ArrayList;

public  class Instruction {
//can add type of instruction 
    int value; // the int value that represents the 32 bits of the instruction after converting from binary to int
    int timeInStage;
    boolean branch;
    public Instruction(int value) {
        this.value = value;
        this.timeInStage = 0;
        branch=false;
    }
    
    public int getopcode() { 
        return value >>> 28;
    }
    public int getRegister(int index,Computer computer) { 
        return computer.registerFile[index];
    }
    public void setRegister(int index, int value,Computer computer) { 
        if(index != 0) // to prevent writing into the ZERO register
            computer.registerFile[index] = value;
    }

    public void execute_in_DECODE_stage(Computer computer)throws ComputerException{
        int r1,r2,r3,shamt,imm,add;
    	int opcode=computer.Instruction_in_Decode_Stage.value>>>28;
    	
    	computer.execute_Stage_Inputs[0]= opcode;
    	switch(opcode) {
            case 0: case 1: case 2: case 5:case 8:case 9:
            r1= ((value<<4)>>>27);
            r2=(value<<9)>>>27;
            r3=(value<<14)>>>27;
            shamt=(value<<19)>>>19;
            
            computer.execute_Stage_Inputs[1]= r1;
            computer.execute_Stage_Inputs[2]=r2 ;
            computer.execute_Stage_Inputs[3]= r3;
            computer.execute_Stage_Inputs[4]=shamt;
            break;
            
            case 3: case 4: case 6: case 10 : case 11:
            	r1= ((value<<4)>>>27);
            	r2=(value<<9)>>>27;
            	imm=(value<<14)>>14;
            	 
                 computer.execute_Stage_Inputs[1]= r1;
                 computer.execute_Stage_Inputs[2]=r2 ;
                 computer.execute_Stage_Inputs[3]= imm;
                 computer.execute_Stage_Inputs[4]=computer.PC;
                 break;
                 
            
            case 7: 
            	add=(value<<4)>>>4;
            //	System.out.println(add);
                computer.execute_Stage_Inputs[1]= add;
                computer.execute_Stage_Inputs[2]=computer.PC; 
                break;
            
           
            default: throw new ComputerException("opcode for instruction " + value + " is not valid");
        }
    }
    public void execute_in_EXECUTE_stage(Computer computer)throws ComputerException{
    	int res;
    	computer.memory_Stage_Inputs[0]=computer.execute_Stage_Inputs[0];
    	switch(computer.execute_Stage_Inputs[0]) {
        
    	case 0: 
        res=getRegister(computer.execute_Stage_Inputs[2], computer) + getRegister(computer.execute_Stage_Inputs[3], computer);
        computer.memory_Stage_Inputs[1]=computer.execute_Stage_Inputs[1];
        computer.memory_Stage_Inputs[2]=res;
        break;
        
        case 1: 
        res=getRegister(computer.execute_Stage_Inputs[2], computer) -getRegister(computer.execute_Stage_Inputs[3], computer);
        computer.memory_Stage_Inputs[1]=computer.execute_Stage_Inputs[1];
        computer.memory_Stage_Inputs[2]=res;
        break;
        
        case 2: 
        res=getRegister(computer.execute_Stage_Inputs[2], computer) * getRegister(computer.execute_Stage_Inputs[3], computer);
        computer.memory_Stage_Inputs[1]=computer.execute_Stage_Inputs[1];
        computer.memory_Stage_Inputs[2]=res;
        break;
        
        case 3: 
        res=computer.execute_Stage_Inputs[3];
        computer.memory_Stage_Inputs[1]=computer.execute_Stage_Inputs[1];
        computer.memory_Stage_Inputs[2]=res;

        break;
        
        case 4:
        if(getRegister(computer.execute_Stage_Inputs[1], computer)==getRegister(computer.execute_Stage_Inputs[2], computer)) {
        	computer.oldvalue=computer.execute_Stage_Inputs[4];
        	computer.PC=computer.execute_Stage_Inputs[4]+computer.execute_Stage_Inputs[3];
        	branch=true;
            //computer.fetchWaitTime=2;

        	//computer.Instruction_in_Fetch_Stage=null;
            //computer.Instruction_in_Decode_Stage=null;
        }
        ////////jjjjjjjjjjjjjjjjjjjjjjj
        	
        	 break;
        case 5:
        res=getRegister(computer.execute_Stage_Inputs[2], computer) & getRegister(computer.execute_Stage_Inputs[3], computer);
        computer.memory_Stage_Inputs[1]=computer.execute_Stage_Inputs[1];
        computer.memory_Stage_Inputs[2]=res;
        break;
        
        case 6: res=getRegister(computer.execute_Stage_Inputs[2], computer)^computer.execute_Stage_Inputs[3];
        computer.memory_Stage_Inputs[1]=computer.execute_Stage_Inputs[1];
        computer.memory_Stage_Inputs[2]=res;
        break;
        
        case 7: 
        computer.oldvalue=computer.execute_Stage_Inputs[2];
        computer.PC=(((computer.execute_Stage_Inputs[2]-1)>>>28)<<28)+computer.execute_Stage_Inputs[1]; 
      //  System.out.println("address = " + computer.execute_Stage_Inputs[1]);
       // System.out.println("pc (after subtracting 1) = " + (computer.execute_Stage_Inputs[2]-1));
       // System.out.println("pc after applying jump logic = " + computer.PC);
       // computer.Instruction_in_Fetch_Stage=null;
       // computer.Instruction_in_Decode_Stage=null;
        
         break;
        
        case 8: 
        	res=getRegister(computer.execute_Stage_Inputs[2], computer)<<computer.execute_Stage_Inputs[4];
        	
        	//System.out.println("ex "+res);
        	computer.memory_Stage_Inputs[1]=computer.execute_Stage_Inputs[1];
            computer.memory_Stage_Inputs[2]=res;
         //   System.out.println(computer.memory_Stage_Inputs[2]);
            break;
        
        case 9: 
        	res=getRegister(computer.execute_Stage_Inputs[2], computer)>>>computer.execute_Stage_Inputs[4];
        	computer.memory_Stage_Inputs[1]=computer.execute_Stage_Inputs[1];
            computer.memory_Stage_Inputs[2]=res;
            break;
        
        case 10: 
        	res=getRegister(computer.execute_Stage_Inputs[2], computer)+computer.execute_Stage_Inputs[3];
        	computer.memory_Stage_Inputs[1]=computer.execute_Stage_Inputs[1];
            computer.memory_Stage_Inputs[2]=res; 
        	break;
        
        case 11: 
        	res=getRegister(computer.execute_Stage_Inputs[2], computer)+computer.execute_Stage_Inputs[3];
        	computer.memory_Stage_Inputs[1]=getRegister(computer.execute_Stage_Inputs[1], computer);
            computer.memory_Stage_Inputs[2]=res;
        	break;
        default: throw new ComputerException("opcode for instruction " + value + " is not valid");
    }
    }
   
    public void execute_in_MEMORY_stage(Computer computer)throws ComputerException{
       computer.writeBack_Stage_Inputs[0]=computer.memory_Stage_Inputs[0];
    	switch(computer.writeBack_Stage_Inputs[0]) {
            case 0:   case 1:   case 2:   case 3:    case 4:  case 5: case 6:case 7: case 8:  case 9: 
            	computer.writeBack_Stage_Inputs[1]=computer.memory_Stage_Inputs[1];
              //  System.out.println("cccc"+computer.memory_Stage_Inputs[2]);

            	computer.writeBack_Stage_Inputs[2]=computer.memory_Stage_Inputs[2];
            	//System.out.println("mem  "+computer.writeBack_Stage_Inputs[2]);
            	break;
            case 10:
            	computer.writeBack_Stage_Inputs[1]=computer.memory_Stage_Inputs[1];
            	computer.writeBack_Stage_Inputs[2]=computer.memory[computer.memory_Stage_Inputs[2]];
            	
            	
            	break;
            case 11:
            	computer.memory[computer.memory_Stage_Inputs[2]]=computer.memory_Stage_Inputs[1];
            	
            	break;
            default: throw new ComputerException("opcode for instruction " + value + " is not valid");
        }
    }
    public void execute_in_WRITEBACK_stage(Computer computer)throws ComputerException{
        switch(computer.writeBack_Stage_Inputs[0]) {
            case 0:  case 1: case 2:case 3:case 5:case 6:case 8:case 9:case 10: 
            	computer.registerFile[computer.writeBack_Stage_Inputs[1]]=computer.writeBack_Stage_Inputs[2]; 
            //	System.out.println("wb   "+computer.writeBack_Stage_Inputs[1]);
            	//System.out.println(computer.writeBack_Stage_Inputs[2]);

            	
            	break;
           
            case 4:case 7: case 11:  break;
            
           
             
            default: throw new ComputerException("opcode for instruction " + value + " is not valid");
        }
    }
public static void main(String[] args) throws ComputerException {
	

    //  String s1="movi R19 66";
	// System.out.println(s1.toUpperCase().split(" ",2)[0]);
     //String s="jmp 16";
     String s="JMP 0";
	 Computer c = new Computer();
     c.trans(s);
     int jumpTranslation = c.instrans;
     Instruction i = new Instruction(jumpTranslation);
     System.out.println("opcode = " + i.getopcode());

    //  System.out.println("translation in decimal = " + jumpTranslation);
    //  String binaryRepresntion = Integer.toBinaryString(jumpTranslation);
    //  System.out.println("translation in binary: " + binaryRepresntion);
    //  System.out.println("length of binary representation = " + binaryRepresntion.length()); 


    //  String result = Long.toBinaryString( Integer.toUnsignedLong(c.instrans) | 0x100000000L ).substring(1);
    //  System.out.println(result);
     /*    
   
     c.registerFile[27]=70;
 	 c.registerFile[12]=1900;
 	 c.memory[2000]=78;
 	c.Instruction_in_Decode_Stage=new Instruction(c.instrans);
 	(c.Instruction_in_Decode_Stage).execute_in_DECODE_stage(c);
 	(c.Instruction_in_Decode_Stage).execute_in_EXECUTE_stage(c);
 	(c.Instruction_in_Decode_Stage).execute_in_MEMORY_stage(c);
 	(c.Instruction_in_Decode_Stage).execute_in_WRITEBACK_stage(c);
 	System.out.println(c.registerFile[27]);*/
 //	System.out.println(-200>>>16);
 //	System.out.println(-200<<16);
     
}

public String getType() throws ComputerException {
    switch(getopcode())
    {
        case 0 : case 1 : case 2 : case 5 : case 8 : case 9 : return "R";
        case 3 : case 4 : case 6 : case 10 : case 11 : return "I";
        case 7 : return "J";
        default: throw new ComputerException("opcode for instruction " + value + " is not valid");
    }
}

public String toString() {
    //return value + "";
    try 
    {
        return value + " (" + getAssemblyFormat() + ")";
    } 
    catch (ComputerException e)
    {
        e.printStackTrace();
    }
    return null;
}

 public String getAssemblyFormat() throws ComputerException {
   ArrayList<String> instructionParts = new ArrayList<String>();
   switch(getopcode())
   {
    case 0 : instructionParts.add("ADD"); break;
    case 1 : instructionParts.add("SUB"); break;
    case 2 : instructionParts.add("MUL"); break;
    case 3 : instructionParts.add("MOVI"); break;
    case 4 : instructionParts.add("JEQ"); break;
    case 5 : instructionParts.add("AND"); break;
    case 6 : instructionParts.add("XORI"); break;
    case 7 : instructionParts.add("JMP"); break;
    case 8 : instructionParts.add("LSL"); break;
    case 9 : instructionParts.add("LSR"); break;
    case 10 : instructionParts.add("MOVR"); break;
    case 11 : instructionParts.add("MOVM"); break;
    default: throw new ComputerException("opcode for instruction " + value + " is not valid");
   }
   switch(getopcode())
   {
    case 0 : case 1 : case 2 : case 5 :
        instructionParts.add("R" + ((value<<4)>>>27));
        instructionParts.add("R" + ((value<<9)>>>27));
        instructionParts.add("R" + ((value<<14)>>>27));
        break;
    case 8 : case 9 :
        instructionParts.add("R" + ((value<<4)>>>27));
        instructionParts.add("R" + ((value<<9)>>>27));
        instructionParts.add("" + ((value<<19)>>>19));
        break;  
    case 3 :
        instructionParts.add("R" + ((value<<4)>>>27));
        instructionParts.add("" + ((value<<14)>>14));
        break;
    case 4 : case 6 : case 10 : case 11 :
        instructionParts.add("R" + ((value<<4)>>>27));
        instructionParts.add("R" + ((value<<9)>>>27));
        instructionParts.add("" + ((value<<14)>>14));
        break;
    case 7 : 
        instructionParts.add("" + ((value<<4)>>>4));
        break;
    default : throw new ComputerException("opcode for instruction " + value + " is not valid");
    }
    String instruction = "";
    for(int i = 0 ; i < instructionParts.size()-1 ; i++)
        instruction += instructionParts.get(i) + " ";   
    instruction += instructionParts.get(instructionParts.size()-1);
    return instruction;
 }

}
