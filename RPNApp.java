package week09;

import java.util.*;

/**Runs a RPN calculator. 
  *@author Ashley Midgley.
  */
public class RPNApp{

    /**Stack of type long, used as main stack.*/
    public static Stack<Long> stack = new Stack<Long>();
    /**Boolean specifies whether main method is finished or not.*/
    public static boolean finished = false;
    /**Scanner to read user input. */
    public static Scanner scan = new Scanner(System.in);
    /**String that is the next line to scan and read.*/
    public static String line = scan.nextLine();
    /**StringTokenizer breaks up given string into tokens.*/
    public static StringTokenizer st = new StringTokenizer(line);
    /**dup is a variable used in duplicate to store a dpulicated number.*/
    public static long dup;
    /**Stack that is used to process lines with parentheses.*/
    public static Stack<String> parentheses = new Stack<String>();
    /**beforeParentheses is the number directly before the first parentheses.*/
    public static long beforeParentheses;
    /**sizeWithoutBrackets is the size of the stack without parentheses.*/
    public static int sizeWithoutBrackets;
    /**boolean used to check if an open bracket has been encountered.*/
    public static boolean bracketOpen = false;
    /**boolean used to check if EmptyStackException has been thrown/caught.*/
    public static boolean stackEmpty = false;
    /**Specific value used in roll method.*/
    public static final int ADD = 3;

    /**Main method that runs the RPN calculator.
     *@param args the command line arguments.
     */
    public static void main(String[]args){
        try{
            while(!finished){
                st = new StringTokenizer(line);
                runLine();
                printStack(stack);
                stack.clear();
                line = scan.nextLine();
            }
        }catch(NoSuchElementException e){
        }
    }

    /**Prepares line to process and processes line.*/
    public static void runLine(){
        while (st.hasMoreElements()){
            String nextstr = st.nextElement().toString();
            process(nextstr);
        }
    }

    /**Process each element of the line, check for specific operands or pushes
     *long values into stack.
     *@param s is the element converted to string that we process.
     **/
    public static void process(String s){
        try{
            if(bracketOpen &&(!s.equals(")"))){
                parentheses.push(s);
            }else if(s.equals(")")){
                parenthesesFinish();
            }else{
                switch(s){
                    case("q"): finished=true;
                    break;
                    case("+"): sAdd();
                    break;
                    case("-"): sSubtract();
                    break;
                    case("*"): sMultiply();
                    break;
                    case("/"): sDivide();
                    break;
                    case("%"): sMod();
                    break;
                    case("+!"): repeatAdd();
                    break;
                    case("-!"): repeatSubtract();
                    break;
                    case("*!"): repeatMultiply();
                    break;
                    case("/!"): repeatDivide();
                    break;
                    case("%!"): repeatMod();
                    break;
                    case("d"): duplicate();
                    break;
                    case("o"): outputTop();
                    break;
                    case("c"): copy();
                    break;
                    case("r"): roll();
                    break;
                    case("("): parenthesesStart();
                    break;
                    case(")"): parenthesesFinish();
                    break;
                    default: stack.push(Long.parseLong(s));
                    break;
                }
            }
        }catch(NumberFormatException e){
            System.out.println("Error: bad token "+"'"+s+"'");
            stack.clear();
        }catch(EmptyStackException e){
            if(!stackEmpty){
                System.out.println("Error: too few operands");
            }
        }catch(ArithmeticException e){
            System.out.println("Error: division by 0");
        }
    }

    /**Prints the stack out for the user.
     *@param input is a Stack of Long values that will be printed.
     */
    public static void printStack(Stack<Long> input){
        try{
            if(bracketOpen){
                throw new IllegalArgumentException();
            }else if(!stack.isEmpty()){
                System.out.println(input);
            }
        }catch( IllegalArgumentException e){
            System.out.println("Error: unmatched parentheses");
            System.out.println("Error: too few operands");
        }
    }
    
    /**add method for stack elements.*/
    public static void sAdd(){
        stack.push(stack.pop()+stack.pop());
    }

    /**subtract method for stack elements.*/
    public static void sSubtract(){
        Long i = stack.pop();
        stack.push(stack.pop()-i);
    }

    /**multiply method for stack elements.*/
    public static void sMultiply(){
        stack.push(stack.pop()*stack.pop());
    }

    /**divide method for stack elements.*/
    public static void sDivide(){
        Long i = stack.pop();
        stack.push(stack.pop()/i);
    }

    /**modulus method for stack elements.*/
    public static void sMod(){
        Long i = stack.pop();
        stack.push(stack.pop()%i);
    }

    /**repeat add method for stack elements.*/
    public static void repeatAdd(){
        int i = 0;
        while(i<stack.size()-1){
            sAdd();
        }
    }

    /**repeat subtract method for stack elements.*/
    public static void repeatSubtract(){
        int i = 0;
        while(i<stack.size()-1){
            sSubtract();
        }
        return;
    }

    /**repeat multiply method for stack elements.*/
    public static void repeatMultiply(){
        int i = 0;
        while(i<stack.size()-1){
            sMultiply();
        }
    }

    /**repeat divide method for stack elements.*/
    public static void repeatDivide(){
        int i = 0;
        while(i<stack.size()-1){
            sDivide();
        }
    }

    /**repeat modulus method for stack elements.*/
    public static void repeatMod(){
        int i = 0;
        while(i<stack.size()-1){
            sMod();
        }
    }

    /**duplicate method for stack elements.
     *duplicates top item of stack.
     */
    public static void duplicate(){
        dup = stack.peek();
        stack.push(dup);
    }

    /**outputs the top item of the stack without removing it and single space.*/
    public static void outputTop(){
        System.out.print(stack.peek() +" ");
    }

    /**copies the second from top item the amount of times specified at top
     *of stack.
     */
    public static void copy(){
        try{
            long times = stack.pop();
            long a = stack.pop();
            if(times<0){
                throw new IllegalArgumentException();
            }
            for(int i = 0; i < times; i++){
                stack.push(a);
            }
            if(times==0){
                System.out.println(stack);
            }
            return;
        }catch(EmptyStackException e){
            System.out.println("Too few operands");
        }catch(IllegalArgumentException e){
            System.out.println("Error: negative copy");
        }
    }
    
    /**rolls the top element down k-1 positions.*/
    public static void roll(){
        long k = stack.pop();
        long [] arr = new long[stack.size()];
        for(int i=0; i < stack.size()+ADD;i++){
            long l = stack.pop();
            arr[i] = l;
        }
        long move = k-1;
        int next = 1;
        for(int i=0;i < move; i++){
            if(i < arr.length-1){
                long a = arr[i];
                long b = arr[i+next];
                arr[i] = b;
                arr[i+next] = a;
            }
        }
        for(int i = arr.length-1; i >= 0; i--){
            stack.push(arr[i]);
        }
    }

    /**Sets up the beginning of the parentheses operation for processing.*/
    public static void parenthesesStart(){
        bracketOpen = true;
        beforeParentheses = stack.peek();
        sizeWithoutBrackets = stack.size();
    }

    /**Finishes the parentheses operation. Performs parentheses arithmetic.*/
    public static void parenthesesFinish(){
        try{
            if(!bracketOpen){
                throw new IllegalArgumentException();
            }
            repeatInput(beforeParentheses);
            stack.pop();
            String stackstr = "";
            String temp = "";
            while(!stack.isEmpty()){
                long l = stack.pop();
                if(l!=beforeParentheses){
                    temp += " " + String.valueOf(l);
                }
            }
            stackstr += reverseString(temp);
        
            String result = stackstr + repeatInput(beforeParentheses);
        
            bracketOpen = false;
            st = new StringTokenizer(result);
            runLine();
            parentheses.clear();
            return;
        }catch(IllegalArgumentException e){
            stack.clear();
            stackEmpty=true;
        }
    }

    /**Preparing the string for the repeat arithmetic for parenthesesFinish()
     *method.
     @param input is the elements we need to repeat for parenthesesFinish().
     @return str is the repeated input.
    */
    public static String repeatInput(long input){
        String str = "";
        int i = 0;
        while(i < input){
            for(String s : parentheses){
                str += s+" ";
            }
            i++;
        }
        return str;    
    }

    /**reverses the string output given from parenthesesFinish() method.
     *@param input is the input from parenthesesFinish() method.
     *@return returns the string the correct way to be pushed into stack.
     */
    public static String reverseString(String input){
        String reverse = "";
        int length = input.length();
        for( int i = length - 1 ; i >= 0 ; i-- ) {
            reverse = reverse + input.charAt(i);
        }
        return reverse;
    }
    
}